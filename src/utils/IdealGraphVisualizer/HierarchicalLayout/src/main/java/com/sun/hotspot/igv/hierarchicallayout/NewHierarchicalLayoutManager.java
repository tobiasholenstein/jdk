/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

package com.sun.hotspot.igv.hierarchicallayout;

import com.sun.hotspot.igv.layout.LayoutGraph;
import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Vertex;
import java.awt.Dimension;
import java.awt.Point;
import java.util.*;


public class NewHierarchicalLayoutManager {

    public static final int SWEEP_ITERATIONS = 1;
    public static final int CROSSING_ITERATIONS = 2;
    public static final int DUMMY_HEIGHT = 1;
    public static final int DUMMY_WIDTH = 1;
    public static final int X_OFFSET = 8;
    public static final int LAYER_OFFSET = 8;
    public final int OFFSET = X_OFFSET + DUMMY_WIDTH;
    public static final double SCALE_LAYER_PADDING = 1.5;


    // Options
    private final int maxLayerLength;
    // Algorithm global datastructures
    private final List<LayoutNode> allNodes;
    private final HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private final Set<Link> reversedLinks;
    private final HashMap<Link, List<Point>> reversedLinkStartPoints;
    private final HashMap<Link, List<Point>> reversedLinkEndPoints;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;


    private void assertLinks() {
        for (LayoutNode node : allNodes) {
            if (!node.isDummy()) {
                for (LayoutEdge predEdge : node.preds) {
                    assert predEdge.link != null;
                }
                for (LayoutEdge succEdge : node.succs) {
                    if (succEdge.to.isDummy()) {
                        assert !succEdge.to.succs.isEmpty() || succEdge.link != null;
                    } else {
                        assert succEdge.link != null;
                    }
                }
            }
        }
    }

    private void assertNodeSynced() {
        for (LayoutLayer layer : layers) {
            for (LayoutNode node : layer) {
                assert allNodes.contains(node);
            }
        }

        for (LayoutNode node : allNodes) {
            boolean found = false;
            for (LayoutLayer layer : layers) {
                if (layer.contains(node)) {
                    found = true;
                    break;
                }
            }
            assert found;
        }

        for (Vertex vertex : graph.getVertices()) {
            LayoutNode node = vertexToLayoutNode.get(vertex);
            assert allNodes.contains(node);
        }
    }

    private void assertLayerNr() {
        for (int l = 0; l < layers.length; ++l) {
            LayoutLayer layer = layers[l];
            for (LayoutNode node : layer) {
                assert node.layer == l;
            }
        }
    }


    private void assertOrder() {
        for (int l = 0; l < layers.length; ++l) {
            LayoutLayer layer = layers[l];
            for (int pos = 1; pos < layer.size(); ++pos) {
                LayoutNode leftNode = layer.get(pos-1);
                LayoutNode rightNode = layer.get(pos);
                assert leftNode.pos + 1 == rightNode.pos;
                //assert leftNode.x <= rightNode.x;
            }
            for (LayoutNode node : layer) {
                assert node.layer >= 0;
                assert node.layer < layers.length;
                assert node.pos >= 0;
                assert node.pos < layer.size();
                assert node.layer == l;
                assert node.pos < layers[node.layer].size();
            }
        }
        assertNodeSynced();
        for (LayoutNode node : allNodes) {
            assert node.layer >= 0;
            assert node.layer < layers.length;
            assert node.pos >= 0;
            assert node.pos < layers[node.layer].size();
        }

        assertNodeSynced();
    }

    private void addNode(LayoutNode node) {
        LayoutLayer layer = layers[node.layer];
        int pos = node.pos;

        // update pos of the nodes right (and including) of pos
        for (LayoutNode n : layer) {
            if (n.pos >= pos) {
                n.pos += 1;
            }
        }

        // insert in layer at pos
        if (pos < layer.size()) {
            layer.add(pos, node);
        } else {
            layer.add(node);
        }

        // update x of the nodes right of inserted node at pos
        int prevRightSide = node.getRightBorder();
        for (LayoutNode n : layer) {
            if (n.pos > pos) {
                n.x = Math.max(n.x, prevRightSide + X_OFFSET);
                prevRightSide = n.getRightBorder();
            }
        }
        // adjust Y of movedNode
        node.y = layer.y;

        allNodes.add(node);
        assertOrder();
    }

    // remove a node : do not update x, but assert that pos withing layer is correct
    private void removeNode(LayoutNode node) {
        int layer = node.layer;
        layers[layer].remove(node);
        updateLayerPositions(layer);
        // Remove node from graph layout
        allNodes.remove(node);
    }

    private void applyRemoveLinkAction(Link l) {
        Vertex from = l.getFrom().getVertex();
        Vertex to = l.getTo().getVertex();
        LayoutNode toNode = vertexToLayoutNode.get(to);
        LayoutNode fromNode = vertexToLayoutNode.get(from);

        if (toNode.layer < fromNode.layer) {
            // Reversed edge
            toNode = fromNode;
            reversedLinks.remove(l);
            reversedLinkEndPoints.remove(l);
            reversedLinkStartPoints.remove(l);
        }

        // Remove preds-edges bottom up, starting at "to" node
        // Cannot start from "from" node since there might be joint edges
        List<LayoutEdge> toNodePredsEdges = List.copyOf(toNode.preds);
        for (LayoutEdge edge : toNodePredsEdges) {
            LayoutNode n = edge.from;
            LayoutEdge edgeToRemove;

            if (edge.link != null && edge.link.equals(l)) {
                toNode.preds.remove(edge);
                edgeToRemove = edge;
            } else {
                // Wrong edge, look at next
                continue;
            }

            if (n.vertex != null && n.vertex.equals(from)) {
                // No dummy nodes inbetween 'from' and 'to' vertex
                n.succs.remove(edgeToRemove);
                break;
            } else {
                // Must remove edges between dummy nodes
                boolean found = true;
                LayoutNode prev = toNode;
                while (n.vertex == null && found) {
                    found = false;

                    if (n.succs.size() <= 1 && n.preds.size() <= 1) {
                        // Dummy node used only for this link, remove if not already removed
                        if (allNodes.contains(n)) {
                            removeNode(n);
                        }
                    } else {
                        // anchor node, should not be removed
                        break;
                    }

                    if (n.preds.size() == 1) {
                        n.succs.remove(edgeToRemove);
                        prev = n;
                        edgeToRemove = n.preds.get(0);
                        n = edgeToRemove.from;
                        found = true;
                    }
                }

                n.succs.remove(edgeToRemove);
                prev.preds.remove(edgeToRemove);
            }
            break;
        }

        // ensure neighbor edge consistency
        for (LayoutNode n : allNodes) {
            for (LayoutEdge e : n.succs) {
                assert allNodes.contains(e.from);
                assert allNodes.contains(e.to);
            }
            for (LayoutEdge e : n.preds) {
                assert allNodes.contains(e.from);
                assert allNodes.contains(e.to);
            }
        }

    }

    private boolean tryMoveNodeInSamePosition(LayoutNode node, int x, int layerNr) {
        LayoutLayer layer = layers[layerNr];
        assert layer.contains(node);
        int leftBound = Integer.MIN_VALUE;
        int rightBound = Integer.MAX_VALUE;
        if (node.pos > 0) {
            LayoutNode leftNode = layer.get(node.pos-1);
            leftBound = leftNode.getRightBorder();
        }
        if (node.pos < layer.size()-1) {
            LayoutNode rightNode = layer.get(node.pos+1);
            rightBound = rightNode.getLeftBorder();
        }

        // the node did not change position withing the layer
        if (leftBound < x && x < rightBound) {
            x = Math.max(x, leftBound + X_OFFSET);
            x = Math.min(x, rightBound - X_OFFSET - node.getWholeWidth());
            // same layer and position, just adjust x pos
            node.x = x;
            return true;
        }
        return false;
    }

    public void removeEdges(LayoutNode movedNode) {
        for (Link inputLink : graph.getInputLinks(movedNode.vertex)) {
            if (inputLink.getFrom().getVertex() == inputLink.getTo().getVertex()) continue;
            applyRemoveLinkAction(inputLink);
        }
        for (Link outputLink : graph.getOutputLinks(movedNode.vertex)) {
            if (outputLink.getFrom().getVertex() == outputLink.getTo().getVertex()) continue;
            applyRemoveLinkAction(outputLink);
        }

        // remove link connected to movedNode
        for (Link link : graph.getLinks()) {
            if (link.getTo().getVertex() == movedNode.vertex) {
                link.setControlPoints(new ArrayList<>());
                reversedLinks.remove(link);
                reversedLinkStartPoints.remove(link);
            } else if (link.getFrom().getVertex() == movedNode.vertex) {
                link.setControlPoints(new ArrayList<>());
                reversedLinks.remove(link);
                reversedLinkEndPoints.remove(link);
            }
        }
        movedNode.height = movedNode.vertex.getSize().height;
        movedNode.width = movedNode.vertex.getSize().width;

        assert movedNode.succs.isEmpty();
        assert movedNode.preds.isEmpty();
        for (Link link : reversedLinks) {
            assert link.getFrom().getVertex() != movedNode.vertex;
            assert link.getTo().getVertex() != movedNode.vertex;
            assert vertexToLayoutNode.get(link.getFrom().getVertex()) != movedNode;
            assert vertexToLayoutNode.get(link.getTo().getVertex()) != movedNode;
        }
    }

    public void addEdges(LayoutNode movedNode) {

        // BuildDatastructure
        List<Link> nodeLinks = new ArrayList<>(graph.getInputLinks(movedNode.vertex));
        nodeLinks.addAll(graph.getOutputLinks(movedNode.vertex));
        nodeLinks.sort(LINK_COMPARATOR);

        Set<LayoutNode> reversedLayoutNodes = new HashSet<>();
        assertOrder();
        for (Link link : nodeLinks) {
            if (link.getFrom().getVertex() == link.getTo().getVertex()) continue;

            assertOrder();
            LayoutEdge layoutEdge = createLayoutEdge(link);
            assertOrder();

            LayoutNode fromNode = layoutEdge.from;
            LayoutNode toNode = layoutEdge.to;

            assert fromNode.layer  != toNode.layer;
            if (fromNode.layer > toNode.layer) {
                assertOrder();
                reverseEdge(layoutEdge);
                assertOrder();

                //updateNodeWithReversedEdges(fromNode);
                //updateNodeWithReversedEdges(toNode);

                reversedLayoutNodes.add(fromNode);
                reversedLayoutNodes.add(toNode);
            }
        }
        assertOrder();


        // ReverseEdges
        for (LayoutNode layoutNode : reversedLayoutNodes) {
            computeReversedLinkPoints(layoutNode);
        }
        assertOrder();


        // CreateDummyNodes
        createDummiesForNode(movedNode);

        assertNodeSynced();
        assertLayerNr();

        updatePositions(); // TODO: find optimal positions

        assertNodeSynced();
        assertLayerNr();
        assertOrder();


        //addEdges(movedNode);
        assertOrder();
    }

    /**
     * Find the optimal position within the given layer to insert the given node.
     * The optimum is given by the least amount of edge crossings.
     */
    private int optimalPosition(LayoutNode node, int layer) {
        assert 0 <= layer && layer < layers.length;

        layers[layer].sort(Comparator.comparingInt(n -> n.pos));
        int edgeCrossings = Integer.MAX_VALUE;
        int optimalPos = -1;

        // Try each possible position in the layer
        for (int i = 0; i < layers[layer].size() + 1; i++) {
            int xCoord;
            if (i == 0) {
                xCoord = layers[layer].get(i).x - node.width - 1;
            } else {
                xCoord = layers[layer].get(i - 1).x + layers[layer].get(i - 1).width + 1;
            }

            int currentCrossings = 0;

            if (0 <= layer - 1) {
                // For each link with an end point in vertex, check how many edges cross it
                for (LayoutEdge edge : node.preds) {
                    if (edge.from.layer == layer - 1) {
                        int fromNodeXCoord = edge.from.x;
                        if (edge.from.vertex != null) {
                            fromNodeXCoord += edge.relativeFrom;
                        }
                        int toNodeXCoord = xCoord;
                        if (node.vertex != null) {
                            toNodeXCoord += edge.relativeTo;
                        }
                        for (LayoutNode n : layers[layer - 1]) {
                            for (LayoutEdge e : n.succs) {
                                if (e.to == null) {
                                    continue;
                                }
                                int compFromXCoord = e.from.x;
                                if (e.from.vertex != null) {
                                    compFromXCoord += e.relativeFrom;
                                }
                                int compToXCoord = e.to.x;
                                if (e.to.vertex != null) {
                                    compToXCoord += e.relativeTo;
                                }
                                if ((fromNodeXCoord > compFromXCoord && toNodeXCoord < compToXCoord)
                                        || (fromNodeXCoord < compFromXCoord
                                        && toNodeXCoord > compToXCoord)) {
                                    currentCrossings += 1;
                                }
                            }
                        }
                    }
                }
            }
            // Edge crossings across current layer and layer below
            if (layer + 1 < layers.length) {
                // For each link with an end point in vertex, check how many edges cross it
                for (LayoutEdge edge : node.succs) {
                    if (edge.to.layer == layer + 1) {
                        int toNodeXCoord = edge.to.x;
                        if (edge.to.vertex != null) {
                            toNodeXCoord += edge.relativeTo;
                        }
                        int fromNodeXCoord = xCoord;
                        if (node.vertex != null) {
                            fromNodeXCoord += edge.relativeFrom;
                        }
                        for (LayoutNode n : layers[layer + 1]) {
                            for (LayoutEdge e : n.preds) {
                                if (e.from == null) {
                                    continue;
                                }
                                int compFromXCoord = e.from.x;
                                if (e.from.vertex != null) {
                                    compFromXCoord += e.relativeFrom;
                                }
                                int compToXCoord = e.to.x;
                                if (e.to.vertex != null) {
                                    compToXCoord += e.relativeTo;
                                }
                                if ((fromNodeXCoord > compFromXCoord && toNodeXCoord < compToXCoord)
                                        || (fromNodeXCoord < compFromXCoord
                                        && toNodeXCoord > compToXCoord)) {
                                    currentCrossings += 1;
                                }
                            }
                        }
                    }
                }
            }
            if (currentCrossings <= edgeCrossings) {
                edgeCrossings = currentCrossings;
                optimalPos = i;
            }
        }
        assert optimalPos != -1;
        return optimalPos;
    }

    private void updateNodeWithReversedEdges(LayoutNode node) {
        // Reset node data in case there were previous reversed edges
        node.width = (int) node.vertex.getSize().getWidth();
        node.height = (int) node.vertex.getSize().getHeight();
        node.topYOffset = 0;
        node.bottomYOffset = 0;
        node.leftXOffset = 0;
        node.rightXOffset = 0;

        int reversedDown = 0;
        // Reset relativeFrom for all succ edges
        for (LayoutEdge e : node.succs) {
            if (e.link == null) {
                continue;
            }
            e.relativeFrom = e.link.getFrom().getRelativePosition().x;
            if (reversedLinks.contains(e.link)) {
                e.relativeFrom = e.link.getTo().getRelativePosition().x;
                ++reversedDown;
            }
        }


        int reversedUp = 0;
        // Reset relativeTo for all pred edges
        for (LayoutEdge e : node.preds) {
            if (e.link == null) {
                continue;
            }
            e.relativeTo = e.link.getTo().getRelativePosition().x;
            if (reversedLinks.contains(e.link)) {
                e.relativeTo = e.link.getFrom().getRelativePosition().x;
                ++reversedUp;
            }
        }

        final int offset = X_OFFSET + DUMMY_WIDTH;

        int widthFactor = reversedDown;
        node.width += widthFactor * offset;

        int minX = 0;
        if (reversedDown > 0) {
            minX = -offset * reversedUp;
        }

        if (minX < 0) {
            for (LayoutEdge e : node.preds) {
                e.relativeTo -= minX;
            }

            for (LayoutEdge e : node.succs) {
                e.relativeFrom -= minX;
            }

            node.leftXOffset = -minX;
            node.width -= minX;
        }
    }


    private int findLayer(int y) {
        int optimalLayer = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int l = 0; l < layerCount; l++) {
            int layerY = layers[l].getCenter();
            int distance = Math.abs(layerY-y);
            if (distance < minDistance) {
                minDistance = distance;
                optimalLayer = l;
            }
        }
        assert 0 <= optimalLayer : "did not find a layer to place";
        return optimalLayer;
    }

    private int findPosInLayer(int x, int layerNr) {
        LayoutLayer layer = layers[layerNr];

        // find the position in the new layer at location
        int newPos = 0;
        for (int j = 1; j < layer.size(); j++) {
            LayoutNode leftNode = layer.get(j-1);
            LayoutNode rightNode = layer.get(j);
            if (x < leftNode.getRightSide()) {
                newPos = leftNode.pos;
                break;
            } else if (x < rightNode.getRightSide()) {
                newPos = rightNode.pos;
                break;
            } else {
                newPos = rightNode.pos + 1;
            }
        }
        return newPos;
    }

    public void moveNode(LayoutNode node, int newX, int newLayerNr) {
        int newPos = findPosInLayer(newX, newLayerNr);

        // remove from old layer and update positions in old layer
        removeNode(node);

        // set x of movedNode
        node.x = newX;

        if (node.layer != newLayerNr) { // insert into a different layer
            node.layer = newLayerNr;
            node.pos = newPos;
        } else { // move within the same layer
            //assert movedNode.pos != newPos; // handled before
            if (node.pos < newPos) { // moved to the right
                // adjust because we have already removed movedNode in this layer
                node.pos = newPos - 1;
            } else { // moved to the left
                node.pos = newPos;
            }
        }
        addNode(node);
    }

    // check that NO neighbors of node are in a given layer
    private boolean canMoveNodeToLayer(LayoutNode node, int layerNr) {
        for (Link inputLink : graph.getInputLinks(node.vertex)) {
            if (inputLink.getFrom().getVertex() == inputLink.getTo().getVertex()) continue;
            LayoutNode fromNode = vertexToLayoutNode.get(inputLink.getFrom().getVertex());
            if (fromNode.layer == layerNr) {
                return false;
            }
        }
        for (Link outputLink : graph.getOutputLinks(node.vertex)) {
            if (outputLink.getFrom().getVertex() == outputLink.getTo().getVertex()) continue;
            LayoutNode toNode = vertexToLayoutNode.get(outputLink.getTo().getVertex());
            if (toNode.layer == layerNr) {
                return false;
            }
        }
        return true;

    }

    public void assertPos() {
        for (LayoutLayer layer : layers) {
            for (LayoutNode node : layer) {
                assert node.layer >= 0;
                assert node.layer < layers.length;
                assert node.pos >= 0;
                assert node.pos < layer.size();
            }
        }
    }

    public void moveFigureTo(Vertex movedVertex, Point loc) {
        LayoutNode movedNode = vertexToLayoutNode.get(movedVertex);
        Point newLocation = new Point(loc.x, loc.y + movedNode.height/2);

        int newLayerNr = findLayer(newLocation.y);
        if (!canMoveNodeToLayer(movedNode, newLayerNr)) {
            return;
        }

        if (movedNode.layer == newLayerNr) { // we move the node in the same layer
            // the node did not change position withing the layer
            if (tryMoveNodeInSamePosition(movedNode, newLocation.x, newLayerNr)) {
                new WriteResult().run();
                return;
            }
            moveNode(movedNode, newLocation.x, movedNode.layer);
        } else { // only remove edges if we moved the node to a new layer
            assertOrder();
            removeEdges(movedNode);
            assertOrder();
            moveNode(movedNode, newLocation.x, newLayerNr);
            assertPos();
            assertOrder();
            addEdges(movedNode);
            assertPos();
        }

        assertOrder();
        new AssignXCoordinates().run();
        assertOrder();
        new AssignYCoordinates().run();
        assertOrder();
        new WriteResult().run();
        assertOrder();
    }

    private class LayoutNode {

        public int optimal_x;
        public int x;
        public int y;
        public int width;
        public int height;
        public int layer = -1;
        public int leftXOffset;
        public int topYOffset;
        public int rightXOffset;
        public int bottomYOffset;
        public final Vertex vertex; // Only used for non-dummy nodes, otherwise null

        public final List<LayoutEdge> preds = new ArrayList<>();
        public final List<LayoutEdge> succs = new ArrayList<>();
        public int pos = -1; // Position within layer

        public float crossingNumber = 0;

        public void loadCrossingNumber(boolean up) {
            crossingNumber = 0;
            int count = 0;
            if (up) {
                count = succs.stream().map((e) -> e.loadCrossingNumber(up, this)).reduce(count, Integer::sum);
            } else {
                count = preds.stream().map((e) -> e.loadCrossingNumber(up, this)).reduce(count, Integer::sum);
            }
            if (count > 0) {
                crossingNumber /= count;
            } else {
                crossingNumber = 0;
            }
        }

        public LayoutNode(Vertex v) {
            vertex = v;
            if (v == null) {
                height = DUMMY_HEIGHT;
                width = DUMMY_WIDTH;
            } else {
                Dimension size = v.getSize();
                height = size.height;
                width = size.width;
            }
        }

        public LayoutNode() {
            this(null);
        }

        public int getLeftSide() {
            return x + leftXOffset;
        }

        public int getLeftBorder() {
            return x;
        }

        public int getWholeWidth() {
            return leftXOffset + width + rightXOffset;
        }

        public int getWholeHeight() {
            return topYOffset + height + bottomYOffset;
        }

        public int getRightSide() {
            return x + leftXOffset + width;
        }

        public int getRightBorder() {
            return x + leftXOffset + width + rightXOffset;
        }

        public int getCenterX() {
            return getLeftSide() + (width / 2);
        }

        public int getTop() {
            return y + topYOffset;
        }

        public int getBottom() {
            return y + topYOffset + height;
        }

        public boolean isDummy() {
            return vertex == null;
        }

        @Override
        public String toString() {
            if (vertex != null) {
                return vertex.toString();
            } else {
                return "dummy";
            }
        }
    }

    private class LayoutEdge {

        public LayoutNode from;
        public LayoutNode to;
        public int relativeFrom;
        public int relativeTo;
        public Link link;

        public int loadCrossingNumber(boolean up, LayoutNode source) {
            int nr;
            if (up) {
                nr = getEndPoint();
            } else {
                nr = getStartPoint();
            }
            source.crossingNumber += nr;
            return 1;
        }

        public int getStartPoint() {
            return relativeFrom + from.getLeftSide();
        }

        public int getEndPoint() {
            return relativeTo + to.getLeftSide();
        }

        public LayoutEdge(LayoutNode from, LayoutNode to) {
            this.from = from;
            this.to = to;
        }

        public LayoutEdge(LayoutNode from, LayoutNode to, int relativeFrom, int relativeTo, Link link) {
            this(from, to);
            this.relativeFrom = relativeFrom;
            this.relativeTo = relativeTo;
            this.link = link;
        }
    }

    public NewHierarchicalLayoutManager() {
        maxLayerLength = -1;

        vertexToLayoutNode = new HashMap<>();
        reversedLinks = new HashSet<>();
        reversedLinkStartPoints = new HashMap<>();
        reversedLinkEndPoints = new HashMap<>();
        allNodes = new ArrayList<>();
    }

    public void doLayout(LayoutGraph graph) {
        this.graph = graph;

        // #############################################################
        // Step 1: Build up data structure
        // creates:
        // resets vertexToLayoutNode.clear();
        //        nodes.clear();
        //  - LayoutNodes : nodes, vertexToLayoutNode
        //  - LayoutEdge :  LayoutNode.preds, LayoutNode.succs
        new BuildDatastructure().run();
        assertNodePos();

        // #############################################################
        // STEP 2: Reverse edges, handle backedges
        // resets reversedLinks
        // resets reversedLinkStartPoints
        // resets reversedLinkEndPoints
        // - LayoutEdge e : reverse backedges
        // - add Link : reversedLinks.add(e.link);
        // - set reversedLinkEndPoints.put(link, endpoints);
        // - set reversedLinkStartPoints.put(link, start-points);
        // - LayoutNode n with reversed edges:
        //      - n.yOffset
        //      - n.xOffset
        //      - n.width
        new ReverseEdges().run();
        assertNodePos();

        // #############################################################
        // STEP 3: Assign layers
        // creates layers = new LayoutLayer[layerCount];
        // - sets LayoutNode.layer and inserts in layers[n.layer]
        new AssignLayers().run();
        assertNodePos();
        assertEdgesConnected();
        //assertLayerNr();

        // #############################################################
        // STEP 4: Create dummy nodes
        // - takes List<LayoutNode> nodes
        // - replaces single LayoutEdge between LayoutNodes with a chain of new LayoutEdges and dummy LayoutNodes
        // - adds the new dummy LayoutNodes to nodes
        new CreateDummyNodes().run();
        assertLayerNr();
        assertNodePos();
        assertEdgesConnected();
        assertNodeSynced();

        // #############################################################
        // STEP 5: Crossing Reduction
        // - sets for each LayoutNode n.pos
        new CrossingReduction().run();
        assertNodePos();
        assertOrder();

        // #############################################################
        // STEP 6: Assign X coordinates
        // for all LayoutNode n in nodes
        // - sets n.x
        new AssignXCoordinates().run();
        assertOrder();

        // #############################################################
        // STEP 7: Assign Y coordinates
        // for LayoutLayer layer: layer.y, layer.height
        // for each LayoutNode n
        //  - node.y
        //  - node.topYOffset
        //  - node.bottomYOffset
        new AssignYCoordinates().run();
        assertOrder();

        // #############################################################
        // STEP 8: Write back to interface
        new WriteResult().run();
        assertOrder();
    }

    public LayoutEdge createLayoutEdge(Link link ) {
        LayoutEdge edge = new LayoutEdge(
                vertexToLayoutNode.get(link.getFrom().getVertex()),
                vertexToLayoutNode.get(link.getTo().getVertex()),
                link.getFrom().getRelativePosition().x,
                link.getTo().getRelativePosition().x,
                link);
        edge.from.succs.add(edge);
        edge.to.preds.add(edge);
        return edge;
    }

    public static final Comparator<Link> LINK_COMPARATOR =
            Comparator.comparing((Link l) -> l.getFrom().getVertex())
                    .thenComparing(l -> l.getTo().getVertex())
                    .thenComparingInt(l -> l.getFrom().getRelativePosition().x)
                    .thenComparingInt(l -> l.getTo().getRelativePosition().x);

    private class BuildDatastructure {

        private void run() {
            // cleanup
            vertexToLayoutNode.clear();
            allNodes.clear();

            // Set up nodes
            for (Vertex v : graph.getVertices()) {
                LayoutNode node = new LayoutNode(v);
                allNodes.add(node);
                vertexToLayoutNode.put(v, node);
            }

            // Set up edges
            List<Link> links = new ArrayList<>(graph.getLinks());
            links.sort(LINK_COMPARATOR);
            for (Link link : links) {
                createLayoutEdge(link);
            }
        }
    }

    private void reverseEdge(LayoutEdge layoutEdge) {
        assert !reversedLinks.contains(layoutEdge.link);
        reversedLinks.add(layoutEdge.link);

        LayoutNode oldFrom = layoutEdge.from;
        LayoutNode oldTo = layoutEdge.to;
        int oldRelativeFrom = layoutEdge.relativeFrom;
        int oldRelativeTo = layoutEdge.relativeTo;

        layoutEdge.from = oldTo;
        layoutEdge.to = oldFrom;
        layoutEdge.relativeFrom = oldRelativeTo;
        layoutEdge.relativeTo = oldRelativeFrom;

        oldFrom.succs.remove(layoutEdge);
        oldFrom.preds.add(layoutEdge);
        oldTo.preds.remove(layoutEdge);
        oldTo.succs.add(layoutEdge);
    }

    private boolean computeReversedStartPoints(LayoutNode node, boolean left) {
        TreeMap<Integer, ArrayList<LayoutEdge>> sortedDownMap = left ? new TreeMap<>() : new TreeMap<>(Collections.reverseOrder());
        for (LayoutEdge succEdge : node.succs) {
            if (reversedLinks.contains(succEdge.link)) {
                sortedDownMap.putIfAbsent(succEdge.relativeFrom, new ArrayList<>());
                sortedDownMap.get(succEdge.relativeFrom).add(succEdge);
            }
        }

        int offsetX = left ? -OFFSET : OFFSET;
        int currentX = left ? 0 : node.width;
        int startY = 0;
        int currentY = 0;
        for (Map.Entry<Integer, ArrayList<LayoutEdge>> entry : sortedDownMap.entrySet()) {
            int startX = entry.getKey();
            ArrayList<LayoutEdge> reversedSuccs = entry.getValue();

            currentX += offsetX;
            currentY -= OFFSET;
            node.topYOffset += OFFSET;

            for (LayoutEdge succEdge : reversedSuccs) {
                succEdge.relativeFrom = currentX;
            }

            ArrayList<Point> startPoints = new ArrayList<>();
            startPoints.add(new Point(currentX, startY));
            startPoints.add(new Point(currentX, currentY));
            startPoints.add(new Point(startX, currentY));
            startPoints.add(new Point(startX, startY));
            for (LayoutEdge revEdge : reversedSuccs) {
                reversedLinkStartPoints.put(revEdge.link, startPoints);
            }
        }
        node.leftXOffset += left ? sortedDownMap.size() * OFFSET : 0;
        node.rightXOffset += left ? 0 : sortedDownMap.size() * OFFSET;

        return !sortedDownMap.isEmpty();
    }

    private boolean computeReversedEndPoints(LayoutNode node, boolean left) {
        TreeMap<Integer, ArrayList<LayoutEdge>> sortedUpMap = left ? new TreeMap<>() : new TreeMap<>(Collections.reverseOrder());
        for (LayoutEdge predEdge : node.preds) {
            if (reversedLinks.contains(predEdge.link)) {
                sortedUpMap.putIfAbsent(predEdge.relativeTo, new ArrayList<>());
                sortedUpMap.get(predEdge.relativeTo).add(predEdge);
            }
        }

        int offsetX = left ? -OFFSET : OFFSET;
        int currentX = left ? 0 : node.width;
        int startY = node.height;
        int currentY = node.height;
        for (Map.Entry<Integer, ArrayList<LayoutEdge>> entry : sortedUpMap.entrySet()) {
            int startX = entry.getKey();
            ArrayList<LayoutEdge> reversedPreds = entry.getValue();

            currentX += offsetX;
            currentY += OFFSET;
            node.bottomYOffset += OFFSET;

            for (LayoutEdge predEdge : reversedPreds) {
                predEdge.relativeTo = currentX;
            }

            ArrayList<Point> endPoints = new ArrayList<>();
            endPoints.add(new Point(currentX, startY));
            endPoints.add(new Point(currentX, currentY));
            endPoints.add(new Point(startX, currentY));
            endPoints.add(new Point(startX, startY));
            for (LayoutEdge revEdge : reversedPreds) {
                reversedLinkEndPoints.put(revEdge.link, endPoints);
            }
        }
        node.leftXOffset += left ? sortedUpMap.size() * OFFSET : 0;
        node.rightXOffset += left ? 0 : sortedUpMap.size() * OFFSET;

        return !sortedUpMap.isEmpty();
    }


    private void computeReversedLinkPoints(LayoutNode node) {
        // reset node, except (x, y)
        node.width = node.vertex.getSize().width;
        node.height = node.vertex.getSize().height;
        node.topYOffset = 0;
        node.bottomYOffset = 0;
        node.leftXOffset = 0;
        node.rightXOffset = 0;

        boolean reverseLeft = false; // default is false
        boolean hasReversedDown = computeReversedStartPoints(node, reverseLeft);
        computeReversedEndPoints(node, hasReversedDown != reverseLeft);
    }

    private class ReverseEdges {

        private HashSet<LayoutNode> visited;
        private HashSet<LayoutNode> active;

        private void run() {
            reversedLinks.clear();
            reversedLinkStartPoints.clear();
            reversedLinkEndPoints.clear();

            // Remove self-edges
            for (LayoutNode node : allNodes) {
                ArrayList<LayoutEdge> succs = new ArrayList<>(node.succs);
                for (LayoutEdge e : succs) {
                    assert e.from == node;
                    if (e.to == node) {
                        node.succs.remove(e);
                        node.preds.remove(e);
                    }
                }
            }

            // Reverse inputs of roots
            for (LayoutNode node : allNodes) {
                if (node.vertex.isRoot()) {
                    for (LayoutEdge predEdge : new ArrayList<>(node.preds)) {
                        reverseEdge(predEdge);
                    }
                }
            }

            visited = new HashSet<>();
            active = new HashSet<>();

            // detect back-edges in control-flow
            controlFlowBackEdges();

            visited.clear();
            active.clear();

            // Start DFS and reverse back edges
            allNodes.forEach(this::DFS);
            allNodes.forEach(NewHierarchicalLayoutManager.this::computeReversedLinkPoints);
        }

        private void controlFlowBackEdges() {
            ArrayList<LayoutNode> workingList = new ArrayList<>();
            for (LayoutNode node : allNodes) {
                if (node.preds.isEmpty()) {
                    workingList.add(node);
                }
            }
            // detect back-edges in control-flow
            while (!workingList.isEmpty()) {
                ArrayList<LayoutNode> newWorkingList = new ArrayList<>();
                for (LayoutNode node : workingList) {
                    if (node.vertex.getPrority() < 4 && !node.vertex.isRoot()) {
                        continue;
                    }
                    visited.add(node);
                    ArrayList<LayoutEdge> succs = new ArrayList<>(node.succs);
                    for (LayoutEdge edge : succs) {
                        if (reversedLinks.contains(edge.link)) {
                            continue;
                        }

                        LayoutNode succNode = edge.to;
                        if (visited.contains(succNode)) {
                            // we found a back edge, reverse it
                            reverseEdge(edge);

                        } else {
                            newWorkingList.add(succNode);
                        }
                    }
                }
                workingList = newWorkingList;
            }

        }

        private void DFS(LayoutNode startNode) {
            if (visited.contains(startNode)) {
                return;
            }

            Stack<LayoutNode> workingList = new Stack<>();
            workingList.push(startNode);

            while (!workingList.empty()) {
                LayoutNode node = workingList.pop();

                if (visited.contains(node)) {
                    // Node no longer active
                    active.remove(node);
                    continue;
                }

                // Repush immediately to know when no longer active
                workingList.push(node);
                visited.add(node);
                active.add(node);

                ArrayList<LayoutEdge> succs = new ArrayList<>(node.succs);
                for (LayoutEdge e : succs) {
                    if (active.contains(e.to)) {
                        assert visited.contains(e.to);
                        // Encountered back edge
                        reverseEdge(e);
                    } else if (!visited.contains(e.to)) {
                        workingList.push(e.to);
                    }
                }
            }
        }

    }

    private class AssignLayers {

        private void run() {
            ArrayList<LayoutNode> workingList = new ArrayList<>();

            // add all root nodes to layer 0
            for (LayoutNode node : allNodes) {
                if (node.preds.isEmpty()) {
                    workingList.add(node);
                    node.layer = 0;
                }
            }

            // assign layers downwards starting from roots
            int layer = 1;
            while (!workingList.isEmpty()) {
                ArrayList<LayoutNode> newWorkingList = new ArrayList<>();
                // workingList.sort(Comparator.comparingInt(v -> v.vertex.getPrority()));
                for (LayoutNode node : workingList) {
                    for (LayoutEdge succEdge : node.succs) {
                        LayoutNode succNode = succEdge.to;
                        if (succNode.layer == -1) {
                            // This node was not assigned before.
                            boolean assignedPred = true;
                            for (LayoutEdge predEdge : succNode.preds) {
                                LayoutNode predNode = predEdge.from;
                                if (predNode.layer == -1 || predNode.layer >= layer) {
                                    // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                    assignedPred = false;
                                    break;
                                }
                            }
                            if (assignedPred) {
                                // This successor node can be assigned.
                                succNode.layer = layer;
                                newWorkingList.add(succNode);
                            }
                        }
                    }
                }
                workingList = newWorkingList;
                layer++;
            }

            // add all leaves to working list, reset layer of non-leave nodes
            for (LayoutNode node : allNodes) {
                if (node.succs.isEmpty()) {
                    node.layer = (layer - 2 - node.layer);
                    workingList.add(node);
                } else {
                    node.layer = -1;
                }
            }

            // assign layer upwards starting from leaves
            // sinks non-leave nodes down as much as possible
            layer = 1;
            while (!workingList.isEmpty()) {
                ArrayList<LayoutNode> newWorkingList = new ArrayList<>();
                for (LayoutNode node : workingList) {
                    if (node.layer < layer) {
                        for (LayoutEdge predEdge : node.preds) {
                            LayoutNode predNode = predEdge.from;
                            if (predNode.layer == -1) {
                                // This node was not assigned before.
                                boolean assignedSucc = true;
                                for (LayoutEdge succEdge : predNode.succs) {
                                    LayoutNode succNode = succEdge.to;
                                    if (succNode.layer == -1 || succNode.layer >= layer) {
                                        // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                        assignedSucc = false;
                                        break;
                                    }
                                }

                                if (assignedSucc) {
                                    // This predecessor node can be assigned.
                                    predNode.layer = layer;
                                    newWorkingList.add(predNode);
                                }
                            }
                        }
                    } else {
                        newWorkingList.add(node);
                    }
                }

                workingList = newWorkingList;
                layer++;
            }

            layerCount = layer - 1;
            for (LayoutNode n : allNodes) {
                n.layer = (layerCount - 1 - n.layer);
            }
        }
    }

    public void createDummiesForNode(LayoutNode layoutNode) {
        assert !layoutNode.isDummy();

        HashMap<Integer, List<LayoutEdge>> portsToUnprocessedEdges = new HashMap<>();
        ArrayList<LayoutEdge> succs = new ArrayList<>(layoutNode.succs);
        HashMap<Integer, LayoutNode> portToTopNode = new HashMap<>();
        HashMap<Integer, HashMap<Integer, LayoutNode>> portToBottomNodeMapping = new HashMap<>();
        for (LayoutEdge succEdge : succs) {
            int startPort = succEdge.relativeFrom;
            LayoutNode fromNode = succEdge.from;
            LayoutNode toNode = succEdge.to;
            assert fromNode.layer < toNode.layer;

            // edge is longer than one layer => needs dummy nodes
            if (fromNode.layer != toNode.layer - 1) {
                // the edge needs to be cut
                if (maxLayerLength != -1 && toNode.layer - fromNode.layer > maxLayerLength) {
                    assert maxLayerLength > 2;
                    // remove the succEdge before replacing it
                    toNode.preds.remove(succEdge);
                    fromNode.succs.remove(succEdge);

                    LayoutNode topCutNode = portToTopNode.get(startPort);
                    if (topCutNode == null) {
                        topCutNode = new LayoutNode();
                        topCutNode.layer = fromNode.layer + 1;
                        allNodes.add(topCutNode);
                        layers[topCutNode.layer].add(topCutNode);
                        portToTopNode.put(startPort, topCutNode);
                        portToBottomNodeMapping.put(startPort, new HashMap<>());
                    }
                    LayoutEdge edgeToTopCut = new LayoutEdge(fromNode, topCutNode, succEdge.relativeFrom, topCutNode.width / 2, succEdge.link);
                    fromNode.succs.add(edgeToTopCut);
                    topCutNode.preds.add(edgeToTopCut);
                    assert topCutNode.isDummy();

                    HashMap<Integer, LayoutNode> layerToBottomNode = portToBottomNodeMapping.get(startPort);
                    LayoutNode bottomCutNode = layerToBottomNode.get(toNode.layer);
                    if (bottomCutNode == null) {
                        bottomCutNode = new LayoutNode();
                        bottomCutNode.layer = toNode.layer - 1;
                        allNodes.add(bottomCutNode);
                        layers[bottomCutNode.layer].add(bottomCutNode);
                        layerToBottomNode.put(toNode.layer, bottomCutNode);
                    }
                    LayoutEdge bottomEdge = new LayoutEdge(bottomCutNode, toNode, bottomCutNode.width / 2, succEdge.relativeTo, succEdge.link);
                    toNode.preds.add(bottomEdge);
                    bottomCutNode.succs.add(bottomEdge);
                    assert bottomCutNode.isDummy();

                } else { // the edge is not cut, but needs dummy nodes
                    portsToUnprocessedEdges.putIfAbsent(startPort, new ArrayList<>());
                    portsToUnprocessedEdges.get(startPort).add(succEdge);
                }
            }
        }

        for (Map.Entry<Integer, List<LayoutEdge>> portToUnprocessedEdges : portsToUnprocessedEdges.entrySet()) {
            Integer startPort = portToUnprocessedEdges.getKey();
            List<LayoutEdge> unprocessedEdges = portToUnprocessedEdges.getValue();
            unprocessedEdges.sort(Comparator.comparingInt(e -> e.to.layer));

            if (unprocessedEdges.size() == 1) {
                // process a single edge
                LayoutEdge singleEdge = unprocessedEdges.get(0);
                assert singleEdge.link != null;
                LayoutNode fromNode = singleEdge.from;
                if (singleEdge.to.layer > fromNode.layer + 1) {
                    LayoutEdge previousEdge = singleEdge;
                    for (int i = fromNode.layer + 1; i < previousEdge.to.layer; i++) {
                        LayoutNode dummyNode = new LayoutNode();
                        dummyNode.layer = i;
                        dummyNode.preds.add(previousEdge);
                        allNodes.add(dummyNode);
                        layers[dummyNode.layer].add(dummyNode);
                        LayoutEdge dummyEdge = new LayoutEdge(dummyNode, previousEdge.to, dummyNode.width / 2, previousEdge.relativeTo, null);
                        dummyNode.succs.add(dummyEdge);
                        previousEdge.relativeTo = dummyNode.width / 2;
                        previousEdge.to.preds.remove(previousEdge);
                        previousEdge.to.preds.add(dummyEdge);
                        previousEdge.to = dummyNode;
                        previousEdge = dummyEdge;
                    }
                    previousEdge.link = singleEdge.link;
                    singleEdge.link = null;
                }
            } else {
                int lastLayer = unprocessedEdges.get(unprocessedEdges.size() - 1).to.layer;
                int dummyCnt = lastLayer - layoutNode.layer - 1;
                LayoutEdge[] dummyEdges = new LayoutEdge[dummyCnt];
                LayoutNode[] dummyNodes = new LayoutNode[dummyCnt];

                dummyNodes[0] = new LayoutNode();
                dummyNodes[0].layer = layoutNode.layer + 1;
                dummyEdges[0] = new LayoutEdge(layoutNode, dummyNodes[0], startPort, dummyNodes[0].width / 2, null);
                dummyNodes[0].preds.add(dummyEdges[0]);
                layoutNode.succs.add(dummyEdges[0]);
                for (int j = 1; j < dummyCnt; j++) {
                    dummyNodes[j] = new LayoutNode();
                    dummyNodes[j].layer = layoutNode.layer + j + 1;
                    dummyEdges[j] = new LayoutEdge(dummyNodes[j - 1], dummyNodes[j], dummyNodes[j - 1].width / 2, dummyNodes[j].width / 2, null);
                    dummyNodes[j].preds.add(dummyEdges[j]);
                    dummyNodes[j - 1].succs.add(dummyEdges[j]);
                }
                for (LayoutEdge unprocessedEdge : unprocessedEdges) {
                    assert unprocessedEdge.link != null;
                    assert unprocessedEdge.to.layer - layoutNode.layer - 2 >= 0;
                    assert unprocessedEdge.to.layer - layoutNode.layer - 2 < dummyCnt;
                    LayoutNode anchorNode = dummyNodes[unprocessedEdge.to.layer - layoutNode.layer - 2];
                    anchorNode.succs.add(unprocessedEdge);
                    unprocessedEdge.from = anchorNode;
                    unprocessedEdge.relativeFrom = anchorNode.width / 2;
                    layoutNode.succs.remove(unprocessedEdge);
                }
                allNodes.addAll(Arrays.asList(dummyNodes));
                for (LayoutNode dummyNode : dummyNodes) {
                    layers[dummyNode.layer].add(dummyNode);
                }
            }
        }
    }

    private class CreateDummyNodes {

        @SuppressWarnings({"unchecked"})
        private void createLayers() {
            layers = new LayoutLayer[layerCount];
            for (int i = 0; i < layerCount; i++) {
                layers[i] = new LayoutLayer();
            }


        }

        private void run() {
            createLayers();
            // Generate initial ordering
            HashSet<LayoutNode> visited = new HashSet<>();
            for (LayoutNode n : allNodes) {
                if (n.layer == 0) {
                    layers[0].add(n);
                    visited.add(n);
                } else if (n.preds.isEmpty()) {
                    layers[n.layer].add(n);
                    visited.add(n);
                }
            }


            //assertLayerNr();
            ArrayList<LayoutNode> currentNodes = new ArrayList<>(allNodes);
            for (LayoutNode layoutNode : currentNodes) {
                assert !layoutNode.isDummy();
                createDummiesForNode(layoutNode);
            }

            for (int i = 0; i < layers.length - 1; i++) {
                for (LayoutNode n : layers[i]) {
                    for (LayoutEdge e : n.succs) {
                        if (e.to.isDummy()) continue;
                        if (!visited.contains(e.to)) {
                            visited.add(e.to);
                            layers[i + 1].add(e.to);
                            e.to.layer = i + 1;
                        }
                    }
                }
            }
            assertLayerNr();
            assertNodeSynced();
            assertLinks();
        }
    }

    private void updateLayerPositions(int index) {
        int pos = 0;
        for (LayoutNode n : layers[index]) {
            n.pos = pos;
            pos++;
        }
    }

    private void updatePositions() {
        assert layers.length == layerCount;
        for (int l = 0; l < layerCount; ++l) {
            updateLayerPositions(l);
        }
    }

    private class CrossingReduction {

        private final Comparator<LayoutNode> CROSSING_NODE_COMPARATOR = (n1, n2) -> Float.compare(n1.crossingNumber, n2.crossingNumber);

        public CrossingReduction() {}

        private void run() {
            for (int i = 0; i < CROSSING_ITERATIONS; i++) {
                downSweep();
                upSweep();
            }
            downSweep();
            updatePositions();
            assertOrder();
        }


        private void updateXOfLayer(int index) {
            int x = 0;
            for (LayoutNode n : layers[index]) {
                n.x = x;
                x += n.getWholeWidth() + OFFSET;
            }
        }

        private void downSweep() {
            for (int i = 0; i < layerCount; i++) {
                for (LayoutNode n : layers[i]) {
                    n.loadCrossingNumber(false);
                }

                updateCrossingNumbers(i, true);
                layers[i].sort(CROSSING_NODE_COMPARATOR);
                updateXOfLayer(i);

            }
        }

        private void upSweep() {
            for (int i = layerCount - 1; i >= 0; i--) {
                for (LayoutNode n : layers[i]) {
                    n.loadCrossingNumber(true);
                }

                updateCrossingNumbers(i, false);
                layers[i].sort(CROSSING_NODE_COMPARATOR);
                updateXOfLayer(i);
            }
        }

        private void updateCrossingNumbers(int index, boolean down) {
            List<LayoutNode> layer;
            layer = layers[index];
            int diff = 0;
            LayoutNode prev;
            LayoutNode next;
            for (int i = 0; i < layer.size(); i++) {
                LayoutNode n = layer.get(i);
                if (down ? n.preds.isEmpty() : n.succs.isEmpty()) {
                    prev = null;
                    next = null;
                    if (i > 0) {
                        prev = layer.get(i - 1);
                    }
                    if (i < layer.size() - 1) {
                        next = layer.get(i + 1);
                    }

                    if (prev != null && next != null) {
                        n.crossingNumber = (prev.crossingNumber + next.crossingNumber) / 2;
                    } else if (prev != null) {
                        n.crossingNumber = prev.crossingNumber + diff;
                    } else if (next != null) {
                        n.crossingNumber = next.crossingNumber - diff;
                    }
                }
            }
        }
    }

    private class AssignXCoordinates {

        private int[][] space;
        private LayoutNode[][] downProcessingOrder;
        private LayoutNode[][] upProcessingOrder;

        private final Comparator<LayoutNode> DUMMY_NODES_FIRST = Comparator.comparing(LayoutNode::isDummy).reversed();

        private final Comparator<LayoutNode> NODE_PROCESSING_BOTH_COMPARATOR = DUMMY_NODES_FIRST.thenComparingInt(n -> n.preds.size() + n.succs.size());

        private final Comparator<LayoutNode> NODE_PROCESSING_DOWN_COMPARATOR = DUMMY_NODES_FIRST.thenComparingInt(n -> n.preds.size());

        private final Comparator<LayoutNode> NODE_PROCESSING_UP_COMPARATOR = DUMMY_NODES_FIRST.thenComparing(n -> n.succs.size());

        private final Comparator<LayoutNode> DUMMY_NODES_THEN_OPTMMAL_X = DUMMY_NODES_FIRST.thenComparing(n -> n.optimal_x);


        private void createArrays() {
            space = new int[layers.length][];
            downProcessingOrder = new LayoutNode[layers.length][];
            upProcessingOrder = new LayoutNode[layers.length][];

            for (int i = 0; i < layers.length; i++) {
                LayoutLayer layer = layers[i];
                space[i] = new int[layer.size()];
                int curX = 0;
                for (int y = 0; y < layer.size(); ++y) {
                    space[i][y] = curX;
                    LayoutNode node = layer.get(y);
                    curX += node.getWholeWidth() + X_OFFSET;
                }

                downProcessingOrder[i] = layer.toArray(new LayoutNode[0]);
                upProcessingOrder[i] = layer.toArray(new LayoutNode[0]);
                Arrays.sort(downProcessingOrder[i], NODE_PROCESSING_DOWN_COMPARATOR);
                Arrays.sort(upProcessingOrder[i], NODE_PROCESSING_UP_COMPARATOR);
                //Arrays.sort(downProcessingOrder[i], NODE_PROCESSING_BOTH_COMPARATOR);
                //Arrays.sort(upProcessingOrder[i], NODE_PROCESSING_BOTH_COMPARATOR);
            }
        }

        private void initialPositions() {
            for (LayoutNode node : allNodes) {
                assert node.layer >= 0;
                assert node.layer < layers.length;
                assert node.pos >= 0;
                assert node.pos < layers[node.layer].size();
                node.x = space[node.layer][node.pos];
            }
        }


        private void run() {
            createArrays();
            initialPositions();
            assertOrder();
            for (int i = 0; i < SWEEP_ITERATIONS; i++) {
                sweepDown();
                sweepUp();
            }
            assertOrder();
        }

        public void getOptimalPositions(LayoutEdge edge, int layer, List<Integer> vals, int correction, boolean up) {
            if (up) {
                if (edge.from.layer <= layer) {
                    vals.add(edge.getStartPoint() - correction);
                } else if (edge.from.isDummy()) {
                    edge.from.preds.forEach(x -> getOptimalPositions(x, layer, vals, correction, up));
                }
            } else {
                if (edge.to.layer >= layer) {
                    vals.add(edge.getEndPoint() - correction);
                } else if (edge.to.isDummy()) {
                    edge.to.succs.forEach(x -> getOptimalPositions(x, layer, vals, correction, up));
                }
            }
        }

        private int calculateOptimalDown(LayoutNode n) {
            List<Integer> values = new ArrayList<>();
            int layer = n.layer - 1;
            for (LayoutEdge e : n.preds) {
                getOptimalPositions(e, layer, values, e.relativeTo, true);
            }
            return median(values, n, true);
        }

        private int calculateOptimalUp(LayoutNode n) {
            List<Integer> values = new ArrayList<>();
            int layer = n.layer + 1;

            for (LayoutEdge e : n.succs) {
                getOptimalPositions(e, layer, values, e.relativeFrom, false);
            }
            return median(values, n, false);
        }

        private int median(List<Integer> values, LayoutNode n, boolean up) {
            if (values.isEmpty()) {
                return n.x;
            }
            if (!n.isDummy() && values.size() == 1 && (up ? n.preds.size() == 1 : n.succs.size() == 1)) {
                LayoutNode node;
                if (up) {
                    node = n.preds.get(0).from;
                } else {
                    node = n.succs.get(0).to;
                }
                if (up ? node.succs.size() == 1 : node.preds.size() == 1) {
                    return node.x + ((node.getWholeWidth() - n.getWholeWidth()) / 2);
                }
            }
            values.sort(Integer::compare);
            if (values.size() % 2 == 0) {
                return (values.get(values.size() / 2 - 1) + values.get(values.size() / 2)) / 2;
            } else {
                return values.get(values.size() / 2);
            }
        }

        private void sweepUp() {
            for (int i = layers.length - 2; i >= 0; i--) {
                for (LayoutNode n : upProcessingOrder[i]) {
                    n.optimal_x = calculateOptimalUp(n);
                }
                Arrays.sort(upProcessingOrder[i], DUMMY_NODES_THEN_OPTMMAL_X);
                NodeRow row = new NodeRow(space[i]);
                for (LayoutNode n : upProcessingOrder[i]) {
                    row.insert(n, n.optimal_x);
                }
            }
        }

        private void sweepDown() {
            for (int i = 1; i < layers.length; i++) {
                for (LayoutNode n : downProcessingOrder[i]) {
                    n.optimal_x = calculateOptimalDown(n);
                }
                Arrays.sort(downProcessingOrder[i], DUMMY_NODES_THEN_OPTMMAL_X);
                NodeRow row = new NodeRow(space[i]);
                for (LayoutNode n : downProcessingOrder[i]) {
                    row.insert(n, n.optimal_x);
                }
            }
        }

    }

    private void assertYLayer() {
        for (int i = 1; i < layerCount; i++) {
            LayoutLayer topLayer = layers[i-1];
            LayoutLayer bottomLayer = layers[i];
            assert topLayer.getBottom() + LAYER_OFFSET <= bottomLayer.getTop();
        }
    }

    private class AssignYCoordinates {

        private void run() {
            int currentY = 0;
            for (LayoutLayer layer : layers) {
                int layerHeight = layer.height;
                layer.y = currentY;
                int maxXOffset = 0;
                int maxLayerHeight = 0;

                for (LayoutNode layoutNode : layer) {
                    layoutNode.y = currentY;
                    if (!layoutNode.isDummy()) {
                        // center the node
                        int offset = Math.max(layoutNode.topYOffset, layoutNode.bottomYOffset);
                        layoutNode.topYOffset = offset;
                        layoutNode.bottomYOffset = offset;

                        layoutNode.topYOffset += (layerHeight - layoutNode.getWholeHeight()) / 2;
                        layoutNode.bottomYOffset = layerHeight - layoutNode.topYOffset - layoutNode.height;

                    }
                    maxLayerHeight = Math.max(maxLayerHeight, layoutNode.getWholeHeight());

                    for (LayoutEdge succEdge : layoutNode.succs) {
                        maxXOffset = Math.max(Math.abs(succEdge.getStartPoint() - succEdge.getEndPoint()), maxXOffset);
                    }
                }

                layer.height = maxLayerHeight;

                currentY += layerHeight + SCALE_LAYER_PADDING * Math.max((int) (Math.sqrt(maxXOffset) * 2), LAYER_OFFSET * 3);
            }

            assertYLayer();
        }
    }

    private class WriteResult {

        private HashMap<Vertex, Point> computeVertexPositions() {
            HashMap<Vertex, Point> vertexPositions = new HashMap<>();
            for (Vertex v : graph.getVertices()) {
                LayoutNode n = vertexToLayoutNode.get(v);
                vertexPositions.put(v, new Point(n.getLeftSide(), n.getTop()));
            }
            return vertexPositions;
        }

        private HashMap<Link, List<Point>> computeLinkPositions() {
            HashMap<Link, List<Point>> linkToSplitEndPoints = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();

            for (LayoutNode layoutNode : allNodes) {
                if (layoutNode.isDummy()) continue;

                for (LayoutEdge predEdge : layoutNode.preds) {
                    assert predEdge.link != null;

                    LayoutNode fromNode = predEdge.from;
                    LayoutNode toNode = predEdge.to;

                    ArrayList<Point> linkPoints = new ArrayList<>();
                    // input edge stub
                    linkPoints.add(new Point(predEdge.getEndPoint(), toNode.getTop()));
                    linkPoints.add(new Point(predEdge.getEndPoint(), layers[toNode.layer].getTop() - LAYER_OFFSET));

                    LayoutEdge curEdge = predEdge;
                    while (fromNode.isDummy() && !fromNode.preds.isEmpty()) {
                        linkPoints.add(new Point(fromNode.getCenterX(), layers[fromNode.layer].getBottom() + LAYER_OFFSET));
                        linkPoints.add(new Point(fromNode.getCenterX(), layers[fromNode.layer].getTop() - LAYER_OFFSET));
                        curEdge = fromNode.preds.get(0);
                        fromNode = curEdge.from;
                    }
                    linkPoints.add(new Point(curEdge.getStartPoint(), layers[fromNode.layer].getBottom() + LAYER_OFFSET));
                    // output edge stub
                    linkPoints.add(new Point(curEdge.getStartPoint(), fromNode.getBottom()));

                    if (reversedLinks.contains(predEdge.link)) {
                        for (Point relativeEnd : reversedLinkEndPoints.get(predEdge.link)) {
                            Point endPoint = new Point(toNode.getLeftSide() + relativeEnd.x,  toNode.getTop() + relativeEnd.y);
                            linkPoints.add(0, endPoint);
                        }

                        if (!fromNode.isDummy()) {
                            if (reversedLinkStartPoints.containsKey(predEdge.link)) {
                                for (Point relativeStart : reversedLinkStartPoints.get(predEdge.link)) {
                                    Point startPoint = new Point( fromNode.getLeftSide() + relativeStart.x, fromNode.getTop() + relativeStart.y );
                                    linkPoints.add(startPoint);
                                }
                            }
                        }
                    } else {
                        Collections.reverse(linkPoints);
                    }

                    if (fromNode.isDummy()) {
                        if (reversedLinks.contains(predEdge.link)) {
                            Collections.reverse(linkPoints);
                        }
                        linkToSplitEndPoints.put(predEdge.link, linkPoints);

                    } else {
                        linkPositions.put(predEdge.link, linkPoints);
                    }
                }
            }


            for (LayoutNode layoutNode : allNodes) {
                if (layoutNode.isDummy()) continue;

                for (LayoutEdge succEdge : layoutNode.succs) {
                    if (succEdge.link == null) continue;

                    LayoutNode fromNode = succEdge.from;
                    LayoutNode toNode = succEdge.to;

                    ArrayList<Point> linkPoints = new ArrayList<>();
                    linkPoints.add(new Point(succEdge.getStartPoint(), fromNode.getBottom()));
                    linkPoints.add(new Point(succEdge.getStartPoint(), layers[fromNode.layer].getBottom() + LAYER_OFFSET));

                    LayoutEdge curEdge = succEdge;
                    while (toNode.isDummy() && !toNode.succs.isEmpty()) {
                        linkPoints.add(new Point(toNode.getCenterX(), layers[toNode.layer].getTop() - LAYER_OFFSET));
                        linkPoints.add(new Point(toNode.getCenterX(), layers[toNode.layer].getBottom() + LAYER_OFFSET));
                        curEdge = toNode.succs.get(0);
                        toNode = curEdge.to;
                    }
                    linkPoints.add(new Point(curEdge.getEndPoint(), layers[toNode.layer].getTop() - LAYER_OFFSET));
                    linkPoints.add(new Point(curEdge.getEndPoint(), toNode.getTop()));

                    if (reversedLinks.contains(succEdge.link)) {
                        Collections.reverse(linkPoints);

                        if (reversedLinkStartPoints.containsKey(succEdge.link)) {
                            for (Point relativeStart : reversedLinkStartPoints.get(succEdge.link)) {
                                Point startPoint = new Point( fromNode.getLeftSide() + relativeStart.x, fromNode.getTop() + relativeStart.y );
                                linkPoints.add(startPoint);
                            }
                        }

                        if (!toNode.isDummy()) {
                            if (reversedLinkEndPoints.containsKey(succEdge.link)) {
                                for (Point relativeEnd : reversedLinkEndPoints.get(succEdge.link)) {
                                    Point endPoint = new Point(toNode.getLeftSide() + relativeEnd.x,  toNode.getTop() + relativeEnd.y);
                                    linkPoints.add(0, endPoint);
                                }
                            }
                        }
                    }

                    if (linkToSplitEndPoints.containsKey(succEdge.link)) {
                        if (reversedLinks.contains(succEdge.link)) {
                            Collections.reverse(linkPoints);
                        }
                        linkPoints.add(null);
                        linkPoints.addAll(linkToSplitEndPoints.get(succEdge.link));
                        if (reversedLinks.contains(succEdge.link)) {
                            Collections.reverse(linkPoints);
                        }
                        assert !linkPositions.containsKey(succEdge.link);
                    }
                    linkPositions.put(succEdge.link, linkPoints);
                }
            }

            return linkPositions;
        }

        public void run() {
            assert allNodes.size() == (new HashSet<>(allNodes)).size();

            assertEdgesConnected();
            assertOrder();

            HashMap<Vertex, Point> vertexPositions = computeVertexPositions();
            assertOrder();

            HashMap<Link, List<Point>> linkPositions = computeLinkPositions();
            assertOrder();
            assertEdgesConnected();

            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Point point : vertexPositions.values()) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
            }

            for (List<Point> points : linkPositions.values()) {
                for (Point point : points) {
                    if (point != null) {
                        minX = Math.min(minX, point.x);
                        minY = Math.min(minY, point.y);
                    }
                }
            }

            assertOrder();

            for (LayoutNode node : allNodes) {
                minX = Math.min(minX, node.x);
                minY = Math.min(minY, node.y);
            }

            for (LayoutLayer layer : layers) {
                minY = Math.min(minY, layer.y);
            }

            for (LayoutNode node : allNodes) {
                node.x -= minX;
                assert node.x >= 0;
                node.y -= minY;
                assert node.y >= 0;
            }

            assertOrder();

            for (LayoutLayer layer : layers) {
                layer.y -= minY;
                assert layer.y >= 0;
            }

            assertOrder();

            // shift vertices by minX/minY
            for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
                Point point = entry.getValue();
                point.x -= minX;
                point.y -= minY;
                assert point.x >= 0;
                assert point.y >= 0;
                Vertex vertex = entry.getKey();
                vertex.setPosition(point);
            }

            assertOrder();

            // shift links by minX/minY
            for (Map.Entry<Link, List<Point>> entry : linkPositions.entrySet()) {
                Link link = entry.getKey();
                List<Point> points = entry.getValue();
                for (Point p : points) {
                    if (p != null) {
                        p.x -= minX;
                        p.y -= minY;
                        assert p.x >= 0;
                        assert p.y >= 0;
                    }
                }

                // write points back to links
                link.setControlPoints(points);
            }

            assertOrder();
            assertNodePos();
        }
    }

    private class LayoutLayer extends ArrayList<LayoutNode> {

        private int height = 0;
        private int y = 0;

        @Override
        public boolean addAll(Collection<? extends LayoutNode> c) {
            c.forEach(this::setHeight);
            return super.addAll(c);
        }

        private void setHeight(LayoutNode n) {
            height = Math.max(height, n.getWholeHeight());
        }

        @Override
        public boolean add(LayoutNode n) {
            setHeight(n);
            return super.add(n);
        }

        public int getTop() {
            return y;
        }

        public int getCenter() {
            return y + height/2;
        }

        public int getBottom() {
            return y + height;
        }
    }

    private class NodeRow {

        private final TreeSet<LayoutNode> treeSet;
        private final int[] space;

        public NodeRow(int[] space) {
            treeSet = new TreeSet<>(Comparator.comparingInt(n -> n.pos));
            this.space = space;
        }

        public int offset(LayoutNode n1, LayoutNode n2) {
            int v1 = space[n1.pos] + n1.getWholeWidth();
            int v2 = space[n2.pos];
            return v2 - v1;
        }

        public void insert(LayoutNode n, int pos) {
            int minX = Integer.MIN_VALUE;
            SortedSet<LayoutNode> headSet = treeSet.headSet(n, false);
            if (!headSet.isEmpty()) {
                LayoutNode leftNeighbor = headSet.last();
                minX = leftNeighbor.getRightBorder() + offset(leftNeighbor, n);
            }

            int maxX = Integer.MAX_VALUE;
            SortedSet<LayoutNode> tailSet = treeSet.tailSet(n, false);
            if (!tailSet.isEmpty()) {
                LayoutNode rightNeighbor = tailSet.first();
                maxX = rightNeighbor.x - offset(n, rightNeighbor) - n.getWholeWidth();
            }

            assert minX <= maxX : minX + " vs " + maxX;
            if (pos < minX) {
                pos = minX;
            } else if (pos > maxX) {
                pos = maxX;
            }
            n.x = pos;
            treeSet.add(n);
        }
    }

    private void assertNodePos() {
        for (LayoutNode n : allNodes) {
            assert n.x >= 0;
            assert n.y >= 0;
            assert n.rightXOffset >= 0;
            assert n.leftXOffset >= 0;
            assert n.topYOffset >= 0;
            assert n.bottomYOffset >= 0;
            assert n.height >= 0;
            assert n.width >= 0;

            for (LayoutEdge e : n.preds) {
                assert e.from.x >= 0;
                assert e.from.y >= 0;
                assert e.to.x >= 0;
                assert e.to.y >= 0;
            }

            for (LayoutEdge e : n.succs) {
                assert e.from.x >= 0;
                assert e.from.y >= 0;
                assert e.to.x >= 0;
                assert e.to.y >= 0;
            }

        }

        for (Vertex v : graph.getVertices()) {
            LayoutNode n = vertexToLayoutNode.get(v);
            assert n.x >= 0;
            assert n.y >= 0;
            assert n.rightXOffset >= 0;
            assert n.leftXOffset >= 0;
            assert n.topYOffset >= 0;
            assert n.bottomYOffset >= 0;
            assert n.height >= 0;
            assert n.width >= 0;
        }
    }

    private void assertEdgesConnected() {
        /*
        for (LayoutNode layoutNode : allNodes) {
            if (layoutNode.isDummy()) continue;
            assert layoutNode.vertex != null;
            for (LayoutEdge predEdge : layoutNode.preds) {
                Link link = predEdge.link;
                assert link != null;
                LayoutNode fromNode = predEdge.from;
                LayoutNode toNode = predEdge.to;
                assert toNode == layoutNode;

                assert link.getTo().getVertex() != null;
                assert link.getFrom().getVertex() != null;

                LayoutEdge curEdge;
                while (fromNode.isDummy() && !fromNode.preds.isEmpty()) {
                    curEdge = fromNode.preds.get(0);
                    fromNode = curEdge.from;
                }
                if (fromNode.vertex != null) {
                    assert !fromNode.isDummy();
                    assert fromNode.vertex != toNode.vertex;
                    assert link.getTo().getVertex() == toNode.vertex || link.getTo().getVertex() == fromNode.vertex;
                    assert link.getFrom().getVertex() == fromNode.vertex || link.getFrom().getVertex() == toNode.vertex;
                }
            }
        }*/
    }

}

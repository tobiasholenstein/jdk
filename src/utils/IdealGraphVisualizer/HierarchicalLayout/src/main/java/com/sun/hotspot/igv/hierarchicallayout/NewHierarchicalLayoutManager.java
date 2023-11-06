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
import com.sun.hotspot.igv.layout.LayoutManager;
import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Vertex;
import com.sun.hotspot.igv.util.Statistics;
import java.awt.Dimension;
import java.awt.Point;
import java.util.*;


public class NewHierarchicalLayoutManager implements LayoutManager  {

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
    private final List<LayoutNode> dummyNodes;
    private final HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private final Set<Link> reversedLinks;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;


    private void assertLinks() {
        for (LayoutNode node : getLayoutNodes()) {
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
                assert dummyNodes.contains(node) | getLayoutNodes().contains(node);
            }
        }

        for (LayoutNode node : dummyNodes) {
            boolean found = false;
            for (LayoutLayer layer : layers) {
                if (layer.contains(node)) {
                    found = true;
                    break;
                }
            }
            assert found;
        }

        for (LayoutNode node : getLayoutNodes()) {
            boolean found = false;
            for (LayoutLayer layer : layers) {
                if (layer.contains(node)) {
                    found = true;
                    break;
                }
            }
            assert found;
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
        for (LayoutNode node : dummyNodes) {
            assert node.layer >= 0;
            assert node.layer < layers.length;
            assert node.pos >= 0;
            assert node.pos < layers[node.layer].size();
        }

        for (LayoutNode node : getLayoutNodes()) {
            assert node.layer >= 0;
            assert node.layer < layers.length;
            assert node.pos >= 0;
            assert node.pos < layers[node.layer].size();
        }

        assertNodeSynced();
    }

    private void insertNodeAndAdjustLayer(LayoutNode node) {
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


        int minX = node.pos == 0 ? 0 : layer.get(node.pos-1).getRightBorder();
        node.x = Math.max( node.x, minX);

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

        addNode(node);
        assertOrder();
    }

    private void addNode(LayoutNode node) {
        if (node.isDummy()) {
            dummyNodes.add(node);
        } else {
            vertexToLayoutNode.put(node.vertex, node);
        }
    }

    // remove a node : do not update x, but assert that pos withing layer is correct
    private void removeNode(LayoutNode node) {
        int layer = node.layer;
        layers[layer].remove(node);
        updateLayerPositions(layers[layer]);
        // Remove node from graph layout
        if (node.isDummy()) {
            assert node.isDummy();
            dummyNodes.remove(node);
        } else {
            vertexToLayoutNode.remove(node.vertex);
        }
    }

    private void applyRemoveLinkAction(Link link) {
        Vertex from = link.getFrom().getVertex();
        Vertex to = link.getTo().getVertex();
        LayoutNode toNode = vertexToLayoutNode.get(to);
        LayoutNode fromNode = vertexToLayoutNode.get(from);

        if (toNode.layer < fromNode.layer) {
            // Reversed edge
            toNode = fromNode;
            reversedLinks.remove(link);
            toNode.reversedLinkEndPoints.remove(link);
            fromNode.reversedLinkStartPoints.remove(link);
        }

        // Remove preds-edges bottom up, starting at "to" node
        // Cannot start from "from" node since there might be joint edges
        List<LayoutEdge> toNodePredsEdges = List.copyOf(toNode.preds);
        for (LayoutEdge edge : toNodePredsEdges) {
            LayoutNode predNode = edge.from;
            LayoutEdge edgeToRemove;

            if (edge.link != null && edge.link.equals(link)) {
                toNode.preds.remove(edge);
                edgeToRemove = edge;
            } else {
                // Wrong edge, look at next
                continue;
            }

            if (!predNode.isDummy() && predNode.vertex.equals(from)) {
                // No dummy nodes inbetween 'from' and 'to' vertex
                predNode.succs.remove(edgeToRemove);
                break;
            } else {
                // Must remove edges between dummy nodes
                boolean found = true;
                LayoutNode succNode = toNode;
                while (predNode.isDummy() && found) {
                    found = false;

                    if (predNode.succs.size() <= 1 && predNode.preds.size() <= 1) {
                        // Dummy node used only for this link, remove if not already removed
                        if (dummyNodes.contains(predNode)) {
                            removeNode(predNode);
                        }
                    } else {
                        // anchor node, should not be removed
                        break;
                    }

                    if (predNode.preds.size() == 1) {
                        predNode.succs.remove(edgeToRemove);
                        succNode = predNode;
                        edgeToRemove = predNode.preds.get(0);
                        predNode = edgeToRemove.from;
                        found = true;
                    }
                }

                predNode.succs.remove(edgeToRemove);
                succNode.preds.remove(edgeToRemove);
            }
            break;
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
                movedNode.reversedLinkStartPoints.remove(link);
            } else if (link.getFrom().getVertex() == movedNode.vertex) {
                link.setControlPoints(new ArrayList<>());
                reversedLinks.remove(link);
                movedNode.reversedLinkEndPoints.remove(link);
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
                reversedLayoutNodes.add(fromNode);
                reversedLayoutNodes.add(toNode);
            }
        }
        assertOrder();


        // ReverseEdges
        for (LayoutNode layoutNode : reversedLayoutNodes) {
            computeReversedLinkPoints(layoutNode, false);
        }
        assertOrder();

        // CreateDummyNodes
        createDummiesForNodeSuccessor(movedNode, true);
        for (LayoutEdge predEdge : movedNode.preds) {
            insertDummyNodes(predEdge);
        }

        assertNodeSynced();
        assertLayerNr();

        updatePositions();

        assertNodeSynced();
        assertLayerNr();
        assertOrder();

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

    public void removeEmptyLayers(int emtpyLayerNr) {
        for (LayoutNode layoutNode : getLayoutNodes()) {
            for (LayoutEdge predEdge : layoutNode.preds) {
                assert predEdge.link != null;
            }
        }
        for (LayoutNode layoutNode : layers[emtpyLayerNr]) {
            assert layoutNode.isDummy() : "has to be empty layer";
        }

        if (0 < emtpyLayerNr && emtpyLayerNr < layerCount-1) {
            LayoutLayer emptyLayer = layers[emtpyLayerNr];
            for (LayoutNode dummyNode : emptyLayer) {
                assert dummyNode.isDummy();
                LayoutEdge predEdge = dummyNode.preds.get(0);
                LayoutNode fromNode = predEdge.from; // Root
                // remove the dummy node from Root
                fromNode.succs.remove(predEdge);
                if (predEdge.link != null) {
                    assert dummyNode.succs.size() == 1;
                    LayoutEdge succEdge = dummyNode.succs.get(0);
                    succEdge.link = predEdge.link;
                }

                // modify succEdge to come from fromNode and add to succs
                for (LayoutEdge succEdge : dummyNode.succs) {
                    succEdge.from = fromNode;
                    fromNode.succs.add(succEdge);
                    succEdge.relativeFrom = predEdge.relativeFrom;
                }
                assert dummyNode.isDummy();
                dummyNodes.remove(dummyNode);
            }

            for (LayoutNode layoutNode : getLayoutNodes()) {
                for (LayoutEdge predEdge : layoutNode.preds) {
                    assert predEdge.link != null;
                }
            }
        } else if (0 == emtpyLayerNr) {
            assert layers[0].isEmpty();
        } else { // emtpyLayerNr = layerCount -1
            assert 0 != layerCount - 1 || layers[layerCount - 1].isEmpty();
        }

        LayoutLayer[] compactedLayers = new LayoutLayer[layerCount - 1];
        // copy upper part from layers to extendedLayers
        System.arraycopy(layers, 0, compactedLayers, 0, emtpyLayerNr);
        // copy lower part from layers to extendedLayers
        System.arraycopy(layers, emtpyLayerNr + 1, compactedLayers, emtpyLayerNr, layerCount - emtpyLayerNr - 1);

        --layerCount;
        layers = compactedLayers;

        for (int l = emtpyLayerNr; l < layerCount; l++) {
            for (LayoutNode layoutNode : layers[l]) {
                layoutNode.layer = l;
            }
        }

        for (LayoutNode layoutNode : getLayoutNodes()) {
            for (LayoutEdge predEdge : layoutNode.preds) {
                assert predEdge.link != null;
            }
        }
    }

    public void moveNode(LayoutNode node, int newX, int newLayerNr) {
        int newPos = findPosInLayer(newX, newLayerNr);

        // remove from old layer and update positions in old layer
        int oldLayerNr = node.layer;
        removeNode(node);

        // set x of movedNode
        node.x = newX;

        boolean shouldRemoveEmptyLayers = false;
        if (node.layer != newLayerNr) { // insert into a different layer
            node.layer = newLayerNr;
            node.pos = newPos;
            shouldRemoveEmptyLayers = true;
            for (LayoutNode layoutNode : layers[oldLayerNr]) {
                if (!layoutNode.isDummy()) {
                    shouldRemoveEmptyLayers = false;
                    break;
                }
            }
        } else { // move within the same layer
            //assert movedNode.pos != newPos; // handled before
            if (node.pos < newPos) { // moved to the right
                // adjust because we have already removed movedNode in this layer
                node.pos = newPos - 1;
            } else { // moved to the left
                node.pos = newPos;
            }
        }
        insertNodeAndAdjustLayer(node);

        if (shouldRemoveEmptyLayers) {
            removeEmptyLayers(oldLayerNr);
        }
    }

    // check that NO neighbors of node are in a given layer
    private int insertNewLayerIfNeeded(LayoutNode node, int layerNr) {
        for (LayoutNode layoutNode : getLayoutNodes()) {
            for (LayoutEdge predEdge : layoutNode.preds) {
                assert predEdge.link != null;
            }
        }
        for (Link inputLink : graph.getInputLinks(node.vertex)) {
            if (inputLink.getFrom().getVertex() == inputLink.getTo().getVertex()) continue;
            LayoutNode fromNode = vertexToLayoutNode.get(inputLink.getFrom().getVertex());
            if (fromNode.layer == layerNr) {
                moveExpandLayerDown(layerNr + 1);
                return layerNr + 1;
            }
        }
        for (Link outputLink : graph.getOutputLinks(node.vertex)) {
            if (outputLink.getFrom().getVertex() == outputLink.getTo().getVertex()) continue;
            LayoutNode toNode = vertexToLayoutNode.get(outputLink.getTo().getVertex());
            if (toNode.layer == layerNr) {
                moveExpandLayerDown(layerNr);
                return layerNr;
            }
        }

        for (LayoutNode layoutNode : getLayoutNodes()) {
            for (LayoutEdge predEdge : layoutNode.preds) {
                assert predEdge.link != null;
            }
        }
        return layerNr;

    }

    private void moveExpandLayerDown(int layerNr) {
        assert layerNr < layerCount;
        LayoutLayer[] extendedLayers = new LayoutLayer[layerCount + 1];
        // copy upper part from layers to extendedLayers
        System.arraycopy(layers, 0, extendedLayers, 0, layerNr);
        // insert new LayoutLayer at layerNr into extendedLayers
        extendedLayers[layerNr] = new LayoutLayer();
        // copy lower part from layers to extendedLayers
        System.arraycopy(layers, layerNr, extendedLayers, layerNr + 1, layerCount - layerNr);

        for (LayoutNode oldNodeBelow : layers[layerNr]) {
            for (LayoutEdge predEdge : oldNodeBelow.preds) {
                LayoutNode dummyNode = createDummyBetween(predEdge);
                assert dummyNode.isDummy();
                dummyNodes.add(dummyNode);
                dummyNode.layer = layerNr;
                dummyNode.x = oldNodeBelow.x;
                extendedLayers[layerNr].add(dummyNode);
            }
        }


        extendedLayers[layerNr].sort(Comparator.comparingInt(n -> n.x));
        updateLayerPositions(extendedLayers[layerNr]);

        ++layerCount;
        layers = extendedLayers;

        // update layer field in nodes below layerNr
        for (int l = layerNr + 1; l < layerCount; l++) {
            for (LayoutNode layoutNode : layers[l]) {
                layoutNode.layer = l;
            }
        }

        assertPos();
        assertOrder();
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
        if (movedNode.layer == newLayerNr) { // we move the node in the same layer
            // the node did not change position withing the layer
            if (tryMoveNodeInSamePosition(movedNode, newLocation.x, newLayerNr)) {
                optimizeBackedgeCrossing();
                straightenEdges();
                new AssignYCoordinates().run();
                assertOrder();
                new WriteResult().run();
                return;
            }
            moveNode(movedNode, newLocation.x, movedNode.layer);
        } else { // only remove edges if we moved the node to a new layer
            newLayerNr = insertNewLayerIfNeeded(movedNode, newLayerNr);
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
        //new AssignXCoordinates().run();
        optimizeBackedgeCrossing();
        straightenEdges();
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

        public final HashMap<Link, List<Point>> reversedLinkStartPoints = new HashMap<>();
        public final HashMap<Link, List<Point>> reversedLinkEndPoints = new HashMap<>();
        public int pos = -1; // Position within layer

        public float crossingNumber = 0;
        public boolean reverseLeft = false;

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
        dummyNodes = new ArrayList<>();
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

    public List<LayoutNode> getNodes() {
        List<LayoutNode> allNodes = new ArrayList<>(dummyNodes);
        allNodes.addAll(getLayoutNodes());
        return allNodes;
    }

    @Override
    public void doLayout(LayoutGraph graph, Set<? extends Link> importantLinks) {
        doLayout(graph);
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
            dummyNodes.clear();

            // Set up nodes
            for (Vertex v : graph.getVertices()) {
                LayoutNode node = new LayoutNode(v);
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
        //assert !reversedLinks.contains(layoutEdge.link);
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
                succEdge.relativeFrom = succEdge.link.getTo().getRelativePosition().x;
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

            ArrayList<Point> startPoints = new ArrayList<>();
            startPoints.add(new Point(currentX, startY));
            startPoints.add(new Point(currentX, currentY));
            startPoints.add(new Point(startX, currentY));
            startPoints.add(new Point(startX, startY));
            for (LayoutEdge revEdge : reversedSuccs) {
                revEdge.relativeFrom = currentX;
                node.reversedLinkStartPoints.put(revEdge.link, startPoints);
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
                predEdge.relativeTo = predEdge.link.getFrom().getRelativePosition().x;
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

            ArrayList<Point> endPoints = new ArrayList<>();
            endPoints.add(new Point(currentX, startY));
            endPoints.add(new Point(currentX, currentY));
            endPoints.add(new Point(startX, currentY));
            endPoints.add(new Point(startX, startY));
            for (LayoutEdge revEdge : reversedPreds) {
                revEdge.relativeTo = currentX;
                node.reversedLinkEndPoints.put(revEdge.link, endPoints);
            }
        }
        node.leftXOffset += left ? sortedUpMap.size() * OFFSET : 0;
        node.rightXOffset += left ? 0 : sortedUpMap.size() * OFFSET;

        return !sortedUpMap.isEmpty();
    }


    private void computeReversedLinkPoints(LayoutNode node, boolean reverseLeft) {
        // reset node, except (x, y)
        node.reverseLeft = reverseLeft;
        node.width = node.vertex.getSize().width;
        node.height = node.vertex.getSize().height;
        node.topYOffset = 0;
        node.bottomYOffset = 0;
        node.leftXOffset = 0;
        node.rightXOffset = 0;
        node.reversedLinkStartPoints.clear();
        node.reversedLinkEndPoints.clear();

        //boolean reverseLeft = false; // default is false
        boolean hasReversedDown = computeReversedStartPoints(node, reverseLeft);
        boolean hasReversedUP = computeReversedEndPoints(node, hasReversedDown != reverseLeft);
        assert !hasReversedDown || !node.reversedLinkStartPoints.isEmpty();
        assert !hasReversedUP || !node.reversedLinkEndPoints.isEmpty();
    }

    public Collection<LayoutNode> getLayoutNodes() {
        return vertexToLayoutNode.values();
    }

    private class ReverseEdges {

        private HashSet<LayoutNode> visited;
        private HashSet<LayoutNode> active;

        private void run() {
            reversedLinks.clear();

            // Remove self-edges
            for (LayoutNode node : getLayoutNodes()) {
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
            for (LayoutNode node : getLayoutNodes()) {
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
            for (LayoutNode node : getLayoutNodes()) {
                DFS(node);
            }
            for (LayoutNode node : getLayoutNodes()) {
                computeReversedLinkPoints(node, false);
            }
        }

        private void controlFlowBackEdges() {
            ArrayList<LayoutNode> workingList = new ArrayList<>();
            for (LayoutNode node : getLayoutNodes()) {
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
            for (LayoutNode node : getLayoutNodes()) {
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
            for (LayoutNode node : getLayoutNodes()) {
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
            for (LayoutNode n : getLayoutNodes()) {
                n.layer = (layerCount - 1 - n.layer);
            }
        }
    }

    private void insertDummyNodes(LayoutEdge edge) {
        LayoutNode fromNode = edge.from;
        LayoutNode toNode = edge.to;
        if (Math.abs(toNode.layer - fromNode.layer) <= 1) return;

        boolean hasEdgeFromSamePort = false;
        LayoutEdge edgeFromSamePort = new LayoutEdge(fromNode, toNode);

        for (LayoutEdge succEdge : fromNode.succs) {
            if (succEdge.relativeFrom == edge.relativeFrom && succEdge.to.vertex == null) {
                edgeFromSamePort = succEdge;
                hasEdgeFromSamePort = true;
                break;
            }
        }

        if (!hasEdgeFromSamePort) {
            processSingleEdge(edge);
        } else {
            LayoutEdge curEdge = edgeFromSamePort;
            boolean newEdge = true;
            while (curEdge.to.layer < toNode.layer - 1 && curEdge.to.vertex == null && newEdge) {
                // Traverse down the chain of dummy nodes linking together the edges originating
                // from the same port
                newEdge = false;
                if (curEdge.to.succs.size() == 1) {
                    curEdge = curEdge.to.succs.get(0);
                    newEdge = true;
                } else {
                    for (LayoutEdge e : curEdge.to.succs) {
                        if (e.to.vertex == null) {
                            curEdge = e;
                            newEdge = true;
                            break;
                        }
                    }
                }
            }

            LayoutNode prevDummy;
            if (curEdge.to.vertex != null) {
                prevDummy = curEdge.from;
            } else {
                prevDummy = curEdge.to;
            }

            edge.from = prevDummy;
            edge.relativeFrom = prevDummy.width / 2;
            fromNode.succs.remove(edge);
            prevDummy.succs.add(edge);
            processSingleEdge(edge);
        }
    }

    private void processSingleEdge(LayoutEdge layoutEdge) {
        LayoutNode layoutNode = layoutEdge.to;
        if (layoutEdge.to.layer - 1 > layoutEdge.from.layer) {
            LayoutEdge prevEdge = layoutEdge;
            for (int l = layoutNode.layer - 1; l > prevEdge.from.layer; l--) {
                LayoutNode dummyNode = createDummyBetween(prevEdge);
                insertNode(dummyNode, l);
                prevEdge = dummyNode.preds.get(0);
            }
        }
    }

    private LayoutNode createDummyBetween(LayoutEdge layoutEdge) {
        LayoutNode dummyNode = new LayoutNode();
        dummyNode.width = DUMMY_WIDTH;
        dummyNode.height = DUMMY_HEIGHT;
        dummyNode.succs.add(layoutEdge);
        LayoutEdge result = new LayoutEdge(layoutEdge.from, dummyNode, layoutEdge.relativeFrom, 0, layoutEdge.link);
        dummyNode.preds.add(result);
        layoutEdge.relativeFrom = 0;
        layoutEdge.from.succs.remove(layoutEdge);
        layoutEdge.from.succs.add(result);
        layoutEdge.from = dummyNode;
        return dummyNode;
    }

    private void insertNode(LayoutNode node, int layer) {
        node.layer = layer;
        List<LayoutNode> layerNodes = layers[layer];

        if (layerNodes.isEmpty()) {
            node.pos = 0;
        } else {
            node.pos = optimalPosition(node, layer);
        }

        for (LayoutNode n : layerNodes) {
            if (n.pos >= node.pos) {
                n.pos += 1;
            }
        }
        layerNodes.add(node);

        addNode(node);

        if (node.vertex != null) {
            vertexToLayoutNode.put(node.vertex, node);
        }
    }

    public void createDummiesForNodeSuccessor(LayoutNode layoutNode, boolean optimalPos) {
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
                        if (optimalPos) {
                            topCutNode.pos = optimalPosition(topCutNode, topCutNode.layer);
                            topCutNode.x = 0;
                            insertNodeAndAdjustLayer(topCutNode);
                        } else {
                            assert topCutNode.isDummy();
                            dummyNodes.add(topCutNode);
                            layers[topCutNode.layer].add(topCutNode);
                        }
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
                        if (optimalPos) {
                            bottomCutNode.pos = optimalPosition(bottomCutNode, bottomCutNode.layer);
                            bottomCutNode.x = 0;
                            insertNodeAndAdjustLayer(bottomCutNode);
                        } else {
                            assert bottomCutNode.isDummy();
                            dummyNodes.add(bottomCutNode);
                            layers[bottomCutNode.layer].add(bottomCutNode);
                        }
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
                        if (optimalPos) {
                            dummyNode.pos = optimalPosition(dummyNode, dummyNode.layer);
                            dummyNode.x = 0;
                            insertNodeAndAdjustLayer(dummyNode);
                        } else {
                            assert dummyNode.isDummy();
                            dummyNodes.add(dummyNode);
                            layers[dummyNode.layer].add(dummyNode);
                        }
                        LayoutEdge dummyEdge = new LayoutEdge(dummyNode, previousEdge.to, dummyNode.width / 2, previousEdge.relativeTo, null);
                        dummyNode.succs.add(dummyEdge);
                        previousEdge.relativeTo = dummyNode.width / 2;
                        previousEdge.to.preds.remove(previousEdge);
                        previousEdge.to.preds.add(dummyEdge);
                        previousEdge.to = dummyNode;
                        previousEdge = dummyEdge;
                    }
                    previousEdge.link = singleEdge.link;
                }
            } else {
                int lastLayer = unprocessedEdges.get(unprocessedEdges.size() - 1).to.layer;
                int dummyCnt = lastLayer - layoutNode.layer - 1;
                LayoutEdge[] newDummyEdges = new LayoutEdge[dummyCnt];
                LayoutNode[] newDummyNodes = new LayoutNode[dummyCnt];

                newDummyNodes[0] = new LayoutNode();
                newDummyNodes[0].layer = layoutNode.layer + 1;
                newDummyEdges[0] = new LayoutEdge(layoutNode, newDummyNodes[0], startPort, newDummyNodes[0].width / 2, null);
                newDummyNodes[0].preds.add(newDummyEdges[0]);
                layoutNode.succs.add(newDummyEdges[0]);
                for (int j = 1; j < dummyCnt; j++) {
                    newDummyNodes[j] = new LayoutNode();
                    newDummyNodes[j].layer = layoutNode.layer + j + 1;
                    newDummyEdges[j] = new LayoutEdge(newDummyNodes[j - 1], newDummyNodes[j], newDummyNodes[j - 1].width / 2, newDummyNodes[j].width / 2, null);
                    newDummyNodes[j].preds.add(newDummyEdges[j]);
                    newDummyNodes[j - 1].succs.add(newDummyEdges[j]);
                }
                for (LayoutEdge unprocessedEdge : unprocessedEdges) {
                    assert unprocessedEdge.link != null;
                    assert unprocessedEdge.to.layer - layoutNode.layer - 2 >= 0;
                    assert unprocessedEdge.to.layer - layoutNode.layer - 2 < dummyCnt;
                    LayoutNode anchorNode = newDummyNodes[unprocessedEdge.to.layer - layoutNode.layer - 2];
                    anchorNode.succs.add(unprocessedEdge);
                    unprocessedEdge.from = anchorNode;
                    unprocessedEdge.relativeFrom = anchorNode.width / 2;
                    layoutNode.succs.remove(unprocessedEdge);
                }
                for (LayoutNode dummyNode : newDummyNodes) {
                    if (optimalPos) {
                        dummyNode.pos = optimalPosition(dummyNode, dummyNode.layer);
                        dummyNode.x = 0;
                        insertNodeAndAdjustLayer(dummyNode);
                    } else {
                        assert dummyNode.isDummy();
                        dummyNodes.add(dummyNode);
                        layers[dummyNode.layer].add(dummyNode);
                    }
                }
            }
        }
    }

    private class CreateDummyNodes {

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
            for (LayoutNode layoutNode : getLayoutNodes()) {
                if (layoutNode.layer == 0) {
                    layers[0].add(layoutNode);
                    visited.add(layoutNode);
                } else if (layoutNode.preds.isEmpty()) {
                    layers[layoutNode.layer].add(layoutNode);
                    visited.add(layoutNode);
                }
            }

            ArrayList<LayoutNode> currentNodes = new ArrayList<>(getLayoutNodes());
            for (LayoutNode layoutNode : currentNodes) {
                assert !layoutNode.isDummy();
                createDummiesForNodeSuccessor(layoutNode, false);
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

    private void updateLayerPositions(LayoutLayer layer) {
        int pos = 0;
        for (LayoutNode layoutNode : layer) {
            layoutNode.pos = pos;
            pos++;
        }
    }

    private void updatePositions() {
        assert layers.length == layerCount;
        for (int l = 0; l < layerCount; ++l) {
            updateLayerPositions(layers[l]);
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

    public int getBackedgeCrossingScore(LayoutNode node) {
        int score = 0;
        for (LayoutEdge predEdge : node.preds) {
            if (reversedLinks.contains(predEdge.link)) {
                List<Point> points = node.reversedLinkEndPoints.get(predEdge.link);
                int x0 = points.get(points.size()-1).x;
                int xn = points.get(0).x;
                int startPoint = predEdge.getStartPoint();
                int endPoint = predEdge.getEndPoint();
                int win = (x0 < xn) ? (startPoint - endPoint) : (endPoint - startPoint);
                score += win;
            }
        }
        for (LayoutEdge succEdge : node.succs) {
            if (reversedLinks.contains(succEdge.link)) {
                List<Point> points = node.reversedLinkStartPoints.get(succEdge.link);
                int x0 = points.get(points.size()-1).x;
                int xn = points.get(0).x;
                int startPoint = succEdge.getStartPoint();
                int endPoint = succEdge.getEndPoint();
                int win = (x0 > xn) ? (startPoint - endPoint) : (endPoint - startPoint);
                score += win;
            }
        }
        return score;
    }


    public void optimizeBackedgeCrossing() {
        for (LayoutNode node : getLayoutNodes()) {
            if (node.reversedLinkStartPoints.isEmpty() && node.reversedLinkEndPoints.isEmpty()) continue;
            int orig_score = getBackedgeCrossingScore(node);
            computeReversedLinkPoints(node, !node.reverseLeft);
            int reverse_score = getBackedgeCrossingScore(node);
            if (orig_score > reverse_score) {
                computeReversedLinkPoints(node, !node.reverseLeft);
            }
        }
    }

    private void tryAlignDummy(int x, LayoutNode dummy) {
        if (x == dummy.x) return;
        LayoutLayer nextLayer = layers[dummy.layer];
        if (dummy.x < x) {
            // try move nextDummyNode.x to the right
            int rightPos = dummy.pos + 1;
            if (rightPos < nextLayer.size()) {
                // we have a right neighbor
                LayoutNode rightNode = nextLayer.get(rightPos);
                int rightShift = x - dummy.x;
                if (dummy.getRightSide() + rightShift <= rightNode.getLeftSide()) {
                    // it is possible to shift nextDummyNode right
                    dummy.x = x;
                }
            } else {
                // nextDummyNode is the right-most node, so we can always move nextDummyNode to the right
                dummy.x = x;
            }
        } else {
            // try move nextDummyNode.x to the left
            int leftPos = dummy.pos - 1;
            if (leftPos >= 0) {
                // we have a left neighbor
                LayoutNode leftNode = nextLayer.get(leftPos);
                int leftShift = dummy.x - x;
                if (leftNode.getRightSide() <= dummy.getLeftSide() - leftShift) {
                    // it is possible to shift nextDummyNode left
                    dummy.x = x;
                }
            } else {
                // nextDummyNode is the left-most node, so we can always move nextDummyNode to the left
                dummy.x = x;
            }
        }
    }

    private void straightenDown(LayoutNode node) {
        if (node.succs.size() == 1) {
            LayoutEdge succEdge = node.succs.get(0);
            LayoutNode succDummy = succEdge.to;
            if (!succDummy.isDummy()) return;
            if (node.isDummy()) {
                tryAlignDummy(node.x, succDummy);
            } else {
                tryAlignDummy(succEdge.getStartPoint(), succDummy);
            }
        }
    }

    private void straightenUp(LayoutNode node) {
        if (node.preds.size() == 1) {
            LayoutEdge predEdge = node.preds.get(0);
            if (predEdge.to.succs.size() != 1) return;
            LayoutNode predDummy = predEdge.from;
            if (!predDummy.isDummy()) return;
            if (node.isDummy()) {
                tryAlignDummy(node.x, predDummy);
            } else {
                tryAlignDummy(predEdge.getEndPoint(), predDummy);
            }
        }
    }

    private void straightenLayer(LayoutLayer layer) {
        for (LayoutNode node : layer) {
            //if (!dummy.isDummy()) continue;
            straightenDown(node);
            //straightenUp(node);
        }
        for (int i = layer.size() - 1; i >= 0; i--) {
            LayoutNode node = layer.get(i);
            //if (!dummy.isDummy()) continue;
            straightenDown(node);
            //straightenUp(node);
        }
    }

    private void straightenEdges() {
        for (LayoutLayer layer : layers) {
            for (int pos = 1; pos < layer.size(); ++pos) {
                LayoutNode leftNode = layer.get(pos - 1);
                LayoutNode rightNode = layer.get(pos);
                assert leftNode.pos + 1 == rightNode.pos;
                assert leftNode.x <= rightNode.x;
                assert leftNode.getRightSide() <= rightNode.getLeftSide();
            }
        }
        for (LayoutLayer layer : layers) {
            straightenLayer(layer);
        }
        for (int i = layers.length - 1; i >= 0; i--) {
            straightenLayer(layers[i]);
        }
    }

    private class AssignXCoordinates {

        int[][] space;
        LayoutNode[][] downProcessingOrder;
        LayoutNode[][] upProcessingOrder;

        private final Comparator<LayoutNode> DUMMY_NODES_FIRST = Comparator.comparing(LayoutNode::isDummy).reversed();

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
                downProcessingOrder[i] = new LayoutNode[layer.size()];
                upProcessingOrder[i] = new LayoutNode[layer.size()];
                int curX = 0;
                for (int j = 0; j < layer.size(); j++) {
                    space[i][j] = curX;
                    LayoutNode node = layer.get(j);
                    curX += node.getWholeWidth() + X_OFFSET;
                    downProcessingOrder[i][j] = node;
                    upProcessingOrder[i][j] = node;
                }
                Arrays.sort(downProcessingOrder[i], NODE_PROCESSING_DOWN_COMPARATOR);
                Arrays.sort(upProcessingOrder[i], NODE_PROCESSING_UP_COMPARATOR);
            }
        }

        private void initialPositions() {
            for (LayoutNode layoutNode : getLayoutNodes()) {
                assert layoutNode.layer >= 0;
                assert layoutNode.layer < layers.length;
                assert layoutNode.pos >= 0;
                assert layoutNode.pos < layers[layoutNode.layer].size();
                layoutNode.x = space[layoutNode.layer][layoutNode.pos];
            }
            for (LayoutNode dummyNode : dummyNodes) {
                assert dummyNode.layer >= 0;
                assert dummyNode.layer < layers.length;
                assert dummyNode.pos >= 0;
                assert dummyNode.pos < layers[dummyNode.layer].size();
                dummyNode.x = space[dummyNode.layer][dummyNode.pos];
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
            optimizeBackedgeCrossing();
            straightenEdges();

            assertOrder();
        }


        private int calculateOptimalDown(LayoutNode node) {
            int size = node.preds.size();
            if (size == 0) {
                return node.x;
            }

            int[] values = new int[size];
            for (int i = 0; i < size; i++) {
                LayoutEdge edge = node.preds.get(i);
                values[i] = edge.getStartPoint() - edge.relativeTo;
            }
            if (!node.isDummy() && size == 1) {
                LayoutNode fromNode = node.preds.get(0).from;
                if (fromNode.succs.size() == 1) {
                    return fromNode.x + ((fromNode.getWholeWidth() - node.getWholeWidth()) / 2);
                }
            }
            return Statistics.median(values);
        }

        private int calculateOptimalUp(LayoutNode node) {
            int size = node.succs.size();
            if (size == 0) {
                return node.x;
            }
            int[] values = new int[size];
            for (int i = 0; i < size; i++) {
                LayoutEdge edge = node.succs.get(i);
                values[i] = edge.getEndPoint() - edge.relativeFrom;
            }
            if (!node.isDummy() && size == 1) {
                LayoutNode toNode = node.succs.get(0).to;
                if (toNode.preds.size() == 1) {
                    return toNode.x + ((toNode.getWholeWidth() - node.getWholeWidth()) / 2);
                }
            }
            return Statistics.median(values);
        }

        private void processRow(int[] space, LayoutNode[] processingOrder) {
            Arrays.sort(processingOrder, DUMMY_NODES_THEN_OPTMMAL_X);
            TreeSet<LayoutNode> treeSet = new TreeSet<>(Comparator.comparingInt(n -> n.pos));
            for (LayoutNode node : processingOrder) {
                int minX = Integer.MIN_VALUE;
                SortedSet<LayoutNode> headSet = treeSet.headSet(node, false);
                if (!headSet.isEmpty()) {
                    LayoutNode leftNeighbor = headSet.last();
                    minX = leftNeighbor.getLeftBorder() + space[node.pos] - space[leftNeighbor.pos];
                }

                int maxX = Integer.MAX_VALUE;
                SortedSet<LayoutNode> tailSet = treeSet.tailSet(node, false);
                if (!tailSet.isEmpty()) {
                    LayoutNode rightNeighbor = tailSet.first();
                    maxX = rightNeighbor.getLeftBorder() + space[node.pos] - space[rightNeighbor.pos];
                }

                assert minX <= maxX : minX + " vs " + maxX;
                int x = node.optimal_x;
                if (x < minX) {
                    x = minX;
                } else if (x > maxX) {
                    x = maxX;
                }
                node.x = x;
                treeSet.add(node);
            }
        }

        private void sweepUp() {
            for (int i = layers.length - 2; i >= 0; i--) {
                for (LayoutNode node : upProcessingOrder[i]) {
                    node.optimal_x = calculateOptimalUp(node);
                }
                processRow(space[i], upProcessingOrder[i]);
            }
        }

        private void sweepDown() {
            for (int i = 1; i < layers.length; i++) {
                for (LayoutNode node : downProcessingOrder[i]) {
                    node.optimal_x = calculateOptimalDown(node);
                }
                processRow(space[i], downProcessingOrder[i]);
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

        private void updateLayerHeight(LayoutLayer layer) {
            int maxLayerHeight = 0;
            for (LayoutNode layoutNode : layer) {
                if (!layoutNode.isDummy()) {
                    // center the node
                    int offset = Math.max(layoutNode.topYOffset, layoutNode.bottomYOffset);
                    layoutNode.topYOffset = offset;
                    layoutNode.bottomYOffset = offset;
                }
                maxLayerHeight = Math.max(maxLayerHeight, layoutNode.getWholeHeight());
            }
            layer.height = maxLayerHeight;
        }

        private double getScaledLayerPadding(LayoutLayer layer) {
            int maxXOffset = 0;

            for (LayoutNode layoutNode : layer) {
                for (LayoutEdge succEdge : layoutNode.succs) {
                    maxXOffset = Math.max(Math.abs(succEdge.getStartPoint() - succEdge.getEndPoint()), maxXOffset);
                }
            }

            return SCALE_LAYER_PADDING * Math.max((int) (Math.sqrt(maxXOffset) * 2), LAYER_OFFSET * 3);
        }

        private void run() {
            int currentY = 0;
            for (LayoutLayer layer : layers) {
                layer.y = currentY;
                updateLayerHeight(layer);
                for (LayoutNode layoutNode : layer) {
                    layoutNode.y = currentY + (layer.height - layoutNode.getWholeHeight()) / 2;
                }
                currentY += layer.height + getScaledLayerPadding(layer);
            }

            assertYLayer();
        }
    }

    private class WriteResult {

        private HashMap<Vertex, Point> computeVertexPositions() {
            HashMap<Vertex, Point> vertexPositions = new HashMap<>();
            for (Map.Entry<Vertex, LayoutNode> entry : vertexToLayoutNode.entrySet()) {
                Vertex v = entry.getKey();
                LayoutNode n = entry.getValue();
                vertexPositions.put(v, new Point(n.getLeftSide(), n.getTop()));
            }
            return vertexPositions;
        }

        private HashMap<Link, List<Point>> computeLinkPositions() {
            HashMap<Link, List<Point>> linkToSplitEndPoints = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();

            for (LayoutNode layoutNode : getLayoutNodes()) {
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
                        for (Point relativeEnd : toNode.reversedLinkEndPoints.get(predEdge.link)) {
                            Point endPoint = new Point(toNode.getLeftSide() + relativeEnd.x,  toNode.getTop() + relativeEnd.y);
                            linkPoints.add(0, endPoint);
                        }

                        if (!fromNode.isDummy()) {
                            if (fromNode.reversedLinkStartPoints.containsKey(predEdge.link)) {
                                for (Point relativeStart : fromNode.reversedLinkStartPoints.get(predEdge.link)) {
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


            for (LayoutNode layoutNode : getLayoutNodes()) {
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

                        if (fromNode.reversedLinkStartPoints.containsKey(succEdge.link)) {
                            for (Point relativeStart : fromNode.reversedLinkStartPoints.get(succEdge.link)) {
                                Point startPoint = new Point( fromNode.getLeftSide() + relativeStart.x, fromNode.getTop() + relativeStart.y );
                                linkPoints.add(startPoint);
                            }
                        }

                        if (!toNode.isDummy()) {
                            if (toNode.reversedLinkEndPoints.containsKey(succEdge.link)) {
                                for (Point relativeEnd : toNode.reversedLinkEndPoints.get(succEdge.link)) {
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
            // takes dummyNodes, layers, vertexToLayoutNode and reversedLinks
            assert dummyNodes.size() == (new HashSet<>(dummyNodes)).size();

            assertEdgesConnected();
            assertOrder();

            // takes vertexToLayoutNode
            HashMap<Vertex, Point> vertexPositions = computeVertexPositions();
            assertOrder();

            // takes vertexToLayoutNode and reversedLinks
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

            for (LayoutNode layoutNode : getLayoutNodes()) {
                minX = Math.min(minX, layoutNode.x);
                minY = Math.min(minY, layoutNode.y);
            }
            for (LayoutNode dummyNode : dummyNodes) {
                minX = Math.min(minX, dummyNode.x);
                minY = Math.min(minY, dummyNode.y);
            }

            for (LayoutLayer layer : layers) {
                minY = Math.min(minY, layer.y);
            }

            for (LayoutNode layoutNode : getLayoutNodes()) {
                layoutNode.x -= minX;
                assert layoutNode.x >= 0;
                layoutNode.y -= minY;
                assert layoutNode.y >= 0;
            }
            for (LayoutNode dummyNode : dummyNodes) {
                dummyNode.x -= minX;
                assert dummyNode.x >= 0;
                dummyNode.y -= minY;
                assert dummyNode.y >= 0;
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

    private void assertNodePos() {
        for (LayoutNode n : getLayoutNodes()) {
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
        for (LayoutNode n : dummyNodes) {
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
        for (LayoutNode layoutNode : getLayoutNodes()) {
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

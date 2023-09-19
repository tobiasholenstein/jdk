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
    public static final int LAYER_OFFSET = 4;
    public final int OFFSET = X_OFFSET + DUMMY_WIDTH;
    public static final double SCALE_LAYER_PADDING = 1.5;


    // Options
    private final int maxLayerLength;
    // Algorithm global datastructures
    private final Set<Link> reversedLinks;
    private final List<LayoutNode> allNodes;
    private final HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private final HashMap<Link, List<Point>> reversedLinkStartPoints;
    private final HashMap<Link, List<Point>> reversedLinkEndPoints;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;

    private void assertOrder() {
        for (LayoutLayer layer : layers) {
            for (LayoutNode node : layer) {
                assert allNodes.contains(node);
            }
            for (int pos = 1; pos < layer.size(); ++pos) {
                LayoutNode leftNode = layer.get(pos-1);
                LayoutNode rightNode = layer.get(pos);
                assert leftNode.pos + 1 == rightNode.pos;
                assert leftNode.x <= rightNode.x;
            }
        }
    }

    private void addNode(LayoutNode node) {
        LayoutLayer layer = layers[node.layer];
        int pos = node.pos;

        // update pos of the nodes right (and including) of pos
        for (LayoutNode n : layer) {
            if (n.pos >= pos) {
                n.pos += 1;
                n.x += node.getWholeWidth() + X_OFFSET;
            }
        }

        // insert in layer at pos
        if (pos < layer.size()) {
            layer.add(pos, node);
        } else {
            layer.add(node);
        }

        // adjust Y of movedNode
        node.y = layer.y;

        allNodes.add(node);
    }

    private void removeNode(LayoutNode node) {
        int layer = node.layer;
        int pos = node.pos;
        layers[layer].remove(node);

        // Update position of remaining nodes on the same layer
        for (LayoutNode n : layers[layer]) {
            if (n.pos > pos) {
                n.pos -= 1;
                n.x -= node.getWholeWidth() + X_OFFSET;
            }
        }

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

    private int findPosInLayer(LayoutLayer layer, int xLoc) {
        // find the position in the new layer at location
        int newPos = 0;
        for (int j = 1; j < layer.size(); j++) {
            LayoutNode leftNode = layer.get(j-1);
            LayoutNode rightNode = layer.get(j);
            if (xLoc < leftNode.getRightSide()) {
                newPos = leftNode.pos;
                break;
            } else if (xLoc < rightNode.getRightSide()) {
                newPos = rightNode.pos;
                break;
            } else {
                newPos = rightNode.pos + 1;
            }
        }
        return newPos;
    }

    public void moveFigureTo(Vertex movedVertex, Point loc) {
        LayoutNode movedNode = vertexToLayoutNode.get(movedVertex);
        Point newLocation = new Point(loc.x, loc.y + movedNode.height/2);

        for (int i = 0; i < layerCount; i++) {
            LayoutLayer newLayer = layers[i];
            assert newLayer.size()>0;

            if (newLayer.y <= newLocation.y && newLocation.y <= newLayer.getBottom()) {

                if (movedNode.layer == i) { // we move the node in the same layer
                    int newX = newLocation.x;
                    int leftBound = Integer.MIN_VALUE;
                    int rightBound = Integer.MAX_VALUE;
                    if (movedNode.pos > 0) {
                        LayoutNode leftNode = newLayer.get(movedNode.pos-1);
                        leftBound = leftNode.getRightSide();
                    }
                    if (movedNode.pos < newLayer.size()-1) {
                        LayoutNode rightNode = newLayer.get(movedNode.pos+1);
                        rightBound = rightNode.getLeftSide();
                    }

                    // the node did not change position withing the layer
                    if (leftBound < newX && newX < rightBound) {
                        newX = Math.max(newX, leftBound + X_OFFSET);
                        newX = Math.min(newX, rightBound - X_OFFSET - movedNode.getWholeWidth());
                        // same layer and position, just adjust x pos
                        movedNode.x = newX;
                        assertOrder();
                        new WriteResult().run();
                        assertOrder();
                        return;
                    }

                } else { // only remove links if we moved the node to a new layer
                    for (Link inputLink : graph.getInputLinks(movedVertex)) {
                        applyRemoveLinkAction(inputLink);
                    }
                    for (Link inputLink : graph.getOutputLinks(movedVertex)) {
                        applyRemoveLinkAction(inputLink);
                    }
                }

                int newPos = findPosInLayer(newLayer, newLocation.x);

                // remove from old layer and update positions in old layer
                removeNode(movedNode);

                // set x of movedNode
                movedNode.x = newLocation.x;

                if (movedNode.layer != i) { // insert into a different layer
                    movedNode.layer = i;
                    movedNode.pos = newPos;
                } else { // move within the same layer
                    assert movedNode.pos != newPos; // handled before
                    if (movedNode.pos < newPos) { // moved to the right
                        // adjust because we have already removed movedNode in this layer
                        movedNode.pos = newPos - 1;
                    } else { // moved to the left
                        movedNode.pos = newPos;
                    }
                }
                addNode(movedNode);
                break;
            }
        }

        for (LayoutLayer layer : layers) {
            for (LayoutNode node : layer) {
                assert allNodes.contains(node);
            }
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
        public int xOffset;
        public int yOffset;
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
            return xOffset + x;
        }

        public int getWholeWidth() {
            return xOffset + width;
        }

        public int getWholeHeight() {
            return height + yOffset + bottomYOffset;
        }

        public int getRightSide() {
            return x + getWholeWidth();
        }

        public int getCenterX() {
            return getLeftSide() + (width / 2);
        }

        public int getBottom() {
            return y + height;
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
        maxLayerLength = 8; //-1;

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

        // #############################################################
        // STEP 3: Assign layers
        // - sets LayoutNode.layer
        new AssignLayers().run();

        // #############################################################
        // STEP 4: Create dummy nodes
        // - takes List<LayoutNode> nodes
        // - replaces single LayoutEdge between LayoutNodes with a chain of new LayoutEdges and dummy LayoutNodes
        // - adds the new dummy LayoutNodes to nodes
        new CreateDummyNodes().run();

        // #############################################################
        // STEP 5: Crossing Reduction
        // creates layers = new LayoutLayer[layerCount];
        // - sets for each LayoutNode n,und n.pos
        new CrossingReduction().run();
        assertOrder();

        // #############################################################
        // STEP 6: Assign X coordinates
        // for all LayoutNode n in nodes
        // - sets n.x =
        new AssignXCoordinates().run();
        assertOrder();

        // #############################################################
        // STEP 7: Assign Y coordinates
        // sets for LayoutLayer layer: layer.y;
        // for each LayoutNode n
        //  - node.y
        //  - node.yOffset
        //  - node.bottomYOffset
        new AssignYCoordinates().run();
        assertOrder();

        // #############################################################
        // STEP 8: Write back to interface
        new WriteResult().run();
        assertOrder();
    }

    private class BuildDatastructure {

        private final Comparator<Link> LINK_COMPARATOR =
                Comparator.comparing((Link l) -> l.getFrom().getVertex())
                        .thenComparing(l -> l.getTo().getVertex())
                        .thenComparingInt(l -> l.getFrom().getRelativePosition().x)
                        .thenComparingInt(l -> l.getTo().getRelativePosition().x);

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
            List<? extends Link> links = new ArrayList<>(graph.getLinks());

            links.sort(LINK_COMPARATOR);
            for (Link l : links) {
                LayoutEdge edge = new LayoutEdge(
                        vertexToLayoutNode.get(l.getFrom().getVertex()),
                        vertexToLayoutNode.get(l.getTo().getVertex()),
                        l.getFrom().getRelativePosition().x,
                        l.getTo().getRelativePosition().x,
                        l);
                edge.from.succs.add(edge);
                edge.to.preds.add(edge);
            }
        }
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
                    boolean ok = true;
                    for (LayoutEdge e : node.preds) {
                        if (e.from.vertex.isRoot()) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        reverseAllInputs(node);
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
            DFS();
            resolveInsOuts();
        }

        private void resolveInsOuts() {

            for (LayoutNode node : allNodes) {
                SortedSet<Integer> reversedDown = new TreeSet<>();

                for (LayoutEdge e : node.succs) {
                    if (reversedLinks.contains(e.link)) {
                        reversedDown.add(e.relativeFrom);
                    }
                }

                SortedSet<Integer> reversedUp;
                if (reversedDown.isEmpty()) {
                    reversedUp = new TreeSet<>(Collections.reverseOrder());
                } else {
                    reversedUp = new TreeSet<>();
                }

                for (LayoutEdge e : node.preds) {
                    if (reversedLinks.contains(e.link)) {
                        reversedUp.add(e.relativeTo);
                    }
                }

                int cur = -reversedDown.size() * OFFSET;
                int curWidth = node.width + reversedDown.size() * OFFSET;
                for (int pos : reversedDown) {
                    ArrayList<LayoutEdge> reversedSuccs = new ArrayList<>();
                    for (LayoutEdge e : node.succs) {
                        if (e.relativeFrom == pos && reversedLinks.contains(e.link)) {
                            reversedSuccs.add(e);
                            e.relativeFrom = curWidth;
                        }
                    }

                    ArrayList<Point> startPoints = new ArrayList<>();
                    startPoints.add(new Point(curWidth, cur));
                    startPoints.add(new Point(pos, cur));
                    startPoints.add(new Point(pos, cur));
                    startPoints.add(new Point(pos, 0));
                    for (LayoutEdge e : reversedSuccs) {
                        reversedLinkStartPoints.put(e.link, startPoints);
                    }

                    cur += OFFSET;
                    node.yOffset += OFFSET;
                    curWidth -= OFFSET;
                }
                node.width += reversedDown.size() * OFFSET;

                if (reversedDown.isEmpty()) {
                    cur = OFFSET;
                } else {
                    cur = -OFFSET;
                }
                int bottomYOffset = 0;

                for (int pos : reversedUp) {
                    ArrayList<LayoutEdge> reversedPreds = new ArrayList<>();
                    for (LayoutEdge e : node.preds) {
                        if (e.relativeTo == pos && reversedLinks.contains(e.link)) {
                            if (reversedDown.isEmpty()) {
                                e.relativeTo = node.width + OFFSET;
                            } else {
                                e.relativeTo = cur;
                            }

                            reversedPreds.add(e);
                        }
                    }
                    bottomYOffset += OFFSET;
                    ArrayList<Point> endPoints = new ArrayList<>();

                    node.width += OFFSET;
                    if (reversedDown.isEmpty()) {
                        endPoints.add(new Point(node.width, node.height + bottomYOffset));
                    } else {
                        endPoints.add(new Point(cur, node.height + bottomYOffset));
                        cur -= OFFSET;
                    }

                    endPoints.add(new Point(pos, node.height + bottomYOffset));
                    endPoints.add(new Point(pos, node.height + bottomYOffset));
                    endPoints.add(new Point(pos, node.height));
                    for (LayoutEdge e : reversedPreds) {
                        reversedLinkEndPoints.put(e.link, endPoints);
                    }
                }

                if (!reversedDown.isEmpty()) {
                    node.xOffset = reversedUp.size() * OFFSET;
                }
            }
        }

        private void reverseEdge(LayoutEdge e) {
            assert !reversedLinks.contains(e.link);
            reversedLinks.add(e.link);

            LayoutNode oldFrom = e.from;
            LayoutNode oldTo = e.to;
            int oldRelativeFrom = e.relativeFrom;
            int oldRelativeTo = e.relativeTo;

            e.from = oldTo;
            e.to = oldFrom;
            e.relativeFrom = oldRelativeTo;
            e.relativeTo = oldRelativeFrom;

            oldFrom.succs.remove(e);
            oldFrom.preds.add(e);
            oldTo.preds.remove(e);
            oldTo.succs.add(e);

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

        private void DFS() {
            allNodes.forEach(this::DFS);
        }

        private void reverseAllInputs(LayoutNode node) {
            for (LayoutEdge e : node.preds) {
                assert !reversedLinks.contains(e.link);
                reversedLinks.add(e.link);
                node.succs.add(e);
                e.from.preds.add(e);
                e.from.succs.remove(e);
                int oldRelativeFrom = e.relativeFrom;
                int oldRelativeTo = e.relativeTo;
                e.to = e.from;
                e.from = node;
                e.relativeFrom = oldRelativeTo;
                e.relativeTo = oldRelativeFrom;
            }
            node.preds.clear();
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

    private class CreateDummyNodes {


        public void createDummiesForNode(LayoutNode layoutNode) {
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
                }
            }
        }

        private void run() {
            ArrayList<LayoutNode> currentNodes = new ArrayList<>(allNodes);
            for (LayoutNode layoutNode : currentNodes) {
                assert !layoutNode.isDummy();
                createDummiesForNode(layoutNode);
            }

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
    }

    private class CrossingReduction {

        private final Comparator<LayoutNode> CROSSING_NODE_COMPARATOR = (n1, n2) -> Float.compare(n1.crossingNumber, n2.crossingNumber);

        public CrossingReduction() {}

        @SuppressWarnings({"unchecked"})
        private void createLayers() {
            layers = new LayoutLayer[layerCount];
            for (int i = 0; i < layerCount; i++) {
                layers[i] = new LayoutLayer();
            }

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

            for (int i = 0; i < layers.length - 1; i++) {
                for (LayoutNode n : layers[i]) {
                    for (LayoutEdge e : n.succs) {
                        if (!visited.contains(e.to)) {
                            visited.add(e.to);
                            layers[i + 1].add(e.to);
                        }
                    }
                }
            }
        }

        private void run() {
            createLayers();
            for (int i = 0; i < CROSSING_ITERATIONS; i++) {
                downSweep();
                upSweep();
            }
            downSweep();
            updatePositions();
        }


        private void updateXOfLayer(int index) {
            int x = 0;
            for (LayoutNode n : layers[index]) {
                n.x = x;
                x += n.getWholeWidth() + OFFSET;
            }
        }

        private void updatePositions() {
            for (int i = 0; i < layerCount; ++i) {
                updateLayerPositions(i);
            }
        }

        private void updateLayerPositions(int index) {
            int pos = 0;
            for (LayoutNode n : layers[index]) {
                n.pos = pos;
                pos++;
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
            }
        }

        private void initialPositions() {
            for (LayoutNode n : allNodes) {
                n.x = space[n.layer][n.pos];
            }
        }



        private void run() {
            //assertOrder();
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

    private class AssignYCoordinates {

        private void run() {
            int curY = 0;
            for (LayoutLayer layer : layers) {
                int layerHeight = layer.height;
                layer.y = curY;
                int maxXOffset = 0;
                for (LayoutNode n : layer) {
                    n.y = curY;
                    if (!n.isDummy()) {
                        n.yOffset = (layerHeight - n.getWholeHeight()) / 2 + n.yOffset;
                        n.y += n.yOffset;
                        n.bottomYOffset = layerHeight - n.yOffset - n.height;
                    }
                    for (LayoutEdge e : n.succs) {
                        maxXOffset = Math.max(Math.abs(e.getStartPoint() - e.getEndPoint()), maxXOffset);
                    }
                }
                curY += layerHeight + SCALE_LAYER_PADDING * Math.max((int) (Math.sqrt(maxXOffset) * 2), LAYER_OFFSET * 3);
            }
        }
    }

    private class WriteResult {

        private HashMap<Vertex, Point> computeVertexPositions() {
            HashMap<Vertex, Point> vertexPositions = new HashMap<>();
            for (Vertex v : graph.getVertices()) {
                LayoutNode n = vertexToLayoutNode.get(v);
                vertexPositions.put(v, new Point(n.getLeftSide(), n.y));
            }
            return vertexPositions;
        }

        private HashMap<Link, List<Point>> computeLinkPositions() {
            HashMap<Link, List<Point>> linkToSplitStartPoints = new HashMap<>();
            HashMap<Link, List<Point>> linkToSplitEndPoints = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();

            for (LayoutNode layoutNode : allNodes) {
                for (LayoutEdge predEdge : layoutNode.preds) {
                    if (predEdge.link != null) {
                        ArrayList<Point> linkPoints = new ArrayList<>();
                        linkPoints.add(new Point(predEdge.getEndPoint(), predEdge.to.y));
                        linkPoints.add(new Point(predEdge.getEndPoint(), layers[predEdge.to.layer].y - LAYER_OFFSET));

                        LayoutNode fromNode = predEdge.from;
                        LayoutNode toNode = predEdge.to;
                        LayoutEdge curEdge = predEdge;
                        while (fromNode.isDummy() && !fromNode.preds.isEmpty()) {
                            linkPoints.add(new Point(fromNode.getCenterX(), layers[fromNode.layer].getBottom() + LAYER_OFFSET));
                            linkPoints.add(new Point(fromNode.getCenterX(), fromNode.y - LAYER_OFFSET));
                            curEdge = fromNode.preds.get(0);
                            fromNode = curEdge.from;
                        }
                        linkPoints.add(new Point(curEdge.getStartPoint(), layers[fromNode.layer].getBottom() + LAYER_OFFSET));
                        linkPoints.add(new Point(curEdge.getStartPoint(), fromNode.getBottom()));
                        Collections.reverse(linkPoints);

                        if (fromNode.isDummy()) { // a dummy node without predecessors is a split end-point
                            if (reversedLinkEndPoints.containsKey(predEdge.link)) {
                                for (Point endPoint : reversedLinkEndPoints.get(predEdge.link)) {
                                    linkPoints.add(new Point(endPoint.x + toNode.getLeftSide(), endPoint.y + toNode.y));
                                }
                            }

                            if (linkToSplitStartPoints.containsKey(predEdge.link)) {
                                linkPoints.add(0, null);
                                linkPoints.addAll(0, linkToSplitStartPoints.get(predEdge.link));

                                if (reversedLinks.contains(predEdge.link)) {
                                    Collections.reverse(linkPoints);
                                }
                                linkPositions.put(predEdge.link, linkPoints);
                            } else {
                                linkToSplitEndPoints.put(predEdge.link, linkPoints);
                            }

                        } else {
                            if (reversedLinks.contains(predEdge.link)) {
                                Collections.reverse(linkPoints);
                                if (reversedLinkStartPoints.containsKey(predEdge.link)) {
                                    for (Point startPoint : reversedLinkStartPoints.get(predEdge.link)) {
                                        linkPoints.add(new Point(startPoint.x + fromNode.getLeftSide(), startPoint.y + fromNode.y));
                                    }
                                }

                                if (reversedLinkEndPoints.containsKey(predEdge.link)) {
                                    for (Point endPoint : reversedLinkEndPoints.get(predEdge.link)) {
                                        linkPoints.add(0, new Point(endPoint.x + toNode.getLeftSide(), endPoint.y + toNode.y));
                                    }
                                }
                            }
                            linkPositions.put(predEdge.link, linkPoints);
                        }

                        // No longer needed!
                        predEdge.link = null;
                        //visited_edges.add(e.link);
                    }
                }

                for (LayoutEdge succEdge : layoutNode.succs) {
                    if (succEdge.link != null) {
                        ArrayList<Point> points = new ArrayList<>();
                        points.add(new Point(succEdge.getStartPoint(), succEdge.from.getBottom()));
                        points.add(new Point(succEdge.getStartPoint(), layers[succEdge.from.layer].getBottom() + LAYER_OFFSET));

                        LayoutNode cur = succEdge.to;
                        LayoutNode other = succEdge.from;
                        LayoutEdge curEdge = succEdge;
                        while (cur.isDummy() && !cur.succs.isEmpty()) {
                            points.add(new Point(cur.getCenterX(), layers[cur.layer].y - LAYER_OFFSET));
                            points.add(new Point(cur.getCenterX(), layers[cur.layer].getBottom() + LAYER_OFFSET));

                            curEdge = cur.succs.get(0);
                            cur = curEdge.to;
                        }

                        points.add(new Point(curEdge.getEndPoint(), layers[cur.layer].y - LAYER_OFFSET));
                        points.add(new Point(curEdge.getEndPoint(), cur.y));

                        if (cur.succs.isEmpty() && cur.isDummy()) {
                            if (reversedLinkStartPoints.containsKey(succEdge.link)) {
                                for (Point p1 : reversedLinkStartPoints.get(succEdge.link)) {
                                    points.add(0, new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                }
                            }

                            if (linkToSplitEndPoints.containsKey(succEdge.link)) {
                                points.add(null);
                                points.addAll(linkToSplitEndPoints.get(succEdge.link));

                                if (reversedLinks.contains(succEdge.link)) {
                                    Collections.reverse(points);
                                }
                                assert !linkPositions.containsKey(succEdge.link);
                                linkPositions.put(succEdge.link, points);
                            } else {
                                linkToSplitStartPoints.put(succEdge.link, points);
                            }
                        } else {
                            if (reversedLinks.contains(succEdge.link)) {
                                if (reversedLinkStartPoints.containsKey(succEdge.link)) {
                                    for (Point p1 : reversedLinkStartPoints.get(succEdge.link)) {
                                        points.add(0, new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                    }
                                }
                                if (reversedLinkEndPoints.containsKey(succEdge.link)) {
                                    for (Point p1 : reversedLinkEndPoints.get(succEdge.link)) {
                                        points.add(new Point(p1.x + cur.getLeftSide(), p1.y + cur.y));
                                    }
                                }
                                Collections.reverse(points);
                            }
                            linkPositions.put(succEdge.link, points);
                        }

                        succEdge.link = null;
                        //visited_edges.add(e.link);

                    }
                }
            }
            return linkPositions;
        }

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


        public void run() {
            assertLinks();
            assertOrder();

            HashMap<Vertex, Point> vertexPositions = computeVertexPositions();
            assertOrder();

            HashMap<Link, List<Point>> linkPositions = computeLinkPositions();
            assertOrder();


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
                node.x -= minX;
                assert node.x>=0;
                node.y -= minY;
                assert node.y>=0;
            }

            assertOrder();


            for (LayoutLayer layer : layers) {
                layer.y -= minY;
            }

            assertOrder();


            // shift vertices by minX/minY
            for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
                Point point = entry.getValue();
                point.x -= minX;
                point.y -= minY;
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
                    }
                }

                // write points back to links
                link.setControlPoints(points);
            }

            assertOrder();

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
                minX = leftNeighbor.getRightSide() + offset(leftNeighbor, n);
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

}

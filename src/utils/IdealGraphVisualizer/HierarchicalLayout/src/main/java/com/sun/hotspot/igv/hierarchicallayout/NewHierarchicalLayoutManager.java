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
import com.sun.hotspot.igv.layout.Port;
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



    // Options
    private final int maxLayerLength;
    // Algorithm global datastructures
    private final Set<Link> reversedLinks;
    private final List<LayoutNode> nodes;
    private final HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private final HashMap<Link, List<Point>> reversedLinkStartPoints;
    private final HashMap<Link, List<Point>> reversedLinkEndPoints;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;

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
        nodes.remove(node);
    }


    private void removeSuccDummyNodes(LayoutNode node) {
        if (node.isDummy()) {
            for (LayoutEdge succEdge : node.succs) {
                removeSuccDummyNodes(succEdge.to);
            }
            removeNode(node);
        }
    }

    private void removePredDummyNodes(LayoutNode node) {
        if (node.isDummy()) {
            for (LayoutEdge predEdge : node.preds) {
                removePredDummyNodes(predEdge.from);
            }
            removeNode(node);
        }
    }

    public void moveFigureTo(Vertex movedVertex, Point newLocation) {
        LayoutNode movedNode = vertexToLayoutNode.get(movedVertex);
        System.out.println("[old] layer " + movedNode.layer);
        System.out.print(" at pos " + movedNode.pos);

        removeSuccDummyNodes(movedNode);
        removePredDummyNodes(movedNode);

        for (int i = 0; i < layerCount; i++) {
            LayoutLayer newLayer = layers[i];
            if (newLayer.y <= newLocation.y && newLocation.y <= newLayer.getBottom()) {

                // find the position in the new layer
                int newPos = 0;
                for (int j = 0; j < newLayer.size()-1; j++) {
                    LayoutNode n = newLayer.get(j);
                    LayoutNode nNext = newLayer.get(j+1);
                    if (n.getLeftSide() <= newLocation.x && newLocation.x <= n.getRightSide()) {
                        newPos = n.pos;
                        break;
                    } else if (n.getRightSide() <= newLocation.x && newLocation.x <= nNext.getLeftSide()) {
                        newPos = nNext.pos;
                        break;
                    } else if (nNext.getRightSide() <=newLocation.x) {
                        newPos = nNext.pos + 1;
                    }
                }

                // remove from old layer and update positions in old layer
                removeNode(movedNode);

                // set the new layer
                movedNode.layer = i;

                // insert into newPos in newLayer and update pos of the other nodes
                for (LayoutNode n : newLayer) {
                    if (n.pos >= newPos) {
                        n.pos += 1;
                        n.x += movedNode.getWholeWidth() + X_OFFSET;
                    }
                }
                movedNode.pos = newPos;
                newLayer.add(newPos, movedNode);

                System.out.print("   -> Move to layer " + i);
                System.out.println(" at pos " + newPos);
                break;
            }
        }


        // remove link connected to movedNode
        for (Link link : graph.getLinks()) {
            if (link.getTo().getVertex()==movedNode.vertex) {
                link.setControlPoints(new ArrayList<>());
            } else if (link.getFrom().getVertex()==movedNode.vertex) {
                link.setControlPoints(new ArrayList<>());
            }
        }


        // adjust Y of movedNode
        movedNode.y = layers[movedNode.layer].y;

        // set X of movedNode
        movedNode.x = newLocation.x;

        for (LayoutLayer layer : layers) {
            for (LayoutNode n : layer) {
                if (!n.isDummy()) {
                    n.vertex.setPosition(new Point(n.x, n.y));
                }
            }
        }

        new WriteResult().run();


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
        maxLayerLength = -1;

        vertexToLayoutNode = new HashMap<>();
        reversedLinks = new HashSet<>();
        reversedLinkStartPoints = new HashMap<>();
        reversedLinkEndPoints = new HashMap<>();
        nodes = new ArrayList<>();
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

        // #############################################################
        // STEP 6: Assign X coordinates
        // for all LayoutNode n in nodes
        // - sets n.x =
        new AssignXCoordinates().run();

        // #############################################################
        // STEP 7: Assign Y coordinates
        // sets for LayoutLayer layer: layer.y;
        // for each LayoutNode n
        //  - node.y
        //  - node.yOffset
        //  - node.bottomYOffset
        new AssignYCoordinates().run();

        // #############################################################
        // STEP 8: Write back to interface
        new WriteResult().run();
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
            nodes.clear();

            // Set up nodes
            for (Vertex v : graph.getVertices()) {
                LayoutNode node = new LayoutNode(v);
                nodes.add(node);
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
            for (LayoutNode node : nodes) {
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
            for (LayoutNode node : nodes) {
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

            for (LayoutNode node : nodes) {
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
            for (LayoutNode node : nodes) {
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
            nodes.forEach(this::DFS);
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
            for (LayoutNode node : nodes) {
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
            for (LayoutNode node : nodes) {
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
            for (LayoutNode n : nodes) {
                n.layer = (layerCount - 1 - n.layer);
            }
        }
    }

    private class CreateDummyNodes {


        public void createDummiesForNode(LayoutNode n) {
            HashMap<Integer, List<LayoutEdge>> portHash = new HashMap<>();
            ArrayList<LayoutEdge> succs = new ArrayList<>(n.succs);
            HashMap<Integer, LayoutNode> topNodeHash = new HashMap<>();
            HashMap<Integer, HashMap<Integer, LayoutNode>> bottomNodeHash = new HashMap<>();
            for (LayoutEdge e : succs) {
                assert e.from.layer < e.to.layer;
                if (e.from.layer != e.to.layer - 1) {
                    if (maxLayerLength != -1 && e.to.layer - e.from.layer > maxLayerLength) {
                        assert maxLayerLength > 2;
                        e.to.preds.remove(e);
                        e.from.succs.remove(e);

                        LayoutNode topNode = topNodeHash.get(e.relativeFrom);
                        if (topNode == null) {
                            topNode = new LayoutNode();
                            topNode.layer = e.from.layer + 1;
                            nodes.add(topNode);
                            topNodeHash.put(e.relativeFrom, topNode);
                            bottomNodeHash.put(e.relativeFrom, new HashMap<>());
                        }
                        assert e.link != null;
                        LayoutEdge topEdge = new LayoutEdge(e.from, topNode, e.relativeFrom, topNode.width / 2, e.link);
                        e.from.succs.add(topEdge);
                        topNode.preds.add(topEdge);
                        assert topNode.isDummy();

                        HashMap<Integer, LayoutNode> hash = bottomNodeHash.get(e.relativeFrom);

                        LayoutNode bottomNode;
                        if (hash.containsKey(e.to.layer)) {
                            bottomNode = hash.get(e.to.layer);
                        } else {
                            bottomNode = new LayoutNode();
                            bottomNode.layer = e.to.layer - 1;
                            nodes.add(bottomNode);
                            hash.put(e.to.layer, bottomNode);
                        }
                        assert e.link != null;
                        LayoutEdge bottomEdge = new LayoutEdge(bottomNode, e.to, bottomNode.width / 2, e.relativeTo, e.link);
                        e.to.preds.add(bottomEdge);
                        bottomNode.succs.add(bottomEdge);
                        assert bottomNode.isDummy();

                    } else {
                        Integer i = e.relativeFrom;
                        if (!portHash.containsKey(i)) {
                            portHash.put(i, new ArrayList<>());
                        }
                        portHash.get(i).add(e);
                    }
                }
            }
            for (LayoutEdge succEdge : succs) {
                assert succEdge.link != null;
            }
            for (LayoutEdge succEdge : succs) {
                Integer i = succEdge.relativeFrom;
                if (portHash.containsKey(i)) {

                    List<LayoutEdge> list = portHash.get(i);
                    list.sort(Comparator.comparingInt(e -> e.to.layer));

                    if (list.size() == 1) {
                        processSingleEdge(list.get(0));
                    } else {

                        int maxLayer = list.get(list.size() - 1).to.layer;

                        int cnt = maxLayer - n.layer - 1;
                        LayoutEdge[] edges = new LayoutEdge[cnt];
                        LayoutNode[] nodes = new LayoutNode[cnt];

                        nodes[0] = new LayoutNode();
                        nodes[0].layer = n.layer + 1;
                        edges[0] = new LayoutEdge(n, nodes[0], i, nodes[0].width / 2, null);
                        nodes[0].preds.add(edges[0]);
                        assert nodes[0].isDummy();
                        n.succs.add(edges[0]);
                        for (int j = 1; j < cnt; j++) {
                            nodes[j] = new LayoutNode();
                            nodes[j].layer = n.layer + j + 1;
                            edges[j] = new LayoutEdge(nodes[j - 1], nodes[j], nodes[j - 1].width / 2, nodes[j].width / 2, null);
                            nodes[j - 1].succs.add(edges[j]);
                            nodes[j].preds.add(edges[j]);
                            assert nodes[j].isDummy();
                        }
                        for (LayoutEdge curEdge : list) {
                            assert curEdge.to.layer - n.layer - 2 >= 0;
                            assert curEdge.to.layer - n.layer - 2 < cnt;
                            LayoutNode anchor = nodes[curEdge.to.layer - n.layer - 2];
                            for (int l = 0; l < curEdge.to.layer - n.layer - 1; ++l) {
                                anchor = nodes[l];
                            }
                            anchor.succs.add(curEdge);
                            curEdge.from = anchor;
                            curEdge.relativeFrom = anchor.width / 2;
                            n.succs.remove(curEdge);
                        }
                    }
                    portHash.remove(i);
                }
            }
        }


        private void run() {
            ArrayList<LayoutNode> currentNodes = new ArrayList<>(nodes);
            for (LayoutNode n : currentNodes) {
                createDummiesForNode(n);
            }
        }

        private void processSingleEdge(LayoutEdge e) {
            LayoutNode n = e.from;
            if (e.to.layer > n.layer + 1) {
                LayoutEdge last = e;
                for (int i = n.layer + 1; i < last.to.layer; i++) {
                    last = addBetween(last, i);
                }
                last.link = e.link;
                assert last.link != null;
            }
            assert e.link != null;

        }

        private LayoutEdge addBetween(LayoutEdge e, int layer) {
            LayoutNode n = new LayoutNode();
            n.layer = layer;
            n.preds.add(e);
            nodes.add(n);
            LayoutEdge result = new LayoutEdge(n, e.to, n.width / 2, e.relativeTo, null);
            n.succs.add(result);
            assert n.isDummy();
            e.relativeTo = n.width / 2;
            e.to.preds.remove(e);
            e.to.preds.add(result);
            e.to = n;
            return result;
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
            for (LayoutNode n : nodes) {
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
            for (LayoutNode n : nodes) {
                n.x = space[n.layer][n.pos];
            }
        }

        private void run() {
            createArrays();
            initialPositions();

            for (int i = 0; i < SWEEP_ITERATIONS; i++) {
                sweepDown();
                sweepUp();
            }
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
                curY += layerHeight + Math.max((int) (Math.sqrt(maxXOffset) * 2), LAYER_OFFSET * 3);
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
            HashMap<Link, List<Point>> splitStartPoints = new HashMap<>();
            HashMap<Link, List<Point>> splitEndPoints = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();

            HashSet<Link> visited_edges = new HashSet<>();
            for (LayoutNode n : nodes) {
                for (LayoutEdge e : n.preds) {
                    if (e.link != null && !visited_edges.contains(e.link)) {
                        ArrayList<Point> points = new ArrayList<>();

                        points.add(new Point(e.getEndPoint(), e.to.y));
                        points.add(new Point(e.getEndPoint(), layers[e.to.layer].y - LAYER_OFFSET));

                        LayoutNode cur = e.from;
                        LayoutNode other = e.to;
                        LayoutEdge curEdge = e;
                        while (cur.isDummy() && !cur.preds.isEmpty()) {
                            points.add(new Point(cur.getCenterX(), layers[cur.layer].getBottom() + LAYER_OFFSET));
                            points.add(new Point(cur.getCenterX(), cur.y - LAYER_OFFSET));

                            curEdge = cur.preds.get(0);
                            cur = curEdge.from;
                        }

                        points.add(new Point(curEdge.getStartPoint(), layers[cur.layer].getBottom() + LAYER_OFFSET));
                        points.add(new Point(curEdge.getStartPoint(), cur.getBottom()));

                        Collections.reverse(points);

                        if (cur.isDummy()) {
                            if (reversedLinkEndPoints.containsKey(e.link)) {
                                for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                    points.add(new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                }
                            }

                            if (splitStartPoints.containsKey(e.link)) {
                                points.add(0, null);
                                points.addAll(0, splitStartPoints.get(e.link));

                                if (reversedLinks.contains(e.link)) {
                                    Collections.reverse(points);
                                }
                                linkPositions.put(e.link, points);
                            } else {
                                splitEndPoints.put(e.link, points);
                            }

                        } else {
                            if (reversedLinks.contains(e.link)) {
                                Collections.reverse(points);
                                if (reversedLinkStartPoints.containsKey(e.link)) {
                                    for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                        points.add(new Point(p1.x + cur.getLeftSide(), p1.y + cur.y));
                                    }
                                }

                                if (reversedLinkEndPoints.containsKey(e.link)) {
                                    for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                        points.add(0, new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                    }
                                }
                            }

                            linkPositions.put(e.link, points);
                        }

                        visited_edges.add(e.link);
                    }
                }

                for (LayoutEdge e : n.succs) {
                    if (e.link != null && !visited_edges.contains(e.link)) {
                        ArrayList<Point> points = new ArrayList<>();
                        points.add(new Point(e.getStartPoint(), e.from.getBottom()));
                        points.add(new Point(e.getStartPoint(), layers[e.from.layer].getBottom() + LAYER_OFFSET));

                        LayoutNode cur = e.to;
                        LayoutNode other = e.from;
                        LayoutEdge curEdge = e;
                        while (cur.isDummy() && !cur.succs.isEmpty()) {
                            points.add(new Point(cur.getCenterX(), layers[cur.layer].y - LAYER_OFFSET));
                            points.add(new Point(cur.getCenterX(), layers[cur.layer].getBottom() + LAYER_OFFSET));

                            curEdge = cur.succs.get(0);
                            cur = curEdge.to;
                        }

                        points.add(new Point(curEdge.getEndPoint(), layers[cur.layer].y - LAYER_OFFSET));
                        points.add(new Point(curEdge.getEndPoint(), cur.y));

                        if (cur.succs.isEmpty() && cur.isDummy()) {
                            if (reversedLinkStartPoints.containsKey(e.link)) {
                                for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                    points.add(0, new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                }
                            }

                            if (splitEndPoints.containsKey(e.link)) {
                                points.add(null);
                                points.addAll(splitEndPoints.get(e.link));

                                if (reversedLinks.contains(e.link)) {
                                    Collections.reverse(points);
                                }
                                assert !linkPositions.containsKey(e.link);
                                linkPositions.put(e.link, points);
                            } else {
                                splitStartPoints.put(e.link, points);
                            }
                        } else {
                            if (reversedLinks.contains(e.link)) {
                                if (reversedLinkStartPoints.containsKey(e.link)) {
                                    for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                        points.add(0, new Point(p1.x + other.getLeftSide(), p1.y + other.y));
                                    }
                                }
                                if (reversedLinkEndPoints.containsKey(e.link)) {
                                    for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                        points.add(new Point(p1.x + cur.getLeftSide(), p1.y + cur.y));
                                    }
                                }
                                Collections.reverse(points);
                            }
                            linkPositions.put(e.link, points);
                        }

                        visited_edges.add(e.link);
                    }
                }
            }
            return linkPositions;
        }

        public void run() {
            HashMap<Vertex, Point> vertexPositions = computeVertexPositions();
            HashMap<Link, List<Point>> linkPositions = computeLinkPositions();

            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Point point : vertexPositions.values()) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
            }

            for (List<Point> points : linkPositions.values()) {
                for (Point point : points) {
                    minX = Math.min(minX, point.x);
                    minY = Math.min(minY, point.y);
                }
            }

            for (LayoutNode node : nodes) {
                node.x -= minX;
                node.y -= minY;
            }

            for (LayoutLayer layer : layers) {
                layer.y -= minY;
            }

            // shift vertices by minX/minY
            for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
                Point point = entry.getValue();
                point.x -= minX;
                point.y -= minY;
                Vertex vertex = entry.getKey();
                vertex.setPosition(point);
            }

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

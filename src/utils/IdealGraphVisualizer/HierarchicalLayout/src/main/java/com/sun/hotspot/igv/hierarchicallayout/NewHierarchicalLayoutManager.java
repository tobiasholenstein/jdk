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
import java.util.stream.Collectors;


public class NewHierarchicalLayoutManager {

    public static final int SWEEP_ITERATIONS = 1;
    public static final int CROSSING_ITERATIONS = 2;
    public static final int DUMMY_HEIGHT = 1;
    public static final int DUMMY_WIDTH = 1;
    public static final int MAX_LAYER_LENGTH = -1;
    public static final int X_OFFSET = 8;
    public static final int LAYER_OFFSET = 8;


    // Options
    private final int offset;
    private final int maxLayerLength;
    // Algorithm global datastructures
    private final Set<Link> reversedLinks;
    private final Set<LayoutEdge> longEdges;
    private final List<LayoutNode> nodes;
    private final HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private final HashMap<Link, List<Point>> reversedLinkStartPoints;
    private final HashMap<Link, List<Point>> reversedLinkEndPoints;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;

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
            int count = loadCrossingNumber(up, this);
            if (count > 0) {
                crossingNumber /= count;
            } else {
                crossingNumber = 0;
            }
        }

        public int loadCrossingNumber(boolean up, LayoutNode source) {
            int count = 0;
            if (up) {
                count = succs.stream().map((e) -> e.loadCrossingNumber(up, source)).reduce(count, Integer::sum);
            } else {
                count = preds.stream().map((e) -> e.loadCrossingNumber(up, source)).reduce(count, Integer::sum);
            }
            return count;
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
            int factor = 1;
            int nr;
            if (up) {
                nr = getEndPoint() * factor;
            } else {
                nr = getStartPoint() * factor;
            }
            source.crossingNumber += nr;
            return factor;
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
        //when default value 1 is used, the calculated RelativeTo and relativeFrom
        //of dummyNodes are 0 which negatively affects calculations
        this.maxLayerLength = MAX_LAYER_LENGTH;

        offset = X_OFFSET + DUMMY_WIDTH;

        vertexToLayoutNode = new HashMap<>();
        reversedLinks = new HashSet<>();
        reversedLinkStartPoints = new HashMap<>();
        reversedLinkEndPoints = new HashMap<>();
        nodes = new ArrayList<>();
        longEdges = new HashSet<>();
    }

    private void cleanup() {
        vertexToLayoutNode.clear();
        reversedLinks.clear();
        reversedLinkStartPoints.clear();
        reversedLinkEndPoints.clear();
        nodes.clear();
        longEdges.clear();
    }

    public void doLayout(LayoutGraph graph) {
        this.graph = graph;

        cleanup();

        // #############################################################
        // Step 1: Build up data structure
        new BuildDatastructure().run();

        // #############################################################
        // STEP 2: Reverse edges, handle backedges
        new ReverseEdges().run();

        // #############################################################
        // STEP 3: Assign layers
        new AssignLayers().run();

        // #############################################################
        // STEP 4: Create dummy nodes
        new CreateDummyNodes().run();

        // #############################################################
        // STEP 5: Crossing Reduction
        new CrossingReduction().run();

        // #############################################################
        // STEP 6: Assign X coordinates
        new AssignXCoordinates().run();

        // #############################################################
        // STEP 7: Assign Y coordinates
        new AssignYCoordinates().run();

        // #############################################################
        // STEP 8: Write back to interface
        new WriteResult().run();
    }

    private class BuildDatastructure {

        private void run() {
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

                int cur = -reversedDown.size() * offset;
                int curWidth = node.width + reversedDown.size() * offset;
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

                    cur += offset;
                    node.yOffset += offset;
                    curWidth -= offset;
                }
                node.width += reversedDown.size() * offset;

                if (reversedDown.isEmpty()) {
                    cur = offset;
                } else {
                    cur = -offset;
                }
                for (int pos : reversedUp) {
                    ArrayList<LayoutEdge> reversedPreds = new ArrayList<>();
                    for (LayoutEdge e : node.preds) {
                        if (e.relativeTo == pos && reversedLinks.contains(e.link)) {
                            if (reversedDown.isEmpty()) {
                                e.relativeTo = node.width + offset;
                            } else {
                                e.relativeTo = cur;
                            }

                            reversedPreds.add(e);
                        }
                    }
                    node.bottomYOffset += offset;
                    ArrayList<Point> endPoints = new ArrayList<>();

                    node.width += offset;
                    if (reversedDown.isEmpty()) {
                        endPoints.add(new Point(node.width, node.height + node.bottomYOffset));
                    } else {
                        endPoints.add(new Point(cur, node.height + node.bottomYOffset));
                        cur -= offset;
                    }

                    endPoints.add(new Point(pos, node.height + node.bottomYOffset));
                    endPoints.add(new Point(pos, node.height + node.bottomYOffset));
                    endPoints.add(new Point(pos, node.height));
                    for (LayoutEdge e : reversedPreds) {
                        reversedLinkEndPoints.put(e.link, endPoints);
                    }
                }

                if (!reversedDown.isEmpty()) {
                    node.xOffset = reversedUp.size() * offset;
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


        private void run() {

            HashMap<Integer, List<LayoutEdge>> portHash = new HashMap<>();
            ArrayList<LayoutNode> currentNodes = new ArrayList<>(nodes);
            for (LayoutNode n : currentNodes) {
                portHash.clear();

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

                            LayoutEdge topEdge;

                            LayoutNode topNode = topNodeHash.get(e.relativeFrom);
                            if (topNode == null) {
                                topNode = new LayoutNode();
                                topNode.layer = e.from.layer + 1;
                                nodes.add(topNode);
                                topNodeHash.put(e.relativeFrom, topNode);
                                bottomNodeHash.put(e.relativeFrom, new HashMap<>());
                            }
                            topEdge = new LayoutEdge(e.from, topNode, e.relativeFrom, topNode.width / 2, e.link);
                            e.from.succs.add(topEdge);
                            topNode.preds.add(topEdge);

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

                            LayoutEdge bottomEdge = new LayoutEdge(bottomNode, e.to, bottomNode.width / 2, e.relativeTo, e.link);
                            e.to.preds.add(bottomEdge);
                            bottomNode.succs.add(bottomEdge);

                        } else {
                            Integer i = e.relativeFrom;
                            if (!portHash.containsKey(i)) {
                                portHash.put(i, new ArrayList<>());
                            }
                            portHash.get(i).add(e);
                        }
                    }
                }

                for (LayoutEdge e : succs) {
                    Integer i = e.relativeFrom;
                    if (portHash.containsKey(i)) {

                        List<LayoutEdge> list = portHash.get(i);
                        list.sort(LAYER_COMPARATOR);

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
                            n.succs.add(edges[0]);
                            for (int j = 1; j < cnt; j++) {
                                nodes[j] = new LayoutNode();
                                nodes[j].layer = n.layer + j + 1;
                                edges[j] = new LayoutEdge(nodes[j - 1], nodes[j], nodes[j - 1].width / 2, nodes[j].width / 2, null);
                                nodes[j - 1].succs.add(edges[j]);
                                nodes[j].preds.add(edges[j]);
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
        }

        private void processSingleEdge(LayoutEdge e) {
            LayoutNode n = e.from;
            if (e.to.layer > n.layer + 1) {
                LayoutEdge last = e;
                for (int i = n.layer + 1; i < last.to.layer; i++) {
                    last = addBetween(last, i);
                }
            }
        }

        private LayoutEdge addBetween(LayoutEdge e, int layer) {
            LayoutNode n = new LayoutNode();
            n.layer = layer;
            n.preds.add(e);
            nodes.add(n);
            LayoutEdge result = new LayoutEdge(n, e.to, n.width / 2, e.relativeTo, null);
            n.succs.add(result);
            e.relativeTo = n.width / 2;
            e.to.preds.remove(e);
            e.to.preds.add(result);
            e.to = n;
            return result;
        }


    }

    private class CrossingReduction {


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
            //will be reassigned
            initX();
            // Optimize
            for (int i = 0; i < CROSSING_ITERATIONS; i++) {
                downSweep();
                upSweep();
            }
            downSweep();
            updatePositions();
        }

        private void initX() {
            for (int i = 0; i < layers.length; i++) {
                updateXOfLayer(i);
            }
        }

        private void updateXOfLayer(int index) {
            int x = 0;
            for (LayoutNode n : layers[index]) {
                n.x = x;
                x += n.getWholeWidth() + offset;
            }
        }

        private void updatePositions() {
            for (int i = 0; i < layerCount; ++i) {
                updateLayerPositions(i);
            }
        }

        private void updateLayerPositions(int index) {
            int z = 0;
            for (LayoutNode n : layers[index]) {
                n.pos = z;
                z++;
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

        private void initialPositions() {
            for (LayoutNode n : nodes) {
                n.x = space[n.layer][n.pos];
            }
        }

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
                NodeRow row = new NodeRow(space[i]);
                for (LayoutNode n : upProcessingOrder[i]) {
                    n.optimal_x = calculateOptimalUp(n);
                }
                Arrays.sort(upProcessingOrder[i], (n1, n2) -> {
                    int ret = DUMMY_NODES_FIRST.compare(n1, n2);
                    if (ret != 0) {
                        return ret;
                    }
                    return NODE_POSITION_OPTIMAL.compare(n1, n2);
                });
                for (LayoutNode n : upProcessingOrder[i]) {
                    row.insert(n, n.optimal_x);
                }
            }
        }

        private void sweepDown() {
            for (int i = 1; i < layers.length; i++) {
                NodeRow row = new NodeRow(space[i]);
                for (LayoutNode n : downProcessingOrder[i]) {
                    int optimal = calculateOptimalDown(n);
                    row.insert(n, optimal);
                }
            }
        }

    }

    private class AssignYCoordinates {

        private void run() {
            int curY = 0;
            for (LayoutLayer layer : layers) {
                int maxHeight = layer.height;
                layer.y = curY;
                int maxXOffset = 0;
                for (LayoutNode n : layer) {
                    n.y = curY;
                    if (!n.isDummy()) {
                        n.yOffset = (maxHeight - n.getWholeHeight()) / 2 + n.yOffset;
                        n.y += n.yOffset;
                        n.bottomYOffset = maxHeight - n.yOffset - n.height;
                    }
                    for (LayoutEdge e : n.succs) {
                        maxXOffset = Math.max(Math.abs(e.getStartPoint() - e.getEndPoint()), maxXOffset);
                    }
                }
                curY += maxHeight + Math.max((int) (Math.sqrt(maxXOffset) * 2), LAYER_OFFSET * 3);
            }
        }
    }

    private class WriteResult {

        private HashMap<Point, Point> pointsIdentity;
        private final int addition = LAYER_OFFSET / 2;

        public void run() {
            HashMap<Link, List<Point>> splitStartPoints = new HashMap<>();
            HashMap<Link, List<Point>> splitEndPoints = new HashMap<>();
            pointsIdentity = new HashMap<>();
            HashMap<Vertex, Point> vertexPositions = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();
            for (Vertex v : graph.getVertices()) {
                LayoutNode n = vertexToLayoutNode.get(v);
                vertexPositions.put(v, new Point(n.getLeftSide(), n.y));
            }

            for (LayoutNode n : nodes) {
                for (LayoutEdge e : n.preds) {
                    if (e.link != null) {
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

                        if (cur.isDummy() && cur.preds.isEmpty()) {
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
                                assert !linkPositions.containsKey(e.link);
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

                        // No longer needed!
                        e.link = null;
                    }
                }

                for (LayoutEdge e : n.succs) {
                    if (e.link != null) {
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

                        e.link = null;
                    }
                }
            }

            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
                Vertex v = entry.getKey();
                Point p = entry.getValue();
                Dimension s = v.getSize();
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                maxX = Math.max(maxX, p.x + s.width);
                maxY = Math.max(maxY, p.y + s.height);

            }

            for (Map.Entry<Link, List<Point>> entry : linkPositions.entrySet()) {
                for (Point p : entry.getValue()) {
                    if (p != null) {
                        minX = Math.min(minX, p.x);
                        minY = Math.min(minY, p.y);
                        maxX = Math.max(maxX, p.x);
                        maxY = Math.max(maxY, p.y);
                    }
                }

            }

            for (Map.Entry<Vertex, Point> entry : vertexPositions.entrySet()) {
                Point p = entry.getValue();
                p.x -= minX;
                p.y -= minY;
                entry.getKey().setPosition(p);
            }

            for (LayoutEdge e : longEdges) {
                e.from.succs.add(e);
                e.to.preds.add(e);
                linkPositions.put(e.link, makeLongEnding(e));
            }

            Map<Port, List<Map.Entry<Link, List<Point>>>> coLinks = new HashMap<>();
            for (Map.Entry<Link, List<Point>> entry : linkPositions.entrySet()) {
                Link l = entry.getKey();
                coLinks.computeIfAbsent(l.getFrom(), p -> new ArrayList<>()).add(entry);

                List<Point> points = entry.getValue();
                for (Point p : points) {
                    if (p != null) {
                        p.x -= minX;
                        p.y -= minY;
                    }
                }
            }

            for (List<Map.Entry<Link, List<Point>>> links : coLinks.values()) {
                //reduce links points identified by their Output Port
                reduceLinksPoints(links);
                //write reduced lists of points to links
                links.forEach(e -> e.getKey().setControlPoints(e.getValue()));
            }
        }

        private class ReductionEntry {

            final Point lastPoint;
            final int nextPointIndex;
            final Point nextPoint;
            final List<Point> reducedPoints;
            final List<Map.Entry<Link, List<Point>>> entries;

            public ReductionEntry(Point lastPoint, int nextPointIndex, Point nextPoint, List<Map.Entry<Link, List<Point>>> entries, List<Point> reducedPoints) {
                this.lastPoint = lastPoint;
                this.nextPointIndex = nextPointIndex;
                this.nextPoint = nextPoint;
                this.entries = entries;
                this.reducedPoints = reducedPoints;
            }
        }

        /**
         * This algorithm reduces number of points on correlating edges to their
         * identities and removes any unnecessary points along the edges.
         *
         * Algorithm expects Links to begin at the same point. Reduced points
         * are written back to its corresponding Map.Entry. Middle points in
         * line are omitted. Equal points are replaced by single identity.
         *
         * @param links List of Map.Entries of Points on Link.
         */
        private void reduceLinksPoints(List<Map.Entry<Link, List<Point>>> links) {
            Point firstPoint = links.get(0).getValue().get(0);
            ArrayList<Point> points = new ArrayList<>();
            //first Point is always the same
            points.add(firstPoint);
            //Avoiding SOE by not using recursion
            //prepare entries for reduction
            Queue<ReductionEntry> tasks = new ArrayDeque<>(makeReductionEntries(links, points, firstPoint, 0));
            while (!tasks.isEmpty()) {
                tasks.addAll(reducePoints(tasks.remove()));
            }
            //overwrite Points at same place by "identity"
            //needed for ClusterNodes setPosition() as it moves points by "identity!
            for (Map.Entry<Link, List<Point>> pnts : links) {
                List<Point> newPoints = new ArrayList<>(pnts.getValue().size());
                for (Point p : pnts.getValue()) {
                    newPoints.add(pointsIdentity.computeIfAbsent(p, x -> x));
                }
                pnts.setValue(newPoints);
            }
        }

        /**
         * Creates List of ReductionEntries, for the algorithm to process. This
         * method creates separate ReductionEntries for edges that has same
         * direction. Also this method writes reduced points back to Map.Entry.
         *
         * @param entries List of mapping of Link to its points.
         * @param reducedPoints Current already reduced points.
         * @param lastPoint Last Point that was saved as reduced Point.
         * @param lastPointIndex Last Point index in unreduced Points list.
         * @return List of ReductionEntries.
         */
        private List<ReductionEntry> makeReductionEntries(List<Map.Entry<Link, List<Point>>> entries, List<Point> reducedPoints, Point lastPoint, int lastPointIndex) {
            assert assertCorrectPointAtIndex(entries, reducedPoints, lastPoint, lastPointIndex);
            int nextIndex = lastPointIndex + 1;
            Map<Point, List<Map.Entry<Link, List<Point>>>> nexts = new HashMap<>();
            //separate edges by their direction and write back finished edges.
            for (Map.Entry<Link, List<Point>> entry : entries) {
                List<Point> controlPoints = entry.getValue();
                if (controlPoints.size() > nextIndex) {
                    //collect links by their next points (separate branches)
                    nexts.computeIfAbsent(controlPoints.get(nextIndex), p -> new ArrayList<>()).add(entry);
                } else {
                    assert reducedPoints.size() > 1;
                    //write complete reduced points to its Map.Entry
                    entry.setValue(reducedPoints);
                }
            }
            //collect edges to ReductionEntries (copy reduced Points list if there is branching)
            boolean branching = nexts.size() > 1;
            return nexts.entrySet().stream().map(e
                            -> new ReductionEntry(lastPoint, nextIndex, e.getKey(), e.getValue(),
                            branching ? new ArrayList<>(reducedPoints) : reducedPoints))
                    .collect(Collectors.toList());
        }

        /**
         * Returns if points are in line.
         *
         * @return {@code true} if points are in line.
         */
        private boolean pointsInLine(Point p1, Point p2, Point p3) {
            return p1 != null && p2 != null && p3 != null
                    && (p1.x - p2.x) * (p2.y - p3.y) == (p1.y - p2.y) * (p2.x - p3.x);
        }

        /**
         * Method processes ReductionEntry to minimize Points on correlating
         * edges. Algorithm expects that current points are the same for all
         * edges. (lastP-currentP-nextP)
         * <p>
         * It then scans for next point and determines if edges branches, turns
         * or stays at line. If edges stays at line, it skips current Point and
         * moves to next. If edges turns, it saves current Point as last Point
         * and scans again. If edges branches it just saves current point and
         * let makeReductionEntries() separate the branches.
         *
         * @param task
         * @return List of ReductionEntries for further reduction.
         */
        private List<ReductionEntry> reducePoints(ReductionEntry task) {
            //lastPoint is already saved in reduced edges
            Point lastPoint = task.lastPoint;
            //currentPoint can be omited as middle point if in line with lastPoint and nextPoint
            Point currentPoint = task.nextPoint;
            int currentIndex = task.nextPointIndex;
            //null must stay where it is, save and move on
            if (currentPoint == null || lastPoint == null) {
                task.reducedPoints.add(currentPoint);
                return makeReductionEntries(task.entries, task.reducedPoints, currentPoint, currentIndex);
            }
            List<Point> points = task.entries.get(0).getValue();
            //if we are processing multiple edges at once - check for branching
            boolean multiple = task.entries.size() > 1;
            //process edges till we are done or we found branching (only single edges ends)
            while (points.size() > currentIndex + 1) {
                final int nextIndex = currentIndex + 1;
                final Point nextPoint = points.get(nextIndex);
                //branching check (next points of all edges are equal)
                if (multiple && !task.entries.stream().map(Map.Entry::getValue).allMatch(pts -> Objects.equals(nextPoint, pts.get(nextIndex)))) {
                    break;
                }
                //if edges turns save current point as last point (turning point)
                if (!pointsInLine(lastPoint, currentPoint, nextPoint)) {
                    task.reducedPoints.add(currentPoint);
                    lastPoint = currentPoint;
                }
                //move points by a step
                currentPoint = nextPoint;
                currentIndex = nextIndex;
            }
            task.reducedPoints.add(currentPoint);
            //prepare branched or finished edges for future processing or writeback
            return makeReductionEntries(task.entries, task.reducedPoints, currentPoint, currentIndex);
        }

        private boolean assertCorrectPointAtIndex(List<Map.Entry<Link, List<Point>>> entries, List<Point> reducedPoints, Point lastPoint, int lastPointIndex) {
            //assert that last point in reduced points is indeed the lastPoint (it was already added to list)
            if (!Objects.equals(reducedPoints.get(reducedPoints.size() - 1), lastPoint)) {
                return false;
            }
            //assert that all entries has the lastPoint at lastPointIndex position (no stray edges)
            for (Map.Entry<Link, List<Point>> entry : entries) {
                if (!Objects.equals(entry.getValue().get(lastPointIndex), lastPoint)) {
                    return false;
                }
            }
            return true;
        }

        private List<Point> makeLongEnding(LayoutEdge e) {
            List<Point> points = new ArrayList<>();
            if (!reversedLinks.contains(e.link)) {
                points.add(new Point(e.getStartPoint(), e.from.getBottom()));
                makeLongEndingStart(points);
                points.add(null);
                makeLongEndingEnd(points, new Point(e.getEndPoint(), e.to.y));
            } else {
                int diffX = e.from.getLeftSide();
                int diffY = e.from.y;
                for (Point p : reversedLinkStartPoints.get(e.link)) {
                    points.add(new Point(p.x + diffX, p.y + diffY));
                }
                Collections.reverse(points);
                points.add(new Point(points.get(points.size() - 1).x, diffY + e.from.height));
                makeLongEndingStart(points);
                points.add(null);
                List<Point> ends = reversedLinkEndPoints.get(e.link);
                diffX = e.to.getLeftSide();
                diffY = e.to.y;
                makeLongEndingEnd(points, new Point(ends.get(0).x + diffX, diffY));
                for (Point p : ends) {
                    points.add(new Point(p.x + diffX, p.y + diffY));
                }
                Collections.reverse(points);
            }
            return points;
        }

        private void makeLongEndingStart(List<Point> points) {
            Point last = points.get(points.size() - 1);
            points.add(last = new Point(last.x, last.y + addition));
            points.add(last = new Point(last.x + addition, last.y + addition));
            points.add(new Point(last.x, last.y + (addition * 2)));
        }

        private void makeLongEndingEnd(List<Point> points, Point last) {
            points.add(last = new Point(last.x - addition, last.y - (addition * 3)));
            points.add(last = new Point(last.x, last.y + addition));
            points.add(last = new Point(last.x + addition, last.y + addition));
            points.add(new Point(last.x, last.y + addition));
        }

    }

    private class LayoutLayer extends ArrayList<LayoutNode> {

        private int height = 0;
        private int y = 0;

        @Override
        public boolean addAll(Collection<? extends LayoutNode> c) {
            c.forEach(this::add0);
            return super.addAll(c);
        }

        private void add0(LayoutNode n) {
            height = Math.max(height, n.getWholeHeight());
        }

        @Override
        public boolean add(LayoutNode n) {
            add0(n);
            return super.add(n);
        }

        private boolean remove0(LayoutNode n) {
            return true;
        }

        @Override
        public boolean remove(Object o) {
            return (o instanceof LayoutNode) && super.remove(o) && remove0((LayoutNode) o);
        }

        public int getBottom() {
            return y + height;
        }
    }

    private class NodeRow {

        private final TreeSet<LayoutNode> treeSet;
        private final int[] space;

        public NodeRow(int[] space) {
            treeSet = new TreeSet<>(NODE_POSITION_COMPARATOR);
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

    private static final Comparator<LayoutNode> NODE_POSITION_COMPARATOR = Comparator.comparingInt(n -> n.pos);

    private static final Comparator<LayoutNode> NODE_POSITION_OPTIMAL = Comparator.comparingInt(n -> n.optimal_x);

    private static final Comparator<LayoutNode> DUMMY_NODES_FIRST = Comparator.comparing(LayoutNode::isDummy).reversed();

    private static final Comparator<LayoutNode> NODE_DOWN = Comparator.comparingInt(n -> n.preds.size());
    private static final Comparator<LayoutNode> NODE_UP = Comparator.comparingInt(n -> n.succs.size());

    private static final Comparator<LayoutNode> NODE_PROCESSING_DOWN_COMPARATOR = DUMMY_NODES_FIRST.thenComparing(NODE_DOWN);

    private static final Comparator<LayoutNode> NODE_PROCESSING_UP_COMPARATOR = DUMMY_NODES_FIRST.thenComparing(NODE_UP);

    private static final Comparator<LayoutNode> CROSSING_NODE_COMPARATOR = (n1, n2) -> Float.compare(n1.crossingNumber, n2.crossingNumber);

    private static final Comparator<LayoutEdge> LAYER_COMPARATOR = Comparator.comparingInt(e -> e.to.layer);

    private static final Comparator<Link> LINK_COMPARATOR =
            Comparator.comparing((Link l) -> l.getFrom().getVertex())
                    .thenComparing(l -> l.getTo().getVertex())
                    .thenComparingInt(l -> l.getFrom().getRelativePosition().x)
                    .thenComparingInt(l -> l.getTo().getRelativePosition().x);
}

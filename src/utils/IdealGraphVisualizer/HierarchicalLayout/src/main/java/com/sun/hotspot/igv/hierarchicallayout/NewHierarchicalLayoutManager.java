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
import com.sun.hotspot.igv.util.Statistics;
import java.awt.Dimension;
import java.awt.Point;
import java.util.*;


public class NewHierarchicalLayoutManager {

    private static final int DUMMY_HEIGHT = 1;
    private static final int DUMMY_WIDTH = 1;
    private static final int X_OFFSET = 8;
    private static final int LAYER_OFFSET = 8;
    private static final int MIN_LAYER_DIFFERENCE = 1;

    // Algorithm global datastructures
    private Set<Link> reversedLinks;
    private Set<LayoutEdge> selfEdges;
    private List<LayoutNode> nodes;
    private HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private HashMap<Link, List<Point>> reversedLinkStartPoints;
    private HashMap<Link, List<Point>> reversedLinkEndPoints;
    private HashMap<Link, List<Point>> splitStartPoints;
    private HashMap<Link, List<Point>> splitEndPoints;
    private LayoutGraph graph;
    private List<LayoutNode>[] layers;
    private int layerCount;


    public NewHierarchicalLayoutManager() {}

    public void doLayout(LayoutGraph graph) {
        this.graph = graph;

        vertexToLayoutNode = new HashMap<>();
        reversedLinks = new HashSet<>();
        selfEdges = new HashSet<>();
        reversedLinkStartPoints = new HashMap<>();
        reversedLinkEndPoints = new HashMap<>();
        nodes = new ArrayList<>();
        splitStartPoints = new HashMap<>();
        splitEndPoints = new HashMap<>();

        // #############################################################
        // Step 1: Build up data structure
        buildDatastructure();

        // Remove self-edges from the beginning.
        removeSelfEdges(false);

        // #############################################################
        // STEP 2: Reverse edges, handle backedges
        reverseEdges();

        // Hide self-edges from the layout algorithm and save them for later.
        removeSelfEdges(true);

        // #############################################################
        // STEP 3: Assign layers
        assignLayers();

        // #############################################################
        // STEP 4: Create dummy nodes
        createDummyNodes();

        // #############################################################
        // STEP 5: Crossing Reduction
        crossingReduction();

        // #############################################################
        // STEP 7: Assign X coordinates
        assignXCoordinates();

        // #############################################################
        // STEP 6: Assign Y coordinates
        assignYCoordinates();

        // Put saved self-edges back so that they are assigned points.
        for (LayoutEdge e : selfEdges) {
            e.from.succs.add(e);
            e.to.preds.add(e);
        }

        // #############################################################
        // STEP 8: Write back to interface
        writeResult();
    }

    private void buildDatastructure() {
        // Set up nodes
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());
        // Order roots first to create more natural layer assignments.
        vertices.sort((Vertex a, Vertex b) ->
                a.isRoot() == b.isRoot() ? a.compareTo(b) : Boolean.compare(b.isRoot(), a.isRoot()));

        for (Vertex v : vertices) {
            LayoutNode node = new LayoutNode();
            Dimension size = v.getSize();
            node.width = (int) size.getWidth();
            node.height = (int) size.getHeight();
            node.vertex = v;
            nodes.add(node);
            vertexToLayoutNode.put(v, node);
        }

        // Set up edges
        List<Link> links = new ArrayList<>(graph.getLinks());
        links.sort(linkComparator);
        for (Link l : links) {
            LayoutEdge edge = new LayoutEdge();
            edge.from = vertexToLayoutNode.get(l.getFrom().getVertex());
            edge.to = vertexToLayoutNode.get(l.getTo().getVertex());
            edge.relativeFrom = l.getFrom().getRelativePosition().x;
            edge.relativeTo = l.getTo().getRelativePosition().x;
            edge.link = l;
            edge.vip = l.isVIP();
            edge.from.succs.add(edge);
            edge.to.preds.add(edge);
        }
    }

    // Remove self-edges, possibly saving them into the selfEdges set.
    private void removeSelfEdges(boolean save) {
        for (LayoutNode node : nodes) {
            for (LayoutEdge e : new ArrayList<>(node.succs)) {
                if (e.to == node) {
                    if (save) {
                        selfEdges.add(e);
                    }
                    node.succs.remove(e);
                    node.preds.remove(e);
                }
            }
        }
    }

    private static final Comparator<LayoutNode> nodeProcessingDownComparator = (n1, n2) -> {
        int n1VIP = 0;
        for (LayoutEdge e : n1.preds) {
            if (e.vip) {
                n1VIP++;
            }
        }
        int n2VIP = 0;
        for (LayoutEdge e : n2.preds) {
            if (e.vip) {
                n2VIP++;
            }
        }
        if (n1VIP != n2VIP) {
            return n2VIP - n1VIP;
        }
        if (n1.vertex == null) {
            if (n2.vertex == null) {
                return 0;
            }
            return -1;
        }
        if (n2.vertex == null) {
            return 1;
        }
        return n1.preds.size() - n2.preds.size();
    };
    private static final Comparator<LayoutNode> nodeProcessingUpComparator = (n1, n2) -> {
        int n1VIP = 0;
        for (LayoutEdge e : n1.succs) {
            if (e.vip) {
                n1VIP++;
            }
        }
        int n2VIP = 0;
        for (LayoutEdge e : n2.succs) {
            if (e.vip) {
                n2VIP++;
            }
        }
        if (n1VIP != n2VIP) {
            return n2VIP - n1VIP;
        }
        if (n1.vertex == null) {
            if (n2.vertex == null) {
                return 0;
            }
            return -1;
        }
        if (n2.vertex == null) {
            return 1;
        }
        return n1.succs.size() - n2.succs.size();
    };


    private ArrayList<Integer>[] space;
    private ArrayList<LayoutNode>[] downProcessingOrder;
    private ArrayList<LayoutNode>[] upProcessingOrder;

    private void initialPositions() {
        for (LayoutNode n : nodes) {
            n.x = space[n.layer].get(n.pos);
        }
    }

    @SuppressWarnings("unchecked")
    private void createArrays() {
        space = new ArrayList[layers.length];
        downProcessingOrder = new ArrayList[layers.length];
        upProcessingOrder = new ArrayList[layers.length];
    }

    private void assignXCoordinates() {
        createArrays();

        for (int i = 0; i < layers.length; i++) {
            space[i] = new ArrayList<>();
            downProcessingOrder[i] = new ArrayList<>();
            upProcessingOrder[i] = new ArrayList<>();

            int curX = 0;
            for (LayoutNode n : layers[i]) {
                space[i].add(curX);
                curX += n.width + X_OFFSET;
                downProcessingOrder[i].add(n);
                upProcessingOrder[i].add(n);
            }

            downProcessingOrder[i].sort(nodeProcessingDownComparator);
            upProcessingOrder[i].sort(nodeProcessingUpComparator);
        }

        initialPositions();
        for (int i = 0; i < 1; i++) {
            sweepDown();
            //adjustSpace();
            sweepUp();
            //adjustSpace();
        }

        //sweepDown();
        //adjustSpace();
        //sweepUp();
    }

    private void adjustSpace() {
        for (int i = 0; i < layers.length; i++) {
            for (LayoutNode n : layers[i]) {
                space[i].add(n.x);
            }
        }
    }

    private int calculateOptimalDown(LayoutNode n) {
        int size = n.preds.size();
        if (size == 0) {
            return n.x;
        }
        int vipCount = 0;
        for (LayoutEdge e : n.preds) {
            if (e.vip) {
                vipCount++;
            }
        }

        if (vipCount == 0) {
            int[] values = new int[size];
            for (int i = 0; i < size; i++) {
                LayoutEdge e = n.preds.get(i);
                values[i] = e.from.x + e.relativeFrom - e.relativeTo;
            }
            return Statistics.median(values);
        } else {
            int z = 0;
            int[] values = new int[vipCount];
            for (int i = 0; i < size; i++) {
                LayoutEdge e = n.preds.get(i);
                if (e.vip) {
                    values[z++] = e.from.x + e.relativeFrom - e.relativeTo;
                }
            }
            return Statistics.median(values);
        }
    }

    private int calculateOptimalBoth(LayoutNode n) {
        if (n.preds.size() == n.succs.size()) {
            return n.x;
        }

        int[] values = new int[n.preds.size() + n.succs.size()];
        int i = 0;

        for (LayoutEdge e : n.preds) {
            values[i] = e.from.x + e.relativeFrom - e.relativeTo;
            i++;
        }

        for (LayoutEdge e : n.succs) {
            values[i] = e.to.x + e.relativeTo - e.relativeFrom;
            i++;
        }

        return Statistics.median(values);
    }

    private int calculateOptimalUp(LayoutNode n) {
        int size = n.succs.size();
        if (size == 0) {
            return n.x;
        }
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            LayoutEdge e = n.succs.get(i);
            values[i] = e.to.x + e.relativeTo - e.relativeFrom;
            if (e.vip) {
                return values[i];
            }
        }
        return Statistics.median(values);
    }

    private void sweepUp() {
        for (int i = layers.length - 1; i >= 0; i--) {
            NodeRow r = new NodeRow(space[i]);
            for (LayoutNode n : upProcessingOrder[i]) {
                int optimal = calculateOptimalUp(n);
                r.insert(n, optimal);
            }
        }
    }

    private void sweepDown() {
        for (int i = 1; i < layers.length; i++) {
            NodeRow r = new NodeRow(space[i]);
            for (LayoutNode n : downProcessingOrder[i]) {
                int optimal = calculateOptimalDown(n);
                r.insert(n, optimal);
            }
        }
    }


    private static class NodeRow {

        private final TreeSet<LayoutNode> treeSet;
        private final ArrayList<Integer> space;

        public NodeRow(ArrayList<Integer> space) {
            treeSet = new TreeSet<>(Comparator.comparingInt(n -> n.pos));
            this.space = space;
        }

        public int offset(LayoutNode n1, LayoutNode n2) {
            int v1 = space.get(n1.pos) + n1.width;
            int v2 = space.get(n2.pos);
            return v2 - v1;
        }

        public void insert(LayoutNode n, int pos) {

            SortedSet<LayoutNode> headSet = treeSet.headSet(n);

            LayoutNode leftNeighbor;
            int minX = Integer.MIN_VALUE;
            if (!headSet.isEmpty()) {
                leftNeighbor = headSet.last();
                minX = leftNeighbor.x + leftNeighbor.width + offset(leftNeighbor, n);
            }

            if (pos < minX) {
                n.x = minX;
            } else {

                LayoutNode rightNeighbor;
                SortedSet<LayoutNode> tailSet = treeSet.tailSet(n);
                int maxX = Integer.MAX_VALUE;
                if (!tailSet.isEmpty()) {
                    rightNeighbor = tailSet.first();
                    maxX = rightNeighbor.x - offset(n, rightNeighbor) - n.width;
                }

                n.x = Math.min(pos, maxX);
            }

            treeSet.add(n);
        }
    }
    private static final Comparator<LayoutNode> crossingNodeComparator = Comparator.comparingInt(n -> n.crossingNumber);


    @SuppressWarnings("unchecked")
    private void createLayers() {
        layers = new List[layerCount];

        for (int i = 0; i < layerCount; i++) {
            layers[i] = new ArrayList<>();
        }
    }

    private void crossingReduction() {
        createLayers();

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
                        if (!nodes.contains(e.to)) {
                            nodes.add(e.to);
                        }
                    }
                }
            }
        }

        updatePositions();

        initX();

        // Optimize
        for (int i = 0; i < 2; i++) {
            downSweep();
            upSweep();
        }
        downSweep();
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
            x += n.width + X_OFFSET;
        }
    }

    private void updatePositions() {
        for (List<LayoutNode> layer : layers) {
            int z = 0;
            for (LayoutNode n : layer) {
                n.pos = z;
                z++;
            }
        }
    }

    private void downSweep() {

        // Downsweep
        for (int i = 1; i < layerCount; i++) {

            for (LayoutNode n : layers[i]) {
                n.crossingNumber = 0;
            }

            for (LayoutNode n : layers[i]) {

                int sum = 0;
                int count = 0;
                for (LayoutEdge e : n.preds) {
                    int cur = e.from.x + e.relativeFrom;
                    int factor = 1;
                    sum += cur * factor;
                    count += factor;
                }

                if (count > 0) {
                    sum /= count;
                    n.crossingNumber = sum;
                }
            }

            updateCrossingNumbers(i, true);
            layers[i].sort(crossingNodeComparator);
            updateXOfLayer(i);

            int z = 0;
            for (LayoutNode n : layers[i]) {
                n.pos = z;
                z++;
            }
        }
    }

    private void updateCrossingNumbers(int index, boolean down) {
        for (int i = 0; i < layers[index].size(); i++) {
            LayoutNode n = layers[index].get(i);
            LayoutNode prev = null;
            if (i > 0) {
                prev = layers[index].get(i - 1);
            }
            LayoutNode next = null;
            if (i < layers[index].size() - 1) {
                next = layers[index].get(i + 1);
            }

            boolean cond = n.succs.isEmpty();
            if (down) {
                cond = n.preds.isEmpty();
            }

            if (cond) {

                if (prev != null && next != null) {
                    n.crossingNumber = (prev.crossingNumber + next.crossingNumber) / 2;
                } else if (prev != null) {
                    n.crossingNumber = prev.crossingNumber;
                } else if (next != null) {
                    n.crossingNumber = next.crossingNumber;
                }
            }
        }
    }

    private void upSweep() {
        // Upsweep
        for (int i = layerCount - 2; i >= 0; i--) {

            for (LayoutNode n : layers[i]) {
                n.crossingNumber = 0;
            }

            for (LayoutNode n : layers[i]) {

                int count = 0;
                int sum = 0;
                for (LayoutEdge e : n.succs) {
                    int cur = e.to.x + e.relativeTo;
                    int factor = 1;
                    sum += cur * factor;
                    count += factor;
                }

                if (count > 0) {
                    sum /= count;
                    n.crossingNumber = sum;
                }

            }

            updateCrossingNumbers(i, false);
            layers[i].sort(crossingNodeComparator);
            updateXOfLayer(i);

            int z = 0;
            for (LayoutNode n : layers[i]) {
                n.pos = z;
                z++;
            }
        }
    }

    private void assignYCoordinates() {
        int curY = 0;

        for (List<LayoutNode> layer : layers) {
            int maxHeight = 0;
            int baseLine = 0;
            int bottomBaseLine = 0;
            for (LayoutNode n : layer) {
                maxHeight = Math.max(maxHeight, n.height - n.yOffset - n.bottomYOffset);
                baseLine = Math.max(baseLine, n.yOffset);
                bottomBaseLine = Math.max(bottomBaseLine, n.bottomYOffset);
            }

            int maxXOffset = 0;
            for (LayoutNode n : layer) {
                if (n.vertex == null) {
                    // Dummy node
                    n.y = curY;
                    n.height = maxHeight + baseLine + bottomBaseLine;

                } else {
                    n.y = curY + baseLine + (maxHeight - (n.height - n.yOffset - n.bottomYOffset)) / 2 - n.yOffset;
                }

                for (LayoutEdge e : n.succs) {
                    int curXOffset = Math.abs(n.x - e.to.x);
                    maxXOffset = Math.max(curXOffset, maxXOffset);
                }
            }

            curY += maxHeight + baseLine + bottomBaseLine;
            curY += LAYER_OFFSET + ((int) (Math.sqrt(maxXOffset) * 1.5));
        }
    }

    private void createDummyNodes() {

         Comparator<LayoutEdge> comparator = Comparator.comparingInt(e -> e.to.layer);
            HashMap<Integer, List<LayoutEdge>> portHash = new HashMap<>();
            ArrayList<LayoutNode> currentNodes = new ArrayList<>(nodes);
            for (LayoutNode n : currentNodes) {
                portHash.clear();

                ArrayList<LayoutEdge> succs = new ArrayList<>(n.succs);
                for (LayoutEdge e : succs) {
                    if (e.from.layer != e.to.layer - 1) {
                        Integer i = e.relativeFrom;
                        if (!portHash.containsKey(i)) {
                            portHash.put(i, new ArrayList<>());
                        }
                        portHash.get(i).add(e);
                    }
                }

                succs = new ArrayList<>(n.succs);
                for (LayoutEdge e : succs) {

                    Integer i = e.relativeFrom;
                    if (portHash.containsKey(i)) {

                        List<LayoutEdge> list = portHash.get(i);
                        list.sort(comparator);

                        if (list.size() == 1) {
                            processSingleEdge(list.get(0));
                        } else {

                            int maxLayer = list.get(0).to.layer;
                            for (LayoutEdge curEdge : list) {
                                maxLayer = Math.max(maxLayer, curEdge.to.layer);
                            }

                            int cnt = maxLayer - n.layer - 1;
                            LayoutEdge[] edges = new LayoutEdge[cnt];
                            LayoutNode[] nodes = new LayoutNode[cnt];
                            edges[0] = new LayoutEdge();
                            edges[0].from = n;
                            edges[0].relativeFrom = i;
                            edges[0].vip = e.vip;
                            n.succs.add(edges[0]);

                            nodes[0] = new LayoutNode();
                            nodes[0].width = DUMMY_WIDTH;
                            nodes[0].height = DUMMY_HEIGHT;
                            nodes[0].layer = n.layer + 1;
                            nodes[0].preds.add(edges[0]);
                            edges[0].to = nodes[0];
                            edges[0].relativeTo = 0;
                            for (int j = 1; j < cnt; j++) {
                                edges[j] = new LayoutEdge();
                                edges[j].vip = e.vip;
                                edges[j].from = nodes[j - 1];
                                edges[j].relativeFrom = nodes[j - 1].width / 2;
                                nodes[j - 1].succs.add(edges[j]);
                                nodes[j] = new LayoutNode();
                                nodes[j].width = DUMMY_WIDTH;
                                nodes[j].height = DUMMY_HEIGHT;
                                nodes[j].layer = n.layer + j + 1;
                                nodes[j].preds.add(edges[j]);
                                edges[j].to = nodes[j];
                                edges[j].relativeTo = nodes[j].width / 2;
                            }

                            for (LayoutEdge curEdge : list) {
                                LayoutNode anchor = nodes[curEdge.to.layer - n.layer - 2];
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
        LayoutNode n = e.to;
        if (e.to.layer - 1 > e.from.layer) {
            LayoutEdge last = e;
            for (int i = n.layer - 1; i > last.from.layer; i--) {
                last = addBetween(last, i);
            }
        }
    }

    private LayoutEdge addBetween(LayoutEdge e, int layer) {
        LayoutNode n = new LayoutNode();
        n.width = DUMMY_WIDTH;
        n.height = DUMMY_HEIGHT;
        n.layer = layer;
        n.succs.add(e);
        nodes.add(n);
        LayoutEdge result = new LayoutEdge();
        result.vip = e.vip;
        n.preds.add(result);
        result.to = n;
        result.relativeTo = 0;
        result.from = e.from;
        result.relativeFrom = e.relativeFrom;
        result.link = e.link;
        e.relativeFrom = 0;
        e.from.succs.remove(e);
        e.from.succs.add(result);
        e.from = n;
        return result;
    }

    private void assignLayers() {
            assignLayerDownwards();
            assignLayerUpwards();
        }

    private void assignLayerDownwards() {
        ArrayList<LayoutNode> hull = new ArrayList<>();
        for (LayoutNode n : nodes) {
            if (n.preds.isEmpty()) {
                hull.add(n);
                n.layer = 0;
            }
        }

        int z = MIN_LAYER_DIFFERENCE;
        while (!hull.isEmpty()) {
            ArrayList<LayoutNode> newSet = new ArrayList<>();
            for (LayoutNode n : hull) {
                for (LayoutEdge se : n.succs) {
                    LayoutNode s = se.to;
                    if (s.layer == -1) {
                        // This node was not assigned before.
                        boolean assignedPred = true;
                        for (LayoutEdge pe : s.preds) {
                            LayoutNode p = pe.from;
                            if (p.layer == -1 || p.layer >= z) {
                                // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                assignedPred = false;
                                break;
                            }
                        }

                        if (assignedPred) {
                            // This successor node can be assigned.
                            s.layer = z;
                            newSet.add(s);
                        }
                    }
                }
            }

            hull = newSet;
            z += MIN_LAYER_DIFFERENCE;
        }

        layerCount = z - MIN_LAYER_DIFFERENCE;
        for (LayoutNode n : nodes) {
            n.layer = (layerCount - 1 - n.layer);
        }
    }

    private void assignLayerUpwards() {
        ArrayList<LayoutNode> hull = new ArrayList<>();
        for (LayoutNode n : nodes) {
            if (n.succs.isEmpty()) {
                hull.add(n);
            } else {
                n.layer = -1;
            }
        }

        int z = MIN_LAYER_DIFFERENCE;
        while (!hull.isEmpty()) {
            ArrayList<LayoutNode> newSet = new ArrayList<>();
            for (LayoutNode n : hull) {
                if (n.layer < z) {
                    for (LayoutEdge se : n.preds) {
                        LayoutNode s = se.from;
                        if (s.layer == -1) {
                            // This node was not assigned before.
                            boolean assignedSucc = true;
                            for (LayoutEdge pe : s.succs) {
                                LayoutNode p = pe.to;
                                if (p.layer == -1 || p.layer >= z) {
                                    // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                    assignedSucc = false;
                                    break;
                                }
                            }

                            if (assignedSucc) {
                                // This predecessor node can be assigned.
                                s.layer = z;
                                newSet.add(s);
                            }
                        }
                    }
                } else {
                    newSet.add(n);
                }
            }

            hull = newSet;
            z += MIN_LAYER_DIFFERENCE;
        }

        layerCount = z - MIN_LAYER_DIFFERENCE;

        for (LayoutNode n : nodes) {
            n.layer = (layerCount - 1 - n.layer);
        }
    }

    private HashSet<LayoutNode> visited;private HashSet<LayoutNode> active;

    private void reverseEdges() {
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

        // Start DFS and reverse back edges
        visited = new HashSet<>();
        active = new HashSet<>();
        for (LayoutNode node : nodes) {
            DFS(node);
        }

        for (LayoutNode node : nodes) {

            SortedSet<Integer> reversedDown = new TreeSet<>();

            boolean hasSelfEdge = false;
            for (LayoutEdge e : node.succs) {
                if (reversedLinks.contains(e.link)) {
                    reversedDown.add(e.relativeFrom);
                    if (e.from == e.to) {
                        hasSelfEdge = true;
                    }
                }
            }

            // Whether the node has non-self reversed edges going downwards.
            // If so, reversed edges going upwards are drawn to the left.
            boolean hasReversedDown =
                reversedDown.size() > 0 &&
                !(reversedDown.size() == 1 && hasSelfEdge);

            SortedSet<Integer> reversedUp = null;
            if (hasReversedDown) {
                reversedUp = new TreeSet<>();
            } else {
                reversedUp = new TreeSet<>(Collections.reverseOrder());
            }

            for (LayoutEdge e : node.preds) {
                if (reversedLinks.contains(e.link)) {
                    reversedUp.add(e.relativeTo);
                }
            }

            final int offset = X_OFFSET + DUMMY_WIDTH;

            int curY = 0;
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
                startPoints.add(new Point(curWidth, curY));
                startPoints.add(new Point(pos, curY));
                startPoints.add(new Point(pos, reversedDown.size() * offset));
                for (LayoutEdge e : reversedSuccs) {
                    reversedLinkStartPoints.put(e.link, startPoints);
                }

                node.inOffsets.put(pos, -curY);
                curY += offset;
                node.height += offset;
                node.yOffset += offset;
                curWidth -= offset;
            }

            int widthFactor = reversedDown.size();
            if (hasSelfEdge) {
                widthFactor--;
            }
            node.width += widthFactor * offset;

            int curX = 0;
            int minX = 0;
            if (hasReversedDown) {
                minX = -offset * reversedUp.size();
            }

            int oldNodeHeight = node.height;
            for (int pos : reversedUp) {
                ArrayList<LayoutEdge> reversedPreds = new ArrayList<>();
                for (LayoutEdge e : node.preds) {
                    if (e.relativeTo == pos && reversedLinks.contains(e.link)) {
                        if (hasReversedDown) {
                            e.relativeTo = curX - offset;
                        } else {
                            e.relativeTo = node.width + offset;
                        }

                        reversedPreds.add(e);
                    }
                }
                node.height += offset;
                ArrayList<Point> endPoints = new ArrayList<>();

                node.width += offset;
                if (hasReversedDown) {
                    curX -= offset;
                    endPoints.add(new Point(curX, node.height));
                } else {
                    curX += offset;
                    endPoints.add(new Point(node.width, node.height));
                }

                node.outOffsets.put(pos - minX, curX);
                curX += offset;
                node.bottomYOffset += offset;

                endPoints.add(new Point(pos, node.height));
                endPoints.add(new Point(pos, oldNodeHeight));
                for (LayoutEdge e : reversedPreds) {
                    reversedLinkEndPoints.put(e.link, endPoints);
                }
            }

            if (minX < 0) {
                for (LayoutEdge e : node.preds) {
                    e.relativeTo -= minX;
                }

                for (LayoutEdge e : node.succs) {
                    e.relativeFrom -= minX;
                }

                node.xOffset = -minX;
                node.width += -minX;
            }
        }

    }

    private void DFS(LayoutNode startNode) {
        if (visited.contains(startNode)) {
            return;
        }

        Stack<LayoutNode> stack = new Stack<>();
        stack.push(startNode);

        while (!stack.empty()) {
            LayoutNode node = stack.pop();

            if (visited.contains(node)) {
                // Node no longer active
                active.remove(node);
                continue;
            }

            // Repush immediately to know when no longer active
            stack.push(node);
            visited.add(node);
            active.add(node);

            ArrayList<LayoutEdge> succs = new ArrayList<>(node.succs);
            for (LayoutEdge e : succs) {
                if (active.contains(e.to)) {
                    // Encountered back edge
                    reverseEdge(e);
                } else if (!visited.contains(e.to)) {
                    stack.push(e.to);
                }
            }
        }
    }

    private void reverseAllInputs(LayoutNode node) {
        for (LayoutEdge e : node.preds) {
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

    private void reverseEdge(LayoutEdge e) {
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

    private final Comparator<Link> linkComparator = (l1, l2) -> {
        if (l1.isVIP() && !l2.isVIP()) {
            return -1;
        }

        if (!l1.isVIP() && l2.isVIP()) {
            return 1;
        }

        int result = l1.getFrom().getVertex().compareTo(l2.getFrom().getVertex());
        if (result != 0) {
            return result;
        }
        result = l1.getTo().getVertex().compareTo(l2.getTo().getVertex());
        if (result != 0) {
            return result;
        }
        result = l1.getFrom().getRelativePosition().x - l2.getFrom().getRelativePosition().x;
        if (result != 0) {
            return result;
        }
        result = l1.getTo().getRelativePosition().x - l2.getTo().getRelativePosition().x;
        return result;
    };

    private void writeResult() {
        HashMap<Vertex, Point> vertexPositions = new HashMap<>();
        HashMap<Link, List<Point>> linkPositions = new HashMap<>();
        for (Vertex v : graph.getVertices()) {
            LayoutNode n = vertexToLayoutNode.get(v);
            vertexPositions.put(v, new Point(n.x + n.xOffset, n.y + n.yOffset));
        }

        for (LayoutNode n : nodes) {

            for (LayoutEdge e : n.preds) {
                if (e.link != null && !linkPositions.containsKey(e.link)) {
                    ArrayList<Point> points = new ArrayList<>();

                    Point p = new Point(e.to.x + e.relativeTo, e.to.y + e.to.yOffset + e.link.getTo().getRelativePosition().y);
                    points.add(p);
                    if (e.to.inOffsets.containsKey(e.relativeTo)) {
                        points.add(new Point(p.x, p.y + e.to.inOffsets.get(e.relativeTo) + e.link.getTo().getRelativePosition().y));
                    }

                    LayoutNode cur = e.from;
                    LayoutNode other = e.to;
                    LayoutEdge curEdge = e;
                    while (cur.vertex == null && cur.preds.size() != 0) {
                        if (points.size() > 1 && points.get(points.size() - 1).x == cur.x + cur.width / 2 && points.get(points.size() - 2).x == cur.x + cur.width / 2) {
                            points.remove(points.size() - 1);
                        }
                        points.add(new Point(cur.x + cur.width / 2, cur.y + cur.height));
                        if (points.size() > 1 && points.get(points.size() - 1).x == cur.x + cur.width / 2 && points.get(points.size() - 2).x == cur.x + cur.width / 2) {
                            points.remove(points.size() - 1);
                        }
                        points.add(new Point(cur.x + cur.width / 2, cur.y));
                        curEdge = cur.preds.get(0);
                        cur = curEdge.from;
                    }

                    p = new Point(cur.x + curEdge.relativeFrom, cur.y + cur.height - cur.bottomYOffset + (curEdge.link == null ? 0 : curEdge.link.getFrom().getRelativePosition().y));
                    if (curEdge.from.outOffsets.containsKey(curEdge.relativeFrom)) {
                        points.add(new Point(p.x, p.y + curEdge.from.outOffsets.get(curEdge.relativeFrom) + (curEdge.link == null ? 0 : curEdge.link.getFrom().getRelativePosition().y)));
                    }
                    points.add(p);

                    Collections.reverse(points);

                    if (cur.vertex == null && cur.preds.size() == 0) {

                        if (reversedLinkEndPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                points.add(new Point(p1.x + e.to.x, p1.y + e.to.y));
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
                            if (selfEdges.contains(e)) {
                                // For self edges, it is enough with the
                                // start and end points computed by ReverseEdges.
                                points.clear();
                            }
                        }
                        if (reversedLinkStartPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                points.add(new Point(p1.x + cur.x, p1.y + cur.y));
                            }
                        }

                        if (reversedLinkEndPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                points.add(0, new Point(p1.x + other.x, p1.y + other.y));
                            }
                        }

                        linkPositions.put(e.link, points);
                    }
                }
            }

            for (LayoutEdge e : n.succs) {
                if (e.link != null && !linkPositions.containsKey(e.link)) {
                    ArrayList<Point> points = new ArrayList<>();
                    Point p = new Point(e.from.x + e.relativeFrom, e.from.y + e.from.height - e.from.bottomYOffset + e.link.getFrom().getRelativePosition().y);
                    points.add(p);
                    if (e.from.outOffsets.containsKey(e.relativeFrom)) {
                        Point pOffset = new Point(p.x, p.y + e.from.outOffsets.get(e.relativeFrom) +
                                e.link.getFrom().getRelativePosition().y + e.from.yOffset);
                        if (!pOffset.equals(p)) {
                            points.add(pOffset);
                        }
                    }

                    LayoutNode cur = e.to;
                    LayoutNode other = e.from;
                    LayoutEdge curEdge = e;
                    while (cur.vertex == null && !cur.succs.isEmpty()) {
                        if (points.size() > 1 && points.get(points.size() - 1).x == cur.x + cur.width / 2 && points.get(points.size() - 2).x == cur.x + cur.width / 2) {
                            points.remove(points.size() - 1);
                        }
                        points.add(new Point(cur.x + cur.width / 2, cur.y));
                        if (points.size() > 1 && points.get(points.size() - 1).x == cur.x + cur.width / 2 && points.get(points.size() - 2).x == cur.x + cur.width / 2) {
                            points.remove(points.size() - 1);
                        }
                        points.add(new Point(cur.x + cur.width / 2, cur.y + cur.height));
                        if (cur.succs.isEmpty()) {
                            break;
                        }
                        curEdge = cur.succs.get(0);
                        cur = curEdge.to;
                    }

                    p = new Point(cur.x + curEdge.relativeTo, cur.y + cur.yOffset + ((curEdge.link == null) ? 0 : curEdge.link.getTo().getRelativePosition().y));
                    points.add(p);
                    if (curEdge.to.inOffsets.containsKey(curEdge.relativeTo)) {
                        points.add(new Point(p.x, p.y + curEdge.to.inOffsets.get(curEdge.relativeTo) + ((curEdge.link == null) ? 0 : curEdge.link.getTo().getRelativePosition().y)));
                    }

                    if (cur.succs.isEmpty() && cur.vertex == null) {
                        if (reversedLinkStartPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                points.add(0, new Point(p1.x + other.x, p1.y + other.y));
                            }
                        }

                        if (splitEndPoints.containsKey(e.link)) {
                            points.add(null);
                            points.addAll(splitEndPoints.get(e.link));

                            if (reversedLinks.contains(e.link)) {
                                Collections.reverse(points);
                            }
                            linkPositions.put(e.link, points);
                        } else {
                            splitStartPoints.put(e.link, points);
                        }
                    } else {

                        if (reversedLinkStartPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkStartPoints.get(e.link)) {
                                points.add(0, new Point(p1.x + other.x + other.xOffset, p1.y + other.y));
                            }
                        }
                        if (reversedLinkEndPoints.containsKey(e.link)) {
                            for (Point p1 : reversedLinkEndPoints.get(e.link)) {
                                points.add(new Point(p1.x + cur.x + cur.xOffset, p1.y + cur.y));
                            }
                        }
                        if (reversedLinks.contains(e.link)) {
                            Collections.reverse(points);
                        }
                        linkPositions.put(e.link, points);
                    }

                }
            }
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Vertex v : vertexPositions.keySet()) {
            Point p = vertexPositions.get(v);
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
        }

        for (Link l : linkPositions.keySet()) {
            List<Point> points = linkPositions.get(l);
            for (Point p : points) {
                if (p != null) {
                    minX = Math.min(minX, p.x);
                    minY = Math.min(minY, p.y);
                }
            }

        }

        for (Vertex v : vertexPositions.keySet()) {
            Point p = vertexPositions.get(v);
            p.x -= minX;
            p.y -= minY;
            v.setPosition(p);
        }

        for (Link l : linkPositions.keySet()) {
            List<Point> points = linkPositions.get(l);
            for (Point p : points) {
                if (p != null) {
                    p.x -= minX;
                    p.y -= minY;
                }
            }
            l.setControlPoints(points);
            //l.setControlPoints(Arrays.asList( points.get(0), points.get(points.size() - 1)));
        }
    }
}

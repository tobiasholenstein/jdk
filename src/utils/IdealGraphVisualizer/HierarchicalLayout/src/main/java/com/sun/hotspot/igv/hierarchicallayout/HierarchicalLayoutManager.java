/*
 * Copyright (c) 2008, 2023, Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 * @author Thomas Wuerthinger
 */
public class HierarchicalLayoutManager extends LayoutManager {

    public static final int SWEEP_ITERATIONS = 1;
    public static final int CROSSING_ITERATIONS = 2;
    public static final int NODE_OFFSET = 8;
    public static final int LAYER_OFFSET = 8;
    public static final double SCALE_LAYER_PADDING = 1.5;
    private final boolean combine;
    public static final int MIN_LAYER_DIFFERENCE = 1;

    // Algorithm global datastructures
    private Set<Link> reversedLinks;
    private List<LayoutNode> nodes;
    private HashMap<Vertex, LayoutNode> vertexToLayoutNode;
    private HashMap<Link, List<Point>> reversedLinkStartPoints;
    private HashMap<Link, List<Point>> reversedLinkEndPoints;
    private HashMap<Link, List<Point>> splitStartPoints;
    private HashMap<Link, List<Point>> splitEndPoints;
    private LayoutGraph graph;
    private LayoutLayer[] layers;
    private int layerCount;

    public HierarchicalLayoutManager(boolean combineEdges) {
        this.combine = combineEdges;
    }

    public List<LayoutNode> getNodes() {
        return nodes;
    }

    private void removeSelfEdges() {
        for (LayoutNode node : nodes) {
            for (LayoutEdge e : new ArrayList<>(node.getSuccs())) {
                if (e.getTo() == node) {
                    node.getSuccs().remove(e);
                    node.getPreds().remove(e);
                }
            }
        }
    }

    @Override
    public void setCutEdges(boolean enable) {
        maxLayerLength = enable ? 10 : -1;
    }

    @Override
    public void doLayout(LayoutGraph graph) {
        this.graph = graph;

        vertexToLayoutNode = new HashMap<>();
        reversedLinks = new HashSet<>();
        reversedLinkStartPoints = new HashMap<>();
        reversedLinkEndPoints = new HashMap<>();
        nodes = new ArrayList<>();
        splitStartPoints = new HashMap<>();
        splitEndPoints = new HashMap<>();

        // #############################################################
        // Step 1: Build up data structure
        new BuildDatastructure().run();

        removeSelfEdges();

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
        // STEP 7: Assign X coordinates
        new AssignXCoordinates().run();

        // #############################################################
        // STEP 6: Assign Y coordinates
        new AssignYCoordinates().run();

        // #############################################################
        // STEP 8: Write back to interface
        new WriteResult().run();
    }

    private class WriteResult {

        protected void run() {

            HashMap<Vertex, Point> vertexPositions = new HashMap<>();
            HashMap<Link, List<Point>> linkPositions = new HashMap<>();
            for (Vertex v : graph.getVertices()) {
                LayoutNode n = vertexToLayoutNode.get(v);
                assert !vertexPositions.containsKey(v);
                vertexPositions.put(v, new Point(n.getLeft(), n.getTop()));
            }

            for (LayoutNode n : nodes) {

                for (LayoutEdge e : n.getPreds()) {
                    if (e.getLink() != null && !linkPositions.containsKey(e.getLink())) {
                        ArrayList<Point> points = new ArrayList<>();

                        Point p = new Point(e.getToX(), e.getTo().getY() + e.getTo().getTopMargin() + e.getLink().getTo().getRelativePosition().y);
                        points.add(p);
                        if (e.getTo().getInOffsets().containsKey(e.getRelativeToX())) {
                            points.add(new Point(p.x, p.y + e.getTo().getInOffsets().get(e.getRelativeToX()) + e.getLink().getTo().getRelativePosition().y));
                        }

                        LayoutNode cur = e.getFrom();
                        LayoutNode other = e.getTo();
                        LayoutEdge curEdge = e;
                        while (cur.isDummy() && cur.hasPreds()) {
                            if (points.size() > 1 && points.get(points.size() - 1).x == cur.getX() + cur.getWidth() / 2 && points.get(points.size() - 2).x == cur.getX() + cur.getWidth() / 2) {
                                points.remove(points.size() - 1);
                            }
                            points.add(new Point(cur.getX() + cur.getWidth() / 2, cur.getY() + cur.getHeight()));
                            if (points.size() > 1 && points.get(points.size() - 1).x == cur.getX() + cur.getWidth() / 2 && points.get(points.size() - 2).x == cur.getX() + cur.getWidth() / 2) {
                                points.remove(points.size() - 1);
                            }
                            points.add(new Point(cur.getX() + cur.getWidth() / 2, cur.getY()));
                            assert cur.getPreds().size() == 1;
                            curEdge = cur.getPreds().get(0);
                            cur = curEdge.getFrom();
                        }

                        p = new Point(cur.getX() + curEdge.getRelativeFromX(), cur.getY() + cur.getHeight() - cur.getBottomMargin() + (curEdge.getLink() == null ? 0 : curEdge.getLink().getFrom().getRelativePosition().y));
                        if (curEdge.getFrom().getOutOffsets().containsKey(curEdge.getRelativeFromX())) {
                            points.add(new Point(p.x, p.y + curEdge.getFrom().getOutOffsets().get(curEdge.getRelativeFromX()) + (curEdge.getLink() == null ? 0 : curEdge.getLink().getFrom().getRelativePosition().y)));
                        }
                        points.add(p);

                        Collections.reverse(points);

                        if (cur.isDummy() && !cur.hasPreds()) {

                            if (reversedLinkEndPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkEndPoints.get(e.getLink())) {
                                    points.add(new Point(p1.x + e.getTo().getX(), p1.y + e.getTo().getY()));
                                }
                            }

                            if (splitStartPoints.containsKey(e.getLink())) {
                                points.add(0, null);
                                points.addAll(0, splitStartPoints.get(e.getLink()));

                                //checkPoints(points);
                                if (reversedLinks.contains(e.getLink())) {
                                    Collections.reverse(points);
                                }
                                assert !linkPositions.containsKey(e.getLink());
                                linkPositions.put(e.getLink(), points);
                            } else {
                                splitEndPoints.put(e.getLink(), points);
                            }

                        } else {
                            if (reversedLinks.contains(e.getLink())) {
                                Collections.reverse(points);
                            }
                            if (reversedLinkStartPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkStartPoints.get(e.getLink())) {
                                    points.add(new Point(p1.x + cur.getX(), p1.y + cur.getY()));
                                }
                            }

                            if (reversedLinkEndPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkEndPoints.get(e.getLink())) {
                                    points.add(0, new Point(p1.x + other.getX(), p1.y + other.getY()));
                                }
                            }

                            assert !linkPositions.containsKey(e.getLink());
                            linkPositions.put(e.getLink(), points);
                        }
                    }
                }

                for (LayoutEdge e : n.getSuccs()) {
                    if (e.getLink() != null && !linkPositions.containsKey(e.getLink())) {
                        ArrayList<Point> points = new ArrayList<>();
                        Point p = new Point(e.getFromX(), e.getFrom().getY() + e.getFrom().getHeight() - e.getFrom().getBottomMargin() + e.getLink().getFrom().getRelativePosition().y);
                        points.add(p);
                        if (e.getFrom().getOutOffsets().containsKey(e.getRelativeFromX())) {
                            Point pOffset = new Point(p.x, p.y + e.getFrom().getOutOffsets().get(e.getRelativeFromX()) +
                                                      e.getLink().getFrom().getRelativePosition().y + e.getFrom().getTopMargin());
                            if (!pOffset.equals(p)) {
                                points.add(pOffset);
                            }
                        }

                        LayoutNode cur = e.getTo();
                        LayoutNode other = e.getFrom();
                        LayoutEdge curEdge = e;
                        while (cur.isDummy() && cur.hasSuccs()) {
                            if (points.size() > 1 && points.get(points.size() - 1).x == cur.getX() + cur.getWidth() / 2 && points.get(points.size() - 2).x == cur.getX() + cur.getWidth() / 2) {
                                points.remove(points.size() - 1);
                            }
                            points.add(new Point(cur.getX() + cur.getWidth() / 2, cur.getY()));
                            if (points.size() > 1 && points.get(points.size() - 1).x == cur.getX() + cur.getWidth() / 2 && points.get(points.size() - 2).x == cur.getX() + cur.getWidth() / 2) {
                                points.remove(points.size() - 1);
                            }
                            points.add(new Point(cur.getX() + cur.getWidth() / 2, cur.getY() + cur.getHeight()));
                            if (!cur.hasSuccs()) {
                                break;
                            }
                            assert cur.getSuccs().size() == 1;
                            curEdge = cur.getSuccs().get(0);
                            cur = curEdge.getTo();
                        }

                        p = new Point(cur.getX() + curEdge.getRelativeToX(), cur.getY() + cur.getTopMargin() + ((curEdge.getLink() == null) ? 0 : curEdge.getLink().getTo().getRelativePosition().y));
                        points.add(p);
                        if (curEdge.getTo().getInOffsets().containsKey(curEdge.getRelativeToX())) {
                            points.add(new Point(p.x, p.y + curEdge.getTo().getInOffsets().get(curEdge.getRelativeToX()) + ((curEdge.getLink() == null) ? 0 : curEdge.getLink().getTo().getRelativePosition().y)));
                        }

                        if (!cur.hasSuccs() && cur.isDummy()) {
                            if (reversedLinkStartPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkStartPoints.get(e.getLink())) {
                                    points.add(0, new Point(p1.x + other.getX(), p1.y + other.getY()));
                                }
                            }

                            if (splitEndPoints.containsKey(e.getLink())) {
                                points.add(null);
                                points.addAll(splitEndPoints.get(e.getLink()));

                                if (reversedLinks.contains(e.getLink())) {
                                    Collections.reverse(points);
                                }
                                assert !linkPositions.containsKey(e.getLink());
                                linkPositions.put(e.getLink(), points);
                            } else {
                                splitStartPoints.put(e.getLink(), points);
                            }
                        } else {

                            if (reversedLinkStartPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkStartPoints.get(e.getLink())) {
                                    points.add(0, new Point(p1.x + other.getLeft(), p1.y + other.getY()));
                                }
                            }
                            if (reversedLinkEndPoints.containsKey(e.getLink())) {
                                for (Point p1 : reversedLinkEndPoints.get(e.getLink())) {
                                    points.add(new Point(p1.x + cur.getLeft(), p1.y + cur.getY()));
                                }
                            }
                            if (reversedLinks.contains(e.getLink())) {
                                Collections.reverse(points);
                            }
                            assert !linkPositions.containsKey(e.getLink());
                            linkPositions.put(e.getLink(), points);
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

    public static final Comparator<LayoutNode> nodePositionComparator = Comparator.comparingInt(n -> n.getPos());
    public static final Comparator<LayoutNode> nodeProcessingDownComparator = (n1, n2) -> {
        if (n1.isDummy()) {
            if (n2.isDummy()) {
                return 0;
            }
            return -1;
        }
        if (n2.isDummy()) {
            return 1;
        }
        return n1.getPreds().size() - n2.getPreds().size();
    };
    public static final Comparator<LayoutNode> nodeProcessingUpComparator = (n1, n2) -> {
        if (n1.isDummy()) {
            if (n2.isDummy()) {
                return 0;
            }
            return -1;
        }
        if (n2.isDummy()) {
            return 1;
        }
        return n1.getSuccs().size() - n2.getSuccs().size();
    };

    private class AssignXCoordinates {

        private ArrayList<Integer>[] space;
        private ArrayList<LayoutNode>[] downProcessingOrder;
        private ArrayList<LayoutNode>[] upProcessingOrder;

        private void initialPositions() {
            for (LayoutNode n : nodes) {
                n.setX(space[n.getLayer()].get(n.getPos()));
            }
        }

        @SuppressWarnings("unchecked")
        private void createArrays() {
            space = new ArrayList[layers.length];
            downProcessingOrder = new ArrayList[layers.length];
            upProcessingOrder = new ArrayList[layers.length];
        }

        protected void run() {
            createArrays();

            for (int i = 0; i < layers.length; i++) {
                space[i] = new ArrayList<>();
                downProcessingOrder[i] = new ArrayList<>();
                upProcessingOrder[i] = new ArrayList<>();

                int curX = 0;
                for (LayoutNode n : layers[i]) {
                    space[i].add(curX);
                    curX += n.getWidth() + NODE_OFFSET;
                    downProcessingOrder[i].add(n);
                    upProcessingOrder[i].add(n);
                }

                downProcessingOrder[i].sort(nodeProcessingDownComparator);
                upProcessingOrder[i].sort(nodeProcessingUpComparator);
            }

            initialPositions();
            for (int i = 0; i < SWEEP_ITERATIONS; i++) {
                sweepDown();
                adjustSpace();
                sweepUp();
                adjustSpace();
            }

            sweepDown();
            adjustSpace();
            sweepUp();
        }

        private void adjustSpace() {
            for (int i = 0; i < layers.length; i++) {
                for (LayoutNode n : layers[i]) {
                    space[i].add(n.getX());
                }
            }
        }

        private int calculateOptimalDown(LayoutNode n) {
            int size = n.getPreds().size();
            if (size == 0) {
                return n.getX();
            }
            int[] values = new int[size];
            for (int i = 0; i < size; i++) {
                LayoutEdge e = n.getPreds().get(i);
                values[i] = e.getFromX() - e.getRelativeToX();
            }
            return Statistics.median(values);
        }

        private int calculateOptimalBoth(LayoutNode n) {
            if (n.getPreds().size() == n.getSuccs().size()) {
                return n.getX();
            }

            int[] values = new int[n.getPreds().size() + n.getSuccs().size()];
            int i = 0;

            for (LayoutEdge e : n.getPreds()) {
                values[i] = e.getFromX() - e.getRelativeToX();
                i++;
            }

            for (LayoutEdge e : n.getSuccs()) {
                values[i] = e.getToX() - e.getRelativeFromX();
                i++;
            }

            return Statistics.median(values);
        }

        private int calculateOptimalUp(LayoutNode n) {
            int size = n.getSuccs().size();
            if (size == 0) {
                return n.getX();
            }
            int[] values = new int[size];
            for (int i = 0; i < size; i++) {
                LayoutEdge e = n.getSuccs().get(i);
                values[i] = e.getToX() - e.getRelativeFromX();
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
    }

    public static class NodeRow {

        private final TreeSet<LayoutNode> treeSet;
        private final ArrayList<Integer> space;

        public NodeRow(ArrayList<Integer> space) {
            treeSet = new TreeSet<>(nodePositionComparator);
            this.space = space;
        }

        public int offset(LayoutNode n1, LayoutNode n2) {
            int v1 = space.get(n1.getPos()) + n1.getWidth();
            int v2 = space.get(n2.getPos());
            return v2 - v1;
        }

        public void insert(LayoutNode n, int pos) {

            SortedSet<LayoutNode> headSet = treeSet.headSet(n);

            LayoutNode leftNeighbor;
            int minX = Integer.MIN_VALUE;
            if (!headSet.isEmpty()) {
                leftNeighbor = headSet.last();
                minX = leftNeighbor.getX() + leftNeighbor.getWidth() + offset(leftNeighbor, n);
            }

            if (pos < minX) {
                n.setX(minX);
            } else {

                LayoutNode rightNeighbor;
                SortedSet<LayoutNode> tailSet = treeSet.tailSet(n);
                int maxX = Integer.MAX_VALUE;
                if (!tailSet.isEmpty()) {
                    rightNeighbor = tailSet.first();
                    maxX = rightNeighbor.getX() - offset(n, rightNeighbor) - n.getWidth();
                }

                n.setX(Math.min(pos, maxX));

                assert minX <= maxX : minX + " vs " + maxX;
            }

            treeSet.add(n);
        }
    }
    private static final Comparator<LayoutNode> crossingNodeComparator = Comparator.comparingDouble(n -> n.getWeightedPosition());

    private class CrossingReduction {

        @SuppressWarnings("unchecked")
        private void createLayers() {
            layers = new LayoutLayer[layerCount];
            for (int i = 0; i < layerCount; i++) {
                layers[i] = new LayoutLayer();
            }
        }

        protected void run() {
            createLayers();

            // Generate initial ordering
            HashSet<LayoutNode> visited = new HashSet<>();
            for (LayoutNode n : nodes) {
                if (n.getLayer() == 0) {
                    layers[0].add(n);
                    visited.add(n);
                } else if (!n.hasPreds()) {
                    layers[n.getLayer()].add(n);
                    visited.add(n);
                }
            }

            for (int i = 0; i < layers.length - 1; i++) {
                for (LayoutNode n : layers[i]) {
                    for (LayoutEdge e : n.getSuccs()) {
                        if (!visited.contains(e.getTo())) {
                            visited.add(e.getTo());
                            layers[i + 1].add(e.getTo());
                            if (!nodes.contains(e.getTo())) {
                                nodes.add(e.getTo());
                            }
                        }
                    }
                }
            }

            updatePositions();

            initX();

            // Optimize
            for (int i = 0; i < CROSSING_ITERATIONS; i++) {
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
                n.setX(x);
                x += n.getWidth() + NODE_OFFSET;
            }
        }

        private void updatePositions() {
            for (List<LayoutNode> layer : layers) {
                int z = 0;
                for (LayoutNode n : layer) {
                    n.setPos(z);
                    z++;
                }
            }
        }

        private void downSweep() {

            // Downsweep
            for (int i = 1; i < layerCount; i++) {

                for (LayoutNode n : layers[i]) {
                    n.setWeightedPosition(0);
                }

                for (LayoutNode n : layers[i]) {

                    int sum = 0;
                    int count = 0;
                    for (LayoutEdge e : n.getPreds()) {
                        int cur = e.getFromX();
                        int factor = 1;
                        sum += cur * factor;
                        count += factor;
                    }

                    if (count > 0) {
                        sum /= count;
                        n.setWeightedPosition(sum);
                    }
                }

                updateCrossingNumbers(i, true);
                layers[i].sort(crossingNodeComparator);
                updateXOfLayer(i);

                int z = 0;
                for (LayoutNode n : layers[i]) {
                    n.setPos(z);
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

                boolean cond = !n.hasSuccs();
                if (down) {
                    cond = !n.hasPreds();
                }

                if (cond) {
                    if (prev != null && next != null) {
                        n.setWeightedPosition((prev.getWeightedPosition() + next.getWeightedPosition()) / 2);
                    } else if (prev != null) {
                        n.setWeightedPosition(prev.getWeightedPosition());
                    } else if (next != null) {
                        n.setWeightedPosition(next.getWeightedPosition());
                    }
                }
            }
        }

        private void upSweep() {
            // Upsweep
            for (int i = layerCount - 2; i >= 0; i--) {

                for (LayoutNode n : layers[i]) {
                    n.setWeightedPosition(0);
                }

                for (LayoutNode n : layers[i]) {

                    int count = 0;
                    int sum = 0;
                    for (LayoutEdge e : n.getSuccs()) {
                        int cur = e.getToX();
                        int factor = 1;
                        sum += cur * factor;
                        count += factor;
                    }

                    if (count > 0) {
                        sum /= count;
                        n.setWeightedPosition(sum);
                    }

                }

                updateCrossingNumbers(i, false);
                layers[i].sort(crossingNodeComparator);
                updateXOfLayer(i);

                int z = 0;
                for (LayoutNode n : layers[i]) {
                    n.setPos(z);
                    z++;
                }
            }
        }


    }

    private class AssignYCoordinates {

        protected void run() {
            int curY = 0;

            for (List<LayoutNode> layer : layers) {
                int maxHeight = 0;
                int baseLine = 0;
                int bottomBaseLine = 0;
                for (LayoutNode n : layer) {
                    maxHeight = Math.max(maxHeight, n.getHeight() - n.getTopMargin() - n.getBottomMargin());
                    baseLine = Math.max(baseLine, n.getTopMargin());
                    bottomBaseLine = Math.max(bottomBaseLine, n.getBottomMargin());
                }

                int maxXOffset = 0;
                for (LayoutNode n : layer) {
                    if (n.isDummy()) {
                        // Dummy node
                        n.setY(curY);
                        n.setHeight(maxHeight + baseLine + bottomBaseLine);

                    } else {
                        n.setY(curY + baseLine + (maxHeight - (n.getHeight() - n.getTopMargin() - n.getBottomMargin())) / 2 - n.getTopMargin());
                    }

                    for (LayoutEdge e : n.getSuccs()) {
                        int curXOffset = Math.abs(n.getX() - e.getTo().getX());
                        maxXOffset = Math.max(curXOffset, maxXOffset);
                    }
                }

                curY += maxHeight + baseLine + bottomBaseLine;
                curY += LAYER_OFFSET + ((int) (Math.sqrt(maxXOffset) * SCALE_LAYER_PADDING));
            }
        }
    }

    private class CreateDummyNodes {


        protected void run() {

            if (combine) {

                Comparator<LayoutEdge> comparator = Comparator.comparingInt(e -> e.getTo().getLayer());
                HashMap<Integer, List<LayoutEdge>> portHash = new HashMap<>();
                ArrayList<LayoutNode> currentNodes = new ArrayList<>(nodes);
                for (LayoutNode n : currentNodes) {
                    portHash.clear();

                    ArrayList<LayoutEdge> succs = new ArrayList<>(n.getSuccs());
                    HashMap<Integer, LayoutNode> topNodeHash = new HashMap<>();
                    HashMap<Integer, HashMap<Integer, LayoutNode>> bottomNodeHash = new HashMap<>();
                    for (LayoutEdge e : succs) {
                        assert e.getFrom().getLayer() < e.getTo().getLayer();
                        if (e.getFrom().getLayer() != e.getTo().getLayer() - 1) {
                            if (maxLayerLength != -1 && e.getTo().getLayer() - e.getFrom().getLayer() > maxLayerLength) {
                                assert maxLayerLength > 2;
                                e.getTo().getPreds().remove(e);
                                e.getFrom().getSuccs().remove(e);

                                LayoutEdge topEdge;

                                if (topNodeHash.containsKey(e.getRelativeFromX())) {
                                    LayoutNode topNode = topNodeHash.get(e.getRelativeFromX());
                                    topEdge = new LayoutEdge(e.getFrom(), topNode, e.getRelativeFromX(), topNode.getWidth() / 2, e.getLink());
                                    e.getFrom().getSuccs().add(topEdge);
                                    topNode.getPreds().add(topEdge);
                                } else {

                                    LayoutNode topNode = new LayoutNode();
                                    topNode.setLayer(e.getFrom().getLayer() + 1);
                                    nodes.add(topNode);
                                    topEdge = new LayoutEdge(e.getFrom(), topNode, e.getRelativeFromX(), 0, e.getLink());
                                    e.getFrom().getSuccs().add(topEdge);
                                    topNode.getPreds().add(topEdge);
                                    topNodeHash.put(e.getRelativeFromX(), topNode);
                                    bottomNodeHash.put(e.getRelativeFromX(), new HashMap<>());
                                }

                                HashMap<Integer, LayoutNode> hash = bottomNodeHash.get(e.getRelativeFromX());

                                LayoutNode bottomNode;
                                if (hash.containsKey(e.getTo().getLayer())) {
                                    bottomNode = hash.get(e.getTo().getLayer());
                                } else {

                                    bottomNode = new LayoutNode();
                                    bottomNode.setLayer(e.getTo().getLayer() - 1);
                                    nodes.add(bottomNode);
                                    hash.put(e.getTo().getLayer(), bottomNode);
                                }
                                LayoutEdge bottomEdge = new LayoutEdge(bottomNode, e.getTo(), bottomNode.getWidth() / 2, e.getRelativeToX(), e.getLink());
                                e.getTo().getPreds().add(bottomEdge);
                                bottomNode.getSuccs().add(bottomEdge);

                            } else {
                                Integer i = e.getRelativeFromX();
                                if (!portHash.containsKey(i)) {
                                    portHash.put(i, new ArrayList<>());
                                }
                                portHash.get(i).add(e);
                            }
                        }
                    }

                    succs = new ArrayList<>(n.getSuccs());
                    for (LayoutEdge e : succs) {

                        Integer i = e.getRelativeFromX();
                        if (portHash.containsKey(i)) {

                            List<LayoutEdge> list = portHash.get(i);
                            list.sort(comparator);

                            if (list.size() == 1) {
                                processSingleEdge(list.get(0));
                            } else {

                                int maxLayer = list.get(0).getTo().getLayer();
                                for (LayoutEdge curEdge : list) {
                                    maxLayer = Math.max(maxLayer, curEdge.getTo().getLayer());
                                }

                                int cnt = maxLayer - n.getLayer() - 1;
                                LayoutEdge[] edges = new LayoutEdge[cnt];
                                LayoutNode[] nodes = new LayoutNode[cnt];
                                edges[0] = new LayoutEdge(n, null, i, 0, null);
                                n.getSuccs().add(edges[0]);

                                nodes[0] = new LayoutNode();
                                nodes[0].setLayer(n.getLayer() + 1);
                                nodes[0].getPreds().add(edges[0]);
                                edges[0].setTo(nodes[0]);
                                edges[0].setRelativeToX(nodes[0].getWidth() / 2);
                                for (int j = 1; j < cnt; j++) {
                                    nodes[j] = new LayoutNode();
                                    nodes[j].setLayer(n.getLayer() + j + 1);
                                    edges[j] = new LayoutEdge(nodes[j - 1], nodes[j]);
                                    nodes[j - 1].getSuccs().add(edges[j]);
                                    nodes[j].getPreds().add(edges[j]);
                                }

                                for (LayoutEdge curEdge : list) {
                                    assert curEdge.getTo().getLayer() - n.getLayer() - 2 >= 0;
                                    assert curEdge.getTo().getLayer() - n.getLayer() - 2 < cnt;
                                    LayoutNode anchor = nodes[curEdge.getTo().getLayer() - n.getLayer() - 2];
                                    anchor.getSuccs().add(curEdge);
                                    curEdge.setFrom(anchor);
                                    curEdge.setRelativeFromX(anchor.getWidth() / 2);
                                    n.getSuccs().remove(curEdge);
                                }

                            }

                            portHash.remove(i);
                        }
                    }
                }
            } else {
                ArrayList<LayoutNode> currentNodes = new ArrayList<>(nodes);
                for (LayoutNode n : currentNodes) {
                    for (LayoutEdge e : List.copyOf(n.getSuccs())) {
                        processSingleEdge(e);
                    }
                }
            }
        }

        private void processSingleEdge(LayoutEdge e) {
            LayoutNode n = e.getTo();
            if (e.getTo().getLayer() - 1 > e.getFrom().getLayer()) {
                LayoutEdge last = e;
                for (int i = n.getLayer() - 1; i > last.getFrom().getLayer(); i--) {
                    last = addBetween(last, i);
                }
            }
        }

        private LayoutEdge addBetween(LayoutEdge e, int layer) {
            LayoutNode n = new LayoutNode();
            n.setLayer(layer);
            n.getSuccs().add(e);
            nodes.add(n);
            LayoutEdge result = new LayoutEdge(e.getFrom(), n, e.getRelativeFromX(), n.getWidth() / 2, e.getLink());
            n.getPreds().add(result);
            e.setRelativeFromX(n.getWidth() / 2);
            e.getFrom().getSuccs().remove(e);
            e.getFrom().getSuccs().add(result);
            e.setFrom(n);
            return result;
        }

    }

    private class AssignLayers {


        protected void run() {
            assignLayerDownwards();
            assignLayerUpwards();
        }

        private void assignLayerDownwards() {
            ArrayList<LayoutNode> hull = new ArrayList<>();
            for (LayoutNode n : nodes) {
                if (!n.hasPreds()) {
                    hull.add(n);
                    n.setLayer(0);
                }
            }

            int z = MIN_LAYER_DIFFERENCE;
            while (!hull.isEmpty()) {
                ArrayList<LayoutNode> newSet = new ArrayList<>();
                for (LayoutNode n : hull) {
                    for (LayoutEdge se : n.getSuccs()) {
                        LayoutNode s = se.getTo();
                        if (s.getLayer() != -1) {
                            // This node was assigned before.
                        } else {
                            boolean unassignedPred = false;
                            for (LayoutEdge pe : s.getPreds()) {
                                LayoutNode p = pe.getFrom();
                                if (p.getLayer() == -1 || p.getLayer() >= z) {
                                    // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                    unassignedPred = true;
                                    break;
                                }
                            }

                            if (unassignedPred) {
                                // This successor node can not yet be assigned.
                            } else {
                                s.setLayer(z);
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
                n.setLayer((layerCount - 1 - n.getLayer()));
            }
        }

        private void assignLayerUpwards() {
            ArrayList<LayoutNode> hull = new ArrayList<>();
            for (LayoutNode n : nodes) {
                if (!n.hasSuccs()) {
                    hull.add(n);
                } else {
                    n.setLayer(-1);
                }
            }

            int z = MIN_LAYER_DIFFERENCE;
            while (!hull.isEmpty()) {
                ArrayList<LayoutNode> newSet = new ArrayList<>();
                for (LayoutNode n : hull) {
                    if (n.getLayer() < z) {
                        for (LayoutEdge se : n.getPreds()) {
                            LayoutNode s = se.getFrom();
                            if (s.getLayer() != -1) {
                                // This node was assigned before.
                            } else {
                                boolean unassignedSucc = false;
                                for (LayoutEdge pe : s.getSuccs()) {
                                    LayoutNode p = pe.getTo();
                                    if (p.getLayer() == -1 || p.getLayer() >= z) {
                                        // This now has an unscheduled successor or a successor that was scheduled only in this round.
                                        unassignedSucc = true;
                                        break;
                                    }
                                }

                                if (unassignedSucc) {
                                    // This predecessor node can not yet be assigned.
                                } else {
                                    s.setLayer(z);
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
                n.setLayer((layerCount - 1 - n.getLayer()));
            }
        }


    }

    private class ReverseEdges {

        private HashSet<LayoutNode> visited;
        private HashSet<LayoutNode> active;

        protected void run() {

            // Reverse inputs of roots
            for (LayoutNode node : nodes) {
                if (node.getVertex().isRoot()) {
                    boolean ok = true;
                    for (LayoutEdge e : node.getPreds()) {
                        if (e.getFrom().getVertex().isRoot()) {
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
                for (LayoutEdge e : node.getSuccs()) {
                    if (reversedLinks.contains(e.getLink())) {
                        reversedDown.add(e.getRelativeFromX());
                        if (e.getFrom() == e.getTo()) {
                            hasSelfEdge = true;
                        }
                    }
                }

                // Whether the node has non-self reversed edges going downwards.
                // If so, reversed edges going upwards are drawn to the left.
                boolean hasReversedDown =
                        !reversedDown.isEmpty() &&
                    !(reversedDown.size() == 1 && hasSelfEdge);

                SortedSet<Integer> reversedUp = null;
                if (hasReversedDown) {
                    reversedUp = new TreeSet<>();
                } else {
                    reversedUp = new TreeSet<>(Collections.reverseOrder());
                }

                for (LayoutEdge e : node.getPreds()) {
                    if (reversedLinks.contains(e.getLink())) {
                        reversedUp.add(e.getRelativeToX());
                    }
                }

                final int offset = NODE_OFFSET + LayoutNode.DUMMY_WIDTH;

                int curY = 0;
                int curWidth = node.getWidth() + reversedDown.size() * offset;
                for (int pos : reversedDown) {
                    ArrayList<LayoutEdge> reversedSuccs = new ArrayList<>();
                    for (LayoutEdge e : node.getSuccs()) {
                        if (e.getRelativeFromX() == pos && reversedLinks.contains(e.getLink())) {
                            reversedSuccs.add(e);
                            e.setRelativeFromX(curWidth);
                        }
                    }

                    ArrayList<Point> startPoints = new ArrayList<>();
                    startPoints.add(new Point(curWidth, curY));
                    startPoints.add(new Point(pos, curY));
                    startPoints.add(new Point(pos, reversedDown.size() * offset));
                    for (LayoutEdge e : reversedSuccs) {
                        reversedLinkStartPoints.put(e.getLink(), startPoints);
                    }

                    node.getInOffsets().put(pos, -curY);
                    curY += offset;
                    node.setHeight(node.getHeight() + offset);
                    node.setTopMargin(node.getTopMargin() + offset);
                    curWidth -= offset;
                }

                int widthFactor = reversedDown.size();
                if (hasSelfEdge) {
                    widthFactor--;
                }
                node.setWidth(node.getWidth() + widthFactor * offset);

                int curX = 0;
                int minX = 0;
                if (hasReversedDown) {
                    minX = -offset * reversedUp.size();
                }

                int oldNodeHeight = node.getHeight();
                for (int pos : reversedUp) {
                    ArrayList<LayoutEdge> reversedPreds = new ArrayList<>();
                    for (LayoutEdge e : node.getPreds()) {
                        if (e.getRelativeToX() == pos && reversedLinks.contains(e.getLink())) {
                            if (hasReversedDown) {
                                e.setRelativeToX(curX - offset);
                            } else {
                                e.setRelativeToX(node.getWidth() + offset);
                            }

                            reversedPreds.add(e);
                        }
                    }
                    node.setHeight(node.getHeight() + offset);
                    ArrayList<Point> endPoints = new ArrayList<>();

                    node.setWidth(node.getWidth() + offset);
                    if (hasReversedDown) {
                        curX -= offset;
                        endPoints.add(new Point(curX, node.getHeight()));
                    } else {
                        curX += offset;
                        endPoints.add(new Point(node.getWidth(), node.getHeight()));
                    }

                    node.getOutOffsets().put(pos - minX, curX);
                    curX += offset;
                    node.setBottomMargin(node.getBottomMargin() + offset);

                    endPoints.add(new Point(pos, node.getHeight()));
                    endPoints.add(new Point(pos, oldNodeHeight));
                    for (LayoutEdge e : reversedPreds) {
                        reversedLinkEndPoints.put(e.getLink(), endPoints);
                    }
                }

                if (minX < 0) {
                    for (LayoutEdge e : node.getPreds()) {
                        e.setRelativeToX(e.getRelativeToX() - minX);
                    }

                    for (LayoutEdge e : node.getSuccs()) {
                        e.setRelativeFromX(e.getRelativeFromX() - minX);
                    }

                    node.setLeftMargin(-minX);
                    node.setWidth(node.getWidth() + -minX);
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

                ArrayList<LayoutEdge> succs = new ArrayList<>(node.getSuccs());
                for (LayoutEdge e : succs) {
                    if (active.contains(e.getTo())) {
                        assert visited.contains(e.getTo());
                        // Encountered back edge
                        reverseEdge(e);
                    } else if (!visited.contains(e.getTo())) {
                        stack.push(e.getTo());
                    }
                }
            }
        }

        private void reverseAllInputs(LayoutNode node) {
            for (LayoutEdge e : node.getPreds()) {
                assert !reversedLinks.contains(e.getLink());
                reversedLinks.add(e.getLink());
                node.getSuccs().add(e);
                e.getFrom().getPreds().add(e);
                e.getFrom().getSuccs().remove(e);
                int oldRelativeFrom = e.getRelativeFromX();
                int oldRelativeTo = e.getRelativeToX();
                e.setTo(e.getFrom());
                e.setFrom(node);
                e.setRelativeFromX(oldRelativeTo);
                e.setRelativeToX(oldRelativeFrom);
            }
            node.getPreds().clear();
        }

        private void reverseEdge(LayoutEdge e) {
            assert !reversedLinks.contains(e.getLink());
            reversedLinks.add(e.getLink());

            LayoutNode oldFrom = e.getFrom();
            LayoutNode oldTo = e.getTo();
            int oldRelativeFrom = e.getRelativeFromX();
            int oldRelativeTo = e.getRelativeToX();

            e.setFrom(oldTo);
            e.setTo(oldFrom);
            e.setRelativeFromX(oldRelativeTo);
            e.setRelativeToX(oldRelativeFrom);

            oldFrom.getSuccs().remove(e);
            oldFrom.getPreds().add(e);
            oldTo.getPreds().remove(e);
            oldTo.getSuccs().add(e);
        }


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

    private class BuildDatastructure {

        protected void run() {
            // Set up nodes
            List<Vertex> vertices = new ArrayList<>(graph.getVertices());
            // Order roots first to create more natural layer assignments.
            vertices.sort((Vertex a, Vertex b) ->
                    a.isRoot() == b.isRoot() ? a.compareTo(b) : Boolean.compare(b.isRoot(), a.isRoot()));

            for (Vertex v : vertices) {
                LayoutNode node = new LayoutNode(v);
                Dimension size = v.getSize();
                node.setWidth((int) size.getWidth());
                node.setHeight((int) size.getHeight());
                nodes.add(node);
                vertexToLayoutNode.put(v, node);
            }

            // Set up edges
            List<Link> links = new ArrayList<>(graph.getLinks());
            links.sort(linkComparator);
            for (Link l : links) {
                LayoutEdge edge = new LayoutEdge(vertexToLayoutNode.get(l.getFrom().getVertex()),
                                                 vertexToLayoutNode.get(l.getTo().getVertex()),
                                                 l.getFrom().getRelativePosition().x,
                                                 l.getTo().getRelativePosition().x,
                                                 l);
                assert vertexToLayoutNode.containsKey(l.getFrom().getVertex());
                assert vertexToLayoutNode.containsKey(l.getTo().getVertex());
                edge.getFrom().getSuccs().add(edge);
                edge.getTo().getPreds().add(edge);
            }

        }

    }
}

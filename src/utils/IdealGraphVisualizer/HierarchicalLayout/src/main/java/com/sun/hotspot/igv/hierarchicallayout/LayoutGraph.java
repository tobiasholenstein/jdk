/*
 * Copyright (c) 2008, 2022, Oracle and/or its affiliates. All rights reserved.
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

import static com.sun.hotspot.igv.hierarchicallayout.LayoutEdge.LAYOUT_EDGE_LAYER_COMPARATOR;
import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Port;
import com.sun.hotspot.igv.layout.Vertex;
import java.util.*;
import java.util.stream.Collectors;

import static com.sun.hotspot.igv.hierarchicallayout.LayoutNode.NODE_POS_COMPARATOR;

/**
 *
 * @author Thomas Wuerthinger
 */
public class LayoutGraph {

    public static final Comparator<Link> LINK_COMPARATOR =
            Comparator.comparing((Link l) -> l.getFrom().getVertex())
                    .thenComparing(l -> l.getTo().getVertex())
                    .thenComparingInt(l -> l.getFrom().getRelativePosition().x)
                    .thenComparingInt(l -> l.getTo().getRelativePosition().x);

    private final Set<? extends Link> links;
    private final SortedSet<Vertex> vertices;
    private final HashMap<Vertex, List<Port>> inputPorts;
    private final HashMap<Vertex, List<Port>> outputPorts;
    private final HashMap<Port, List<Link>> portLinks;

    private final List<LayoutNode> dummyNodes;
    private final List<LayoutNode> layoutNodes;
    private final LinkedHashMap<Vertex, LayoutNode> vertexToLayoutNode;

    private List<LayoutLayer> layers;

    public LayoutGraph(Collection<? extends Link> links) {
        this(links, new HashSet<>());
    }

    public void initLayers(int layerCount) {
        layers = new ArrayList<>(layerCount);
        for (int i = 0; i < layerCount; i++) {
            layers.add(new LayoutLayer());
        }
    }

    public List<LayoutNode> getDummyNodes() {
        return Collections.unmodifiableList(dummyNodes);
    }

    private LayoutLayer createNewLayer(int layerNr) {
        LayoutLayer layer = new LayoutLayer();
        layers.add(layerNr, layer);

        // update layer field in nodes below layerNr
        for (int l = layerNr + 1; l < getLayerCount(); l++) {
            for (LayoutNode layoutNode : getLayer(l)) {
                layoutNode.setLayer(l);
            }
        }
        return layer;
    }

    private void deleteLayer(int layerNr) {
        layers.remove(layerNr);

        // Update the layer field in nodes below the deleted layer
        for (int l = layerNr; l < getLayerCount(); l++) {
            for (LayoutNode layoutNode : getLayer(l)) {
                layoutNode.setLayer(l);
            }
        }
    }


    // check that NO neighbors of node are in a given layer
    // otherwise insert a new layer
    // return the layerNr where the node can now be safely inserted
    public int insertNewLayerIfNeeded(LayoutNode node, int layerNr) {
        for (Link inputLink : getInputLinks(node.getVertex())) {
            if (inputLink.getFrom().getVertex() == inputLink.getTo().getVertex()) continue;
            LayoutNode fromNode = getLayoutNode(inputLink.getFrom().getVertex());
            if (fromNode.getLayer() == layerNr) {
                moveExpandLayerDown(layerNr + 1);
                return layerNr + 1;
            }
        }
        for (Link outputLink : getOutputLinks(node.getVertex())) {
            if (outputLink.getFrom().getVertex() == outputLink.getTo().getVertex()) continue;
            LayoutNode toNode = getLayoutNode(outputLink.getTo().getVertex());
            if (toNode.getLayer() == layerNr) {
                moveExpandLayerDown(layerNr);
                return layerNr;
            }
        }
        return layerNr;

    }

    // inserts a new layer at layerNr
    // inserts dummy nodes acoring to layerNr - 1
    // moves the layer from previous layerNr to layerNr + 1
    private void moveExpandLayerDown(int layerNr) {
        LayoutLayer newLayer =  createNewLayer(layerNr);

        if (layerNr == 0) return;
        LayoutLayer layerAbove = getLayer(layerNr - 1);

        for (LayoutNode fromNode : layerAbove) {
            int fromX = fromNode.getX();
            Map<Integer, List<LayoutEdge>> successorsByX = fromNode.groupSuccessorsByX();
            fromNode.getSuccs().clear();

            for (Map.Entry<Integer, List<LayoutEdge>> entry : successorsByX.entrySet()) {
                Integer relativeFromX = entry.getKey();
                List<LayoutEdge> edges = entry.getValue();
                LayoutNode dummyNode = new LayoutNode();
                dummyNode.setX(fromX + relativeFromX);
                dummyNode.setLayer(layerNr);
                dummyNode.getSuccs().addAll(edges);
                LayoutEdge dummyEdge = new LayoutEdge(fromNode, dummyNode, relativeFromX, 0, edges.get(0).getLink());
                if (edges.get(0).isReversed()) dummyEdge.reverse();

                fromNode.getSuccs().add(dummyEdge);
                dummyNode.getPreds().add(dummyEdge);
                for (LayoutEdge edge : edges) {
                    edge.setFrom(dummyNode);
                }
                addNodeToLayer(dummyNode, layerNr);
            }
        }

        newLayer.sortNodesByXAndSetPositions();
    }

    public List<LayoutLayer> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public List<LayoutNode> getLayoutNodes() {
        return layoutNodes;
    }

    public LayoutNode getLayoutNode(Vertex vertex) {
        return vertexToLayoutNode.get(vertex);
    }

    public LayoutGraph(Collection<? extends Link> links, Collection<? extends Vertex> additionalVertices) {
        this.links = new HashSet<>(links);

        vertices = new TreeSet<>(additionalVertices);
        portLinks = new HashMap<>(links.size());
        inputPorts = new HashMap<>(links.size());
        outputPorts = new HashMap<>(links.size());

        for (Link link : links) {
            assert link.getFrom() != null;
            assert link.getTo() != null;
            Port fromPort = link.getFrom();
            Port toPort = link.getTo();
            Vertex fromVertex = fromPort.getVertex();
            Vertex toVertex = toPort.getVertex();

            vertices.add(fromVertex);
            vertices.add(toVertex);

            outputPorts.computeIfAbsent(fromVertex, k -> new ArrayList<>()).add(fromPort);
            inputPorts.computeIfAbsent(toVertex, k -> new ArrayList<>()).add(toPort);

            portLinks.computeIfAbsent(fromPort, k -> new ArrayList<>()).add(link);
            portLinks.computeIfAbsent(toPort, k -> new ArrayList<>()).add(link);
        }

        // cleanup
        vertexToLayoutNode = new LinkedHashMap<>();
        dummyNodes = new ArrayList<>();
        layoutNodes = new ArrayList<>();

        // Set up nodes
        for (Vertex v : getVertices()) {
            LayoutNode node = new LayoutNode(v);
            layoutNodes.add(node);
            vertexToLayoutNode.put(v, node);
        }

        // Set up edges
        List<Link> sortedLinks = new ArrayList<>(links);
        sortedLinks.sort(LINK_COMPARATOR);
        for (Link link : links) {
            createLayoutEdge(link);
        }
    }

    public void addNodeToLayer(LayoutNode node, int layerNumber) {
        node.setLayer(layerNumber);
        getLayer(layerNumber).add(node);

        // Register node in the appropriate collection based on its type
        registerNode(node);
    }

    private void registerNode(LayoutNode node) {
        if (node.isDummy()) {
            dummyNodes.add(node);
        } else {
            vertexToLayoutNode.put(node.getVertex(), node);
        }
    }


    public void removeNode(LayoutNode node) {
        int layer = node.getLayer();
        layers.get(layer).remove(node);
        layers.get(layer).updateLayerPositions();
        // Remove node from graph layout
        if (node.isDummy()) {
            dummyNodes.remove(node);
        } else {
            vertexToLayoutNode.remove(node.getVertex());
        }
    }

    public void updatePositions() {
        for (LayoutLayer layer : layers) {
            layer.updateLayerPositions();
        }
    }

    public LayoutEdge createLayoutEdge(Link link) {
        LayoutEdge edge = new LayoutEdge(
                vertexToLayoutNode.get(link.getFrom().getVertex()),
                vertexToLayoutNode.get(link.getTo().getVertex()),
                link.getFrom().getRelativePosition().x,
                link.getTo().getRelativePosition().x,
                link);
        edge.getFrom().getSuccs().add(edge);
        edge.getTo().getPreds().add(edge);
        return edge;
    }

    public Set<? extends Link> getLinks() {
        return links;
    }

    public SortedSet<Vertex> getVertices() {
        return vertices;
    }

    public boolean containsVertex(Vertex vertex) {
        return vertices.contains(vertex);
    }

    public Set<Vertex> findRootVertices() {
        return vertices.stream()
                .filter(v -> inputPorts.getOrDefault(v, Collections.emptyList()).isEmpty())
                .collect(Collectors.toSet());
    }

    public List<Link> getInputLinks(Vertex vertex) {
        List<Link> inputLinks = new ArrayList<>();
        for (Port inputPort : inputPorts.getOrDefault(vertex, Collections.emptyList())) {
            inputLinks.addAll(portLinks.getOrDefault(inputPort, Collections.emptyList()));
        }
        return inputLinks;
    }

    public List<Link> getOutputLinks(Vertex vertex) {
        List<Link> outputLinks = new ArrayList<>();
        for (Port outputPort : outputPorts.getOrDefault(vertex, Collections.emptyList())) {
            outputLinks.addAll(portLinks.getOrDefault(outputPort, Collections.emptyList()));
        }
        return outputLinks;
    }

    private List<Link> getAllLinks(Vertex vertex) {
        List<Link> allLinks = new ArrayList<>();

        for (Port inputPort : inputPorts.getOrDefault(vertex, Collections.emptyList())) {
            allLinks.addAll(portLinks.getOrDefault(inputPort, Collections.emptyList()));
        }

        for (Port outputPort : outputPorts.getOrDefault(vertex, Collections.emptyList())) {
            allLinks.addAll(portLinks.getOrDefault(outputPort, Collections.emptyList()));
        }

        return allLinks;
    }

    private void removeEdges(LayoutNode movedNode) {
        for (Link inputLink : getAllLinks(movedNode.getVertex())) {
            Vertex from = inputLink.getFrom().getVertex();
            Vertex to = inputLink.getTo().getVertex();
            LayoutNode toNode = getLayoutNode(to);
            LayoutNode fromNode = getLayoutNode(from);

            if (toNode.getLayer() < fromNode.getLayer()) {
                // Reversed edge
                toNode = fromNode;
                toNode.getReversedLinkEndPoints().remove(inputLink);
                fromNode.getReversedLinkStartPoints().remove(inputLink);
            }

            // Remove preds-edges bottom up, starting at "to" node
            // Cannot start from "from" node since there might be joint edges
            List<LayoutEdge> toNodePredsEdges = List.copyOf(toNode.getPreds());
            for (LayoutEdge edge : toNodePredsEdges) {
                LayoutNode predNode = edge.getFrom();
                LayoutEdge edgeToRemove;

                if (edge.getLink() != null && edge.getLink().equals(inputLink)) {
                    toNode.getPreds().remove(edge);
                    edgeToRemove = edge;
                } else {
                    // Wrong edge, look at next
                    continue;
                }

                if (!predNode.isDummy() && predNode.getVertex().equals(from)) {
                    // No dummy nodes inbetween 'from' and 'to' vertex
                    predNode.getSuccs().remove(edgeToRemove);
                    break;
                } else {
                    // Must remove edges between dummy nodes
                    boolean found = true;
                    LayoutNode succNode = toNode;
                    while (predNode.isDummy() && found) {
                        found = false;

                        if (predNode.getSuccs().size() <= 1 && predNode.getPreds().size() <= 1) {
                            // Dummy node used only for this link, remove if not already removed
                            removeNode(predNode);
                        } else {
                            // anchor node, should not be removed
                            break;
                        }

                        if (predNode.getPreds().size() == 1) {
                            predNode.getSuccs().remove(edgeToRemove);
                            succNode = predNode;
                            edgeToRemove = predNode.getPreds().get(0);
                            predNode = edgeToRemove.getFrom();
                            found = true;
                        }
                    }

                    predNode.getSuccs().remove(edgeToRemove);
                    succNode.getPreds().remove(edgeToRemove);
                }
                break;
            }
        }

        // remove link connected to movedNode
        for (Link link : getLinks()) {
            if (link.getTo().getVertex() == movedNode.getVertex()) {
                link.setControlPoints(new ArrayList<>());
                movedNode.getReversedLinkStartPoints().remove(link);
            } else if (link.getFrom().getVertex() == movedNode.getVertex()) {
                link.setControlPoints(new ArrayList<>());
                movedNode.getReversedLinkEndPoints().remove(link);
            }
        }

        movedNode.initSize();
    }

    public void removeNodeAndEdges(LayoutNode node) {
        removeEdges(node);
        removeNode(node);
    }


    public LayoutLayer getLayer(int layerNr) {
        return layers.get(layerNr);
    }

    public int findLayer(int y) {
        int optimalLayer = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int l = 0; l < getLayerCount(); l++) {
            // Check if y is within this layer's bounds
            if (y >= getLayer(l).getTop() && y <= getLayer(l).getBottom()) {
                return l;
            }

            int distance = Math.abs(getLayer(l).getCenter() - y);
            if (distance < minDistance) {
                minDistance = distance;
                optimalLayer = l;
            }
        }
        return optimalLayer;
    }

    public void positionLayers() {
        int currentY = 0;
        for (LayoutLayer layer : getLayers()) {
            layer.setTop(currentY);

            // Calculate the maximum layer height and set it for the layer
            int maxLayerHeight = layer.calculateMaxLayerHeight();
            layer.setHeight(maxLayerHeight);

            // Center nodes vertically within the layer
            layer.centerNodesVertically();

            // Update currentY to account for the padded bottom of this layer
            currentY += layer.calculateScalePaddedBottom();
        }
    }

    public void optimizeBackEdgeCrossings() {
        for (LayoutNode node : getLayoutNodes()) {
            if (node.getReversedLinkStartPoints().isEmpty() && node.getReversedLinkEndPoints().isEmpty()) continue;
            node.computeReversedLinkPoints();
        }
    }

    public void removeEmptyLayers() {
        int i = 0;
        while (i < getLayerCount()) {
            LayoutLayer layer = getLayer(i);
            if (layer.isDummyLayer()) {
                removeEmptyLayer(i);
            } else {
                i++; // Move to the next layer only if no removal occurred
            }
        }
    }

    private void removeEmptyLayer(int layerNr) {
        LayoutLayer layer = getLayer(layerNr);
        if (!layer.isDummyLayer()) return;

        for (LayoutNode dummyNode : layer) {
            if (dummyNode.getSuccs().isEmpty()) {
                dummyNode.setLayer(layerNr + 1);
                getLayer(layerNr + 1).add(dummyNode);
                dummyNode.setX(dummyNode.calculateOptimalPositionDown());
                getLayer(layerNr + 1).sortNodesByXAndSetPositions();
                continue;
            } else if (dummyNode.getPreds().isEmpty()) {
                dummyNode.setLayer(layerNr - 1);
                dummyNode.setX(dummyNode.calculateOptimalPositionUp());
                getLayer(layerNr - 1).add(dummyNode);
                getLayer(layerNr - 1).sortNodesByXAndSetPositions();
                continue;
            }
            LayoutEdge layoutEdge = dummyNode.getPreds().get(0);

            // remove the layoutEdge
            LayoutNode fromNode = layoutEdge.getFrom();
            fromNode.getSuccs().remove(layoutEdge);

            List<LayoutEdge> successorEdges = dummyNode.getSuccs();
            for (LayoutEdge successorEdge : successorEdges) {
                successorEdge.setRelativeFromX(layoutEdge.getRelativeFromX());
                successorEdge.setFrom(fromNode);
                fromNode.getSuccs().add(successorEdge);
            }
            dummyNode.getPreds().clear();
            dummyNode.getSuccs().clear();
            dummyNodes.remove(dummyNode);
        }

        deleteLayer(layerNr);
    }

    /**
     * Repositions the given LayoutNode to the specified x-coordinate within its layer,
     * ensuring no overlap with adjacent nodes and maintaining a minimum NODE_OFFSET distance.
     *
     * @param layoutNode  The LayoutNode to be repositioned.
     * @param newX        The desired new x-coordinate for the layoutNode.
     */
    private void repositionLayoutNodeX(LayoutNode layoutNode, int newX) {
        int currentX = layoutNode.getX();

        // Early exit if the desired position is the same as the current position
        if (newX == currentX) {
            return;
        }

        LayoutLayer layer = getLayer(layoutNode.getLayer());
        if (newX > currentX) {
            layer.attemptMoveRight(layoutNode, newX);
        } else {
            layer.attemptMoveLeft(layoutNode, newX);
        }
    }

    /**
     * Aligns the x-coordinate of a single dummy successor node for the given LayoutNode.
     * If the node has exactly one successor and that successor is a dummy node,
     * this method sets the dummy node's x-coordinate to either the node's x-coordinate
     * (if the node is a dummy) or to the starting x-coordinate of the connecting edge.
     *
     * @param node The LayoutNode whose single dummy successor needs to be aligned.
     */
    private void alignSingleSuccessorDummyNodeX(LayoutNode node) {
        // Retrieve the list of successor edges
        List<LayoutEdge> successors = node.getSuccs();

        // Proceed only if there is exactly one successor
        if (successors.size() != 1) {
            return;
        }

        LayoutEdge successorEdge = successors.get(0);
        LayoutNode successorNode = successorEdge.getTo();

        // Proceed only if the successor node is a dummy node
        if (!successorNode.isDummy()) {
            return;
        }

        // Determine the target x-coordinate based on whether the current node is a dummy
        int targetX = node.isDummy() ? node.getX() : successorEdge.getStartX();

        // Align the successor dummy node to the target x-coordinate
        repositionLayoutNodeX(successorNode, targetX);
    }

    /**
     * Aligns the x-coordinates of dummy successor nodes within the specified layer.
     * Performs alignment in both forward and backward directions to ensure consistency.
     *
     * @param layer The LayoutLayer whose nodes' dummy successors need alignment.
     */
    private void alignLayerDummySuccessors(LayoutLayer layer) {
        // Forward pass: Align dummy successors from the first node to the last.
        for (LayoutNode node : layer) {
            alignSingleSuccessorDummyNodeX(node);
        }

        // Backward pass: Align dummy successors from the last node to the first.
        for (int i = layer.size() - 1; i >= 0; i--) {
            LayoutNode node = layer.get(i);
            alignSingleSuccessorDummyNodeX(node);
        }
    }

    /**
     * Aligns the x-coordinates of dummy successor nodes across all layers.
     * Performs alignment in both forward and backward directions for comprehensive coverage.
     */
    public void straightenEdges() {
        // Forward pass: Align dummy successors from the first layer to the last.
        for (int i = 0; i < getLayerCount(); i++) {
            alignLayerDummySuccessors(getLayer(i));
        }

        // Backward pass: Align dummy successors from the last layer to the first.
        for (int i = getLayerCount() - 1; i >= 0; i--) {
            alignLayerDummySuccessors(getLayer(i));
        }
    }


    private int optimalPosition(LayoutNode node, int layerNr) {

        getLayer(layerNr).sort(NODE_POS_COMPARATOR);
        int edgeCrossings = Integer.MAX_VALUE;
        int optimalPos = -1;

        for (int i = 0; i < getLayer(layerNr).size() + 1; i++) {
            int xCoord;
            if (i == 0) {
                xCoord = getLayer(layerNr).get(i).getX() - node.getWidth() - 1;
            } else {
                xCoord = getLayer(layerNr).get(i - 1).getX() + getLayer(layerNr).get(i - 1).getWidth() + 1;
            }

            int currentCrossings = 0;

            if (0 <= layerNr - 1) {
                for (LayoutEdge edge : node.getPreds()) {
                    if (edge.getFrom().getLayer() == layerNr - 1) {
                        int fromNodeXCoord = edge.getFromX();
                        int toNodeXCoord = xCoord;
                        if (!node.isDummy()) {
                            toNodeXCoord += edge.getRelativeToX();
                        }
                        for (LayoutNode n : getLayer(layerNr - 1)) {
                            for (LayoutEdge e : n.getSuccs()) {
                                if (e.getTo() == null) {
                                    continue;
                                }
                                int compFromXCoord = e.getFromX();
                                int compToXCoord = e.getToX();
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
            // Edge crossings across current layerNr and layerNr below
            if (layerNr + 1 < getLayerCount()) {
                // For each link with an end point in vertex, check how many edges cross it
                for (LayoutEdge edge : node.getSuccs()) {
                    if (edge.getTo().getLayer() == layerNr + 1) {
                        int toNodeXCoord = edge.getToX();
                        int fromNodeXCoord = xCoord;
                        if (!node.isDummy()) {
                            fromNodeXCoord += edge.getRelativeFromX();
                        }
                        for (LayoutNode n : getLayer(layerNr + 1)) {
                            for (LayoutEdge e : n.getPreds()) {
                                if (e.getFrom() == null) {
                                    continue;
                                }
                                int compFromXCoord = e.getFromX();
                                int compToXCoord = e.getToX();
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
        return optimalPos;
    }


    public void createAndReverseLayoutEdges(LayoutNode node) {
        List<Link> nodeLinks = new ArrayList<>(getInputLinks(node.getVertex()));
        nodeLinks.addAll(getOutputLinks(node.getVertex()));
        nodeLinks.sort(LINK_COMPARATOR);

        List<LayoutNode> reversedLayoutNodes = new ArrayList<>();
        for (Link link : nodeLinks) {
            if (link.getFrom().getVertex() == link.getTo().getVertex()) continue;
            LayoutEdge layoutEdge = createLayoutEdge(link);

            LayoutNode fromNode = layoutEdge.getFrom();
            LayoutNode toNode = layoutEdge.getTo();

            if (fromNode.getLayer() > toNode.getLayer()) {
                HierarchicalLayoutManager.ReverseEdges.reverseEdge(layoutEdge);
                reversedLayoutNodes.add(fromNode);
                reversedLayoutNodes.add(toNode);
            }
        }

        // ReverseEdges
        for (LayoutNode layoutNode : reversedLayoutNodes) {
            layoutNode.computeReversedLinkPoints();
        }
    }

    public void createDummiesForNodePredecessor(LayoutNode layoutNode) {
        for (LayoutEdge predEdge : layoutNode.getPreds()) {
            LayoutNode fromNode = predEdge.getFrom();
            LayoutNode toNode = predEdge.getTo();
            if (Math.abs(toNode.getLayer() - fromNode.getLayer()) <= 1) continue;

            boolean hasEdgeFromSamePort = false;
            LayoutEdge edgeFromSamePort = new LayoutEdge(fromNode, toNode, predEdge.getLink());
            if (predEdge.isReversed()) edgeFromSamePort.reverse();

            for (LayoutEdge succEdge : fromNode.getSuccs()) {
                if (succEdge.getRelativeFromX() == predEdge.getRelativeFromX() && succEdge.getTo().isDummy()) {
                    edgeFromSamePort = succEdge;
                    hasEdgeFromSamePort = true;
                    break;
                }
            }

            if (hasEdgeFromSamePort) {
                LayoutEdge curEdge = edgeFromSamePort;
                boolean newEdge = true;
                while (curEdge.getTo().getLayer() < toNode.getLayer() - 1 && curEdge.getTo().isDummy() && newEdge) {
                    // Traverse down the chain of dummy nodes linking together the edges originating
                    // from the same port
                    newEdge = false;
                    if (curEdge.getTo().getSuccs().size() == 1) {
                        curEdge = curEdge.getTo().getSuccs().get(0);
                        newEdge = true;
                    } else {
                        for (LayoutEdge e : curEdge.getTo().getSuccs()) {
                            if (e.getTo().isDummy()) {
                                curEdge = e;
                                newEdge = true;
                                break;
                            }
                        }
                    }
                }

                LayoutNode prevDummy;
                if (!curEdge.getTo().isDummy()) {
                    prevDummy = curEdge.getFrom();
                } else {
                    prevDummy = curEdge.getTo();
                }

                predEdge.setFrom(prevDummy);
                predEdge.setRelativeFromX(prevDummy.getWidth() / 2);
                fromNode.getSuccs().remove(predEdge);
                prevDummy.getSuccs().add(predEdge);
            }

            LayoutNode layoutNode1 = predEdge.getTo();
            if (predEdge.getTo().getLayer() - 1 > predEdge.getFrom().getLayer()) {
                LayoutEdge prevEdge = predEdge;
                for (int l = layoutNode1.getLayer() - 1; l > prevEdge.getFrom().getLayer(); l--) {
                    LayoutNode dummyNode = new LayoutNode();
                    dummyNode.getSuccs().add(prevEdge);
                    LayoutEdge result = new LayoutEdge(prevEdge.getFrom(), dummyNode, prevEdge.getRelativeFromX(), 0, prevEdge.getLink());
                    if (prevEdge.isReversed()) result.reverse();
                    dummyNode.getPreds().add(result);
                    prevEdge.setRelativeFromX(0);
                    prevEdge.getFrom().getSuccs().remove(prevEdge);
                    prevEdge.getFrom().getSuccs().add(result);
                    prevEdge.setFrom(dummyNode);
                    dummyNode.setLayer(l);
                    List<LayoutNode> layerNodes = getLayer(l);
                    if (layerNodes.isEmpty()) {
                        dummyNode.setPos(0);
                    } else {
                        dummyNode.setPos(optimalPosition(dummyNode, l));
                    }
                    for (LayoutNode n : layerNodes) {
                        if (n.getPos() >= dummyNode.getPos()) {
                            n.setPos(n.getPos() + 1);
                        }
                    }
                    addNodeToLayer(dummyNode, l);
                    prevEdge = dummyNode.getPreds().get(0);
                }
            }
        }
    }

    public void createDummiesForNodeSuccessor(LayoutNode layoutNode, int maxLayerLength) {
        HashMap<Integer, List<LayoutEdge>> portsToUnprocessedEdges = new HashMap<>();
        ArrayList<LayoutEdge> succs = new ArrayList<>(layoutNode.getSuccs());
        HashMap<Integer, LayoutNode> portToTopNode = new HashMap<>();
        HashMap<Integer, HashMap<Integer, LayoutNode>> portToBottomNodeMapping = new HashMap<>();
        for (LayoutEdge succEdge : succs) {
            int startPort = succEdge.getRelativeFromX();
            LayoutNode fromNode = succEdge.getFrom();
            LayoutNode toNode = succEdge.getTo();

            // edge is longer than one layer => needs dummy nodes
            if (fromNode.getLayer() != toNode.getLayer() - 1) {
                // the edge needs to be cut
                if (maxLayerLength != -1 && toNode.getLayer() - fromNode.getLayer() > maxLayerLength) {
                    // remove the succEdge before replacing it
                    toNode.getPreds().remove(succEdge);
                    fromNode.getSuccs().remove(succEdge);

                    LayoutNode topCutNode = portToTopNode.get(startPort);
                    if (topCutNode == null) {
                        topCutNode = new LayoutNode();
                        topCutNode.setLayer(fromNode.getLayer() + 1);
                        addNodeToLayer(topCutNode, topCutNode.getLayer());
                        portToTopNode.put(startPort, topCutNode);
                        portToBottomNodeMapping.put(startPort, new HashMap<>());
                    }
                    LayoutEdge edgeToTopCut = new LayoutEdge(fromNode, topCutNode, succEdge.getRelativeFromX(), topCutNode.getWidth() / 2, succEdge.getLink());
                    if (succEdge.isReversed()) edgeToTopCut.reverse();
                    fromNode.getSuccs().add(edgeToTopCut);
                    topCutNode.getPreds().add(edgeToTopCut);

                    HashMap<Integer, LayoutNode> layerToBottomNode = portToBottomNodeMapping.get(startPort);
                    LayoutNode bottomCutNode = layerToBottomNode.get(toNode.getLayer());
                    if (bottomCutNode == null) {
                        bottomCutNode = new LayoutNode();
                        bottomCutNode.setLayer(toNode.getLayer() - 1);
                        addNodeToLayer(bottomCutNode, bottomCutNode.getLayer());
                        layerToBottomNode.put(toNode.getLayer(), bottomCutNode);
                    }
                    LayoutEdge bottomEdge = new LayoutEdge(bottomCutNode, toNode, bottomCutNode.getWidth() / 2, succEdge.getRelativeToX(), succEdge.getLink());
                    if (succEdge.isReversed()) bottomEdge.reverse();
                    toNode.getPreds().add(bottomEdge);
                    bottomCutNode.getSuccs().add(bottomEdge);

                } else { // the edge is not cut, but needs dummy nodes
                    portsToUnprocessedEdges.putIfAbsent(startPort, new ArrayList<>());
                    portsToUnprocessedEdges.get(startPort).add(succEdge);
                }
            }
        }

        for (Map.Entry<Integer, List<LayoutEdge>> portToUnprocessedEdges : portsToUnprocessedEdges.entrySet()) {
            Integer startPort = portToUnprocessedEdges.getKey();
            List<LayoutEdge> unprocessedEdges = portToUnprocessedEdges.getValue();
            unprocessedEdges.sort(LAYOUT_EDGE_LAYER_COMPARATOR);

            if (unprocessedEdges.size() == 1) {
                // process a single edge
                LayoutEdge singleEdge = unprocessedEdges.get(0);
                LayoutNode fromNode = singleEdge.getFrom();
                if (singleEdge.getTo().getLayer() > fromNode.getLayer() + 1) {
                    LayoutEdge previousEdge = singleEdge;
                    for (int i = fromNode.getLayer() + 1; i < previousEdge.getTo().getLayer(); i++) {
                        LayoutNode dummyNode = new LayoutNode();
                        dummyNode.setLayer(i);
                        dummyNode.getPreds().add(previousEdge);
                        addNodeToLayer(dummyNode, dummyNode.getLayer());
                        LayoutEdge dummyEdge = new LayoutEdge(dummyNode, previousEdge.getTo(), dummyNode.getWidth() / 2, previousEdge.getRelativeToX(), singleEdge.getLink());
                        if (previousEdge.isReversed()) dummyEdge.reverse();
                        dummyNode.getSuccs().add(dummyEdge);
                        previousEdge.setRelativeToX(dummyNode.getWidth() / 2);
                        previousEdge.getTo().getPreds().remove(previousEdge);
                        previousEdge.getTo().getPreds().add(dummyEdge);
                        previousEdge.setTo(dummyNode);
                        previousEdge = dummyEdge;
                    }
                }
            } else {
                int lastLayer = unprocessedEdges.get(unprocessedEdges.size() - 1).getTo().getLayer();
                int dummyCnt = lastLayer - layoutNode.getLayer() - 1;
                LayoutEdge[] newDummyEdges = new LayoutEdge[dummyCnt];
                LayoutNode[] newDummyNodes = new LayoutNode[dummyCnt];

                newDummyNodes[0] = new LayoutNode();
                newDummyNodes[0].setLayer(layoutNode.getLayer() + 1);
                newDummyEdges[0] = new LayoutEdge(layoutNode, newDummyNodes[0], startPort, newDummyNodes[0].getWidth() / 2, null);
                newDummyNodes[0].getPreds().add(newDummyEdges[0]);
                layoutNode.getSuccs().add(newDummyEdges[0]);
                for (int j = 1; j < dummyCnt; j++) {
                    newDummyNodes[j] = new LayoutNode();
                    newDummyNodes[j].setLayer(layoutNode.getLayer() + j + 1);
                    newDummyEdges[j] = new LayoutEdge(newDummyNodes[j - 1], newDummyNodes[j], null);
                    newDummyNodes[j].getPreds().add(newDummyEdges[j]);
                    newDummyNodes[j - 1].getSuccs().add(newDummyEdges[j]);
                }
                for (LayoutEdge unprocessedEdge : unprocessedEdges) {
                    LayoutNode anchorNode = newDummyNodes[unprocessedEdge.getTo().getLayer() - layoutNode.getLayer() - 2];
                    anchorNode.getSuccs().add(unprocessedEdge);
                    unprocessedEdge.setFrom(anchorNode);
                    unprocessedEdge.setRelativeFromX(anchorNode.getWidth() / 2);
                    layoutNode.getSuccs().remove(unprocessedEdge);
                }
                for (LayoutNode dummyNode : newDummyNodes) {
                    addNodeToLayer(dummyNode, dummyNode.getLayer());
                }
            }
        }
    }

    public void addEdges(LayoutNode node, int maxLayerLength) {
        createAndReverseLayoutEdges(node);
        createDummiesForNodeSuccessor(node, maxLayerLength);
        createDummiesForNodePredecessor(node);
        updatePositions();
    }
}

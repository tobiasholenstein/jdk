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

import static com.sun.hotspot.igv.hierarchicallayout.LayoutManager.NODE_OFFSET;
import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Port;
import com.sun.hotspot.igv.layout.Vertex;
import java.util.*;
import java.util.stream.Collectors;

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
    private final HashMap<Vertex, Set<Port>> inputPorts;
    private final HashMap<Vertex, Set<Port>> outputPorts;
    private final HashMap<Port, Set<Link>> portLinks;

    private final List<LayoutNode> dummyNodes;
    private final LinkedHashMap<Vertex, LayoutNode> vertexToLayoutNode;

    private List<LayoutLayer> layers;

    public LayoutGraph(Set<? extends Link> links) {
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

    public LayoutLayer createNewLayer(int layerNr) {
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

    public List<LayoutLayer> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public Collection<LayoutNode> getLayoutNodes() {
        return vertexToLayoutNode.values();
    }

    public LayoutNode getLayoutNode(Vertex vertex) {
        return vertexToLayoutNode.get(vertex);
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

    public LayoutGraph(Set<? extends Link> links, Set<? extends Vertex> additionalVertices) {
        this.links = links;

        vertices = new TreeSet<>(additionalVertices);
        portLinks = new HashMap<>(links.size());
        inputPorts = new HashMap<>(links.size());
        outputPorts = new HashMap<>(links.size());

        for (Link link : links) {
            if (link.getFrom() == null || link.getTo() == null) {
                continue;
            }
            Port fromPort = link.getFrom();
            Port toPort = link.getTo();
            Vertex fromVertex = fromPort.getVertex();
            Vertex toVertex = toPort.getVertex();

            vertices.add(fromVertex);
            vertices.add(toVertex);

            outputPorts.computeIfAbsent(fromVertex, k -> new HashSet<>()).add(fromPort);
            inputPorts.computeIfAbsent(toVertex, k -> new HashSet<>()).add(toPort);

            portLinks.computeIfAbsent(fromPort, k -> new HashSet<>()).add(link);
            portLinks.computeIfAbsent(toPort, k -> new HashSet<>()).add(link);
        }

        // cleanup
        vertexToLayoutNode = new LinkedHashMap<>();
        dummyNodes = new ArrayList<>();


        // Set up nodes
        for (Vertex v : getVertices()) {
            LayoutNode node = new LayoutNode(v);
            vertexToLayoutNode.put(v, node);
        }

        // Set up edges
        List<Link> sortedLinks = new ArrayList<>(links);
        sortedLinks.sort(LINK_COMPARATOR);
        for (Link link : links) {
            createLayoutEdge(link);
        }
    }

    public void addNode(LayoutNode node) {
        int layer = node.getLayer();
        layers.get(layer).add(node);
        layers.get(layer).updateLayerPositions();

        if (node.isDummy()) {
            dummyNodes.add(node);
        } else {
            vertexToLayoutNode.put(node.getVertex(), node);
        }
    }

    public void insertNodeAtPos(LayoutNode node, int layerNr, int pos) {
        node.setPos(pos);
        node.setX(0);

        LayoutLayer layer = getLayer(layerNr);

        // update pos of the nodes right (and including) of pos
        for (LayoutNode n : layer) {
            if (n.getPos() >= pos) {
                n.setPos(n.getPos() + 1);
            }
        }

        // insert in layer at pos
        if (pos < layer.size()) {
            layer.add(pos, node);
        } else {
            layer.add(node);
        }

        int minX = node.getPos() == 0 ? 0 : layer.get(node.getPos() - 1).getOuterRight() + NODE_OFFSET;
        node.setX(Math.max(node.getX(), minX));

        // update x of the nodes right of inserted node at pos
        int prevRightSide = node.getOuterRight();
        for (LayoutNode n : layer) {
            if (n.getPos() > pos) {
                n.setX(Math.max(n.getX(), prevRightSide + NODE_OFFSET));
                prevRightSide = n.getOuterRight();
            }
        }

        addNode(node);
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

    public Set<Vertex> findRootVertices() {
        return vertices.stream()
                .filter(v -> inputPorts.getOrDefault(v, Collections.emptySet()).isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<Link> getInputLinks(Vertex vertex) {
        Set<Link> inputLinks = new HashSet<>();
        for (Port inputPort : inputPorts.getOrDefault(vertex, Collections.emptySet())) {
            inputLinks.addAll(portLinks.getOrDefault(inputPort, Collections.emptySet()));
        }
        return inputLinks;
    }

    public Set<Link> getOutputLinks(Vertex vertex) {
        Set<Link> outputLinks = new HashSet<>();
        for (Port outputPort : outputPorts.getOrDefault(vertex, Collections.emptySet())) {
            outputLinks.addAll(portLinks.getOrDefault(outputPort, Collections.emptySet()));
        }
        return outputLinks;
    }

    private Set<Link> getAllLinks(Vertex vertex) {
        Set<Link> allLinks = new HashSet<>();

        for (Port inputPort : inputPorts.getOrDefault(vertex, Collections.emptySet())) {
            allLinks.addAll(portLinks.getOrDefault(inputPort, Collections.emptySet()));
        }

        for (Port outputPort : outputPorts.getOrDefault(vertex, Collections.emptySet())) {
            allLinks.addAll(portLinks.getOrDefault(outputPort, Collections.emptySet()));
        }

        return allLinks;
    }

    private void removeEdges(LayoutNode node) {
        for (Link inputLink : getAllLinks(node.getVertex())) {
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
                            if (getDummyNodes().contains(predNode)) {
                                removeNode(predNode);
                            }
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
            if (link.getTo().getVertex() == node.getVertex()) {
                link.setControlPoints(new ArrayList<>());
                node.getReversedLinkStartPoints().remove(link);
            } else if (link.getFrom().getVertex() == node.getVertex()) {
                link.setControlPoints(new ArrayList<>());
                node.getReversedLinkEndPoints().remove(link);
            }
        }

        assert node.getPreds().isEmpty();
        assert node.getSuccs().isEmpty();

        node.initSize();
    }

    public void removeNodeAndEdges(LayoutNode node) {
        removeEdges(node);
        removeNode(node);
    }


    public LayoutLayer getLayer(int layerNr) {
        return layers.get(layerNr);
    }

    public int getLayerNr(LayoutLayer layer) {
        return layers.indexOf(layer);
    }

    public int findLayer(int y) {
        int optimalLayer = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int l = 0; l < getLayerCount(); l++) {
            int layerY = getLayer(l).getCenter();
            int distance = Math.abs(layerY - y);
            if (distance < minDistance) {
                minDistance = distance;
                optimalLayer = l;
            }
        }
        return optimalLayer;
    }

    public void insertNodeIntoLayer(LayoutNode node, LayoutLayer layer) {
        int layerNr = getLayerNr(layer);
        node.setLayer(layerNr);

        for (LayoutNode n : layer) {
            if (n.getPos() >= node.getPos()) {
                n.setPos(n.getPos() + 1);
            }
        }

        addNode(node);

        if (!node.isDummy()) {
            vertexToLayoutNode.put(node.getVertex(), node);
        }
    }

    public void removeLayer(int removeLayerNr) {
        // Create a new ArrayList to store the compacted layers
        List<LayoutLayer> compactedLayers = new ArrayList<>();

        // Copy upper part from layers to compactedLayers
        compactedLayers.addAll(layers.subList(0, removeLayerNr));

        // Copy lower part from layers to compactedLayers
        compactedLayers.addAll(layers.subList(removeLayerNr + 1, getLayerCount()));

        // Replace the old layers list with the new compacted one
        layers = compactedLayers;

        for (int l = removeLayerNr; l < getLayerCount(); l++) {
            for (LayoutNode layoutNode : getLayer(l)) {
                layoutNode.setLayer(l);
            }
        }
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
}

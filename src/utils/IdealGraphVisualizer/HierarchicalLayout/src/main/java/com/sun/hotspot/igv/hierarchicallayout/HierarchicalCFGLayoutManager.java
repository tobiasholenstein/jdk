/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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

import com.sun.hotspot.igv.layout.*;
import java.awt.*;
import java.util.*;

public class HierarchicalCFGLayoutManager extends LayoutManager {

    private final FontMetrics fontMetrics;
    // Lays out nodes within a single cluster (basic block).
    private final LinearLayoutManager subManager;
    // Lays out clusters in the CFG.
    private final HierarchicalLayoutManager manager;
    private final Set<? extends Cluster> clusters;
    private final Set<? extends Link> clusterLinks;

    public HierarchicalCFGLayoutManager(Set<? extends Link> clusterLinks, Set<? extends Cluster> clusters) {
        this.clusterLinks = clusterLinks;
        this.clusters = clusters;
        // Anticipate block label sizes to dimension blocks appropriately.
        Canvas canvas = new Canvas();
        fontMetrics = canvas.getFontMetrics(TITLE_FONT);
        this.subManager = new LinearLayoutManager(clusters);
        this.manager =  new HierarchicalLayoutManager();
    }

    @Override
    public void setCutEdges(boolean enable) {
        manager.setCutEdges(enable);
        subManager.setCutEdges(enable);
        maxLayerLength = enable ? 10 : -1;
    }

    Map<Cluster, ClusterNode> clusterNodesMap;
    Map<Link, ClusterEdge> clusterEdgesMap;

    public void doLayout(LayoutGraph graph) {
        // Create cluster-level nodes and edges.
        clusterNodesMap = createClusterNodes(graph.getVertices());
        assert clusterNodesMap.size() == clusters.size();
        clusterEdgesMap = createClusterEdges(clusterNodesMap);
        assert clusterEdgesMap.size() == clusterLinks.size();

        // Compute layout for each cluster.
        for (ClusterNode clusterNode : clusterNodesMap.values()) {
            subManager.doLayout(new LayoutGraph(clusterNode.getSubEdges(), clusterNode.getSubNodes()));
            clusterNode.updateSize();
        }

        // mark root nodes
        LayoutGraph clusterGraph = new LayoutGraph(clusterEdgesMap.values(), clusterNodesMap.values());
        for (Vertex rootVertex : clusterGraph.findRootVertices()) {
            assert rootVertex instanceof ClusterNode;
            ((ClusterNode) rootVertex).setRoot(true);
        }

        // Compute inter-cluster layout.
        manager.doLayout(clusterGraph);

        // Write back results.
        writeBackClusterBounds();
        writeBackClusterEdgePoints();
    }

    private Map<Cluster, ClusterNode> createClusterNodes(SortedSet<Vertex> vertices) {
        Map<Cluster, ClusterNode> clusterNodes = new HashMap<>();
        for (Cluster cluster : clusters) {
            String blockLabel = "B" + cluster;
            Dimension emptySize = new Dimension(fontMetrics.stringWidth(blockLabel) + ClusterNode.PADDING,
                                                fontMetrics.getHeight() + ClusterNode.PADDING);
            ClusterNode clusterNode = new ClusterNode(cluster, cluster.toString(), fontMetrics.getHeight(), emptySize);
            clusterNodes.put(cluster, clusterNode);
        }

        for (Vertex vertex : vertices) {
            Cluster cluster = vertex.getCluster();
            clusterNodes.get(cluster).addSubNode(vertex);
        }
        return clusterNodes;
    }

    private Map<Link, ClusterEdge> createClusterEdges(Map<Cluster, ClusterNode> clusterNodes) {
        Map<Link, ClusterEdge> clusterEdges = new HashMap<>();

        for (Link clusterLink : clusterLinks) {
            ClusterNode fromClusterNode = clusterNodes.get(clusterLink.getFromCluster());
            ClusterNode toClusterNode = clusterNodes.get(clusterLink.getToCluster());
            assert fromClusterNode != null;
            assert toClusterNode != null;
            clusterEdges.put(clusterLink, new ClusterEdge(fromClusterNode, toClusterNode));
        }

        return clusterEdges;
    }

    private void writeBackClusterBounds() {
        assert clusterNodesMap.size() == clusters.size();
        for (Cluster cluster : clusters) {
            ClusterNode clusterNode = clusterNodesMap.get(cluster);
            cluster.setBounds(new Rectangle(clusterNode.getPosition(), clusterNode.getSize()));
        }
    }

    private void writeBackClusterEdgePoints() {
        assert clusterEdgesMap.size() == clusterLinks.size();
        for (Link clusterLink : clusterLinks) {
            ClusterEdge clusterEdge = clusterEdgesMap.get(clusterLink);
            if (clusterEdge.getControlPoints() != null) {
                clusterLink.setControlPoints(clusterEdge.getControlPoints());
            } else {
                clusterLink.setControlPoints(new ArrayList<>());
            }
        }
    }

    public void moveCluster(Cluster movedCluster) {
        Vertex clusterVertex = clusterNodesMap.get(movedCluster);
        System.out.println("moveCluster " + clusterVertex.getPosition());
        manager.moveVertex(clusterVertex);
        writeBackClusterBounds();
        writeBackClusterEdgePoints();
        System.out.println("moveCluster2 " + clusterVertex.getPosition());
    }
}

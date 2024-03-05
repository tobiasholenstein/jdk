/*
 * Copyright (c) 1998, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 */
package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.*;
import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.settings.Settings;
import java.util.*;
import org.openide.util.Lookup;


public class DiagramViewModel {

    private int firstPosition = -1;


    public int getFirstPosition() {
        return firstPosition;
    }

    public void setPosition(int fp) {
        if (firstPosition != fp) {
            firstPosition = fp;
            cachedInputGraph = getFirstGraph();
            rebuildDiagram();
            graphChangedEvent.fire();
        }
    }

    private final Group group;
    private ArrayList<InputGraph> graphs;
    private Set<Integer> hiddenNodes;
    private Set<Integer> selectedNodes;
    private FilterChain filterChain;
    private final FilterChain customFilterChain;
    private final FilterChain filtersOrder;
    private Diagram diagram;
    private InputGraph cachedInputGraph;
    private final ChangedEvent<DiagramViewModel> diagramChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramViewModel> graphChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramViewModel> selectedNodesChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramViewModel> hiddenNodesChangedEvent = new ChangedEvent<>(this);
    private boolean showNodeHull;

    private final ChangedListener<FilterChain> filterChainChangedListener = changedFilterChain -> {
        assert filterChain == changedFilterChain;
        rebuildDiagram();
    };

    public Group getGroup() {
        return group;
    }

    public void setShowSea() {
        diagramChangedEvent.fire();
    }


    public boolean getShowNodeHull() {
        return showNodeHull;
    }

    public void setShowNodeHull(boolean b) {
        showNodeHull = b;
        diagramChangedEvent.fire();
    }

    private void initGroup() {
        group.getChangedEvent().addListener(g -> {
            assert g == group;
            if (group.getGraphs().isEmpty()) {
                // If the group has been emptied, all corresponding graph views
                // will be closed, so do nothing.
                return;
            }
            filterGraphs();
            setSelectedNodes(selectedNodes);
        });
        filterGraphs();
    }

    public DiagramViewModel(InputGraph graph) {
        group = graph.getGroup();
        initGroup();

        FilterChainProvider provider = Lookup.getDefault().lookup(FilterChainProvider.class);
        assert provider != null;
        customFilterChain = provider.createNewCustomFilterChain();
        setFilterChain(provider.getFilterChain());
        filtersOrder = provider.getAllFiltersOrdered();
        showNodeHull = true;
        hiddenNodes = new HashSet<>();
        selectedNodes = new HashSet<>();
        selectGraph(graph);
    }

    public ChangedEvent<DiagramViewModel> getDiagramChangedEvent() {
        return diagramChangedEvent;
    }

    public ChangedEvent<DiagramViewModel> getGraphChangedEvent() {
        return graphChangedEvent;
    }

    public ChangedEvent<DiagramViewModel> getSelectedNodesChangedEvent() {
        return selectedNodesChangedEvent;
    }

    public ChangedEvent<DiagramViewModel> getHiddenNodesChangedEvent() {
        return hiddenNodesChangedEvent;
    }

    public Set<Integer> getSelectedNodes() {
        return selectedNodes;
    }

    public Set<Integer> getHiddenNodes() {
        return hiddenNodes;
    }

    public void setSelectedNodes(Set<Integer> nodes) {
        selectedNodes = nodes;
        selectedNodesChangedEvent.fire();
    }

    public void showFigures(Collection<Figure> figures) {
        boolean somethingChanged = false;
        for (Figure f : figures) {
            if (hiddenNodes.remove(f.getInputNode().getId())) {
                somethingChanged = true;
            }
        }
        if (somethingChanged) {
            hiddenNodesChangedEvent.fire();
        }
    }

    public Set<Figure> getSelectedFigures() {
        Set<Figure> result = new HashSet<>();
        for (Figure f : diagram.getFigures()) {
            if (getSelectedNodes().contains(f.getInputNode().getId())) {
                result.add(f);
            }
        }
        return result;
    }

    public void showOnly(final Set<Integer> nodes) {
        final HashSet<Integer> allNodes = new HashSet<>(getGroup().getAllNodes());
        allNodes.removeAll(nodes);
        setHiddenNodes(allNodes);
    }

    public void setHiddenNodes(Set<Integer> nodes) {
        hiddenNodes = nodes;
        selectedNodes.removeAll(hiddenNodes);
        hiddenNodesChangedEvent.fire();
    }

    private void setFilterChain(FilterChain newFC) {
        assert newFC != null && customFilterChain != null;
        if (filterChain != null) {
            filterChain.getChangedEvent().removeListener(filterChainChangedListener);
        }
        if (newFC.getName().equals(customFilterChain.getName())) {
            filterChain = customFilterChain;
        } else {
            filterChain = newFC;
        }
        filterChain.getChangedEvent().addListener(filterChainChangedListener);
    }

    void close() {
        filterChain.getChangedEvent().removeListener(filterChainChangedListener);
    }

    private void rebuildDiagram() {
        // clear diagram
        InputGraph graph = getGraph();
        diagram = new Diagram(graph,
                Settings.get().get(Settings.NODE_TEXT, Settings.NODE_TEXT_DEFAULT),
                Settings.get().get(Settings.NODE_SHORT_TEXT, Settings.NODE_SHORT_TEXT_DEFAULT),
                Settings.get().get(Settings.NODE_TINY_TEXT, Settings.NODE_TINY_TEXT_DEFAULT));
        filterChain.applyInOrder(diagram, filtersOrder);
        diagramChangedEvent.fire();
    }

    /*
     * Select the set of graphs to be presented.
     */
    private void filterGraphs() {
        this.graphs = new ArrayList<>(group.getGraphs());
    }

    public InputGraph getFirstGraph() {
        InputGraph firstGraph;
        if (getFirstPosition() < graphs.size()) {
            firstGraph = graphs.get(getFirstPosition());
        } else {
            firstGraph = graphs.get(graphs.size() - 1);
        }
        if (firstGraph.isDiffGraph()) {
            firstGraph = firstGraph.getFirstGraph();
        }
        return firstGraph;
    }

    public void selectGraph(InputGraph graph) {
        setPosition(graphs.indexOf(graph));
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public InputGraph getGraph() {
        return cachedInputGraph;
    }
}

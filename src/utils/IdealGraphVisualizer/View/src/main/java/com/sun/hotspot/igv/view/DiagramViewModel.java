package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.*;
import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import java.util.*;
import org.openide.util.Lookup;


public class DiagramViewModel {

    private final Group group;
    private final ArrayList<InputGraph> graphs;
    private Set<Integer> hiddenNodes;
    private Set<Integer> selectedNodes;
    private FilterChain filterChain;
    private final FilterChain customFilterChain;
    private final FilterChain filtersOrder;
    private Diagram diagram;
    private int position = -1;
    private InputGraph inputGraph;
    private final ChangedEvent<DiagramViewModel> diagramChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramViewModel> selectedNodesChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramViewModel> hiddenNodesChangedEvent = new ChangedEvent<>(this);
    private boolean showNodeHull;

    private final ChangedListener<FilterChain> filterChainChangedListener = changedFilterChain -> {
        assert filterChain == changedFilterChain;
        rebuildDiagram();
    };

    public DiagramViewModel(InputGraph graph) {
        group = graph.getGroup();
        graphs = new ArrayList<>(group.getGraphs());

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int fp) {
        if (position != fp) {
            position = fp;
            if (position < graphs.size()) {
                inputGraph = graphs.get(position);
            } else {
                inputGraph = graphs.get(graphs.size() - 1);
            }
            rebuildDiagram();
        }
    }

    public void selectGraph(InputGraph graph) {
        setPosition(graphs.indexOf(graph));
    }

    public InputGraph getGraph() {
        return inputGraph;
    }

    public Group getGroup() {
        return group;
    }

    public void showDiagram() {
        diagramChangedEvent.fire();
    }


    public boolean getShowNodeHull() {
        return showNodeHull;
    }

    public void setShowNodeHull(boolean b) {
        showNodeHull = b;
        diagramChangedEvent.fire();
    }

    public ChangedEvent<DiagramViewModel> getDiagramChangedEvent() {
        return diagramChangedEvent;
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

    public static final String NODE_TEXT_DEFAULT = "[idx] [name]";

    public static final String NODE_TINY_TEXT_DEFAULT = "[idx]";

    public static final String NODE_SHORT_TEXT_DEFAULT = "[idx] [name]";


    private void rebuildDiagram() {
        // clear diagram
        InputGraph graph = getGraph();
        diagram = new Diagram(graph,
                NODE_TEXT_DEFAULT,
                NODE_SHORT_TEXT_DEFAULT,
                NODE_TINY_TEXT_DEFAULT);
        filterChain.applyInOrder(diagram, filtersOrder);
        diagramChangedEvent.fire();
    }

    public Diagram getDiagram() {
        return diagram;
    }
}

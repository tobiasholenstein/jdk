package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.*;
import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
import com.sun.hotspot.igv.graph.*;
import com.sun.hotspot.igv.hierarchicallayout.*;
import com.sun.hotspot.igv.layout.LayoutGraph;
import com.sun.hotspot.igv.util.DoubleClickAction;
import com.sun.hotspot.igv.util.DoubleClickHandler;
import com.sun.hotspot.igv.view.actions.*;
import com.sun.hotspot.igv.view.widgets.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.model.*;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

public class DiagramScene extends ObjectScene implements DoubleClickHandler {

    private final WidgetAction selectAction;
    private final JScrollPane scrollPane;
    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;
    private final NewHierarchicalLayoutManager seaLayoutManager;
    public static final float ALPHA = 0.4f;
    public static final float ZOOM_MAX_FACTOR = 4.0f;
    public static final float ZOOM_MIN_FACTOR = 0.25f;
    public static final float ZOOM_INCREMENT = 1.5f;

    public void zoomIn(Point zoomCenter, double factor) {
        centredZoom(super.getZoomFactor() * factor, zoomCenter);
    }

    public void zoomOut(Point zoomCenter, double factor) {
        centredZoom(super.getZoomFactor() / factor, zoomCenter);
    }

    public void setZoomPercentage(int percentage) {
        centredZoom((double)percentage / 100.0, null);
    }

    private void centredZoom(double zoomFactor, Point zoomCenter) {
        zoomFactor = Math.max(zoomFactor, ZOOM_MIN_FACTOR);
        zoomFactor = Math.min(zoomFactor,  ZOOM_MAX_FACTOR);

        double oldZoom = super.getZoomFactor();
        Rectangle visibleRect = super.getView().getVisibleRect();
        if (zoomCenter == null) {
            zoomCenter = new Point(visibleRect.x + visibleRect.width / 2, visibleRect.y + visibleRect.height / 2);
            zoomCenter =  super.getScene().convertViewToScene(zoomCenter);
        }

        super.setZoomFactor(zoomFactor);
        validateAll();

        Point location = super.getScene().getLocation();
        visibleRect.x += (int)(zoomFactor * (double)(location.x + zoomCenter.x)) - (int)(oldZoom * (double)(location.x + zoomCenter.x));
        visibleRect.y += (int)(zoomFactor * (double)(location.y + zoomCenter.y)) - (int)(oldZoom * (double)(location.y + zoomCenter.y));

        // Ensure to be within area
        visibleRect.x = Math.max(0, visibleRect.x);
        visibleRect.y = Math.max(0, visibleRect.y);

        super.getView().scrollRectToVisible(visibleRect);
        validateAll();
    }

    private JScrollPane createScrollPane(MouseZoomAction mouseZoomAction) {
        JComponent viewComponent = super.createView();
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.add(viewComponent);
        JScrollPane scrollPane = new JScrollPane(centeringPanel, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);

        // remove the default MouseWheelListener of the JScrollPane
        for (MouseWheelListener listener: scrollPane.getMouseWheelListeners()) {
            scrollPane.removeMouseWheelListener(listener);
        }

        // add a new MouseWheelListener for zooming if the mouse is outside the viewComponent
        // but still inside the scrollPane
        scrollPane.addMouseWheelListener(mouseZoomAction);
        return scrollPane;
    }

    private final Group group;
    private final ArrayList<InputGraph> graphs;
    private Set<Integer> hiddenNodes;
    private Set<Integer> selectedNodes;
    private FilterChain filterChain;
    private final FilterChain filtersOrder;
    private Diagram diagram;
    private int position = -1;
    private InputGraph inputGraph;
    private final ChangedEvent<DiagramScene> diagramChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramScene> selectedNodesChangedEvent = new ChangedEvent<>(this);
    private final ChangedEvent<DiagramScene> hiddenNodesChangedEvent = new ChangedEvent<>(this);
    private boolean showNodeHull;

    private final ChangedListener<FilterChain> filterChainChangedListener = changedFilterChain -> {
        assert filterChain == changedFilterChain;
        rebuildDiagram();
    };

    public DiagramScene(InputGraph inputGraph) {
        group = inputGraph.getGroup();
        graphs = new ArrayList<>(group.getGraphs());

        FilterChainProvider provider = Lookup.getDefault().lookup(FilterChainProvider.class);
        assert provider != null;
        filterChain = provider.getFilterChain();
        filterChain.getChangedEvent().addListener(filterChainChangedListener);
        filtersOrder = provider.getAllFiltersOrdered();

        showNodeHull = true;
        hiddenNodes = new HashSet<>();
        selectedNodes = new HashSet<>();
        setPosition(graphs.indexOf(inputGraph));


        //////
        MouseZoomAction mouseZoomAction = new MouseZoomAction(this);
        scrollPane = createScrollPane(mouseZoomAction);

        connectionLayer = new LayerWidget(this);
        super.addChild(connectionLayer);

        mainLayer = new LayerWidget(this);
        super.addChild(mainLayer);

        selectAction = new CustomSelectAction(new SelectProvider() {
            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return false;
            }

            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return DiagramScene.super.findObject(widget) != null;
            }

            public void select(Widget widget, Point localLocation, boolean invertSelection) {
                EditorTopComponent editor = EditorTopComponent.getActive();
                if (editor != null) {
                    editor.requestActive();
                }
                Set<Integer> nodeSelection = new HashSet<>();
                Object o = DiagramScene.super.findObject(widget);
                if (o instanceof Figure) {
                    nodeSelection.add(((Figure) o).getInputNode().getId());
                } else if (o instanceof Slot) {
                    nodeSelection.addAll(((Slot) o).getSource().getSourceNodesAsSet());
                }
                setSelectedNodes(nodeSelection);
            }
        });

        super.getActions().addAction(selectAction);
        super.getActions().addAction(new CustomizablePanAction(MouseEvent.BUTTON1_DOWN_MASK));
        super.getActions().addAction(new DoubleClickAction(this));
        super.getActions().addAction(mouseZoomAction);

        getDiagramChangedEvent().addListener(m -> update());
        getHiddenNodesChangedEvent().addListener(m -> relayout());

        seaLayoutManager = new NewHierarchicalLayoutManager();
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

    public ChangedEvent<DiagramScene> getDiagramChangedEvent() {
        return diagramChangedEvent;
    }


    public ChangedEvent<DiagramScene> getHiddenNodesChangedEvent() {
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

    void close() {
        filterChain.getChangedEvent().removeListener(filterChainChangedListener);
    }

    private void rebuildDiagram() {
        // clear diagram
        InputGraph graph = getGraph();
        diagram = new Diagram(graph);
        filterChain.applyInOrder(diagram, filtersOrder);
        diagramChangedEvent.fire();
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public Component getComponent() {
        return scrollPane;
    }

    private void clearObjects() {
        slotMap.clear();
        figureMap.clear();
        for (Object o :  new ArrayList<>(super.getObjects())) {
            super.removeObject(o);
        }
    }

    private void rebuildMainLayer() {
        mainLayer.removeChildren();
        for (Figure figure : getDiagram().getFigures()) {
            FigureWidget figureWidget = new FigureWidget(figure, this);
            figureWidget.setVisible(false);
            figureWidget.getActions().addAction(selectAction);
            figureWidget.getActions().addAction(ActionFactory.createMoveAction(null, new MoveProvider() {

                private int startLayerY;

                @Override
                public void movementStarted(Widget widget) {
                    widget.bringToFront();
                    startLayerY = widget.getLocation().y;
                }

                @Override
                public void movementFinished(Widget widget) {

                    Set<Figure> selectedFigures = getSelectedFigures();
                    for (Figure figure : selectedFigures) {
                        FigureWidget fw = findFigureWidget(figure);
                        Point newLocation = new Point(fw.getLocation().x, fw.getLocation().y);
                        seaLayoutManager.moveVertex(figure, newLocation);
                    }

                    rebuildConnectionLayer();
                    updateFigureWidgetLocations();
                    validateAll();
                }

                private static final int MAGNET_SIZE = 5;

                private int magnetToStartLayerY(Widget widget, Point location) {
                    int shiftY = location.y - widget.getLocation().y;
                    if (Math.abs(location.y - startLayerY) <= MAGNET_SIZE) {
                        if (Math.abs(widget.getLocation().y - startLayerY) > MAGNET_SIZE) {
                            shiftY = startLayerY - widget.getLocation().y;
                        } else {
                            shiftY = 0;
                        }
                    }
                    return shiftY;
                }

                @Override
                public Point getOriginalLocation(Widget widget) {
                    return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
                }

                @Override
                public void setNewLocation(Widget widget, Point location) {
                    int shiftX = location.x - widget.getLocation().x;
                    int shiftY = magnetToStartLayerY(widget, location);

                    List<Figure> selectedFigures = new ArrayList<>(getSelectedFigures());
                    selectedFigures.sort(Comparator.comparingInt(f -> f.getPosition().x));
                    for (Figure figure : selectedFigures) {
                        FigureWidget fw = findFigureWidget(figure);
                        if (figureToInLineWidget.containsKey(fw.getFigure())) {
                            for (LineWidget lw : figureToInLineWidget.get(fw.getFigure())) {
                                Point toPt = lw.getTo();
                                lw.setTo(new Point(toPt.x + shiftX, toPt.y + shiftY));
                                Point fromPt = lw.getFrom();
                                lw.setFrom(new Point(fromPt.x + shiftX, fromPt.y));
                                lw.revalidate();
                                LineWidget pred = lw.getPredecessor();
                                pred.setTo(new Point(pred.getTo().x + shiftX, pred.getTo().y));
                                pred.revalidate();

                            }
                        }
                        if (figureToOutLineWidget.containsKey(fw.getFigure())) {
                            for (LineWidget lw : figureToOutLineWidget.get(fw.getFigure())) {
                                Point toPt = lw.getTo();
                                lw.setTo(new Point(toPt.x + shiftX, toPt.y));
                                Point fromPt = lw.getFrom();
                                lw.setFrom(new Point(fromPt.x + shiftX, fromPt.y + shiftY));
                                lw.revalidate();
                                for (LineWidget succ : lw.getSuccessors()) {
                                    succ.setFrom(new Point(succ.getFrom().x + shiftX, succ.getFrom().y));
                                    succ.revalidate();
                                }
                            }
                        }
                        Point newLocation = new Point(fw.getLocation().x + shiftX, fw.getLocation().y + shiftY);
                        ActionFactory.createDefaultMoveProvider().setNewLocation(fw, newLocation);
                    }
                }
            }));
            super.addObject(figure, figureWidget);
            figureMap.put(figure, figureWidget);
            mainLayer.addChild(figureWidget);

            for (InputSlot inputSlot : figure.getInputSlots()) {
                SlotWidget slotWidget = new InputSlotWidget(inputSlot, this, figureWidget);
                slotWidget.getActions().addAction(new DoubleClickAction(slotWidget));
                slotWidget.getActions().addAction(selectAction);
                slotMap.put(inputSlot, slotWidget);
            }

            for (OutputSlot outputSlot : figure.getOutputSlots()) {
                SlotWidget slotWidget = new OutputSlotWidget(outputSlot, this, figureWidget);
                slotWidget.getActions().addAction(new DoubleClickAction(slotWidget));
                slotWidget.getActions().addAction(selectAction);
                slotMap.put(outputSlot, slotWidget);
            }
        }
    }


    Map<Figure, FigureWidget> figureMap = new HashMap<>();

    Map<Slot, SlotWidget> slotMap = new HashMap<>();

    public SlotWidget findSlotWidget(Slot slot) {
        return slotMap.get(slot);
    }

    public FigureWidget findFigureWidget(Figure figure) {
        return figureMap.get(figure);
    }

    private void update() {
        clearObjects();
        rebuildMainLayer();
        relayout();
        setFigureSelection(getSelectedFigures());
        validateAll();
    }

    public void validateAll() {
        super.validate();
        scrollPane.validate();
    }

    private boolean isVisibleFigureConnection(FigureConnection figureConnection) {
        FigureWidget w1 = findFigureWidget(figureConnection.getInputSlot().getFigure());
        FigureWidget w2 = findFigureWidget(figureConnection.getOutputSlot().getFigure());
        return w1.isVisible() && w2.isVisible();
    }


    private void processOutputSlot(OutputSlot outputSlot, List<FigureConnection> connections, int controlPointIndex, Point lastPoint, LineWidget predecessor) {
        Map<Point, List<FigureConnection>> pointMap = new HashMap<>(connections.size());

        if (predecessor != null) {
            if (controlPointIndex == 2) {
                Figure figure = outputSlot.getFigure();
                if (figureToOutLineWidget.containsKey(figure)) {
                    figureToOutLineWidget.get(figure).add(predecessor);
                } else {
                    figureToOutLineWidget.put(figure, new HashSet<>(Collections.singleton(predecessor)));
                }
            }
        }

        for (FigureConnection connection : connections) {
            if (isVisibleFigureConnection(connection)) {
                List<Point> controlPoints = connection.getControlPoints();
                if (controlPointIndex < controlPoints.size()) {
                    Point currentPoint = controlPoints.get(controlPointIndex);
                    currentPoint = new Point(currentPoint.x, currentPoint.y);
                    if (pointMap.containsKey(currentPoint)) {
                        pointMap.get(currentPoint).add(connection);
                    } else {
                        pointMap.put(currentPoint, new ArrayList<>(Collections.singletonList(connection)));
                    }
                } else if (controlPointIndex == controlPoints.size()) {
                    if (predecessor != null) {
                        Figure figure = ((Slot) connection.getTo()).getFigure();
                        if (figureToInLineWidget.containsKey(figure)) {
                            figureToInLineWidget.get(figure).add(predecessor);
                        } else {
                            figureToInLineWidget.put(figure, new HashSet<>(Collections.singleton(predecessor)));
                        }
                    }
                }
            }
        }

        for (Point currentPoint : pointMap.keySet()) {
            List<FigureConnection> connectionList = pointMap.get(currentPoint);

            boolean isBold = false;
            boolean isDashed = true;
            boolean isVisible = true;
            for (Connection c : connectionList) {
                if (c.getStyle() == Connection.ConnectionStyle.BOLD) {
                    isBold = true;
                } else if (c.getStyle() == Connection.ConnectionStyle.INVISIBLE) {
                    isVisible = false;
                }
                if (c.getStyle() != Connection.ConnectionStyle.DASHED) {
                    isDashed = false;
                }
            }

            LineWidget newPredecessor = predecessor;
            if (lastPoint != null) {
                Point src = new Point(lastPoint);
                Point dest = new Point(currentPoint);
                newPredecessor = new LineWidget(this, outputSlot, connectionList, src, dest, predecessor, isBold, isDashed);
                newPredecessor.setVisible(isVisible);

                connectionLayer.addChild(newPredecessor);
                newPredecessor.getActions().addAction(ActionFactory.createMoveAction(null, new MoveProvider() {


                    Point startLocation;
                    Point origFrom;
                    @Override
                    public void movementStarted(Widget widget) {
                        LineWidget lineWidget = (LineWidget) widget;
                        startLocation = lineWidget.getClientAreaLocation();
                        origFrom = lineWidget.getFrom();
                    }

                    @Override
                    public void movementFinished(Widget widget) {
                        LineWidget lineWidget = (LineWidget) widget;
                        if (lineWidget.getPredecessor() == null) return;
                        if (lineWidget.getSuccessors().isEmpty()) return;
                        if (lineWidget.getFrom().x != lineWidget.getTo().x) return;

                        int shiftX = lineWidget.getClientAreaLocation().x - startLocation.x;
                        if (shiftX == 0) return;

                        Point newFrom = new Point(origFrom.x + shiftX, origFrom.y);
                        seaLayoutManager.moveLink(lineWidget.getFromFigure(), origFrom, newFrom);

                        seaLayoutManager.writeBack();
                        rebuildConnectionLayer();
                        updateFigureWidgetLocations();
                        validateAll();
                    }

                    @Override
                    public Point getOriginalLocation(Widget widget) {
                        LineWidget lineWidget = (LineWidget) widget;
                        return lineWidget.getClientAreaLocation();
                    }

                    @Override
                    public void setNewLocation(Widget widget, Point location) {
                        LineWidget lineWidget = (LineWidget) widget;
                        if (lineWidget.getPredecessor() == null) return;
                        if (lineWidget.getSuccessors().isEmpty()) return;
                        if (lineWidget.getFrom().x != lineWidget.getTo().x) return;

                        int shiftX = location.x - lineWidget.getClientAreaLocation().x;
                        if (shiftX == 0) return;

                        Point oldFrom = lineWidget.getFrom();
                        Point newFrom = new Point(oldFrom.x + shiftX, oldFrom.y);

                        Point oldTo = lineWidget.getTo();
                        Point newTo = new Point(oldTo.x + shiftX, oldTo.y);

                        lineWidget.setTo(newTo);
                        lineWidget.setFrom(newFrom);
                        lineWidget.revalidate();

                        LineWidget predecessor = lineWidget.getPredecessor();
                        Point toPt = predecessor.getTo();
                        predecessor.setTo(new Point(toPt.x + shiftX, toPt.y));
                        predecessor.revalidate();

                        for (LineWidget successor : lineWidget.getSuccessors()) {
                            Point fromPt = successor.getFrom();
                            successor.setFrom(new Point(fromPt.x + shiftX, fromPt.y));
                            successor.revalidate();
                        }
                    }
                }));
            }

            processOutputSlot(outputSlot, connectionList, controlPointIndex + 1, currentPoint, newPredecessor);
        }
    }

    @Override
    public void handleDoubleClick(Widget w, WidgetAction.WidgetMouseEvent e) {
        clearSelectedNodes();
    }

    public void addSelectedNodes(Collection<InputNode> nodes, boolean showIfHidden) {
        Set<Integer> nodeIds = new HashSet<>(getSelectedNodes());
        for (InputNode inputNode : nodes) {
            nodeIds.add(inputNode.getId());
        }
        Set<Figure> selectedFigures = new HashSet<>();
        for (Figure figure : getDiagram().getFigures()) {
            if (nodeIds.contains(figure.getInputNode().getId())) {
                selectedFigures.add(figure);
            }
        }
        setFigureSelection(selectedFigures);
        if (showIfHidden) {
            showFigures(getSelectedFigures());
        }
    }

    public void clearSelectedNodes() {
        super.setSelectedObjects(Collections.emptySet());
    }

    private void setFigureSelection(Set<Figure> list) {
        super.setSelectedObjects(new HashSet<>(list));
    }

    private void rebuildConnectionLayer() {
        figureToOutLineWidget.clear();
        figureToInLineWidget.clear();
        connectionLayer.removeChildren();
        for (Figure figure : getDiagram().getFigures()) {
            for (OutputSlot outputSlot : figure.getOutputSlots()) {
                List<FigureConnection> connectionList = new ArrayList<>(outputSlot.getConnections());
                processOutputSlot(outputSlot, connectionList, 0, null, null);
            }
        }
    }

    private void updateVisibleFigureWidgets() {
        for (Figure figure : getDiagram().getFigures()) {
            FigureWidget figureWidget = findFigureWidget(figure);
            figureWidget.setBoundary(false);
            figureWidget.setVisible(!getHiddenNodes().contains(figure.getInputNode().getId()));
        }
    }

    private void updateNodeHull() {
        if (getShowNodeHull()) {
            List<FigureWidget> boundaries = new ArrayList<>();
            for (Figure figure : getDiagram().getFigures()) {
                FigureWidget figureWidget = findFigureWidget(figure);
                if (!figureWidget.isVisible()) {
                    Set<Figure> neighborSet = new HashSet<>(figure.getPredecessorSet());
                    neighborSet.addAll(figure.getSuccessorSet());
                    boolean hasVisibleNeighbor = false;
                    for (Figure neighbor : neighborSet) {
                        FigureWidget neighborWidget = findFigureWidget(neighbor);
                        if (neighborWidget.isVisible()) {
                            hasVisibleNeighbor = true;
                            break;
                        }
                    }
                    if (hasVisibleNeighbor) {
                        figureWidget.setBoundary(true);
                        boundaries.add(figureWidget);
                    }
                }
            }
            for (FigureWidget figureWidget : boundaries) {
                figureWidget.setVisible(true);
            }
        } else {
            getSelectedNodes().removeAll(getHiddenNodes());
        }
    }

    private Set<Figure> getVisibleFigures() {
        HashSet<Figure> visibleFigures = new HashSet<>();
        for (Figure figure : getDiagram().getFigures()) {
            FigureWidget figureWidget = findFigureWidget(figure);
            if (figureWidget.isVisible()) {
                visibleFigures.add(figure);
            }
        }
        return visibleFigures;
    }

    private HashSet<Connection> getVisibleConnections() {
        HashSet<Connection> visibleConnections = new HashSet<>();
        for (FigureConnection connection : getDiagram().getConnections()) {
            if (isVisibleFigureConnection(connection)) {
                visibleConnections.add(connection);
            }
        }
        return visibleConnections;
    }

    private void updateFigureWidgetLocations() {
        for (Figure figure : getDiagram().getFigures()) {
            FigureWidget figureWidget = findFigureWidget(figure);
            if (figureWidget.isVisible()) {
                Point location = new Point(figure.getPosition());
                figureWidget.setPreferredLocation(location);
            }
        }
    }
    Map<Figure, Set<LineWidget>> figureToOutLineWidget = new HashMap<>();
    Map<Figure, Set<LineWidget>> figureToInLineWidget = new HashMap<>();

    private void relayout() {
        updateVisibleFigureWidgets();
        updateNodeHull();
        Set<Figure> visibleFigures = getVisibleFigures();
        Set<Connection> visibleConnections = getVisibleConnections();
        seaLayoutManager.doLayout(new LayoutGraph(visibleConnections, visibleFigures));
        rebuildConnectionLayer();
        updateFigureWidgetLocations();
        validateAll();
    }
}

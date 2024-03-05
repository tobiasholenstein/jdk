package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.*;
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
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.*;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public class DiagramScene extends ObjectScene implements DoubleClickHandler {

    private final WidgetAction selectAction;
    private final JScrollPane scrollPane;
    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;
    private final DiagramViewModel model;
    private final HierarchicalLayoutManager seaLayoutManager;
    public static final float ALPHA = 0.4f;
    public static final int BORDER_SIZE = 100;
    public static final float ZOOM_MAX_FACTOR = 4.0f;
    public static final float ZOOM_MIN_FACTOR = 0.25f;
    public static final float ZOOM_INCREMENT = 1.5f;

    @SuppressWarnings("unchecked")
    public <T> T getWidget(Object o) {
        Widget w = findWidget(o);
        return (T) w;
    }

    public double getZoomMinFactor() {
        double factorWidth = scrollPane.getViewport().getViewRect().getWidth() / getBounds().getWidth() ;
        double factorHeight = scrollPane.getViewport().getViewRect().getHeight() / getBounds().getHeight();
        double zoomToFit = 0.98 * Math.min(factorWidth, factorHeight);
        return Math.min(zoomToFit, ZOOM_MIN_FACTOR);
    }

    public double getZoomMaxFactor() {
        return ZOOM_MAX_FACTOR;
    }

    public void zoomIn(Point zoomCenter, double factor) {
        centredZoom(getZoomFactor() * factor, zoomCenter);
    }

    public void zoomOut(Point zoomCenter, double factor) {
        centredZoom(getZoomFactor() / factor, zoomCenter);
    }

    public void setZoomPercentage(int percentage) {
        centredZoom((double)percentage / 100.0, null);
    }

    private void centredZoom(double zoomFactor, Point zoomCenter) {
        zoomFactor = Math.max(zoomFactor, getZoomMinFactor());
        zoomFactor = Math.min(zoomFactor,  getZoomMaxFactor());

        double oldZoom = getZoomFactor();
        Rectangle visibleRect = getView().getVisibleRect();
        if (zoomCenter == null) {
            zoomCenter = new Point(visibleRect.x + visibleRect.width / 2, visibleRect.y + visibleRect.height / 2);
            zoomCenter =  getScene().convertViewToScene(zoomCenter);
        }

        setZoomFactor(zoomFactor);
        validateAll();

        Point location = getScene().getLocation();
        visibleRect.x += (int)(zoomFactor * (double)(location.x + zoomCenter.x)) - (int)(oldZoom * (double)(location.x + zoomCenter.x));
        visibleRect.y += (int)(zoomFactor * (double)(location.y + zoomCenter.y)) - (int)(oldZoom * (double)(location.y + zoomCenter.y));

        // Ensure to be within area
        visibleRect.x = Math.max(0, visibleRect.x);
        visibleRect.y = Math.max(0, visibleRect.y);

        getView().scrollRectToVisible(visibleRect);
        validateAll();
    }

    private JScrollPane createScrollPane(MouseZoomAction mouseZoomAction) {
        setBackground(Color.WHITE);
        setOpaque(true);

        JComponent viewComponent = createView();
        viewComponent.setBackground(Color.WHITE);
        viewComponent.setOpaque(true);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setBackground(Color.WHITE);
        centeringPanel.setOpaque(true);
        centeringPanel.add(viewComponent);

        JScrollPane scrollPane = new JScrollPane(centeringPanel, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

        // remove the default MouseWheelListener of the JScrollPane
        for (MouseWheelListener listener: scrollPane.getMouseWheelListeners()) {
            scrollPane.removeMouseWheelListener(listener);
        }

        // add a new MouseWheelListener for zooming if the mouse is outside the viewComponent
        // but still inside the scrollPane
        scrollPane.addMouseWheelListener(mouseZoomAction);
        return scrollPane;
    }

    public DiagramScene(DiagramViewModel model) {
        setCheckClipping(true);

        MouseZoomAction mouseZoomAction = new MouseZoomAction(this);
        scrollPane = createScrollPane(mouseZoomAction);

        // This panAction handles the event only when the left mouse button is
        // pressed without any modifier keys, otherwise it will not consume it
        // and the selection action (below) will handle the event
        CustomizablePanAction panAction = new CustomizablePanAction(MouseEvent.BUTTON1_DOWN_MASK);
        getActions().addAction(panAction);

        // handle default double-click, when not handled by other DoubleClickHandler
        getActions().addAction(new DoubleClickAction(this));

        selectAction = new CustomSelectAction(new SelectProvider() {
            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return false;
            }

            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return findObject(widget) != null;
            }

            public void select(Widget widget, Point localLocation, boolean invertSelection) {
                EditorTopComponent editor = EditorTopComponent.getActive();
                if (editor != null) {
                    editor.requestActive();
                }
                Object object = findObject(widget);
                setFocusedObject(object);
                if (object != null) {
                    if (!invertSelection && getSelectedObjects().contains(object)) {
                        return;
                    }
                    userSelectionSuggested(Collections.singleton(object), invertSelection);
                } else {
                    userSelectionSuggested(Collections.emptySet(), invertSelection);
                }
            }
        });

        getActions().addAction(selectAction);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);

        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        setBorder(BorderFactory.createLineBorder(Color.white, BORDER_SIZE));
        getActions().addAction(mouseZoomAction);

        ObjectSceneListener selectionChangedListener = new ObjectSceneListener() {

            @Override
            public void objectAdded(ObjectSceneEvent arg0, Object arg1) {}

            @Override
            public void objectRemoved(ObjectSceneEvent arg0, Object arg1) {}

            @Override
            public void objectStateChanged(ObjectSceneEvent e, Object o, ObjectState oldState, ObjectState newState) {}

            @Override
            public void selectionChanged(ObjectSceneEvent e, Set<Object> oldSet, Set<Object> newSet) {
                Set<Integer> nodeSelection = new HashSet<>();
                for (Object o : newSet) {
                    if (o instanceof Figure) {
                        nodeSelection.add(((Figure) o).getInputNode().getId());
                    } else if (o instanceof Slot) {
                        nodeSelection.addAll(((Slot) o).getSource().getSourceNodesAsSet());
                    }
                }
                getModel().setSelectedNodes(nodeSelection);
            }

            @Override
            public void highlightingChanged(ObjectSceneEvent e, Set<Object> oldSet, Set<Object> newSet) {}

            @Override
            public void hoverChanged(ObjectSceneEvent e, Object oldObject, Object newObject) {}

            @Override
            public void focusChanged(ObjectSceneEvent arg0, Object arg1, Object arg2) {}
        };
        addObjectSceneListener(selectionChangedListener, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);

        model.getDiagramChangedEvent().addListener(m -> update());
        model.getHiddenNodesChangedEvent().addListener(m -> relayout());

        seaLayoutManager = new HierarchicalLayoutManager();

        this.model = model;
    }

    public DiagramViewModel getModel() {
        return model;
    }

    public Component getComponent() {
        return scrollPane;
    }

    public boolean isAllVisible() {
        return model.getHiddenNodes().isEmpty();
    }


    private void clearObjects() {
        Collection<Object> objects = new ArrayList<>(getObjects());
        for (Object o : objects) {
            removeObject(o);
        }
    }

    private void rebuildMainLayer() {
        mainLayer.removeChildren();
        for (Figure figure : getModel().getDiagram().getFigures()) {
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

                    Set<Figure> selectedFigures = model.getSelectedFigures();
                    for (Figure figure : selectedFigures) {
                        FigureWidget fw = getWidget(figure);
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

                    List<Figure> selectedFigures = new ArrayList<>( model.getSelectedFigures());
                    selectedFigures.sort(Comparator.comparingInt(f -> f.getPosition().x));
                    for (Figure figure : selectedFigures) {
                        FigureWidget fw = getWidget(figure);
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
            addObject(figure, figureWidget);
            mainLayer.addChild(figureWidget);

            for (InputSlot inputSlot : figure.getInputSlots()) {
                SlotWidget slotWidget = new InputSlotWidget(inputSlot, this, figureWidget);
                slotWidget.getActions().addAction(new DoubleClickAction(slotWidget));
                slotWidget.getActions().addAction(selectAction);
                addObject(inputSlot, slotWidget);
            }

            for (OutputSlot outputSlot : figure.getOutputSlots()) {
                SlotWidget slotWidget = new OutputSlotWidget(outputSlot, this, figureWidget);
                slotWidget.getActions().addAction(new DoubleClickAction(slotWidget));
                slotWidget.getActions().addAction(selectAction);
                addObject(outputSlot, slotWidget);
            }
        }
    }

    private void update() {
        clearObjects();
        rebuildMainLayer();
        relayout();
        setFigureSelection(model.getSelectedFigures());
        validateAll();
    }

    public void validateAll() {
        validate();
        scrollPane.validate();
    }

    private boolean isVisibleFigureConnection(FigureConnection figureConnection) {
        Widget w1 = getWidget(figureConnection.getFrom().getVertex());
        Widget w2 = getWidget(figureConnection.getTo().getVertex());
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
        Set<Integer> nodeIds = new HashSet<>(model.getSelectedNodes());
        for (InputNode inputNode : nodes) {
            nodeIds.add(inputNode.getId());
        }
        Set<Figure> selectedFigures = new HashSet<>();
        for (Figure figure : model.getDiagram().getFigures()) {
            if (nodeIds.contains(figure.getInputNode().getId())) {
                selectedFigures.add(figure);
            }
        }
        setFigureSelection(selectedFigures);
        if (showIfHidden) {
            model.showFigures(model.getSelectedFigures());
        }
    }

    public void clearSelectedNodes() {
        setSelectedObjects(Collections.emptySet());
    }

    private void setFigureSelection(Set<Figure> list) {
        super.setSelectedObjects(new HashSet<>(list));
    }

    private void rebuildConnectionLayer() {
        figureToOutLineWidget.clear();
        figureToInLineWidget.clear();
        connectionLayer.removeChildren();
        for (Figure figure : getModel().getDiagram().getFigures()) {
            for (OutputSlot outputSlot : figure.getOutputSlots()) {
                List<FigureConnection> connectionList = new ArrayList<>(outputSlot.getConnections());
                processOutputSlot(outputSlot, connectionList, 0, null, null);
            }
        }
    }

    private void updateVisibleFigureWidgets() {
        for (Figure figure : getModel().getDiagram().getFigures()) {
            FigureWidget figureWidget = getWidget(figure);
            figureWidget.setBoundary(false);
            figureWidget.setVisible(!model.getHiddenNodes().contains(figure.getInputNode().getId()));
        }
    }

    private void updateNodeHull() {
        if (getModel().getShowNodeHull()) {
            List<FigureWidget> boundaries = new ArrayList<>();
            for (Figure figure : getModel().getDiagram().getFigures()) {
                FigureWidget figureWidget = getWidget(figure);
                if (!figureWidget.isVisible()) {
                    Set<Figure> neighborSet = new HashSet<>(figure.getPredecessorSet());
                    neighborSet.addAll(figure.getSuccessorSet());
                    boolean hasVisibleNeighbor = false;
                    for (Figure neighbor : neighborSet) {
                        FigureWidget neighborWidget = getWidget(neighbor);
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
            getModel().getSelectedNodes().removeAll(getModel().getHiddenNodes());
        }
    }

    private Set<Figure> getVisibleFigures() {
        HashSet<Figure> visibleFigures = new HashSet<>();
        for (Figure figure : getModel().getDiagram().getFigures()) {
            FigureWidget figureWidget = getWidget(figure);
            if (figureWidget.isVisible()) {
                visibleFigures.add(figure);
            }
        }
        return visibleFigures;
    }

    private HashSet<Connection> getVisibleConnections() {
        HashSet<Connection> visibleConnections = new HashSet<>();
        for (FigureConnection connection : getModel().getDiagram().getConnections()) {
            if (isVisibleFigureConnection(connection)) {
                visibleConnections.add(connection);
            }
        }
        return visibleConnections;
    }

    private void updateFigureWidgetLocations() {
        for (Figure figure : getModel().getDiagram().getFigures()) {
            FigureWidget figureWidget = getWidget(figure);
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

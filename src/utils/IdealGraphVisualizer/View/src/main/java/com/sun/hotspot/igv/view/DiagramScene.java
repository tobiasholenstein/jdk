package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.data.Group;
import com.sun.hotspot.igv.data.InputGraph;
import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
import com.sun.hotspot.igv.graph.*;
import com.sun.hotspot.igv.hierarchicallayout.HierarchicalLayoutManager;
import com.sun.hotspot.igv.layout.LayoutGraph;
import com.sun.hotspot.igv.util.DoubleClickAction;
import com.sun.hotspot.igv.util.DoubleClickHandler;
import com.sun.hotspot.igv.view.actions.CustomSelectAction;
import com.sun.hotspot.igv.view.actions.CustomizablePanAction;
import com.sun.hotspot.igv.view.actions.MouseZoomAction;
import com.sun.hotspot.igv.view.widgets.FigureWidget;
import com.sun.hotspot.igv.view.widgets.LineWidget;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

public class DiagramScene extends ObjectScene implements DoubleClickHandler {

    public static final float ALPHA = 0.4f;
    public static final float ZOOM_INCREMENT = 1.5f;
    private static final float ZOOM_MAX_FACTOR = 4.0f;
    private static final float ZOOM_MIN_FACTOR = 0.25f;
    private final JScrollPane scrollPane;
    private final LayerWidget figureLayer;
    private final LayerWidget connectionLayer;
    private final HierarchicalLayoutManager seaLayoutManager;
    private final FilterChain filtersOrder;
    private final FilterChain filterChain;
    private final Map<Figure, FigureWidget> figureMap = new HashMap<>();
    private final Map<FigureWidget, Set<LineWidget>> figureWidgetToOutLineWidgets = new HashMap<>();
    private final Map<FigureWidget, Set<LineWidget>> figureWidgetToInLineWidgets = new HashMap<>();
    private final Group group;
    private Set<Integer> hiddenNodesByID;
    private Set<Integer> selectedNodesByID;
    private int position = -1;
    private InputGraph inputGraph;
    private Diagram diagram;
    private boolean showNodeHull;
    private final ChangedListener<FilterChain> filterChangedListener = filter -> buildDiagram();

    public DiagramScene(InputGraph inputGraph) {
        group = inputGraph.getGroup();

        FilterChainProvider provider = Lookup.getDefault().lookup(FilterChainProvider.class);
        assert provider != null;
        filterChain = provider.getFilterChain();
        filterChain.getChangedEvent().addListener(filterChangedListener);
        filtersOrder = provider.getAllFiltersOrdered();

        showNodeHull = true;
        hiddenNodesByID = new HashSet<>();
        selectedNodesByID = new HashSet<>();

        MouseZoomAction mouseZoomAction = new MouseZoomAction(this);
        scrollPane = createScrollPane(mouseZoomAction);

        connectionLayer = new LayerWidget(this);
        super.addChild(connectionLayer);

        figureLayer = new LayerWidget(this);
        super.addChild(figureLayer);

        super.getActions().addAction(new CustomizablePanAction(MouseEvent.BUTTON1_DOWN_MASK));
        super.getActions().addAction(new DoubleClickAction(this));
        super.getActions().addAction(mouseZoomAction);

        seaLayoutManager = new HierarchicalLayoutManager();

        setPosition(group.getGraphs().indexOf(inputGraph));
    }

    public void zoomIn(Point zoomCenter, double factor) {
        centredZoom(super.getZoomFactor() * factor, zoomCenter);
    }

    public void zoomOut(Point zoomCenter, double factor) {
        centredZoom(super.getZoomFactor() / factor, zoomCenter);
    }

    public void setZoomPercentage(int percentage) {
        centredZoom((double) percentage / 100.0, null);
    }

    private void centredZoom(double zoomFactor, Point zoomCenter) {
        zoomFactor = Math.max(zoomFactor, ZOOM_MIN_FACTOR);
        zoomFactor = Math.min(zoomFactor, ZOOM_MAX_FACTOR);

        double oldZoom = super.getZoomFactor();
        Rectangle visibleRect = super.getView().getVisibleRect();
        if (zoomCenter == null) {
            zoomCenter = new Point(visibleRect.x + visibleRect.width / 2, visibleRect.y + visibleRect.height / 2);
            zoomCenter = super.getScene().convertViewToScene(zoomCenter);
        }

        super.setZoomFactor(zoomFactor);
        validateAll();

        Point location = super.getScene().getLocation();
        visibleRect.x += (int) (zoomFactor * (double) (location.x + zoomCenter.x)) - (int) (oldZoom * (double) (location.x + zoomCenter.x));
        visibleRect.y += (int) (zoomFactor * (double) (location.y + zoomCenter.y)) - (int) (oldZoom * (double) (location.y + zoomCenter.y));

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
        for (MouseWheelListener listener : scrollPane.getMouseWheelListeners()) {
            scrollPane.removeMouseWheelListener(listener);
        }

        // add a new MouseWheelListener for zooming if the mouse is outside the viewComponent
        // but still inside the scrollPane
        scrollPane.addMouseWheelListener(mouseZoomAction);
        return scrollPane;
    }

    private void setPosition(int pos) {
        if (position != pos && pos >= 0 && position < getGroup().getGraphs().size()) {
            position = pos;
            inputGraph = group.getGraphs().get(position);
            buildDiagram();
        }
    }

    public void nextGraph() {
        if (position < getGroup().getGraphs().size() - 1) {
            setPosition(position + 1);
        }
    }

    public void previousGraph() {
        if (position > 0) {
            setPosition(position - 1);
        }
    }

    public InputGraph getGraph() {
        return inputGraph;
    }

    public Group getGroup() {
        return group;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    private void buildDiagram() {
        // create diagram
        diagram = new Diagram(inputGraph);

        // filter diagram
        filterChain.applyInOrder(diagram, filtersOrder);

        // draw diagram
        rebuildFigureLayer();
        relayout();
        validateAll();
    }

    private void relayout() {  // visible nodes changed
        updateVisibleFigureWidgets();
        Set<Figure> visibleFigures = diagram.getVisibleFigures();
        Set<Connection> visibleConnections = diagram.getVisibleConnections();
        seaLayoutManager.doLayout(new LayoutGraph(visibleConnections, visibleFigures));
        updateWidgetPositions();
    }

    private void updateVisibleFigureWidgets() {
        // Initially set all figures to not boundary and update visibility based on hiddenNodesByID
        diagram.getFigures().forEach(figure -> {
            figure.setBoundary(false);
            figure.setVisible(!hiddenNodesByID.contains(figure.getInputNode().getId()));
        });

        if (showNodeHull) { // update node hull
            // Marks non-visible figures with visible neighbors as boundary figures and makes them visible
            diagram.getFigures().stream()
                    .filter(figure -> !figure.isVisible())
                    .filter(figure -> Stream.concat(figure.getPredecessors().stream(), figure.getSuccessors().stream())
                            .anyMatch(Figure::isVisible))
                    .peek(figure -> figure.setBoundary(true))
                    .toList() // needed!
                    .forEach(figure -> figure.setVisible(true));
        } else {
            selectedNodesByID.removeAll(hiddenNodesByID);
        }

        // Update figure widgets' visibility
        figureMap.forEach((figure, fw) -> fw.setVisible(figure.isVisible()));
    }

    private void updateWidgetPositions() {
        figureWidgetToOutLineWidgets.clear();
        figureWidgetToInLineWidgets.clear();
        connectionLayer.removeChildren();
        for (FigureWidget figureWidget : figureMap.values()) {
            figureWidget.updatePosition();
            List<Connection> visibleConnections = figureWidget.getFigure().getVisibleConnections();
            createLineWidgets(figureWidget, visibleConnections, 0, null, null);
        }
        validateAll();
    }

    private void createLineWidgets(FigureWidget fromFigureWidget, List<Connection> connections, int controlPointIndex, Point lastPoint, LineWidget prevLineWidget) {
        Map<Point, List<Connection>> pointMap = new HashMap<>(connections.size());

        if (prevLineWidget != null) {
            if (controlPointIndex == 2) {
                figureWidgetToOutLineWidgets.computeIfAbsent(fromFigureWidget, k -> new HashSet<>()).add(prevLineWidget);
            }

            for (Connection connection : connections) {
                if (controlPointIndex == connection.getControlPoints().size()) {
                    FigureWidget toFigureWidget = figureMap.get(connection.getTo());
                    figureWidgetToInLineWidgets.computeIfAbsent(toFigureWidget, k -> new HashSet<>()).add(prevLineWidget);
                }
            }
        }

        for (Connection connection : connections) {
            List<Point> controlPoints = connection.getControlPoints();
            if (controlPointIndex < controlPoints.size()) {
                Point currentPoint = new Point(controlPoints.get(controlPointIndex));
                pointMap.computeIfAbsent(currentPoint, k -> new ArrayList<>()).add(connection);
            }
        }

        for (Map.Entry<Point, List<Connection>> entry : pointMap.entrySet()) {
            Point currentPoint = entry.getKey();
            List<Connection> connectionList = entry.getValue();
            LineWidget lineWidget = null;
            if (lastPoint != null) {
                boolean isBold = connectionList.stream().anyMatch(c -> c.getStyle() == Connection.ConnectionStyle.BOLD);
                boolean isDashed = connectionList.stream().allMatch(c -> c.getStyle() == Connection.ConnectionStyle.DASHED);
                boolean isVisible = connectionList.stream().noneMatch(c -> c.getStyle() == Connection.ConnectionStyle.INVISIBLE);

                Point src = new Point(lastPoint);
                Point dest = new Point(currentPoint);
                lineWidget = new LineWidget(this, fromFigureWidget, connectionList, src, dest, prevLineWidget, isBold, isDashed);
                lineWidget.setVisible(isVisible);

                connectionLayer.addChild(lineWidget);
                attachLineMovement(lineWidget);
            }
            createLineWidgets(fromFigureWidget, connectionList, controlPointIndex + 1, currentPoint, lineWidget);
        }
    }


    //  Movement logic encapsulation for LineWidgets
    private void attachLineMovement(LineWidget lineWidget) {
        lineWidget.getActions().addAction(ActionFactory.createMoveAction(null, new MoveProvider() {
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
                seaLayoutManager.moveLink(lineWidget.getFromFigureWidget().getFigure(), origFrom, newFrom);
                seaLayoutManager.writeBack();
                updateWidgetPositions();
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

    private void rebuildFigureLayer() {
        // clear Objects
        figureMap.clear();
        figureLayer.removeChildren();
        for (Figure figure : getDiagram().getFigures()) {
            FigureWidget figureWidget = new FigureWidget(figure, this);
            CustomSelectAction figureSelectAction = new CustomSelectAction(new SelectProvider() {
                public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                    return false;
                }

                public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                    return true;
                }

                public void select(Widget widget, Point localLocation, boolean invertSelection) {
                    EditorTopComponent editor = EditorTopComponent.getActive();
                    if (editor != null) {
                        editor.requestActive();
                    }
                    Set<Integer> nodeSelection = new HashSet<>();
                    nodeSelection.add(figure.getInputNode().getId());
                    selectedNodesByID = nodeSelection;
                }
            });
            figureWidget.getActions().addAction(figureSelectAction);
            attachFigureMovement(figureWidget);

            figureMap.put(figure, figureWidget);
            figureLayer.addChild(figureWidget);
        }
    }

    private void attachFigureMovement(FigureWidget figureWidget) {
        figureWidget.getActions().addAction(ActionFactory.createMoveAction(null, new MoveProvider() {

            private static final int MAGNET_SIZE = 5;
            private int startLayerY;

            @Override
            public void movementStarted(Widget widget) {
                widget.bringToFront();
                startLayerY = widget.getLocation().y;
            }

            @Override
            public void movementFinished(Widget widget) {
                Set<FigureWidget> selectedFigures = getSelectedFigureWidgets();
                for (FigureWidget fw : selectedFigures) {
                    Point newLocation = new Point(fw.getLocation().x, fw.getLocation().y);
                    seaLayoutManager.moveVertex(fw.getFigure(), newLocation);
                }
                updateWidgetPositions();
            }

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

                for (FigureWidget fw : getSelectedFigureWidgets()) {
                    if (figureWidgetToInLineWidgets.containsKey(fw)) {
                        for (LineWidget lw : figureWidgetToInLineWidgets.get(fw)) {
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
                    if (figureWidgetToOutLineWidgets.containsKey(fw)) {
                        for (LineWidget lw : figureWidgetToOutLineWidgets.get(fw)) {
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
    }

    public boolean getShowNodeHull() {
        return showNodeHull;
    }

    public void setShowNodeHull(boolean b) {
        showNodeHull = b;
        relayout();
    }

    public Set<Integer> getHiddenNodesByID() {
        return hiddenNodesByID;
    }

    public void setHiddenNodesByID(Set<Integer> nodes) {
        hiddenNodesByID = nodes;
        selectedNodesByID.removeAll(hiddenNodesByID);
        relayout();
    }

    public void extractSelectedNodes() {
        HashSet<Integer> allNodes = new HashSet<>(getGroup().getAllNodes());
        allNodes.removeAll(selectedNodesByID);
        setHiddenNodesByID(allNodes);
    }

    public void hideSelectedNodes() {
        HashSet<Integer> nodes = new HashSet<>(hiddenNodesByID);
        nodes.addAll(selectedNodesByID);
        setHiddenNodesByID(nodes);
    }

    private Set<FigureWidget> getSelectedFigureWidgets() {
        Set<FigureWidget> result = new HashSet<>();
        for (Map.Entry<Figure, FigureWidget> entry : figureMap.entrySet()) {
            Figure figure = entry.getKey();
            FigureWidget figureWidget = entry.getValue();
            if (selectedNodesByID.contains(figure.getInputNode().getId())) {
                result.add(figureWidget);
            }
        }
        return result;
    }

    @Override
    public void handleDoubleClick(Widget w, WidgetAction.WidgetMouseEvent e) {
        // clear selection
        selectedNodesByID = new HashSet<>();
    }

    public void validateAll() {
        super.validate();
        scrollPane.validate();
    }

    public Component getComponent() {
        return scrollPane;
    }

    void close() {
        filterChain.getChangedEvent().removeListener(filterChangedListener);
    }
}

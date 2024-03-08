package com.sun.hotspot.igv.view;

import com.sun.hotspot.igv.data.ChangedListener;
import com.sun.hotspot.igv.data.Group;
import com.sun.hotspot.igv.data.InputGraph;
import com.sun.hotspot.igv.filter.FilterChain;
import com.sun.hotspot.igv.filter.FilterChainProvider;
import com.sun.hotspot.igv.graph.Connection;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.hierarchicallayout.HierarchicalLayoutManager;
import com.sun.hotspot.igv.layout.LayoutGraph;
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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

public class DiagramScene extends ObjectScene {

    public static final float ALPHA = 0.4f;
    public static final float ZOOM_INCREMENT = 1.5f;
    private static final float ZOOM_MAX_FACTOR = 4.0f;
    private static final float ZOOM_MIN_FACTOR = 0.25f;
    private final JScrollPane scrollPane;
    private final Widget figureLayer;
    private final Widget connectionLayer;
    private final HierarchicalLayoutManager seaLayoutManager;
    private final FilterChain filtersOrder;
    private final FilterChain filterChain;
    private final Map<Figure, FigureWidget> figureToFigureWidget = new HashMap<>();
    private final Map<Figure, Set<LineWidget>> figureToOutLineWidgets = new HashMap<>();
    private final Map<Figure, Set<LineWidget>> figureToInLineWidgets = new HashMap<>();
    private final Group group;
    private int position;
    private Diagram diagram;
    private final ChangedListener<FilterChain> filterChangedListener = filter -> buildDiagram();

    public DiagramScene(InputGraph inputGraph) {
        group = inputGraph.getGroup();
        position = group.getGraphs().indexOf(inputGraph);

        FilterChainProvider provider = Lookup.getDefault().lookup(FilterChainProvider.class);
        assert provider != null;
        filterChain = provider.getFilterChain();
        filterChain.getChangedEvent().addListener(filterChangedListener);
        filtersOrder = provider.getAllFiltersOrdered();

        MouseZoomAction mouseZoomAction = new MouseZoomAction(this);
        scrollPane = createScrollPane(mouseZoomAction);

        connectionLayer = new Widget(this);
        addChild(connectionLayer);

        figureLayer = new Widget(this);
        addChild(figureLayer);

        getActions().addAction(new CustomizablePanAction(MouseEvent.BUTTON1_DOWN_MASK));
        getActions().addAction(mouseZoomAction);

        seaLayoutManager = new HierarchicalLayoutManager();

        buildDiagram();
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

    public void nextGraph() {
        if (position < group.size() - 1) {
            ++position;
            buildDiagram();
        }
    }

    public void previousGraph() {
        if (position > 0) {
            --position;
            buildDiagram();
        }
    }

    public InputGraph getGraph() {
        return group.getGraphs().get(position);
    }

    private void buildDiagram() {
        // create diagram
        diagram = new Diagram(getGraph());

        // filter diagram
        filterChain.applyInOrder(diagram, filtersOrder);

        // draw diagram
        rebuildFigureLayer();
        computeLayout();
        validateAll();
    }

    private void computeLayout() {  // visible nodes changed
        diagram.updateFigureVisibility();

        // Update FigureWidgets' visibility
        figureToFigureWidget.forEach((figure, fw) -> fw.setVisible(figure.isVisible()));

        Set<Figure> visibleFigures = diagram.getVisibleFigures();
        Set<Connection> visibleConnections = diagram.getVisibleConnections();
        seaLayoutManager.doLayout(new LayoutGraph(visibleConnections, visibleFigures));
        updateFigurePositions();
    }

    private void updateFigurePositions() {
        figureToOutLineWidgets.clear();
        figureToInLineWidgets.clear();
        connectionLayer.removeChildren();
        figureToFigureWidget.values().forEach(FigureWidget::updatePosition);
        figureToFigureWidget.keySet().forEach(f ->
                createLineWidgets(f, f.getVisibleConnections(), 0, null, null));
        validateAll();
    }

    private void createLineWidgets(Figure fromFigure, List<Connection> connections, int controlPointIndex, Point lastPoint, LineWidget prevLineWidget) {
        Map<Point, List<Connection>> pointMap = new HashMap<>(connections.size());
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
            if (lastPoint != null) {
                boolean isBold = connectionList.stream().anyMatch(c -> c.getStyle() == Connection.ConnectionStyle.BOLD);
                boolean isDashed = connectionList.stream().allMatch(c -> c.getStyle() == Connection.ConnectionStyle.DASHED);
                boolean isVisible = connectionList.stream().noneMatch(c -> c.getStyle() == Connection.ConnectionStyle.INVISIBLE);

                Point src = new Point(lastPoint);
                Point dest = new Point(currentPoint);
                LineWidget lineWidget = new LineWidget(this, fromFigure, connectionList, src, dest, prevLineWidget, isBold, isDashed);
                lineWidget.setVisible(isVisible);

                connectionLayer.addChild(lineWidget);
                attachLineMovement(lineWidget);

                if (controlPointIndex == 1) {
                    figureToOutLineWidgets.computeIfAbsent(fromFigure, k -> new HashSet<>()).add(lineWidget);
                }

                for (Connection connection : connections) {
                    if (controlPointIndex == connection.getControlPoints().size() - 1) {
                        figureToInLineWidgets.computeIfAbsent(connection.getTo(), k -> new HashSet<>()).add(lineWidget);
                    }
                }
                createLineWidgets(fromFigure, connectionList, controlPointIndex + 1, currentPoint, lineWidget);
            } else {
                createLineWidgets(fromFigure, connectionList, controlPointIndex + 1, currentPoint, null);
            }
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
                seaLayoutManager.moveLink(lineWidget.getFromFigure(), origFrom, newFrom);
                seaLayoutManager.writeBack();
                updateFigurePositions();
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
        figureToFigureWidget.clear();
        figureLayer.removeChildren();
        for (Figure figure : diagram.getFigures()) {
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
                    selectFigureExclusively(figure);
                }
            });
            figureWidget.getActions().addAction(figureSelectAction);
            attachFigureMovement(figureWidget);

            figureToFigureWidget.put(figure, figureWidget);
            figureLayer.addChild(figureWidget);
        }
    }

    private void attachFigureMovement(FigureWidget figureWidget) {
        figureWidget.getActions().addAction(ActionFactory.createMoveAction(null, new MoveProvider() {

            private static final int MAGNET_SIZE = 5;
            private int startLayerY;

            /**
             * Calculates the vertical shift for a widget to a new location, applying a magnetic snap effect
             * towards a specific y-coordinate if the target location is within a specified proximity.
             *
             * @param widget The widget that needs to be moved.
             * @param targetLoc The target location for the widget.
             * @param snapY The y-coordinate to snap to.
             * @param magnetRange The size of the magnetic effect range around the snapY.
             * @return The calculated vertical shift, adjusted for the magnetic snap effect if applicable.
             */
            private static int calculateMagnetShiftY(Widget widget, Point targetLoc, int snapY, int magnetRange) {
                int initialShiftY = targetLoc.y - widget.getLocation().y;
                boolean isTargetNearMagnet = Math.abs(targetLoc.y - snapY) <= magnetRange;
                boolean isCurrentNearMagnet = Math.abs(widget.getLocation().y - snapY) <= magnetRange;
                if (isTargetNearMagnet && !isCurrentNearMagnet) {
                    return snapY - widget.getLocation().y;
                } else if (isTargetNearMagnet) {
                    return 0;
                } else {
                    return initialShiftY;
                }
            }

            @Override
            public void movementStarted(Widget widget) {
                widget.bringToFront();
                startLayerY = widget.getLocation().y;
            }

            @Override
            public void movementFinished(Widget widget) {
                for (Figure figure : diagram.getSelectedFigures()) {
                    seaLayoutManager.moveVertex(figure, widget.getLocation());
                }
                updateFigurePositions();
            }

            @Override
            public Point getOriginalLocation(Widget widget) {
                return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
            }

            @Override
            public void setNewLocation(Widget widget, Point location) {
                int shiftX = location.x - widget.getLocation().x;
                int shiftY = calculateMagnetShiftY(widget, location, startLayerY, MAGNET_SIZE);
                diagram.getSelectedFigures().forEach(figure -> {
                    updateLineWidgets(figureToInLineWidgets.get(figure), shiftX, shiftY, true);
                    updateLineWidgets(figureToOutLineWidgets.get(figure), shiftX, shiftY, false);
                });
                Point newLoc = new Point(widget.getLocation().x + shiftX, widget.getLocation().y + shiftY);
                ActionFactory.createDefaultMoveProvider().setNewLocation(widget, newLoc);
            }

            /**
             * Updates the positions of line widgets based on the specified shifts.
             * It optionally shifts the predecessor or successors' positions to maintain relative positioning.
             *
             * @param lineWidgets The collection of line widgets to be updated.
             * @param shiftX      The horizontal shift to be applied.
             * @param shiftY      The vertical shift to be applied.
             * @param shiftPred   Flag indicating whether to shift the predecessor (true) or successors (false).
             */
            private static void updateLineWidgets(Collection<LineWidget> lineWidgets, int shiftX, int shiftY, boolean shiftPred) {
                if (lineWidgets == null) return;
                for (LineWidget lineWidget : lineWidgets) {
                    Point fromPt = lineWidget.getFrom();
                    Point toPt = lineWidget.getTo();
                    if (shiftPred) {
                        // Shift the line widget and its predecessor if applicable.
                        lineWidget.setFrom(new Point(fromPt.x + shiftX, fromPt.y));
                        lineWidget.setTo(new Point(toPt.x + shiftX, toPt.y + shiftY));
                        LineWidget pred = lineWidget.getPredecessor();
                        if (pred != null) {
                            pred.setTo(new Point(pred.getTo().x + shiftX, pred.getTo().y));
                            pred.revalidate();
                        }
                    } else {
                        // Shift the line widget and its successors.
                        lineWidget.setFrom(new Point(fromPt.x + shiftX, fromPt.y + shiftY));
                        lineWidget.setTo(new Point(toPt.x + shiftX, toPt.y));
                        for (LineWidget succ : lineWidget.getSuccessors()) {
                            succ.setFrom(new Point(succ.getFrom().x + shiftX, succ.getFrom().y));
                            succ.revalidate();
                        }
                    }
                    lineWidget.revalidate();
                }
            }
        }));
    }

    public boolean getShowBoundaryFigures() {
        return diagram.getShowBoundaryFigures();
    }

    public void setShowBoundaryFigures(boolean b) {
        diagram.setShowBoundaryFigures(b);
        computeLayout();
    }

    public List<Integer> getHiddenNodesByID() {
        return diagram.getHiddenNodesByID();
    }

    public void selectFigure(Figure figure) {
        diagram.selectFigure(figure);
    }

    private void selectFigureExclusively(Figure figure) {
        diagram.clearFigureSelection();
        diagram.selectFigure(figure);
    }

    public void clearFigureSelection() {
        diagram.clearFigureSelection();
    }

    public void extractFigure(Figure figure) {
        diagram.extractFigure(figure);
        computeLayout();
    }

    public void extractSelectedFigures() {
        diagram.extractSelectedFigures();
        computeLayout();
    }

    public void hideFigure(Figure figure) {
        diagram.hideFigure(figure);
        computeLayout();
    }

    public void hideSelectedFigures() {
        diagram.hideSelectedFigures();
        computeLayout();
    }

    public void showFigure(Figure figure) {
        diagram.showFigure(figure);
        computeLayout();
    }

    public void showAllFigures() {
        diagram.showAllFigures();
        computeLayout();
    }

    public boolean checkAllFiguresVisible() {
        return diagram.checkAllFiguresVisible();
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

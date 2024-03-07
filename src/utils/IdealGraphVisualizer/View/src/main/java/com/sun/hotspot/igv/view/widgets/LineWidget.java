package com.sun.hotspot.igv.view.widgets;

import com.sun.hotspot.igv.graph.Connection;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.graph.OutputSlot;
import com.sun.hotspot.igv.util.DoubleClickAction;
import com.sun.hotspot.igv.util.DoubleClickHandler;
import com.sun.hotspot.igv.util.StringUtils;
import com.sun.hotspot.igv.view.DiagramScene;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;


public class LineWidget extends Widget implements DoubleClickHandler {

    private final static double ZOOM_FACTOR = 0.1;
    public final int BORDER = 5;
    public final int ARROW_SIZE = 6;
    public final int BOLD_ARROW_SIZE = 7;
    public final int BOLD_STROKE_WIDTH = 2;
    private final OutputSlot outputSlot;
    private final DiagramScene scene;
    private final List<? extends Connection> connections;
    private final LineWidget predecessor;
    private final List<LineWidget> successors;
    private final boolean isBold;
    private final boolean isDashed;
    private Point from;
    private Point to;
    private Rectangle clientArea;

    public LineWidget(DiagramScene scene, OutputSlot s, List<? extends Connection> connections, Point from, Point to, LineWidget predecessor, boolean isBold, boolean isDashed) {
        super(scene);
        this.scene = scene;
        this.outputSlot = s;
        this.connections = connections;
        this.from = from;
        this.to = to;
        this.predecessor = predecessor;
        this.successors = new ArrayList<>();
        if (predecessor != null) {
            predecessor.addSuccessor(this);
        }

        this.isBold = isBold;
        this.isDashed = isDashed;

        computeClientArea();

        Color color = Color.BLACK;
        if (!connections.isEmpty()) {
            color = connections.get(0).getColor();
        }
        setToolTipText("<HTML>" + generateToolTipText(this.connections) + "</HTML>");

        setCheckClipping(false);
        setBackground(color);
        getActions().addAction(new DoubleClickAction(this));
    }


    public Point getClientAreaLocation() {
        return clientArea.getLocation();
    }

    private void computeClientArea() {
        int minX = from.x;
        int minY = from.y;
        int maxX = to.x;
        int maxY = to.y;
        if (minX > maxX) {
            int tmp = minX;
            minX = maxX;
            maxX = tmp;
        }

        if (minY > maxY) {
            int tmp = minY;
            minY = maxY;
            maxY = tmp;
        }

        clientArea = new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
        clientArea.grow(BORDER, BORDER);
    }

    private String generateToolTipText(List<? extends Connection> conn) {
        StringBuilder sb = new StringBuilder();
        for (Connection c : conn) {
            sb.append(StringUtils.escapeHTML(c.getToolTipText()));
            sb.append("<br>");
        }
        return sb.toString();
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
        computeClientArea();
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
        computeClientArea();
    }

    private void addSuccessor(LineWidget widget) {
        this.successors.add(widget);
    }

    @Override
    protected Rectangle calculateClientArea() {
        return clientArea;
    }

    @Override
    protected void paintWidget() {
        if (scene.getZoomFactor() < ZOOM_FACTOR) {
            return;
        }

        Graphics2D g = this.getGraphics();
        g.setPaint(this.getBackground());
        float width = 1.0f;

        if (isBold) {
            width = BOLD_STROKE_WIDTH;
        }

        Stroke oldStroke = g.getStroke();
        if (isDashed) {
            float[] dashPattern = {5, 5, 5, 5};
            g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10,
                    dashPattern, 0));
        } else {
            g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        }

        g.drawLine(from.x, from.y, to.x, to.y);

        boolean sameFrom = false;
        boolean sameTo = successors.isEmpty();
        for (LineWidget w : successors) {
            if (w.getFrom().equals(getTo())) {
                sameTo = true;
                break;
            }
        }

        if (predecessor == null || predecessor.getTo().equals(getFrom())) {
            sameFrom = true;
        }


        int size = ARROW_SIZE;
        if (isBold) {
            size = BOLD_ARROW_SIZE;
        }

        if (!sameFrom) {
            g.fillPolygon(
                    new int[]{from.x - size / 2, from.x + size / 2, from.x},
                    new int[]{from.y - size / 2, from.y - size / 2, from.y + size / 2},
                    3);
        }
        if (!sameTo) {
            g.fillPolygon(
                    new int[]{to.x - size / 2, to.x + size / 2, to.x},
                    new int[]{to.y - size / 2, to.y - size / 2, to.y + size / 2},
                    3);
        }
        g.setStroke(oldStroke);
        super.paintWidget();
    }

    @Override
    public boolean isHitAt(Point localPoint) {
        return Line2D.ptLineDistSq(from.x, from.y, to.x, to.y, localPoint.x, localPoint.y) <= BORDER * BORDER;
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
    }

    public LineWidget getPredecessor() {
        return predecessor;
    }

    public List<LineWidget> getSuccessors() {
        return successors;
    }

    public Figure getFromFigure() {
        if (outputSlot != null) {
            return outputSlot.getFigure();
        }
        return null;
    }

    @Override
    public void handleDoubleClick(Widget w, WidgetAction.WidgetMouseEvent event) {
        Set<Object> selectedObjects = new HashSet<>();

        boolean additiveSelection = (event.getModifiersEx() & DoubleClickAction.getModifierMask()) != 0;
        if (additiveSelection) {
            selectedObjects.addAll(scene.getSelectedObjects());
        }

        for (Connection connection : connections) {
            selectedObjects.add(connection.getTo());
            selectedObjects.add(connection.getFrom());
        }
        scene.setSelectedObjects(selectedObjects);
    }
}

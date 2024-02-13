package com.sun.hotspot.igv.demo;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

public class ZoomableScrollPane extends ScrollPane {

    private double scaleValue = 1.0;
    private final Scale scaleTransform;
    private final double zoomIntensity = 0.02;

    public ZoomableScrollPane(Pane content) {
        super(content);
        setPannable(false);
        setContent(content);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        content.getTransforms().add(scaleTransform);
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (!event.isControlDown()) return;

            // Calculate the scale factor based on the direction of the scroll.
            double scaleFactor = (event.getDeltaY() > 0) ? 1 + zoomIntensity : 1 - zoomIntensity;

            // Adjust the scale value based on the calculated scale factor.
            scaleValue *= scaleFactor;

            // Calculate the position of the mouse relative to the content's current scale.
            Point2D mousePosInZoomTarget = content.parentToLocal(new Point2D(event.getX(), event.getY()));
            Point2D pivotOnTarget = content.parentToLocal(content.localToParent(mousePosInZoomTarget));

            // Update the scale transformation to apply the new zoom level.
            scaleTransform.setX(scaleValue);
            scaleTransform.setY(scaleValue);

            // Set the pivot points dynamically based on the current mouse position.
            scaleTransform.setPivotX(pivotOnTarget.getX());
            scaleTransform.setPivotY(pivotOnTarget.getY());

            event.consume();
        });
        /*
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (isDragging()) {
                // Optionally consume the event if you want to prevent zooming while dragging
                event.consume();
                return; // Skip handling if dragging is in progress
            }

            if (!event.isControlDown() || event.getDeltaY() == 0) {
                return; // Ignore if not zooming
            }

            double scaleFactor = (event.getDeltaY() > 0) ? 1 + zoomIntensity : 1 - zoomIntensity;
            scaleValue *= scaleFactor; // Update the scale value based on zoom direction

            // Calculate the mouse's position relative to the content
            double viewportCenterX = getHvalue() * (content.getWidth() - getViewportBounds().getWidth());
            double viewportCenterY = getVvalue() * (content.getHeight() - getViewportBounds().getHeight());
            double mouseContentX = event.getX() + viewportCenterX;
            double mouseContentY = event.getY() + viewportCenterY;
            System.out.println(event.getX() + " , " + event.getY());

            // Set the pivot points for scaling and apply the new scale value
            scaleTransform.setPivotX(mouseContentX);
            scaleTransform.setPivotY(mouseContentY);
            scaleTransform.setX(scaleValue);
            scaleTransform.setY(scaleValue);

            event.consume();
        });

         */
    }

    public double getScaleValue() {
        return scaleValue;
    }

    public void zoomToActual() {
        scaleValue = 1.0;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);
    }

    public void zoomIn() {
        scaleValue += zoomIntensity;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);
    }

    public void zoomOut() {
        scaleValue -= zoomIntensity;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);
    }

    private boolean isDragging = false;

    // Existing constructor and methods...

    public void setDragging(boolean isDragging) {
        setPannable(!isDragging);
        this.isDragging = isDragging;
    }

    public boolean isDragging() {
        return isDragging;
    }
}


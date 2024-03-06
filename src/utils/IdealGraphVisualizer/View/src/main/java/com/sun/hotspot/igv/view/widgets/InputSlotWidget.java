package com.sun.hotspot.igv.view.widgets;

import com.sun.hotspot.igv.graph.Connection;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.graph.InputSlot;
import com.sun.hotspot.igv.view.DiagramScene;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class InputSlotWidget extends SlotWidget {

    private final InputSlot inputSlot;
    private final DiagramScene scene;

    public InputSlotWidget(InputSlot slot, DiagramScene scene, FigureWidget fw) {
        super(slot, scene, fw);
        inputSlot = slot;
        this.scene = scene;
    }

    @Override
    protected int yOffset() {
        return 0;
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();
        if (getScene().getZoomFactor() < TEXT_ZOOM_FACTOR) {
            return;
        }
        // If there is a gap between the current slot and the previous one, and
        // both are visible, draw a label in between signaling the gap.
        int index = inputSlot.getPosition();
        int originalIndex = inputSlot.getOriginalIndex();
        InputSlot prevSlot = index > 0 ? inputSlot.getFigure().getInputSlots().get(index - 1) : null;
        int prevOriginalIndex = index > 0 ? prevSlot.getOriginalIndex() : -1;
        if (originalIndex > prevOriginalIndex + 1 &&
                hasVisibleConnection(inputSlot) && hasVisibleConnection(prevSlot)) {
            Graphics2D g = scene.getGraphics();
            String label = "...";
            g.setColor(Color.BLACK);
            g.setFont(Diagram.SLOT_FONT.deriveFont(Font.BOLD));
            Rectangle2D labelRect = new Canvas().getFontMetrics(Diagram.SLOT_FONT).getStringBounds(label, g);
            int slotWidth = this.calculateClientArea().width;
            assert this.getBounds() != null;
            int xStart = this.getBounds().x + (inputSlot.hasSourceNodes() ? 0 : (slotWidth / 2));
            int prevXEnd;
            if (index > 0) {
                // Compute X coordinates of previous input slot comparing its
                // calculateClientArea() with that of the current slot.
                InputSlotWidget prevWidget = (InputSlotWidget) scene.findSlotWidget(prevSlot);

                int prevSlotWidth = prevWidget.calculateClientArea().width;
                int xStartAbs = inputSlot.getRelativePosition().x - (slotWidth / 2);
                int prevXStartAbs = prevSlot.getRelativePosition().x - (prevSlotWidth / 2);
                int prevXStart = prevXStartAbs - xStartAbs;
                prevXEnd = prevXStart + (prevSlot.hasSourceNodes() ? prevSlotWidth : (prevSlotWidth / 2));
            } else {
                // No previous input slot, just set its position to the left of
                // the current one.
                prevXEnd = xStart - (int) (labelRect.getWidth()) - 4;
            }
            int midX = (prevXEnd + xStart) / 2;
            g.drawString(label, midX - (int) (labelRect.getWidth() / 2), 3);
        }
    }

    // This method needs to be called at painting time, so that the right
    // FigureWidget::isVisible() result is picked up.
    private boolean hasVisibleConnection(InputSlot slot) {
        if (slot == null) {
            return true;
        }
        if (slot.hasSourceNodes()) {
            return true;
        }
        for (Connection c : slot.getConnections()) {
            Figure f = c.getOutputSlot().getFigure();
            FigureWidget fw = scene.findFigureWidget(f);
            if (fw.isVisible()) {
                return true;
            }
        }
        return false;
    }
}

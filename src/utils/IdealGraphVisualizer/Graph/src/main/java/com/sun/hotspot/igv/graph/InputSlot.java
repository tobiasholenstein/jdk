package com.sun.hotspot.igv.graph;

import java.awt.Point;

public class InputSlot extends Slot {

    private int originalIndex;

    protected InputSlot(Figure figure, int wantedIndex) {
        super(figure, wantedIndex);
        this.originalIndex = -1;
    }

    @Override
    public int getPosition() {
        return getFigure().getInputSlots().indexOf(this);
    }

    @Override
    public int yOffset() {
        return 0;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    @Override
    public Point getRelativePosition() {
        int gap = getFigure().getWidth() - Figure.getSlotsWidth(getFigure().getInputSlots());
        double gapRatio = (double) gap / (double) (getFigure().getInputSlots().size() + 1);
        int gapAmount = (int) ((getPosition() + 1) * gapRatio);
        return new Point(gapAmount + Figure.getSlotsWidth(getAllBefore(getFigure().getInputSlots(), this)) + getWidth() / 2, 0);
    }

    @Override
    public String getToolTipText() {
        return super.getToolTipText() + " [" + originalIndex + "]";
    }

    @Override
    public String toString() {
        return "InputSlot[figure=" + this.getFigure().toString() + ", position=" + getPosition() + "]";
    }
}

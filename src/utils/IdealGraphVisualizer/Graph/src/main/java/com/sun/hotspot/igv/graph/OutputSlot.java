package com.sun.hotspot.igv.graph;

import java.awt.Point;

public class OutputSlot extends Slot {

    protected OutputSlot(Figure figure, int wantedIndex) {
        super(figure, wantedIndex);
    }

    @Override
    public int getPosition() {
        return getFigure().getOutputSlots().indexOf(this);
    }

    @Override
    public Point getRelativePosition() {
        int gap = getFigure().getWidth() - Figure.getSlotsWidth(getFigure().getOutputSlots());
        if (gap < 0) {
            gap = 0;
        }
        double gapRatio = (double) gap / (double) (getFigure().getOutputSlots().size() + 1);
        int gapAmount = (int) ((getPosition() + 1) * gapRatio);
        return new Point(gapAmount + Figure.getSlotsWidth(getAllBefore(getFigure().getOutputSlots(), this)) + getWidth() / 2, 0);
    }

    @Override
    public String toString() {
        return "OutputSlot[figure=" + this.getFigure().toString() + ", position=" + getPosition() + "]";
    }

    @Override
    public int yOffset() {
        return getFigure().getHeight();
    }
}

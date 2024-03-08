package com.sun.hotspot.igv.hierarchicallayout;

import java.util.ArrayList;
import java.util.Collection;

public class LayoutLayer extends ArrayList<LayoutNode> {

    private int height = 0;
    private int y = 0;

    @Override
    public boolean addAll(Collection<? extends LayoutNode> c) {
        c.forEach(this::updateHeight);
        return super.addAll(c);
    }

    private void updateHeight(LayoutNode n) {
        height = Math.max(height, n.getOuterHeight());
    }

    @Override
    public boolean add(LayoutNode n) {
        updateHeight(n);
        return super.add(n);
    }

    public void swap(int i, int j) {
        LayoutNode n1 = get(i);
        LayoutNode n2 = get(j);
        int x1 = n1.getX();
        int x2 = n2.getX();
        n1.setX(x2);
        n2.setX(x1);
        int p1 = n1.getPos();
        int p2 = n2.getPos();
        n1.setPos(p2);
        n2.setPos(p1);
        set(j, n1);
        set(i, n2);
    }

    public void shiftTop(int shift) {
        y += shift;
    }

    public int getTop() {
        return y;
    }

    public void setTop(int top) {
        y = top;
    }

    public int getCenter() {
        return y + height / 2;
    }

    public int getBottom() {
        return y + height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

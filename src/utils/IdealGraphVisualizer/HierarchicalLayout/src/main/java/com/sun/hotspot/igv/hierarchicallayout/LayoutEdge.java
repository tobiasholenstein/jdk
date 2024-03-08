package com.sun.hotspot.igv.hierarchicallayout;

import com.sun.hotspot.igv.layout.Link;

public class LayoutEdge {

    private LayoutNode from;
    private LayoutNode to;
    // Horizontal distance relative to start of 'from'.
    private int relativeFromX;
    // Horizontal distance relative to start of 'to'.
    private int relativeToX;
    private Link link;
    private boolean isReversed;

    public LayoutEdge(LayoutNode from, LayoutNode to) {
        this.from = from;
        this.to = to;
        this.isReversed = false;
    }

    public LayoutEdge(LayoutNode from, LayoutNode to, int relativeFromX, int relativeToX, Link link) {
        this(from, to);
        this.relativeFromX = relativeFromX;
        this.relativeToX = relativeToX;
        this.link = link;
    }

    public int getStartX() {
        return relativeFromX + from.getLeft();
    }

    public int getEndX() {
        return relativeToX + to.getLeft();
    }

    public void reverse() {
        isReversed = !isReversed;
    }

    public boolean isReversed() {
        return isReversed;
    }

    @Override
    public String toString() {
        return "Edge " + from + ", " + to;
    }

    public LayoutNode getFrom() {
        return from;
    }

    public void setFrom(LayoutNode from) {
        this.from = from;
    }

    public LayoutNode getTo() {
        return to;
    }

    public void setTo(LayoutNode to) {
        this.to = to;
    }

    public int getFromX() {
        return from.getX() + getRelativeFromX();
    }

    public int getToX() {
        return to.getX() + getRelativeToX();
    }

    public int getRelativeFromX() {
        return relativeFromX;
    }

    public void setRelativeFromX(int relativeFromX) {
        this.relativeFromX = relativeFromX;
    }

    public int getRelativeToX() {
        return relativeToX;
    }

    public void setRelativeToX(int relativeToX) {
        this.relativeToX = relativeToX;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}

package com.sun.hotspot.igv.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;


public interface Vertex extends Comparable<Vertex> {

    int getPriority();

    Set<Link> getInputLinks();

    Set<Link> getOutputLinks();

    Dimension getSize();

    Point getPosition();

    void setPosition(Point p);

    boolean isRoot();
}

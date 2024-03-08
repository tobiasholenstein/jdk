/*
 * Copyright (c) 2008, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */
package com.sun.hotspot.igv.graph;

import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Port;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Link {

    private final InputSlot inputSlot;
    private final OutputSlot outputSlot;
    private final String label;
    private Color color;
    private ConnectionStyle style;
    private List<Point> controlPoints;
    protected Connection(InputSlot inputSlot, OutputSlot outputSlot, String label) {
        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.label = label;
        this.controlPoints = new ArrayList<>();
        this.color = Color.BLACK;
        this.style = ConnectionStyle.NORMAL;
    }

    public boolean isVisible() {
        return getTo().isVisible() && getFrom().isVisible() && style != ConnectionStyle.INVISIBLE;
    }

    public InputSlot getInputSlot() {
        return inputSlot;
    }

    public OutputSlot getOutputSlot() {
        return outputSlot;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public ConnectionStyle getStyle() {
        return style;
    }

    public void setStyle(ConnectionStyle s) {
        style = s;
    }

    public String getLabel() {
        return label;
    }

    public void remove() {
        inputSlot.getFigure().removePredecessor(outputSlot.getFigure());
        inputSlot.connections.remove(this);
        outputSlot.getFigure().removeSuccessor(inputSlot.getFigure());
        outputSlot.connections.remove(this);
    }

    public String getToolTipText() {
        StringBuilder builder = new StringBuilder();
        if (label != null) {
            builder.append(label).append(": ");
        }
        // Resolve strings lazily every time the tooltip is shown, instead of
        // eagerly as for node labels, for efficiency.
        String shortNodeText = getInputSlot().getFigure().getDiagram().getShortNodeText();
        builder.append(getOutputSlot().getFigure().getProperties().resolveString(shortNodeText));
        builder.append(" → ");
        builder.append(getInputSlot().getFigure().getProperties().resolveString(shortNodeText));
        builder.append(" [")
                .append(getInputSlot().getOriginalIndex())
                .append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "FigureConnection('" + label + "', " + getFrom() + " to " + getTo() + ")";
    }

    @Override
    public Port getFromPort() {
        return outputSlot;
    }

    @Override
    public Port getToPort() {
        return inputSlot;
    }

    @Override
    public Figure getFrom() {
        return outputSlot.getFigure();
    }

    @Override
    public Figure getTo() {
        return inputSlot.getFigure();
    }

    @Override
    public List<Point> getControlPoints() {
        return controlPoints;
    }

    @Override
    public void setControlPoints(List<Point> list) {
        controlPoints = list;
    }

    public enum ConnectionStyle {
        NORMAL,
        DASHED,
        BOLD,
        INVISIBLE
    }
}


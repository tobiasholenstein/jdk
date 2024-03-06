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

import com.sun.hotspot.igv.data.InputNode;
import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.layout.Cluster;
import com.sun.hotspot.igv.layout.Vertex;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Figure extends Properties.Entity implements Vertex {

    public static final int PADDING = 4;
    public static final int SLOT_OFFSET = 16;
    public static final int WARNING_WIDTH = 16;
    public static final int BORDER = 1;
    public static final double BOLD_LINE_FACTOR = 1.06;
    protected List<InputSlot> inputSlots;
    protected List<OutputSlot> outputSlots;
    private final InputNode inputNode;
    private final Diagram diagram;
    private Point position;
    private final List<Figure> predecessors;
    private final List<Figure> successors;
    private Color color;
    private String warning;
    private final int id;
    private String[] lines;
    private int height = -1;
    private int width = -1;
    private Block block;
    private final FontMetrics metrics;

    public int getHeight() {
        if (height == -1) {
            updateHeight();
        }
        return height;
    }

    public int getSlotsHeight() {
        int slotHeight = 0;
        if (hasNamedInputSlot() || hasNamedOutputSlot()) {
            slotHeight += Slot.SLOT_HEIGHT;
        }
        return slotHeight;
    }

    private void updateHeight() {
        height = getLines().length * metrics.getHeight() + 2 * PADDING;
        height += getSlotsHeight();
    }

    public static <T> List<T> getAllBefore(List<T> inputList, T tIn) {
        List<T> result = new ArrayList<>();
        for(T t : inputList) {
            if(t.equals(tIn)) {
                break;
            }
            result.add(t);
        }
        return result;
    }

    public static int getSlotsWidth(Collection<? extends Slot> slots) {
        int result = Figure.SLOT_OFFSET;
        for(Slot s : slots) {
            result += s.getWidth() + Figure.SLOT_OFFSET;
        }
        return result;
    }

    public int getWidth() {
        if (width == -1) {
            updateWidth();
        }
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private void updateWidth() {
        width = 0;
        for (String s : getLines()) {
            int cur = metrics.stringWidth(s);
            if (cur > width) {
                width = cur;
            }
        }
        width += 2 * PADDING;
        if (getWarning() != null) {
            width += WARNING_WIDTH + PADDING;
        }
        width = Math.max(width, Figure.getSlotsWidth(inputSlots));
        width = Math.max(width, Figure.getSlotsWidth(outputSlots));
        width = (int)(width * BOLD_LINE_FACTOR);
    }

    protected Figure(Diagram diagram, int id, InputNode node) {
        this.diagram = diagram;
        this.inputNode = node;
        this.inputSlots = new ArrayList<>(5);
        this.outputSlots = new ArrayList<>(1);
        this.predecessors = new ArrayList<>(6);
        this.successors = new ArrayList<>(6);
        this.id = id;
        this.position = new Point(0, 0);
        this.color = Color.WHITE;
        Canvas canvas = new Canvas();
        this.metrics = canvas.getFontMetrics(Diagram.FONT.deriveFont(Font.BOLD));
    }

    public int getId() {
        return id;
    }

    public void setColor(Color newColor) {
        if (newColor == Color.WHITE) {
            this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
        } else {
            this.color = newColor;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setWarning(String warning) {
        this.warning = getProperties().resolveString(warning);
    }

    public String getWarning() {
        return warning;
    }

    public Block getBlock() {
        return block;
    }

    public List<Figure> getPredecessors() {
        return Collections.unmodifiableList(predecessors);
    }

    public Set<Figure> getPredecessorSet() {
        return Collections.unmodifiableSet(new HashSet<>(getPredecessors()));
    }

    public Set<Figure> getSuccessorSet() {
        return Collections.unmodifiableSet(new HashSet<>(getSuccessors()));
    }

    public List<Figure> getSuccessors() {
        return Collections.unmodifiableList(successors);
    }

    protected void addPredecessor(Figure f) {
        this.predecessors.add(f);
    }

    protected void addSuccessor(Figure f) {
        this.successors.add(f);
    }

    protected void removePredecessor(Figure f) {
        assert predecessors.contains(f);
        predecessors.remove(f);
    }

    protected void removeSuccessor(Figure f) {
        assert successors.contains(f);
        successors.remove(f);
    }

    @Override
    public void setPosition(Point p) {
        this.position = p;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public InputNode getInputNode() {
        return inputNode;
    }

    public InputSlot createInputSlot() {
        InputSlot slot = new InputSlot(this, -1);
        inputSlots.add(slot);
        return slot;
    }

    public void removeSlot(Slot s) {

        assert inputSlots.contains(s) || outputSlots.contains(s);

        List<FigureConnection> connections = new ArrayList<>(s.getConnections());
        for (FigureConnection c : connections) {
            c.remove();
        }

        if (inputSlots.contains(s)) {
            inputSlots.remove(s);
        } else outputSlots.remove(s);
    }

    public void createOutputSlot() {
        OutputSlot slot = new OutputSlot(this, -1);
        outputSlots.add(slot);
    }

    public OutputSlot createOutputSlot(int index) {
        OutputSlot slot = new OutputSlot(this, index);
        outputSlots.add(slot);
        outputSlots.sort(Slot.slotIndexComparator);
        return slot;
    }

    public List<InputSlot> getInputSlots() {
        return Collections.unmodifiableList(inputSlots);
    }

    public Set<Slot> getSlots() {
        Set<Slot> result = new HashSet<>();
        result.addAll(getInputSlots());
        result.addAll(getOutputSlots());
        return result;
    }

    public List<OutputSlot> getOutputSlots() {
        return Collections.unmodifiableList(outputSlots);
    }

    public boolean hasNamedInputSlot() {
        for (InputSlot is : getInputSlots()) {
            if (is.hasSourceNodes() && is.shouldShowName()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNamedOutputSlot() {
        for (OutputSlot os : getOutputSlots()) {
            if (os.hasSourceNodes() && os.shouldShowName()) {
                return true;
            }
        }
        return false;
    }

    void removeInputSlot(InputSlot s) {
        s.removeAllConnections();
        inputSlots.remove(s);
    }

    void removeOutputSlot(OutputSlot s) {
        s.removeAllConnections();
        outputSlots.remove(s);
    }

    public String[] getLines() {
        if (lines == null) {
            updateLines();
        }
        return lines;
    }

    public void updateLines() {
        String[] strings = diagram.getNodeText().split("\n");
        List<String> result = new ArrayList<>(strings.length + 1);

        for (String string : strings) {
            result.add(getProperties().resolveString(string));
        }

        String extraLabel = getProperties().get("extra_label");
        if (extraLabel != null) {
            result.add(extraLabel);
        }

        lines = result.toArray(new String[0]);
        // Set the "label" property of the input node, so that by default
        // search is done on the node label (without line breaks). See also
        // class NodeQuickSearch in the View module.
        String label = inputNode.getProperties().resolveString(diagram.getNodeText());
        inputNode.getProperties().setProperty("label", label.replaceAll("\\R", " "));

        // Update figure dimensions, as these are affected by the node text.
        updateWidth();
        updateHeight();
    }

    @Override
    public int getPrority() {
        String category = getInputNode().getProperties().get("category");
        if (category.equals("control")) {
            return 5;
        } else if (category.equals("mixed")) {
            return 4;
        } else if (category.equals("other")) {
            return 3;
        } else if (category.equals("data")) {
            return 2;
        } else if (category.equals("memory")) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Dimension getSize() {
        int width = getWidth();
        int height = getHeight();
        return new Dimension(width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Figure)) {
            return false;
        }
        return getInputNode().equals(((Figure) o).getInputNode());
    }

    @Override
    public int hashCode() {
        return getInputNode().hashCode();
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public Cluster getCluster() {
        return block;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean isRoot() {
        if (inputNode != null && inputNode.getProperties().get("name") != null) {
            return inputNode.getProperties().get("name").equals("Root");
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Vertex f) {
        return toString().compareTo(f.toString());
    }
}

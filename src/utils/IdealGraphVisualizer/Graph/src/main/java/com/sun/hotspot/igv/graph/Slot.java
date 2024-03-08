package com.sun.hotspot.igv.graph;

import com.sun.hotspot.igv.data.InputNode;
import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.layout.Port;
import com.sun.hotspot.igv.util.StringUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class Slot implements Port, Properties.Provider {
    public static final int SLOT_WIDTH = 10;
    public static final int SLOT_HEIGHT = 10;
    public static final Comparator<Slot> slotIndexComparator = Comparator.comparingInt(o -> o.wantedIndex);
    private final int wantedIndex;
    private final Figure figure;
    protected List<Connection> connections;
    private Color color;
    private String text;
    private String shortName;

    private InputNode sourceNode;

    protected Slot(Figure figure, int wantedIndex) {
        this.figure = figure;
        connections = new ArrayList<>(2);
        this.wantedIndex = wantedIndex;
        text = "";
        shortName = "";
        sourceNode = null;
        assert figure != null;
    }

    @Override
    public Properties getProperties() {
        Properties p = new Properties();
        if (sourceNode != null) {
            p.add(sourceNode.getProperties());
        } else {
            p.setProperty("name", "Slot");
            p.setProperty("figure", figure.getProperties().get("name"));
            p.setProperty("connectionCount", Integer.toString(connections.size()));
        }
        return p;
    }

    public void setSourceNode(InputNode n) {
        sourceNode = n;
    }

    public int getWidth() {
        assert shortName != null;
        if (shortName.isEmpty()) {
            return 0;
        } else {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setFont(Diagram.SLOT_FONT.deriveFont(Font.BOLD));
            FontMetrics metrics = g.getFontMetrics();
            return Math.max(SLOT_WIDTH, metrics.stringWidth(shortName) + 6);
        }
    }

    public int getHeight() {
        return SLOT_HEIGHT;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String s) {
        assert s != null;
        this.shortName = s;

    }

    public String getToolTipText() {
        StringBuilder sb = new StringBuilder();
        String shortNodeText = figure.getDiagram().getShortNodeText();
        if (!text.isEmpty()) {
            sb.append(text);
            if (!shortNodeText.isEmpty()) {
                sb.append(": ");
            }
        }

        sb.append(StringUtils.escapeHTML(sourceNode.getProperties().resolveString(shortNodeText)));

        return sb.toString();
    }

    public boolean shouldShowName() {
        return getShortName() != null && !getShortName().isEmpty();
    }

    public boolean hasSourceNodes() {
        return sourceNode != null;
    }

    public void setText(String s) {
        if (s == null) {
            s = "";
        }
        this.text = s;
    }

    public Figure getFigure() {
        return figure;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public List<Connection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    public void removeAllConnections() {
        List<Connection> connectionsCopy = new ArrayList<>(this.connections);
        for (Connection c : connectionsCopy) {
            c.remove();
        }
    }

    public abstract int getPosition();

    public static <T> List<T> getAllBefore(List<T> inputList, T tIn) {
        List<T> result = new ArrayList<>();
        for (T t : inputList) {
            if (t.equals(tIn)) {
                break;
            }
            result.add(t);
        }
        return result;
    }

    public abstract int yOffset();
}


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
package com.sun.hotspot.igv.view.widgets;

import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.graph.Diagram;
import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.graph.InputSlot;
import com.sun.hotspot.igv.graph.OutputSlot;
import com.sun.hotspot.igv.util.DoubleClickAction;
import com.sun.hotspot.igv.util.DoubleClickHandler;
import com.sun.hotspot.igv.util.PropertiesConverter;
import com.sun.hotspot.igv.util.PropertiesSheet;
import com.sun.hotspot.igv.view.DiagramScene;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory.SerialAlignment;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 * @author Thomas Wuerthinger
 */
public class FigureWidget extends Widget implements Properties.Provider, DoubleClickHandler {

    private static final double LABEL_ZOOM_FACTOR = 0.3;
    private static final Image warningSign = ImageUtilities.loadImage("com/sun/hotspot/igv/view/images/warning.png");
    private final Figure figure;
    private final Widget middleWidget;
    private final ArrayList<LabelWidget> labelWidgets;
    private final DiagramScene diagramScene;

    public FigureWidget(final Figure f, DiagramScene scene) {
        super(scene);

        assert this.getScene() != null;
        assert this.getScene().getView() != null;

        this.figure = f;
        this.setCheckClipping(true);
        this.diagramScene = scene;

        middleWidget = new Widget(scene);
        middleWidget.setPreferredBounds(new Rectangle(0, 0, f.getWidth(), f.getHeight()));
        middleWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(SerialAlignment.CENTER, 0));
        middleWidget.setBackground(f.getColor());
        middleWidget.setOpaque(true);
        middleWidget.getActions().addAction(new DoubleClickAction(this));
        middleWidget.setCheckClipping(false);
        addChild(middleWidget);

        Widget textWidget = new Widget(scene);
        textWidget.setLayout(LayoutFactory.createVerticalFlowLayout(SerialAlignment.CENTER, 0));
        middleWidget.addChild(textWidget);

        String[] strings = figure.getLines();
        labelWidgets = new ArrayList<>(strings.length);

        for (String displayString : strings) {
            LabelWidget lw = new LabelWidget(scene);
            labelWidgets.add(lw);
            textWidget.addChild(lw);
            lw.setLabel(displayString);
            lw.setFont(Diagram.FONT);
            lw.setForeground(getTextColor());
            lw.setAlignment(LabelWidget.Alignment.CENTER);
            lw.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
            lw.setCheckClipping(false);
        }
        formatExtraLabel();

        for (int i = 1; i < labelWidgets.size(); i++) {
            labelWidgets.get(i).setFont(Diagram.FONT.deriveFont(Font.ITALIC));
            labelWidgets.get(i).setForeground(Color.DARK_GRAY);
        }


        int textHeight = f.getHeight() - 2 * Figure.PADDING - f.getSlotsHeight();
        if (getFigure().getWarning() != null) {
            ImageWidget warningWidget = new ImageWidget(scene, warningSign);
            warningWidget.setToolTipText(getFigure().getWarning());
            middleWidget.addChild(warningWidget);
            int textWidth = f.getWidth() - 4 * Figure.BORDER;
            textWidth -= Figure.WARNING_WIDTH + Figure.PADDING;
            textWidget.setPreferredBounds(new Rectangle(0, 0, textWidth, textHeight));
        } else {
            int textWidth = f.getWidth() - 4 * Figure.BORDER;
            textWidget.setPreferredBounds(new Rectangle(0, 0, textWidth, textHeight));
        }

        // Initialize node for property sheet
        Node node = new AbstractNode(Children.LEAF) {

            @Override
            protected Sheet createSheet() {
                Sheet s = super.createSheet();
                PropertiesSheet.initializeSheet(f.getProperties(), s);
                return s;
            }
        };
        node.setDisplayName(getName());

        setToolTipText(PropertiesConverter.convertToHTML(f.getProperties()));

        addBorder();
        addSlots();
    }

    private void addSlots() {
        for (InputSlot inputSlot : figure.getInputSlots()) {
            InputSlotWidget slotWidget = new InputSlotWidget(inputSlot, diagramScene, this);
            this.addChild(slotWidget);
        }

        for (OutputSlot outputSlot : figure.getOutputSlots()) {
            OutputSlotWidget slotWidget = new OutputSlotWidget(outputSlot, diagramScene, this);
            this.addChild(slotWidget);
        }
    }

    public void updatePosition() {
        if (figure.isVisible()) {
            setPreferredLocation(figure.getPosition());
        }
    }

    private void formatExtraLabel() {
        if (getFigure().getProperties().get("extra_label") != null) {
            LabelWidget extraLabelWidget = labelWidgets.get(labelWidgets.size() - 1);
            extraLabelWidget.setFont(Diagram.FONT.deriveFont(Font.ITALIC));
            extraLabelWidget.setForeground(Color.DARK_GRAY);
        }
    }

    public int getFigureHeight() {
        return middleWidget.getPreferredBounds().height;
    }

    private void addBorder() {
        Font font = Diagram.FONT;
        Color borderColor = Color.BLACK;
        Color innerBorderColor = getFigure().getColor();

        Border innerBorder = new RoundedBorder(borderColor, Figure.BORDER);
        Border outerBorder = new RoundedBorder(innerBorderColor, Figure.BORDER);
        Border roundedBorder = BorderFactory.createCompoundBorder(innerBorder, outerBorder);
        middleWidget.setBorder(roundedBorder);

        for (LabelWidget labelWidget : labelWidgets) {
            labelWidget.setFont(font);
        }
        repaint();
    }

    public String getName() {
        return getProperties().get("name");
    }

    @Override
    public Properties getProperties() {
        return figure.getProperties();
    }

    public Figure getFigure() {
        return figure;
    }

    private Color getTextColor() {
        Color bg = figure.getColor();
        double brightness = bg.getRed() * 0.21 + bg.getGreen() * 0.72 + bg.getBlue() * 0.07;
        if (brightness < 150) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    @Override
    protected void paintChildren() {
        Composite oldComposite = null;
        if (figure.isBoundary()) {
            oldComposite = getScene().getGraphics().getComposite();
            float alpha = DiagramScene.ALPHA;
            this.getScene().getGraphics().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }

        if (diagramScene.getZoomFactor() < LABEL_ZOOM_FACTOR) {
            for (LabelWidget labelWidget : labelWidgets) {
                labelWidget.setVisible(false);
            }
            super.paintChildren();
            for (LabelWidget labelWidget : labelWidgets) {
                labelWidget.setVisible(true);
            }
        } else {
            Color oldColor = null;
            if (figure.isBoundary()) {
                for (LabelWidget labelWidget : labelWidgets) {
                    oldColor = labelWidget.getForeground();
                    labelWidget.setForeground(Color.BLACK);
                }
            }
            super.paintChildren();
            if (figure.isBoundary()) {
                for (LabelWidget labelWidget : labelWidgets) {
                    labelWidget.setForeground(oldColor);
                }
            }
        }

        if (figure.isBoundary()) {
            getScene().getGraphics().setComposite(oldComposite);
        }
    }

    @Override
    public void handleDoubleClick(Widget w, WidgetAction.WidgetMouseEvent e) {
        if (diagramScene.getHiddenNodesByID().isEmpty()) {
            final Set<Integer> hiddenNodes = new HashSet<>(diagramScene.getGroup().getAllNodes());
            hiddenNodes.remove(this.getFigure().getInputNode().getId());
            diagramScene.setHiddenNodesByID(hiddenNodes);
        } else if (figure.isBoundary()) {
            final Set<Integer> hiddenNodes = new HashSet<>(diagramScene.getHiddenNodesByID());
            hiddenNodes.remove(this.getFigure().getInputNode().getId());
            diagramScene.setHiddenNodesByID(hiddenNodes);
        } else {
            final Set<Integer> hiddenNodes = new HashSet<>(diagramScene.getHiddenNodesByID());
            hiddenNodes.add(this.getFigure().getInputNode().getId());
            diagramScene.setHiddenNodesByID(hiddenNodes);
        }
    }

    public static class RoundedBorder extends LineBorder {

        final float RADIUS = 3f;

        public RoundedBorder(Color color, int thickness) {
            super(color, thickness);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if ((this.thickness > 0) && (g instanceof Graphics2D g2d)) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color oldColor = g2d.getColor();
                g2d.setColor(this.lineColor);
                int offs = this.thickness;
                int size = offs + offs;
                Shape outer = new RoundRectangle2D.Float(x, y, width, height, RADIUS, RADIUS);
                Shape inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, RADIUS, RADIUS);
                Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
                path.append(outer, false);
                path.append(inner, false);
                g2d.fill(path);
                g2d.setColor(oldColor);
            }
        }
    }
}

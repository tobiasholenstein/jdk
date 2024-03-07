/*
 * Copyright (c) 2008, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.hotspot.igv.coordinator;

import com.sun.hotspot.igv.data.InputGraph;
import com.sun.hotspot.igv.data.Properties;
import com.sun.hotspot.igv.util.PropertiesSheet;
import com.sun.hotspot.igv.util.StringUtils;
import com.sun.hotspot.igv.view.EditorTopComponent;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.actions.OpenAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;


public class GraphNode extends AbstractNode {

    private final InputGraph graph;
    private boolean selected = false;

    /**
     * Creates a new instance of GraphNode
     */
    public GraphNode(InputGraph graph) {
        this(graph, new InstanceContent());
    }

    private GraphNode(InputGraph graph, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        this.graph = graph;
        this.setDisplayName(graph.getName());

        content.add(graph);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public String getName() {
        return graph.getName();
    }

    @Override
    public void setName(String name) {
        graph.setName(name);
        fireDisplayNameChange(null, null);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        fireDisplayNameChange(null, null);
        fireIconChange();
    }

    @Override
    public String getHtmlDisplayName() {
        String htmlDisplayName = StringUtils.escapeHTML(getDisplayName());
        if (selected) {
            htmlDisplayName = "<b>" + htmlDisplayName + "</b>";
        }
        return htmlDisplayName;
    }

    @Override
    public String getDisplayName() {
        return graph.getDisplayName();
    }

    @Override
    protected Sheet createSheet() {
        Sheet s = super.createSheet();
        Properties p = new Properties();
        p.add(graph.getProperties());
        p.setProperty("nodeCount", Integer.toString(graph.getNodes().size()));
        p.setProperty("edgeCount", Integer.toString(graph.getEdges().size()));
        PropertiesSheet.initializeSheet(p, s);
        return s;
    }

    @Override
    public Image getIcon(int i) {
        if (selected) {
            return ImageUtilities.loadImage("com/sun/hotspot/igv/coordinator/images/graph_selected.png");
        } else {
            return ImageUtilities.loadImage("com/sun/hotspot/igv/coordinator/images/graph.png");
        }
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    public Action[] getActions(boolean b) {
        return new Action[]{
                OpenAction.findObject(OpenAction.class, true)
        };
    }

    @Override
    public Action getPreferredAction() {
        // Return an Action that should be performed on double-click
        return new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    EditorTopComponent etc = new EditorTopComponent(graph);
                    etc.open();
                    etc.requestActive();
                });
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GraphNode) {
            return (graph == ((GraphNode) obj).graph);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }
}

/*
 * Copyright (c) 2008, 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.hotspot.igv.data;

import java.util.*;

/**
 * @author Thomas Wuerthinger
 */
public class InputGraph extends Properties.Entity implements FolderElement {

    private final Map<Integer, InputNode> nodes;
    private final List<InputEdge> edges;
    private final Map<String, InputBlock> blocks;
    private final ChangedEvent<InputGraph> displayNameChangedEvent = new ChangedEvent<>(this);
    private Folder parent;
    private Group parentGroup;

    public InputGraph(String name) {
        setName(name);
        nodes = new LinkedHashMap<>();
        edges = new ArrayList<>();
        blocks = new LinkedHashMap<>();
    }

    @Override
    public ChangedEvent<InputGraph> getDisplayNameChangedEvent() {
        return displayNameChangedEvent;
    }

    @Override
    public String getName() {
        return getProperties().get("name");
    }

    @Override
    public void setName(String name) {
        getProperties().setProperty("name", name);
        displayNameChangedEvent.fire();
    }

    @Override
    public String getDisplayName() {
        return getIndex() + 1 + ". " + getName();
    }

    public int getIndex() {
        Group group = getGroup();
        if (group != null) {
            return group.getGraphs().indexOf(this);
        } else {
            return -1;
        }
    }

    public Collection<InputNode> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    public Set<Integer> getNodesAsSet() {
        return Collections.unmodifiableSet(nodes.keySet());
    }

    public Collection<InputBlock> getBlocks() {
        return Collections.unmodifiableCollection(blocks.values());
    }

    public void addNode(InputNode node) {
        nodes.put(node.getId(), node);
    }

    public InputNode getNode(int id) {
        return nodes.get(id);
    }

    public InputNode removeNode(int index) {
        return nodes.remove(index);
    }

    public Collection<InputEdge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public void removeEdge(InputEdge c) {
        edges.remove(c);
    }

    public void addEdge(InputEdge c) {
        edges.add(c);
    }

    public Group getGroup() {
        return parentGroup;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph ").append(getName()).append(" ").append(getProperties().toString()).append("\n");
        for (InputNode n : nodes.values()) {
            sb.append(n.toString());
            sb.append("\n");
        }

        for (InputEdge c : edges) {
            sb.append(c.toString());
            sb.append("\n");
        }

        for (InputBlock b : getBlocks()) {
            sb.append(b.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public InputBlock addBlock(String name) {
        final InputBlock b = new InputBlock(this, name);
        blocks.put(b.getName(), b);
        return b;
    }

    @Override
    public Folder getParent() {
        return parent;
    }

    @Override
    public void setParent(Folder parent) {
        this.parent = parent;
        if (parent instanceof Group) {
            assert this.parentGroup == null;
            this.parentGroup = (Group) parent;
            assert displayNameChangedEvent != null;
            assert this.parentGroup.getDisplayNameChangedEvent() != null;
            this.parentGroup.getDisplayNameChangedEvent().addListener(l -> displayNameChangedEvent.fire());
        }
    }
}

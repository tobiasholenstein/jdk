package com.sun.hotspot.igv.data;

import java.util.*;

public class Group extends Properties.Entity implements ChangedEventProvider<Group>, Folder, FolderElement {

    private final List<InputGraph> graphs;
    private final transient ChangedEvent<Group> changedEvent;
    private final ChangedEvent<Group> displayNameChangedEvent = new ChangedEvent<>(this);
    private InputMethod method;
    private Folder parent;

    public Group(Folder parent) {
        graphs = new ArrayList<>();
        changedEvent = new ChangedEvent<>(this);
        this.parent = parent;

        // Ensure that name is never null
        getProperties().setProperty("name", "");
    }

    public InputMethod getMethod() {
        return method;
    }

    public void setMethod(InputMethod method) {
        this.method = method;
    }

    @Override
    public ChangedEvent<Group> getChangedEvent() {
        return changedEvent;
    }

    @Override
    public void addElement(FolderElement element) {
        assert element instanceof InputGraph;
        graphs.add((InputGraph) element);
        element.setParent(this);
        getChangedEvent().fire();
    }

    @Override
    public List<FolderElement> getElements() {
        return Collections.unmodifiableList(graphs);
    }

    public List<InputGraph> getGraphs() {
        return Collections.unmodifiableList(graphs);
    }

    public Set<Integer> getAllNodes() {
        Set<Integer> result = new HashSet<>();
        for (InputGraph g : graphs) {
            result.addAll(g.getNodesAsSet());
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group ").append(getProperties()).append("\n");
        for (FolderElement g : getElements()) {
            sb.append(g.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public ChangedEvent<Group> getDisplayNameChangedEvent() {
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
        String displayName = (getParent() == null ? "" : getIndex() + 1 + " - ") + getName();
        if (getProperties().get("osr") != null) {
            displayName += " [OSR]";
        }
        return displayName;
    }

    public int getIndex() {
        Folder parent = getParent();
        if (parent != null) {
            return parent.getElements().indexOf(this);
        } else {
            return -1;
        }
    }

    public String getType() {
        return getProperties().get("type");
    }

    @Override
    public Folder getParent() {
        return parent;
    }

    @Override
    public void setParent(Folder parent) {
        this.parent = parent;
    }
}

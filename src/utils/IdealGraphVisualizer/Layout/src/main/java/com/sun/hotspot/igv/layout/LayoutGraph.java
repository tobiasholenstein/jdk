package com.sun.hotspot.igv.layout;

import java.util.*;


public class LayoutGraph {

    private final Set<? extends Link> links;
    private final SortedSet<Vertex> vertices;
    private final HashMap<Vertex, Set<Port>> inputPorts;
    private final HashMap<Vertex, Set<Port>> outputPorts;
    private final HashMap<Port, Set<Link>> portLinks;

    public LayoutGraph(Set<? extends Link> links, Set<? extends Vertex> additionalVertices) {
        this.links = links;
        assert verify();

        vertices = new TreeSet<>();
        portLinks = new HashMap<>(links.size());
        inputPorts = new HashMap<>(links.size());
        outputPorts = new HashMap<>(links.size());

        for (Link l : links) {
            if (l.getFrom() == null || l.getTo() == null) {
                continue;
            }
            Port p = l.getFrom();
            Port p2 = l.getTo();
            Vertex v1 = l.getFrom().getVertex();
            Vertex v2 = l.getTo().getVertex();

            if (!vertices.contains(v1)) {

                outputPorts.put(v1, new HashSet<>(1));
                inputPorts.put(v1, new HashSet<>(3));
                vertices.add(v1);
                assert vertices.contains(v1);
            }

            if (!vertices.contains(v2)) {
                vertices.add(v2);
                assert vertices.contains(v2);
                outputPorts.put(v2, new HashSet<>(1));
                inputPorts.put(v2, new HashSet<>(3));
            }

            if (!portLinks.containsKey(p)) {
                HashSet<Link> hashSet = new HashSet<>(3);
                portLinks.put(p, hashSet);
            }

            if (!portLinks.containsKey(p2)) {
                portLinks.put(p2, new HashSet<>(3));
            }

            outputPorts.get(v1).add(p);
            inputPorts.get(v2).add(p2);

            portLinks.get(p).add(l);
            portLinks.get(p2).add(l);
        }

        for (Vertex v : additionalVertices) {
            if (!vertices.contains(v)) {
                outputPorts.put(v, new HashSet<>(1));
                inputPorts.put(v, new HashSet<>(3));
                vertices.add(v);
            }
        }
    }

    public Set<Port> getInputPorts(Vertex v) {
        return this.inputPorts.get(v);
    }

    public Set<Port> getOutputPorts(Vertex v) {
        return this.outputPorts.get(v);
    }

    public Set<Link> getPortLinks(Port p) {
        return portLinks.get(p);
    }

    public Set<? extends Link> getLinks() {
        return links;
    }

    public boolean verify() {
        return true;
    }

    public SortedSet<Vertex> getVertices() {
        return vertices;
    }

    public Set<Link> getInputLinks(Vertex vertex) {
        Set<Link> inputLinks = new HashSet<>();
        for (Port inputPort : getInputPorts(vertex)) {
            inputLinks.addAll(getPortLinks(inputPort));
        }
        return inputLinks;
    }

    public Set<Link> getOutputLinks(Vertex vertex) {
        Set<Link> outputLinks = new HashSet<>();
        for (Port outputPort : getOutputPorts(vertex)) {
            outputLinks.addAll(getPortLinks(outputPort));
        }
        return outputLinks;
    }
}

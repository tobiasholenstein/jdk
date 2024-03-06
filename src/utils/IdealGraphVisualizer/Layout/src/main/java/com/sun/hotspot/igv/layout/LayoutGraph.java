package com.sun.hotspot.igv.layout;

import java.util.*;


public class LayoutGraph {

    private final Set<? extends Link> links;
    private final Set<? extends Vertex> vertices;

    public LayoutGraph(Set<? extends Link> links, Set<? extends Vertex> vertices) {
        this.links = links;
        this.vertices = vertices;
    }

    public Set<? extends Link> getLinks() {
        return links;
    }

    public Set<? extends Vertex> getVertices() {
        return vertices;
    }

    public Set<Link> getInputLinks(Vertex vertex) {
        return vertex.getInputLinks();
    }

    public Set<Link> getOutputLinks(Vertex vertex) {
        return vertex.getOutputLinks();
    }
}

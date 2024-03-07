package com.sun.hotspot.igv.layout;

import java.util.Set;

public record LayoutGraph(Set<? extends Link> links, Set<? extends Vertex> vertices) {}

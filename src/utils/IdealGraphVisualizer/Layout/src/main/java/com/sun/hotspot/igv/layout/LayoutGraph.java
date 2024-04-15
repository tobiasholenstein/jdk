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
package com.sun.hotspot.igv.layout;

import java.util.*;

/**
 *
 * @author Thomas Wuerthinger
 */
public class LayoutGraph {

    private final Set<? extends Link> links;
    private final SortedSet<Vertex> vertices;
    private final HashMap<Vertex, Set<Port>> inputPorts;
    private final HashMap<Vertex, Set<Port>> outputPorts;
    private final HashMap<Port, Set<Link>> portLinks;

    public LayoutGraph(Set<? extends Link> links) {
        this(links, new HashSet<>());
    }

    public LayoutGraph(Set<? extends Link> links, Set<? extends Vertex> additionalVertices) {
        this.links = links;

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
            Vertex v1 = p.getVertex();
            Vertex v2 = p2.getVertex();

            if (!vertices.contains(v1)) {

                outputPorts.put(v1, new HashSet<>());
                inputPorts.put(v1, new HashSet<>());
                vertices.add(v1);
                assert vertices.contains(v1);
            }

            if (!vertices.contains(v2)) {
                vertices.add(v2);
                assert vertices.contains(v2);
                outputPorts.put(v2, new HashSet<>());
                inputPorts.put(v2, new HashSet<>());
            }

            if (!portLinks.containsKey(p)) {
                HashSet<Link> hashSet = new HashSet<>();
                portLinks.put(p, hashSet);
            }

            if (!portLinks.containsKey(p2)) {
                portLinks.put(p2, new HashSet<>());
            }

            outputPorts.get(v1).add(p);
            inputPorts.get(v2).add(p2);

            portLinks.get(p).add(l);
            portLinks.get(p2).add(l);
        }

        for (Vertex v : additionalVertices) {
            if (!vertices.contains(v)) {
                outputPorts.put(v, new HashSet<>());
                inputPorts.put(v, new HashSet<>());
                vertices.add(v);
            }
        }
    }

    public Set<? extends Link> getLinks() {
        return links;
    }

    public SortedSet<Vertex> getVertices() {
        return vertices;
    }

    private void markNotRoot(Set<Vertex> notRootSet, Vertex v, Vertex startingVertex) {
        if (notRootSet.contains(v)) {
            return;
        }
        if (v != startingVertex) {
            notRootSet.add(v);
        }
        Set<Port> outPorts = this.outputPorts.get(v);
        for (Port p : outPorts) {
            Set<Link> portLinks = this.portLinks.get(p);
            for (Link l : portLinks) {
                Port other = l.getTo();
                Vertex otherVertex = other.getVertex();
                if (otherVertex != startingVertex) {
                    markNotRoot(notRootSet, otherVertex, startingVertex);
                }
            }
        }
    }

    public Set<Vertex> findRootVertices() {
        Set<Vertex> notRootSet = new HashSet<>();
        Set<Vertex> tmpVertices = getVertices();
        for (Vertex v : tmpVertices) {
            if (!notRootSet.contains(v)) {
                if (this.inputPorts.get(v).isEmpty()) {
                    markNotRoot(notRootSet, v, v);
                }
            }
        }

        for (Vertex v : tmpVertices) {
            if (!notRootSet.contains(v)) {
                markNotRoot(notRootSet, v, v);
            }
        }

        Set<Vertex> result = new HashSet<>();
        for (Vertex v : tmpVertices) {
            if (!notRootSet.contains(v)) {
                result.add(v);
            }
        }
        return result;
    }

    public Set<Link> getInputLinks(Vertex vertex) {
        Set<Link> inputLinks = new HashSet<>();
        for (Port inputPort : this.inputPorts.get(vertex)) {
            inputLinks.addAll(portLinks.get(inputPort));
        }
        return inputLinks;
    }

    public Set<Link> getOutputLinks(Vertex vertex) {
        Set<Link> outputLinks = new HashSet<>();
        for (Port outputPort : outputPorts.get(vertex)) {
            outputLinks.addAll(portLinks.get(outputPort));
        }
        return outputLinks;
    }
}

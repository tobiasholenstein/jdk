/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.hotspot.igv.hierarchicallayout;

import com.sun.hotspot.igv.layout.Link;
import com.sun.hotspot.igv.layout.Vertex;
import java.awt.Dimension;
import java.awt.Point;
import java.util.*;

public class LayoutNode {

    public static final int DUMMY_HEIGHT = 1;
    public static final int DUMMY_WIDTH = 1;
    public int optimal_x;

    public int x;
    public int y;
    public int width;
    public int height;
    public int layer = -1;
    public int leftXOffset;
    public int topYOffset;
    public int rightXOffset;
    public int bottomYOffset;

    // TODO make final
    public Vertex vertex; // Only used for non-dummy nodes, otherwise null

    public List<LayoutEdge> preds = new ArrayList<>();
    public List<LayoutEdge> succs = new ArrayList<>();
    public HashMap<Integer, Integer> outOffsets = new HashMap<>();
    public HashMap<Integer, Integer> inOffsets = new HashMap<>();
    public final HashMap<Link, List<Point>> reversedLinkStartPoints = new HashMap<>();
    public final HashMap<Link, List<Point>> reversedLinkEndPoints = new HashMap<>();
    public int pos = -1; // Position within layer

    public int crossingNumber;
    public float weightedPosition = 0;
    public boolean reverseLeft = false;

    public LayoutNode(Vertex v) {
        vertex = v;
        if (v == null) {
            height = DUMMY_HEIGHT;
            width = DUMMY_WIDTH;
        } else {
            Dimension size = v.getSize();
            height = size.height;
            width = size.width;
        }
    }

    public LayoutNode() {
        this(null);
    }

    public int getDegree() {
        return preds.size() + succs.size();
    }

    public float averagePosition(boolean weighted) {
        float totalWeightedPosition = 0;
        float totalWeight = 0;

        for (LayoutEdge predEdge : preds) {
            LayoutNode predNode = predEdge.from;
            int weight = weighted ? predNode.getDegree() : 1;
            totalWeightedPosition += weight * predEdge.getStartPoint();
            totalWeight += weight;
        }
        for (LayoutEdge succEdge : succs) {
            LayoutNode succNode = succEdge.to;
            int weight = weighted ? succNode.getDegree() : 1;
            totalWeightedPosition += weight * succEdge.getEndPoint();
            totalWeight += weight;
        }

        // Calculate the (weighted) average position for the node based on neighbor positions and weights (degree)
        return totalWeight > 0 ? totalWeightedPosition / totalWeight : 0;
    }

    public int getLeftSide() {
        return x + leftXOffset;
    }

    public int getLeftBorder() {
        return x;
    }

    public int getWholeWidth() {
        return leftXOffset + width + rightXOffset;
    }

    public int getWholeHeight() {
        return topYOffset + height + bottomYOffset;
    }

    public int getRightSide() {
        return x + leftXOffset + width;
    }

    public int getRightBorder() {
        return x + leftXOffset + width + rightXOffset;
    }

    public int getCenterX() {
        return getLeftSide() + (width / 2);
    }

    public int getTop() {
        return y + topYOffset;
    }

    public int getBottom() {
        return y + topYOffset + height;
    }

    public boolean isDummy() {
        return vertex == null;
    }
    @Override
    public String toString() {
        if (vertex != null) {
            return vertex.toString();
        } else {
            return "dummy";
        }
    }
}

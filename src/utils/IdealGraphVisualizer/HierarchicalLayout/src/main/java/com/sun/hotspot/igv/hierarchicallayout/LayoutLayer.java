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

import java.util.ArrayList;
import java.util.Collection;

public class LayoutLayer extends ArrayList<LayoutNode> {

    private int height = 0;
    private int y = 0;

    @Override
    public boolean addAll(Collection<? extends LayoutNode> c) {
        c.forEach(this::updateHeight);
        return super.addAll(c);
    }

    private void updateHeight(LayoutNode n) {
        height = Math.max(height, n.getOuterHeight());
    }

    @Override
    public boolean add(LayoutNode n) {
        updateHeight(n);
        return super.add(n);
    }

    public void swap(int i, int j) {
        LayoutNode n1 = get(i);
        LayoutNode n2 = get(j);
        set(j, n1);
        set(i, n2);
    }

    public void setTop(int top) {
        y = top;
    }

    public void shiftTop(int shift) {
        y += shift;
    }

    public int getTop() {
        return y;
    }

    public int getCenter() {
        return y + height / 2;
    }

    public int getBottom() {
        return y + height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

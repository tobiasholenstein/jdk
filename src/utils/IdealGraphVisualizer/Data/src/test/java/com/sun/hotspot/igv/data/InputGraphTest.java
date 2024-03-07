/*
 * Copyright (c) 2008, 2021, Oracle and/or its affiliates. All rights reserved.
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

import org.junit.*;

/**
 * @author Thomas Wuerthinger
 */
public class InputGraphTest {

    private static final InputNode N1 = new InputNode(1);
    private static final InputNode N2 = new InputNode(2);
    private static final InputNode N3 = new InputNode(3);
    private static final InputNode N4 = new InputNode(4);
    private static final InputNode N5 = new InputNode(5);
    private static final InputEdge E12 = new InputEdge((char) 0, (char) 0, 1, 2, "", "");
    private static final InputEdge E13 = new InputEdge((char) 0, (char) 0, 1, 3, "", "");
    private static final InputEdge E24 = new InputEdge((char) 0, (char) 0, 2, 4, "", "");
    private static final InputEdge E34 = new InputEdge((char) 0, (char) 0, 3, 4, "", "");
    private static final InputEdge E54 = new InputEdge((char) 0, (char) 0, 5, 4, "", "");
    /**
     * 1
     * / \
     * 2   3
     * \  |  5
     * \ | /
     * 4
     */
    private static InputGraph referenceGraph;
    private static InputGraph emptyGraph;

    public InputGraphTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Group group = new Group(null);

        emptyGraph = new InputGraph("emptyGraph");
        group.addElement(emptyGraph);

        referenceGraph = new InputGraph("referenceGraph");
        group.addElement(referenceGraph);
        referenceGraph.addNode(N1);
        referenceGraph.addNode(N2);
        referenceGraph.addNode(N3);
        referenceGraph.addNode(N4);
        referenceGraph.addNode(N5);

        referenceGraph.addEdge(E12);
        referenceGraph.addEdge(E13);
        referenceGraph.addEdge(E24);
        referenceGraph.addEdge(E34);
        referenceGraph.addEdge(E54);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of equals method, of class InputGraph.
     */
    @Test
    public void testEquals() {

        Group parentA = new Group(null);
        InputGraph a = new InputGraph("graph");
        parentA.addElement(a);

        Group parentB = new Group(null);
        InputGraph b = new InputGraph("graph");
        parentB.addElement(b);

        InputGraph c = new InputGraph("graph");
        parentB.addElement(c);

        Util.assertGraphEquals(a, b);
        Util.assertGraphEquals(b, c);

        a.addNode(new InputNode(1));
        Util.assertGraphNotEquals(a, b);

        b.addNode(new InputNode(1));
        Util.assertGraphEquals(a, b);
    }


    /**
     * Test of getNext method, of class InputGraph.
     */
    @Test
    public void testGetNextPrev() {
        final Group group = new Group(null);

        final InputGraph a = new InputGraph("a");

        final InputGraph b = new InputGraph("b");

        final InputGraph c = new InputGraph("c");
        group.addElement(a);
        group.addElement(b);
        group.addElement(c);
    }
}

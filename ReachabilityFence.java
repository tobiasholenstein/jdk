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
 */


import java.lang.ref.Reference;
import java.lang.ref.Cleaner;


public class ReachabilityFence {
    static class A {
        public B obj;
        public A(B obj) {
            this.obj = obj;
        }
    }

    static class B {
        public static int[] arr = new int[1024];
        public final int id;
        public B(int id) {
            this.id = id;
        }
    }

    static void test0(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int i = 0; i < limit; i++) {
            B bar = foo.obj;
            assert bar != null;
            int id = bar.id;
            int idx = i % 1024;
            arr[idx] = id * arr[idx];
            Reference.reachabilityFence(bar);
        }
        assert arr1[0] == arr0;
    }

    static void test1(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int j = 0; j < limit; j += arr.length) {
            for (int i = 0; i < arr.length; i++) {
                B bar = foo.obj;
                assert bar != null;
                int id = bar.id;
                int idx = i;
                arr[idx] = id * arr[idx];
                Reference.reachabilityFence(bar);
            }
        }

        assert arr1[0] == arr0;
    }

    static void test2(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int j = 0; j < limit; j += arr.length) {
            for (int i = 0; i < arr.length; i++) {
                B bar = foo.obj;
                assert bar != null;
                int id = bar.id;
                int idx = i;
                arr[idx] = id * arr[idx];
                Reference.reachabilityFence(bar);
            }
        }

        assert arr1[0] == arr0;
    }

    static void test3(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int j = 0; j < limit; j += arr.length) {
            for (int i = 0; i < arr.length; i++) {
                B bar = foo.obj;
                assert bar != null;
                bar.getClass(); // NPE
                Reference.reachabilityFence(bar);
            }
        }

        assert arr1[0] == arr0;
    }

    static void test(int nr) {
        final A foo = new A(new B(1));
        Cleaner.create().register(foo.obj, () -> {
            // called when foo.obj is no longer reachable and garbage colllected
            System.out.println("!!! GONE !!!");
            B.arr[0] = 1_000_000 + foo.obj.id;
        });

        Thread threadGC = new Thread() {
            // requests garbage collection every 50 milliseconds.
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(10);
                        System.gc();
                    }
                } catch (Throwable e) {
                    throw new InternalError(e);
                }
            }
        };
        threadGC.setDaemon(true);

        Thread threadUpdate = new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                    int newId = foo.obj.id + 1;
                    foo.obj = null; // newB;
                    System.out.println("!!! CLEAN !!!");
                } catch (Throwable e) {
                    throw new InternalError(e);
                }
            }
        };
        threadUpdate.setDaemon(true);

        threadGC.start();
        threadUpdate.start();


        int limit = Integer.MAX_VALUE - 1024;
        try {
            switch (nr) {
                case 0: test0(foo, foo.obj.arr, foo.obj.arr, limit); break;
                case 1: test1(foo, foo.obj.arr, foo.obj.arr, limit); break;
                case 2: test2(foo, foo.obj.arr, foo.obj.arr, limit); break;
                case 3: test3(foo, foo.obj.arr, foo.obj.arr, limit); break;
                default: throw new Error("Invalid test case number: " + nr);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred in test" + nr + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    /Users/tholenst/dev/jdk/build/macosx-aarch64-debug/jdk/bin/java -XX:CompileCommand=compileonly,*ReachabilityFence::* -XX:PrintIdealGraphLevel=3 ReachabilityFence.java
    */
    public static void main(String[] args) {
        test(0);
        test(1);
        test(2);
        test(3);
    }
}

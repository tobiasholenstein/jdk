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


/*
/Users/tholenst/dev/jdk/build/macosx-aarch64-debug/jdk/bin/java -XX:CompileCommand=compileonly,*ReachabilityFence::test1  -ea  ReachabilityFence.java
*/
public class ReachabilityFence {
    static class A {
        public B obj;
        public A(B obj) {
            this.obj = obj;
        }
    }

    static class B {
        public final int id;
        static int[] arr = new int[1024];
        public B(int id) {
            this.id = id;
        }
    }

    static void test(A foo, int[] arr, int limit) {
        int val = B.arr[0];
        test1(foo, arr, limit);
        assert B.arr[0] == val : "Invariant violated";

    }


    static void test1(A foo, int[] arr, int limit) {
        for (long j = 0; j < limit; j += 1) {
            for (int i = 0; i < arr.length ; i++) {
                B bar = foo.obj;
                // it would be bad in perforance if B bar load is not move out of the loop anymore after the fix
                if (i > 0) {
                    arr[i] = bar.id * arr[i];

                }
                Reference.reachabilityFence(bar);
            }
        }
    }


    public static void main(String[] args) {
        final A foo = new A(new B(1));

        // calls this lambda if GC collected he object
        Cleaner.create().register(foo.obj, () -> {
            System.out.println("!!! GONE !!!");
            B.arr[0] = 42;
        });

        for (int j = 0; j < foo.obj.arr.length; j += 1) {
            foo.obj.arr[j] = 1;
        }

        for (int i = 0; i < 20_000; i++) {
            test(foo, foo.obj.arr, 100);
        }

        Thread threadGC = new Thread() {
            public void run() {
                try {
                    while (true) {
                        // triggers GC every 50ms
                        Thread.sleep(50);
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
                    Thread.sleep(500);
                    foo.obj = null; // triggers the GC to remove the object
                    System.out.println("!!! CLEAN !!!");
                } catch (Throwable e) {
                    throw new InternalError(e);
                }
            }
        };
        threadUpdate.setDaemon(true);

        threadGC.start();
        threadUpdate.start();

        test(foo, foo.obj.arr, 10_000_000);
    }
}

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

import java.lang.ref.Cleaner;
import java.lang.ref.Reference;
import java.lang.reflect.*;
import jdk.internal.misc.Unsafe;


/*
/Users/tholenst/dev/jdk/build/macosx-aarch64-debug/jdk/bin/java
--add-opens java.base/jdk.internal.misc=ALL-UNNAMED --add-exports java.base/jdk.internal.misc=ALL-UNNAMED -XX:CompileCommand=compileonly,*ReachabilityFence::*   -Xbatch -ea  ReachabilityFence.java
*/

public class ReachabilityFence {

    static class MyBuffer {

        private static Unsafe UNSAFE;

        static {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE = (Unsafe) field.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static int current = 0;
        private static long payload[] = new long[10];
        private boolean isFreed = false;  // Track if memory is freed

        private final int id;

        public MyBuffer(long size) {
            id = current++ % payload.length;  // Wrap around when current exceeds 10
            payload[id] = UNSAFE.allocateMemory(size);

            // Register a cleaner to free the memory when the buffer is garbage collected
            int lid = id;
            Cleaner.create().register(this, () -> free(lid));
        }

        private void free(int id) {
            isFreed = true;
            UNSAFE.freeMemory(payload[id]);
            payload[id] = 0;
        }

        public void put(int offset, byte b) {
            if (isFreed) {
                throw new IllegalStateException("Memory has already been freed.");
            }
            UNSAFE.putByte(payload[id] + offset, b);
        }

        public byte get(int offset) {
            if (isFreed) {
                throw new IllegalStateException("Memory has already been freed.");
            }
            return UNSAFE.getByte(payload[id] + offset);
        }
    }

    static MyBuffer[] buffers = new MyBuffer[10];  // Multiple buffers to increase memory pressure

    static void initBuffers() {
        for (int i = 0; i < buffers.length; ++i) {
            buffers[i] = new MyBuffer(100);
            for (int j = 0; j < 100; ++j) {
                buffers[i].put(j, (byte) 42);
            }
        }
    }

    static void test(int limit, int time) {
        for (long j = 0; j < limit; j++) {
            for (MyBuffer myBuffer : buffers) {
                if (myBuffer == null) continue;
                for (int i = 0; i < 100; i++) {
                    byte b = myBuffer.get(i);
                    if (b != 42) {
                        throw new RuntimeException(
                            "Unexpected value = " + b +
                            ". Buffer was garbage collected before reachabilityFence was reached!"
                        );
                    }
                }
                // Keep the buffer live while we read from it
                Reference.reachabilityFence(myBuffer);

                try {
                    Thread.sleep(time);  // Increase the chance for GC to happen during the sleep
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        initBuffers();  // Initialize multiple buffers for more memory usage

        Thread gcThread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < buffers.length; ++i) {
                    buffers[i] = null;
                }
                System.gc();  // Trigger GC aggressively

                try {
                    Thread.sleep(1);  // Minimal sleep to continuously trigger GC
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Recreate all buffers quickly after freeing them
                initBuffers();
            }
        });
        gcThread.start();

        // Add multiple threads to increase the load on memory and garbage collection
        Thread[] testThreads = new Thread[10];
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = new Thread(() -> test(10_000_000, 1));  // Run with multiple threads
            testThreads[i].start();
        }

        for (Thread t : testThreads) {
            t.join();  // Wait for all threads to finish
        }
    }
}

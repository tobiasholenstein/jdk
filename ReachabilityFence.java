import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.lang.ref.Reference;
import java.lang.ref.Cleaner;


// /Users/tholenst/dev/jdk/build/macosx-aarch64-debug/jdk/bin/java -XX:CompileCommand=compileonly,*ReachabilityFence::*  -Xmx512M -Xms512M ReachabilityFence.java

public class ReachabilityFence {
    // Nested class A containing an instance of class B
    static class A {
        public B obj;
        public A(B obj) {
            this.obj = obj;
        }
    }

    // Nested class B containing an integer ID and a static integer array
    static class B {
        public final int id;

        static int[] arr = new int[1024];

        public B(int id) {
            this.id = id;
        }
    }


    static void test(A foo, int[] arr, int limit) {
        test1(foo, arr, arr, limit);
        test2(foo, arr, arr, limit);
    }


    // Manipulation with reachability fence after each operation
    static void test1(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int j = 0; j < limit; j += arr.length) {
            for (int i = 0; i < arr.length; i++) {
                B bar = foo.obj;
                int id = bar.id;
                int idx = i;
                arr[idx] = id * arr[idx] ;
                Reference.reachabilityFence(bar);
            }
        }

        if (arr1[0] != arr0) {
            throw new AssertionError(arr[0] + " != " + arr0);
        }
    }


    // Test case to trigger a NullPointerException (NPE) if 'bar' is prematurely collected
    static void test2(A foo, int[] arr, int[] arr1, int limit) {
        int arr0 = arr[0];

        for (int j = 0; j < limit; j += arr.length) {
            for (int i = 0; i < arr.length; i++) {
                B bar = foo.obj;
                bar.getClass(); // NPE
                Reference.reachabilityFence(bar);
            }
        }

        if (arr1[0] != arr0) {
            throw new AssertionError(arr[0] + " != " + arr0);
        }
    }

    public static void main(String[] args) {
        
        // Thread to continuously trigger garbage collection
        Thread threadGC = new Thread() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(50);
                        System.gc();
                    }
                } catch (Throwable e) {
                    throw new InternalError(e);
                }
            }
        };
        threadGC.setDaemon(true);
        threadGC.start();


        // Final test call with max integer limit
        for (int i = 0; i < 20_000; i++) {
            System.out.println("Test " + i);

            // Setup a test environment with object 'A' containing 'B' and register a cleaner
            final A foo = new A(new B(1));



            // Thread to simulate object reference changes during execution
            Thread threadUpdate = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        int newId = foo.obj.id + 1;
                        foo.obj = null; // newB; Simulate losing the reference
                        System.out.println("!!! CLEAN !!!");
                    } catch (Throwable e) {
                        throw new InternalError(e);
                    }
                }
            };
            threadUpdate.setDaemon(true);
            threadUpdate.start();

            test(foo, foo.obj.arr, foo.obj.arr.length);
        }

        System.out.println("Test completed.");
    }
}

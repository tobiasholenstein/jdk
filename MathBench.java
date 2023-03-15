import java.util.Random;
import java.util.concurrent.TimeUnit;

/*

/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation MathBench.java

/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:CompileCommand=compileonly,MathBench::* -XX:CompileCommand=compileonly,java.lang.Math::* -XX:CompileCommand=compileonly,java.lang.StrictMath::* -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation -XX:+PrintInlining -XX:CompileCommand=quiet MathBench.java | grep -v java.lang.invoke.MethodHandle::


-XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:PrintAssemblyOptions=intel

*/


public class MathBench {

    public static void main(String[] args) throws Exception {
        final Random random = new Random();
        double a = random.nextDouble();
        double b = random.nextDouble();


        sinBench(a);
        cosBench(a);
        tanBench(a);

        acosBench(a);
        asinBench(a);
        atanBench(a);

        expBench(a);
        logBench(a);
        log10Bench(a);

        // sqrt
        cbrtBench(a);
        IEEEremainderBench(a, b);
        // ceil
        // floor
        // rint
        atan2Bench(a, b);
        powBench(a, b);

        sinhBench(a);
        coshBench(a);
        tanhBench(a);

        hypotBench(a, b);
        expm1Bench(a);
        log1pBench(a);
    }

    public static void IEEEremainderBench(double a, double b) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.IEEEremainder(a, b);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.IEEEremainder)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.IEEEremainder(a, b);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.IEEEremainder)");

        assert Math.IEEEremainder(a, b) == StrictMath.IEEEremainder(a, b);
    }

    public static void hypotBench(double a, double b) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.hypot(a, b);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.hypot)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.hypot(a, b);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.hypot)");

        assert Math.hypot(a, b) == StrictMath.hypot(a, b);
    }

    public static void powBench(double a, double b) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.pow(a, b);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.pow)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.pow(a, b);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.pow)");

        assert Math.pow(a, b) == StrictMath.pow(a, b);
    }

    public static void log1pBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.log1p(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.log1p)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.log1p(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.log1p)");

        assert Math.log1p(a) == StrictMath.log1p(a);
    }

    public static void expm1Bench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.expm1(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.expm1)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.expm1(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.expm1)");

        assert Math.expm1(a) == StrictMath.expm1(a);
    }

    public static void tanhBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.tanh(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.tanh)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.tanh(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.tanh)");

        assert Math.tanh(a) == StrictMath.tanh(a);
    }

    public static void coshBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.cosh(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.cosh)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.cosh(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.cosh)");

        assert Math.cosh(a) == StrictMath.cosh(a);
    }

    public static void sinhBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.sinh(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.sinh)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.sinh(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.sinh)");

        assert Math.sinh(a) == StrictMath.sinh(a);
    }

    public static void atanBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.atan(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.atan)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.atan(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.atan)");

        assert Math.atan(a) == StrictMath.atan(a);
    }

    public static void asinBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.asin(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.asin)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.asin(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.asin)");

        assert Math.asin(a) == StrictMath.asin(a);
    }

    public static void acosBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.acos(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.acos)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.acos(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.acos)");

        assert Math.acos(a) == StrictMath.acos(a);
    }

    public static void atan2Bench(double a, double b) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.atan2(a, b);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.atan2)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.atan2(a, b);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.atan2)");

        assert Math.atan2(a, b) == StrictMath.atan2(a, b);
    }

    public static void tanBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.tan(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.tan)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.tan(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.tan)");

        assert Math.tan(a) == StrictMath.tan(a);
    }

    public static void cosBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.cos(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.cos)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.cos(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.cos)");

        assert Math.cos(a) == StrictMath.cos(a);
    }

    public static void sinBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.sin(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.sin)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.sin(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.sin)");

        assert Math.sin(a) == StrictMath.sin(a);
    }

    public static void cbrtBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.cbrt(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.cbrt)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.cbrt(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.cbrt)");

        assert Math.cbrt(a) == StrictMath.cbrt(a);
    }

    public static void expBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.exp(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.exp)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.exp(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.exp)");

        assert Math.exp(a) == StrictMath.exp(a);
    }

    public static void log10Bench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.log10(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.log10)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.log10(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.log10)");

        assert Math.log10(a) == StrictMath.log10(a);
    }

    public static void logBench(double a) {
        final long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            Math.log(a);
        }
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.log)");


        final long start2 = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.log(a);
        }
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath.log)");

        assert Math.log(a) == StrictMath.log(a);
    }

}


/*
lscpu: Intel(R) Xeon(R) Gold 6354 CPU @ 3.00GHz

95 ms (Math.sin)
3 ms (StrictMath.sin)

94 ms (Math.cos)
3 ms (StrictMath.cos)

141 ms (Math.tan)
62 ms (StrictMath.tan)
50 ms (Math.acos)
48 ms (StrictMath.acos)
43 ms (Math.asin)
42 ms (StrictMath.asin)
50 ms (Math.atan)
48 ms (StrictMath.atan)
52 ms (Math.exp)
38 ms (StrictMath.exp)
70 ms (Math.log)
83 ms (StrictMath.log)
78 ms (Math.log10)
84 ms (StrictMath.log10)
4 ms (Math.cbrt)
2 ms (StrictMath.cbrt)
754 ms (Math.IEEEremainder)
758 ms (StrictMath.IEEEremainder)
124 ms (Math.atan2)
119 ms (StrictMath.atan2)
143 ms (Math.pow)
498 ms (StrictMath.pow)
58 ms (Math.sinh)
55 ms (StrictMath.sinh)
57 ms (Math.cosh)
55 ms (StrictMath.cosh)
57 ms (Math.tanh)
56 ms (StrictMath.tanh)
52 ms (Math.hypot)
49 ms (StrictMath.hypot)
54 ms (Math.expm1)
54 ms (StrictMath.expm1)
56 ms (Math.log1p)
54 ms (StrictMath.log1p)

lscpu Neoverse-N1 aarch64 (top not empty):
85 ms (Math.sin)
102 ms (StrictMath.sin)
87 ms (Math.cos)
122 ms (StrictMath.cos)
239 ms (Math.tan)
289 ms (StrictMath.tan)
119 ms (Math.acos)
116 ms (StrictMath.acos)
133 ms (Math.asin)
129 ms (StrictMath.asin)
141 ms (Math.atan)
137 ms (StrictMath.atan)
127 ms (Math.exp)
147 ms (StrictMath.exp)
116 ms (Math.log)
125 ms (StrictMath.log)
165 ms (Math.log10)
130 ms (StrictMath.log10)
5 ms (Math.cbrt)
5 ms (StrictMath.cbrt)
573 ms (Math.IEEEremainder)
568 ms (StrictMath.IEEEremainder)
191 ms (Math.atan2)
185 ms (StrictMath.atan2)
616 ms (Math.pow)
590 ms (StrictMath.pow)
153 ms (Math.sinh)
148 ms (StrictMath.sinh)
149 ms (Math.cosh)
147 ms (StrictMath.cosh)
224 ms (Math.tanh)
221 ms (StrictMath.tanh)
63 ms (Math.hypot)
59 ms (StrictMath.hypot)
151 ms (Math.expm1)
151 ms (StrictMath.expm1)
160 ms (Math.log1p)
155 ms (StrictMath.log1p)

Mac M1 Max:
25 ms (Math.sin)
362 ms (StrictMath.sin)
29 ms (Math.cos)
389 ms (StrictMath.cos)
593 ms (Math.tan)
438 ms (StrictMath.tan)
48 ms (Math.acos)
36 ms (StrictMath.acos)
46 ms (Math.asin)
42 ms (StrictMath.asin)
51 ms (Math.atan)
42 ms (StrictMath.atan)

684 ms (Math.exp)
49 ms (StrictMath.exp)

503 ms (Math.log)
48 ms (StrictMath.log)

649 ms (Math.log10)
59 ms (StrictMath.log10)

10 ms (Math.cbrt)
2 ms (StrictMath.cbrt)

294 ms (Math.IEEEremainder)
293 ms (StrictMath.IEEEremainder)
74 ms (Math.atan2)
67 ms (StrictMath.atan2)

1319 ms (Math.pow)
248 ms (StrictMath.pow)

65 ms (Math.sinh)
46 ms (StrictMath.sinh)
47 ms (Math.cosh)
44 ms (StrictMath.cosh)
81 ms (Math.tanh)
73 ms (StrictMath.tanh)
39 ms (Math.hypot)
34 ms (StrictMath.hypot)
67 ms (Math.expm1)
69 ms (StrictMath.expm1)
59 ms (Math.log1p)
53 ms (StrictMath.log1p)

*/

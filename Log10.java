import java.util.Random;
import java.util.concurrent.TimeUnit;

/*

/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:CompileCommand=compileonly,Main::compute -XX:CompileCommand=compileonly,Main::computeFdLibm  -XX:CompileCommand=compileonly,Main::computeLog10 Log10.java

*/

public class Main {

    public static void main(String[] args) throws Exception {
        final Random random = new Random();
        double value = random.nextDouble();


        final long start = System.nanoTime();
        compute(value);
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.log10)");


        final long start2 = System.nanoTime();
        computeFdLibm(value);
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (FdLibm.log10)");

        assert Math.log(value) == computeLog10(value);
    }


    public static void compute(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            Math.log(value);
        }
    }

    public static void computeFdLibm(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            computeLog10(value);
        }
    }


    /**
     * Return the low-order 32 bits of the double argument as an int.
     */
    private static int __LO(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int)transducer;
    }


    /**
     * Return the high-order 32 bits of the double argument as an int.
     */
    private static int __HI(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int)(transducer >> 32);
    }

    /**
     * Return a double with its high-order bits of the second argument
     * and the low-order bits of the first argument..
     */
    private static double __HI(double x, int high) {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0x0000_0000_FFFF_FFFFL) |
                                       ( ((long)high)) << 32 );
    }


    private static final double TWO54    = 0x1.0p54; // 1.80143985094819840000e+16

    private static final double ivln10    = 0x1.bcb7b1526e50ep-2;  // 4.34294481903251816668e-01
    private static final double log10_2hi = 0x1.34413509f6p-2;     // 3.01029995663611771306e-01;
    private static final double log10_2lo = 0x1.9fef311f12b36p-42; // 3.69423907715893078616e-13;

    public static double computeLog10(double x) {
        double y, z;
        int i, k;

        int hx = __HI(x); // high word of x
        int lx = __LO(x); // low word of x

        k=0;
        if (hx < 0x0010_0000) {                  /* x < 2**-1022  */
            if (((hx & 0x7fff_ffff) | lx) == 0) {
                return -TWO54/0.0;               /* log(+-0)=-inf */
            }
            if (hx < 0) {
                return (x - x)/0.0;              /* log(-#) = NaN */
            }
            k -= 54;
            x *= TWO54; /* subnormal number, scale up x */
            hx = __HI(x);
        }

        if (hx >= 0x7ff0_0000) {
            return x + x;
        }

        k += (hx >> 20) - 1023;
        i  = (k  & 0x8000_0000) >>> 31; // unsigned shift
        hx = (hx & 0x000f_ffff) | ((0x3ff - i) << 20);
        y  = (double)(k + i);
        x = __HI(x, hx); // replace high word of x with hx
        z  = y * log10_2lo + ivln10 * StrictMath.log(x);
        return  z + y * log10_2hi;
    }


}

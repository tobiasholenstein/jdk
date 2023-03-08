import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        final Random random = new Random(42);
        final double[] values = new double[100_000_000];
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextDouble();
        }


            final long start = System.nanoTime();
            double blackhole = compute(values);
            final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println(elapsed + " ms (" + blackhole + ")");
    }

    public static double compute(double[] values) {
        double blackhole = 0;
        for (int i = 0; i < values.length; i++) {
            blackhole += foo(values[i]);
        }
        return blackhole;
    }


    public static double foo(double value) {
        return Math.log(value);
    }
}

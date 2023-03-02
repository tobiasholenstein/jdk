import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        //while (true) {
            final Random random = new Random(42);
            final double[] values = new double[100_000_000];
            for (int i = 0; i < values.length; i++)
                values[i] = random.nextDouble();

            System.gc();

            final long start = System.nanoTime();

            double blackhole = 0;
            for (int i = 0; i < values.length; i++)
                blackhole += Math.log(values[i]);

            final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println(elapsed + "ms (" + blackhole + ")");
        //}
    }
}

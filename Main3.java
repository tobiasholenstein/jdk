import java.util.Random;
import java.util.concurrent.TimeUnit;

/*

/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:CompileCommand=compileonly,Main::compute -XX:CompileCommand=compileonly,Main::computeFdLibm  -XX:CompileCommand=compileonly,java.lang.FdLibm*::compute -XX:CompileCommand=dontinline,java.lang.Double::* Main3.java

*/


public class Main {

    public static void main(String[] args) throws Exception {
        final Random random = new Random();
        double value = random.nextDouble();


        final long start = System.nanoTime();
        compute(value);
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math)");


        final long start2 = System.nanoTime();
        computeFdLibm(value);
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (StrictMath)");

        double res1 = Math.log(value);
        double res2 = StrictMath.log(value);

        System.out.println(res1);
        System.out.println(res2);
        assert res1 == res2;
    }


    public static void compute(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            Math.cos(value);
        }
    }

    public static void computeFdLibm(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.cos(value);
        }
    }
}


/*

-XX:CompileCommand=dontinline,java.lang.FdLibm::__LO -XX:CompileCommand=dontinline,java.lang.FdLibm::__HI

521 ms (Math.log) // default impl. delegates to StrictMath
1779 ms (StrictMath.log)

646 ms (Math.log10) // default impl. delegates to StrictMath
3333 ms (StrictMath.log10)

693 ms (Math.exp) // default impl. delegates to StrictMath
1863 ms (StrictMath.exp)



 -XX:-TieredCompilation

523 ms (Math.log) // default impl. delegates to StrictMath
45 ms (StrictMath.log)

631 ms (Math.log10) // default impl. delegates to StrictMath
49 ms (StrictMath.log10)

685 ms (Math.exp) // default impl. delegates to StrictMath
47 ms (StrictMath.exp)

10 ms (Math.cbrt) // default impl. delegates to StrictMath
1 ms (StrictMath.cbrt)





22 ms (Math.sin) // default impl. delegates to StrictMath
360 ms (StrictMath.sin)

28 ms (Math.cos) // default impl. delegates to StrictMath
391 ms (StrictMath.cos)



739 ms (Math.tan) // default impl. delegates to StrictMath
579 ms (StrictMath.tan)

786 ms (Math.atan2) // default impl. delegates to StrictMath
781 ms (StrictMath.atan2)

758 ms (Math.hypot) // default impl. delegates to StrictMath
753 ms (StrictMath.hypot)

1322 ms (Math.pow) // default impl. delegates to StrictMath
1382 ms (StrictMath.pow)



40 ms (Math.acos) // default impl. delegates to StrictMath
36 ms (StrictMath.acos)

47 ms (Math.asin) // default impl. delegates to StrictMath
43 ms (StrictMath.asin)

39 ms (Math.acos) // default impl. delegates to StrictMath
36 ms (StrictMath.acos)

43 ms (Math.atan) // default impl. delegates to StrictMath
39 ms (StrictMath.atan)

51 ms (Math.sinh) // default impl. delegates to StrictMath
46 ms (StrictMath.sinh)

49 ms (Math.cosh) // default impl. delegates to StrictMath
44 ms (StrictMath.cosh)

75 ms (Math.tanh) // default impl. delegates to StrictMath
70 ms (StrictMath.tanh)

47 ms (Math.expm1) // default impl. delegates to StrictMath
44 ms (StrictMath.expm1)

60 ms (Math.log1p) // default impl. delegates to StrictMath
55 ms (StrictMath.log1p)

*/

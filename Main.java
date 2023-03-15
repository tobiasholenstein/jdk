import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        final Random random = new Random();

        final long start = System.nanoTime();
        compute(random.nextDouble());
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        System.out.println(elapsed + " ms");
    }

    public static void compute(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            StrictMath.log(value);
        }
    }


}

/*
Reproduce:
./test.sh

NOT Reproduce:
./test.sh -XX:CompileCommand=dontinline,java.lang.FdLibm::__LO -XX:CompileCommand=dontinline,java.lang.FdLibm::__HI

*/

/*

made not compilable on level 4  java.lang.String::indexOf (7 bytes)   excluded by CompileCommand
   8394  107             Main::logfn (5 bytes)
                            @ 1   java.lang.Math::log (5 bytes)   inline (hot)
                              @ 1   java.lang.StrictMath::log (5 bytes)   inline (hot)
   8399  107             Main::logfn (5 bytes)   made not entrant
   8400  108             Main::logfn (5 bytes)
                            @ 1   java.lang.Math::log (5 bytes)   inline (hot)
                              @ 1   java.lang.StrictMath::log (5 bytes)   inline (hot)
                                @ 1   java.lang.FdLibm$Log::compute (443 bytes)   too big
   8401  109 %           Main::compute @ 2 (20 bytes)
                            @ 9   Main::logfn (5 bytes)   disallowed by CompileCommand
   8402  110             Main::compute (20 bytes)
                            @ 9   Main::logfn (5 bytes)   disallowed by CompileCommand
   8402  111             java.lang.FdLibm$Log::compute (443 bytes)

   made not compilable on level 4  java.lang.FdLibm::__LO (8 bytes)   excluded by CompileCommand
                            @ 1   java.lang.FdLibm::__HI (11 bytes)   inline (hot)
                              @ 1   java.lang.Double::doubleToRawLongBits (0 bytes)   (intrinsic)
                            @ 7   java.lang.FdLibm::__LO (8 bytes)   inline (hot)
                              @ 1   java.lang.Double::doubleToRawLongBits (0 bytes)   (intrinsic)
                            @ 114   java.lang.FdLibm::__HI (20 bytes)   inline (hot)
                              @ 1   java.lang.Double::doubleToRawLongBits (0 bytes)   (intrinsic)
                              @ 16   java.lang.Double::longBitsToDouble (0 bytes)   (intrinsic)
*/

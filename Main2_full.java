import java.util.Random;
import java.util.concurrent.TimeUnit;

/*

/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:CompileCommand=compileonly,Main::compute -XX:CompileCommand=compileonly,Main::computeFdLibm  -XX:CompileCommand=compileonly,Main::computeLog -XX:CompileCommand=dontinline,java.lang.Double::* Main2_full.java


/Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:CompileCommand=dontinline,Main::computeLog -XX:CompileCommand=compileonly,Main::compute -XX:CompileCommand=compileonly,Main::computeFdLibm  -XX:CompileCommand=compileonly,Main::computeLog -XX:CompileCommand=dontinline,java.lang.Double::* -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation -XX:+PrintInlining -XX:CompileCommand=quiet Main2_full.java | grep -v java.lang.invoke.MethodHandle::


*/

public class Main {

    public static void main(String[] args) throws Exception {
        final Random random = new Random();
        double value = random.nextDouble();


        final long start = System.nanoTime();
        compute(value);
        final long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println(elapsed + " ms (Math.log)");


        final long start2 = System.nanoTime();
        computeFdLibm(value);
        final long elapsed2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2);
        System.out.println(elapsed2 + " ms (FdLibm.Log)");

        assert Math.log(value) == computeLog(value);
    }


    public static void compute(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            Math.log(value); // {runtime_call SharedRuntime::dlog(double)}
        }
    }

    public static void computeFdLibm(double value) {
        for (int i = 0; i < 10_000_000; i++) {
            computeLog(value);
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

    private static final double
        ln2_hi = 0x1.62e42feep-1,       // 6.93147180369123816490e-01
        ln2_lo = 0x1.a39ef35793c76p-33, // 1.90821492927058770002e-10

        Lg1    = 0x1.5555555555593p-1,  // 6.666666666666735130e-01
        Lg2    = 0x1.999999997fa04p-2,  // 3.999999999940941908e-01
        Lg3    = 0x1.2492494229359p-2,  // 2.857142874366239149e-01
        Lg4    = 0x1.c71c51d8e78afp-3,  // 2.222219843214978396e-01
        Lg5    = 0x1.7466496cb03dep-3,  // 1.818357216161805012e-01
        Lg6    = 0x1.39a09d078c69fp-3,  // 1.531383769920937332e-01
        Lg7    = 0x1.2f112df3e5244p-3;  // 1.479819860511658591e-01

    private static final double zero = 0.0;

    static double computeLog(double x) {
        double hfsq, f, s, z, R, w, t1, t2, dk;
        int k, hx, i, j;
        /*unsigned*/ int lx;

        hx = __HI(x);           // high word of x
        lx = __LO(x);           // low  word of x

        k=0;
        if (hx < 0x0010_0000) {                  // x < 2**-1022
            if (((hx & 0x7fff_ffff) | lx) == 0) { // log(+-0) = -inf
                return -TWO54/zero;
            }
            if (hx < 0) {                        // log(-#) = NaN
                return (x - x)/zero;
            }
            k -= 54;
            x *= TWO54;    // subnormal number, scale up x
            hx = __HI(x);  // high word of x
        }
        if (hx >= 0x7ff0_0000) {
            return x + x;
        }
        k += (hx >> 20) - 1023;
        hx &= 0x000f_ffff;
        i = (hx + 0x9_5f64) & 0x10_0000;
        x =__HI(x, hx | (i ^ 0x3ff0_0000));  // normalize x or x/2
        k += (i >> 20);
        f = x - 1.0;
        if ((0x000f_ffff & (2 + hx)) < 3) {// |f| < 2**-20
            if (f == zero) {
                if (k == 0) {
                    return zero;
                } else {
                    dk = (double)k;
                    return dk*ln2_hi + dk*ln2_lo;
                }
            }
            R = f*f*(0.5 - 0.33333333333333333*f);
            if (k == 0) {
                return f - R;
            } else {
                dk = (double)k;
                return dk*ln2_hi - ((R - dk*ln2_lo) - f);
            }
        }
        s = f/(2.0 + f);
        dk = (double)k;
        z = s*s;
        i = hx - 0x6_147a;
        w = z*z;
        j = 0x6b851 - hx;
        t1= w*(Lg2 + w*(Lg4 + w*Lg6));
        t2= z*(Lg1 + w*(Lg3 + w*(Lg5 + w*Lg7)));
        i |= j;
        R = t2 + t1;
        if (i > 0) {
            hfsq = 0.5*f*f;
            if (k == 0) {
                return f-(hfsq - s*(hfsq + R));
            } else {
                return dk*ln2_hi - ((hfsq - (s*(hfsq + R) + dk*ln2_lo)) - f);
            }
        } else {
            if (k == 0) {
                return f - s*(f - R);
            } else {
                return dk*ln2_hi - ((s*(f - R) - dk*ln2_lo) - f);
            }
        }
    }

    /*


    ============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    3453  123             Main::computeLog (452 bytes)
 total in heap  [0x00000001100b6c90,0x00000001100b7430] = 1952
 relocation     [0x00000001100b6dd8,0x00000001100b6e20] = 72
 constants      [0x00000001100b6e40,0x00000001100b6e80] = 64
 main code      [0x00000001100b6e80,0x00000001100b7080] = 512
 stub code      [0x00000001100b7080,0x00000001100b7090] = 16
 oops           [0x00000001100b7090,0x00000001100b7098] = 8
 metadata       [0x00000001100b7098,0x00000001100b70a0] = 8
 scopes data    [0x00000001100b70a0,0x00000001100b7238] = 408
 scopes pcs     [0x00000001100b7238,0x00000001100b7428] = 496
 dependencies   [0x00000001100b7428,0x00000001100b7430] = 8

[Disassembly]
--------------------------------------------------------------------------------

[Constant Pool]
             Address          hex4                    hex8
  0x00000001100b6e40:   0xdf3e5244      0x3fc2f112df3e5244
  0x00000001100b6e44:   0x3fc2f112
  0x00000001100b6e48:   0x96cb03de      0x3fc7466496cb03de
  0x00000001100b6e4c:   0x3fc74664
  0x00000001100b6e50:   0xd078c69f      0x3fc39a09d078c69f
  0x00000001100b6e54:   0x3fc39a09
  0x00000001100b6e58:   0x1d8e78af      0x3fcc71c51d8e78af
  0x00000001100b6e5c:   0x3fcc71c5
  0x00000001100b6e60:   0x94229359      0x3fd2492494229359
  0x00000001100b6e64:   0x3fd24924
  0x00000001100b6e68:   0x9997fa04      0x3fd999999997fa04
  0x00000001100b6e6c:   0x3fd99999
  0x00000001100b6e70:   0x55555593      0x3fe5555555555593
  0x00000001100b6e74:   0x3fe55555
  0x00000001100b6e78:   0x00000000      0x0000000000000000
  0x00000001100b6e7c:   0x00000000

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000120e90e58} 'computeLog' '(D)D' in 'Main'
  # parm0:    v0:v0     = double
  #           [sp+0x50]  (sp of caller)
  0x00000001100b6e80:   	nop                         ;   {no_reloc}
  0x00000001100b6e84:   	sub	x9, sp, #0x14, lsl #12
  0x00000001100b6e88:   	str	xzr, [x9]
  0x00000001100b6e8c:   	sub	sp, sp, #0x50
  0x00000001100b6e90:   	stp	x29, x30, [sp, #0x40]
  0x00000001100b6e94:   	ldr	w8, #0x1e8
  0x00000001100b6e98:   	ldr	w9, [x28, #0x20]
  0x00000001100b6e9c:   	cmp	x8, x9
  0x00000001100b6ea0:   	b.ne	#0x1c8              ;*synchronization entry
                                                            ; - Main::computeLog@-1 (line 84)
  0x00000001100b6ea4:   	fmov	x10, d0             ;*invokestatic doubleToRawLongBits {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@1 (line 84)
  0x00000001100b6ea8:   	asr	x11, x10, #32
  0x00000001100b6eac:   	mov	w17, w11            ;*l2i {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@7 (line 84)
  0x00000001100b6eb0:   	and	w12, w17, #0xfffff  ;*iand {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@101 (line 103)
  0x00000001100b6eb4:   	mov	w11, #0x5f64
  0x00000001100b6eb8:   	movk	w11, #0x9, lsl #16
  0x00000001100b6ebc:   	add	w13, w12, w11       ;*iadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@108 (line 104)
  0x00000001100b6ec0:   	and	w11, w13, #0x100000
  0x00000001100b6ec4:   	eor	w14, w11, #0x3ff00000
  0x00000001100b6ec8:   	orr	w11, w14, w12
  0x00000001100b6ecc:   	sbfiz	x11, x11, #32, #32
  0x00000001100b6ed0:   	and	x14, x10, #0xffffffff
  0x00000001100b6ed4:   	orr	x11, x14, x11
  0x00000001100b6ed8:   	fmov	d16, x11
  0x00000001100b6edc:   	fmov	d17, #1.00000000
  0x00000001100b6ee0:   	fsub	d16, d16, d17       ;*dsub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@139 (line 107)
  0x00000001100b6ee4:   	fmov	d17, #2.00000000
  0x00000001100b6ee8:   	fadd	d17, d16, d17
  0x00000001100b6eec:   	fdiv	d17, d16, d17       ;*ddiv {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@248 (line 125)
  0x00000001100b6ef0:   	fmul	d18, d17, d17       ;*dmul {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@260 (line 127)
  0x00000001100b6ef4:   	fmul	d19, d18, d18       ;*dmul {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@274 (line 129)
  0x00000001100b6ef8:   	ldr	d20, #-0xb8         ;   {section_word}
  0x00000001100b6efc:   	fmul	d20, d19, d20
  0x00000001100b6f00:   	ldr	d21, #-0xb8         ;   {section_word}
  0x00000001100b6f04:   	fadd	d20, d20, d21
  0x00000001100b6f08:   	ldr	d21, #-0xb8         ;   {section_word}
  0x00000001100b6f0c:   	fmul	d21, d19, d21
  0x00000001100b6f10:   	fmul	d20, d20, d19
  0x00000001100b6f14:   	ldr	d22, #-0xbc         ;   {section_word}
  0x00000001100b6f18:   	ldr	d23, #-0xb8         ;   {section_word}
  0x00000001100b6f1c:   	fadd	d21, d21, d22
  0x00000001100b6f20:   	fadd	d20, d20, d23
  0x00000001100b6f24:   	fmul	d21, d21, d19
  0x00000001100b6f28:   	fmul	d20, d20, d19
  0x00000001100b6f2c:   	ldr	d22, #-0xc4         ;   {section_word}
  0x00000001100b6f30:   	lsr	w13, w13, #20
  0x00000001100b6f34:   	ldr	d23, #-0xc4         ;   {section_word}
  0x00000001100b6f38:   	fadd	d21, d21, d22
  0x00000001100b6f3c:   	add	w11, w13, w17, asr #20;*isub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@93 (line 102)
  0x00000001100b6f40:   	fadd	d20, d20, d23
  0x00000001100b6f44:   	mov	w13, #0xb851
  0x00000001100b6f48:   	movk	w13, #0x6, lsl #16
  0x00000001100b6f4c:   	sub	w15, w13, w12
  0x00000001100b6f50:   	mov	w13, #0xeb86
  0x00000001100b6f54:   	movk	w13, #0xfff9, lsl #16
  0x00000001100b6f58:   	add	w13, w12, w13
  0x00000001100b6f5c:   	add	w14, w12, #0x2
  0x00000001100b6f60:   	orr	w13, w13, w15       ;*ior {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@339 (line 133)
  0x00000001100b6f64:   	fmul	d19, d21, d19
  0x00000001100b6f68:   	sub	w12, w17, #0x100, lsl #12
  0x00000001100b6f6c:   	fmul	d18, d20, d18
  0x00000001100b6f70:   	and	w14, w14, #0xfffff  ;*iand {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@148 (line 108)
  0x00000001100b6f74:   	sub	w15, w11, #0x3ff    ;*iadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@134 (line 106)
  0x00000001100b6f78:   	orr	w8, wzr, #0x7fe00000
  0x00000001100b6f7c:   	cmp	w12, w8
  0x00000001100b6f80:   	b.hs	#0x70               ;*if_icmplt {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@76 (line 99)
  0x00000001100b6f84:   	cmp	w14, #0x3
  0x00000001100b6f88:   	b.lt	#0x88               ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@150 (line 108)
  0x00000001100b6f8c:   	fadd	d18, d19, d18       ;*dadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@346 (line 134)
  0x00000001100b6f90:   	scvtf	d19, w15            ;*i2d {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@253 (line 126)
  0x00000001100b6f94:   	cmp	w13, #0x0
  0x00000001100b6f98:   	b.gt	#0x98               ;*ifle {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@351 (line 135)
  0x00000001100b6f9c:   	cmp	w11, #0x3ff
  0x00000001100b6fa0:   	b.ne	#0x28               ;*ifne {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@411 (line 143)
  0x00000001100b6fa4:   	fsub	d18, d16, d18
  0x00000001100b6fa8:   	fmul	d17, d18, d17
  0x00000001100b6fac:   	fsub	d0, d16, d17        ;*dsub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@424 (line 144)
  0x00000001100b6fb0:   	ldp	x29, x30, [sp, #0x40]
  0x00000001100b6fb4:   	add	sp, sp, #0x50
  0x00000001100b6fb8:   	ldr	x8, [x28, #0x378]   ;   {poll_return}
  0x00000001100b6fbc:   	cmp	sp, x8
  0x00000001100b6fc0:   	b.hi	#0x9c
  0x00000001100b6fc4:   	ret
  0x00000001100b6fc8:   	str	d19, [sp, #0x18]
  0x00000001100b6fcc:   	str	d18, [sp, #0x10]
  0x00000001100b6fd0:   	mov	w1, #-0xbb
  0x00000001100b6fd4:   	str	d16, [sp]
  0x00000001100b6fd8:   	str	d17, [sp, #0x8]
  0x00000001100b6fdc:   	mov	w29, w15
  0x00000001100b6fe0:   	bl	#-0x67ae0           ; ImmutableOopMap {}
                                                            ;*ifne {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::computeLog@411 (line 143)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x00000001100b6fe4:   	nop                         ;   {other}
  0x00000001100b6fe8:   	movk	xzr, #0x354
  0x00000001100b6fec:   	movk	xzr, #0x0           ;*ifne {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@411 (line 143)
  0x00000001100b6ff0:   	mov	w29, w10            ;*l2i {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@14 (line 85)
  0x00000001100b6ff4:   	mov	w1, #-0xc3
  0x00000001100b6ff8:   	str	d0, [sp]
  0x00000001100b6ffc:   	str	w17, [sp, #0xc]
  0x00000001100b7000:   	bl	#-0x67b00           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::computeLog@24 (line 88)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x00000001100b7004:   	nop                         ;   {other}
  0x00000001100b7008:   	movk	xzr, #0x374
  0x00000001100b700c:   	movk	xzr, #0x100         ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@24 (line 88)
  0x00000001100b7010:   	mov	w1, #-0xbb
  0x00000001100b7014:   	str	d16, [sp]
  0x00000001100b7018:   	mov	w29, w15
  0x00000001100b701c:   	str	w14, [sp, #0x8]
  0x00000001100b7020:   	bl	#-0x67b20           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::computeLog@150 (line 108)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x00000001100b7024:   	nop                         ;   {other}
  0x00000001100b7028:   	movk	xzr, #0x394
  0x00000001100b702c:   	movk	xzr, #0x200         ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@150 (line 108)
  0x00000001100b7030:   	str	d19, [sp, #0x20]
  0x00000001100b7034:   	str	d18, [sp, #0x18]
  0x00000001100b7038:   	mov	w1, #-0xbb
  0x00000001100b703c:   	str	d16, [sp]
  0x00000001100b7040:   	str	d17, [sp, #0x8]
  0x00000001100b7044:   	mov	w29, w15
  0x00000001100b7048:   	str	w13, [sp, #0x10]
  0x00000001100b704c:   	bl	#-0x67b4c           ; ImmutableOopMap {}
                                                            ;*ifle {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::computeLog@351 (line 135)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x00000001100b7050:   	nop                         ;   {other}
  0x00000001100b7054:   	movk	xzr, #0x3c0
  0x00000001100b7058:   	movk	xzr, #0x300         ;*ifle {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::computeLog@351 (line 135)
  0x00000001100b705c:   	adr	x8, #-0xa4          ;   {internal_word}
  0x00000001100b7060:   	str	x8, [x28, #0x390]
  0x00000001100b7064:   	b	#-0x681e4           ;   {runtime_call SafepointBlob}
  0x00000001100b7068:   	mov	x8, #0x3400
  0x00000001100b706c:   	movk	x8, #0x1003, lsl #16
  0x00000001100b7070:   	movk	x8, #0x1, lsl #32
  0x00000001100b7074:   	blr	x8
  0x00000001100b7078:   	b	#-0x1d4
  0x00000001100b707c:   	udf	#0x1                ;   {other}
[Exception Handler]
  0x00000001100b7080:   	b	#-0x43f00           ;   {no_reloc}
[Deopt Handler Code]
  0x00000001100b7084:   	adr	x30, #0x0
  0x00000001100b7088:   	b	#-0x67ec8           ;   {runtime_call DeoptimizationBlob}
  0x00000001100b708c:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

    */



}

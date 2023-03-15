Default
CompileCommand: compileonly Main.compute bool compileonly = true
CompileCommand: dontinline Main.logfn bool dontinline = true
CompileCommand: compileonly Main.logfn bool compileonly = true
CompileCommand: compileonly java/lang/FdLibm*.compute bool compileonly = true
CompileCommand: dontinline java/lang/Double.* bool dontinline = true

Compiled method (n/a)     537    1     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLLL)L (native)
 total in heap  [0x0000000104777110,0x00000001047772a0] = 400
 relocation     [0x0000000104777258,0x0000000104777260] = 8
 main code      [0x0000000104777280,0x00000001047772a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001143c0a80} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104777280:   	nop
  0x0000000104777284:   	ldr	w12, [x7, #0x24]
  0x0000000104777288:   	lsl	x12, x12, #3
  0x000000010477728c:   	ldr	x12, [x12, #0x10]
  0x0000000104777290:   	cbz	x12, #0xc
  0x0000000104777294:   	ldr	x8, [x12, #0x40]
  0x0000000104777298:   	br	x8
  0x000000010477729c:   	b	#-0x3141c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     554    2     n       java.lang.invoke.MethodHandle::linkToStatic(LLL)L (native)
 total in heap  [0x000000010477cb90,0x000000010477cd20] = 400
 relocation     [0x000000010477ccd8,0x000000010477cce0] = 8
 main code      [0x000000010477cd00,0x000000010477cd20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114413100} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010477cd00:   	nop
  0x000000010477cd04:   	ldr	w12, [x3, #0x24]
  0x000000010477cd08:   	lsl	x12, x12, #3
  0x000000010477cd0c:   	ldr	x12, [x12, #0x10]
  0x000000010477cd10:   	cbz	x12, #0xc
  0x000000010477cd14:   	ldr	x8, [x12, #0x40]
  0x000000010477cd18:   	br	x8
  0x000000010477cd1c:   	b	#-0x36e9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     557    3     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLL)L (native)
 total in heap  [0x000000010477ce90,0x000000010477d030] = 416
 relocation     [0x000000010477cfd8,0x000000010477cfe0] = 8
 main code      [0x000000010477d000,0x000000010477d030] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011441c4b8} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x000000010477d000:   	nop
  0x000000010477d004:   	ldr	w12, [x1, #0x14]
  0x000000010477d008:   	lsl	x12, x12, #3
  0x000000010477d00c:   	ldr	w12, [x12, #0x28]
  0x000000010477d010:   	lsl	x12, x12, #3
  0x000000010477d014:   	ldr	w12, [x12, #0x24]
  0x000000010477d018:   	lsl	x12, x12, #3
  0x000000010477d01c:   	ldr	x12, [x12, #0x10]
  0x000000010477d020:   	cbz	x12, #0xc
  0x000000010477d024:   	ldr	x8, [x12, #0x40]
  0x000000010477d028:   	br	x8
  0x000000010477d02c:   	b	#-0x371ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     559    4     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLLL)L (native)
 total in heap  [0x000000010477d190,0x000000010477d328] = 408
 relocation     [0x000000010477d2d8,0x000000010477d2e0] = 8
 main code      [0x000000010477d300,0x000000010477d324] = 36
 stub code      [0x000000010477d324,0x000000010477d328] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011441c630} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010477d300:   	nop
  0x000000010477d304:   	ldr	xzr, [x1]
  0x000000010477d308:   	ldr	w12, [x0, #0x24]
  0x000000010477d30c:   	lsl	x12, x12, #3
  0x000000010477d310:   	ldr	x12, [x12, #0x10]
  0x000000010477d314:   	cbz	x12, #0xc
  0x000000010477d318:   	ldr	x8, [x12, #0x40]
  0x000000010477d31c:   	br	x8
  0x000000010477d320:   	b	#-0x374a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010477d324:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     566    5     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)V (native)
 total in heap  [0x000000010477f090,0x000000010477f228] = 408
 relocation     [0x000000010477f1d8,0x000000010477f1e0] = 8
 main code      [0x000000010477f200,0x000000010477f224] = 36
 stub code      [0x000000010477f224,0x000000010477f228] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011444a7f0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010477f200:   	nop
  0x000000010477f204:   	ldr	xzr, [x1]
  0x000000010477f208:   	ldr	w12, [x4, #0x24]
  0x000000010477f20c:   	lsl	x12, x12, #3
  0x000000010477f210:   	ldr	x12, [x12, #0x10]
  0x000000010477f214:   	cbz	x12, #0xc
  0x000000010477f218:   	ldr	x8, [x12, #0x40]
  0x000000010477f21c:   	br	x8
  0x000000010477f220:   	b	#-0x393a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010477f224:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     568    6     n       java.lang.invoke.MethodHandle::invokeBasic(LL)L (native)
 total in heap  [0x000000010477f710,0x000000010477f8b0] = 416
 relocation     [0x000000010477f858,0x000000010477f860] = 8
 main code      [0x000000010477f880,0x000000010477f8b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011444bc38} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x000000010477f880:   	nop
  0x000000010477f884:   	ldr	w12, [x1, #0x14]
  0x000000010477f888:   	lsl	x12, x12, #3
  0x000000010477f88c:   	ldr	w12, [x12, #0x28]
  0x000000010477f890:   	lsl	x12, x12, #3
  0x000000010477f894:   	ldr	w12, [x12, #0x24]
  0x000000010477f898:   	lsl	x12, x12, #3
  0x000000010477f89c:   	ldr	x12, [x12, #0x10]
  0x000000010477f8a0:   	cbz	x12, #0xc
  0x000000010477f8a4:   	ldr	x8, [x12, #0x40]
  0x000000010477f8a8:   	br	x8
  0x000000010477f8ac:   	b	#-0x39a2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     569    7     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)L (native)
 total in heap  [0x000000010477fa10,0x000000010477fba8] = 408
 relocation     [0x000000010477fb58,0x000000010477fb60] = 8
 main code      [0x000000010477fb80,0x000000010477fba4] = 36
 stub code      [0x000000010477fba4,0x000000010477fba8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011444bd50} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010477fb80:   	nop
  0x000000010477fb84:   	ldr	xzr, [x1]
  0x000000010477fb88:   	ldr	w12, [x4, #0x24]
  0x000000010477fb8c:   	lsl	x12, x12, #3
  0x000000010477fb90:   	ldr	x12, [x12, #0x10]
  0x000000010477fb94:   	cbz	x12, #0xc
  0x000000010477fb98:   	ldr	x8, [x12, #0x40]
  0x000000010477fb9c:   	br	x8
  0x000000010477fba0:   	b	#-0x39d20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010477fba4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     589    8     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)L (native)
 total in heap  [0x0000000104782010,0x00000001047821a8] = 408
 relocation     [0x0000000104782158,0x0000000104782160] = 8
 main code      [0x0000000104782180,0x00000001047821a4] = 36
 stub code      [0x00000001047821a4,0x00000001047821a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114489e80} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104782180:   	nop
  0x0000000104782184:   	ldr	xzr, [x1]
  0x0000000104782188:   	ldr	w12, [x3, #0x24]
  0x000000010478218c:   	lsl	x12, x12, #3
  0x0000000104782190:   	ldr	x12, [x12, #0x10]
  0x0000000104782194:   	cbz	x12, #0xc
  0x0000000104782198:   	ldr	x8, [x12, #0x40]
  0x000000010478219c:   	br	x8
  0x00000001047821a0:   	b	#-0x3c320           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047821a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     601    9     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)I (native)
 total in heap  [0x0000000104786590,0x0000000104786720] = 400
 relocation     [0x00000001047866d8,0x00000001047866e0] = 8
 main code      [0x0000000104786700,0x0000000104786720] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001144c7c20} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104786700:   	nop
  0x0000000104786704:   	ldr	w12, [x4, #0x24]
  0x0000000104786708:   	lsl	x12, x12, #3
  0x000000010478670c:   	ldr	x12, [x12, #0x10]
  0x0000000104786710:   	cbz	x12, #0xc
  0x0000000104786714:   	ldr	x8, [x12, #0x40]
  0x0000000104786718:   	br	x8
  0x000000010478671c:   	b	#-0x4089c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     610   10     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)V (native)
 total in heap  [0x0000000104786c10,0x0000000104786da8] = 408
 relocation     [0x0000000104786d58,0x0000000104786d60] = 8
 main code      [0x0000000104786d80,0x0000000104786da4] = 36
 stub code      [0x0000000104786da4,0x0000000104786da8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001144e1190} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104786d80:   	nop
  0x0000000104786d84:   	ldr	xzr, [x1]
  0x0000000104786d88:   	ldr	w12, [x3, #0x24]
  0x0000000104786d8c:   	lsl	x12, x12, #3
  0x0000000104786d90:   	ldr	x12, [x12, #0x10]
  0x0000000104786d94:   	cbz	x12, #0xc
  0x0000000104786d98:   	ldr	x8, [x12, #0x40]
  0x0000000104786d9c:   	br	x8
  0x0000000104786da0:   	b	#-0x40f20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104786da4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     612   11     n       java.lang.invoke.MethodHandle::invokeBasic(L)L (native)
 total in heap  [0x0000000104786f10,0x00000001047870b0] = 416
 relocation     [0x0000000104787058,0x0000000104787060] = 8
 main code      [0x0000000104787080,0x00000001047870b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001144e18c0} 'invokeBasic' '(Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000104787080:   	nop
  0x0000000104787084:   	ldr	w12, [x1, #0x14]
  0x0000000104787088:   	lsl	x12, x12, #3
  0x000000010478708c:   	ldr	w12, [x12, #0x28]
  0x0000000104787090:   	lsl	x12, x12, #3
  0x0000000104787094:   	ldr	w12, [x12, #0x24]
  0x0000000104787098:   	lsl	x12, x12, #3
  0x000000010478709c:   	ldr	x12, [x12, #0x10]
  0x00000001047870a0:   	cbz	x12, #0xc
  0x00000001047870a4:   	ldr	x8, [x12, #0x40]
  0x00000001047870a8:   	br	x8
  0x00000001047870ac:   	b	#-0x4122c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     616   12     n       java.lang.invoke.MethodHandle::linkToStatic(LL)L (native)
 total in heap  [0x0000000104787210,0x00000001047873a0] = 400
 relocation     [0x0000000104787358,0x0000000104787360] = 8
 main code      [0x0000000104787380,0x00000001047873a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001144e9a08} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104787380:   	nop
  0x0000000104787384:   	ldr	w12, [x2, #0x24]
  0x0000000104787388:   	lsl	x12, x12, #3
  0x000000010478738c:   	ldr	x12, [x12, #0x10]
  0x0000000104787390:   	cbz	x12, #0xc
  0x0000000104787394:   	ldr	x8, [x12, #0x40]
  0x0000000104787398:   	br	x8
  0x000000010478739c:   	b	#-0x4151c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     620   13     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)I (native)
 total in heap  [0x0000000104787f90,0x0000000104788128] = 408
 relocation     [0x00000001047880d8,0x00000001047880e0] = 8
 main code      [0x0000000104788100,0x0000000104788124] = 36
 stub code      [0x0000000104788124,0x0000000104788128] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001144f7a90} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104788100:   	nop
  0x0000000104788104:   	ldr	xzr, [x1]
  0x0000000104788108:   	ldr	w12, [x4, #0x24]
  0x000000010478810c:   	lsl	x12, x12, #3
  0x0000000104788110:   	ldr	x12, [x12, #0x10]
  0x0000000104788114:   	cbz	x12, #0xc
  0x0000000104788118:   	ldr	x8, [x12, #0x40]
  0x000000010478811c:   	br	x8
  0x0000000104788120:   	b	#-0x422a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104788124:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     635   14     n       java.lang.invoke.MethodHandle::linkToStatic(L)L (native)
 total in heap  [0x0000000104788610,0x00000001047887a0] = 400
 relocation     [0x0000000104788758,0x0000000104788760] = 8
 main code      [0x0000000104788780,0x00000001047887a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145ae8f0} 'linkToStatic' '(Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104788780:   	nop
  0x0000000104788784:   	ldr	w12, [x1, #0x24]
  0x0000000104788788:   	lsl	x12, x12, #3
  0x000000010478878c:   	ldr	x12, [x12, #0x10]
  0x0000000104788790:   	cbz	x12, #0xc
  0x0000000104788794:   	ldr	x8, [x12, #0x40]
  0x0000000104788798:   	br	x8
  0x000000010478879c:   	b	#-0x4291c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     639   15     n       java.lang.invoke.MethodHandle::linkToStatic(LLLL)L (native)
 total in heap  [0x0000000104789010,0x00000001047891a0] = 400
 relocation     [0x0000000104789158,0x0000000104789160] = 8
 main code      [0x0000000104789180,0x00000001047891a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145c3320} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104789180:   	nop
  0x0000000104789184:   	ldr	w12, [x4, #0x24]
  0x0000000104789188:   	lsl	x12, x12, #3
  0x000000010478918c:   	ldr	x12, [x12, #0x10]
  0x0000000104789190:   	cbz	x12, #0xc
  0x0000000104789194:   	ldr	x8, [x12, #0x40]
  0x0000000104789198:   	br	x8
  0x000000010478919c:   	b	#-0x4331c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     642   16     n       java.lang.invoke.MethodHandle::invokeBasic()L (native)
 total in heap  [0x0000000104789690,0x0000000104789830] = 416
 relocation     [0x00000001047897d8,0x00000001047897e0] = 8
 main code      [0x0000000104789800,0x0000000104789830] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145c76b8} 'invokeBasic' '()Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  #           [sp+0x0]  (sp of caller)
  0x0000000104789800:   	nop
  0x0000000104789804:   	ldr	w12, [x1, #0x14]
  0x0000000104789808:   	lsl	x12, x12, #3
  0x000000010478980c:   	ldr	w12, [x12, #0x28]
  0x0000000104789810:   	lsl	x12, x12, #3
  0x0000000104789814:   	ldr	w12, [x12, #0x24]
  0x0000000104789818:   	lsl	x12, x12, #3
  0x000000010478981c:   	ldr	x12, [x12, #0x10]
  0x0000000104789820:   	cbz	x12, #0xc
  0x0000000104789824:   	ldr	x8, [x12, #0x40]
  0x0000000104789828:   	br	x8
  0x000000010478982c:   	b	#-0x439ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     643   17     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)L (native)
 total in heap  [0x0000000104789990,0x0000000104789b28] = 408
 relocation     [0x0000000104789ad8,0x0000000104789ae0] = 8
 main code      [0x0000000104789b00,0x0000000104789b24] = 36
 stub code      [0x0000000104789b24,0x0000000104789b28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145c77d0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104789b00:   	nop
  0x0000000104789b04:   	ldr	xzr, [x1]
  0x0000000104789b08:   	ldr	w12, [x2, #0x24]
  0x0000000104789b0c:   	lsl	x12, x12, #3
  0x0000000104789b10:   	ldr	x12, [x12, #0x10]
  0x0000000104789b14:   	cbz	x12, #0xc
  0x0000000104789b18:   	ldr	x8, [x12, #0x40]
  0x0000000104789b1c:   	br	x8
  0x0000000104789b20:   	b	#-0x43ca0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104789b24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     647   18     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)V (native)
 total in heap  [0x0000000104789c90,0x0000000104789e28] = 408
 relocation     [0x0000000104789dd8,0x0000000104789de0] = 8
 main code      [0x0000000104789e00,0x0000000104789e28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145d9838} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104789e00:   	nop
  0x0000000104789e04:   	ldr	w10, [x1, #0x8]
  0x0000000104789e08:   	eor	x10, x10, #0x800000000
  0x0000000104789e0c:   	ldr	x11, [x2, #0x10]
  0x0000000104789e10:   	add	x12, x10, x11, uxtx #3
  0x0000000104789e14:   	ldr	x12, [x12, #0x1c0]
  0x0000000104789e18:   	cbz	x12, #0xc
  0x0000000104789e1c:   	ldr	x8, [x12, #0x40]
  0x0000000104789e20:   	br	x8
  0x0000000104789e24:   	b	#-0x43fa4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     649   19     n       java.lang.invoke.MethodHandle::linkToStatic(LLL)I (native)
 total in heap  [0x0000000104789f90,0x000000010478a120] = 400
 relocation     [0x000000010478a0d8,0x000000010478a0e0] = 8
 main code      [0x000000010478a100,0x000000010478a120] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145da9c8} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478a100:   	nop
  0x000000010478a104:   	ldr	w12, [x3, #0x24]
  0x000000010478a108:   	lsl	x12, x12, #3
  0x000000010478a10c:   	ldr	x12, [x12, #0x10]
  0x000000010478a110:   	cbz	x12, #0xc
  0x000000010478a114:   	ldr	x8, [x12, #0x40]
  0x000000010478a118:   	br	x8
  0x000000010478a11c:   	b	#-0x4429c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     651   20     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)V (native)
 total in heap  [0x000000010478a290,0x000000010478a428] = 408
 relocation     [0x000000010478a3d8,0x000000010478a3e0] = 8
 main code      [0x000000010478a400,0x000000010478a424] = 36
 stub code      [0x000000010478a424,0x000000010478a428] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145dce60} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478a400:   	nop
  0x000000010478a404:   	ldr	xzr, [x1]
  0x000000010478a408:   	ldr	w12, [x2, #0x24]
  0x000000010478a40c:   	lsl	x12, x12, #3
  0x000000010478a410:   	ldr	x12, [x12, #0x10]
  0x000000010478a414:   	cbz	x12, #0xc
  0x000000010478a418:   	ldr	x8, [x12, #0x40]
  0x000000010478a41c:   	br	x8
  0x000000010478a420:   	b	#-0x445a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010478a424:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     654   21     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)L (native)
 total in heap  [0x000000010478a590,0x000000010478a768] = 472
 relocation     [0x000000010478a6d8,0x000000010478a6e0] = 8
 main code      [0x000000010478a700,0x000000010478a764] = 100
 stub code      [0x000000010478a764,0x000000010478a768] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145df470} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478a700:   	nop
  0x000000010478a704:   	ldr	w10, [x1, #0x8]
  0x000000010478a708:   	eor	x10, x10, #0x800000000
  0x000000010478a70c:   	ldr	w14, [x3, #0x18]
  0x000000010478a710:   	lsl	x14, x14, #3
  0x000000010478a714:   	ldr	x14, [x14, #0x10]
  0x000000010478a718:   	ldr	x12, [x3, #0x10]
  0x000000010478a71c:   	ldr	w11, [x10, #0xa0]
  0x000000010478a720:   	add	x11, x10, x11, uxtx #3
  0x000000010478a724:   	add	x11, x11, #0x1c0
  0x000000010478a728:   	add	x10, x10, x12, uxtx #3
  0x000000010478a72c:   	ldr	x12, [x11]
  0x000000010478a730:   	cmp	x14, x12
  0x000000010478a734:   	b.eq	#0x14
  0x000000010478a738:   	cbz	x12, #0x28
  0x000000010478a73c:   	ldr	x12, [x11, #0x10]!
  0x000000010478a740:   	cmp	x14, x12
  0x000000010478a744:   	b.ne	#-0xc
  0x000000010478a748:   	ldr	w11, [x11, #0x8]
  0x000000010478a74c:   	ldr	x12, [x10, w11, uxtw]
  0x000000010478a750:   	cbz	x12, #0xc
  0x000000010478a754:   	ldr	x8, [x12, #0x40]
  0x000000010478a758:   	br	x8
  0x000000010478a75c:   	b	#-0x448dc           ;   {runtime_call AbstractMethodError throw_exception}
  0x000000010478a760:   	b	#-0x631e0           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x000000010478a764:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     660   22     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)I (native)
 total in heap  [0x000000010478a890,0x000000010478aa68] = 472
 relocation     [0x000000010478a9d8,0x000000010478a9e0] = 8
 main code      [0x000000010478aa00,0x000000010478aa64] = 100
 stub code      [0x000000010478aa64,0x000000010478aa68] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145f5ed0} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478aa00:   	nop
  0x000000010478aa04:   	ldr	w10, [x1, #0x8]
  0x000000010478aa08:   	eor	x10, x10, #0x800000000
  0x000000010478aa0c:   	ldr	w14, [x3, #0x18]
  0x000000010478aa10:   	lsl	x14, x14, #3
  0x000000010478aa14:   	ldr	x14, [x14, #0x10]
  0x000000010478aa18:   	ldr	x12, [x3, #0x10]
  0x000000010478aa1c:   	ldr	w11, [x10, #0xa0]
  0x000000010478aa20:   	add	x11, x10, x11, uxtx #3
  0x000000010478aa24:   	add	x11, x11, #0x1c0
  0x000000010478aa28:   	add	x10, x10, x12, uxtx #3
  0x000000010478aa2c:   	ldr	x12, [x11]
  0x000000010478aa30:   	cmp	x14, x12
  0x000000010478aa34:   	b.eq	#0x14
  0x000000010478aa38:   	cbz	x12, #0x28
  0x000000010478aa3c:   	ldr	x12, [x11, #0x10]!
  0x000000010478aa40:   	cmp	x14, x12
  0x000000010478aa44:   	b.ne	#-0xc
  0x000000010478aa48:   	ldr	w11, [x11, #0x8]
  0x000000010478aa4c:   	ldr	x12, [x10, w11, uxtw]
  0x000000010478aa50:   	cbz	x12, #0xc
  0x000000010478aa54:   	ldr	x8, [x12, #0x40]
  0x000000010478aa58:   	br	x8
  0x000000010478aa5c:   	b	#-0x44bdc           ;   {runtime_call AbstractMethodError throw_exception}
  0x000000010478aa60:   	b	#-0x634e0           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x000000010478aa64:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     767   23     n       java.lang.invoke.MethodHandle::linkToInterface(LL)L (native)
 total in heap  [0x000000010478c090,0x000000010478c268] = 472
 relocation     [0x000000010478c1d8,0x000000010478c1e0] = 8
 main code      [0x000000010478c200,0x000000010478c264] = 100
 stub code      [0x000000010478c264,0x000000010478c268] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114575bb8} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478c200:   	nop
  0x000000010478c204:   	ldr	w10, [x1, #0x8]
  0x000000010478c208:   	eor	x10, x10, #0x800000000
  0x000000010478c20c:   	ldr	w14, [x2, #0x18]
  0x000000010478c210:   	lsl	x14, x14, #3
  0x000000010478c214:   	ldr	x14, [x14, #0x10]
  0x000000010478c218:   	ldr	x12, [x2, #0x10]
  0x000000010478c21c:   	ldr	w11, [x10, #0xa0]
  0x000000010478c220:   	add	x11, x10, x11, uxtx #3
  0x000000010478c224:   	add	x11, x11, #0x1c0
  0x000000010478c228:   	add	x10, x10, x12, uxtx #3
  0x000000010478c22c:   	ldr	x12, [x11]
  0x000000010478c230:   	cmp	x14, x12
  0x000000010478c234:   	b.eq	#0x14
  0x000000010478c238:   	cbz	x12, #0x28
  0x000000010478c23c:   	ldr	x12, [x11, #0x10]!
  0x000000010478c240:   	cmp	x14, x12
  0x000000010478c244:   	b.ne	#-0xc
  0x000000010478c248:   	ldr	w11, [x11, #0x8]
  0x000000010478c24c:   	ldr	x12, [x10, w11, uxtw]
  0x000000010478c250:   	cbz	x12, #0xc
  0x000000010478c254:   	ldr	x8, [x12, #0x40]
  0x000000010478c258:   	br	x8
  0x000000010478c25c:   	b	#-0x463dc           ;   {runtime_call AbstractMethodError throw_exception}
  0x000000010478c260:   	b	#-0x64ce0           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x000000010478c264:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     774   24     n       java.lang.invoke.MethodHandle::linkToStatic(LL)I (native)
 total in heap  [0x000000010478c390,0x000000010478c520] = 400
 relocation     [0x000000010478c4d8,0x000000010478c4e0] = 8
 main code      [0x000000010478c500,0x000000010478c520] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011457bff0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478c500:   	nop
  0x000000010478c504:   	ldr	w12, [x2, #0x24]
  0x000000010478c508:   	lsl	x12, x12, #3
  0x000000010478c50c:   	ldr	x12, [x12, #0x10]
  0x000000010478c510:   	cbz	x12, #0xc
  0x000000010478c514:   	ldr	x8, [x12, #0x40]
  0x000000010478c518:   	br	x8
  0x000000010478c51c:   	b	#-0x4669c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     776   25     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)L (native)
 total in heap  [0x000000010478c690,0x000000010478c828] = 408
 relocation     [0x000000010478c7d8,0x000000010478c7e0] = 8
 main code      [0x000000010478c800,0x000000010478c828] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011457cc18} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478c800:   	nop
  0x000000010478c804:   	ldr	w10, [x1, #0x8]
  0x000000010478c808:   	eor	x10, x10, #0x800000000
  0x000000010478c80c:   	ldr	x11, [x2, #0x10]
  0x000000010478c810:   	add	x12, x10, x11, uxtx #3
  0x000000010478c814:   	ldr	x12, [x12, #0x1c0]
  0x000000010478c818:   	cbz	x12, #0xc
  0x000000010478c81c:   	ldr	x8, [x12, #0x40]
  0x000000010478c820:   	br	x8
  0x000000010478c824:   	b	#-0x469a4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     779   26     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)I (native)
 total in heap  [0x000000010478c990,0x000000010478cb28] = 408
 relocation     [0x000000010478cad8,0x000000010478cae0] = 8
 main code      [0x000000010478cb00,0x000000010478cb24] = 36
 stub code      [0x000000010478cb24,0x000000010478cb28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001145213b0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478cb00:   	nop
  0x000000010478cb04:   	ldr	xzr, [x1]
  0x000000010478cb08:   	ldr	w12, [x2, #0x24]
  0x000000010478cb0c:   	lsl	x12, x12, #3
  0x000000010478cb10:   	ldr	x12, [x12, #0x10]
  0x000000010478cb14:   	cbz	x12, #0xc
  0x000000010478cb18:   	ldr	x8, [x12, #0x40]
  0x000000010478cb1c:   	br	x8
  0x000000010478cb20:   	b	#-0x46ca0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010478cb24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     870   27     n       java.lang.invoke.MethodHandle::linkToStatic(IL)I (native)
 total in heap  [0x000000010478e510,0x000000010478e6a0] = 400
 relocation     [0x000000010478e658,0x000000010478e660] = 8
 main code      [0x000000010478e680,0x000000010478e6a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011466b508} 'linkToStatic' '(ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478e680:   	nop
  0x000000010478e684:   	ldr	w12, [x2, #0x24]
  0x000000010478e688:   	lsl	x12, x12, #3
  0x000000010478e68c:   	ldr	x12, [x12, #0x10]
  0x000000010478e690:   	cbz	x12, #0xc
  0x000000010478e694:   	ldr	x8, [x12, #0x40]
  0x000000010478e698:   	br	x8
  0x000000010478e69c:   	b	#-0x4881c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     938   28     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)V (native)
 total in heap  [0x000000010478f290,0x000000010478f428] = 408
 relocation     [0x000000010478f3d8,0x000000010478f3e0] = 8
 main code      [0x000000010478f400,0x000000010478f424] = 36
 stub code      [0x000000010478f424,0x000000010478f428] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146c1778} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478f400:   	nop
  0x000000010478f404:   	ldr	xzr, [x1]
  0x000000010478f408:   	ldr	w12, [x5, #0x24]
  0x000000010478f40c:   	lsl	x12, x12, #3
  0x000000010478f410:   	ldr	x12, [x12, #0x10]
  0x000000010478f414:   	cbz	x12, #0xc
  0x000000010478f418:   	ldr	x8, [x12, #0x40]
  0x000000010478f41c:   	br	x8
  0x000000010478f420:   	b	#-0x495a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010478f424:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     940   29     n       java.lang.invoke.MethodHandle::invokeBasic(LLL)L (native)
 total in heap  [0x000000010478f590,0x000000010478f730] = 416
 relocation     [0x000000010478f6d8,0x000000010478f6e0] = 8
 main code      [0x000000010478f700,0x000000010478f730] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146c1890} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x000000010478f700:   	nop
  0x000000010478f704:   	ldr	w12, [x1, #0x14]
  0x000000010478f708:   	lsl	x12, x12, #3
  0x000000010478f70c:   	ldr	w12, [x12, #0x28]
  0x000000010478f710:   	lsl	x12, x12, #3
  0x000000010478f714:   	ldr	w12, [x12, #0x24]
  0x000000010478f718:   	lsl	x12, x12, #3
  0x000000010478f71c:   	ldr	x12, [x12, #0x10]
  0x000000010478f720:   	cbz	x12, #0xc
  0x000000010478f724:   	ldr	x8, [x12, #0x40]
  0x000000010478f728:   	br	x8
  0x000000010478f72c:   	b	#-0x498ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     941   30     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)L (native)
 total in heap  [0x000000010478f890,0x000000010478fa28] = 408
 relocation     [0x000000010478f9d8,0x000000010478f9e0] = 8
 main code      [0x000000010478fa00,0x000000010478fa24] = 36
 stub code      [0x000000010478fa24,0x000000010478fa28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146c19a8} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478fa00:   	nop
  0x000000010478fa04:   	ldr	xzr, [x1]
  0x000000010478fa08:   	ldr	w12, [x5, #0x24]
  0x000000010478fa0c:   	lsl	x12, x12, #3
  0x000000010478fa10:   	ldr	x12, [x12, #0x10]
  0x000000010478fa14:   	cbz	x12, #0xc
  0x000000010478fa18:   	ldr	x8, [x12, #0x40]
  0x000000010478fa1c:   	br	x8
  0x000000010478fa20:   	b	#-0x49ba0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010478fa24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     996   31     n       java.lang.invoke.MethodHandle::linkToStatic(IIL)I (native)
 total in heap  [0x000000010478fb90,0x000000010478fd20] = 400
 relocation     [0x000000010478fcd8,0x000000010478fce0] = 8
 main code      [0x000000010478fd00,0x000000010478fd20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d5cc8} 'linkToStatic' '(IILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010478fd00:   	nop
  0x000000010478fd04:   	ldr	w12, [x3, #0x24]
  0x000000010478fd08:   	lsl	x12, x12, #3
  0x000000010478fd0c:   	ldr	x12, [x12, #0x10]
  0x000000010478fd10:   	cbz	x12, #0xc
  0x000000010478fd14:   	ldr	x8, [x12, #0x40]
  0x000000010478fd18:   	br	x8
  0x000000010478fd1c:   	b	#-0x49e9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     997   32     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)V (native)
 total in heap  [0x000000010478fe90,0x0000000104790028] = 408
 relocation     [0x000000010478ffd8,0x000000010478ffe0] = 8
 main code      [0x0000000104790000,0x0000000104790024] = 36
 stub code      [0x0000000104790024,0x0000000104790028] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d61b0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104790000:   	nop
  0x0000000104790004:   	ldr	xzr, [x1]
  0x0000000104790008:   	ldr	w12, [x3, #0x24]
  0x000000010479000c:   	lsl	x12, x12, #3
  0x0000000104790010:   	ldr	x12, [x12, #0x10]
  0x0000000104790014:   	cbz	x12, #0xc
  0x0000000104790018:   	ldr	x8, [x12, #0x40]
  0x000000010479001c:   	br	x8
  0x0000000104790020:   	b	#-0x4a1a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104790024:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     998   33     n       java.lang.invoke.MethodHandle::invokeBasic(I)L (native)
 total in heap  [0x0000000104790190,0x0000000104790330] = 416
 relocation     [0x00000001047902d8,0x00000001047902e0] = 8
 main code      [0x0000000104790300,0x0000000104790330] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d62c8} 'invokeBasic' '(I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000104790300:   	nop
  0x0000000104790304:   	ldr	w12, [x1, #0x14]
  0x0000000104790308:   	lsl	x12, x12, #3
  0x000000010479030c:   	ldr	w12, [x12, #0x28]
  0x0000000104790310:   	lsl	x12, x12, #3
  0x0000000104790314:   	ldr	w12, [x12, #0x24]
  0x0000000104790318:   	lsl	x12, x12, #3
  0x000000010479031c:   	ldr	x12, [x12, #0x10]
  0x0000000104790320:   	cbz	x12, #0xc
  0x0000000104790324:   	ldr	x8, [x12, #0x40]
  0x0000000104790328:   	br	x8
  0x000000010479032c:   	b	#-0x4a4ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     998   34     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)L (native)
 total in heap  [0x0000000104790490,0x0000000104790628] = 408
 relocation     [0x00000001047905d8,0x00000001047905e0] = 8
 main code      [0x0000000104790600,0x0000000104790624] = 36
 stub code      [0x0000000104790624,0x0000000104790628] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d63e0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104790600:   	nop
  0x0000000104790604:   	ldr	xzr, [x1]
  0x0000000104790608:   	ldr	w12, [x3, #0x24]
  0x000000010479060c:   	lsl	x12, x12, #3
  0x0000000104790610:   	ldr	x12, [x12, #0x10]
  0x0000000104790614:   	cbz	x12, #0xc
  0x0000000104790618:   	ldr	x8, [x12, #0x40]
  0x000000010479061c:   	br	x8
  0x0000000104790620:   	b	#-0x4a7a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104790624:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1000   35     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLIILL)I (native)
 total in heap  [0x0000000104790790,0x0000000104790928] = 408
 relocation     [0x00000001047908d8,0x00000001047908e0] = 8
 main code      [0x0000000104790900,0x0000000104790924] = 36
 stub code      [0x0000000104790924,0x0000000104790928] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d65b8} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104790900:   	nop
  0x0000000104790904:   	ldr	xzr, [x1]
  0x0000000104790908:   	ldr	w12, [x7, #0x24]
  0x000000010479090c:   	lsl	x12, x12, #3
  0x0000000104790910:   	ldr	x12, [x12, #0x10]
  0x0000000104790914:   	cbz	x12, #0xc
  0x0000000104790918:   	ldr	x8, [x12, #0x40]
  0x000000010479091c:   	br	x8
  0x0000000104790920:   	b	#-0x4aaa0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104790924:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1002   36     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLIIL)V (native)
 total in heap  [0x0000000104790e10,0x0000000104790fa8] = 408
 relocation     [0x0000000104790f58,0x0000000104790f60] = 8
 main code      [0x0000000104790f80,0x0000000104790fa4] = 36
 stub code      [0x0000000104790fa4,0x0000000104790fa8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d6700} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6   = int
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104790f80:   	nop
  0x0000000104790f84:   	ldr	xzr, [x1]
  0x0000000104790f88:   	ldr	w12, [x7, #0x24]
  0x0000000104790f8c:   	lsl	x12, x12, #3
  0x0000000104790f90:   	ldr	x12, [x12, #0x10]
  0x0000000104790f94:   	cbz	x12, #0xc
  0x0000000104790f98:   	ldr	x8, [x12, #0x40]
  0x0000000104790f9c:   	br	x8
  0x0000000104790fa0:   	b	#-0x4b120           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104790fa4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1003   37     n       java.lang.invoke.MethodHandle::invokeBasic(LLLII)L (native)
 total in heap  [0x0000000104791110,0x00000001047912b0] = 416
 relocation     [0x0000000104791258,0x0000000104791260] = 8
 main code      [0x0000000104791280,0x00000001047912b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d6818} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;II)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5   = int
  # parm4:    c_rarg6   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000104791280:   	nop
  0x0000000104791284:   	ldr	w12, [x1, #0x14]
  0x0000000104791288:   	lsl	x12, x12, #3
  0x000000010479128c:   	ldr	w12, [x12, #0x28]
  0x0000000104791290:   	lsl	x12, x12, #3
  0x0000000104791294:   	ldr	w12, [x12, #0x24]
  0x0000000104791298:   	lsl	x12, x12, #3
  0x000000010479129c:   	ldr	x12, [x12, #0x10]
  0x00000001047912a0:   	cbz	x12, #0xc
  0x00000001047912a4:   	ldr	x8, [x12, #0x40]
  0x00000001047912a8:   	br	x8
  0x00000001047912ac:   	b	#-0x4b42c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1004   38     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLIIL)L (native)
 total in heap  [0x0000000104791410,0x00000001047915a8] = 408
 relocation     [0x0000000104791558,0x0000000104791560] = 8
 main code      [0x0000000104791580,0x00000001047915a4] = 36
 stub code      [0x00000001047915a4,0x00000001047915a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146d6930} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6   = int
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104791580:   	nop
  0x0000000104791584:   	ldr	xzr, [x1]
  0x0000000104791588:   	ldr	w12, [x7, #0x24]
  0x000000010479158c:   	lsl	x12, x12, #3
  0x0000000104791590:   	ldr	x12, [x12, #0x10]
  0x0000000104791594:   	cbz	x12, #0xc
  0x0000000104791598:   	ldr	x8, [x12, #0x40]
  0x000000010479159c:   	br	x8
  0x00000001047915a0:   	b	#-0x4b720           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047915a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1015   39     n       java.lang.invoke.MethodHandle::linkToStatic(LL)V (native)
 total in heap  [0x0000000104791a90,0x0000000104791c20] = 400
 relocation     [0x0000000104791bd8,0x0000000104791be0] = 8
 main code      [0x0000000104791c00,0x0000000104791c20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146df5a0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104791c00:   	nop
  0x0000000104791c04:   	ldr	w12, [x2, #0x24]
  0x0000000104791c08:   	lsl	x12, x12, #3
  0x0000000104791c0c:   	ldr	x12, [x12, #0x10]
  0x0000000104791c10:   	cbz	x12, #0xc
  0x0000000104791c14:   	ldr	x8, [x12, #0x40]
  0x0000000104791c18:   	br	x8
  0x0000000104791c1c:   	b	#-0x4bd9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1020   40     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)I (native)
 total in heap  [0x0000000104791d90,0x0000000104791f28] = 408
 relocation     [0x0000000104791ed8,0x0000000104791ee0] = 8
 main code      [0x0000000104791f00,0x0000000104791f28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e0288} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104791f00:   	nop
  0x0000000104791f04:   	ldr	w10, [x1, #0x8]
  0x0000000104791f08:   	eor	x10, x10, #0x800000000
  0x0000000104791f0c:   	ldr	x11, [x3, #0x10]
  0x0000000104791f10:   	add	x12, x10, x11, uxtx #3
  0x0000000104791f14:   	ldr	x12, [x12, #0x1c0]
  0x0000000104791f18:   	cbz	x12, #0xc
  0x0000000104791f1c:   	ldr	x8, [x12, #0x40]
  0x0000000104791f20:   	br	x8
  0x0000000104791f24:   	b	#-0x4c0a4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1021   41     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)V (native)
 total in heap  [0x0000000104792090,0x0000000104792268] = 472
 relocation     [0x00000001047921d8,0x00000001047921e0] = 8
 main code      [0x0000000104792200,0x0000000104792264] = 100
 stub code      [0x0000000104792264,0x0000000104792268] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e03a0} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104792200:   	nop
  0x0000000104792204:   	ldr	w10, [x1, #0x8]
  0x0000000104792208:   	eor	x10, x10, #0x800000000
  0x000000010479220c:   	ldr	w14, [x3, #0x18]
  0x0000000104792210:   	lsl	x14, x14, #3
  0x0000000104792214:   	ldr	x14, [x14, #0x10]
  0x0000000104792218:   	ldr	x12, [x3, #0x10]
  0x000000010479221c:   	ldr	w11, [x10, #0xa0]
  0x0000000104792220:   	add	x11, x10, x11, uxtx #3
  0x0000000104792224:   	add	x11, x11, #0x1c0
  0x0000000104792228:   	add	x10, x10, x12, uxtx #3
  0x000000010479222c:   	ldr	x12, [x11]
  0x0000000104792230:   	cmp	x14, x12
  0x0000000104792234:   	b.eq	#0x14
  0x0000000104792238:   	cbz	x12, #0x28
  0x000000010479223c:   	ldr	x12, [x11, #0x10]!
  0x0000000104792240:   	cmp	x14, x12
  0x0000000104792244:   	b.ne	#-0xc
  0x0000000104792248:   	ldr	w11, [x11, #0x8]
  0x000000010479224c:   	ldr	x12, [x10, w11, uxtw]
  0x0000000104792250:   	cbz	x12, #0xc
  0x0000000104792254:   	ldr	x8, [x12, #0x40]
  0x0000000104792258:   	br	x8
  0x000000010479225c:   	b	#-0x4c3dc           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000104792260:   	b	#-0x6ace0           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000104792264:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1024   42     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLL)L (native)
 total in heap  [0x0000000104792390,0x0000000104792520] = 400
 relocation     [0x00000001047924d8,0x00000001047924e0] = 8
 main code      [0x0000000104792500,0x0000000104792520] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e2b48} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104792500:   	nop
  0x0000000104792504:   	ldr	w12, [x6, #0x24]
  0x0000000104792508:   	lsl	x12, x12, #3
  0x000000010479250c:   	ldr	x12, [x12, #0x10]
  0x0000000104792510:   	cbz	x12, #0xc
  0x0000000104792514:   	ldr	x8, [x12, #0x40]
  0x0000000104792518:   	br	x8
  0x000000010479251c:   	b	#-0x4c69c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1025   43     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLL)L (native)
 total in heap  [0x0000000104792690,0x0000000104792830] = 416
 relocation     [0x00000001047927d8,0x00000001047927e0] = 8
 main code      [0x0000000104792800,0x0000000104792830] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e2c60} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000104792800:   	nop
  0x0000000104792804:   	ldr	w12, [x1, #0x14]
  0x0000000104792808:   	lsl	x12, x12, #3
  0x000000010479280c:   	ldr	w12, [x12, #0x28]
  0x0000000104792810:   	lsl	x12, x12, #3
  0x0000000104792814:   	ldr	w12, [x12, #0x24]
  0x0000000104792818:   	lsl	x12, x12, #3
  0x000000010479281c:   	ldr	x12, [x12, #0x10]
  0x0000000104792820:   	cbz	x12, #0xc
  0x0000000104792824:   	ldr	x8, [x12, #0x40]
  0x0000000104792828:   	br	x8
  0x000000010479282c:   	b	#-0x4c9ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1027   44     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLL)L (native)
 total in heap  [0x0000000104792990,0x0000000104792b28] = 408
 relocation     [0x0000000104792ad8,0x0000000104792ae0] = 8
 main code      [0x0000000104792b00,0x0000000104792b24] = 36
 stub code      [0x0000000104792b24,0x0000000104792b28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e2d78} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104792b00:   	nop
  0x0000000104792b04:   	ldr	xzr, [x1]
  0x0000000104792b08:   	ldr	w12, [x7, #0x24]
  0x0000000104792b0c:   	lsl	x12, x12, #3
  0x0000000104792b10:   	ldr	x12, [x12, #0x10]
  0x0000000104792b14:   	cbz	x12, #0xc
  0x0000000104792b18:   	ldr	x8, [x12, #0x40]
  0x0000000104792b1c:   	br	x8
  0x0000000104792b20:   	b	#-0x4cca0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104792b24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1029   45     n       java.lang.invoke.MethodHandle::linkToStatic(LJL)L (native)
 total in heap  [0x0000000104792c90,0x0000000104792e20] = 400
 relocation     [0x0000000104792dd8,0x0000000104792de0] = 8
 main code      [0x0000000104792e00,0x0000000104792e20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e3100} 'linkToStatic' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104792e00:   	nop
  0x0000000104792e04:   	ldr	w12, [x3, #0x24]
  0x0000000104792e08:   	lsl	x12, x12, #3
  0x0000000104792e0c:   	ldr	x12, [x12, #0x10]
  0x0000000104792e10:   	cbz	x12, #0xc
  0x0000000104792e14:   	ldr	x8, [x12, #0x40]
  0x0000000104792e18:   	br	x8
  0x0000000104792e1c:   	b	#-0x4cf9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1030   46     n       java.lang.invoke.MethodHandle::invokeBasic(LJ)L (native)
 total in heap  [0x0000000104792f90,0x0000000104793130] = 416
 relocation     [0x00000001047930d8,0x00000001047930e0] = 8
 main code      [0x0000000104793100,0x0000000104793130] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e3218} 'invokeBasic' '(Ljava/lang/Object;J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000104793100:   	nop
  0x0000000104793104:   	ldr	w12, [x1, #0x14]
  0x0000000104793108:   	lsl	x12, x12, #3
  0x000000010479310c:   	ldr	w12, [x12, #0x28]
  0x0000000104793110:   	lsl	x12, x12, #3
  0x0000000104793114:   	ldr	w12, [x12, #0x24]
  0x0000000104793118:   	lsl	x12, x12, #3
  0x000000010479311c:   	ldr	x12, [x12, #0x10]
  0x0000000104793120:   	cbz	x12, #0xc
  0x0000000104793124:   	ldr	x8, [x12, #0x40]
  0x0000000104793128:   	br	x8
  0x000000010479312c:   	b	#-0x4d2ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1031   47     n       java.lang.invoke.MethodHandle::linkToSpecial(LLJL)L (native)
 total in heap  [0x0000000104793290,0x0000000104793428] = 408
 relocation     [0x00000001047933d8,0x00000001047933e0] = 8
 main code      [0x0000000104793400,0x0000000104793424] = 36
 stub code      [0x0000000104793424,0x0000000104793428] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e3330} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104793400:   	nop
  0x0000000104793404:   	ldr	xzr, [x1]
  0x0000000104793408:   	ldr	w12, [x4, #0x24]
  0x000000010479340c:   	lsl	x12, x12, #3
  0x0000000104793410:   	ldr	x12, [x12, #0x10]
  0x0000000104793414:   	cbz	x12, #0xc
  0x0000000104793418:   	ldr	x8, [x12, #0x40]
  0x000000010479341c:   	br	x8
  0x0000000104793420:   	b	#-0x4d5a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104793424:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1033   48     n       java.lang.invoke.MethodHandle::linkToStatic(JLILL)J (native)
 total in heap  [0x0000000104793910,0x0000000104793aa0] = 400
 relocation     [0x0000000104793a58,0x0000000104793a60] = 8
 main code      [0x0000000104793a80,0x0000000104793aa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e35f8} 'linkToStatic' '(JLjava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104793a80:   	nop
  0x0000000104793a84:   	ldr	w12, [x5, #0x24]
  0x0000000104793a88:   	lsl	x12, x12, #3
  0x0000000104793a8c:   	ldr	x12, [x12, #0x10]
  0x0000000104793a90:   	cbz	x12, #0xc
  0x0000000104793a94:   	ldr	x8, [x12, #0x40]
  0x0000000104793a98:   	br	x8
  0x0000000104793a9c:   	b	#-0x4dc1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1034   49     n       java.lang.invoke.MethodHandle::invokeBasic(JLIL)J (native)
 total in heap  [0x0000000104793c10,0x0000000104793db0] = 416
 relocation     [0x0000000104793d58,0x0000000104793d60] = 8
 main code      [0x0000000104793d80,0x0000000104793db0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e3710} 'invokeBasic' '(JLjava/lang/Object;ILjava/lang/Object;)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000104793d80:   	nop
  0x0000000104793d84:   	ldr	w12, [x1, #0x14]
  0x0000000104793d88:   	lsl	x12, x12, #3
  0x0000000104793d8c:   	ldr	w12, [x12, #0x28]
  0x0000000104793d90:   	lsl	x12, x12, #3
  0x0000000104793d94:   	ldr	w12, [x12, #0x24]
  0x0000000104793d98:   	lsl	x12, x12, #3
  0x0000000104793d9c:   	ldr	x12, [x12, #0x10]
  0x0000000104793da0:   	cbz	x12, #0xc
  0x0000000104793da4:   	ldr	x8, [x12, #0x40]
  0x0000000104793da8:   	br	x8
  0x0000000104793dac:   	b	#-0x4df2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1035   50     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLILL)J (native)
 total in heap  [0x0000000104794290,0x0000000104794428] = 408
 relocation     [0x00000001047943d8,0x00000001047943e0] = 8
 main code      [0x0000000104794400,0x0000000104794424] = 36
 stub code      [0x0000000104794424,0x0000000104794428] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e3828} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104794400:   	nop
  0x0000000104794404:   	ldr	xzr, [x1]
  0x0000000104794408:   	ldr	w12, [x6, #0x24]
  0x000000010479440c:   	lsl	x12, x12, #3
  0x0000000104794410:   	ldr	x12, [x12, #0x10]
  0x0000000104794414:   	cbz	x12, #0xc
  0x0000000104794418:   	ldr	x8, [x12, #0x40]
  0x000000010479441c:   	br	x8
  0x0000000104794420:   	b	#-0x4e5a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104794424:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1038   51     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLL)L (native)
 total in heap  [0x0000000104794910,0x0000000104794aa0] = 400
 relocation     [0x0000000104794a58,0x0000000104794a60] = 8
 main code      [0x0000000104794a80,0x0000000104794aa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e6708} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104794a80:   	nop
  0x0000000104794a84:   	ldr	w12, [x5, #0x24]
  0x0000000104794a88:   	lsl	x12, x12, #3
  0x0000000104794a8c:   	ldr	x12, [x12, #0x10]
  0x0000000104794a90:   	cbz	x12, #0xc
  0x0000000104794a94:   	ldr	x8, [x12, #0x40]
  0x0000000104794a98:   	br	x8
  0x0000000104794a9c:   	b	#-0x4ec1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1040   52     n       java.lang.invoke.MethodHandle::invokeBasic(LLLL)L (native)
 total in heap  [0x0000000104794c10,0x0000000104794db0] = 416
 relocation     [0x0000000104794d58,0x0000000104794d60] = 8
 main code      [0x0000000104794d80,0x0000000104794db0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e68b0} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000104794d80:   	nop
  0x0000000104794d84:   	ldr	w12, [x1, #0x14]
  0x0000000104794d88:   	lsl	x12, x12, #3
  0x0000000104794d8c:   	ldr	w12, [x12, #0x28]
  0x0000000104794d90:   	lsl	x12, x12, #3
  0x0000000104794d94:   	ldr	w12, [x12, #0x24]
  0x0000000104794d98:   	lsl	x12, x12, #3
  0x0000000104794d9c:   	ldr	x12, [x12, #0x10]
  0x0000000104794da0:   	cbz	x12, #0xc
  0x0000000104794da4:   	ldr	x8, [x12, #0x40]
  0x0000000104794da8:   	br	x8
  0x0000000104794dac:   	b	#-0x4ef2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1041   53     n       java.lang.invoke.MethodHandle::invokeBasic(JLI)J (native)
 total in heap  [0x0000000104795290,0x0000000104795430] = 416
 relocation     [0x00000001047953d8,0x00000001047953e0] = 8
 main code      [0x0000000104795400,0x0000000104795430] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e6bd8} 'invokeBasic' '(JLjava/lang/Object;I)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000104795400:   	nop
  0x0000000104795404:   	ldr	w12, [x1, #0x14]
  0x0000000104795408:   	lsl	x12, x12, #3
  0x000000010479540c:   	ldr	w12, [x12, #0x28]
  0x0000000104795410:   	lsl	x12, x12, #3
  0x0000000104795414:   	ldr	w12, [x12, #0x24]
  0x0000000104795418:   	lsl	x12, x12, #3
  0x000000010479541c:   	ldr	x12, [x12, #0x10]
  0x0000000104795420:   	cbz	x12, #0xc
  0x0000000104795424:   	ldr	x8, [x12, #0x40]
  0x0000000104795428:   	br	x8
  0x000000010479542c:   	b	#-0x4f5ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1042   54     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLIL)J (native)
 total in heap  [0x0000000104795590,0x0000000104795728] = 408
 relocation     [0x00000001047956d8,0x00000001047956e0] = 8
 main code      [0x0000000104795700,0x0000000104795724] = 36
 stub code      [0x0000000104795724,0x0000000104795728] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e6cf0} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104795700:   	nop
  0x0000000104795704:   	ldr	xzr, [x1]
  0x0000000104795708:   	ldr	w12, [x5, #0x24]
  0x000000010479570c:   	lsl	x12, x12, #3
  0x0000000104795710:   	ldr	x12, [x12, #0x10]
  0x0000000104795714:   	cbz	x12, #0xc
  0x0000000104795718:   	ldr	x8, [x12, #0x40]
  0x000000010479571c:   	br	x8
  0x0000000104795720:   	b	#-0x4f8a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104795724:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1043   55     n       java.lang.invoke.MethodHandle::linkToStatic(JL)L (native)
 total in heap  [0x0000000104795890,0x0000000104795a20] = 400
 relocation     [0x00000001047959d8,0x00000001047959e0] = 8
 main code      [0x0000000104795a00,0x0000000104795a20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e6ef8} 'linkToStatic' '(JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104795a00:   	nop
  0x0000000104795a04:   	ldr	w12, [x2, #0x24]
  0x0000000104795a08:   	lsl	x12, x12, #3
  0x0000000104795a0c:   	ldr	x12, [x12, #0x10]
  0x0000000104795a10:   	cbz	x12, #0xc
  0x0000000104795a14:   	ldr	x8, [x12, #0x40]
  0x0000000104795a18:   	br	x8
  0x0000000104795a1c:   	b	#-0x4fb9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1046   56     n       java.lang.invoke.MethodHandle::invokeBasic(J)L (native)
 total in heap  [0x0000000104795b90,0x0000000104795d30] = 416
 relocation     [0x0000000104795cd8,0x0000000104795ce0] = 8
 main code      [0x0000000104795d00,0x0000000104795d30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e8010} 'invokeBasic' '(J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000104795d00:   	nop
  0x0000000104795d04:   	ldr	w12, [x1, #0x14]
  0x0000000104795d08:   	lsl	x12, x12, #3
  0x0000000104795d0c:   	ldr	w12, [x12, #0x28]
  0x0000000104795d10:   	lsl	x12, x12, #3
  0x0000000104795d14:   	ldr	w12, [x12, #0x24]
  0x0000000104795d18:   	lsl	x12, x12, #3
  0x0000000104795d1c:   	ldr	x12, [x12, #0x10]
  0x0000000104795d20:   	cbz	x12, #0xc
  0x0000000104795d24:   	ldr	x8, [x12, #0x40]
  0x0000000104795d28:   	br	x8
  0x0000000104795d2c:   	b	#-0x4feac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1046   57     n       java.lang.invoke.MethodHandle::linkToSpecial(LJL)L (native)
 total in heap  [0x0000000104795e90,0x0000000104796028] = 408
 relocation     [0x0000000104795fd8,0x0000000104795fe0] = 8
 main code      [0x0000000104796000,0x0000000104796024] = 36
 stub code      [0x0000000104796024,0x0000000104796028] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e8128} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104796000:   	nop
  0x0000000104796004:   	ldr	xzr, [x1]
  0x0000000104796008:   	ldr	w12, [x3, #0x24]
  0x000000010479600c:   	lsl	x12, x12, #3
  0x0000000104796010:   	ldr	x12, [x12, #0x10]
  0x0000000104796014:   	cbz	x12, #0xc
  0x0000000104796018:   	ldr	x8, [x12, #0x40]
  0x000000010479601c:   	br	x8
  0x0000000104796020:   	b	#-0x501a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104796024:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1048   58     n       java.lang.invoke.MethodHandle::linkToStatic(JIL)J (native)
 total in heap  [0x0000000104796190,0x0000000104796320] = 400
 relocation     [0x00000001047962d8,0x00000001047962e0] = 8
 main code      [0x0000000104796300,0x0000000104796320] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e84e0} 'linkToStatic' '(JILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104796300:   	nop
  0x0000000104796304:   	ldr	w12, [x3, #0x24]
  0x0000000104796308:   	lsl	x12, x12, #3
  0x000000010479630c:   	ldr	x12, [x12, #0x10]
  0x0000000104796310:   	cbz	x12, #0xc
  0x0000000104796314:   	ldr	x8, [x12, #0x40]
  0x0000000104796318:   	br	x8
  0x000000010479631c:   	b	#-0x5049c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1049   59     n       java.lang.invoke.MethodHandle::invokeBasic(JI)J (native)
 total in heap  [0x0000000104796490,0x0000000104796630] = 416
 relocation     [0x00000001047965d8,0x00000001047965e0] = 8
 main code      [0x0000000104796600,0x0000000104796630] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e85f8} 'invokeBasic' '(JI)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000104796600:   	nop
  0x0000000104796604:   	ldr	w12, [x1, #0x14]
  0x0000000104796608:   	lsl	x12, x12, #3
  0x000000010479660c:   	ldr	w12, [x12, #0x28]
  0x0000000104796610:   	lsl	x12, x12, #3
  0x0000000104796614:   	ldr	w12, [x12, #0x24]
  0x0000000104796618:   	lsl	x12, x12, #3
  0x000000010479661c:   	ldr	x12, [x12, #0x10]
  0x0000000104796620:   	cbz	x12, #0xc
  0x0000000104796624:   	ldr	x8, [x12, #0x40]
  0x0000000104796628:   	br	x8
  0x000000010479662c:   	b	#-0x507ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1050   60     n       java.lang.invoke.MethodHandle::linkToSpecial(LJIL)J (native)
 total in heap  [0x0000000104796790,0x0000000104796928] = 408
 relocation     [0x00000001047968d8,0x00000001047968e0] = 8
 main code      [0x0000000104796900,0x0000000104796924] = 36
 stub code      [0x0000000104796924,0x0000000104796928] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146e8710} 'linkToSpecial' '(Ljava/lang/Object;JILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104796900:   	nop
  0x0000000104796904:   	ldr	xzr, [x1]
  0x0000000104796908:   	ldr	w12, [x4, #0x24]
  0x000000010479690c:   	lsl	x12, x12, #3
  0x0000000104796910:   	ldr	x12, [x12, #0x10]
  0x0000000104796914:   	cbz	x12, #0xc
  0x0000000104796918:   	ldr	x8, [x12, #0x40]
  0x000000010479691c:   	br	x8
  0x0000000104796920:   	b	#-0x50aa0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000104796924:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1053   61     n       java.lang.invoke.MethodHandle::linkToStatic(LLLJL)L (native)
 total in heap  [0x0000000104796e10,0x0000000104796fa0] = 400
 relocation     [0x0000000104796f58,0x0000000104796f60] = 8
 main code      [0x0000000104796f80,0x0000000104796fa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146ea520} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104796f80:   	nop
  0x0000000104796f84:   	ldr	w12, [x5, #0x24]
  0x0000000104796f88:   	lsl	x12, x12, #3
  0x0000000104796f8c:   	ldr	x12, [x12, #0x10]
  0x0000000104796f90:   	cbz	x12, #0xc
  0x0000000104796f94:   	ldr	x8, [x12, #0x40]
  0x0000000104796f98:   	br	x8
  0x0000000104796f9c:   	b	#-0x5111c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1054   62     n       java.lang.invoke.MethodHandle::invokeBasic(LLLJ)L (native)
 total in heap  [0x0000000104797110,0x00000001047972b0] = 416
 relocation     [0x0000000104797258,0x0000000104797260] = 8
 main code      [0x0000000104797280,0x00000001047972b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146ea668} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000104797280:   	nop
  0x0000000104797284:   	ldr	w12, [x1, #0x14]
  0x0000000104797288:   	lsl	x12, x12, #3
  0x000000010479728c:   	ldr	w12, [x12, #0x28]
  0x0000000104797290:   	lsl	x12, x12, #3
  0x0000000104797294:   	ldr	w12, [x12, #0x24]
  0x0000000104797298:   	lsl	x12, x12, #3
  0x000000010479729c:   	ldr	x12, [x12, #0x10]
  0x00000001047972a0:   	cbz	x12, #0xc
  0x00000001047972a4:   	ldr	x8, [x12, #0x40]
  0x00000001047972a8:   	br	x8
  0x00000001047972ac:   	b	#-0x5142c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1057   63     n       java.lang.invoke.MethodHandle::invokeBasic(I)J (native)
 total in heap  [0x0000000104797410,0x00000001047975b0] = 416
 relocation     [0x0000000104797558,0x0000000104797560] = 8
 main code      [0x0000000104797580,0x00000001047975b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146eb888} 'invokeBasic' '(I)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000104797580:   	nop
  0x0000000104797584:   	ldr	w12, [x1, #0x14]
  0x0000000104797588:   	lsl	x12, x12, #3
  0x000000010479758c:   	ldr	w12, [x12, #0x28]
  0x0000000104797590:   	lsl	x12, x12, #3
  0x0000000104797594:   	ldr	w12, [x12, #0x24]
  0x0000000104797598:   	lsl	x12, x12, #3
  0x000000010479759c:   	ldr	x12, [x12, #0x10]
  0x00000001047975a0:   	cbz	x12, #0xc
  0x00000001047975a4:   	ldr	x8, [x12, #0x40]
  0x00000001047975a8:   	br	x8
  0x00000001047975ac:   	b	#-0x5172c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1058   64     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)J (native)
 total in heap  [0x0000000104797710,0x00000001047978a8] = 408
 relocation     [0x0000000104797858,0x0000000104797860] = 8
 main code      [0x0000000104797880,0x00000001047978a4] = 36
 stub code      [0x00000001047978a4,0x00000001047978a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146eb9a0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104797880:   	nop
  0x0000000104797884:   	ldr	xzr, [x1]
  0x0000000104797888:   	ldr	w12, [x3, #0x24]
  0x000000010479788c:   	lsl	x12, x12, #3
  0x0000000104797890:   	ldr	x12, [x12, #0x10]
  0x0000000104797894:   	cbz	x12, #0xc
  0x0000000104797898:   	ldr	x8, [x12, #0x40]
  0x000000010479789c:   	br	x8
  0x00000001047978a0:   	b	#-0x51a20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047978a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1118   65     n       java.lang.invoke.MethodHandle::linkToStatic(LIL)I (native)
 total in heap  [0x0000000104797a10,0x0000000104797ba0] = 400
 relocation     [0x0000000104797b58,0x0000000104797b60] = 8
 main code      [0x0000000104797b80,0x0000000104797ba0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001146f2058} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000104797b80:   	nop
  0x0000000104797b84:   	ldr	w12, [x3, #0x24]
  0x0000000104797b88:   	lsl	x12, x12, #3
  0x0000000104797b8c:   	ldr	x12, [x12, #0x10]
  0x0000000104797b90:   	cbz	x12, #0xc
  0x0000000104797b94:   	ldr	x8, [x12, #0x40]
  0x0000000104797b98:   	br	x8
  0x0000000104797b9c:   	b	#-0x51d1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1381   66     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)I (native)
 total in heap  [0x000000010479b890,0x000000010479ba28] = 408
 relocation     [0x000000010479b9d8,0x000000010479b9e0] = 8
 main code      [0x000000010479ba00,0x000000010479ba28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b04e68} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479ba00:   	nop
  0x000000010479ba04:   	ldr	w10, [x1, #0x8]
  0x000000010479ba08:   	eor	x10, x10, #0x800000000
  0x000000010479ba0c:   	ldr	x11, [x2, #0x10]
  0x000000010479ba10:   	add	x12, x10, x11, uxtx #3
  0x000000010479ba14:   	ldr	x12, [x12, #0x1c0]
  0x000000010479ba18:   	cbz	x12, #0xc
  0x000000010479ba1c:   	ldr	x8, [x12, #0x40]
  0x000000010479ba20:   	br	x8
  0x000000010479ba24:   	b	#-0x55ba4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1386   67     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLL)V (native)
 total in heap  [0x000000010479bb90,0x000000010479bd28] = 408
 relocation     [0x000000010479bcd8,0x000000010479bce0] = 8
 main code      [0x000000010479bd00,0x000000010479bd24] = 36
 stub code      [0x000000010479bd24,0x000000010479bd28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b05410} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479bd00:   	nop
  0x000000010479bd04:   	ldr	xzr, [x1]
  0x000000010479bd08:   	ldr	w12, [x6, #0x24]
  0x000000010479bd0c:   	lsl	x12, x12, #3
  0x000000010479bd10:   	ldr	x12, [x12, #0x10]
  0x000000010479bd14:   	cbz	x12, #0xc
  0x000000010479bd18:   	ldr	x8, [x12, #0x40]
  0x000000010479bd1c:   	br	x8
  0x000000010479bd20:   	b	#-0x55ea0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479bd24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1387   68     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLL)L (native)
 total in heap  [0x000000010479be90,0x000000010479c028] = 408
 relocation     [0x000000010479bfd8,0x000000010479bfe0] = 8
 main code      [0x000000010479c000,0x000000010479c024] = 36
 stub code      [0x000000010479c024,0x000000010479c028] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b05528} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479c000:   	nop
  0x000000010479c004:   	ldr	xzr, [x1]
  0x000000010479c008:   	ldr	w12, [x6, #0x24]
  0x000000010479c00c:   	lsl	x12, x12, #3
  0x000000010479c010:   	ldr	x12, [x12, #0x10]
  0x000000010479c014:   	cbz	x12, #0xc
  0x000000010479c018:   	ldr	x8, [x12, #0x40]
  0x000000010479c01c:   	br	x8
  0x000000010479c020:   	b	#-0x561a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479c024:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1391   69     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLL)V (native)
 total in heap  [0x000000010479c190,0x000000010479c328] = 408
 relocation     [0x000000010479c2d8,0x000000010479c2e0] = 8
 main code      [0x000000010479c300,0x000000010479c324] = 36
 stub code      [0x000000010479c324,0x000000010479c328] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b05640} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479c300:   	nop
  0x000000010479c304:   	ldr	xzr, [x1]
  0x000000010479c308:   	ldr	w12, [x7, #0x24]
  0x000000010479c30c:   	lsl	x12, x12, #3
  0x000000010479c310:   	ldr	x12, [x12, #0x10]
  0x000000010479c314:   	cbz	x12, #0xc
  0x000000010479c318:   	ldr	x8, [x12, #0x40]
  0x000000010479c31c:   	br	x8
  0x000000010479c320:   	b	#-0x564a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479c324:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1475   70     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)L (native)
 total in heap  [0x000000010479c490,0x000000010479c628] = 408
 relocation     [0x000000010479c5d8,0x000000010479c5e0] = 8
 main code      [0x000000010479c600,0x000000010479c628] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b12a28} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479c600:   	nop
  0x000000010479c604:   	ldr	w10, [x1, #0x8]
  0x000000010479c608:   	eor	x10, x10, #0x800000000
  0x000000010479c60c:   	ldr	x11, [x3, #0x10]
  0x000000010479c610:   	add	x12, x10, x11, uxtx #3
  0x000000010479c614:   	ldr	x12, [x12, #0x1c0]
  0x000000010479c618:   	cbz	x12, #0xc
  0x000000010479c61c:   	ldr	x8, [x12, #0x40]
  0x000000010479c620:   	br	x8
  0x000000010479c624:   	b	#-0x567a4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1477   71     n       java.lang.invoke.MethodHandle::linkToInterface(LL)I (native)
 total in heap  [0x000000010479c790,0x000000010479c968] = 472
 relocation     [0x000000010479c8d8,0x000000010479c8e0] = 8
 main code      [0x000000010479c900,0x000000010479c964] = 100
 stub code      [0x000000010479c964,0x000000010479c968] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b12c00} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479c900:   	nop
  0x000000010479c904:   	ldr	w10, [x1, #0x8]
  0x000000010479c908:   	eor	x10, x10, #0x800000000
  0x000000010479c90c:   	ldr	w14, [x2, #0x18]
  0x000000010479c910:   	lsl	x14, x14, #3
  0x000000010479c914:   	ldr	x14, [x14, #0x10]
  0x000000010479c918:   	ldr	x12, [x2, #0x10]
  0x000000010479c91c:   	ldr	w11, [x10, #0xa0]
  0x000000010479c920:   	add	x11, x10, x11, uxtx #3
  0x000000010479c924:   	add	x11, x11, #0x1c0
  0x000000010479c928:   	add	x10, x10, x12, uxtx #3
  0x000000010479c92c:   	ldr	x12, [x11]
  0x000000010479c930:   	cmp	x14, x12
  0x000000010479c934:   	b.eq	#0x14
  0x000000010479c938:   	cbz	x12, #0x28
  0x000000010479c93c:   	ldr	x12, [x11, #0x10]!
  0x000000010479c940:   	cmp	x14, x12
  0x000000010479c944:   	b.ne	#-0xc
  0x000000010479c948:   	ldr	w11, [x11, #0x8]
  0x000000010479c94c:   	ldr	x12, [x10, w11, uxtw]
  0x000000010479c950:   	cbz	x12, #0xc
  0x000000010479c954:   	ldr	x8, [x12, #0x40]
  0x000000010479c958:   	br	x8
  0x000000010479c95c:   	b	#-0x56adc           ;   {runtime_call AbstractMethodError throw_exception}
  0x000000010479c960:   	b	#-0x753e0           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x000000010479c964:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2276   72     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)V (native)
 total in heap  [0x000000010479d890,0x000000010479da28] = 408
 relocation     [0x000000010479d9d8,0x000000010479d9e0] = 8
 main code      [0x000000010479da00,0x000000010479da28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b64b90} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479da00:   	nop
  0x000000010479da04:   	ldr	w10, [x1, #0x8]
  0x000000010479da08:   	eor	x10, x10, #0x800000000
  0x000000010479da0c:   	ldr	x11, [x3, #0x10]
  0x000000010479da10:   	add	x12, x10, x11, uxtx #3
  0x000000010479da14:   	ldr	x12, [x12, #0x1c0]
  0x000000010479da18:   	cbz	x12, #0xc
  0x000000010479da1c:   	ldr	x8, [x12, #0x40]
  0x000000010479da20:   	br	x8
  0x000000010479da24:   	b	#-0x57ba4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2306   73     n       java.lang.invoke.MethodHandle::linkToVirtual(LLIIL)L (native)
 total in heap  [0x000000010479db90,0x000000010479dd28] = 408
 relocation     [0x000000010479dcd8,0x000000010479dce0] = 8
 main code      [0x000000010479dd00,0x000000010479dd28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b6e2f0} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479dd00:   	nop
  0x000000010479dd04:   	ldr	w10, [x1, #0x8]
  0x000000010479dd08:   	eor	x10, x10, #0x800000000
  0x000000010479dd0c:   	ldr	x11, [x5, #0x10]
  0x000000010479dd10:   	add	x12, x10, x11, uxtx #3
  0x000000010479dd14:   	ldr	x12, [x12, #0x1c0]
  0x000000010479dd18:   	cbz	x12, #0xc
  0x000000010479dd1c:   	ldr	x8, [x12, #0x40]
  0x000000010479dd20:   	br	x8
  0x000000010479dd24:   	b	#-0x57ea4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2309   74     n       java.lang.invoke.MethodHandle::linkToVirtual(LIL)L (native)
 total in heap  [0x000000010479de90,0x000000010479e028] = 408
 relocation     [0x000000010479dfd8,0x000000010479dfe0] = 8
 main code      [0x000000010479e000,0x000000010479e028] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b6e608} 'linkToVirtual' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479e000:   	nop
  0x000000010479e004:   	ldr	w10, [x1, #0x8]
  0x000000010479e008:   	eor	x10, x10, #0x800000000
  0x000000010479e00c:   	ldr	x11, [x3, #0x10]
  0x000000010479e010:   	add	x12, x10, x11, uxtx #3
  0x000000010479e014:   	ldr	x12, [x12, #0x1c0]
  0x000000010479e018:   	cbz	x12, #0xc
  0x000000010479e01c:   	ldr	x8, [x12, #0x40]
  0x000000010479e020:   	br	x8
  0x000000010479e024:   	b	#-0x581a4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2317   75     n       java.lang.invoke.MethodHandle::linkToSpecial(LLIIL)L (native)
 total in heap  [0x000000010479e190,0x000000010479e328] = 408
 relocation     [0x000000010479e2d8,0x000000010479e2e0] = 8
 main code      [0x000000010479e300,0x000000010479e324] = 36
 stub code      [0x000000010479e324,0x000000010479e328] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b6e750} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479e300:   	nop
  0x000000010479e304:   	ldr	xzr, [x1]
  0x000000010479e308:   	ldr	w12, [x5, #0x24]
  0x000000010479e30c:   	lsl	x12, x12, #3
  0x000000010479e310:   	ldr	x12, [x12, #0x10]
  0x000000010479e314:   	cbz	x12, #0xc
  0x000000010479e318:   	ldr	x8, [x12, #0x40]
  0x000000010479e31c:   	br	x8
  0x000000010479e320:   	b	#-0x584a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479e324:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2389   76     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)I (native)
 total in heap  [0x000000010479e490,0x000000010479e628] = 408
 relocation     [0x000000010479e5d8,0x000000010479e5e0] = 8
 main code      [0x000000010479e600,0x000000010479e624] = 36
 stub code      [0x000000010479e624,0x000000010479e628] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b770e0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479e600:   	nop
  0x000000010479e604:   	ldr	xzr, [x1]
  0x000000010479e608:   	ldr	w12, [x5, #0x24]
  0x000000010479e60c:   	lsl	x12, x12, #3
  0x000000010479e610:   	ldr	x12, [x12, #0x10]
  0x000000010479e614:   	cbz	x12, #0xc
  0x000000010479e618:   	ldr	x8, [x12, #0x40]
  0x000000010479e61c:   	br	x8
  0x000000010479e620:   	b	#-0x587a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479e624:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2395   77     n       java.lang.invoke.MethodHandle::linkToStatic(IL)L (native)
 total in heap  [0x000000010479e790,0x000000010479e920] = 400
 relocation     [0x000000010479e8d8,0x000000010479e8e0] = 8
 main code      [0x000000010479e900,0x000000010479e920] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b79598} 'linkToStatic' '(ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479e900:   	nop
  0x000000010479e904:   	ldr	w12, [x2, #0x24]
  0x000000010479e908:   	lsl	x12, x12, #3
  0x000000010479e90c:   	ldr	x12, [x12, #0x10]
  0x000000010479e910:   	cbz	x12, #0xc
  0x000000010479e914:   	ldr	x8, [x12, #0x40]
  0x000000010479e918:   	br	x8
  0x000000010479e91c:   	b	#-0x58a9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2478   78     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)I (native)
 total in heap  [0x000000010479ea90,0x000000010479ec28] = 408
 relocation     [0x000000010479ebd8,0x000000010479ebe0] = 8
 main code      [0x000000010479ec00,0x000000010479ec24] = 36
 stub code      [0x000000010479ec24,0x000000010479ec28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b92f90} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479ec00:   	nop
  0x000000010479ec04:   	ldr	xzr, [x1]
  0x000000010479ec08:   	ldr	w12, [x3, #0x24]
  0x000000010479ec0c:   	lsl	x12, x12, #3
  0x000000010479ec10:   	ldr	x12, [x12, #0x10]
  0x000000010479ec14:   	cbz	x12, #0xc
  0x000000010479ec18:   	ldr	x8, [x12, #0x40]
  0x000000010479ec1c:   	br	x8
  0x000000010479ec20:   	b	#-0x58da0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x000000010479ec24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2570   79     n       java.lang.invoke.MethodHandle::linkToStatic(LIIL)L (native)
 total in heap  [0x000000010479ed90,0x000000010479ef20] = 400
 relocation     [0x000000010479eed8,0x000000010479eee0] = 8
 main code      [0x000000010479ef00,0x000000010479ef20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b9a000} 'linkToStatic' '(Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479ef00:   	nop
  0x000000010479ef04:   	ldr	w12, [x4, #0x24]
  0x000000010479ef08:   	lsl	x12, x12, #3
  0x000000010479ef0c:   	ldr	x12, [x12, #0x10]
  0x000000010479ef10:   	cbz	x12, #0xc
  0x000000010479ef14:   	ldr	x8, [x12, #0x40]
  0x000000010479ef18:   	br	x8
  0x000000010479ef1c:   	b	#-0x5909c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2579   80     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)J (native)
 total in heap  [0x000000010479f090,0x000000010479f220] = 400
 relocation     [0x000000010479f1d8,0x000000010479f1e0] = 8
 main code      [0x000000010479f200,0x000000010479f220] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b9bf18} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479f200:   	nop
  0x000000010479f204:   	ldr	w12, [x4, #0x24]
  0x000000010479f208:   	lsl	x12, x12, #3
  0x000000010479f20c:   	ldr	x12, [x12, #0x10]
  0x000000010479f210:   	cbz	x12, #0xc
  0x000000010479f214:   	ldr	x8, [x12, #0x40]
  0x000000010479f218:   	br	x8
  0x000000010479f21c:   	b	#-0x5939c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2717   81     n       java.lang.invoke.MethodHandle::linkToStatic(L)V (native)
 total in heap  [0x000000010479f710,0x000000010479f8a0] = 400
 relocation     [0x000000010479f858,0x000000010479f860] = 8
 main code      [0x000000010479f880,0x000000010479f8a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114b9e268} 'linkToStatic' '(Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479f880:   	nop
  0x000000010479f884:   	ldr	w12, [x1, #0x24]
  0x000000010479f888:   	lsl	x12, x12, #3
  0x000000010479f88c:   	ldr	x12, [x12, #0x10]
  0x000000010479f890:   	cbz	x12, #0xc
  0x000000010479f894:   	ldr	x8, [x12, #0x40]
  0x000000010479f898:   	br	x8
  0x000000010479f89c:   	b	#-0x59a1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2818   82     n       java.lang.invoke.MethodHandle::linkToStatic(LLLL)I (native)
 total in heap  [0x000000010479fd90,0x000000010479ff20] = 400
 relocation     [0x000000010479fed8,0x000000010479fee0] = 8
 main code      [0x000000010479ff00,0x000000010479ff20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114ba15a0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x000000010479ff00:   	nop
  0x000000010479ff04:   	ldr	w12, [x4, #0x24]
  0x000000010479ff08:   	lsl	x12, x12, #3
  0x000000010479ff0c:   	ldr	x12, [x12, #0x10]
  0x000000010479ff10:   	cbz	x12, #0xc
  0x000000010479ff14:   	ldr	x8, [x12, #0x40]
  0x000000010479ff18:   	br	x8
  0x000000010479ff1c:   	b	#-0x5a09c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2947   83     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLLLL)L (native)
 total in heap  [0x00000001047a0410,0x00000001047a05b0] = 416
 relocation     [0x00000001047a0558,0x00000001047a0560] = 8
 main code      [0x00000001047a0580,0x00000001047a05b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114ba7748} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm6:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  # parm7:    [sp+0x0]   = 'java/lang/Object'  (sp of caller)
  0x00000001047a0580:   	nop
  0x00000001047a0584:   	ldr	w12, [x1, #0x14]
  0x00000001047a0588:   	lsl	x12, x12, #3
  0x00000001047a058c:   	ldr	w12, [x12, #0x28]
  0x00000001047a0590:   	lsl	x12, x12, #3
  0x00000001047a0594:   	ldr	w12, [x12, #0x24]
  0x00000001047a0598:   	lsl	x12, x12, #3
  0x00000001047a059c:   	ldr	x12, [x12, #0x10]
  0x00000001047a05a0:   	cbz	x12, #0xc
  0x00000001047a05a4:   	ldr	x8, [x12, #0x40]
  0x00000001047a05a8:   	br	x8
  0x00000001047a05ac:   	b	#-0x5a72c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2949   84     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLLLLL)L (native)
 total in heap  [0x00000001047a0710,0x00000001047a08a8] = 408
 relocation     [0x00000001047a0858,0x00000001047a0860] = 8
 main code      [0x00000001047a0880,0x00000001047a08a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114ba7860} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  # parm8:    [sp+0x0]   = 'java/lang/Object'  (sp of caller)
  # parm9:    [sp+0x8]   = 'java/lang/invoke/MemberName'
  0x00000001047a0880:   	nop
  0x00000001047a0884:   	ldr	x19, [sp, #0x8]
  0x00000001047a0888:   	ldr	xzr, [x1]
  0x00000001047a088c:   	ldr	w12, [x19, #0x24]
  0x00000001047a0890:   	lsl	x12, x12, #3
  0x00000001047a0894:   	ldr	x12, [x12, #0x10]
  0x00000001047a0898:   	cbz	x12, #0xc
  0x00000001047a089c:   	ldr	x8, [x12, #0x40]
  0x00000001047a08a0:   	br	x8
  0x00000001047a08a4:   	b	#-0x5aa24           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2958   85     n       java.lang.invoke.MethodHandle::linkToStatic(LIL)L (native)
 total in heap  [0x00000001047a0a10,0x00000001047a0ba0] = 400
 relocation     [0x00000001047a0b58,0x00000001047a0b60] = 8
 main code      [0x00000001047a0b80,0x00000001047a0ba0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bae628} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a0b80:   	nop
  0x00000001047a0b84:   	ldr	w12, [x3, #0x24]
  0x00000001047a0b88:   	lsl	x12, x12, #3
  0x00000001047a0b8c:   	ldr	x12, [x12, #0x10]
  0x00000001047a0b90:   	cbz	x12, #0xc
  0x00000001047a0b94:   	ldr	x8, [x12, #0x40]
  0x00000001047a0b98:   	br	x8
  0x00000001047a0b9c:   	b	#-0x5ad1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2959   86     n       java.lang.invoke.MethodHandle::invokeBasic(LI)L (native)
 total in heap  [0x00000001047a0d10,0x00000001047a0eb0] = 416
 relocation     [0x00000001047a0e58,0x00000001047a0e60] = 8
 main code      [0x00000001047a0e80,0x00000001047a0eb0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bae770} 'invokeBasic' '(Ljava/lang/Object;I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x00000001047a0e80:   	nop
  0x00000001047a0e84:   	ldr	w12, [x1, #0x14]
  0x00000001047a0e88:   	lsl	x12, x12, #3
  0x00000001047a0e8c:   	ldr	w12, [x12, #0x28]
  0x00000001047a0e90:   	lsl	x12, x12, #3
  0x00000001047a0e94:   	ldr	w12, [x12, #0x24]
  0x00000001047a0e98:   	lsl	x12, x12, #3
  0x00000001047a0e9c:   	ldr	x12, [x12, #0x10]
  0x00000001047a0ea0:   	cbz	x12, #0xc
  0x00000001047a0ea4:   	ldr	x8, [x12, #0x40]
  0x00000001047a0ea8:   	br	x8
  0x00000001047a0eac:   	b	#-0x5b02c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2960   87     n       java.lang.invoke.MethodHandle::linkToSpecial(LLIL)L (native)
 total in heap  [0x00000001047a1010,0x00000001047a11a8] = 408
 relocation     [0x00000001047a1158,0x00000001047a1160] = 8
 main code      [0x00000001047a1180,0x00000001047a11a4] = 36
 stub code      [0x00000001047a11a4,0x00000001047a11a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114baec38} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a1180:   	nop
  0x00000001047a1184:   	ldr	xzr, [x1]
  0x00000001047a1188:   	ldr	w12, [x4, #0x24]
  0x00000001047a118c:   	lsl	x12, x12, #3
  0x00000001047a1190:   	ldr	x12, [x12, #0x10]
  0x00000001047a1194:   	cbz	x12, #0xc
  0x00000001047a1198:   	ldr	x8, [x12, #0x40]
  0x00000001047a119c:   	br	x8
  0x00000001047a11a0:   	b	#-0x5b320           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a11a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2964   88     n       java.lang.invoke.MethodHandle::linkToStatic(LILL)V (native)
 total in heap  [0x00000001047a1310,0x00000001047a14a0] = 400
 relocation     [0x00000001047a1458,0x00000001047a1460] = 8
 main code      [0x00000001047a1480,0x00000001047a14a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bb3ca8} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a1480:   	nop
  0x00000001047a1484:   	ldr	w12, [x4, #0x24]
  0x00000001047a1488:   	lsl	x12, x12, #3
  0x00000001047a148c:   	ldr	x12, [x12, #0x10]
  0x00000001047a1490:   	cbz	x12, #0xc
  0x00000001047a1494:   	ldr	x8, [x12, #0x40]
  0x00000001047a1498:   	br	x8
  0x00000001047a149c:   	b	#-0x5b61c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2966   89     n       java.lang.invoke.MethodHandle::invokeBasic(LIL)V (native)
 total in heap  [0x00000001047a1610,0x00000001047a17b0] = 416
 relocation     [0x00000001047a1758,0x00000001047a1760] = 8
 main code      [0x00000001047a1780,0x00000001047a17b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bb3f28} 'invokeBasic' '(Ljava/lang/Object;ILjava/lang/Object;)V' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3   = int
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a1780:   	nop
  0x00000001047a1784:   	ldr	w12, [x1, #0x14]
  0x00000001047a1788:   	lsl	x12, x12, #3
  0x00000001047a178c:   	ldr	w12, [x12, #0x28]
  0x00000001047a1790:   	lsl	x12, x12, #3
  0x00000001047a1794:   	ldr	w12, [x12, #0x24]
  0x00000001047a1798:   	lsl	x12, x12, #3
  0x00000001047a179c:   	ldr	x12, [x12, #0x10]
  0x00000001047a17a0:   	cbz	x12, #0xc
  0x00000001047a17a4:   	ldr	x8, [x12, #0x40]
  0x00000001047a17a8:   	br	x8
  0x00000001047a17ac:   	b	#-0x5b92c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2966   90     n       java.lang.invoke.MethodHandle::linkToSpecial(LLILL)V (native)
 total in heap  [0x00000001047a1910,0x00000001047a1aa8] = 408
 relocation     [0x00000001047a1a58,0x00000001047a1a60] = 8
 main code      [0x00000001047a1a80,0x00000001047a1aa4] = 36
 stub code      [0x00000001047a1aa4,0x00000001047a1aa8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bb4040} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a1a80:   	nop
  0x00000001047a1a84:   	ldr	xzr, [x1]
  0x00000001047a1a88:   	ldr	w12, [x5, #0x24]
  0x00000001047a1a8c:   	lsl	x12, x12, #3
  0x00000001047a1a90:   	ldr	x12, [x12, #0x10]
  0x00000001047a1a94:   	cbz	x12, #0xc
  0x00000001047a1a98:   	ldr	x8, [x12, #0x40]
  0x00000001047a1a9c:   	br	x8
  0x00000001047a1aa0:   	b	#-0x5bc20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a1aa4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2968   91     n       java.lang.invoke.MethodHandle::invokeBasic(L)I (native)
 total in heap  [0x00000001047a1c10,0x00000001047a1db0] = 416
 relocation     [0x00000001047a1d58,0x00000001047a1d60] = 8
 main code      [0x00000001047a1d80,0x00000001047a1db0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bb4158} 'invokeBasic' '(Ljava/lang/Object;)I' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a1d80:   	nop
  0x00000001047a1d84:   	ldr	w12, [x1, #0x14]
  0x00000001047a1d88:   	lsl	x12, x12, #3
  0x00000001047a1d8c:   	ldr	w12, [x12, #0x28]
  0x00000001047a1d90:   	lsl	x12, x12, #3
  0x00000001047a1d94:   	ldr	w12, [x12, #0x24]
  0x00000001047a1d98:   	lsl	x12, x12, #3
  0x00000001047a1d9c:   	ldr	x12, [x12, #0x10]
  0x00000001047a1da0:   	cbz	x12, #0xc
  0x00000001047a1da4:   	ldr	x8, [x12, #0x40]
  0x00000001047a1da8:   	br	x8
  0x00000001047a1dac:   	b	#-0x5bf2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2976   92     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLLLL)L (native)
 total in heap  [0x00000001047a2290,0x00000001047a2420] = 400
 relocation     [0x00000001047a23d8,0x00000001047a23e0] = 8
 main code      [0x00000001047a2400,0x00000001047a2420] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc5758} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a2400:   	nop
  0x00000001047a2404:   	ldr	w12, [x0, #0x24]
  0x00000001047a2408:   	lsl	x12, x12, #3
  0x00000001047a240c:   	ldr	x12, [x12, #0x10]
  0x00000001047a2410:   	cbz	x12, #0xc
  0x00000001047a2414:   	ldr	x8, [x12, #0x40]
  0x00000001047a2418:   	br	x8
  0x00000001047a241c:   	b	#-0x5c59c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2978   93     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLLL)L (native)
 total in heap  [0x00000001047a2590,0x00000001047a2730] = 416
 relocation     [0x00000001047a26d8,0x00000001047a26e0] = 8
 main code      [0x00000001047a2700,0x00000001047a2730] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc58a0} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm6:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a2700:   	nop
  0x00000001047a2704:   	ldr	w12, [x1, #0x14]
  0x00000001047a2708:   	lsl	x12, x12, #3
  0x00000001047a270c:   	ldr	w12, [x12, #0x28]
  0x00000001047a2710:   	lsl	x12, x12, #3
  0x00000001047a2714:   	ldr	w12, [x12, #0x24]
  0x00000001047a2718:   	lsl	x12, x12, #3
  0x00000001047a271c:   	ldr	x12, [x12, #0x10]
  0x00000001047a2720:   	cbz	x12, #0xc
  0x00000001047a2724:   	ldr	x8, [x12, #0x40]
  0x00000001047a2728:   	br	x8
  0x00000001047a272c:   	b	#-0x5c8ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2982   94     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)L (native)
 total in heap  [0x00000001047a2890,0x00000001047a2a20] = 400
 relocation     [0x00000001047a29d8,0x00000001047a29e0] = 8
 main code      [0x00000001047a2a00,0x00000001047a2a20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc7e28} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a2a00:   	nop
  0x00000001047a2a04:   	ldr	w12, [x4, #0x24]
  0x00000001047a2a08:   	lsl	x12, x12, #3
  0x00000001047a2a0c:   	ldr	x12, [x12, #0x10]
  0x00000001047a2a10:   	cbz	x12, #0xc
  0x00000001047a2a14:   	ldr	x8, [x12, #0x40]
  0x00000001047a2a18:   	br	x8
  0x00000001047a2a1c:   	b	#-0x5cb9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2983   95     n       java.lang.invoke.MethodHandle::invokeBasic(LLI)L (native)
 total in heap  [0x00000001047a2b90,0x00000001047a2d30] = 416
 relocation     [0x00000001047a2cd8,0x00000001047a2ce0] = 8
 main code      [0x00000001047a2d00,0x00000001047a2d30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc7f70} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  #           [sp+0x0]  (sp of caller)
  0x00000001047a2d00:   	nop
  0x00000001047a2d04:   	ldr	w12, [x1, #0x14]
  0x00000001047a2d08:   	lsl	x12, x12, #3
  0x00000001047a2d0c:   	ldr	w12, [x12, #0x28]
  0x00000001047a2d10:   	lsl	x12, x12, #3
  0x00000001047a2d14:   	ldr	w12, [x12, #0x24]
  0x00000001047a2d18:   	lsl	x12, x12, #3
  0x00000001047a2d1c:   	ldr	x12, [x12, #0x10]
  0x00000001047a2d20:   	cbz	x12, #0xc
  0x00000001047a2d24:   	ldr	x8, [x12, #0x40]
  0x00000001047a2d28:   	br	x8
  0x00000001047a2d2c:   	b	#-0x5ceac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2985   96     n       java.lang.invoke.MethodHandle::linkToStatic(JJL)I (native)
 total in heap  [0x00000001047a2e90,0x00000001047a3020] = 400
 relocation     [0x00000001047a2fd8,0x00000001047a2fe0] = 8
 main code      [0x00000001047a3000,0x00000001047a3020] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc8b10} 'linkToStatic' '(JJLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a3000:   	nop
  0x00000001047a3004:   	ldr	w12, [x3, #0x24]
  0x00000001047a3008:   	lsl	x12, x12, #3
  0x00000001047a300c:   	ldr	x12, [x12, #0x10]
  0x00000001047a3010:   	cbz	x12, #0xc
  0x00000001047a3014:   	ldr	x8, [x12, #0x40]
  0x00000001047a3018:   	br	x8
  0x00000001047a301c:   	b	#-0x5d19c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2987   97     n       java.lang.invoke.MethodHandle::linkToStatic(FFL)I (native)
 total in heap  [0x00000001047a3510,0x00000001047a36a0] = 400
 relocation     [0x00000001047a3658,0x00000001047a3660] = 8
 main code      [0x00000001047a3680,0x00000001047a36a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc8eb0} 'linkToStatic' '(FFLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    v1        = float
  # parm2:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a3680:   	nop
  0x00000001047a3684:   	ldr	w12, [x1, #0x24]
  0x00000001047a3688:   	lsl	x12, x12, #3
  0x00000001047a368c:   	ldr	x12, [x12, #0x10]
  0x00000001047a3690:   	cbz	x12, #0xc
  0x00000001047a3694:   	ldr	x8, [x12, #0x40]
  0x00000001047a3698:   	br	x8
  0x00000001047a369c:   	b	#-0x5d81c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2988   98     n       java.lang.invoke.MethodHandle::linkToStatic(DDL)I (native)
 total in heap  [0x00000001047a3f10,0x00000001047a40a0] = 400
 relocation     [0x00000001047a4058,0x00000001047a4060] = 8
 main code      [0x00000001047a4080,0x00000001047a40a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc9258} 'linkToStatic' '(DDLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    v1:v1     = double
  # parm2:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a4080:   	nop
  0x00000001047a4084:   	ldr	w12, [x1, #0x24]
  0x00000001047a4088:   	lsl	x12, x12, #3
  0x00000001047a408c:   	ldr	x12, [x12, #0x10]
  0x00000001047a4090:   	cbz	x12, #0xc
  0x00000001047a4094:   	ldr	x8, [x12, #0x40]
  0x00000001047a4098:   	br	x8
  0x00000001047a409c:   	b	#-0x5e21c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2989   99     n       java.lang.invoke.MethodHandle::linkToStatic(JL)I (native)
 total in heap  [0x00000001047a4590,0x00000001047a4720] = 400
 relocation     [0x00000001047a46d8,0x00000001047a46e0] = 8
 main code      [0x00000001047a4700,0x00000001047a4720] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc9370} 'linkToStatic' '(JLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a4700:   	nop
  0x00000001047a4704:   	ldr	w12, [x2, #0x24]
  0x00000001047a4708:   	lsl	x12, x12, #3
  0x00000001047a470c:   	ldr	x12, [x12, #0x10]
  0x00000001047a4710:   	cbz	x12, #0xc
  0x00000001047a4714:   	ldr	x8, [x12, #0x40]
  0x00000001047a4718:   	br	x8
  0x00000001047a471c:   	b	#-0x5e89c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2990  100     n       java.lang.invoke.MethodHandle::linkToStatic(FL)I (native)
 total in heap  [0x00000001047a4c10,0x00000001047a4da0] = 400
 relocation     [0x00000001047a4d58,0x00000001047a4d60] = 8
 main code      [0x00000001047a4d80,0x00000001047a4da0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc9488} 'linkToStatic' '(FLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a4d80:   	nop
  0x00000001047a4d84:   	ldr	w12, [x1, #0x24]
  0x00000001047a4d88:   	lsl	x12, x12, #3
  0x00000001047a4d8c:   	ldr	x12, [x12, #0x10]
  0x00000001047a4d90:   	cbz	x12, #0xc
  0x00000001047a4d94:   	ldr	x8, [x12, #0x40]
  0x00000001047a4d98:   	br	x8
  0x00000001047a4d9c:   	b	#-0x5ef1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2991  101     n       java.lang.invoke.MethodHandle::linkToStatic(DL)I (native)
 total in heap  [0x00000001047a4f10,0x00000001047a50a0] = 400
 relocation     [0x00000001047a5058,0x00000001047a5060] = 8
 main code      [0x00000001047a5080,0x00000001047a50a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc95a0} 'linkToStatic' '(DLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5080:   	nop
  0x00000001047a5084:   	ldr	w12, [x1, #0x24]
  0x00000001047a5088:   	lsl	x12, x12, #3
  0x00000001047a508c:   	ldr	x12, [x12, #0x10]
  0x00000001047a5090:   	cbz	x12, #0xc
  0x00000001047a5094:   	ldr	x8, [x12, #0x40]
  0x00000001047a5098:   	br	x8
  0x00000001047a509c:   	b	#-0x5f21c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2992  102     n       java.lang.invoke.MethodHandle::linkToStatic(FL)L (native)
 total in heap  [0x00000001047a5210,0x00000001047a53a0] = 400
 relocation     [0x00000001047a5358,0x00000001047a5360] = 8
 main code      [0x00000001047a5380,0x00000001047a53a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc96b8} 'linkToStatic' '(FLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5380:   	nop
  0x00000001047a5384:   	ldr	w12, [x1, #0x24]
  0x00000001047a5388:   	lsl	x12, x12, #3
  0x00000001047a538c:   	ldr	x12, [x12, #0x10]
  0x00000001047a5390:   	cbz	x12, #0xc
  0x00000001047a5394:   	ldr	x8, [x12, #0x40]
  0x00000001047a5398:   	br	x8
  0x00000001047a539c:   	b	#-0x5f51c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2993  103     n       java.lang.invoke.MethodHandle::linkToStatic(DL)L (native)
 total in heap  [0x00000001047a5510,0x00000001047a56a0] = 400
 relocation     [0x00000001047a5658,0x00000001047a5660] = 8
 main code      [0x00000001047a5680,0x00000001047a56a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bc97d0} 'linkToStatic' '(DLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5680:   	nop
  0x00000001047a5684:   	ldr	w12, [x1, #0x24]
  0x00000001047a5688:   	lsl	x12, x12, #3
  0x00000001047a568c:   	ldr	x12, [x12, #0x10]
  0x00000001047a5690:   	cbz	x12, #0xc
  0x00000001047a5694:   	ldr	x8, [x12, #0x40]
  0x00000001047a5698:   	br	x8
  0x00000001047a569c:   	b	#-0x5f81c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2995  104     n       java.lang.invoke.MethodHandle::invokeBasic(II)I (native)
 total in heap  [0x00000001047a5810,0x00000001047a59b0] = 416
 relocation     [0x00000001047a5958,0x00000001047a5960] = 8
 main code      [0x00000001047a5980,0x00000001047a59b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bca590} 'invokeBasic' '(II)I' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5980:   	nop
  0x00000001047a5984:   	ldr	w12, [x1, #0x14]
  0x00000001047a5988:   	lsl	x12, x12, #3
  0x00000001047a598c:   	ldr	w12, [x12, #0x28]
  0x00000001047a5990:   	lsl	x12, x12, #3
  0x00000001047a5994:   	ldr	w12, [x12, #0x24]
  0x00000001047a5998:   	lsl	x12, x12, #3
  0x00000001047a599c:   	ldr	x12, [x12, #0x10]
  0x00000001047a59a0:   	cbz	x12, #0xc
  0x00000001047a59a4:   	ldr	x8, [x12, #0x40]
  0x00000001047a59a8:   	br	x8
  0x00000001047a59ac:   	b	#-0x5fb2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2996  105     n       java.lang.invoke.MethodHandle::linkToSpecial(LIIL)I (native)
 total in heap  [0x00000001047a5b10,0x00000001047a5ca8] = 408
 relocation     [0x00000001047a5c58,0x00000001047a5c60] = 8
 main code      [0x00000001047a5c80,0x00000001047a5ca4] = 36
 stub code      [0x00000001047a5ca4,0x00000001047a5ca8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bca6a8} 'linkToSpecial' '(Ljava/lang/Object;IILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5c80:   	nop
  0x00000001047a5c84:   	ldr	xzr, [x1]
  0x00000001047a5c88:   	ldr	w12, [x4, #0x24]
  0x00000001047a5c8c:   	lsl	x12, x12, #3
  0x00000001047a5c90:   	ldr	x12, [x12, #0x10]
  0x00000001047a5c94:   	cbz	x12, #0xc
  0x00000001047a5c98:   	ldr	x8, [x12, #0x40]
  0x00000001047a5c9c:   	br	x8
  0x00000001047a5ca0:   	b	#-0x5fe20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a5ca4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3009  106     n       java.lang.invoke.MethodHandle::invokeBasic(L)V (native)
 total in heap  [0x00000001047a5e10,0x00000001047a5fb0] = 416
 relocation     [0x00000001047a5f58,0x00000001047a5f60] = 8
 main code      [0x00000001047a5f80,0x00000001047a5fb0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bce808} 'invokeBasic' '(Ljava/lang/Object;)V' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a5f80:   	nop
  0x00000001047a5f84:   	ldr	w12, [x1, #0x14]
  0x00000001047a5f88:   	lsl	x12, x12, #3
  0x00000001047a5f8c:   	ldr	w12, [x12, #0x28]
  0x00000001047a5f90:   	lsl	x12, x12, #3
  0x00000001047a5f94:   	ldr	w12, [x12, #0x24]
  0x00000001047a5f98:   	lsl	x12, x12, #3
  0x00000001047a5f9c:   	ldr	x12, [x12, #0x10]
  0x00000001047a5fa0:   	cbz	x12, #0xc
  0x00000001047a5fa4:   	ldr	x8, [x12, #0x40]
  0x00000001047a5fa8:   	br	x8
  0x00000001047a5fac:   	b	#-0x6012c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    3015  107             Main::logfn (5 bytes)
 total in heap  [0x00000001047aa910,0x00000001047aab78] = 616
 relocation     [0x00000001047aaa58,0x00000001047aaa70] = 24
 main code      [0x00000001047aaa80,0x00000001047aab00] = 128
 stub code      [0x00000001047aab00,0x00000001047aab10] = 16
 oops           [0x00000001047aab10,0x00000001047aab18] = 8
 metadata       [0x00000001047aab18,0x00000001047aab20] = 8
 scopes data    [0x00000001047aab20,0x00000001047aab30] = 16
 scopes pcs     [0x00000001047aab30,0x00000001047aab70] = 64
 dependencies   [0x00000001047aab70,0x00000001047aab78] = 8

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114e90658} 'logfn' '(D)D' in 'Main'
  # parm0:    v0:v0     = double
  #           [sp+0x20]  (sp of caller)
  0x00000001047aaa80:   	nop
  0x00000001047aaa84:   	sub	sp, sp, #0x20
  0x00000001047aaa88:   	stp	x29, x30, [sp, #0x10]
  0x00000001047aaa8c:   	ldr	w8, #0x70
  0x00000001047aaa90:   	ldr	w9, [x28, #0x20]
  0x00000001047aaa94:   	cmp	x8, x9
  0x00000001047aaa98:   	b.ne	#0x50               ;*synchronization entry
                                                            ; - Main::logfn@-1 (line 24)
  0x00000001047aaa9c:   	adr	x9, #0x18
  0x00000001047aaaa0:   	mov	x8, #0x6a0c         ;   {runtime_call _ZN13SharedRuntime4dlogEd}
  0x00000001047aaaa4:   	movk	x8, #0x3f9, lsl #16
  0x00000001047aaaa8:   	movk	x8, #0x1, lsl #32
  0x00000001047aaaac:   	stp	xzr, x9, [sp, #-0x10]!
  0x00000001047aaab0:   	blr	x8
  0x00000001047aaab4:   	nop                         ;   {other}
  0x00000001047aaab8:   	movk	xzr, #0x0
  0x00000001047aaabc:   	movk	xzr, #0x0
  0x00000001047aaac0:   	add	sp, sp, #0x10       ;*invokestatic log {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::logfn@1 (line 24)
  0x00000001047aaac4:   	ldp	x29, x30, [sp, #0x10]
  0x00000001047aaac8:   	add	sp, sp, #0x20
  0x00000001047aaacc:   	ldr	x8, [x28, #0x378]   ;   {poll_return}
  0x00000001047aaad0:   	cmp	sp, x8
  0x00000001047aaad4:   	b.hi	#0x8
  0x00000001047aaad8:   	ret
  0x00000001047aaadc:   	adr	x8, #-0x10          ;   {internal_word}
  0x00000001047aaae0:   	str	x8, [x28, #0x390]
  0x00000001047aaae4:   	b	#-0x63c64           ;   {runtime_call SafepointBlob}
  0x00000001047aaae8:   	mov	x8, #0xb400
  0x00000001047aaaec:   	movk	x8, #0x472, lsl #16
  0x00000001047aaaf0:   	movk	x8, #0x1, lsl #32
  0x00000001047aaaf4:   	blr	x8
  0x00000001047aaaf8:   	b	#-0x5c
  0x00000001047aaafc:   	udf	#0x1                ;   {other}
[Exception Handler]
  0x00000001047aab00:   	b	#-0x3f980           ;   {no_reloc}
[Deopt Handler Code]
  0x00000001047aab04:   	adr	x30, #0x0
  0x00000001047aab08:   	b	#-0x63948           ;   {runtime_call DeoptimizationBlob}
  0x00000001047aab0c:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    3017  108 %           Main::compute @ 2 (20 bytes)
 total in heap  [0x00000001047a8a90,0x00000001047a8e40] = 944
 relocation     [0x00000001047a8bd8,0x00000001047a8c00] = 40
 main code      [0x00000001047a8c00,0x00000001047a8ce0] = 224
 stub code      [0x00000001047a8ce0,0x00000001047a8d10] = 48
 oops           [0x00000001047a8d10,0x00000001047a8d18] = 8
 metadata       [0x00000001047a8d18,0x00000001047a8d28] = 16
 scopes data    [0x00000001047a8d28,0x00000001047a8d70] = 72
 scopes pcs     [0x00000001047a8d70,0x00000001047a8e20] = 176
 dependencies   [0x00000001047a8e20,0x00000001047a8e28] = 8
 handler table  [0x00000001047a8e28,0x00000001047a8e40] = 24

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114e905a8} 'compute' '(D)V' in 'Main'
  0x00000001047a8c00:   	brk	#0
  0x00000001047a8c04:   	nop
  0x00000001047a8c08:   	nop
  0x00000001047a8c0c:   	nop
  0x00000001047a8c10:   	nop
  0x00000001047a8c14:   	sub	x9, sp, #0x14, lsl #12
  0x00000001047a8c18:   	str	xzr, [x9]
  0x00000001047a8c1c:   	sub	sp, sp, #0x30
  0x00000001047a8c20:   	stp	x29, x30, [sp, #0x20]
  0x00000001047a8c24:   	ldr	w8, #0xb4
  0x00000001047a8c28:   	ldr	w9, [x28, #0x20]
  0x00000001047a8c2c:   	cmp	x8, x9
  0x00000001047a8c30:   	b.ne	#0x94
  0x00000001047a8c34:   	ldr	d16, [x1, #0x8]
  0x00000001047a8c38:   	str	d16, [sp]
  0x00000001047a8c3c:   	ldr	w29, [x1]
  0x00000001047a8c40:   	mov	x0, x1
  0x00000001047a8c44:   	adr	x9, #0x18
  0x00000001047a8c48:   	mov	x8, #0x5080         ;   {runtime_call _ZN13SharedRuntime17OSR_migration_endEPl}
  0x00000001047a8c4c:   	movk	x8, #0x3f9, lsl #16
  0x00000001047a8c50:   	movk	x8, #0x1, lsl #32
  0x00000001047a8c54:   	stp	xzr, x9, [sp, #-0x10]!
  0x00000001047a8c58:   	blr	x8
  0x00000001047a8c5c:   	nop                         ;   {other}
  0x00000001047a8c60:   	movk	xzr, #0x0
  0x00000001047a8c64:   	movk	xzr, #0x0
  0x00000001047a8c68:   	add	sp, sp, #0x10       ;*goto {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@16 (line 17)
  0x00000001047a8c6c:   	b	#0x24               ;*iload_2 {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@2 (line 17)
  0x00000001047a8c70:   	ldr	d0, [sp]
  0x00000001047a8c74:   	bl	#0x1e0c             ; ImmutableOopMap {}
                                                            ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
                                                            ;   {static_call}
  0x00000001047a8c78:   	nop                         ;   {other}
  0x00000001047a8c7c:   	movk	xzr, #0x1e8
  0x00000001047a8c80:   	movk	xzr, #0x0           ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
  0x00000001047a8c84:   	ldr	x10, [x28, #0x380]
  0x00000001047a8c88:   	add	w29, w29, #0x1      ; ImmutableOopMap {}
                                                            ;*goto {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::compute@16 (line 17)
  0x00000001047a8c8c:   	ldr	wzr, [x10]          ;*iload_2 {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@2 (line 17)
                                                            ;   {poll}
  0x00000001047a8c90:   	mov	w8, #0x9680
  0x00000001047a8c94:   	movk	w8, #0x98, lsl #16
  0x00000001047a8c98:   	cmp	w29, w8
  0x00000001047a8c9c:   	b.lt	#-0x2c              ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@5 (line 17)
  0x00000001047a8ca0:   	mov	w1, #-0xbb
  0x00000001047a8ca4:   	bl	#-0x617a4           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::compute@5 (line 17)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x00000001047a8ca8:   	nop                         ;   {other}
  0x00000001047a8cac:   	movk	xzr, #0x218
  0x00000001047a8cb0:   	movk	xzr, #0x200         ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
  0x00000001047a8cb4:   	mov	x1, x0
  0x00000001047a8cb8:   	ldp	x29, x30, [sp, #0x20]
  0x00000001047a8cbc:   	add	sp, sp, #0x30
  0x00000001047a8cc0:   	b	#-0x389c0           ;   {runtime_call _rethrow_Java}
  0x00000001047a8cc4:   	mov	x8, #0xb400
  0x00000001047a8cc8:   	movk	x8, #0x472, lsl #16
  0x00000001047a8ccc:   	movk	x8, #0x1, lsl #32
  0x00000001047a8cd0:   	blr	x8
  0x00000001047a8cd4:   	b	#-0xa0
  0x00000001047a8cd8:   	udf	#0x1                ;   {other}
  0x00000001047a8cdc:   	udf	#0x0
[Exception Handler]
  0x00000001047a8ce0:   	b	#-0x3db60           ;   {no_reloc}
[Deopt Handler Code]
  0x00000001047a8ce4:   	adr	x30, #0x0
  0x00000001047a8ce8:   	b	#-0x61b28           ;   {runtime_call DeoptimizationBlob}
  0x00000001047a8cec:   	isb                         ;   {static_stub}
  0x00000001047a8cf0:   	mov	x12, #0x0           ;   {metadata(nullptr)}
  0x00000001047a8cf4:   	movk	x12, #0x0, lsl #16
  0x00000001047a8cf8:   	movk	x12, #0x0, lsl #32
  0x00000001047a8cfc:   	mov	x8, #0x0
  0x00000001047a8d00:   	movk	x8, #0x0, lsl #16
  0x00000001047a8d04:   	movk	x8, #0x0, lsl #32
  0x00000001047a8d08:   	br	x8
  0x00000001047a8d0c:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3523  109     n       java.lang.invoke.MethodHandle::linkToStatic(JLJLL)J (native)
 total in heap  [0x00000001047a8410,0x00000001047a85a0] = 400
 relocation     [0x00000001047a8558,0x00000001047a8560] = 8
 main code      [0x00000001047a8580,0x00000001047a85a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd61b8} 'linkToStatic' '(JLjava/lang/Object;JLjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a8580:   	nop
  0x00000001047a8584:   	ldr	w12, [x5, #0x24]
  0x00000001047a8588:   	lsl	x12, x12, #3
  0x00000001047a858c:   	ldr	x12, [x12, #0x10]
  0x00000001047a8590:   	cbz	x12, #0xc
  0x00000001047a8594:   	ldr	x8, [x12, #0x40]
  0x00000001047a8598:   	br	x8
  0x00000001047a859c:   	b	#-0x6271c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3525  110     n       java.lang.invoke.MethodHandle::invokeBasic(JLJL)J (native)
 total in heap  [0x00000001047a8110,0x00000001047a82b0] = 416
 relocation     [0x00000001047a8258,0x00000001047a8260] = 8
 main code      [0x00000001047a8280,0x00000001047a82b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd62d0} 'invokeBasic' '(JLjava/lang/Object;JLjava/lang/Object;)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = long
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a8280:   	nop
  0x00000001047a8284:   	ldr	w12, [x1, #0x14]
  0x00000001047a8288:   	lsl	x12, x12, #3
  0x00000001047a828c:   	ldr	w12, [x12, #0x28]
  0x00000001047a8290:   	lsl	x12, x12, #3
  0x00000001047a8294:   	ldr	w12, [x12, #0x24]
  0x00000001047a8298:   	lsl	x12, x12, #3
  0x00000001047a829c:   	ldr	x12, [x12, #0x10]
  0x00000001047a82a0:   	cbz	x12, #0xc
  0x00000001047a82a4:   	ldr	x8, [x12, #0x40]
  0x00000001047a82a8:   	br	x8
  0x00000001047a82ac:   	b	#-0x6242c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3525  111     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLJLL)J (native)
 total in heap  [0x00000001047a7a90,0x00000001047a7c28] = 408
 relocation     [0x00000001047a7bd8,0x00000001047a7be0] = 8
 main code      [0x00000001047a7c00,0x00000001047a7c24] = 36
 stub code      [0x00000001047a7c24,0x00000001047a7c28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd63e8} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;JLjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a7c00:   	nop
  0x00000001047a7c04:   	ldr	xzr, [x1]
  0x00000001047a7c08:   	ldr	w12, [x6, #0x24]
  0x00000001047a7c0c:   	lsl	x12, x12, #3
  0x00000001047a7c10:   	ldr	x12, [x12, #0x10]
  0x00000001047a7c14:   	cbz	x12, #0xc
  0x00000001047a7c18:   	ldr	x8, [x12, #0x40]
  0x00000001047a7c1c:   	br	x8
  0x00000001047a7c20:   	b	#-0x61da0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a7c24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3527  112     n       java.lang.invoke.MethodHandle::invokeBasic(JLJ)J (native)
 total in heap  [0x00000001047aa610,0x00000001047aa7b0] = 416
 relocation     [0x00000001047aa758,0x00000001047aa760] = 8
 main code      [0x00000001047aa780,0x00000001047aa7b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6500} 'invokeBasic' '(JLjava/lang/Object;J)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x00000001047aa780:   	nop
  0x00000001047aa784:   	ldr	w12, [x1, #0x14]
  0x00000001047aa788:   	lsl	x12, x12, #3
  0x00000001047aa78c:   	ldr	w12, [x12, #0x28]
  0x00000001047aa790:   	lsl	x12, x12, #3
  0x00000001047aa794:   	ldr	w12, [x12, #0x24]
  0x00000001047aa798:   	lsl	x12, x12, #3
  0x00000001047aa79c:   	ldr	x12, [x12, #0x10]
  0x00000001047aa7a0:   	cbz	x12, #0xc
  0x00000001047aa7a4:   	ldr	x8, [x12, #0x40]
  0x00000001047aa7a8:   	br	x8
  0x00000001047aa7ac:   	b	#-0x6492c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3528  113     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLJL)J (native)
 total in heap  [0x00000001047aa310,0x00000001047aa4a8] = 408
 relocation     [0x00000001047aa458,0x00000001047aa460] = 8
 main code      [0x00000001047aa480,0x00000001047aa4a4] = 36
 stub code      [0x00000001047aa4a4,0x00000001047aa4a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6618} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;JLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047aa480:   	nop
  0x00000001047aa484:   	ldr	xzr, [x1]
  0x00000001047aa488:   	ldr	w12, [x5, #0x24]
  0x00000001047aa48c:   	lsl	x12, x12, #3
  0x00000001047aa490:   	ldr	x12, [x12, #0x10]
  0x00000001047aa494:   	cbz	x12, #0xc
  0x00000001047aa498:   	ldr	x8, [x12, #0x40]
  0x00000001047aa49c:   	br	x8
  0x00000001047aa4a0:   	b	#-0x64620           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047aa4a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3530  114     n       java.lang.invoke.MethodHandle::linkToStatic(JJL)J (native)
 total in heap  [0x00000001047a9c90,0x00000001047a9e20] = 400
 relocation     [0x00000001047a9dd8,0x00000001047a9de0] = 8
 main code      [0x00000001047a9e00,0x00000001047a9e20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6868} 'linkToStatic' '(JJLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a9e00:   	nop
  0x00000001047a9e04:   	ldr	w12, [x3, #0x24]
  0x00000001047a9e08:   	lsl	x12, x12, #3
  0x00000001047a9e0c:   	ldr	x12, [x12, #0x10]
  0x00000001047a9e10:   	cbz	x12, #0xc
  0x00000001047a9e14:   	ldr	x8, [x12, #0x40]
  0x00000001047a9e18:   	br	x8
  0x00000001047a9e1c:   	b	#-0x63f9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3531  115     n       java.lang.invoke.MethodHandle::invokeBasic(JJ)J (native)
 total in heap  [0x00000001047a9990,0x00000001047a9b30] = 416
 relocation     [0x00000001047a9ad8,0x00000001047a9ae0] = 8
 main code      [0x00000001047a9b00,0x00000001047a9b30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6980} 'invokeBasic' '(JJ)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x00000001047a9b00:   	nop
  0x00000001047a9b04:   	ldr	w12, [x1, #0x14]
  0x00000001047a9b08:   	lsl	x12, x12, #3
  0x00000001047a9b0c:   	ldr	w12, [x12, #0x28]
  0x00000001047a9b10:   	lsl	x12, x12, #3
  0x00000001047a9b14:   	ldr	w12, [x12, #0x24]
  0x00000001047a9b18:   	lsl	x12, x12, #3
  0x00000001047a9b1c:   	ldr	x12, [x12, #0x10]
  0x00000001047a9b20:   	cbz	x12, #0xc
  0x00000001047a9b24:   	ldr	x8, [x12, #0x40]
  0x00000001047a9b28:   	br	x8
  0x00000001047a9b2c:   	b	#-0x63cac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3532  116     n       java.lang.invoke.MethodHandle::linkToSpecial(LJJL)J (native)
 total in heap  [0x00000001047a9690,0x00000001047a9828] = 408
 relocation     [0x00000001047a97d8,0x00000001047a97e0] = 8
 main code      [0x00000001047a9800,0x00000001047a9824] = 36
 stub code      [0x00000001047a9824,0x00000001047a9828] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6a98} 'linkToSpecial' '(Ljava/lang/Object;JJLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a9800:   	nop
  0x00000001047a9804:   	ldr	xzr, [x1]
  0x00000001047a9808:   	ldr	w12, [x4, #0x24]
  0x00000001047a980c:   	lsl	x12, x12, #3
  0x00000001047a9810:   	ldr	x12, [x12, #0x10]
  0x00000001047a9814:   	cbz	x12, #0xc
  0x00000001047a9818:   	ldr	x8, [x12, #0x40]
  0x00000001047a981c:   	br	x8
  0x00000001047a9820:   	b	#-0x639a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a9824:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3533  117     n       java.lang.invoke.MethodHandle::invokeBasic(J)J (native)
 total in heap  [0x00000001047a9390,0x00000001047a9530] = 416
 relocation     [0x00000001047a94d8,0x00000001047a94e0] = 8
 main code      [0x00000001047a9500,0x00000001047a9530] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6d18} 'invokeBasic' '(J)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x00000001047a9500:   	nop
  0x00000001047a9504:   	ldr	w12, [x1, #0x14]
  0x00000001047a9508:   	lsl	x12, x12, #3
  0x00000001047a950c:   	ldr	w12, [x12, #0x28]
  0x00000001047a9510:   	lsl	x12, x12, #3
  0x00000001047a9514:   	ldr	w12, [x12, #0x24]
  0x00000001047a9518:   	lsl	x12, x12, #3
  0x00000001047a951c:   	ldr	x12, [x12, #0x10]
  0x00000001047a9520:   	cbz	x12, #0xc
  0x00000001047a9524:   	ldr	x8, [x12, #0x40]
  0x00000001047a9528:   	br	x8
  0x00000001047a952c:   	b	#-0x636ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3534  118     n       java.lang.invoke.MethodHandle::linkToSpecial(LJL)J (native)
 total in heap  [0x00000001047a8e90,0x00000001047a9028] = 408
 relocation     [0x00000001047a8fd8,0x00000001047a8fe0] = 8
 main code      [0x00000001047a9000,0x00000001047a9024] = 36
 stub code      [0x00000001047a9024,0x00000001047a9028] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000114bd6e30} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x00000001047a9000:   	nop
  0x00000001047a9004:   	ldr	xzr, [x1]
  0x00000001047a9008:   	ldr	w12, [x3, #0x24]
  0x00000001047a900c:   	lsl	x12, x12, #3
  0x00000001047a9010:   	ldr	x12, [x12, #0x10]
  0x00000001047a9014:   	cbz	x12, #0xc
  0x00000001047a9018:   	ldr	x8, [x12, #0x40]
  0x00000001047a901c:   	br	x8
  0x00000001047a9020:   	b	#-0x631a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x00000001047a9024:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]
508 ms
DisableIntrinsic
CompileCommand: compileonly Main.compute bool compileonly = true
CompileCommand: dontinline Main.logfn bool dontinline = true
CompileCommand: compileonly Main.logfn bool compileonly = true
CompileCommand: compileonly java/lang/FdLibm*.compute bool compileonly = true
CompileCommand: dontinline java/lang/Double.* bool dontinline = true

Compiled method (n/a)     404    1     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLLL)L (native)
 total in heap  [0x0000000106b1f410,0x0000000106b1f5a0] = 400
 relocation     [0x0000000106b1f558,0x0000000106b1f560] = 8
 main code      [0x0000000106b1f580,0x0000000106b1f5a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001163c0a80} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b1f580:   	nop
  0x0000000106b1f584:   	ldr	w12, [x7, #0x24]
  0x0000000106b1f588:   	lsl	x12, x12, #3
  0x0000000106b1f58c:   	ldr	x12, [x12, #0x10]
  0x0000000106b1f590:   	cbz	x12, #0xc
  0x0000000106b1f594:   	ldr	x8, [x12, #0x40]
  0x0000000106b1f598:   	br	x8
  0x0000000106b1f59c:   	b	#-0x2571c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     419    2     n       java.lang.invoke.MethodHandle::linkToStatic(LLL)L (native)
 total in heap  [0x0000000106b30f10,0x0000000106b310a0] = 400
 relocation     [0x0000000106b31058,0x0000000106b31060] = 8
 main code      [0x0000000106b31080,0x0000000106b310a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116413100} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b31080:   	nop
  0x0000000106b31084:   	ldr	w12, [x3, #0x24]
  0x0000000106b31088:   	lsl	x12, x12, #3
  0x0000000106b3108c:   	ldr	x12, [x12, #0x10]
  0x0000000106b31090:   	cbz	x12, #0xc
  0x0000000106b31094:   	ldr	x8, [x12, #0x40]
  0x0000000106b31098:   	br	x8
  0x0000000106b3109c:   	b	#-0x3721c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     424    3     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLL)L (native)
 total in heap  [0x0000000106b31210,0x0000000106b313b0] = 416
 relocation     [0x0000000106b31358,0x0000000106b31360] = 8
 main code      [0x0000000106b31380,0x0000000106b313b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011641c4b8} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b31380:   	nop
  0x0000000106b31384:   	ldr	w12, [x1, #0x14]
  0x0000000106b31388:   	lsl	x12, x12, #3
  0x0000000106b3138c:   	ldr	w12, [x12, #0x28]
  0x0000000106b31390:   	lsl	x12, x12, #3
  0x0000000106b31394:   	ldr	w12, [x12, #0x24]
  0x0000000106b31398:   	lsl	x12, x12, #3
  0x0000000106b3139c:   	ldr	x12, [x12, #0x10]
  0x0000000106b313a0:   	cbz	x12, #0xc
  0x0000000106b313a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b313a8:   	br	x8
  0x0000000106b313ac:   	b	#-0x3752c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     426    4     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLLL)L (native)
 total in heap  [0x0000000106b31510,0x0000000106b316a8] = 408
 relocation     [0x0000000106b31658,0x0000000106b31660] = 8
 main code      [0x0000000106b31680,0x0000000106b316a4] = 36
 stub code      [0x0000000106b316a4,0x0000000106b316a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011641c630} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b31680:   	nop
  0x0000000106b31684:   	ldr	xzr, [x1]
  0x0000000106b31688:   	ldr	w12, [x0, #0x24]
  0x0000000106b3168c:   	lsl	x12, x12, #3
  0x0000000106b31690:   	ldr	x12, [x12, #0x10]
  0x0000000106b31694:   	cbz	x12, #0xc
  0x0000000106b31698:   	ldr	x8, [x12, #0x40]
  0x0000000106b3169c:   	br	x8
  0x0000000106b316a0:   	b	#-0x37820           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b316a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     434    5     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)V (native)
 total in heap  [0x0000000106b33410,0x0000000106b335a8] = 408
 relocation     [0x0000000106b33558,0x0000000106b33560] = 8
 main code      [0x0000000106b33580,0x0000000106b335a4] = 36
 stub code      [0x0000000106b335a4,0x0000000106b335a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011644a7f0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b33580:   	nop
  0x0000000106b33584:   	ldr	xzr, [x1]
  0x0000000106b33588:   	ldr	w12, [x4, #0x24]
  0x0000000106b3358c:   	lsl	x12, x12, #3
  0x0000000106b33590:   	ldr	x12, [x12, #0x10]
  0x0000000106b33594:   	cbz	x12, #0xc
  0x0000000106b33598:   	ldr	x8, [x12, #0x40]
  0x0000000106b3359c:   	br	x8
  0x0000000106b335a0:   	b	#-0x39720           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b335a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     436    6     n       java.lang.invoke.MethodHandle::invokeBasic(LL)L (native)
 total in heap  [0x0000000106b33a90,0x0000000106b33c30] = 416
 relocation     [0x0000000106b33bd8,0x0000000106b33be0] = 8
 main code      [0x0000000106b33c00,0x0000000106b33c30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011644bc38} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b33c00:   	nop
  0x0000000106b33c04:   	ldr	w12, [x1, #0x14]
  0x0000000106b33c08:   	lsl	x12, x12, #3
  0x0000000106b33c0c:   	ldr	w12, [x12, #0x28]
  0x0000000106b33c10:   	lsl	x12, x12, #3
  0x0000000106b33c14:   	ldr	w12, [x12, #0x24]
  0x0000000106b33c18:   	lsl	x12, x12, #3
  0x0000000106b33c1c:   	ldr	x12, [x12, #0x10]
  0x0000000106b33c20:   	cbz	x12, #0xc
  0x0000000106b33c24:   	ldr	x8, [x12, #0x40]
  0x0000000106b33c28:   	br	x8
  0x0000000106b33c2c:   	b	#-0x39dac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     437    7     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)L (native)
 total in heap  [0x0000000106b33d90,0x0000000106b33f28] = 408
 relocation     [0x0000000106b33ed8,0x0000000106b33ee0] = 8
 main code      [0x0000000106b33f00,0x0000000106b33f24] = 36
 stub code      [0x0000000106b33f24,0x0000000106b33f28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011644bd50} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b33f00:   	nop
  0x0000000106b33f04:   	ldr	xzr, [x1]
  0x0000000106b33f08:   	ldr	w12, [x4, #0x24]
  0x0000000106b33f0c:   	lsl	x12, x12, #3
  0x0000000106b33f10:   	ldr	x12, [x12, #0x10]
  0x0000000106b33f14:   	cbz	x12, #0xc
  0x0000000106b33f18:   	ldr	x8, [x12, #0x40]
  0x0000000106b33f1c:   	br	x8
  0x0000000106b33f20:   	b	#-0x3a0a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b33f24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     459    8     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)L (native)
 total in heap  [0x0000000106b36390,0x0000000106b36528] = 408
 relocation     [0x0000000106b364d8,0x0000000106b364e0] = 8
 main code      [0x0000000106b36500,0x0000000106b36524] = 36
 stub code      [0x0000000106b36524,0x0000000106b36528] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116489e80} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b36500:   	nop
  0x0000000106b36504:   	ldr	xzr, [x1]
  0x0000000106b36508:   	ldr	w12, [x3, #0x24]
  0x0000000106b3650c:   	lsl	x12, x12, #3
  0x0000000106b36510:   	ldr	x12, [x12, #0x10]
  0x0000000106b36514:   	cbz	x12, #0xc
  0x0000000106b36518:   	ldr	x8, [x12, #0x40]
  0x0000000106b3651c:   	br	x8
  0x0000000106b36520:   	b	#-0x3c6a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b36524:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     471    9     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)I (native)
 total in heap  [0x0000000106b3a910,0x0000000106b3aaa0] = 400
 relocation     [0x0000000106b3aa58,0x0000000106b3aa60] = 8
 main code      [0x0000000106b3aa80,0x0000000106b3aaa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001164c7c20} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3aa80:   	nop
  0x0000000106b3aa84:   	ldr	w12, [x4, #0x24]
  0x0000000106b3aa88:   	lsl	x12, x12, #3
  0x0000000106b3aa8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3aa90:   	cbz	x12, #0xc
  0x0000000106b3aa94:   	ldr	x8, [x12, #0x40]
  0x0000000106b3aa98:   	br	x8
  0x0000000106b3aa9c:   	b	#-0x40c1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     484   10     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)V (native)
 total in heap  [0x0000000106b3af90,0x0000000106b3b128] = 408
 relocation     [0x0000000106b3b0d8,0x0000000106b3b0e0] = 8
 main code      [0x0000000106b3b100,0x0000000106b3b124] = 36
 stub code      [0x0000000106b3b124,0x0000000106b3b128] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001164e1190} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3b100:   	nop
  0x0000000106b3b104:   	ldr	xzr, [x1]
  0x0000000106b3b108:   	ldr	w12, [x3, #0x24]
  0x0000000106b3b10c:   	lsl	x12, x12, #3
  0x0000000106b3b110:   	ldr	x12, [x12, #0x10]
  0x0000000106b3b114:   	cbz	x12, #0xc
  0x0000000106b3b118:   	ldr	x8, [x12, #0x40]
  0x0000000106b3b11c:   	br	x8
  0x0000000106b3b120:   	b	#-0x412a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b3b124:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     486   11     n       java.lang.invoke.MethodHandle::invokeBasic(L)L (native)
 total in heap  [0x0000000106b3b290,0x0000000106b3b430] = 416
 relocation     [0x0000000106b3b3d8,0x0000000106b3b3e0] = 8
 main code      [0x0000000106b3b400,0x0000000106b3b430] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001164e18c0} 'invokeBasic' '(Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3b400:   	nop
  0x0000000106b3b404:   	ldr	w12, [x1, #0x14]
  0x0000000106b3b408:   	lsl	x12, x12, #3
  0x0000000106b3b40c:   	ldr	w12, [x12, #0x28]
  0x0000000106b3b410:   	lsl	x12, x12, #3
  0x0000000106b3b414:   	ldr	w12, [x12, #0x24]
  0x0000000106b3b418:   	lsl	x12, x12, #3
  0x0000000106b3b41c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3b420:   	cbz	x12, #0xc
  0x0000000106b3b424:   	ldr	x8, [x12, #0x40]
  0x0000000106b3b428:   	br	x8
  0x0000000106b3b42c:   	b	#-0x415ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     490   12     n       java.lang.invoke.MethodHandle::linkToStatic(LL)L (native)
 total in heap  [0x0000000106b3b590,0x0000000106b3b720] = 400
 relocation     [0x0000000106b3b6d8,0x0000000106b3b6e0] = 8
 main code      [0x0000000106b3b700,0x0000000106b3b720] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001164e9a08} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3b700:   	nop
  0x0000000106b3b704:   	ldr	w12, [x2, #0x24]
  0x0000000106b3b708:   	lsl	x12, x12, #3
  0x0000000106b3b70c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3b710:   	cbz	x12, #0xc
  0x0000000106b3b714:   	ldr	x8, [x12, #0x40]
  0x0000000106b3b718:   	br	x8
  0x0000000106b3b71c:   	b	#-0x4189c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     495   13     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLL)I (native)
 total in heap  [0x0000000106b3c310,0x0000000106b3c4a8] = 408
 relocation     [0x0000000106b3c458,0x0000000106b3c460] = 8
 main code      [0x0000000106b3c480,0x0000000106b3c4a4] = 36
 stub code      [0x0000000106b3c4a4,0x0000000106b3c4a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001164f7a90} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3c480:   	nop
  0x0000000106b3c484:   	ldr	xzr, [x1]
  0x0000000106b3c488:   	ldr	w12, [x4, #0x24]
  0x0000000106b3c48c:   	lsl	x12, x12, #3
  0x0000000106b3c490:   	ldr	x12, [x12, #0x10]
  0x0000000106b3c494:   	cbz	x12, #0xc
  0x0000000106b3c498:   	ldr	x8, [x12, #0x40]
  0x0000000106b3c49c:   	br	x8
  0x0000000106b3c4a0:   	b	#-0x42620           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b3c4a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     508   14     n       java.lang.invoke.MethodHandle::linkToStatic(L)L (native)
 total in heap  [0x0000000106b3c990,0x0000000106b3cb20] = 400
 relocation     [0x0000000106b3cad8,0x0000000106b3cae0] = 8
 main code      [0x0000000106b3cb00,0x0000000106b3cb20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165ae8f0} 'linkToStatic' '(Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3cb00:   	nop
  0x0000000106b3cb04:   	ldr	w12, [x1, #0x24]
  0x0000000106b3cb08:   	lsl	x12, x12, #3
  0x0000000106b3cb0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3cb10:   	cbz	x12, #0xc
  0x0000000106b3cb14:   	ldr	x8, [x12, #0x40]
  0x0000000106b3cb18:   	br	x8
  0x0000000106b3cb1c:   	b	#-0x42c9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     514   15     n       java.lang.invoke.MethodHandle::linkToStatic(LLLL)L (native)
 total in heap  [0x0000000106b3d390,0x0000000106b3d520] = 400
 relocation     [0x0000000106b3d4d8,0x0000000106b3d4e0] = 8
 main code      [0x0000000106b3d500,0x0000000106b3d520] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165c3320} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3d500:   	nop
  0x0000000106b3d504:   	ldr	w12, [x4, #0x24]
  0x0000000106b3d508:   	lsl	x12, x12, #3
  0x0000000106b3d50c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3d510:   	cbz	x12, #0xc
  0x0000000106b3d514:   	ldr	x8, [x12, #0x40]
  0x0000000106b3d518:   	br	x8
  0x0000000106b3d51c:   	b	#-0x4369c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     517   16     n       java.lang.invoke.MethodHandle::invokeBasic()L (native)
 total in heap  [0x0000000106b3da10,0x0000000106b3dbb0] = 416
 relocation     [0x0000000106b3db58,0x0000000106b3db60] = 8
 main code      [0x0000000106b3db80,0x0000000106b3dbb0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165c76b8} 'invokeBasic' '()Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3db80:   	nop
  0x0000000106b3db84:   	ldr	w12, [x1, #0x14]
  0x0000000106b3db88:   	lsl	x12, x12, #3
  0x0000000106b3db8c:   	ldr	w12, [x12, #0x28]
  0x0000000106b3db90:   	lsl	x12, x12, #3
  0x0000000106b3db94:   	ldr	w12, [x12, #0x24]
  0x0000000106b3db98:   	lsl	x12, x12, #3
  0x0000000106b3db9c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3dba0:   	cbz	x12, #0xc
  0x0000000106b3dba4:   	ldr	x8, [x12, #0x40]
  0x0000000106b3dba8:   	br	x8
  0x0000000106b3dbac:   	b	#-0x43d2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     518   17     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)L (native)
 total in heap  [0x0000000106b3dd10,0x0000000106b3dea8] = 408
 relocation     [0x0000000106b3de58,0x0000000106b3de60] = 8
 main code      [0x0000000106b3de80,0x0000000106b3dea4] = 36
 stub code      [0x0000000106b3dea4,0x0000000106b3dea8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165c77d0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3de80:   	nop
  0x0000000106b3de84:   	ldr	xzr, [x1]
  0x0000000106b3de88:   	ldr	w12, [x2, #0x24]
  0x0000000106b3de8c:   	lsl	x12, x12, #3
  0x0000000106b3de90:   	ldr	x12, [x12, #0x10]
  0x0000000106b3de94:   	cbz	x12, #0xc
  0x0000000106b3de98:   	ldr	x8, [x12, #0x40]
  0x0000000106b3de9c:   	br	x8
  0x0000000106b3dea0:   	b	#-0x44020           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b3dea4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     521   18     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)V (native)
 total in heap  [0x0000000106b3e010,0x0000000106b3e1a8] = 408
 relocation     [0x0000000106b3e158,0x0000000106b3e160] = 8
 main code      [0x0000000106b3e180,0x0000000106b3e1a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165d9838} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3e180:   	nop
  0x0000000106b3e184:   	ldr	w10, [x1, #0x8]
  0x0000000106b3e188:   	eor	x10, x10, #0x800000000
  0x0000000106b3e18c:   	ldr	x11, [x2, #0x10]
  0x0000000106b3e190:   	add	x12, x10, x11, uxtx #3
  0x0000000106b3e194:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b3e198:   	cbz	x12, #0xc
  0x0000000106b3e19c:   	ldr	x8, [x12, #0x40]
  0x0000000106b3e1a0:   	br	x8
  0x0000000106b3e1a4:   	b	#-0x44324           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     523   19     n       java.lang.invoke.MethodHandle::linkToStatic(LLL)I (native)
 total in heap  [0x0000000106b3e310,0x0000000106b3e4a0] = 400
 relocation     [0x0000000106b3e458,0x0000000106b3e460] = 8
 main code      [0x0000000106b3e480,0x0000000106b3e4a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165da9c8} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3e480:   	nop
  0x0000000106b3e484:   	ldr	w12, [x3, #0x24]
  0x0000000106b3e488:   	lsl	x12, x12, #3
  0x0000000106b3e48c:   	ldr	x12, [x12, #0x10]
  0x0000000106b3e490:   	cbz	x12, #0xc
  0x0000000106b3e494:   	ldr	x8, [x12, #0x40]
  0x0000000106b3e498:   	br	x8
  0x0000000106b3e49c:   	b	#-0x4461c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     525   20     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)V (native)
 total in heap  [0x0000000106b3e610,0x0000000106b3e7a8] = 408
 relocation     [0x0000000106b3e758,0x0000000106b3e760] = 8
 main code      [0x0000000106b3e780,0x0000000106b3e7a4] = 36
 stub code      [0x0000000106b3e7a4,0x0000000106b3e7a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165dce60} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3e780:   	nop
  0x0000000106b3e784:   	ldr	xzr, [x1]
  0x0000000106b3e788:   	ldr	w12, [x2, #0x24]
  0x0000000106b3e78c:   	lsl	x12, x12, #3
  0x0000000106b3e790:   	ldr	x12, [x12, #0x10]
  0x0000000106b3e794:   	cbz	x12, #0xc
  0x0000000106b3e798:   	ldr	x8, [x12, #0x40]
  0x0000000106b3e79c:   	br	x8
  0x0000000106b3e7a0:   	b	#-0x44920           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b3e7a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     529   21     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)L (native)
 total in heap  [0x0000000106b3e910,0x0000000106b3eae8] = 472
 relocation     [0x0000000106b3ea58,0x0000000106b3ea60] = 8
 main code      [0x0000000106b3ea80,0x0000000106b3eae4] = 100
 stub code      [0x0000000106b3eae4,0x0000000106b3eae8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165df470} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3ea80:   	nop
  0x0000000106b3ea84:   	ldr	w10, [x1, #0x8]
  0x0000000106b3ea88:   	eor	x10, x10, #0x800000000
  0x0000000106b3ea8c:   	ldr	w14, [x3, #0x18]
  0x0000000106b3ea90:   	lsl	x14, x14, #3
  0x0000000106b3ea94:   	ldr	x14, [x14, #0x10]
  0x0000000106b3ea98:   	ldr	x12, [x3, #0x10]
  0x0000000106b3ea9c:   	ldr	w11, [x10, #0xa0]
  0x0000000106b3eaa0:   	add	x11, x10, x11, uxtx #3
  0x0000000106b3eaa4:   	add	x11, x11, #0x1c0
  0x0000000106b3eaa8:   	add	x10, x10, x12, uxtx #3
  0x0000000106b3eaac:   	ldr	x12, [x11]
  0x0000000106b3eab0:   	cmp	x14, x12
  0x0000000106b3eab4:   	b.eq	#0x14
  0x0000000106b3eab8:   	cbz	x12, #0x28
  0x0000000106b3eabc:   	ldr	x12, [x11, #0x10]!
  0x0000000106b3eac0:   	cmp	x14, x12
  0x0000000106b3eac4:   	b.ne	#-0xc
  0x0000000106b3eac8:   	ldr	w11, [x11, #0x8]
  0x0000000106b3eacc:   	ldr	x12, [x10, w11, uxtw]
  0x0000000106b3ead0:   	cbz	x12, #0xc
  0x0000000106b3ead4:   	ldr	x8, [x12, #0x40]
  0x0000000106b3ead8:   	br	x8
  0x0000000106b3eadc:   	b	#-0x44c5c           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000106b3eae0:   	b	#-0x63560           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000106b3eae4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     534   22     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)I (native)
 total in heap  [0x0000000106b3ec10,0x0000000106b3ede8] = 472
 relocation     [0x0000000106b3ed58,0x0000000106b3ed60] = 8
 main code      [0x0000000106b3ed80,0x0000000106b3ede4] = 100
 stub code      [0x0000000106b3ede4,0x0000000106b3ede8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165f5ed0} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b3ed80:   	nop
  0x0000000106b3ed84:   	ldr	w10, [x1, #0x8]
  0x0000000106b3ed88:   	eor	x10, x10, #0x800000000
  0x0000000106b3ed8c:   	ldr	w14, [x3, #0x18]
  0x0000000106b3ed90:   	lsl	x14, x14, #3
  0x0000000106b3ed94:   	ldr	x14, [x14, #0x10]
  0x0000000106b3ed98:   	ldr	x12, [x3, #0x10]
  0x0000000106b3ed9c:   	ldr	w11, [x10, #0xa0]
  0x0000000106b3eda0:   	add	x11, x10, x11, uxtx #3
  0x0000000106b3eda4:   	add	x11, x11, #0x1c0
  0x0000000106b3eda8:   	add	x10, x10, x12, uxtx #3
  0x0000000106b3edac:   	ldr	x12, [x11]
  0x0000000106b3edb0:   	cmp	x14, x12
  0x0000000106b3edb4:   	b.eq	#0x14
  0x0000000106b3edb8:   	cbz	x12, #0x28
  0x0000000106b3edbc:   	ldr	x12, [x11, #0x10]!
  0x0000000106b3edc0:   	cmp	x14, x12
  0x0000000106b3edc4:   	b.ne	#-0xc
  0x0000000106b3edc8:   	ldr	w11, [x11, #0x8]
  0x0000000106b3edcc:   	ldr	x12, [x10, w11, uxtw]
  0x0000000106b3edd0:   	cbz	x12, #0xc
  0x0000000106b3edd4:   	ldr	x8, [x12, #0x40]
  0x0000000106b3edd8:   	br	x8
  0x0000000106b3eddc:   	b	#-0x44f5c           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000106b3ede0:   	b	#-0x63860           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000106b3ede4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     632   23     n       java.lang.invoke.MethodHandle::linkToInterface(LL)L (native)
 total in heap  [0x0000000106b40410,0x0000000106b405e8] = 472
 relocation     [0x0000000106b40558,0x0000000106b40560] = 8
 main code      [0x0000000106b40580,0x0000000106b405e4] = 100
 stub code      [0x0000000106b405e4,0x0000000106b405e8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116575bb8} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b40580:   	nop
  0x0000000106b40584:   	ldr	w10, [x1, #0x8]
  0x0000000106b40588:   	eor	x10, x10, #0x800000000
  0x0000000106b4058c:   	ldr	w14, [x2, #0x18]
  0x0000000106b40590:   	lsl	x14, x14, #3
  0x0000000106b40594:   	ldr	x14, [x14, #0x10]
  0x0000000106b40598:   	ldr	x12, [x2, #0x10]
  0x0000000106b4059c:   	ldr	w11, [x10, #0xa0]
  0x0000000106b405a0:   	add	x11, x10, x11, uxtx #3
  0x0000000106b405a4:   	add	x11, x11, #0x1c0
  0x0000000106b405a8:   	add	x10, x10, x12, uxtx #3
  0x0000000106b405ac:   	ldr	x12, [x11]
  0x0000000106b405b0:   	cmp	x14, x12
  0x0000000106b405b4:   	b.eq	#0x14
  0x0000000106b405b8:   	cbz	x12, #0x28
  0x0000000106b405bc:   	ldr	x12, [x11, #0x10]!
  0x0000000106b405c0:   	cmp	x14, x12
  0x0000000106b405c4:   	b.ne	#-0xc
  0x0000000106b405c8:   	ldr	w11, [x11, #0x8]
  0x0000000106b405cc:   	ldr	x12, [x10, w11, uxtw]
  0x0000000106b405d0:   	cbz	x12, #0xc
  0x0000000106b405d4:   	ldr	x8, [x12, #0x40]
  0x0000000106b405d8:   	br	x8
  0x0000000106b405dc:   	b	#-0x4675c           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000106b405e0:   	b	#-0x65060           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000106b405e4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     640   24     n       java.lang.invoke.MethodHandle::linkToStatic(LL)I (native)
 total in heap  [0x0000000106b40710,0x0000000106b408a0] = 400
 relocation     [0x0000000106b40858,0x0000000106b40860] = 8
 main code      [0x0000000106b40880,0x0000000106b408a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011657bff0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b40880:   	nop
  0x0000000106b40884:   	ldr	w12, [x2, #0x24]
  0x0000000106b40888:   	lsl	x12, x12, #3
  0x0000000106b4088c:   	ldr	x12, [x12, #0x10]
  0x0000000106b40890:   	cbz	x12, #0xc
  0x0000000106b40894:   	ldr	x8, [x12, #0x40]
  0x0000000106b40898:   	br	x8
  0x0000000106b4089c:   	b	#-0x46a1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     642   25     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)L (native)
 total in heap  [0x0000000106b40a10,0x0000000106b40ba8] = 408
 relocation     [0x0000000106b40b58,0x0000000106b40b60] = 8
 main code      [0x0000000106b40b80,0x0000000106b40ba8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011657cc18} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b40b80:   	nop
  0x0000000106b40b84:   	ldr	w10, [x1, #0x8]
  0x0000000106b40b88:   	eor	x10, x10, #0x800000000
  0x0000000106b40b8c:   	ldr	x11, [x2, #0x10]
  0x0000000106b40b90:   	add	x12, x10, x11, uxtx #3
  0x0000000106b40b94:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b40b98:   	cbz	x12, #0xc
  0x0000000106b40b9c:   	ldr	x8, [x12, #0x40]
  0x0000000106b40ba0:   	br	x8
  0x0000000106b40ba4:   	b	#-0x46d24           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     652   26     n       java.lang.invoke.MethodHandle::linkToSpecial(LL)I (native)
 total in heap  [0x0000000106b40d10,0x0000000106b40ea8] = 408
 relocation     [0x0000000106b40e58,0x0000000106b40e60] = 8
 main code      [0x0000000106b40e80,0x0000000106b40ea4] = 36
 stub code      [0x0000000106b40ea4,0x0000000106b40ea8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001165213b0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b40e80:   	nop
  0x0000000106b40e84:   	ldr	xzr, [x1]
  0x0000000106b40e88:   	ldr	w12, [x2, #0x24]
  0x0000000106b40e8c:   	lsl	x12, x12, #3
  0x0000000106b40e90:   	ldr	x12, [x12, #0x10]
  0x0000000106b40e94:   	cbz	x12, #0xc
  0x0000000106b40e98:   	ldr	x8, [x12, #0x40]
  0x0000000106b40e9c:   	br	x8
  0x0000000106b40ea0:   	b	#-0x47020           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b40ea4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     747   27     n       java.lang.invoke.MethodHandle::linkToStatic(IL)I (native)
 total in heap  [0x0000000106b42890,0x0000000106b42a20] = 400
 relocation     [0x0000000106b429d8,0x0000000106b429e0] = 8
 main code      [0x0000000106b42a00,0x0000000106b42a20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x000000011666b508} 'linkToStatic' '(ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b42a00:   	nop
  0x0000000106b42a04:   	ldr	w12, [x2, #0x24]
  0x0000000106b42a08:   	lsl	x12, x12, #3
  0x0000000106b42a0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b42a10:   	cbz	x12, #0xc
  0x0000000106b42a14:   	ldr	x8, [x12, #0x40]
  0x0000000106b42a18:   	br	x8
  0x0000000106b42a1c:   	b	#-0x48b9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     826   28     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)V (native)
 total in heap  [0x0000000106b43610,0x0000000106b437a8] = 408
 relocation     [0x0000000106b43758,0x0000000106b43760] = 8
 main code      [0x0000000106b43780,0x0000000106b437a4] = 36
 stub code      [0x0000000106b437a4,0x0000000106b437a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166c1778} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b43780:   	nop
  0x0000000106b43784:   	ldr	xzr, [x1]
  0x0000000106b43788:   	ldr	w12, [x5, #0x24]
  0x0000000106b4378c:   	lsl	x12, x12, #3
  0x0000000106b43790:   	ldr	x12, [x12, #0x10]
  0x0000000106b43794:   	cbz	x12, #0xc
  0x0000000106b43798:   	ldr	x8, [x12, #0x40]
  0x0000000106b4379c:   	br	x8
  0x0000000106b437a0:   	b	#-0x49920           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b437a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     827   29     n       java.lang.invoke.MethodHandle::invokeBasic(LLL)L (native)
 total in heap  [0x0000000106b43910,0x0000000106b43ab0] = 416
 relocation     [0x0000000106b43a58,0x0000000106b43a60] = 8
 main code      [0x0000000106b43a80,0x0000000106b43ab0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166c1890} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b43a80:   	nop
  0x0000000106b43a84:   	ldr	w12, [x1, #0x14]
  0x0000000106b43a88:   	lsl	x12, x12, #3
  0x0000000106b43a8c:   	ldr	w12, [x12, #0x28]
  0x0000000106b43a90:   	lsl	x12, x12, #3
  0x0000000106b43a94:   	ldr	w12, [x12, #0x24]
  0x0000000106b43a98:   	lsl	x12, x12, #3
  0x0000000106b43a9c:   	ldr	x12, [x12, #0x10]
  0x0000000106b43aa0:   	cbz	x12, #0xc
  0x0000000106b43aa4:   	ldr	x8, [x12, #0x40]
  0x0000000106b43aa8:   	br	x8
  0x0000000106b43aac:   	b	#-0x49c2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     828   30     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)L (native)
 total in heap  [0x0000000106b43c10,0x0000000106b43da8] = 408
 relocation     [0x0000000106b43d58,0x0000000106b43d60] = 8
 main code      [0x0000000106b43d80,0x0000000106b43da4] = 36
 stub code      [0x0000000106b43da4,0x0000000106b43da8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166c19a8} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b43d80:   	nop
  0x0000000106b43d84:   	ldr	xzr, [x1]
  0x0000000106b43d88:   	ldr	w12, [x5, #0x24]
  0x0000000106b43d8c:   	lsl	x12, x12, #3
  0x0000000106b43d90:   	ldr	x12, [x12, #0x10]
  0x0000000106b43d94:   	cbz	x12, #0xc
  0x0000000106b43d98:   	ldr	x8, [x12, #0x40]
  0x0000000106b43d9c:   	br	x8
  0x0000000106b43da0:   	b	#-0x49f20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b43da4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     876   31     n       java.lang.invoke.MethodHandle::linkToStatic(IIL)I (native)
 total in heap  [0x0000000106b43f10,0x0000000106b440a0] = 400
 relocation     [0x0000000106b44058,0x0000000106b44060] = 8
 main code      [0x0000000106b44080,0x0000000106b440a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d5cc8} 'linkToStatic' '(IILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b44080:   	nop
  0x0000000106b44084:   	ldr	w12, [x3, #0x24]
  0x0000000106b44088:   	lsl	x12, x12, #3
  0x0000000106b4408c:   	ldr	x12, [x12, #0x10]
  0x0000000106b44090:   	cbz	x12, #0xc
  0x0000000106b44094:   	ldr	x8, [x12, #0x40]
  0x0000000106b44098:   	br	x8
  0x0000000106b4409c:   	b	#-0x4a21c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     877   32     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)V (native)
 total in heap  [0x0000000106b44210,0x0000000106b443a8] = 408
 relocation     [0x0000000106b44358,0x0000000106b44360] = 8
 main code      [0x0000000106b44380,0x0000000106b443a4] = 36
 stub code      [0x0000000106b443a4,0x0000000106b443a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d61b0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b44380:   	nop
  0x0000000106b44384:   	ldr	xzr, [x1]
  0x0000000106b44388:   	ldr	w12, [x3, #0x24]
  0x0000000106b4438c:   	lsl	x12, x12, #3
  0x0000000106b44390:   	ldr	x12, [x12, #0x10]
  0x0000000106b44394:   	cbz	x12, #0xc
  0x0000000106b44398:   	ldr	x8, [x12, #0x40]
  0x0000000106b4439c:   	br	x8
  0x0000000106b443a0:   	b	#-0x4a520           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b443a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     878   33     n       java.lang.invoke.MethodHandle::invokeBasic(I)L (native)
 total in heap  [0x0000000106b44510,0x0000000106b446b0] = 416
 relocation     [0x0000000106b44658,0x0000000106b44660] = 8
 main code      [0x0000000106b44680,0x0000000106b446b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d62c8} 'invokeBasic' '(I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b44680:   	nop
  0x0000000106b44684:   	ldr	w12, [x1, #0x14]
  0x0000000106b44688:   	lsl	x12, x12, #3
  0x0000000106b4468c:   	ldr	w12, [x12, #0x28]
  0x0000000106b44690:   	lsl	x12, x12, #3
  0x0000000106b44694:   	ldr	w12, [x12, #0x24]
  0x0000000106b44698:   	lsl	x12, x12, #3
  0x0000000106b4469c:   	ldr	x12, [x12, #0x10]
  0x0000000106b446a0:   	cbz	x12, #0xc
  0x0000000106b446a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b446a8:   	br	x8
  0x0000000106b446ac:   	b	#-0x4a82c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     879   34     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)L (native)
 total in heap  [0x0000000106b44810,0x0000000106b449a8] = 408
 relocation     [0x0000000106b44958,0x0000000106b44960] = 8
 main code      [0x0000000106b44980,0x0000000106b449a4] = 36
 stub code      [0x0000000106b449a4,0x0000000106b449a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d63e0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b44980:   	nop
  0x0000000106b44984:   	ldr	xzr, [x1]
  0x0000000106b44988:   	ldr	w12, [x3, #0x24]
  0x0000000106b4498c:   	lsl	x12, x12, #3
  0x0000000106b44990:   	ldr	x12, [x12, #0x10]
  0x0000000106b44994:   	cbz	x12, #0xc
  0x0000000106b44998:   	ldr	x8, [x12, #0x40]
  0x0000000106b4499c:   	br	x8
  0x0000000106b449a0:   	b	#-0x4ab20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b449a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     881   35     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLIILL)I (native)
 total in heap  [0x0000000106b44b10,0x0000000106b44ca8] = 408
 relocation     [0x0000000106b44c58,0x0000000106b44c60] = 8
 main code      [0x0000000106b44c80,0x0000000106b44ca4] = 36
 stub code      [0x0000000106b44ca4,0x0000000106b44ca8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d65b8} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b44c80:   	nop
  0x0000000106b44c84:   	ldr	xzr, [x1]
  0x0000000106b44c88:   	ldr	w12, [x7, #0x24]
  0x0000000106b44c8c:   	lsl	x12, x12, #3
  0x0000000106b44c90:   	ldr	x12, [x12, #0x10]
  0x0000000106b44c94:   	cbz	x12, #0xc
  0x0000000106b44c98:   	ldr	x8, [x12, #0x40]
  0x0000000106b44c9c:   	br	x8
  0x0000000106b44ca0:   	b	#-0x4ae20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b44ca4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     883   36     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLIIL)V (native)
 total in heap  [0x0000000106b45190,0x0000000106b45328] = 408
 relocation     [0x0000000106b452d8,0x0000000106b452e0] = 8
 main code      [0x0000000106b45300,0x0000000106b45324] = 36
 stub code      [0x0000000106b45324,0x0000000106b45328] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d6700} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6   = int
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b45300:   	nop
  0x0000000106b45304:   	ldr	xzr, [x1]
  0x0000000106b45308:   	ldr	w12, [x7, #0x24]
  0x0000000106b4530c:   	lsl	x12, x12, #3
  0x0000000106b45310:   	ldr	x12, [x12, #0x10]
  0x0000000106b45314:   	cbz	x12, #0xc
  0x0000000106b45318:   	ldr	x8, [x12, #0x40]
  0x0000000106b4531c:   	br	x8
  0x0000000106b45320:   	b	#-0x4b4a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b45324:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     885   37     n       java.lang.invoke.MethodHandle::invokeBasic(LLLII)L (native)
 total in heap  [0x0000000106b45490,0x0000000106b45630] = 416
 relocation     [0x0000000106b455d8,0x0000000106b455e0] = 8
 main code      [0x0000000106b45600,0x0000000106b45630] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d6818} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;II)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5   = int
  # parm4:    c_rarg6   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b45600:   	nop
  0x0000000106b45604:   	ldr	w12, [x1, #0x14]
  0x0000000106b45608:   	lsl	x12, x12, #3
  0x0000000106b4560c:   	ldr	w12, [x12, #0x28]
  0x0000000106b45610:   	lsl	x12, x12, #3
  0x0000000106b45614:   	ldr	w12, [x12, #0x24]
  0x0000000106b45618:   	lsl	x12, x12, #3
  0x0000000106b4561c:   	ldr	x12, [x12, #0x10]
  0x0000000106b45620:   	cbz	x12, #0xc
  0x0000000106b45624:   	ldr	x8, [x12, #0x40]
  0x0000000106b45628:   	br	x8
  0x0000000106b4562c:   	b	#-0x4b7ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     886   38     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLIIL)L (native)
 total in heap  [0x0000000106b45790,0x0000000106b45928] = 408
 relocation     [0x0000000106b458d8,0x0000000106b458e0] = 8
 main code      [0x0000000106b45900,0x0000000106b45924] = 36
 stub code      [0x0000000106b45924,0x0000000106b45928] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166d6930} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5   = int
  # parm5:    c_rarg6   = int
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b45900:   	nop
  0x0000000106b45904:   	ldr	xzr, [x1]
  0x0000000106b45908:   	ldr	w12, [x7, #0x24]
  0x0000000106b4590c:   	lsl	x12, x12, #3
  0x0000000106b45910:   	ldr	x12, [x12, #0x10]
  0x0000000106b45914:   	cbz	x12, #0xc
  0x0000000106b45918:   	ldr	x8, [x12, #0x40]
  0x0000000106b4591c:   	br	x8
  0x0000000106b45920:   	b	#-0x4baa0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b45924:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     900   39     n       java.lang.invoke.MethodHandle::linkToStatic(LL)V (native)
 total in heap  [0x0000000106b45e10,0x0000000106b45fa0] = 400
 relocation     [0x0000000106b45f58,0x0000000106b45f60] = 8
 main code      [0x0000000106b45f80,0x0000000106b45fa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166df5a0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b45f80:   	nop
  0x0000000106b45f84:   	ldr	w12, [x2, #0x24]
  0x0000000106b45f88:   	lsl	x12, x12, #3
  0x0000000106b45f8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b45f90:   	cbz	x12, #0xc
  0x0000000106b45f94:   	ldr	x8, [x12, #0x40]
  0x0000000106b45f98:   	br	x8
  0x0000000106b45f9c:   	b	#-0x4c11c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     906   40     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)I (native)
 total in heap  [0x0000000106b46110,0x0000000106b462a8] = 408
 relocation     [0x0000000106b46258,0x0000000106b46260] = 8
 main code      [0x0000000106b46280,0x0000000106b462a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e0288} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b46280:   	nop
  0x0000000106b46284:   	ldr	w10, [x1, #0x8]
  0x0000000106b46288:   	eor	x10, x10, #0x800000000
  0x0000000106b4628c:   	ldr	x11, [x3, #0x10]
  0x0000000106b46290:   	add	x12, x10, x11, uxtx #3
  0x0000000106b46294:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b46298:   	cbz	x12, #0xc
  0x0000000106b4629c:   	ldr	x8, [x12, #0x40]
  0x0000000106b462a0:   	br	x8
  0x0000000106b462a4:   	b	#-0x4c424           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     907   41     n       java.lang.invoke.MethodHandle::linkToInterface(LLL)V (native)
 total in heap  [0x0000000106b46410,0x0000000106b465e8] = 472
 relocation     [0x0000000106b46558,0x0000000106b46560] = 8
 main code      [0x0000000106b46580,0x0000000106b465e4] = 100
 stub code      [0x0000000106b465e4,0x0000000106b465e8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e03a0} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b46580:   	nop
  0x0000000106b46584:   	ldr	w10, [x1, #0x8]
  0x0000000106b46588:   	eor	x10, x10, #0x800000000
  0x0000000106b4658c:   	ldr	w14, [x3, #0x18]
  0x0000000106b46590:   	lsl	x14, x14, #3
  0x0000000106b46594:   	ldr	x14, [x14, #0x10]
  0x0000000106b46598:   	ldr	x12, [x3, #0x10]
  0x0000000106b4659c:   	ldr	w11, [x10, #0xa0]
  0x0000000106b465a0:   	add	x11, x10, x11, uxtx #3
  0x0000000106b465a4:   	add	x11, x11, #0x1c0
  0x0000000106b465a8:   	add	x10, x10, x12, uxtx #3
  0x0000000106b465ac:   	ldr	x12, [x11]
  0x0000000106b465b0:   	cmp	x14, x12
  0x0000000106b465b4:   	b.eq	#0x14
  0x0000000106b465b8:   	cbz	x12, #0x28
  0x0000000106b465bc:   	ldr	x12, [x11, #0x10]!
  0x0000000106b465c0:   	cmp	x14, x12
  0x0000000106b465c4:   	b.ne	#-0xc
  0x0000000106b465c8:   	ldr	w11, [x11, #0x8]
  0x0000000106b465cc:   	ldr	x12, [x10, w11, uxtw]
  0x0000000106b465d0:   	cbz	x12, #0xc
  0x0000000106b465d4:   	ldr	x8, [x12, #0x40]
  0x0000000106b465d8:   	br	x8
  0x0000000106b465dc:   	b	#-0x4c75c           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000106b465e0:   	b	#-0x6b060           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000106b465e4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     912   42     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLL)L (native)
 total in heap  [0x0000000106b46710,0x0000000106b468a0] = 400
 relocation     [0x0000000106b46858,0x0000000106b46860] = 8
 main code      [0x0000000106b46880,0x0000000106b468a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e2b48} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b46880:   	nop
  0x0000000106b46884:   	ldr	w12, [x6, #0x24]
  0x0000000106b46888:   	lsl	x12, x12, #3
  0x0000000106b4688c:   	ldr	x12, [x12, #0x10]
  0x0000000106b46890:   	cbz	x12, #0xc
  0x0000000106b46894:   	ldr	x8, [x12, #0x40]
  0x0000000106b46898:   	br	x8
  0x0000000106b4689c:   	b	#-0x4ca1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     914   43     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLL)L (native)
 total in heap  [0x0000000106b46a10,0x0000000106b46bb0] = 416
 relocation     [0x0000000106b46b58,0x0000000106b46b60] = 8
 main code      [0x0000000106b46b80,0x0000000106b46bb0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e2c60} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b46b80:   	nop
  0x0000000106b46b84:   	ldr	w12, [x1, #0x14]
  0x0000000106b46b88:   	lsl	x12, x12, #3
  0x0000000106b46b8c:   	ldr	w12, [x12, #0x28]
  0x0000000106b46b90:   	lsl	x12, x12, #3
  0x0000000106b46b94:   	ldr	w12, [x12, #0x24]
  0x0000000106b46b98:   	lsl	x12, x12, #3
  0x0000000106b46b9c:   	ldr	x12, [x12, #0x10]
  0x0000000106b46ba0:   	cbz	x12, #0xc
  0x0000000106b46ba4:   	ldr	x8, [x12, #0x40]
  0x0000000106b46ba8:   	br	x8
  0x0000000106b46bac:   	b	#-0x4cd2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     915   44     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLL)L (native)
 total in heap  [0x0000000106b46d10,0x0000000106b46ea8] = 408
 relocation     [0x0000000106b46e58,0x0000000106b46e60] = 8
 main code      [0x0000000106b46e80,0x0000000106b46ea4] = 36
 stub code      [0x0000000106b46ea4,0x0000000106b46ea8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e2d78} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b46e80:   	nop
  0x0000000106b46e84:   	ldr	xzr, [x1]
  0x0000000106b46e88:   	ldr	w12, [x7, #0x24]
  0x0000000106b46e8c:   	lsl	x12, x12, #3
  0x0000000106b46e90:   	ldr	x12, [x12, #0x10]
  0x0000000106b46e94:   	cbz	x12, #0xc
  0x0000000106b46e98:   	ldr	x8, [x12, #0x40]
  0x0000000106b46e9c:   	br	x8
  0x0000000106b46ea0:   	b	#-0x4d020           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b46ea4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     917   45     n       java.lang.invoke.MethodHandle::linkToStatic(LJL)L (native)
 total in heap  [0x0000000106b47010,0x0000000106b471a0] = 400
 relocation     [0x0000000106b47158,0x0000000106b47160] = 8
 main code      [0x0000000106b47180,0x0000000106b471a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e3100} 'linkToStatic' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b47180:   	nop
  0x0000000106b47184:   	ldr	w12, [x3, #0x24]
  0x0000000106b47188:   	lsl	x12, x12, #3
  0x0000000106b4718c:   	ldr	x12, [x12, #0x10]
  0x0000000106b47190:   	cbz	x12, #0xc
  0x0000000106b47194:   	ldr	x8, [x12, #0x40]
  0x0000000106b47198:   	br	x8
  0x0000000106b4719c:   	b	#-0x4d31c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     918   46     n       java.lang.invoke.MethodHandle::invokeBasic(LJ)L (native)
 total in heap  [0x0000000106b47310,0x0000000106b474b0] = 416
 relocation     [0x0000000106b47458,0x0000000106b47460] = 8
 main code      [0x0000000106b47480,0x0000000106b474b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e3218} 'invokeBasic' '(Ljava/lang/Object;J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b47480:   	nop
  0x0000000106b47484:   	ldr	w12, [x1, #0x14]
  0x0000000106b47488:   	lsl	x12, x12, #3
  0x0000000106b4748c:   	ldr	w12, [x12, #0x28]
  0x0000000106b47490:   	lsl	x12, x12, #3
  0x0000000106b47494:   	ldr	w12, [x12, #0x24]
  0x0000000106b47498:   	lsl	x12, x12, #3
  0x0000000106b4749c:   	ldr	x12, [x12, #0x10]
  0x0000000106b474a0:   	cbz	x12, #0xc
  0x0000000106b474a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b474a8:   	br	x8
  0x0000000106b474ac:   	b	#-0x4d62c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     919   47     n       java.lang.invoke.MethodHandle::linkToSpecial(LLJL)L (native)
 total in heap  [0x0000000106b47610,0x0000000106b477a8] = 408
 relocation     [0x0000000106b47758,0x0000000106b47760] = 8
 main code      [0x0000000106b47780,0x0000000106b477a4] = 36
 stub code      [0x0000000106b477a4,0x0000000106b477a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e3330} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b47780:   	nop
  0x0000000106b47784:   	ldr	xzr, [x1]
  0x0000000106b47788:   	ldr	w12, [x4, #0x24]
  0x0000000106b4778c:   	lsl	x12, x12, #3
  0x0000000106b47790:   	ldr	x12, [x12, #0x10]
  0x0000000106b47794:   	cbz	x12, #0xc
  0x0000000106b47798:   	ldr	x8, [x12, #0x40]
  0x0000000106b4779c:   	br	x8
  0x0000000106b477a0:   	b	#-0x4d920           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b477a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     921   48     n       java.lang.invoke.MethodHandle::linkToStatic(JLILL)J (native)
 total in heap  [0x0000000106b47c90,0x0000000106b47e20] = 400
 relocation     [0x0000000106b47dd8,0x0000000106b47de0] = 8
 main code      [0x0000000106b47e00,0x0000000106b47e20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e35f8} 'linkToStatic' '(JLjava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b47e00:   	nop
  0x0000000106b47e04:   	ldr	w12, [x5, #0x24]
  0x0000000106b47e08:   	lsl	x12, x12, #3
  0x0000000106b47e0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b47e10:   	cbz	x12, #0xc
  0x0000000106b47e14:   	ldr	x8, [x12, #0x40]
  0x0000000106b47e18:   	br	x8
  0x0000000106b47e1c:   	b	#-0x4df9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     922   49     n       java.lang.invoke.MethodHandle::invokeBasic(JLIL)J (native)
 total in heap  [0x0000000106b47f90,0x0000000106b48130] = 416
 relocation     [0x0000000106b480d8,0x0000000106b480e0] = 8
 main code      [0x0000000106b48100,0x0000000106b48130] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e3710} 'invokeBasic' '(JLjava/lang/Object;ILjava/lang/Object;)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b48100:   	nop
  0x0000000106b48104:   	ldr	w12, [x1, #0x14]
  0x0000000106b48108:   	lsl	x12, x12, #3
  0x0000000106b4810c:   	ldr	w12, [x12, #0x28]
  0x0000000106b48110:   	lsl	x12, x12, #3
  0x0000000106b48114:   	ldr	w12, [x12, #0x24]
  0x0000000106b48118:   	lsl	x12, x12, #3
  0x0000000106b4811c:   	ldr	x12, [x12, #0x10]
  0x0000000106b48120:   	cbz	x12, #0xc
  0x0000000106b48124:   	ldr	x8, [x12, #0x40]
  0x0000000106b48128:   	br	x8
  0x0000000106b4812c:   	b	#-0x4e2ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     923   50     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLILL)J (native)
 total in heap  [0x0000000106b48610,0x0000000106b487a8] = 408
 relocation     [0x0000000106b48758,0x0000000106b48760] = 8
 main code      [0x0000000106b48780,0x0000000106b487a4] = 36
 stub code      [0x0000000106b487a4,0x0000000106b487a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e3828} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b48780:   	nop
  0x0000000106b48784:   	ldr	xzr, [x1]
  0x0000000106b48788:   	ldr	w12, [x6, #0x24]
  0x0000000106b4878c:   	lsl	x12, x12, #3
  0x0000000106b48790:   	ldr	x12, [x12, #0x10]
  0x0000000106b48794:   	cbz	x12, #0xc
  0x0000000106b48798:   	ldr	x8, [x12, #0x40]
  0x0000000106b4879c:   	br	x8
  0x0000000106b487a0:   	b	#-0x4e920           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b487a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     926   51     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLL)L (native)
 total in heap  [0x0000000106b48c90,0x0000000106b48e20] = 400
 relocation     [0x0000000106b48dd8,0x0000000106b48de0] = 8
 main code      [0x0000000106b48e00,0x0000000106b48e20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e6708} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b48e00:   	nop
  0x0000000106b48e04:   	ldr	w12, [x5, #0x24]
  0x0000000106b48e08:   	lsl	x12, x12, #3
  0x0000000106b48e0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b48e10:   	cbz	x12, #0xc
  0x0000000106b48e14:   	ldr	x8, [x12, #0x40]
  0x0000000106b48e18:   	br	x8
  0x0000000106b48e1c:   	b	#-0x4ef9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     928   52     n       java.lang.invoke.MethodHandle::invokeBasic(LLLL)L (native)
 total in heap  [0x0000000106b48f90,0x0000000106b49130] = 416
 relocation     [0x0000000106b490d8,0x0000000106b490e0] = 8
 main code      [0x0000000106b49100,0x0000000106b49130] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e68b0} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b49100:   	nop
  0x0000000106b49104:   	ldr	w12, [x1, #0x14]
  0x0000000106b49108:   	lsl	x12, x12, #3
  0x0000000106b4910c:   	ldr	w12, [x12, #0x28]
  0x0000000106b49110:   	lsl	x12, x12, #3
  0x0000000106b49114:   	ldr	w12, [x12, #0x24]
  0x0000000106b49118:   	lsl	x12, x12, #3
  0x0000000106b4911c:   	ldr	x12, [x12, #0x10]
  0x0000000106b49120:   	cbz	x12, #0xc
  0x0000000106b49124:   	ldr	x8, [x12, #0x40]
  0x0000000106b49128:   	br	x8
  0x0000000106b4912c:   	b	#-0x4f2ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     929   53     n       java.lang.invoke.MethodHandle::invokeBasic(JLI)J (native)
 total in heap  [0x0000000106b49610,0x0000000106b497b0] = 416
 relocation     [0x0000000106b49758,0x0000000106b49760] = 8
 main code      [0x0000000106b49780,0x0000000106b497b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e6bd8} 'invokeBasic' '(JLjava/lang/Object;I)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b49780:   	nop
  0x0000000106b49784:   	ldr	w12, [x1, #0x14]
  0x0000000106b49788:   	lsl	x12, x12, #3
  0x0000000106b4978c:   	ldr	w12, [x12, #0x28]
  0x0000000106b49790:   	lsl	x12, x12, #3
  0x0000000106b49794:   	ldr	w12, [x12, #0x24]
  0x0000000106b49798:   	lsl	x12, x12, #3
  0x0000000106b4979c:   	ldr	x12, [x12, #0x10]
  0x0000000106b497a0:   	cbz	x12, #0xc
  0x0000000106b497a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b497a8:   	br	x8
  0x0000000106b497ac:   	b	#-0x4f92c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     930   54     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLIL)J (native)
 total in heap  [0x0000000106b49910,0x0000000106b49aa8] = 408
 relocation     [0x0000000106b49a58,0x0000000106b49a60] = 8
 main code      [0x0000000106b49a80,0x0000000106b49aa4] = 36
 stub code      [0x0000000106b49aa4,0x0000000106b49aa8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e6cf0} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b49a80:   	nop
  0x0000000106b49a84:   	ldr	xzr, [x1]
  0x0000000106b49a88:   	ldr	w12, [x5, #0x24]
  0x0000000106b49a8c:   	lsl	x12, x12, #3
  0x0000000106b49a90:   	ldr	x12, [x12, #0x10]
  0x0000000106b49a94:   	cbz	x12, #0xc
  0x0000000106b49a98:   	ldr	x8, [x12, #0x40]
  0x0000000106b49a9c:   	br	x8
  0x0000000106b49aa0:   	b	#-0x4fc20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b49aa4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     932   55     n       java.lang.invoke.MethodHandle::linkToStatic(JL)L (native)
 total in heap  [0x0000000106b49c10,0x0000000106b49da0] = 400
 relocation     [0x0000000106b49d58,0x0000000106b49d60] = 8
 main code      [0x0000000106b49d80,0x0000000106b49da0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e6ef8} 'linkToStatic' '(JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b49d80:   	nop
  0x0000000106b49d84:   	ldr	w12, [x2, #0x24]
  0x0000000106b49d88:   	lsl	x12, x12, #3
  0x0000000106b49d8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b49d90:   	cbz	x12, #0xc
  0x0000000106b49d94:   	ldr	x8, [x12, #0x40]
  0x0000000106b49d98:   	br	x8
  0x0000000106b49d9c:   	b	#-0x4ff1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     934   56     n       java.lang.invoke.MethodHandle::invokeBasic(J)L (native)
 total in heap  [0x0000000106b49f10,0x0000000106b4a0b0] = 416
 relocation     [0x0000000106b4a058,0x0000000106b4a060] = 8
 main code      [0x0000000106b4a080,0x0000000106b4a0b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e8010} 'invokeBasic' '(J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4a080:   	nop
  0x0000000106b4a084:   	ldr	w12, [x1, #0x14]
  0x0000000106b4a088:   	lsl	x12, x12, #3
  0x0000000106b4a08c:   	ldr	w12, [x12, #0x28]
  0x0000000106b4a090:   	lsl	x12, x12, #3
  0x0000000106b4a094:   	ldr	w12, [x12, #0x24]
  0x0000000106b4a098:   	lsl	x12, x12, #3
  0x0000000106b4a09c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4a0a0:   	cbz	x12, #0xc
  0x0000000106b4a0a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b4a0a8:   	br	x8
  0x0000000106b4a0ac:   	b	#-0x5022c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     935   57     n       java.lang.invoke.MethodHandle::linkToSpecial(LJL)L (native)
 total in heap  [0x0000000106b4a210,0x0000000106b4a3a8] = 408
 relocation     [0x0000000106b4a358,0x0000000106b4a360] = 8
 main code      [0x0000000106b4a380,0x0000000106b4a3a4] = 36
 stub code      [0x0000000106b4a3a4,0x0000000106b4a3a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e8128} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4a380:   	nop
  0x0000000106b4a384:   	ldr	xzr, [x1]
  0x0000000106b4a388:   	ldr	w12, [x3, #0x24]
  0x0000000106b4a38c:   	lsl	x12, x12, #3
  0x0000000106b4a390:   	ldr	x12, [x12, #0x10]
  0x0000000106b4a394:   	cbz	x12, #0xc
  0x0000000106b4a398:   	ldr	x8, [x12, #0x40]
  0x0000000106b4a39c:   	br	x8
  0x0000000106b4a3a0:   	b	#-0x50520           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b4a3a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     936   58     n       java.lang.invoke.MethodHandle::linkToStatic(JIL)J (native)
 total in heap  [0x0000000106b4a510,0x0000000106b4a6a0] = 400
 relocation     [0x0000000106b4a658,0x0000000106b4a660] = 8
 main code      [0x0000000106b4a680,0x0000000106b4a6a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e84e0} 'linkToStatic' '(JILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4a680:   	nop
  0x0000000106b4a684:   	ldr	w12, [x3, #0x24]
  0x0000000106b4a688:   	lsl	x12, x12, #3
  0x0000000106b4a68c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4a690:   	cbz	x12, #0xc
  0x0000000106b4a694:   	ldr	x8, [x12, #0x40]
  0x0000000106b4a698:   	br	x8
  0x0000000106b4a69c:   	b	#-0x5081c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     937   59     n       java.lang.invoke.MethodHandle::invokeBasic(JI)J (native)
 total in heap  [0x0000000106b4a810,0x0000000106b4a9b0] = 416
 relocation     [0x0000000106b4a958,0x0000000106b4a960] = 8
 main code      [0x0000000106b4a980,0x0000000106b4a9b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e85f8} 'invokeBasic' '(JI)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4a980:   	nop
  0x0000000106b4a984:   	ldr	w12, [x1, #0x14]
  0x0000000106b4a988:   	lsl	x12, x12, #3
  0x0000000106b4a98c:   	ldr	w12, [x12, #0x28]
  0x0000000106b4a990:   	lsl	x12, x12, #3
  0x0000000106b4a994:   	ldr	w12, [x12, #0x24]
  0x0000000106b4a998:   	lsl	x12, x12, #3
  0x0000000106b4a99c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4a9a0:   	cbz	x12, #0xc
  0x0000000106b4a9a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b4a9a8:   	br	x8
  0x0000000106b4a9ac:   	b	#-0x50b2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     937   60     n       java.lang.invoke.MethodHandle::linkToSpecial(LJIL)J (native)
 total in heap  [0x0000000106b4ab10,0x0000000106b4aca8] = 408
 relocation     [0x0000000106b4ac58,0x0000000106b4ac60] = 8
 main code      [0x0000000106b4ac80,0x0000000106b4aca4] = 36
 stub code      [0x0000000106b4aca4,0x0000000106b4aca8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166e8710} 'linkToSpecial' '(Ljava/lang/Object;JILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4ac80:   	nop
  0x0000000106b4ac84:   	ldr	xzr, [x1]
  0x0000000106b4ac88:   	ldr	w12, [x4, #0x24]
  0x0000000106b4ac8c:   	lsl	x12, x12, #3
  0x0000000106b4ac90:   	ldr	x12, [x12, #0x10]
  0x0000000106b4ac94:   	cbz	x12, #0xc
  0x0000000106b4ac98:   	ldr	x8, [x12, #0x40]
  0x0000000106b4ac9c:   	br	x8
  0x0000000106b4aca0:   	b	#-0x50e20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b4aca4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     944   61     n       java.lang.invoke.MethodHandle::linkToStatic(LLLJL)L (native)
 total in heap  [0x0000000106b4b190,0x0000000106b4b320] = 400
 relocation     [0x0000000106b4b2d8,0x0000000106b4b2e0] = 8
 main code      [0x0000000106b4b300,0x0000000106b4b320] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166ea520} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;JLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4b300:   	nop
  0x0000000106b4b304:   	ldr	w12, [x5, #0x24]
  0x0000000106b4b308:   	lsl	x12, x12, #3
  0x0000000106b4b30c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4b310:   	cbz	x12, #0xc
  0x0000000106b4b314:   	ldr	x8, [x12, #0x40]
  0x0000000106b4b318:   	br	x8
  0x0000000106b4b31c:   	b	#-0x5149c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     946   62     n       java.lang.invoke.MethodHandle::invokeBasic(LLLJ)L (native)
 total in heap  [0x0000000106b4b490,0x0000000106b4b630] = 416
 relocation     [0x0000000106b4b5d8,0x0000000106b4b5e0] = 8
 main code      [0x0000000106b4b600,0x0000000106b4b630] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166ea668} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;J)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4b600:   	nop
  0x0000000106b4b604:   	ldr	w12, [x1, #0x14]
  0x0000000106b4b608:   	lsl	x12, x12, #3
  0x0000000106b4b60c:   	ldr	w12, [x12, #0x28]
  0x0000000106b4b610:   	lsl	x12, x12, #3
  0x0000000106b4b614:   	ldr	w12, [x12, #0x24]
  0x0000000106b4b618:   	lsl	x12, x12, #3
  0x0000000106b4b61c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4b620:   	cbz	x12, #0xc
  0x0000000106b4b624:   	ldr	x8, [x12, #0x40]
  0x0000000106b4b628:   	br	x8
  0x0000000106b4b62c:   	b	#-0x517ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     949   63     n       java.lang.invoke.MethodHandle::invokeBasic(I)J (native)
 total in heap  [0x0000000106b4b790,0x0000000106b4b930] = 416
 relocation     [0x0000000106b4b8d8,0x0000000106b4b8e0] = 8
 main code      [0x0000000106b4b900,0x0000000106b4b930] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166eb888} 'invokeBasic' '(I)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4b900:   	nop
  0x0000000106b4b904:   	ldr	w12, [x1, #0x14]
  0x0000000106b4b908:   	lsl	x12, x12, #3
  0x0000000106b4b90c:   	ldr	w12, [x12, #0x28]
  0x0000000106b4b910:   	lsl	x12, x12, #3
  0x0000000106b4b914:   	ldr	w12, [x12, #0x24]
  0x0000000106b4b918:   	lsl	x12, x12, #3
  0x0000000106b4b91c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4b920:   	cbz	x12, #0xc
  0x0000000106b4b924:   	ldr	x8, [x12, #0x40]
  0x0000000106b4b928:   	br	x8
  0x0000000106b4b92c:   	b	#-0x51aac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     949   64     n       java.lang.invoke.MethodHandle::linkToSpecial(LIL)J (native)
 total in heap  [0x0000000106b4ba90,0x0000000106b4bc28] = 408
 relocation     [0x0000000106b4bbd8,0x0000000106b4bbe0] = 8
 main code      [0x0000000106b4bc00,0x0000000106b4bc24] = 36
 stub code      [0x0000000106b4bc24,0x0000000106b4bc28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166eb9a0} 'linkToSpecial' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4bc00:   	nop
  0x0000000106b4bc04:   	ldr	xzr, [x1]
  0x0000000106b4bc08:   	ldr	w12, [x3, #0x24]
  0x0000000106b4bc0c:   	lsl	x12, x12, #3
  0x0000000106b4bc10:   	ldr	x12, [x12, #0x10]
  0x0000000106b4bc14:   	cbz	x12, #0xc
  0x0000000106b4bc18:   	ldr	x8, [x12, #0x40]
  0x0000000106b4bc1c:   	br	x8
  0x0000000106b4bc20:   	b	#-0x51da0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b4bc24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)     960   65     n       java.lang.invoke.MethodHandle::linkToStatic(LIL)I (native)
 total in heap  [0x0000000106b4bd90,0x0000000106b4bf20] = 400
 relocation     [0x0000000106b4bed8,0x0000000106b4bee0] = 8
 main code      [0x0000000106b4bf00,0x0000000106b4bf20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x00000001166f2058} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4bf00:   	nop
  0x0000000106b4bf04:   	ldr	w12, [x3, #0x24]
  0x0000000106b4bf08:   	lsl	x12, x12, #3
  0x0000000106b4bf0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b4bf10:   	cbz	x12, #0xc
  0x0000000106b4bf14:   	ldr	x8, [x12, #0x40]
  0x0000000106b4bf18:   	br	x8
  0x0000000106b4bf1c:   	b	#-0x5209c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1259   66     n       java.lang.invoke.MethodHandle::linkToVirtual(LL)I (native)
 total in heap  [0x0000000106b4fc10,0x0000000106b4fda8] = 408
 relocation     [0x0000000106b4fd58,0x0000000106b4fd60] = 8
 main code      [0x0000000106b4fd80,0x0000000106b4fda8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b04e68} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b4fd80:   	nop
  0x0000000106b4fd84:   	ldr	w10, [x1, #0x8]
  0x0000000106b4fd88:   	eor	x10, x10, #0x800000000
  0x0000000106b4fd8c:   	ldr	x11, [x2, #0x10]
  0x0000000106b4fd90:   	add	x12, x10, x11, uxtx #3
  0x0000000106b4fd94:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b4fd98:   	cbz	x12, #0xc
  0x0000000106b4fd9c:   	ldr	x8, [x12, #0x40]
  0x0000000106b4fda0:   	br	x8
  0x0000000106b4fda4:   	b	#-0x55f24           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1264   67     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLL)V (native)
 total in heap  [0x0000000106b4ff10,0x0000000106b500a8] = 408
 relocation     [0x0000000106b50058,0x0000000106b50060] = 8
 main code      [0x0000000106b50080,0x0000000106b500a4] = 36
 stub code      [0x0000000106b500a4,0x0000000106b500a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b05410} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b50080:   	nop
  0x0000000106b50084:   	ldr	xzr, [x1]
  0x0000000106b50088:   	ldr	w12, [x6, #0x24]
  0x0000000106b5008c:   	lsl	x12, x12, #3
  0x0000000106b50090:   	ldr	x12, [x12, #0x10]
  0x0000000106b50094:   	cbz	x12, #0xc
  0x0000000106b50098:   	ldr	x8, [x12, #0x40]
  0x0000000106b5009c:   	br	x8
  0x0000000106b500a0:   	b	#-0x56220           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b500a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1265   68     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLL)L (native)
 total in heap  [0x0000000106b50210,0x0000000106b503a8] = 408
 relocation     [0x0000000106b50358,0x0000000106b50360] = 8
 main code      [0x0000000106b50380,0x0000000106b503a4] = 36
 stub code      [0x0000000106b503a4,0x0000000106b503a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b05528} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b50380:   	nop
  0x0000000106b50384:   	ldr	xzr, [x1]
  0x0000000106b50388:   	ldr	w12, [x6, #0x24]
  0x0000000106b5038c:   	lsl	x12, x12, #3
  0x0000000106b50390:   	ldr	x12, [x12, #0x10]
  0x0000000106b50394:   	cbz	x12, #0xc
  0x0000000106b50398:   	ldr	x8, [x12, #0x40]
  0x0000000106b5039c:   	br	x8
  0x0000000106b503a0:   	b	#-0x56520           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b503a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1272   69     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLL)V (native)
 total in heap  [0x0000000106b50510,0x0000000106b506a8] = 408
 relocation     [0x0000000106b50658,0x0000000106b50660] = 8
 main code      [0x0000000106b50680,0x0000000106b506a4] = 36
 stub code      [0x0000000106b506a4,0x0000000106b506a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b05640} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b50680:   	nop
  0x0000000106b50684:   	ldr	xzr, [x1]
  0x0000000106b50688:   	ldr	w12, [x7, #0x24]
  0x0000000106b5068c:   	lsl	x12, x12, #3
  0x0000000106b50690:   	ldr	x12, [x12, #0x10]
  0x0000000106b50694:   	cbz	x12, #0xc
  0x0000000106b50698:   	ldr	x8, [x12, #0x40]
  0x0000000106b5069c:   	br	x8
  0x0000000106b506a0:   	b	#-0x56820           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b506a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1357   70     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)L (native)
 total in heap  [0x0000000106b50810,0x0000000106b509a8] = 408
 relocation     [0x0000000106b50958,0x0000000106b50960] = 8
 main code      [0x0000000106b50980,0x0000000106b509a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b12a28} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b50980:   	nop
  0x0000000106b50984:   	ldr	w10, [x1, #0x8]
  0x0000000106b50988:   	eor	x10, x10, #0x800000000
  0x0000000106b5098c:   	ldr	x11, [x3, #0x10]
  0x0000000106b50990:   	add	x12, x10, x11, uxtx #3
  0x0000000106b50994:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b50998:   	cbz	x12, #0xc
  0x0000000106b5099c:   	ldr	x8, [x12, #0x40]
  0x0000000106b509a0:   	br	x8
  0x0000000106b509a4:   	b	#-0x56b24           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    1360   71     n       java.lang.invoke.MethodHandle::linkToInterface(LL)I (native)
 total in heap  [0x0000000106b50b10,0x0000000106b50ce8] = 472
 relocation     [0x0000000106b50c58,0x0000000106b50c60] = 8
 main code      [0x0000000106b50c80,0x0000000106b50ce4] = 100
 stub code      [0x0000000106b50ce4,0x0000000106b50ce8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b12c00} 'linkToInterface' '(Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b50c80:   	nop
  0x0000000106b50c84:   	ldr	w10, [x1, #0x8]
  0x0000000106b50c88:   	eor	x10, x10, #0x800000000
  0x0000000106b50c8c:   	ldr	w14, [x2, #0x18]
  0x0000000106b50c90:   	lsl	x14, x14, #3
  0x0000000106b50c94:   	ldr	x14, [x14, #0x10]
  0x0000000106b50c98:   	ldr	x12, [x2, #0x10]
  0x0000000106b50c9c:   	ldr	w11, [x10, #0xa0]
  0x0000000106b50ca0:   	add	x11, x10, x11, uxtx #3
  0x0000000106b50ca4:   	add	x11, x11, #0x1c0
  0x0000000106b50ca8:   	add	x10, x10, x12, uxtx #3
  0x0000000106b50cac:   	ldr	x12, [x11]
  0x0000000106b50cb0:   	cmp	x14, x12
  0x0000000106b50cb4:   	b.eq	#0x14
  0x0000000106b50cb8:   	cbz	x12, #0x28
  0x0000000106b50cbc:   	ldr	x12, [x11, #0x10]!
  0x0000000106b50cc0:   	cmp	x14, x12
  0x0000000106b50cc4:   	b.ne	#-0xc
  0x0000000106b50cc8:   	ldr	w11, [x11, #0x8]
  0x0000000106b50ccc:   	ldr	x12, [x10, w11, uxtw]
  0x0000000106b50cd0:   	cbz	x12, #0xc
  0x0000000106b50cd4:   	ldr	x8, [x12, #0x40]
  0x0000000106b50cd8:   	br	x8
  0x0000000106b50cdc:   	b	#-0x56e5c           ;   {runtime_call AbstractMethodError throw_exception}
  0x0000000106b50ce0:   	b	#-0x75760           ;   {runtime_call IncompatibleClassChangeError throw_exception}
[Stub Code]
  0x0000000106b50ce4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2169   72     n       java.lang.invoke.MethodHandle::linkToVirtual(LLL)V (native)
 total in heap  [0x0000000106b51c10,0x0000000106b51da8] = 408
 relocation     [0x0000000106b51d58,0x0000000106b51d60] = 8
 main code      [0x0000000106b51d80,0x0000000106b51da8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b64b90} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b51d80:   	nop
  0x0000000106b51d84:   	ldr	w10, [x1, #0x8]
  0x0000000106b51d88:   	eor	x10, x10, #0x800000000
  0x0000000106b51d8c:   	ldr	x11, [x3, #0x10]
  0x0000000106b51d90:   	add	x12, x10, x11, uxtx #3
  0x0000000106b51d94:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b51d98:   	cbz	x12, #0xc
  0x0000000106b51d9c:   	ldr	x8, [x12, #0x40]
  0x0000000106b51da0:   	br	x8
  0x0000000106b51da4:   	b	#-0x57f24           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2201   73     n       java.lang.invoke.MethodHandle::linkToVirtual(LLIIL)L (native)
 total in heap  [0x0000000106b51f10,0x0000000106b520a8] = 408
 relocation     [0x0000000106b52058,0x0000000106b52060] = 8
 main code      [0x0000000106b52080,0x0000000106b520a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b6e2f0} 'linkToVirtual' '(Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52080:   	nop
  0x0000000106b52084:   	ldr	w10, [x1, #0x8]
  0x0000000106b52088:   	eor	x10, x10, #0x800000000
  0x0000000106b5208c:   	ldr	x11, [x5, #0x10]
  0x0000000106b52090:   	add	x12, x10, x11, uxtx #3
  0x0000000106b52094:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b52098:   	cbz	x12, #0xc
  0x0000000106b5209c:   	ldr	x8, [x12, #0x40]
  0x0000000106b520a0:   	br	x8
  0x0000000106b520a4:   	b	#-0x58224           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2203   74     n       java.lang.invoke.MethodHandle::linkToVirtual(LIL)L (native)
 total in heap  [0x0000000106b52210,0x0000000106b523a8] = 408
 relocation     [0x0000000106b52358,0x0000000106b52360] = 8
 main code      [0x0000000106b52380,0x0000000106b523a8] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b6e608} 'linkToVirtual' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52380:   	nop
  0x0000000106b52384:   	ldr	w10, [x1, #0x8]
  0x0000000106b52388:   	eor	x10, x10, #0x800000000
  0x0000000106b5238c:   	ldr	x11, [x3, #0x10]
  0x0000000106b52390:   	add	x12, x10, x11, uxtx #3
  0x0000000106b52394:   	ldr	x12, [x12, #0x1c0]
  0x0000000106b52398:   	cbz	x12, #0xc
  0x0000000106b5239c:   	ldr	x8, [x12, #0x40]
  0x0000000106b523a0:   	br	x8
  0x0000000106b523a4:   	b	#-0x58524           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2208   75     n       java.lang.invoke.MethodHandle::linkToSpecial(LLIIL)L (native)
 total in heap  [0x0000000106b52510,0x0000000106b526a8] = 408
 relocation     [0x0000000106b52658,0x0000000106b52660] = 8
 main code      [0x0000000106b52680,0x0000000106b526a4] = 36
 stub code      [0x0000000106b526a4,0x0000000106b526a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b6e750} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4   = int
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52680:   	nop
  0x0000000106b52684:   	ldr	xzr, [x1]
  0x0000000106b52688:   	ldr	w12, [x5, #0x24]
  0x0000000106b5268c:   	lsl	x12, x12, #3
  0x0000000106b52690:   	ldr	x12, [x12, #0x10]
  0x0000000106b52694:   	cbz	x12, #0xc
  0x0000000106b52698:   	ldr	x8, [x12, #0x40]
  0x0000000106b5269c:   	br	x8
  0x0000000106b526a0:   	b	#-0x58820           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b526a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2273   76     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLL)I (native)
 total in heap  [0x0000000106b52810,0x0000000106b529a8] = 408
 relocation     [0x0000000106b52958,0x0000000106b52960] = 8
 main code      [0x0000000106b52980,0x0000000106b529a4] = 36
 stub code      [0x0000000106b529a4,0x0000000106b529a8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b770e0} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52980:   	nop
  0x0000000106b52984:   	ldr	xzr, [x1]
  0x0000000106b52988:   	ldr	w12, [x5, #0x24]
  0x0000000106b5298c:   	lsl	x12, x12, #3
  0x0000000106b52990:   	ldr	x12, [x12, #0x10]
  0x0000000106b52994:   	cbz	x12, #0xc
  0x0000000106b52998:   	ldr	x8, [x12, #0x40]
  0x0000000106b5299c:   	br	x8
  0x0000000106b529a0:   	b	#-0x58b20           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b529a4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2282   77     n       java.lang.invoke.MethodHandle::linkToStatic(IL)L (native)
 total in heap  [0x0000000106b52b10,0x0000000106b52ca0] = 400
 relocation     [0x0000000106b52c58,0x0000000106b52c60] = 8
 main code      [0x0000000106b52c80,0x0000000106b52ca0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b79598} 'linkToStatic' '(ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1   = int
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52c80:   	nop
  0x0000000106b52c84:   	ldr	w12, [x2, #0x24]
  0x0000000106b52c88:   	lsl	x12, x12, #3
  0x0000000106b52c8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b52c90:   	cbz	x12, #0xc
  0x0000000106b52c94:   	ldr	x8, [x12, #0x40]
  0x0000000106b52c98:   	br	x8
  0x0000000106b52c9c:   	b	#-0x58e1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2361   78     n       java.lang.invoke.MethodHandle::linkToSpecial(LLL)I (native)
 total in heap  [0x0000000106b52e10,0x0000000106b52fa8] = 408
 relocation     [0x0000000106b52f58,0x0000000106b52f60] = 8
 main code      [0x0000000106b52f80,0x0000000106b52fa4] = 36
 stub code      [0x0000000106b52fa4,0x0000000106b52fa8] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b92f90} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b52f80:   	nop
  0x0000000106b52f84:   	ldr	xzr, [x1]
  0x0000000106b52f88:   	ldr	w12, [x3, #0x24]
  0x0000000106b52f8c:   	lsl	x12, x12, #3
  0x0000000106b52f90:   	ldr	x12, [x12, #0x10]
  0x0000000106b52f94:   	cbz	x12, #0xc
  0x0000000106b52f98:   	ldr	x8, [x12, #0x40]
  0x0000000106b52f9c:   	br	x8
  0x0000000106b52fa0:   	b	#-0x59120           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b52fa4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2454   79     n       java.lang.invoke.MethodHandle::linkToStatic(LIIL)L (native)
 total in heap  [0x0000000106b53110,0x0000000106b532a0] = 400
 relocation     [0x0000000106b53258,0x0000000106b53260] = 8
 main code      [0x0000000106b53280,0x0000000106b532a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b9a000} 'linkToStatic' '(Ljava/lang/Object;IILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b53280:   	nop
  0x0000000106b53284:   	ldr	w12, [x4, #0x24]
  0x0000000106b53288:   	lsl	x12, x12, #3
  0x0000000106b5328c:   	ldr	x12, [x12, #0x10]
  0x0000000106b53290:   	cbz	x12, #0xc
  0x0000000106b53294:   	ldr	x8, [x12, #0x40]
  0x0000000106b53298:   	br	x8
  0x0000000106b5329c:   	b	#-0x5941c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2463   80     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)J (native)
 total in heap  [0x0000000106b53410,0x0000000106b535a0] = 400
 relocation     [0x0000000106b53558,0x0000000106b53560] = 8
 main code      [0x0000000106b53580,0x0000000106b535a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b9bf18} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b53580:   	nop
  0x0000000106b53584:   	ldr	w12, [x4, #0x24]
  0x0000000106b53588:   	lsl	x12, x12, #3
  0x0000000106b5358c:   	ldr	x12, [x12, #0x10]
  0x0000000106b53590:   	cbz	x12, #0xc
  0x0000000106b53594:   	ldr	x8, [x12, #0x40]
  0x0000000106b53598:   	br	x8
  0x0000000106b5359c:   	b	#-0x5971c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2598   81     n       java.lang.invoke.MethodHandle::linkToStatic(L)V (native)
 total in heap  [0x0000000106b53a90,0x0000000106b53c20] = 400
 relocation     [0x0000000106b53bd8,0x0000000106b53be0] = 8
 main code      [0x0000000106b53c00,0x0000000106b53c20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116b9e268} 'linkToStatic' '(Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b53c00:   	nop
  0x0000000106b53c04:   	ldr	w12, [x1, #0x24]
  0x0000000106b53c08:   	lsl	x12, x12, #3
  0x0000000106b53c0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b53c10:   	cbz	x12, #0xc
  0x0000000106b53c14:   	ldr	x8, [x12, #0x40]
  0x0000000106b53c18:   	br	x8
  0x0000000106b53c1c:   	b	#-0x59d9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2717   82     n       java.lang.invoke.MethodHandle::linkToStatic(LLLL)I (native)
 total in heap  [0x0000000106b54110,0x0000000106b542a0] = 400
 relocation     [0x0000000106b54258,0x0000000106b54260] = 8
 main code      [0x0000000106b54280,0x0000000106b542a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116ba15a0} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b54280:   	nop
  0x0000000106b54284:   	ldr	w12, [x4, #0x24]
  0x0000000106b54288:   	lsl	x12, x12, #3
  0x0000000106b5428c:   	ldr	x12, [x12, #0x10]
  0x0000000106b54290:   	cbz	x12, #0xc
  0x0000000106b54294:   	ldr	x8, [x12, #0x40]
  0x0000000106b54298:   	br	x8
  0x0000000106b5429c:   	b	#-0x5a41c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2851   83     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLLLL)L (native)
 total in heap  [0x0000000106b54790,0x0000000106b54930] = 416
 relocation     [0x0000000106b548d8,0x0000000106b548e0] = 8
 main code      [0x0000000106b54900,0x0000000106b54930] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116ba7748} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm6:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  # parm7:    [sp+0x0]   = 'java/lang/Object'  (sp of caller)
  0x0000000106b54900:   	nop
  0x0000000106b54904:   	ldr	w12, [x1, #0x14]
  0x0000000106b54908:   	lsl	x12, x12, #3
  0x0000000106b5490c:   	ldr	w12, [x12, #0x28]
  0x0000000106b54910:   	lsl	x12, x12, #3
  0x0000000106b54914:   	ldr	w12, [x12, #0x24]
  0x0000000106b54918:   	lsl	x12, x12, #3
  0x0000000106b5491c:   	ldr	x12, [x12, #0x10]
  0x0000000106b54920:   	cbz	x12, #0xc
  0x0000000106b54924:   	ldr	x8, [x12, #0x40]
  0x0000000106b54928:   	br	x8
  0x0000000106b5492c:   	b	#-0x5aaac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2853   84     n       java.lang.invoke.MethodHandle::linkToSpecial(LLLLLLLLLL)L (native)
 total in heap  [0x0000000106b54a90,0x0000000106b54c28] = 408
 relocation     [0x0000000106b54bd8,0x0000000106b54be0] = 8
 main code      [0x0000000106b54c00,0x0000000106b54c28] = 40

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116ba7860} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  # parm8:    [sp+0x0]   = 'java/lang/Object'  (sp of caller)
  # parm9:    [sp+0x8]   = 'java/lang/invoke/MemberName'
  0x0000000106b54c00:   	nop
  0x0000000106b54c04:   	ldr	x19, [sp, #0x8]
  0x0000000106b54c08:   	ldr	xzr, [x1]
  0x0000000106b54c0c:   	ldr	w12, [x19, #0x24]
  0x0000000106b54c10:   	lsl	x12, x12, #3
  0x0000000106b54c14:   	ldr	x12, [x12, #0x10]
  0x0000000106b54c18:   	cbz	x12, #0xc
  0x0000000106b54c1c:   	ldr	x8, [x12, #0x40]
  0x0000000106b54c20:   	br	x8
  0x0000000106b54c24:   	b	#-0x5ada4           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2873   85     n       java.lang.invoke.MethodHandle::linkToStatic(LIL)L (native)
 total in heap  [0x0000000106b54d90,0x0000000106b54f20] = 400
 relocation     [0x0000000106b54ed8,0x0000000106b54ee0] = 8
 main code      [0x0000000106b54f00,0x0000000106b54f20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bae628} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b54f00:   	nop
  0x0000000106b54f04:   	ldr	w12, [x3, #0x24]
  0x0000000106b54f08:   	lsl	x12, x12, #3
  0x0000000106b54f0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b54f10:   	cbz	x12, #0xc
  0x0000000106b54f14:   	ldr	x8, [x12, #0x40]
  0x0000000106b54f18:   	br	x8
  0x0000000106b54f1c:   	b	#-0x5b09c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2874   86     n       java.lang.invoke.MethodHandle::invokeBasic(LI)L (native)
 total in heap  [0x0000000106b55090,0x0000000106b55230] = 416
 relocation     [0x0000000106b551d8,0x0000000106b551e0] = 8
 main code      [0x0000000106b55200,0x0000000106b55230] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bae770} 'invokeBasic' '(Ljava/lang/Object;I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b55200:   	nop
  0x0000000106b55204:   	ldr	w12, [x1, #0x14]
  0x0000000106b55208:   	lsl	x12, x12, #3
  0x0000000106b5520c:   	ldr	w12, [x12, #0x28]
  0x0000000106b55210:   	lsl	x12, x12, #3
  0x0000000106b55214:   	ldr	w12, [x12, #0x24]
  0x0000000106b55218:   	lsl	x12, x12, #3
  0x0000000106b5521c:   	ldr	x12, [x12, #0x10]
  0x0000000106b55220:   	cbz	x12, #0xc
  0x0000000106b55224:   	ldr	x8, [x12, #0x40]
  0x0000000106b55228:   	br	x8
  0x0000000106b5522c:   	b	#-0x5b3ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2875   87     n       java.lang.invoke.MethodHandle::linkToSpecial(LLIL)L (native)
 total in heap  [0x0000000106b55390,0x0000000106b55528] = 408
 relocation     [0x0000000106b554d8,0x0000000106b554e0] = 8
 main code      [0x0000000106b55500,0x0000000106b55524] = 36
 stub code      [0x0000000106b55524,0x0000000106b55528] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116baec38} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b55500:   	nop
  0x0000000106b55504:   	ldr	xzr, [x1]
  0x0000000106b55508:   	ldr	w12, [x4, #0x24]
  0x0000000106b5550c:   	lsl	x12, x12, #3
  0x0000000106b55510:   	ldr	x12, [x12, #0x10]
  0x0000000106b55514:   	cbz	x12, #0xc
  0x0000000106b55518:   	ldr	x8, [x12, #0x40]
  0x0000000106b5551c:   	br	x8
  0x0000000106b55520:   	b	#-0x5b6a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b55524:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2885   88     n       java.lang.invoke.MethodHandle::linkToStatic(LILL)V (native)
 total in heap  [0x0000000106b55690,0x0000000106b55820] = 400
 relocation     [0x0000000106b557d8,0x0000000106b557e0] = 8
 main code      [0x0000000106b55800,0x0000000106b55820] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bb3ca8} 'linkToStatic' '(Ljava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b55800:   	nop
  0x0000000106b55804:   	ldr	w12, [x4, #0x24]
  0x0000000106b55808:   	lsl	x12, x12, #3
  0x0000000106b5580c:   	ldr	x12, [x12, #0x10]
  0x0000000106b55810:   	cbz	x12, #0xc
  0x0000000106b55814:   	ldr	x8, [x12, #0x40]
  0x0000000106b55818:   	br	x8
  0x0000000106b5581c:   	b	#-0x5b99c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2886   89     n       java.lang.invoke.MethodHandle::invokeBasic(LIL)V (native)
 total in heap  [0x0000000106b55990,0x0000000106b55b30] = 416
 relocation     [0x0000000106b55ad8,0x0000000106b55ae0] = 8
 main code      [0x0000000106b55b00,0x0000000106b55b30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bb3f28} 'invokeBasic' '(Ljava/lang/Object;ILjava/lang/Object;)V' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3   = int
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b55b00:   	nop
  0x0000000106b55b04:   	ldr	w12, [x1, #0x14]
  0x0000000106b55b08:   	lsl	x12, x12, #3
  0x0000000106b55b0c:   	ldr	w12, [x12, #0x28]
  0x0000000106b55b10:   	lsl	x12, x12, #3
  0x0000000106b55b14:   	ldr	w12, [x12, #0x24]
  0x0000000106b55b18:   	lsl	x12, x12, #3
  0x0000000106b55b1c:   	ldr	x12, [x12, #0x10]
  0x0000000106b55b20:   	cbz	x12, #0xc
  0x0000000106b55b24:   	ldr	x8, [x12, #0x40]
  0x0000000106b55b28:   	br	x8
  0x0000000106b55b2c:   	b	#-0x5bcac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2887   90     n       java.lang.invoke.MethodHandle::linkToSpecial(LLILL)V (native)
 total in heap  [0x0000000106b55c90,0x0000000106b55e28] = 408
 relocation     [0x0000000106b55dd8,0x0000000106b55de0] = 8
 main code      [0x0000000106b55e00,0x0000000106b55e24] = 36
 stub code      [0x0000000106b55e24,0x0000000106b55e28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bb4040} 'linkToSpecial' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/Object;Ljava/lang/invoke/MemberName;)V' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b55e00:   	nop
  0x0000000106b55e04:   	ldr	xzr, [x1]
  0x0000000106b55e08:   	ldr	w12, [x5, #0x24]
  0x0000000106b55e0c:   	lsl	x12, x12, #3
  0x0000000106b55e10:   	ldr	x12, [x12, #0x10]
  0x0000000106b55e14:   	cbz	x12, #0xc
  0x0000000106b55e18:   	ldr	x8, [x12, #0x40]
  0x0000000106b55e1c:   	br	x8
  0x0000000106b55e20:   	b	#-0x5bfa0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b55e24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2889   91     n       java.lang.invoke.MethodHandle::invokeBasic(L)I (native)
 total in heap  [0x0000000106b55f90,0x0000000106b56130] = 416
 relocation     [0x0000000106b560d8,0x0000000106b560e0] = 8
 main code      [0x0000000106b56100,0x0000000106b56130] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bb4158} 'invokeBasic' '(Ljava/lang/Object;)I' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b56100:   	nop
  0x0000000106b56104:   	ldr	w12, [x1, #0x14]
  0x0000000106b56108:   	lsl	x12, x12, #3
  0x0000000106b5610c:   	ldr	w12, [x12, #0x28]
  0x0000000106b56110:   	lsl	x12, x12, #3
  0x0000000106b56114:   	ldr	w12, [x12, #0x24]
  0x0000000106b56118:   	lsl	x12, x12, #3
  0x0000000106b5611c:   	ldr	x12, [x12, #0x10]
  0x0000000106b56120:   	cbz	x12, #0xc
  0x0000000106b56124:   	ldr	x8, [x12, #0x40]
  0x0000000106b56128:   	br	x8
  0x0000000106b5612c:   	b	#-0x5c2ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2897   92     n       java.lang.invoke.MethodHandle::linkToStatic(LLLLLLLL)L (native)
 total in heap  [0x0000000106b56610,0x0000000106b567a0] = 400
 relocation     [0x0000000106b56758,0x0000000106b56760] = 8
 main code      [0x0000000106b56780,0x0000000106b567a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc5758} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm6:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm7:    c_rarg0:c_rarg0 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b56780:   	nop
  0x0000000106b56784:   	ldr	w12, [x0, #0x24]
  0x0000000106b56788:   	lsl	x12, x12, #3
  0x0000000106b5678c:   	ldr	x12, [x12, #0x10]
  0x0000000106b56790:   	cbz	x12, #0xc
  0x0000000106b56794:   	ldr	x8, [x12, #0x40]
  0x0000000106b56798:   	br	x8
  0x0000000106b5679c:   	b	#-0x5c91c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2899   93     n       java.lang.invoke.MethodHandle::invokeBasic(LLLLLLL)L (native)
 total in heap  [0x0000000106b56910,0x0000000106b56ab0] = 416
 relocation     [0x0000000106b56a58,0x0000000106b56a60] = 8
 main code      [0x0000000106b56a80,0x0000000106b56ab0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc58a0} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm4:    c_rarg6:c_rarg6 
                        = 'java/lang/Object'
  # parm5:    c_rarg7:c_rarg7 
                        = 'java/lang/Object'
  # parm6:    c_rarg0:c_rarg0 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b56a80:   	nop
  0x0000000106b56a84:   	ldr	w12, [x1, #0x14]
  0x0000000106b56a88:   	lsl	x12, x12, #3
  0x0000000106b56a8c:   	ldr	w12, [x12, #0x28]
  0x0000000106b56a90:   	lsl	x12, x12, #3
  0x0000000106b56a94:   	ldr	w12, [x12, #0x24]
  0x0000000106b56a98:   	lsl	x12, x12, #3
  0x0000000106b56a9c:   	ldr	x12, [x12, #0x10]
  0x0000000106b56aa0:   	cbz	x12, #0xc
  0x0000000106b56aa4:   	ldr	x8, [x12, #0x40]
  0x0000000106b56aa8:   	br	x8
  0x0000000106b56aac:   	b	#-0x5cc2c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2903   94     n       java.lang.invoke.MethodHandle::linkToStatic(LLIL)L (native)
 total in heap  [0x0000000106b56c10,0x0000000106b56da0] = 400
 relocation     [0x0000000106b56d58,0x0000000106b56d60] = 8
 main code      [0x0000000106b56d80,0x0000000106b56da0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc7e28} 'linkToStatic' '(Ljava/lang/Object;Ljava/lang/Object;ILjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b56d80:   	nop
  0x0000000106b56d84:   	ldr	w12, [x4, #0x24]
  0x0000000106b56d88:   	lsl	x12, x12, #3
  0x0000000106b56d8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b56d90:   	cbz	x12, #0xc
  0x0000000106b56d94:   	ldr	x8, [x12, #0x40]
  0x0000000106b56d98:   	br	x8
  0x0000000106b56d9c:   	b	#-0x5cf1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2905   95     n       java.lang.invoke.MethodHandle::invokeBasic(LLI)L (native)
 total in heap  [0x0000000106b56f10,0x0000000106b570b0] = 416
 relocation     [0x0000000106b57058,0x0000000106b57060] = 8
 main code      [0x0000000106b57080,0x0000000106b570b0] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc7f70} 'invokeBasic' '(Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b57080:   	nop
  0x0000000106b57084:   	ldr	w12, [x1, #0x14]
  0x0000000106b57088:   	lsl	x12, x12, #3
  0x0000000106b5708c:   	ldr	w12, [x12, #0x28]
  0x0000000106b57090:   	lsl	x12, x12, #3
  0x0000000106b57094:   	ldr	w12, [x12, #0x24]
  0x0000000106b57098:   	lsl	x12, x12, #3
  0x0000000106b5709c:   	ldr	x12, [x12, #0x10]
  0x0000000106b570a0:   	cbz	x12, #0xc
  0x0000000106b570a4:   	ldr	x8, [x12, #0x40]
  0x0000000106b570a8:   	br	x8
  0x0000000106b570ac:   	b	#-0x5d22c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2907   96     n       java.lang.invoke.MethodHandle::linkToStatic(JJL)I (native)
 total in heap  [0x0000000106b57210,0x0000000106b573a0] = 400
 relocation     [0x0000000106b57358,0x0000000106b57360] = 8
 main code      [0x0000000106b57380,0x0000000106b573a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc8b10} 'linkToStatic' '(JJLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b57380:   	nop
  0x0000000106b57384:   	ldr	w12, [x3, #0x24]
  0x0000000106b57388:   	lsl	x12, x12, #3
  0x0000000106b5738c:   	ldr	x12, [x12, #0x10]
  0x0000000106b57390:   	cbz	x12, #0xc
  0x0000000106b57394:   	ldr	x8, [x12, #0x40]
  0x0000000106b57398:   	br	x8
  0x0000000106b5739c:   	b	#-0x5d51c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2908   97     n       java.lang.invoke.MethodHandle::linkToStatic(FFL)I (native)
 total in heap  [0x0000000106b57890,0x0000000106b57a20] = 400
 relocation     [0x0000000106b579d8,0x0000000106b579e0] = 8
 main code      [0x0000000106b57a00,0x0000000106b57a20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc8eb0} 'linkToStatic' '(FFLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    v1        = float
  # parm2:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b57a00:   	nop
  0x0000000106b57a04:   	ldr	w12, [x1, #0x24]
  0x0000000106b57a08:   	lsl	x12, x12, #3
  0x0000000106b57a0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b57a10:   	cbz	x12, #0xc
  0x0000000106b57a14:   	ldr	x8, [x12, #0x40]
  0x0000000106b57a18:   	br	x8
  0x0000000106b57a1c:   	b	#-0x5db9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2910   98     n       java.lang.invoke.MethodHandle::linkToStatic(DDL)I (native)
 total in heap  [0x0000000106b58290,0x0000000106b58420] = 400
 relocation     [0x0000000106b583d8,0x0000000106b583e0] = 8
 main code      [0x0000000106b58400,0x0000000106b58420] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc9258} 'linkToStatic' '(DDLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    v1:v1     = double
  # parm2:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b58400:   	nop
  0x0000000106b58404:   	ldr	w12, [x1, #0x24]
  0x0000000106b58408:   	lsl	x12, x12, #3
  0x0000000106b5840c:   	ldr	x12, [x12, #0x10]
  0x0000000106b58410:   	cbz	x12, #0xc
  0x0000000106b58414:   	ldr	x8, [x12, #0x40]
  0x0000000106b58418:   	br	x8
  0x0000000106b5841c:   	b	#-0x5e59c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2911   99     n       java.lang.invoke.MethodHandle::linkToStatic(JL)I (native)
 total in heap  [0x0000000106b58910,0x0000000106b58aa0] = 400
 relocation     [0x0000000106b58a58,0x0000000106b58a60] = 8
 main code      [0x0000000106b58a80,0x0000000106b58aa0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc9370} 'linkToStatic' '(JLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b58a80:   	nop
  0x0000000106b58a84:   	ldr	w12, [x2, #0x24]
  0x0000000106b58a88:   	lsl	x12, x12, #3
  0x0000000106b58a8c:   	ldr	x12, [x12, #0x10]
  0x0000000106b58a90:   	cbz	x12, #0xc
  0x0000000106b58a94:   	ldr	x8, [x12, #0x40]
  0x0000000106b58a98:   	br	x8
  0x0000000106b58a9c:   	b	#-0x5ec1c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2912  100     n       java.lang.invoke.MethodHandle::linkToStatic(FL)I (native)
 total in heap  [0x0000000106b58f90,0x0000000106b59120] = 400
 relocation     [0x0000000106b590d8,0x0000000106b590e0] = 8
 main code      [0x0000000106b59100,0x0000000106b59120] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc9488} 'linkToStatic' '(FLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b59100:   	nop
  0x0000000106b59104:   	ldr	w12, [x1, #0x24]
  0x0000000106b59108:   	lsl	x12, x12, #3
  0x0000000106b5910c:   	ldr	x12, [x12, #0x10]
  0x0000000106b59110:   	cbz	x12, #0xc
  0x0000000106b59114:   	ldr	x8, [x12, #0x40]
  0x0000000106b59118:   	br	x8
  0x0000000106b5911c:   	b	#-0x5f29c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2913  101     n       java.lang.invoke.MethodHandle::linkToStatic(DL)I (native)
 total in heap  [0x0000000106b59290,0x0000000106b59420] = 400
 relocation     [0x0000000106b593d8,0x0000000106b593e0] = 8
 main code      [0x0000000106b59400,0x0000000106b59420] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc95a0} 'linkToStatic' '(DLjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b59400:   	nop
  0x0000000106b59404:   	ldr	w12, [x1, #0x24]
  0x0000000106b59408:   	lsl	x12, x12, #3
  0x0000000106b5940c:   	ldr	x12, [x12, #0x10]
  0x0000000106b59410:   	cbz	x12, #0xc
  0x0000000106b59414:   	ldr	x8, [x12, #0x40]
  0x0000000106b59418:   	br	x8
  0x0000000106b5941c:   	b	#-0x5f59c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2914  102     n       java.lang.invoke.MethodHandle::linkToStatic(FL)L (native)
 total in heap  [0x0000000106b59590,0x0000000106b59720] = 400
 relocation     [0x0000000106b596d8,0x0000000106b596e0] = 8
 main code      [0x0000000106b59700,0x0000000106b59720] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc96b8} 'linkToStatic' '(FLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0        = float
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b59700:   	nop
  0x0000000106b59704:   	ldr	w12, [x1, #0x24]
  0x0000000106b59708:   	lsl	x12, x12, #3
  0x0000000106b5970c:   	ldr	x12, [x12, #0x10]
  0x0000000106b59710:   	cbz	x12, #0xc
  0x0000000106b59714:   	ldr	x8, [x12, #0x40]
  0x0000000106b59718:   	br	x8
  0x0000000106b5971c:   	b	#-0x5f89c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2915  103     n       java.lang.invoke.MethodHandle::linkToStatic(DL)L (native)
 total in heap  [0x0000000106b59890,0x0000000106b59a20] = 400
 relocation     [0x0000000106b599d8,0x0000000106b599e0] = 8
 main code      [0x0000000106b59a00,0x0000000106b59a20] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bc97d0} 'linkToStatic' '(DLjava/lang/invoke/MemberName;)Ljava/lang/Object;' in 'java/lang/invoke/MethodHandle'
  # parm0:    v0:v0     = double
  # parm1:    c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b59a00:   	nop
  0x0000000106b59a04:   	ldr	w12, [x1, #0x24]
  0x0000000106b59a08:   	lsl	x12, x12, #3
  0x0000000106b59a0c:   	ldr	x12, [x12, #0x10]
  0x0000000106b59a10:   	cbz	x12, #0xc
  0x0000000106b59a14:   	ldr	x8, [x12, #0x40]
  0x0000000106b59a18:   	br	x8
  0x0000000106b59a1c:   	b	#-0x5fb9c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2917  104     n       java.lang.invoke.MethodHandle::invokeBasic(II)I (native)
 total in heap  [0x0000000106b59b90,0x0000000106b59d30] = 416
 relocation     [0x0000000106b59cd8,0x0000000106b59ce0] = 8
 main code      [0x0000000106b59d00,0x0000000106b59d30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bca590} 'invokeBasic' '(II)I' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2   = int
  # parm1:    c_rarg3   = int
  #           [sp+0x0]  (sp of caller)
  0x0000000106b59d00:   	nop
  0x0000000106b59d04:   	ldr	w12, [x1, #0x14]
  0x0000000106b59d08:   	lsl	x12, x12, #3
  0x0000000106b59d0c:   	ldr	w12, [x12, #0x28]
  0x0000000106b59d10:   	lsl	x12, x12, #3
  0x0000000106b59d14:   	ldr	w12, [x12, #0x24]
  0x0000000106b59d18:   	lsl	x12, x12, #3
  0x0000000106b59d1c:   	ldr	x12, [x12, #0x10]
  0x0000000106b59d20:   	cbz	x12, #0xc
  0x0000000106b59d24:   	ldr	x8, [x12, #0x40]
  0x0000000106b59d28:   	br	x8
  0x0000000106b59d2c:   	b	#-0x5feac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2918  105     n       java.lang.invoke.MethodHandle::linkToSpecial(LIIL)I (native)
 total in heap  [0x0000000106b59e90,0x0000000106b5a028] = 408
 relocation     [0x0000000106b59fd8,0x0000000106b59fe0] = 8
 main code      [0x0000000106b5a000,0x0000000106b5a024] = 36
 stub code      [0x0000000106b5a024,0x0000000106b5a028] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bca6a8} 'linkToSpecial' '(Ljava/lang/Object;IILjava/lang/invoke/MemberName;)I' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2   = int
  # parm2:    c_rarg3   = int
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5a000:   	nop
  0x0000000106b5a004:   	ldr	xzr, [x1]
  0x0000000106b5a008:   	ldr	w12, [x4, #0x24]
  0x0000000106b5a00c:   	lsl	x12, x12, #3
  0x0000000106b5a010:   	ldr	x12, [x12, #0x10]
  0x0000000106b5a014:   	cbz	x12, #0xc
  0x0000000106b5a018:   	ldr	x8, [x12, #0x40]
  0x0000000106b5a01c:   	br	x8
  0x0000000106b5a020:   	b	#-0x601a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b5a024:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2937  106     n       java.lang.invoke.MethodHandle::invokeBasic(L)V (native)
 total in heap  [0x0000000106b5a190,0x0000000106b5a330] = 416
 relocation     [0x0000000106b5a2d8,0x0000000106b5a2e0] = 8
 main code      [0x0000000106b5a300,0x0000000106b5a330] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bce808} 'invokeBasic' '(Ljava/lang/Object;)V' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5a300:   	nop
  0x0000000106b5a304:   	ldr	w12, [x1, #0x14]
  0x0000000106b5a308:   	lsl	x12, x12, #3
  0x0000000106b5a30c:   	ldr	w12, [x12, #0x28]
  0x0000000106b5a310:   	lsl	x12, x12, #3
  0x0000000106b5a314:   	ldr	w12, [x12, #0x24]
  0x0000000106b5a318:   	lsl	x12, x12, #3
  0x0000000106b5a31c:   	ldr	x12, [x12, #0x10]
  0x0000000106b5a320:   	cbz	x12, #0xc
  0x0000000106b5a324:   	ldr	x8, [x12, #0x40]
  0x0000000106b5a328:   	br	x8
  0x0000000106b5a32c:   	b	#-0x604ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    2941  107             Main::logfn (5 bytes)
 total in heap  [0x0000000106b5ec10,0x0000000106b5ee98] = 648
 relocation     [0x0000000106b5ed58,0x0000000106b5ed68] = 16
 main code      [0x0000000106b5ed80,0x0000000106b5edd8] = 88
 stub code      [0x0000000106b5edd8,0x0000000106b5ede8] = 16
 oops           [0x0000000106b5ede8,0x0000000106b5edf0] = 8
 metadata       [0x0000000106b5edf0,0x0000000106b5ee08] = 24
 scopes data    [0x0000000106b5ee08,0x0000000106b5ee40] = 56
 scopes pcs     [0x0000000106b5ee40,0x0000000106b5ee90] = 80
 dependencies   [0x0000000106b5ee90,0x0000000106b5ee98] = 8

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116e90658} 'logfn' '(D)D' in 'Main'
  # parm0:    v0:v0     = double
  #           [sp+0x30]  (sp of caller)
  0x0000000106b5ed80:   	nop
  0x0000000106b5ed84:   	sub	x9, sp, #0x14, lsl #12
  0x0000000106b5ed88:   	str	xzr, [x9]
  0x0000000106b5ed8c:   	sub	sp, sp, #0x30
  0x0000000106b5ed90:   	stp	x29, x30, [sp, #0x20]
  0x0000000106b5ed94:   	ldr	w8, #0x3c
  0x0000000106b5ed98:   	ldr	w9, [x28, #0x20]
  0x0000000106b5ed9c:   	cmp	x8, x9
  0x0000000106b5eda0:   	b.ne	#0x1c               ;*synchronization entry
                                                            ; - Main::logfn@-1 (line 24)
  0x0000000106b5eda4:   	mov	w1, #0x21
  0x0000000106b5eda8:   	str	d0, [sp]
  0x0000000106b5edac:   	bl	#-0x638ac           ; ImmutableOopMap {}
                                                            ;*invokestatic compute {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.StrictMath::log@1 (line 269)
                                                            ; - java.lang.Math::log@1 (line 357)
                                                            ; - Main::logfn@1 (line 24)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5edb0:   	nop                         ;   {other}
  0x0000000106b5edb4:   	movk	xzr, #0x1a0
  0x0000000106b5edb8:   	movk	xzr, #0x0           ;*invokestatic compute {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.StrictMath::log@1 (line 269)
                                                            ; - java.lang.Math::log@1 (line 357)
                                                            ; - Main::logfn@1 (line 24)
  0x0000000106b5edbc:   	mov	x8, #0xf400
  0x0000000106b5edc0:   	movk	x8, #0x6ad, lsl #16
  0x0000000106b5edc4:   	movk	x8, #0x1, lsl #32
  0x0000000106b5edc8:   	blr	x8
  0x0000000106b5edcc:   	b	#-0x28
  0x0000000106b5edd0:   	udf	#0x1                ;   {other}
  0x0000000106b5edd4:   	udf	#0x0
[Exception Handler]
  0x0000000106b5edd8:   	b	#-0x3fc58           ;   {no_reloc}
[Deopt Handler Code]
  0x0000000106b5eddc:   	adr	x30, #0x0
  0x0000000106b5ede0:   	b	#-0x63c20           ;   {runtime_call DeoptimizationBlob}
  0x0000000106b5ede4:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    2942  108             Main::logfn (5 bytes)
 total in heap  [0x0000000106b5ce90,0x0000000106b5d168] = 728
 relocation     [0x0000000106b5cfd8,0x0000000106b5cff8] = 32
 main code      [0x0000000106b5d000,0x0000000106b5d080] = 128
 stub code      [0x0000000106b5d080,0x0000000106b5d0b0] = 48
 oops           [0x0000000106b5d0b0,0x0000000106b5d0b8] = 8
 metadata       [0x0000000106b5d0b8,0x0000000106b5d0d8] = 32
 scopes data    [0x0000000106b5d0d8,0x0000000106b5d108] = 48
 scopes pcs     [0x0000000106b5d108,0x0000000106b5d148] = 64
 dependencies   [0x0000000106b5d148,0x0000000106b5d150] = 8
 handler table  [0x0000000106b5d150,0x0000000106b5d168] = 24

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116e90658} 'logfn' '(D)D' in 'Main'
  # parm0:    v0:v0     = double
  #           [sp+0x20]  (sp of caller)
  0x0000000106b5d000:   	nop
  0x0000000106b5d004:   	sub	x9, sp, #0x14, lsl #12
  0x0000000106b5d008:   	str	xzr, [x9]
  0x0000000106b5d00c:   	sub	sp, sp, #0x20
  0x0000000106b5d010:   	stp	x29, x30, [sp, #0x10]
  0x0000000106b5d014:   	ldr	w8, #0x68
  0x0000000106b5d018:   	ldr	w9, [x28, #0x20]
  0x0000000106b5d01c:   	cmp	x8, x9
  0x0000000106b5d020:   	b.ne	#0x48
  0x0000000106b5d024:   	bl	#0x68               ; ImmutableOopMap {}
                                                            ;*invokestatic compute {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.StrictMath::log@1 (line 269)
                                                            ; - java.lang.Math::log@1 (line 357)
                                                            ; - Main::logfn@1 (line 24)
                                                            ;   {static_call}
  0x0000000106b5d028:   	nop                         ;   {other}
  0x0000000106b5d02c:   	movk	xzr, #0x198
  0x0000000106b5d030:   	movk	xzr, #0x0
  0x0000000106b5d034:   	ldp	x29, x30, [sp, #0x10]
  0x0000000106b5d038:   	add	sp, sp, #0x20
  0x0000000106b5d03c:   	ldr	x8, [x28, #0x378]   ;   {poll_return}
  0x0000000106b5d040:   	cmp	sp, x8
  0x0000000106b5d044:   	b.hi	#0x18
  0x0000000106b5d048:   	ret                         ;*invokestatic compute {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.StrictMath::log@1 (line 269)
                                                            ; - java.lang.Math::log@1 (line 357)
                                                            ; - Main::logfn@1 (line 24)
  0x0000000106b5d04c:   	mov	x1, x0
  0x0000000106b5d050:   	ldp	x29, x30, [sp, #0x10]
  0x0000000106b5d054:   	add	sp, sp, #0x20
  0x0000000106b5d058:   	b	#-0x3cf58           ;   {runtime_call _rethrow_Java}
  0x0000000106b5d05c:   	adr	x8, #-0x20          ;   {internal_word}
  0x0000000106b5d060:   	str	x8, [x28, #0x390]
  0x0000000106b5d064:   	b	#-0x621e4           ;   {runtime_call SafepointBlob}
  0x0000000106b5d068:   	mov	x8, #0xf400
  0x0000000106b5d06c:   	movk	x8, #0x6ad, lsl #16
  0x0000000106b5d070:   	movk	x8, #0x1, lsl #32
  0x0000000106b5d074:   	blr	x8
  0x0000000106b5d078:   	b	#-0x54
  0x0000000106b5d07c:   	udf	#0x1                ;   {other}
[Exception Handler]
  0x0000000106b5d080:   	b	#-0x3df00           ;   {no_reloc}
[Deopt Handler Code]
  0x0000000106b5d084:   	adr	x30, #0x0
  0x0000000106b5d088:   	b	#-0x61ec8           ;   {runtime_call DeoptimizationBlob}
  0x0000000106b5d08c:   	isb                         ;   {static_stub}
  0x0000000106b5d090:   	mov	x12, #0x6758        ;   {metadata({method} {0x0000000116bd6758} 'compute' '(D)D' in 'java/lang/FdLibm$Log')}
  0x0000000106b5d094:   	movk	x12, #0x16bd, lsl #16
  0x0000000106b5d098:   	movk	x12, #0x1, lsl #32
  0x0000000106b5d09c:   	mov	x8, #0xc654
  0x0000000106b5d0a0:   	movk	x8, #0x6af, lsl #16
  0x0000000106b5d0a4:   	movk	x8, #0x1, lsl #32
  0x0000000106b5d0a8:   	br	x8
  0x0000000106b5d0ac:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    2944  109 %           Main::compute @ 2 (20 bytes)
 total in heap  [0x0000000106b5ca90,0x0000000106b5ce40] = 944
 relocation     [0x0000000106b5cbd8,0x0000000106b5cc00] = 40
 main code      [0x0000000106b5cc00,0x0000000106b5cce0] = 224
 stub code      [0x0000000106b5cce0,0x0000000106b5cd10] = 48
 oops           [0x0000000106b5cd10,0x0000000106b5cd18] = 8
 metadata       [0x0000000106b5cd18,0x0000000106b5cd28] = 16
 scopes data    [0x0000000106b5cd28,0x0000000106b5cd70] = 72
 scopes pcs     [0x0000000106b5cd70,0x0000000106b5ce20] = 176
 dependencies   [0x0000000106b5ce20,0x0000000106b5ce28] = 8
 handler table  [0x0000000106b5ce28,0x0000000106b5ce40] = 24

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116e905a8} 'compute' '(D)V' in 'Main'
  0x0000000106b5cc00:   	brk	#0
  0x0000000106b5cc04:   	nop
  0x0000000106b5cc08:   	nop
  0x0000000106b5cc0c:   	nop
  0x0000000106b5cc10:   	nop
  0x0000000106b5cc14:   	sub	x9, sp, #0x14, lsl #12
  0x0000000106b5cc18:   	str	xzr, [x9]
  0x0000000106b5cc1c:   	sub	sp, sp, #0x30
  0x0000000106b5cc20:   	stp	x29, x30, [sp, #0x20]
  0x0000000106b5cc24:   	ldr	w8, #0xb4
  0x0000000106b5cc28:   	ldr	w9, [x28, #0x20]
  0x0000000106b5cc2c:   	cmp	x8, x9
  0x0000000106b5cc30:   	b.ne	#0x94
  0x0000000106b5cc34:   	ldr	d16, [x1, #0x8]
  0x0000000106b5cc38:   	str	d16, [sp]
  0x0000000106b5cc3c:   	ldr	w29, [x1]
  0x0000000106b5cc40:   	mov	x0, x1
  0x0000000106b5cc44:   	adr	x9, #0x18
  0x0000000106b5cc48:   	mov	x8, #0x9080         ;   {runtime_call _ZN13SharedRuntime17OSR_migration_endEPl}
  0x0000000106b5cc4c:   	movk	x8, #0x634, lsl #16
  0x0000000106b5cc50:   	movk	x8, #0x1, lsl #32
  0x0000000106b5cc54:   	stp	xzr, x9, [sp, #-0x10]!
  0x0000000106b5cc58:   	blr	x8
  0x0000000106b5cc5c:   	nop                         ;   {other}
  0x0000000106b5cc60:   	movk	xzr, #0x0
  0x0000000106b5cc64:   	movk	xzr, #0x0
  0x0000000106b5cc68:   	add	sp, sp, #0x10       ;*goto {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@16 (line 17)
  0x0000000106b5cc6c:   	b	#0x24               ;*iload_2 {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@2 (line 17)
  0x0000000106b5cc70:   	ldr	d0, [sp]
  0x0000000106b5cc74:   	bl	#0x38c              ; ImmutableOopMap {}
                                                            ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
                                                            ;   {static_call}
  0x0000000106b5cc78:   	nop                         ;   {other}
  0x0000000106b5cc7c:   	movk	xzr, #0x1e8
  0x0000000106b5cc80:   	movk	xzr, #0x0           ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
  0x0000000106b5cc84:   	ldr	x10, [x28, #0x380]
  0x0000000106b5cc88:   	add	w29, w29, #0x1      ; ImmutableOopMap {}
                                                            ;*goto {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::compute@16 (line 17)
  0x0000000106b5cc8c:   	ldr	wzr, [x10]          ;*iload_2 {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@2 (line 17)
                                                            ;   {poll}
  0x0000000106b5cc90:   	mov	w8, #0x9680
  0x0000000106b5cc94:   	movk	w8, #0x98, lsl #16
  0x0000000106b5cc98:   	cmp	w29, w8
  0x0000000106b5cc9c:   	b.lt	#-0x2c              ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@5 (line 17)
  0x0000000106b5cca0:   	mov	w1, #-0xbb
  0x0000000106b5cca4:   	bl	#-0x617a4           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) Main::compute@5 (line 17)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5cca8:   	nop                         ;   {other}
  0x0000000106b5ccac:   	movk	xzr, #0x218
  0x0000000106b5ccb0:   	movk	xzr, #0x200         ;*invokestatic logfn {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - Main::compute@9 (line 18)
  0x0000000106b5ccb4:   	mov	x1, x0
  0x0000000106b5ccb8:   	ldp	x29, x30, [sp, #0x20]
  0x0000000106b5ccbc:   	add	sp, sp, #0x30
  0x0000000106b5ccc0:   	b	#-0x3cbc0           ;   {runtime_call _rethrow_Java}
  0x0000000106b5ccc4:   	mov	x8, #0xf400
  0x0000000106b5ccc8:   	movk	x8, #0x6ad, lsl #16
  0x0000000106b5cccc:   	movk	x8, #0x1, lsl #32
  0x0000000106b5ccd0:   	blr	x8
  0x0000000106b5ccd4:   	b	#-0xa0
  0x0000000106b5ccd8:   	udf	#0x1                ;   {other}
  0x0000000106b5ccdc:   	udf	#0x0
[Exception Handler]
  0x0000000106b5cce0:   	b	#-0x3db60           ;   {no_reloc}
[Deopt Handler Code]
  0x0000000106b5cce4:   	adr	x30, #0x0
  0x0000000106b5cce8:   	b	#-0x61b28           ;   {runtime_call DeoptimizationBlob}
  0x0000000106b5ccec:   	isb                         ;   {static_stub}
  0x0000000106b5ccf0:   	mov	x12, #0x0           ;   {metadata(nullptr)}
  0x0000000106b5ccf4:   	movk	x12, #0x0, lsl #16
  0x0000000106b5ccf8:   	movk	x12, #0x0, lsl #32
  0x0000000106b5ccfc:   	mov	x8, #0x0
  0x0000000106b5cd00:   	movk	x8, #0x0, lsl #16
  0x0000000106b5cd04:   	movk	x8, #0x0, lsl #32
  0x0000000106b5cd08:   	br	x8
  0x0000000106b5cd0c:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

============================= C2-compiled nmethod ==============================
----------------------------------- Assembly -----------------------------------

Compiled method (c2)    2947  110             java.lang.FdLibm$Log::compute (443 bytes)
 total in heap  [0x0000000106b5b610,0x0000000106b5bdc8] = 1976
 relocation     [0x0000000106b5b758,0x0000000106b5b7a0] = 72
 constants      [0x0000000106b5b7c0,0x0000000106b5b800] = 64
 main code      [0x0000000106b5b800,0x0000000106b5ba00] = 512
 stub code      [0x0000000106b5ba00,0x0000000106b5ba10] = 16
 metadata       [0x0000000106b5ba10,0x0000000106b5ba28] = 24
 scopes data    [0x0000000106b5ba28,0x0000000106b5bbd0] = 424
 scopes pcs     [0x0000000106b5bbd0,0x0000000106b5bdc0] = 496
 dependencies   [0x0000000106b5bdc0,0x0000000106b5bdc8] = 8

[Disassembly]
--------------------------------------------------------------------------------

[Constant Pool]
             Address          hex4                    hex8      
  0x0000000106b5b7c0:   0xdf3e5244      0x3fc2f112df3e5244      
  0x0000000106b5b7c4:   0x3fc2f112                              
  0x0000000106b5b7c8:   0x96cb03de      0x3fc7466496cb03de      
  0x0000000106b5b7cc:   0x3fc74664                              
  0x0000000106b5b7d0:   0xd078c69f      0x3fc39a09d078c69f      
  0x0000000106b5b7d4:   0x3fc39a09                              
  0x0000000106b5b7d8:   0x1d8e78af      0x3fcc71c51d8e78af      
  0x0000000106b5b7dc:   0x3fcc71c5                              
  0x0000000106b5b7e0:   0x94229359      0x3fd2492494229359      
  0x0000000106b5b7e4:   0x3fd24924                              
  0x0000000106b5b7e8:   0x9997fa04      0x3fd999999997fa04      
  0x0000000106b5b7ec:   0x3fd99999                              
  0x0000000106b5b7f0:   0x55555593      0x3fe5555555555593      
  0x0000000106b5b7f4:   0x3fe55555                              
  0x0000000106b5b7f8:   0x00000000      0x0000000000000000      
  0x0000000106b5b7fc:   0x00000000                              

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd6758} 'compute' '(D)D' in 'java/lang/FdLibm$Log'
  # parm0:    v0:v0     = double
  #           [sp+0x50]  (sp of caller)
  0x0000000106b5b800:   	nop                         ;   {no_reloc}
  0x0000000106b5b804:   	sub	x9, sp, #0x14, lsl #12
  0x0000000106b5b808:   	str	xzr, [x9]
  0x0000000106b5b80c:   	sub	sp, sp, #0x50
  0x0000000106b5b810:   	stp	x29, x30, [sp, #0x40]
  0x0000000106b5b814:   	ldr	w8, #0x1e8
  0x0000000106b5b818:   	ldr	w9, [x28, #0x20]
  0x0000000106b5b81c:   	cmp	x8, x9
  0x0000000106b5b820:   	b.ne	#0x1c8              ;*synchronization entry
                                                            ; - java.lang.FdLibm$Log::compute@-1 (line 1673)
  0x0000000106b5b824:   	fmov	x10, d0             ;*invokestatic doubleToRawLongBits {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm::__HI@1 (line 93)
                                                            ; - java.lang.FdLibm$Log::compute@1 (line 1673)
  0x0000000106b5b828:   	asr	x11, x10, #32
  0x0000000106b5b82c:   	mov	w17, w11            ;*l2i {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm::__HI@9 (line 94)
                                                            ; - java.lang.FdLibm$Log::compute@1 (line 1673)
  0x0000000106b5b830:   	and	w12, w17, #0xfffff  ;*iand {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@92 (line 1692)
  0x0000000106b5b834:   	mov	w11, #0x5f64
  0x0000000106b5b838:   	movk	w11, #0x9, lsl #16
  0x0000000106b5b83c:   	add	w13, w12, w11       ;*iadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@99 (line 1693)
  0x0000000106b5b840:   	and	w11, w13, #0x100000
  0x0000000106b5b844:   	eor	w14, w11, #0x3ff00000
  0x0000000106b5b848:   	orr	w11, w14, w12
  0x0000000106b5b84c:   	sbfiz	x11, x11, #32, #32
  0x0000000106b5b850:   	and	x14, x10, #0xffffffff
  0x0000000106b5b854:   	orr	x11, x14, x11
  0x0000000106b5b858:   	fmov	d16, x11
  0x0000000106b5b85c:   	fmov	d17, #1.00000000
  0x0000000106b5b860:   	fsub	d16, d16, d17       ;*dsub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@130 (line 1696)
  0x0000000106b5b864:   	fmov	d17, #2.00000000
  0x0000000106b5b868:   	fadd	d17, d16, d17
  0x0000000106b5b86c:   	fdiv	d17, d16, d17       ;*ddiv {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@239 (line 1714)
  0x0000000106b5b870:   	fmul	d18, d17, d17       ;*dmul {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@251 (line 1716)
  0x0000000106b5b874:   	fmul	d19, d18, d18       ;*dmul {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@265 (line 1718)
  0x0000000106b5b878:   	ldr	d20, #-0xb8         ;   {section_word}
  0x0000000106b5b87c:   	fmul	d20, d19, d20
  0x0000000106b5b880:   	ldr	d21, #-0xb8         ;   {section_word}
  0x0000000106b5b884:   	fadd	d20, d20, d21
  0x0000000106b5b888:   	ldr	d21, #-0xb8         ;   {section_word}
  0x0000000106b5b88c:   	fmul	d21, d19, d21
  0x0000000106b5b890:   	fmul	d20, d20, d19
  0x0000000106b5b894:   	ldr	d22, #-0xbc         ;   {section_word}
  0x0000000106b5b898:   	ldr	d23, #-0xb8         ;   {section_word}
  0x0000000106b5b89c:   	fadd	d21, d21, d22
  0x0000000106b5b8a0:   	fadd	d20, d20, d23
  0x0000000106b5b8a4:   	fmul	d21, d21, d19
  0x0000000106b5b8a8:   	fmul	d20, d20, d19
  0x0000000106b5b8ac:   	ldr	d22, #-0xc4         ;   {section_word}
  0x0000000106b5b8b0:   	lsr	w13, w13, #20
  0x0000000106b5b8b4:   	ldr	d23, #-0xc4         ;   {section_word}
  0x0000000106b5b8b8:   	fadd	d21, d21, d22
  0x0000000106b5b8bc:   	add	w11, w13, w17, asr #20;*isub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@84 (line 1691)
  0x0000000106b5b8c0:   	fadd	d20, d20, d23
  0x0000000106b5b8c4:   	mov	w13, #0xb851
  0x0000000106b5b8c8:   	movk	w13, #0x6, lsl #16
  0x0000000106b5b8cc:   	sub	w15, w13, w12
  0x0000000106b5b8d0:   	mov	w13, #0xeb86
  0x0000000106b5b8d4:   	movk	w13, #0xfff9, lsl #16
  0x0000000106b5b8d8:   	add	w13, w12, w13
  0x0000000106b5b8dc:   	add	w14, w12, #0x2
  0x0000000106b5b8e0:   	orr	w13, w13, w15       ;*ior {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@330 (line 1722)
  0x0000000106b5b8e4:   	fmul	d19, d21, d19
  0x0000000106b5b8e8:   	sub	w12, w17, #0x100, lsl #12
  0x0000000106b5b8ec:   	fmul	d18, d20, d18
  0x0000000106b5b8f0:   	and	w14, w14, #0xfffff  ;*iand {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@139 (line 1697)
  0x0000000106b5b8f4:   	sub	w15, w11, #0x3ff    ;*iadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@125 (line 1695)
  0x0000000106b5b8f8:   	orr	w8, wzr, #0x7fe00000
  0x0000000106b5b8fc:   	cmp	w12, w8
  0x0000000106b5b900:   	b.hs	#0x70               ;*if_icmplt {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@67 (line 1688)
  0x0000000106b5b904:   	cmp	w14, #0x3
  0x0000000106b5b908:   	b.lt	#0x88               ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@141 (line 1697)
  0x0000000106b5b90c:   	fadd	d18, d19, d18       ;*dadd {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@337 (line 1723)
  0x0000000106b5b910:   	scvtf	d19, w15            ;*i2d {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@244 (line 1715)
  0x0000000106b5b914:   	cmp	w13, #0x0
  0x0000000106b5b918:   	b.gt	#0x98               ;*ifle {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@342 (line 1724)
  0x0000000106b5b91c:   	cmp	w11, #0x3ff
  0x0000000106b5b920:   	b.ne	#0x28               ;*ifne {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@402 (line 1732)
  0x0000000106b5b924:   	fsub	d18, d16, d18
  0x0000000106b5b928:   	fmul	d17, d18, d17
  0x0000000106b5b92c:   	fsub	d0, d16, d17        ;*dsub {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@415 (line 1733)
  0x0000000106b5b930:   	ldp	x29, x30, [sp, #0x40]
  0x0000000106b5b934:   	add	sp, sp, #0x50
  0x0000000106b5b938:   	ldr	x8, [x28, #0x378]   ;   {poll_return}
  0x0000000106b5b93c:   	cmp	sp, x8
  0x0000000106b5b940:   	b.hi	#0x9c
  0x0000000106b5b944:   	ret
  0x0000000106b5b948:   	str	d19, [sp, #0x18]
  0x0000000106b5b94c:   	str	d18, [sp, #0x10]
  0x0000000106b5b950:   	mov	w1, #-0xbb
  0x0000000106b5b954:   	str	d16, [sp]
  0x0000000106b5b958:   	str	d17, [sp, #0x8]
  0x0000000106b5b95c:   	mov	w29, w15
  0x0000000106b5b960:   	bl	#-0x60460           ; ImmutableOopMap {}
                                                            ;*ifne {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) java.lang.FdLibm$Log::compute@402 (line 1732)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5b964:   	nop                         ;   {other}
  0x0000000106b5b968:   	movk	xzr, #0x354
  0x0000000106b5b96c:   	movk	xzr, #0x0           ;*ifne {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@402 (line 1732)
  0x0000000106b5b970:   	mov	w29, w10            ;*l2i {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm::__LO@6 (line 76)
                                                            ; - java.lang.FdLibm$Log::compute@7 (line 1674)
  0x0000000106b5b974:   	mov	w1, #-0xc3
  0x0000000106b5b978:   	str	d0, [sp]
  0x0000000106b5b97c:   	str	w17, [sp, #0xc]
  0x0000000106b5b980:   	bl	#-0x60480           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) java.lang.FdLibm$Log::compute@19 (line 1677)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5b984:   	nop                         ;   {other}
  0x0000000106b5b988:   	movk	xzr, #0x374
  0x0000000106b5b98c:   	movk	xzr, #0x100         ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@19 (line 1677)
  0x0000000106b5b990:   	mov	w1, #-0xbb
  0x0000000106b5b994:   	str	d16, [sp]
  0x0000000106b5b998:   	mov	w29, w15
  0x0000000106b5b99c:   	str	w14, [sp, #0x8]
  0x0000000106b5b9a0:   	bl	#-0x604a0           ; ImmutableOopMap {}
                                                            ;*if_icmpge {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) java.lang.FdLibm$Log::compute@141 (line 1697)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5b9a4:   	nop                         ;   {other}
  0x0000000106b5b9a8:   	movk	xzr, #0x394
  0x0000000106b5b9ac:   	movk	xzr, #0x200         ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@141 (line 1697)
  0x0000000106b5b9b0:   	str	d19, [sp, #0x20]
  0x0000000106b5b9b4:   	str	d18, [sp, #0x18]
  0x0000000106b5b9b8:   	mov	w1, #-0xbb
  0x0000000106b5b9bc:   	str	d16, [sp]
  0x0000000106b5b9c0:   	str	d17, [sp, #0x8]
  0x0000000106b5b9c4:   	mov	w29, w15
  0x0000000106b5b9c8:   	str	w13, [sp, #0x10]
  0x0000000106b5b9cc:   	bl	#-0x604cc           ; ImmutableOopMap {}
                                                            ;*ifle {reexecute=1 rethrow=0 return_oop=0}
                                                            ; - (reexecute) java.lang.FdLibm$Log::compute@342 (line 1724)
                                                            ;   {runtime_call UncommonTrapBlob}
  0x0000000106b5b9d0:   	nop                         ;   {other}
  0x0000000106b5b9d4:   	movk	xzr, #0x3c0
  0x0000000106b5b9d8:   	movk	xzr, #0x300         ;*ifle {reexecute=0 rethrow=0 return_oop=0}
                                                            ; - java.lang.FdLibm$Log::compute@342 (line 1724)
  0x0000000106b5b9dc:   	adr	x8, #-0xa4          ;   {internal_word}
  0x0000000106b5b9e0:   	str	x8, [x28, #0x390]
  0x0000000106b5b9e4:   	b	#-0x60b64           ;   {runtime_call SafepointBlob}
  0x0000000106b5b9e8:   	mov	x8, #0xf400
  0x0000000106b5b9ec:   	movk	x8, #0x6ad, lsl #16
  0x0000000106b5b9f0:   	movk	x8, #0x1, lsl #32
  0x0000000106b5b9f4:   	blr	x8
  0x0000000106b5b9f8:   	b	#-0x1d4
  0x0000000106b5b9fc:   	udf	#0x1                ;   {other}
[Exception Handler]
  0x0000000106b5ba00:   	b	#-0x3c880           ;   {no_reloc}
[Deopt Handler Code]
  0x0000000106b5ba04:   	adr	x30, #0x0
  0x0000000106b5ba08:   	b	#-0x60848           ;   {runtime_call DeoptimizationBlob}
  0x0000000106b5ba0c:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2994  111     n       java.lang.invoke.MethodHandle::linkToStatic(JLJLL)J (native)
 total in heap  [0x0000000106b5c410,0x0000000106b5c5a0] = 400
 relocation     [0x0000000106b5c558,0x0000000106b5c560] = 8
 main code      [0x0000000106b5c580,0x0000000106b5c5a0] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8068} 'linkToStatic' '(JLjava/lang/Object;JLjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = 'java/lang/Object'
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/Object'
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5c580:   	nop
  0x0000000106b5c584:   	ldr	w12, [x5, #0x24]
  0x0000000106b5c588:   	lsl	x12, x12, #3
  0x0000000106b5c58c:   	ldr	x12, [x12, #0x10]
  0x0000000106b5c590:   	cbz	x12, #0xc
  0x0000000106b5c594:   	ldr	x8, [x12, #0x40]
  0x0000000106b5c598:   	br	x8
  0x0000000106b5c59c:   	b	#-0x6271c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2995  112     n       java.lang.invoke.MethodHandle::invokeBasic(JLJL)J (native)
 total in heap  [0x0000000106b5bf90,0x0000000106b5c130] = 416
 relocation     [0x0000000106b5c0d8,0x0000000106b5c0e0] = 8
 main code      [0x0000000106b5c100,0x0000000106b5c130] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8180} 'invokeBasic' '(JLjava/lang/Object;JLjava/lang/Object;)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = long
  # parm3:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5c100:   	nop
  0x0000000106b5c104:   	ldr	w12, [x1, #0x14]
  0x0000000106b5c108:   	lsl	x12, x12, #3
  0x0000000106b5c10c:   	ldr	w12, [x12, #0x28]
  0x0000000106b5c110:   	lsl	x12, x12, #3
  0x0000000106b5c114:   	ldr	w12, [x12, #0x24]
  0x0000000106b5c118:   	lsl	x12, x12, #3
  0x0000000106b5c11c:   	ldr	x12, [x12, #0x10]
  0x0000000106b5c120:   	cbz	x12, #0xc
  0x0000000106b5c124:   	ldr	x8, [x12, #0x40]
  0x0000000106b5c128:   	br	x8
  0x0000000106b5c12c:   	b	#-0x622ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2996  113     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLJLL)J (native)
 total in heap  [0x0000000106b5e590,0x0000000106b5e728] = 408
 relocation     [0x0000000106b5e6d8,0x0000000106b5e6e0] = 8
 main code      [0x0000000106b5e700,0x0000000106b5e724] = 36
 stub code      [0x0000000106b5e724,0x0000000106b5e728] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8298} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;JLjava/lang/Object;Ljava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/Object'
  # parm5:    c_rarg6:c_rarg6 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5e700:   	nop
  0x0000000106b5e704:   	ldr	xzr, [x1]
  0x0000000106b5e708:   	ldr	w12, [x6, #0x24]
  0x0000000106b5e70c:   	lsl	x12, x12, #3
  0x0000000106b5e710:   	ldr	x12, [x12, #0x10]
  0x0000000106b5e714:   	cbz	x12, #0xc
  0x0000000106b5e718:   	ldr	x8, [x12, #0x40]
  0x0000000106b5e71c:   	br	x8
  0x0000000106b5e720:   	b	#-0x648a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b5e724:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2998  114     n       java.lang.invoke.MethodHandle::invokeBasic(JLJ)J (native)
 total in heap  [0x0000000106b5db90,0x0000000106b5dd30] = 416
 relocation     [0x0000000106b5dcd8,0x0000000106b5dce0] = 8
 main code      [0x0000000106b5dd00,0x0000000106b5dd30] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd83b0} 'invokeBasic' '(JLjava/lang/Object;J)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm2:    c_rarg4:c_rarg4 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5dd00:   	nop
  0x0000000106b5dd04:   	ldr	w12, [x1, #0x14]
  0x0000000106b5dd08:   	lsl	x12, x12, #3
  0x0000000106b5dd0c:   	ldr	w12, [x12, #0x28]
  0x0000000106b5dd10:   	lsl	x12, x12, #3
  0x0000000106b5dd14:   	ldr	w12, [x12, #0x24]
  0x0000000106b5dd18:   	lsl	x12, x12, #3
  0x0000000106b5dd1c:   	ldr	x12, [x12, #0x10]
  0x0000000106b5dd20:   	cbz	x12, #0xc
  0x0000000106b5dd24:   	ldr	x8, [x12, #0x40]
  0x0000000106b5dd28:   	br	x8
  0x0000000106b5dd2c:   	b	#-0x63eac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    2999  115     n       java.lang.invoke.MethodHandle::linkToSpecial(LJLJL)J (native)
 total in heap  [0x0000000106b5d890,0x0000000106b5da28] = 408
 relocation     [0x0000000106b5d9d8,0x0000000106b5d9e0] = 8
 main code      [0x0000000106b5da00,0x0000000106b5da24] = 36
 stub code      [0x0000000106b5da24,0x0000000106b5da28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd84c8} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/Object;JLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/Object'
  # parm3:    c_rarg4:c_rarg4 
                        = long
  # parm4:    c_rarg5:c_rarg5 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5da00:   	nop
  0x0000000106b5da04:   	ldr	xzr, [x1]
  0x0000000106b5da08:   	ldr	w12, [x5, #0x24]
  0x0000000106b5da0c:   	lsl	x12, x12, #3
  0x0000000106b5da10:   	ldr	x12, [x12, #0x10]
  0x0000000106b5da14:   	cbz	x12, #0xc
  0x0000000106b5da18:   	ldr	x8, [x12, #0x40]
  0x0000000106b5da1c:   	br	x8
  0x0000000106b5da20:   	b	#-0x63ba0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b5da24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3001  116     n       java.lang.invoke.MethodHandle::linkToStatic(JJL)J (native)
 total in heap  [0x0000000106b5d190,0x0000000106b5d320] = 400
 relocation     [0x0000000106b5d2d8,0x0000000106b5d2e0] = 8
 main code      [0x0000000106b5d300,0x0000000106b5d320] = 32

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8718} 'linkToStatic' '(JJLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = long
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b5d300:   	nop
  0x0000000106b5d304:   	ldr	w12, [x3, #0x24]
  0x0000000106b5d308:   	lsl	x12, x12, #3
  0x0000000106b5d30c:   	ldr	x12, [x12, #0x10]
  0x0000000106b5d310:   	cbz	x12, #0xc
  0x0000000106b5d314:   	ldr	x8, [x12, #0x40]
  0x0000000106b5d318:   	br	x8
  0x0000000106b5d31c:   	b	#-0x6349c           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3002  117     n       java.lang.invoke.MethodHandle::invokeBasic(JJ)J (native)
 total in heap  [0x0000000106b62490,0x0000000106b62630] = 416
 relocation     [0x0000000106b625d8,0x0000000106b625e0] = 8
 main code      [0x0000000106b62600,0x0000000106b62630] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8830} 'invokeBasic' '(JJ)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  # parm1:    c_rarg3:c_rarg3 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b62600:   	nop
  0x0000000106b62604:   	ldr	w12, [x1, #0x14]
  0x0000000106b62608:   	lsl	x12, x12, #3
  0x0000000106b6260c:   	ldr	w12, [x12, #0x28]
  0x0000000106b62610:   	lsl	x12, x12, #3
  0x0000000106b62614:   	ldr	w12, [x12, #0x24]
  0x0000000106b62618:   	lsl	x12, x12, #3
  0x0000000106b6261c:   	ldr	x12, [x12, #0x10]
  0x0000000106b62620:   	cbz	x12, #0xc
  0x0000000106b62624:   	ldr	x8, [x12, #0x40]
  0x0000000106b62628:   	br	x8
  0x0000000106b6262c:   	b	#-0x687ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3003  118     n       java.lang.invoke.MethodHandle::linkToSpecial(LJJL)J (native)
 total in heap  [0x0000000106b62190,0x0000000106b62328] = 408
 relocation     [0x0000000106b622d8,0x0000000106b622e0] = 8
 main code      [0x0000000106b62300,0x0000000106b62324] = 36
 stub code      [0x0000000106b62324,0x0000000106b62328] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8948} 'linkToSpecial' '(Ljava/lang/Object;JJLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = long
  # parm3:    c_rarg4:c_rarg4 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b62300:   	nop
  0x0000000106b62304:   	ldr	xzr, [x1]
  0x0000000106b62308:   	ldr	w12, [x4, #0x24]
  0x0000000106b6230c:   	lsl	x12, x12, #3
  0x0000000106b62310:   	ldr	x12, [x12, #0x10]
  0x0000000106b62314:   	cbz	x12, #0xc
  0x0000000106b62318:   	ldr	x8, [x12, #0x40]
  0x0000000106b6231c:   	br	x8
  0x0000000106b62320:   	b	#-0x684a0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b62324:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3004  119     n       java.lang.invoke.MethodHandle::invokeBasic(J)J (native)
 total in heap  [0x0000000106b61e90,0x0000000106b62030] = 416
 relocation     [0x0000000106b61fd8,0x0000000106b61fe0] = 8
 main code      [0x0000000106b62000,0x0000000106b62030] = 48

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8bc8} 'invokeBasic' '(J)J' in 'java/lang/invoke/MethodHandle'
  # this:     c_rarg1:c_rarg1 
                        = 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg2:c_rarg2 
                        = long
  #           [sp+0x0]  (sp of caller)
  0x0000000106b62000:   	nop
  0x0000000106b62004:   	ldr	w12, [x1, #0x14]
  0x0000000106b62008:   	lsl	x12, x12, #3
  0x0000000106b6200c:   	ldr	w12, [x12, #0x28]
  0x0000000106b62010:   	lsl	x12, x12, #3
  0x0000000106b62014:   	ldr	w12, [x12, #0x24]
  0x0000000106b62018:   	lsl	x12, x12, #3
  0x0000000106b6201c:   	ldr	x12, [x12, #0x10]
  0x0000000106b62020:   	cbz	x12, #0xc
  0x0000000106b62024:   	ldr	x8, [x12, #0x40]
  0x0000000106b62028:   	br	x8
  0x0000000106b6202c:   	b	#-0x681ac           ;   {runtime_call AbstractMethodError throw_exception}
--------------------------------------------------------------------------------
[/Disassembly]

Compiled method (n/a)    3005  120     n       java.lang.invoke.MethodHandle::linkToSpecial(LJL)J (native)
 total in heap  [0x0000000106b61b90,0x0000000106b61d28] = 408
 relocation     [0x0000000106b61cd8,0x0000000106b61ce0] = 8
 main code      [0x0000000106b61d00,0x0000000106b61d24] = 36
 stub code      [0x0000000106b61d24,0x0000000106b61d28] = 4

[Disassembly]
--------------------------------------------------------------------------------
[Constant Pool (empty)]

--------------------------------------------------------------------------------

[Verified Entry Point]
  # {method} {0x0000000116bd8ce0} 'linkToSpecial' '(Ljava/lang/Object;JLjava/lang/invoke/MemberName;)J' in 'java/lang/invoke/MethodHandle'
  # parm0:    c_rarg1:c_rarg1 
                        = 'java/lang/Object'
  # parm1:    c_rarg2:c_rarg2 
                        = long
  # parm2:    c_rarg3:c_rarg3 
                        = 'java/lang/invoke/MemberName'
  #           [sp+0x0]  (sp of caller)
  0x0000000106b61d00:   	nop
  0x0000000106b61d04:   	ldr	xzr, [x1]
  0x0000000106b61d08:   	ldr	w12, [x3, #0x24]
  0x0000000106b61d0c:   	lsl	x12, x12, #3
  0x0000000106b61d10:   	ldr	x12, [x12, #0x10]
  0x0000000106b61d14:   	cbz	x12, #0xc
  0x0000000106b61d18:   	ldr	x8, [x12, #0x40]
  0x0000000106b61d1c:   	br	x8
  0x0000000106b61d20:   	b	#-0x67ea0           ;   {runtime_call AbstractMethodError throw_exception}
[Stub Code]
  0x0000000106b61d24:   	udf	#0x0
--------------------------------------------------------------------------------
[/Disassembly]
53 ms

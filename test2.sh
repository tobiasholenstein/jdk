#!/bin/bash

export DYLD_LIBRARY_PATH=/Users/tholenst/dev/hsdis

OPTIONS="-XX:-TieredCompilation -XX:CompileCommand=compileonly,Main::compute  -XX:CompileCommand=dontinline,Main::computeLog -XX:CompileCommand=compileonly,Main::computeLog -XX:CompileCommand=dontinline,java.lang.Double::* -XX:-UseSuperWord"

# create and override output file
echo 'Default' > out.txt
/usr/bin/time -h -p -o t1 /Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=compileonly,Main::compute -XX:CompileCommand=dontinline,Main::logfn -XX:CompileCommand=compileonly,Main::logfn -XX:-UseSuperWord Main.java >> out.txt

echo 'DisableIntrinsic' >> out.txt
/usr/bin/time -h -p -o t2 /Users/tholenst/dev/jdk4/build/macosx-aarch64/jdk/bin/java -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions $OPTIONS $@ Main2.java >> out.txt

# print the results of unix time for sanity check
echo && echo 'Time (sec)'
grep "user" t1
grep "user" t2
rm t1 t2

echo && echo 'Results'

ENABLED=$(grep "ms" out.txt | cut -d " " -f1 | head -n 1)
echo "$ENABLED ms"

DISABLED=$(grep "ms" out.txt | cut -d " " -f1 | tail -n 1)
echo "$DISABLED ms"

if [[ $DISABLED -gt $ENABLED ]]
   then
       echo "==> NOT Reproduced <=="
   else
       echo "==> Reproduced <=="
fi

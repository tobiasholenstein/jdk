/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package compiler.lib.test_generator;

import java.util.HashMap;
import java.util.Map;

public class InputTemplate1 extends BaseTemplate {

    public InputTemplate1() {
    }

    @Override
    public CodeTemplate getTemplate() {
        /* TODO:
         * use $limit, $i, $lFld for variables
         * all defined functions should use uniqueId to avoid conflict
         * Nesting : we want to be able to nest CodeTemplate in another CodeTemplate e.g. at \{thing}
         *           this would require replacing conflicting variables e.g. $i with $i1 and $i2,
         *           and also replace \{init} from the inner CodeTemplate with a var $limit from outer CodeTemplate
         *
         * TODO:
         * support generation of JTReg tests and IRFramework
        **/

        String statics = """
                static long lFld;
                static A a = new A();
                static boolean flag;
                static class A {
                    int i;
                }
                """;

        String call = "test_\\{uniqueId}();\n";

        String method = """
                 public static void test_\\{uniqueId}() {
                     long limit = lFld;
                     for (int i =\\{init}; i < \\{limit}; i \\{arithm}= \\{stride}) {
                         // Use stride > Integer.MAX_VALUE such that LongCountedLoopNode is not split further into loop nests.
                         for (long j = 0; j < limit; j+=2147483648L) {
                             a.i += 34; // NullCheck with trap on false path -> reason to peel
                             \\{thing}
                             if (j > 0) { // After peeling: j > 0 always true -> loop folded away
                                 break;
                             }
                         }
                     }
                 }
                """;

        return new CodeTemplate(statics, call, method);
    }

    @Override
    public Map<String, String> getRandomReplacements() {
        Map<String, String> replacements = new HashMap<>();

        String init = getRandomValueAsString(integerValues);
        String limit = getRandomValueAsString(integerValues);
        String stride = getRandomValueAsString(integerValuesNonZero);
        String arithm = getRandomValue(new String[]{"+", "-"});
        String thing = getRandomValue(new String[]{"", "synchronized (new Object()) { }"});
        String uniqueId = getUniqueId();

        replacements.put("init", init);
        replacements.put("limit", limit);
        replacements.put("arithm", arithm);
        replacements.put("stride", stride);
        replacements.put("thing", thing);
        replacements.put("uniqueId", uniqueId);
        return replacements;
    }

    @Override
    public String[] getCompileFlags() {
        return new String[] {
                "-Xcomp",
                "-XX:-CreateCoredumpOnCrash"
        };
    }

    /* TODO:
     * Also support per Template:
     * - Timeout
     * - number of test generations
     * - JAVA_HOME
     **/
}

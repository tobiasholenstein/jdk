/*
 * Copyright (c) 2023, 2024, Oracle and/or its affiliates. All rights reserved.
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

import compiler.lib.test_generator.BaseTemplate.CodeTemplate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TemplateGenerator {

    static final String JAVA_HOME = System.getProperty("java.home");
    static final String[] FLAGS = {
            "-XX:CompileCommand=compileonly,GeneratedTest::test*",
            "-XX:-TieredCompilation"
    };
    static final int TIMEOUT = 60;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    static final int TESTS_PER_TASK = 10;
    static final int MAX_TASKS_IN_QUEUE = NUM_THREADS * 2;

    /* TODO:
     * use specific folder for input Templates (input/) and for generated Test (output/)
     **/
    static String[] TEMPLATE_FILES = {
            "InputTemplate1.java",
            "InputTemplate1.java"
    };

    private static long testId = 0L;

    public static long getID() {
        return testId++;
    }

    /* TODO:
     * Measure performance: we want to generate and execute as many tests as possible
     * - Would in memory execution speed up test execution
     * - How to maximize generated+executed tests on multiple cores
     * - Where are the bottleneck?
    **/
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(NUM_THREADS, NUM_THREADS, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(MAX_TASKS_IN_QUEUE));
        for (String filePath : TEMPLATE_FILES) {
            try {
                Class<?> inputTemplateClass = DynamicClassLoader.compileAndLoadClass(filePath);
                BaseTemplate baseTemplate = (BaseTemplate) inputTemplateClass.getDeclaredConstructor().newInstance();
                runTestGen(baseTemplate, threadPool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }

    public static void runTestGen(BaseTemplate baseTemplate, ThreadPoolExecutor threadPool) {
        CodeTemplate template = baseTemplate.getTemplate();
        for (int i = 0; i < TESTS_PER_TASK; i++) {
            Map<String, String> replacements = baseTemplate.getRandomReplacements();
            String[] compileFlags = baseTemplate.getCompileFlags();
            long id = getID();
            threadPool.submit(() -> doWork(template, replacements, compileFlags, id));
        }
    }

    public static void doWork(CodeTemplate template, Map<String, String> replacements, String[] compileFlags, long num) {
        try {
            String javaCode = BaseTemplate.getJavaCode(template, replacements, num);
            String fileName = writeJavaCodeToFile(javaCode, num);
            ProcessOutput processOutput = executeJavaFile(fileName, compileFlags);
            checkExecutionOutput(processOutput);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't find test method: ", e);
        }
    }

    private static String writeJavaCodeToFile(String javaCode, long num) throws IOException {
        String fileName = String.format("GeneratedTest%d.java", num);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(javaCode);
        writer.close();
        return fileName;
    }

    private static ProcessOutput executeJavaFile(String fileName, String[] compileFlags) throws Exception {
        ProcessBuilder builder = getProcessBuilder(fileName, compileFlags);

        Process process = builder.start();
        boolean exited = process.waitFor(TIMEOUT, TimeUnit.SECONDS);
        if (!exited) {
            process.destroyForcibly();
            throw new RuntimeException("Process timeout: execution took too long.");
        }

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.exitValue();

        return new ProcessOutput(exitCode, output);
    }

    private static ProcessBuilder getProcessBuilder(String fileName, String[] compileFlags) {
        List<String> command = new ArrayList<>();

        command.add("%s/bin/java".formatted(JAVA_HOME));
        command.addAll(Arrays.asList(FLAGS));
        if (compileFlags != null) {
            command.addAll(Arrays.asList(compileFlags));
        }
        command.add("-cp");
        command.add(".");
        command.add(fileName);

        ProcessBuilder builder = new ProcessBuilder(command);

        builder.redirectErrorStream(true);
        return builder;
    }

    private static void checkExecutionOutput(ProcessOutput output) {
        if (output.exitCode == 0 && Objects.requireNonNull(output.output).contains("Passed")) {
            System.out.println("All tests passed successfully.");
        } else {
            System.err.println("Some tests failed:");
            System.err.println(output.output);
        }
    }

    /* TODO:
     * improve error handling:
     * - What kind of error is it? error in Template and cannot parse with javac
     * - or runtime crash in Java (e.g. Null-pointer Exception) nor JVM (compiler bug?)
     **/
    public record ProcessOutput(int exitCode, String output) {
    }
}

/*
 * Copyright (C) 2018 Jiri Kalousek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package classanalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.junit.Rule;

/**
 * This class gathers all necessary objects for instrumentation of a class code.
 * Instrumented class has to have an interface that defines all methods that
 * will be tested. This "ClassAnalyzer" allows tracking of code execution.
 *
 * The code in this class is inspired by an example from JaCoCo library.
 *
 * @author Jiri Kalousek
 * @param <I> Interface defining methods from tested class.
 */
public class ClassAnalyzer<I> {

    private final Class classClass;

    private String className;
    private IRuntime runtime;
    private Instrumenter instr;
    private byte[] instrumented;
    private RuntimeData data;
    private Class<?> targetClass;

    public ClassAnalyzer(Class classClass) {
        this.classClass = classClass;
        rule = new ClassAnalyzerRule<>(this);
    }
    
    @Rule
    public ClassAnalyzerRule<I> rule;

    /**
     * This method prepares the actual instrumentation and gives a new instance
     * of set class. That class is extended with probes that allows tracking of
     * code execution.
     *
     * @return Instrumented instance or null if Exception,
     * InstantiationException, or IllegalAccessException is raised.
     */
    public I newInstrumentedInstance() {
        try {
            className = classClass.getName();
            // For instrumentation and runtime we need a IRuntime instance
            // to collect execution data:
            runtime = new LoggerRuntime();

            // The Instrumenter creates a modified version of our test target class
            // that contains additional probes for execution data recording:
            instr = new Instrumenter(runtime);
            InputStream original = getTargetClass(className);
            instrumented = instr.instrument(original, className);
            original.close();

            // Now we're ready to run our instrumented class and need to startup the
            // runtime first:
            data = new RuntimeData();
            runtime.startup(data);

            // In this tutorial we use a special class loader to directly load the
            // instrumented class definition from a byte[] instances.
            final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
            memoryClassLoader.addDefinition(className, instrumented);
            targetClass = memoryClassLoader.loadClass(className);

            return (I) targetClass.newInstance();
        } catch (Exception ex) {
            Logger.getLogger(ClassAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * This method collects execution data and saves results to a csv file. For
     * each test of each class there is a unique csv file created.
     *
     * @param testName Name of the performed JUnit test (usually method name).
     * @param result Result of the JUnit test (to be written into the file).
     * @param args Arguments of tested method.
     * @return True if successfully saved or false if IOException or Exception
     * is raised.
     */
    public boolean saveInstrumentationResults(String testName, boolean result, Object... args) {
        try {
            // At the end of test execution we collect execution data and shutdown
            // the runtime:
            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            // Together with the original class definition we can calculate coverage
            // information:
            final CoverageBuilder coverageBuilder = new CoverageBuilder();
            final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
            InputStream original = getTargetClass(className);
            analyzer.analyzeClass(original, className);
            original.close();

            // line coverage information
            for (final IClassCoverage cc : coverageBuilder.getClasses()) {

                //System.out.printf("Coverage of class %s%n", cc.getName());
                String headerLine = "className,testName";
                String dataLine = cc.getName() + "," + testName;
                for (int i = 0; i < args.length; i++) {
                    dataLine += "," + args[i];
                    headerLine += ",ARG" + (i + 1);
                }

                for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
                    //System.out.printf("Line %s: %s%n", Integer.valueOf(i), getColor(cc.getLine(i).getStatus()));
                    dataLine += "," + getMark(cc.getLine(i).getStatus());
                    headerLine += ",LINE" + i;
                }
                headerLine += ",STATUS";
                dataLine += result?",P":",F";

                writeToFile(className, testName, headerLine, dataLine);
            }

            data.reset();
            runtime.startup(data);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ClassAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This method erases the execution data. After calling this method,
     * execution of the tested (and tracked) code can start again. The last
     * results will be lost.
     */
    public void resetInstrumentation() {
        data.reset();
    }

    /**
     * This method should be called at the end of the class instrumentation.
     */
    public void closeInstrumentation() {
        runtime.shutdown();
    }

    /**
     * A class loader that loads classes from in-memory data.
     */
    private class MemoryClassLoader extends ClassLoader {

        private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

        /**
         * Add a in-memory representation of a class.
         *
         * @param name name of the class
         * @param bytes class definition
         */
        public void addDefinition(final String name, final byte[] bytes) {
            definitions.put(name, bytes);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            final byte[] bytes = definitions.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }

    }

    private InputStream getTargetClass(final String name) {
        final String resource = '/' + name.replace('.', '/') + ".class";
        return getClass().getResourceAsStream(resource);
    }

    private String getMark(final int status) {
        switch (status) {
            case ICounter.NOT_COVERED:
                return "+";
            case ICounter.PARTLY_COVERED:
                return "+";
            case ICounter.FULLY_COVERED:
                return "-";
        }
        return "";
    }

    private void writeToFile(String className, String testName, String headerLine, String dataLine) throws IOException {
        className = className.replace('/', '_').replace('.', '_').replace('\\', '_').toLowerCase();
        testName = testName.replace('/', '_').replace('.', '_').replace('\\', '_').toLowerCase();
        File file = new File(className + "_" + testName + ".csv");
        boolean fileExists = file.exists();
        BufferedWriter bwNew = new BufferedWriter(new FileWriter(file, true));

        //if file does not exist, start with writing a header line
        if (!fileExists) {
            bwNew.write(headerLine);
            bwNew.newLine();
        }
        bwNew.write(dataLine);
        bwNew.newLine();
        bwNew.close();
    }
}

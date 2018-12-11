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
package example;

import classanalyzer.ClassAnalyzer;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example JUnit testing of the TargetClass with class instrumentation (code
 * coverage and subsequent evaluation).
 *
 * @author Jiri Kalousek
 */
public class TargetClassTest extends ClassAnalyzer<ITargetClass> {

    public TargetClassTest() {
        super(TargetClass.class);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws IOException {
    }
    
    @Test
    public void testMid() {
        System.out.println("-- testMid --");

        ITargetClass instance = super.newInstrumentedInstance();
        if (instance == null) {
            fail("Failed to create instrumented instance.");
            return;
        }

        // test vectors
        int[] arg1 =      {3,1,3,5,5,2,2,2};
        int[] arg2 =      {3,2,2,5,3,1,1,1};
        int[] arg3 =      {5,3,1,5,4,3,3,3};
        int[] expResult = {3,2,2,5,4,2,2,2};
        int[] result = new int[8];

        // testing
        for (int i = 0; i < expResult.length; i++) {
            result[i] = instance.mid(arg1[i], arg2[i], arg3[i]);
            assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults(expResult[i] == result[i], arg1[i], arg2[i], arg3[i]));
        }
        
        // evaluation
        for (int i = 0; i < expResult.length; i++) {
            assertEquals(expResult[i], result[i]);
        }
    }
    
    @Test
    public void testBubleSort() {
        System.out.println("-- testBubleSort --");

        ITargetClass instance = super.newInstrumentedInstance();
        if (instance == null) {
            fail("Failed to create instrumented instance.");
            return;
        }

        // test vectors
        int[][] arg = {
        {5,4,1,7,8,6,2,3},
        {3,1,7,8,4,2,5,6},
        {6,1,2,3,5,7,4,8},
        {3,1,7,2,4,8,5,6}
        };
        int[] expResult = {1,2,3,4,5,6,7,8};
        int[][] result = new int[arg.length][8];

        // testing
        for (int i = 0; i < arg.length; i++) {
            result[i] = instance.bubleSort(Arrays.copyOf(arg[i], arg[i].length));
            assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults(Arrays.equals(expResult, result[i]), arrToString(arg[i])));
        }
        
        // evaluation of results
        for (int i = 0; i < arg.length; i++) {
            assertTrue(Arrays.equals(expResult, result[i]));
        }
    }
    
    @Test
    public void testNumParser() {
        System.out.println("-- testNumParser --");

        ITargetClass instance = super.newInstrumentedInstance();
        if (instance == null) {
            fail("Failed to create instrumented instance.");
            return;
        }

        // test vectors
        int[] arg = {0,1,2,3,4,5,6,7,8,9};
        String[] expResult = {"zero","one","two","three","four","five","six","seven","eight","nine"};
        String[] result = new String[arg.length];

        // testing
        for (int i = 0; i < arg.length; i++) {
            result[i] = instance.numParser(arg[i]);
            assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults(expResult[i].equals(result[i]), arg[i]));
        }
        
        // evaluation of results
        for (int i = 0; i < arg.length; i++) {
            assertEquals(expResult[i], result[i]);
        }
    }
    
    private String arrToString(int[] arr){
        String ret = "[";
        for (int i = 0; i < arr.length; i++) {
            ret += "[" + arr[i] + "]";
        }
        return ret + "]";
    }
    
}

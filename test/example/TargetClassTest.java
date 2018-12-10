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
        // test of method max
        System.out.println("-- testMid --");

        int result;
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

        // testing
        for (int i = 0; i < expResult.length; i++) {
            result = instance.mid(arg1[i], arg2[i], arg3[i]);
            assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults(expResult[i] == result, arg1[i], arg2[i], arg3[i]));
            assertEquals(expResult[i], result);
        }
    }
    
}

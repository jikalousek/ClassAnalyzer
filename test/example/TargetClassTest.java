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
    public void testMax() {
        // test of method max
        System.out.println("-- testMax --");
        int arg1, arg2, arg3, expResult, result;
        ITargetClass instance = super.newInstrumentedInstance();
        if (instance == null) {
            fail("Failed to create instrumented instance.");
        }

        // test case 1
        arg1 = 3;
        arg2 = 3;
        arg3 = 5;
        expResult = 3;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);

        // test case 2
        arg1 = 1;
        arg2 = 2;
        arg3 = 3;
        expResult = 2;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        // test case 3
        arg1 = 3;
        arg2 = 2;
        arg3 = 1;
        expResult = 2;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        arg1 = 5;
        arg2 = 5;
        arg3 = 5;
        expResult = 5;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        arg1 = 5;
        arg2 = 3;
        arg3 = 4;
        expResult = 4;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        arg1 = 2;
        arg2 = 1;
        arg3 = 3;
        expResult = 2;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        arg1 = 2;
        arg2 = 1;
        arg3 = 3;
        expResult = 2;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
        
        arg1 = 2;
        arg2 = 1;
        arg3 = 3;
        expResult = 2;
        result = instance.mid(arg1, arg2, arg3);
        assertTrue("Failed to save instrumentation results.", super.saveInstrumentationResults("max", expResult == result, arg1, arg2));
        assertEquals(expResult, result);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geommath;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michal Kandr
 */
public class QuadraticEquationTest {

    public QuadraticEquationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRoot1 method, of class QuadraticEquation.
     */
    @Test
    public void testGetRoot1() {
        System.out.println("getRoot1");

        QuadraticEquation q = new QuadraticEquation(1, -1, -6);
        assertEquals(3, q.getRoot1(),0.001);
    }

    /**
     * Test of getRoot2 method, of class QuadraticEquation.
     */
    @Test
    public void testGetRoot2() {
        System.out.println("getRoot2");

        QuadraticEquation q = new QuadraticEquation(1, -1, -6);
        assertEquals(-2, q.getRoot2(),0.001);

    }

}
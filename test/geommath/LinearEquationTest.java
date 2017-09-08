/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geommath;

import java.awt.geom.Point2D;
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
public class LinearEquationTest {

    public LinearEquationTest() {
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
     * Test of solveSystem2 method, of class LineEquation.
     */
    @Test
    public void testSolveSystem2() {
        System.out.println("solveSystem2");

        LinearEquation r1 = new LinearEquation(1, 2, 3);
        LinearEquation r2 = new LinearEquation(5, 3, 7);

        Point2D.Float res = LinearEquation.solveSystem2(r1, r2);
        assertEquals(16/7.0-3, res.x,0.001);
        assertEquals(-8/7.0, res.y,0.001);
    }

    /**
     * Test of isSolution method, of class LineEquation.
     */
    @Test
    public void testIsSolution() {
        System.out.println("isSolution");

        LinearEquation r1 = new LinearEquation(1, 2, 3);

        assertEquals(true, r1.isSolution(-1, -1));
    }
}
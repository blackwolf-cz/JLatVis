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
public class LineTest {

    public LineTest() {
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
     * Test of getEquation method, of class Line.
     */
    @Test
    public void testGetEquation() {
        System.out.println("getEquation");

        Line l1 = new Line(new Point2D.Float(0,0), new Point2D.Float(3,3)),
               l2 = new Line(5,7,4,-3);
        
        assertEquals(new LinearEquation(-3, 3, 0), l1.getEquation());
        assertEquals(true, (new LinearEquation(-10, 1, 43)).equals(l2.getEquation()));
    }

    /**
     * Test of intersect method, of class Line.
     */
    @Test
    public void testIntersect() {
        System.out.println("intersect");

        Line l1 = new Line(new LinearEquation(1, 1, 2)),
                l2 = new Line(new LinearEquation(2, 2, 4));
        assertEquals(null, l1.intersect(l2));

        l1 = new Line(new LinearEquation(1, 0, 0));
        l2 = new Line(new LinearEquation(0, 1, 0));
        assertEquals(new Point2D.Float(0, 0), l1.intersect(l2));

        l1 = new Line(new LinearEquation(1, -1, 0));
        l2 = new Line(new LinearEquation(1, 1, 3));
        assertEquals(new Point2D.Float((float)(-3/2.0), (float)(-3/2.0)), l1.intersect(l2));
    }

}
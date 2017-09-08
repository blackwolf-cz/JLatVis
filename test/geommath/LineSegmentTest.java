/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geommath;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
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
public class LineSegmentTest {

    public LineSegmentTest() {
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
     * Test of contains method, of class LineSegment.
     */
    @Test
    public void testContains() {
        System.out.println("contains");

        LineSegment p = new LineSegment(0, 0, 3, 3);
        assertEquals(true, p.contains(new Point2D.Float(2, 2)));
        assertEquals(true, p.contains(new Point2D.Float(3, 3)));
        assertEquals(false, p.contains(new Point2D.Float((float)3.5, (float)3.5)));
        assertEquals(false, p.contains(new Point2D.Float(3, 5)));
    }

    /**
     * Test of intersect method, of class LineSegment.
     */
    @Test
    public void testIntersect() {
        System.out.println("intersect");

        LineSegment l1 = new LineSegment(0, 0, 3, 3);
        assertEquals(new Point2D.Float(0, 0), l1.intersect(new Line(0, 0, -2, 2)));
        assertEquals(null, l1.intersect(new Line(new LinearEquation(1, 1, 5))));

        LineSegment l2 = new LineSegment(3, 0, 3, 3),
                l3 = new LineSegment(2, 2, 4, 2);
        assertEquals(new Point2D.Float(3, 2), l2.intersect(l3));

    }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geommath;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Michal Kandr
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({geommath.QuadraticEquationTest.class,geommath.LinearEquationTest.class,geommath.LineTest.class,geommath.LineSegmentTest.class})
public class GeommathSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
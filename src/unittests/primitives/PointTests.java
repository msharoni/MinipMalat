package unittests.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;

  /*
  test point ops
 * @author mor
  */ 
    
public class PointTests {
        
    @Test
    public void  testDistanceSquared(){//returns the duplicates of the difference of the two points
    // ============ Equivalence Partitions Tests ==============
    Point p1 = new Point(1,1,1); 
    Point p2 = new Point(2,2,2); 
    assertEquals( 3, p1.distanceSquared(p2), 0.00001 );
    assertEquals(3, p2.distanceSquared(p1), 0.00001 );

    assertEquals(0, p1.distanceSquared(p1), 0.00001 );
    // =============== Boundary Values Tests ==================
    p1  = new Point(1,0,0); 
    p2 = new Point(2,0,0); 
    assertEquals(1, p2.distanceSquared(p1) , 0.00001 );
    p1  = new Point(0,1,0); 
    p2 = new Point(0,2,0); 
    assertEquals(1, p2.distanceSquared(p1) , 0.00001 );
    p1  = new Point(0,0,1); 
    p2 = new Point(0,0,2); 
    assertEquals(1,p2.distanceSquared(p1), 0.00001 );
    
    
    
    }
    @Test
    public void testDistance(){ // square root
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1,1,1); 
        Point p2 = new Point(2,2,2); 
        assertEquals(Math.sqrt(3) , p1.distance(p2), 0.00001 );
        assertEquals(Math.sqrt(3) , p2.distance(p1), 0.00001 );
    
        assertEquals(0, p1.distance(p1),0.00001 );
        // =============== Boundary Values Tests ==================
        p1  = new Point(1,0,0); 
        p2 = new Point(2,0,0); 
        assertEquals(1, p2.distance(p1), 0.00001 );
        p1  = new Point(0,1,0); 
        p2 = new Point(0,2,0); 
        assertEquals(1,p2.distance(p1) , 0.00001 );
        p1  = new Point(0,0,1); 
        p2 = new Point(0,0,2); 
        assertEquals(1, p2.distance(p1) , 0.00001 );
    }
    @Test
    public void testAdd () { 
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1,0,0);
        Point p1 = new Point(1,1,1); 
        assertEquals( new Point(2,1,1),p1.add(v1));

    }
    @Test
    public void testSubtract () { // subtract the two
            // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1,1,1); 
        Point p2 = new Point(1,0,0);
        assertEquals( new Vector(0,1,1),p1.subtract(p2));
             // =============== Boundary Values Tests ==================
        assertThrows("subtract() for parallel vectors does not throw an exception",
        IllegalArgumentException.class, () -> p1.subtract(p1));
       }


    
}
    
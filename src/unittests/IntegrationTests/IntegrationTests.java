package unittests.IntegrationTests;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;

  /*
  IntegrationTests
 * @author mor
  */ 
      
public class IntegrationTests {
      
    final int nx = 3 , ny = 3 ;

    private int countIntersections(Intersectable shape, Camera camera, int nY, int nX){
        int sum = 0 ; 
        for(int i = 0; i < nY; i++ ){
            for(int j = 0; j< nX; j ++ ){
                List<Point> x = shape.findIntersections(camera.constructRay(nX, nY, j, i));
                sum += x == null ? 0 : x.size(); 
            }
        } 
        return sum; 
    }

    @Test
    public void  testSphere(){
        Camera cmr = new Camera(new Point(0,0,0), new Vector(0,0,-1), new Vector(0,1,0)).setVPSize(3, 3).setVPDistance(1); 
        Sphere sp = new Sphere(new Point(0, 0, -3), 1);
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),2 , 0.00001 );
        
        cmr = new Camera(new Point(0,0,0.5), new Vector(0,0,-1), new Vector(0,1,0)).setVPSize(3, 3).setVPDistance(1); 
        sp = new Sphere(new Point(0,0,-2.5), 2.5);
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),18 , 0.00001 );

        sp = new Sphere(new Point(0,0,-2), 2);
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),10 , 0.00001 );
        
        sp = new Sphere(new Point(0,0,-2), 4);
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),9 , 0.00001 );

                
        sp = new Sphere(new Point(0,0,1), 0.5);
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),0 , 0.00001 );
    }

    @Test
    public void  testTriangle(){
        Camera cmr = new Camera(new Point(0,0,0), new Vector(0,0,-1), new Vector(0,1,0)).setVPSize(3, 3).setVPDistance(1); 
        Triangle sp = new Triangle(new Point(0,1,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),1 , 0.00001 );
         
        sp = new Triangle(new Point(0,20,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),2 , 0.00001 );

 
    }

    @Test
    public void  testPlane(){
        Camera cmr = new Camera(new Point(0,0,0), new Vector(0,0,-1), new Vector(0,1,0)).setVPSize(3, 3).setVPDistance(1); 
        Plane sp = new Plane(new Point(0,1,-5),new Point(0,0,-5),new Point(1,0,-5));
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),9 , 0.00001 );
         
        sp = new Plane(new Point(0, 0, -3), new Vector(0, -0.5, 1));
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),9 , 0.00001 );

        sp = new Plane(new Point(0, 0, -3), new Vector(0, -1, 1));
        assertEquals("intersection are messed up", countIntersections(sp,cmr, ny, nx),6 , 0.00001 );
        
    }

    
}
    
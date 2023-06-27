package geometries;
import java.util.List;
import primitives.*;

import static primitives.Util.isZero;

/**
 *Plane
 *
 * @author mor
 */
public class Plane extends Geometry {
    public final Point q0 ;
    private final Vector normal ;

    public Plane(Point q1 , Point q2 , Point q3 ){ //constructor
        Vector v1 = q2.subtract(q1) ; // two new vectors
        Vector v2 =q3.subtract(q2) ; // two new vectors ;
        this.normal = v1.crossProduct(v2).normalize();
        this.q0 = q1 ;
    }

    public Plane(Point q0, Vector normal) { //simple constructor
        this.q0 = q0;
        this.normal = normal.normalize();
    }

        @Override
        public Vector getNormal(Point point) { //get
            return normal ;
        }

    /**
    *  get without the point
    * @return normal
    */
    public Vector getNormal() { 
        return normal ;
    }

    /**
    *  get with the point
    * @return normal
    */
    @Override
    public boolean equals(Object obj) {//checks if equals
        return (obj instanceof Plane) && this.normal.equals(((Plane) obj).normal) 
        && q0.subtract(((Plane) obj).q0).dotProduct(normal) == 0  ;
    }



    @Override 
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double max){

        if(this.q0.equals(ray.getP0()))
            return null;
        if(this.normal.dotProduct(ray.getDir()) == 0)
            return null;
        double sclr = this.normal.dotProduct(this.q0.subtract(ray.getP0()))/this.normal.dotProduct(ray.getDir());
        if(sclr <=  0|| isZero(sclr) )
            return null;
        return List.of(new GeoPoint(ray.getP0().add(ray.getDir().scale(sclr)),this));
    }
}






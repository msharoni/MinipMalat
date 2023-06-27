package primitives;

import java.util.List;

import geometries.Intersectable.*;

/**
 *represents linear ray in the real numbers world
 *that contains point of start and dir - linear line !
 *
 * @author mor
 */
public class Ray {

    private final  Point p0;
    private final Vector dir;
    private static final double DELTA = 0.1;


    public Point getP0() {

        return this.p0;
    }

    public Vector getDir() {
        return this.dir;
    }

    public Ray(Point p0, Vector dir) { 
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    public Ray(Point q0, Vector dir, Vector normal){
        if (dir.dotProduct(normal)>0)
            this.p0=q0.add(normal.scale(DELTA));
        else
            this.p0=q0.add(normal.scale(-DELTA));
        this.dir=dir;
    }

    @Override
    /*

    checks if the two equal

     @author mor
    */
    public boolean equals(Object obj) { 
        return (obj instanceof Ray) && (((Ray)obj).p0.equals(this.p0) && ((Ray)obj).dir.equals(this.dir));
    }

    @Override
    /*
    converts ray obj to String

     @author mor
    */
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }
    public Point getPoint(double t){
        return this.p0.add(this.dir.scale(t));
        }

    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points ==null)
            return null;
        if(points.size() == 0)
            return null;
        GeoPoint minPoint = points.get(0);
        double minDst = Double.POSITIVE_INFINITY;
        double tmpDst;
        for (GeoPoint point:points)
        {
            tmpDst = this.p0.distance(point.point);
            if(tmpDst < minDst) {
                minPoint = point;
                minDst = tmpDst;
            }
        }
        return minPoint;
    }
    

}
package geometries;

import primitives.Point;
import primitives.Vector;
/**
 *Spgere
 *
 * @author Mor Sharoni
 */
public class Sphere implements Geometry{
    final Point center;
    final double radius;

    public Sphere(Point center, double radius) {//simple constructor
        this.center = center;
        this.radius = radius;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }
    public boolean equals(Object obj) {//checks if equals
        return (obj instanceof Sphere) && this.center.equals(((Sphere) obj).center) && this.radius == ((Sphere) obj).radius;
    }
}

package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Box {
    Point pb;
    Point pt;

    public Box(Point p0, Point p1) {
        this.pb = p0;
        this.pt = p1;
    }

    /**
     * Return wherevere there are intersections
     * with the box
     *
     * @param ray
     * @return
     */
    Boolean isIntersection(Ray ray) {
        // r.dir is unit direction vector of ray
        double x = 1.0f / ray.getDir().getX();
        double y = 1.0f / ray.getDir().getY();
        double z = 1.0f / ray.getDir().getZ();
        Vector dirfrac = new Vector(x, y, z);

        // lb is the corner of AABB with minimal coordinates - left bottom, rt is
        // maximal corner
        // r.org is origin of ray
        Point p0 = ray.getP0();

        double t1 = (pb.getX() - p0.getX()) * dirfrac.getX();
        double t2 = (pt.getX() - p0.getX()) * dirfrac.getX();
        double t3 = (pb.getY() - p0.getY()) * dirfrac.getY();
        double t4 = (pt.getY() - p0.getY()) * dirfrac.getY();
        double t5 = (pb.getZ() - p0.getZ()) * dirfrac.getZ();
        double t6 = (pt.getZ() - p0.getZ()) * dirfrac.getZ();

        double tmin = Math.max(Math.max(Math.min(t1, t2),Math.min(t3, t4)), Math.min(t5, t6));
        double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
            return false;

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
            return false;
        return true;
    }
}
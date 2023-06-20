package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

public class Circle extends Geometry {


    private Point center;
    private double rad;
    protected Plane plane;


    public Circle(Point center, double rad, Plane plane) {
        this.center = center;
        this.rad = rad;
        this.plane = plane;
    }

    /**
     * calculates normal vector to a point on the Vector
     * @param point is the point to find normal vector to
     * @return Vector is normal vector to Point on the Vector
     */
    @Override  public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    /**
     * finds intersections between the ray and the Polygon
     * @param ray is ray to intersect with the Polygon
     * @return list of intersections
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double max) {
        List<GeoPoint> geoPoints = plane.findGeoIntersectionsHelper(ray,2);
        if (geoPoints == null)
            return null;
        GeoPoint geoPoint = geoPoints.get(0);
        if (geoPoint.point.distance(center) > rad)
            return null;
        List<GeoPoint> newGeoPoints = new ArrayList<>();
        for (GeoPoint geo : geoPoints) {
            newGeoPoints.add(new GeoPoint( geo.point,this));
        }
        return newGeoPoints;
    }

}


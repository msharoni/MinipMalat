package geometries;

import java.util.*;
import java.util.stream.Collectors;

import primitives.*;


public abstract class Intersectable {
    public static boolean isBox = true;
    Box box = null;

    public Boolean isIntersection(Ray ray){
        return (!isBox || box==null) || box.isIntersection(ray);
    }

    /**
     * Returns the Intersections (List of Points) of the Geometry with the Given Ray.
     *
     * @param ray
     * @return
     */
    public List<Point> findIntersections(Ray ray) {
        List<GeoPoint> geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());
    }

    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return isIntersection(ray) ? findGeoIntersections(ray, Double.POSITIVE_INFINITY):null;
    }

    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return isIntersection(ray) ? findGeoIntersectionsHelper(ray, maxDistance):null;
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);




    public static class GeoPoint {
        public final Geometry geometry;
        public final Point point;
        public GeoPoint(Point point , Geometry geometry){
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object x) {
            return x instanceof GeoPoint
                    && ((GeoPoint) x).geometry.equals(geometry) &&
                    ((GeoPoint) x).point.equals(point);
        }
    }
}
package geometries;


import java.util.ArrayList;
import java.util.List;
import primitives.*;
/*
 Limited Tube

  @author mor
 */

 /**
     * constructor cylinder with get height
  */
public class Cylinder extends Tube{
    double height;

    public Cylinder(Ray axisRay, double radius, double height ) {
        super(axisRay, radius);
        this.height = height;

        // Create Box
        Point p0 = new Point(axisRay.getP0().getX() - radius, axisRay.getP0().getY() - radius, axisRay.getP0().getZ() - radius);
        Point center = axisRay.getP0().add(axisRay.getDir().scale(height));
        Point p1 = new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius);
        this.box = new Box(p0, p1);
    }

    /**
     * equals between objects
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Cylinder) &&
        super.equals(obj)&&  this.height == ((Cylinder) obj).height;
    }

    @Override
    /*
     * get normal of vector
     * @return normal
     */
    public Vector getNormal(Point point) {
        
        //  checks if it in the bases center to avoid zero vector 
        if( point.equals(axisRay.getP0() ) ||  
            point.equals(axisRay.getP0().add(this.axisRay.getDir().scale(height)) ) ){
            return  this.axisRay.getDir() ;
        } 
        // checks if it is in the bases and if it is it returns axis dir and if it not calc like tube
        double radiusSquared = this.radius * this.radius;
        Vector toOtherBase = this.axisRay.getDir().scale(this.height) ; 
        Point p0Ver2 = this.axisRay.getP0().add(toOtherBase);
        double crossProductp01 =  this.axisRay.getDir().dotProduct(this.axisRay.getP0().subtract(point));
        double crossProductp02 =   this.axisRay.getDir().dotProduct(p0Ver2.subtract(point));
        return  (point.distanceSquared(this.axisRay.getP0()) <= radiusSquared  && crossProductp01  == 0) || 
            (point.distanceSquared(p0Ver2) <= radiusSquared && crossProductp02 == 0) ? 
                this.axisRay.getDir() :
                super.getNormal(point) ;
  
      
    }

     @Override
     public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double max) {
         List<GeoPoint> res = new ArrayList<>();
         List<GeoPoint> lst = super.findGeoIntersectionsHelper(ray,max);
         if (lst != null)
             for (GeoPoint geoPoint : lst) {
                 double distance = Util.alignZero(geoPoint.point.subtract(axisRay.getP0()).dotProduct(axisRay.getDir()));
                 if (distance > 0 && distance <= height)
                     res.add(geoPoint);
             }

         if (res.size() == 0)
             return null;
         return res;
     }
 }
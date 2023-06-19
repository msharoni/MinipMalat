package renderer;

import java.util.List;

import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;

public class RayTracerBasic extends RayTracerBase{
    public RayTracerBasic(Scene scene) {
        super(scene);
    }
//    private Color calcColor(GeoPoint pt, Ray ray){
//
//        Color cr =  sn.al.getIntensity().add(pt.geometry.getEmission());
//        for(LightSource x : sn.lights){
//            Vector n = pt.geometry.getNormal(pt.point).normalize();
//            Vector l = x.getL(pt.point).normalize();
//            Vector r = l.subtract(n.scale(l.dotProduct(n) * 2)).normalize();
//            Color IL = x.getIntensity(pt.point);
//            Vector v = ray.getDir().normalize();
//            if(v.dotProduct(n) * l.dotProduct(n) > 0){
//              // return cr ;
//            }
//            Double3 kd = pt.geometry.getMaterial().getKd();
//            Double3 ks = pt.geometry.getMaterial().getKs();
//            int shine = pt.geometry.getMaterial().getShininess();
//            cr = cr.
//            add(IL.scale( kd.scale(Math.abs(l.dotProduct(n)))))
//            .add(IL.scale(ks.scale(Math.pow(Math.max(0,v.dotProduct(r)),shine))));
//        }
//        return cr;
//    }
private Color calcColor(GeoPoint intersection, Ray ray){
    return sn.al.getIntensity()
            .add(intersection.geometry.getEmission(),
                    calcColorLocalEffects(intersection, ray));
}

    private Color calcColorLocalEffects(GeoPoint geoPoint, Ray ray) {
        Color color = Color.BLACK;
        Vector v = ray.getDir();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = Util.alignZero(n.dotProduct(v));
        if (nv == 0)
            return color;
        Material material = geoPoint.geometry.getMaterial();

        for (LightSource lightSource :
                sn.lights) {
            Vector l = lightSource.getL(geoPoint.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if (nl*nv>0){ //check whether nl and nv have the same sign
                Color iL = lightSource.getIntensity(geoPoint.point);
                color = color.add(iL.scale(calcDiffusive(material, nl)),
                        iL.scale(calcSpecular(material, n, l, nl, v)));
            }
        }
        return color;
    }
    @Override 
    public Color traceRay (Ray ray) {
       List<GeoPoint> intersecions = sn.geometries.findGeoIntersections(ray);
       if(intersecions == null  ){
           return sn.bg ;
       }
       GeoPoint closet = ray.findClosestGeoPoint(intersecions);
       return this.calcColor(closet,ray);

    }
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {

        double ln = l.dotProduct(n);
        Vector r = l.add(n.scale(-2*ln)).normalize();
        double vr = v.dotProduct(r);


        if(-vr > 0){
            Double3 x = material.kS.scale((Math.pow((-1*vr),material.nShininess)));
            return x;
        }
        else return new Double3(0);
    }


    private Double3 calcDiffusive(Material material, double nl) {
        if(nl < 0) //calc |nl|  --> abs(nl)
            nl = nl*-1;
        return material.kD.scale(nl);
    }
}

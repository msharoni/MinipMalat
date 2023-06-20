package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;

public class RayTracerBasic extends RayTracerBase {
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = new Double3(1.0);


    /**
     * find the point where the ray intersacte with object
     * @param ray the ray
     * @return the color that the pixel shuld be peinted by
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return calcColor(closestPoint, ray);
    }

    /**
     * calculate the color of point from ray
     * @param intersection the point to calculate the color on
     * @param ray the ray that 'looking' at the geometry
     * @return the color of the point
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
        if(intersection == null)
            return sn.bg;
        return calcColor(intersection,ray,MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(sn.al.getIntensity());
    }

    private Color calcColor(GeoPoint gp,Ray ray, int level, Double3 k) {
        Color color= calcLocalEffects(gp, ray,k);
        return 1 == level ?color : color.add(calcGlobalEffects(gp, ray, level, k));}


    private Color calcGlobalEffects(GeoPoint intersection, Ray ray,int level,Double3 k){
        Color color=Color.BLACK;
        Vector normal=intersection.geometry.getNormal(intersection.point);
        Material material=intersection.geometry.getMaterial();

        Double3 kkr= material.kR.product(k);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)){
            color= calcGlobalEffect(constructReflectedRay(intersection.point,ray,normal), level,material.kR,kkr);
        }
        Double3 kkt= material.kT.product(k);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)){
            color= color.add(
                    calcGlobalEffect(constructRefractedRay(intersection.point,ray,normal), level,material.kT,kkt));
        }
        return color;
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp= findClosestIntersection(ray);
        return (gp== null ? sn.bg: calcColor(gp, ray, level -1, kkx)).scale(kx);
    }

    private Ray constructReflectedRay(Point intersection,Ray inRay,Vector normal){
        Vector v=inRay.getDir();
        return new Ray(intersection,v.subtract(normal.scale(2*v.dotProduct(normal))), normal);
    }

    private Ray constructRefractedRay(Point intersection,Ray inRay,Vector normal){
        return new Ray(intersection,inRay.getDir(),normal);

    }

    private GeoPoint findClosestIntersection(Ray ray){
        return ray.findClosestGeoPoint(sn.geometries.findGeoIntersections(ray));
    }

    private Color calcLocalEffects(GeoPoint intersection, Ray ray,Double3 k) {
        Color color = intersection.geometry.getEmission();
        Vector v = ray.getDir ();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return color;
        Material material = intersection.geometry.getMaterial();


        for (LightSource lightSource : sn.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0)
            { // sign(nl) == sing(nv)
                Double3 ktr=transparency(intersection,lightSource,l,n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K))
                {
                    Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }

        return color;
    }

    private Double3 calcSpecular(Material material, Vector n, Vector l, double ln, Vector v) {

        Vector r = l.add(n.scale(-2*ln)).normalize();
        double vr = v.dotProduct(r);
        if(vr < 0){
            return material.kS.scale(Math.pow((-1*vr),material.nShininess));
        }
        else return Double3.ZERO;
    }


    /**
     * calculate the diffusive of the matirial
     * @return the diffusive
     */
    private Double3 calcDiffusive(Material material, double nl) {
        if(nl < 0) //calc |nl|  --> abs(nl)
            nl = nl*-1;
        return material.kD.scale(nl);
    }

    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n){
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp.point, lightDirection,n);
        List<GeoPoint> intersections = sn.geometries.findGeoIntersections(lightRay);
        if(intersections == null)
            return new Double3(1);
        Double3 ktr=new Double3(1);
        for (GeoPoint intersection: intersections)
        {
            if(intersection.point.distance(gp.point) < light.getDistance(gp.point))
            {
                ktr=ktr.product(intersection.geometry.getMaterial().kT);
            }
        }
        return ktr;
    }



}


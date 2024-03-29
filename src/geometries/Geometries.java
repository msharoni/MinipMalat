package geometries;
import java.util.LinkedList;
import java.util.List;
import primitives.*;

/**
 *Plane
 *
 * @author mor
 */
public class Geometries extends Intersectable {
    final private List<Intersectable> items ;
    public Geometries(Intersectable... geometries){
        items = new LinkedList<>(List.of(geometries));
    }
    public void add(Intersectable... geometries){
        items.addAll(List.of(geometries));
    }



    @Override 
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double max){
        List<GeoPoint> it = new LinkedList<>();
        for(Intersectable element : this.items){
            List<GeoPoint> x =element.findGeoIntersectionsHelper(ray,max);
            if(x!= null ){
                it.addAll(x);
            }
           
        }
        return it.size() == 0 ? null :  it ;
    }








    }


package lighting;

import primitives.*;

public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

   public  DirectionalLight(Color Intensity,Vector Direction){
       this.direction = Direction ; 
       this.intensity = Intensity;
   }

    public Color getIntensity(Point p){
        return intensity;

    }
    public Vector getL(Point p){
       return direction.normalize() ;
    }
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }


}

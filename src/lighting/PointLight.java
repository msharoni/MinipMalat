package lighting;

import primitives.*;

public class PointLight  extends Light implements LightSource   {
   
    protected Point position ; 
    protected double kC,kL,kQ ;

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this; 
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }
   
    @Override
    public Color getIntensity(Point p){
        double d2 = position.distanceSquared(p);
        double d = Math.sqrt(d2);
        return intensity.scale(1.0d/ (kC + kL *d + kQ *d2 ));
        
    }

    public PointLight(Color intensity, Point position){
        this.intensity = intensity ;
        this.position = position;
        kC =1 ;
        kL = 0 ;
        kQ = 0 ;
    }

    @Override
    public Vector getL(Point p){
            return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

}

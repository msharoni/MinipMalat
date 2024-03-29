package renderer;

import scene.Scene;
import primitives.Color;
import primitives.Ray;

public abstract class RayTracerBase  {
    protected Scene sn ;
    RayTracerBase(Scene sn){
        this.sn = sn ;
    }
    public abstract Color traceRay (Ray ray) ;

}

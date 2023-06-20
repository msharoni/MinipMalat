package scene;
import lighting.*;

import java.util.LinkedList;
import java.util.List;

import geometries.*;
import lighting.AmbientLight;
import primitives.*;

public class Scene {
    public String senceName ; 
    public Color bg ;
    public List<LightSource> lights ;
    public AmbientLight al ;
    public Geometries geometries;

    public Scene setBackground(Color bg) {
        this.bg = bg;
        return this;
    }

    public Scene setAmbientLight(AmbientLight al) {
        this.al = al;
        return this;
    }
    public Scene setAmbientLight(LightSource ... lights) {
        if(lights != null){
        this.lights.addAll(List.of(lights));
        }
        return this;
    }


    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene (String senceName){
        this.lights = new LinkedList<LightSource>();
        this.senceName = senceName; 
        this.bg = Color.BLACK;
        this.al = new AmbientLight();
        this.geometries = new Geometries();
    }
    public Scene Builder () {
        
        return this;
    } ; 
}

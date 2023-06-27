package lighting;

import primitives.*;

public class AmbientLight {
    final private Color intensity;
    public AmbientLight(Color rgb , Double3 hanchata){
        this.intensity = rgb.scale(hanchata);

    }
    public AmbientLight(){
        this.intensity =Color.BLACK;
    }
    public Color getIntensity(){
        return intensity;
    }
    
}

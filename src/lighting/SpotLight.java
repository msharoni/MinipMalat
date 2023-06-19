package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;


/**

 * The SpotLight class represents a spotlight source
 * It extends the PointLight class.
 */
public class SpotLight extends PointLight{

    private Vector direction;

    /**
     * Constructs a SpotLight object with the specified intensity, position, and direction.
     * @param intensity the intensity of the light
     * @param position the position of the light source
     * @param direction the direction of the light
     */
    public SpotLight(Color intensity,Point position,Vector direction) {
        super(intensity,position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        double d = direction.dotProduct(getL(p));
        if(alignZero(d) <= 0){return Color.BLACK;}
        return super.getIntensity(p).scale(d);
    }
}
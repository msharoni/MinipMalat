package geometries;

import primitives.Point;
import primitives.Vector;
/**
 *Triangle
 *
 * @author Mor Sharoni
 */
public class Triangle extends Polygon {
    public Triangle(Point p1 , Point p2 , Point p3) {
        super(p1,p2,p3);
    }

    @Override
    public boolean equals(Object obj) {//checks if equals
        return (obj instanceof Triangle) && super.equals(obj);
    }
}

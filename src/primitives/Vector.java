package primitives;

import java.awt.*;
/**
 *represnts linear vector in the real numbers world
 *
 * @author mor
 */
public class Vector extends Point {

    public Vector(Double3 x ) { 
        super(x);
        if (this.xyz.equals(Double3.ZERO))//gets a point
            throw  new IllegalArgumentException("cant enter the zero vector");

    }

    public Vector(double d1, double d2, double d3) {
        super(d1, d2, d3);
        if (this.xyz.equals(Double3.ZERO))//gets a point
            throw  new IllegalArgumentException("cant enter the zero vector");

    }

    @Override
    public boolean equals(Object obj) { // using fathers func
        return obj instanceof Vector && super.equals(obj);
    }

    @Override
    public String toString() { // prints the point
        return "Vector{" +
                "xyz=" + xyz +
                '}';
    }

    /**
    * v+u
    *
    * @author mor
    */
    public Vector add(Vector vc) { 
        return new Vector(vc.xyz.add(this.xyz));}


    /**
    * vector * t 
    *
    * @author mor
    */
    public Vector scale(double sc) {
        return new Vector(this.xyz.scale(sc));
        
    }

    /**
    *Dot product
    *
    * @author mor
    */
    public double dotProduct(Vector vc) { 
        return this.xyz.d1 * vc.xyz.d1 +
                this.xyz.d2 * vc.xyz.d2 +
                this.xyz.d3 * vc.xyz.d3;

    }

    /**
    *length * length
    *
    * @author mor
    */
    public double lengthSquared() { 
        return this.dotProduct(this);
    }

    /**
    * returns square root of length
    *
    * @author mor
    */
    public double length() { 
        return Math.sqrt(this.lengthSquared());
    }

    /**
    *normalize
    *
    * @author mor
    */
    public Vector normalize(){ 
        return this.scale(1/this.length());
    }

    public Vector crossProduct(Vector vc){ 
        return new Vector( (this.xyz.d2 * vc.xyz.d3 - this.xyz.d3 * vc.xyz.d2)  ,
                (this.xyz.d3 * vc.xyz.d1 - this.xyz.d1 * vc.xyz.d3 )  ,
                (this.xyz.d1 * vc.xyz.d2 - this.xyz.d2 * vc.xyz.d1) ) ; 

    }

}

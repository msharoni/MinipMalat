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

    public Vector Roatate(double angle , Vector axsis ){
        angle = angle / 180 * Math.PI ;
        double cosa = Math.cos(angle ) , sina = Math.sin(angle);
        double  x = axsis.xyz.d1 , y = axsis.xyz.d2 , z=axsis.xyz.d3 ,x2 = x*x ,y2 =y*y, z2 = z*z;
        double tx = this.xyz.d1 , ty = this.xyz.d2 ,tz =this.xyz.d3 ;
        return new Vector(
                (x2*(1-cosa)+ cosa )*tx +  (x*y*(1-cosa)-sina)*ty + (x*z*(1-cosa)+y*sina)*tz ,
                (x*y*(1-cosa)+z*sina)*tx + (y2*(1-cosa)+cosa)*ty + (y*z*(1-cosa)-x*sina)*tz ,
                (x*z*(1-cosa)-y*sina)*tx + (y*z*(1-cosa)+x*sina)*ty + (z2*(1-cosa)+cosa)*tz
        );

    }

    public Vector rotate(Vector axis, double angle){
        double x, y, z;
        double u, v, w;
        x=getX();y=getY();z=getZ();
        u=axis.getX();v=axis.getY();w=axis.getZ();
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(angle))
                + x*Math.cos(angle)
                + (-w*y + v*z)*Math.sin(angle);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(angle))
                + y*Math.cos(angle)
                + (w*x - u*z)*Math.sin(angle);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(angle))
                + z*Math.cos(angle)
                + (-v*x + u*y)*Math.sin(angle);
        return new Vector(xPrime, yPrime, zPrime);
    }
}

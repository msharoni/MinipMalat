package unittests.renderer;
import primitives.*;
import primitives.Vector;
import geometries.*;
import renderer.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import scene.Scene;

import static java.awt.Color.*;

public class BowlingTest {

    final private Scene scene = new Scene("Bowling Scene");

    public void addPin(Point p)
    {
        scene.geometries.add(
                new Sphere(new Point(p.getX(), 1.1, p.getZ()), 2).setEmission(new Color(WHITE)),
                new Cylinder(new Ray(new Point(p.getX(), 0.6, p.getZ()), new Vector(0, 1, 0)), 2, 2.2).setEmission(new Color(WHITE)),
                new Sphere(new Point(p.getX(), 3.1, p.getZ()), 2).setEmission(new Color(WHITE)),
                new Cylinder(new Ray(new Point(p.getX(), 4.6, p.getZ()), new Vector(0, 1, 0)), 1, 3).setEmission(new Color(RED)),
                new Cylinder(new Ray(new Point(p.getX(), 5.3, p.getZ()), new Vector(0, 1, 0)), 1.01, 0.6).setEmission(new Color(WHITE)),
                new Sphere(new Point(p.getX(), 7.3, p.getZ()), 1.25).setEmission(new Color(WHITE)),
                new Cylinder(new Ray(new Point(p.getX(), 7.6, p.getZ()), new Vector(0, 1, 0)), 1.25, 1).setEmission(new Color(WHITE)),
                new Sphere(new Point(p.getX(), 8.3, p.getZ()), 1.25).setEmission(new Color(WHITE))
        );
    }

    @Test
    public void Bowling() {

        Camera camera = new Camera(new Point(-60, 20, 10), new Vector(70, -20, -10), new Vector(1, 3.5, 0)) //
                .setVPSize(150, 150)
                .setVPDistance(100);

        // scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)))
        scene.setBackground(new Color(173, 216, 230));
        scene.geometries.add(new Plane(new Point(10, -0.5, 0), new Vector(0, 1, 0)).setEmission(new Color(0, 100, 0)) //
                .setMaterial(new Material()));

        scene.geometries.add(
                new Polygon(new Point(60, 0, -20), new Point(60, 0, 20), new Point(-60, 0, 20))
                        //.setEmission(new Color(78,79,85))
                        .setMaterial(new Material().setKr(0.05))
                        .setEmission(new Color(205,158,112)),
                new Polygon(new Point(60, 0, -20), new Point(-60, 0, 20), new Point(-60, 0, -20))
                        //.setEmission(new Color(78,79,85))
                        .setMaterial(new Material().setKr(0.05))
                        .setEmission(new Color(205,158,112))
        );

        scene.geometries.add(
                new Polygon(new Point(60, 0, -20), new Point(-60, 0, -20), new Point(60, 40, -20))
                        .setMaterial(new Material().setKt(0.1))
                        .setEmission(new Color(BLACK)),
                new Polygon(new Point(60, 0, -20), new Point(60, 0, 20), new Point(60, 40, 20))
                        .setMaterial(new Material().setKt(0.1))
                        .setEmission(new Color(BLACK)),
                new Polygon(new Point(60, 0, -20), new Point(60, 40, -20), new Point(60, 40, 20))
                        .setMaterial(new Material().setKt(0.1))
                        .setEmission(new Color(BLACK)),
                new Polygon(new Point(60, 0, 20), new Point(60, 40, 20), new Point(-60, 0, 20))
                        .setMaterial(new Material().setKt(0.1))
                        .setEmission(new Color(BLACK))
        );

        addPin(new Point(10 + 3 * Math.sqrt(27), 0, -9));
        addPin(new Point(10 + 3 * Math.sqrt(27), 0, -3));
        addPin(new Point(10 + 3 * Math.sqrt(27), 0, 3));
        addPin(new Point(10 + 3 * Math.sqrt(27), 0, 9));

        addPin(new Point(10 + 2 * Math.sqrt(27), 0, -6));
        addPin(new Point(10 + 2 * Math.sqrt(27), 0, 0));
        addPin(new Point(10 + 2 * Math.sqrt(27), 0, 6));

        addPin(new Point(10 + Math.sqrt(27), 0, -3));
        addPin(new Point(10 + Math.sqrt(27), 0, 3));

        addPin(new Point(10, 0, 0));

        scene.geometries.add(
                new Sphere(new Point(-40, 4, 0), 4)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300))
                        .setEmission(new Color(RED)),
                new Sphere(new Point(-40, 4.504, 0), 3.5).setEmission(new Color(128, 0, 0)),
                new Sphere(new Point(-40.115, 4.486, 0.06), 3.5).setEmission(new Color(128, 0, 0)),
                new Sphere(new Point(-40.115, 4.486, -0.06), 3.5).setEmission(new Color(128, 0, 0))
        );

        scene.lights.add(new SpotLight(new Color(0, 510, 0), new Point(0, 0, 0), new Vector(1, 0, 0)) //
                .setKl(1).setKq(1));

        scene.lights.add(new PointLight(new Color(WHITE), new Point(-60, 20, -20)).setKl(0.001).setKq(0.0002));
        scene.lights.add(new PointLight(new Color(BLUE), new Point(-40, 20, 20)).setKl(0.0005).setKq(0.0001));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ImageWriter imageWriter = new ImageWriter("Bowling", 1500, 1500);
        camera.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene)) //
                .renderImage() //
                .writeToImage();
    }
}
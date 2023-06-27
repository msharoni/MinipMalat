package unittests.renderer;

import org.junit.jupiter.api.Test;

import primitives.*;
import primitives.Color;
import geometries.*;
import renderer.*;
import scene.*;
import lighting.*;

public class UpgradeTests {
    private Scene scene = new Scene("Test scene");

    @Test
    public void Glossy() {
        Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(2500, 2500).setVPDistance(10000); //

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1)));

        scene.geometries.add( //
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 0, 100)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setKr(0.5)),
                new Polygon(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(1500, 1500, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)));

        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150),
                new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        ImageWriter imageWriter = new ImageWriter("Glossy", 500, 500);
        camera.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene).setGlossyBlurry(25)) //
                .renderImage() //
                .writeToImage();
    }

    @Test
    public void Blurry() {

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(150, 150).setVPDistance(1000);

        scene.geometries.add(
                new Polygon(
                        new Point(0, 100, 10),
                        new Point(-100, 100, 10),
                        new Point(-100, -100, 10),
                        new Point(0, -100, 10)).setMaterial(new Material().setKt(0.7))
        );

        scene.geometries.add( //
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(0, 0, 255)) //
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100)));
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500),
                        new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        ImageWriter imageWriter = new ImageWriter("Blurry", 500, 500);
        camera.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene).setGlossyBlurry(25, 700, 20)) //
                .renderImage() //
                .writeToImage();
    }
}
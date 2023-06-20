package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import static primitives.Util.isZero;
import static primitives.Util.random;
import static renderer.Pixel.*;

public class Camera {
    private Point position;
    private Vector vTo;
    private Vector vRight;
    private Vector vUp;
    private double distanceCameraToViewPlane;
    private double width;
    private double height;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    private boolean isAntiAliasingOn;
    private boolean isAdaptiveSuperSamplingOn = false;
    private int eyeRaysAmount = 9;
    private int adaptiveMaxDepth = 4;

    // Depth Of Field
    private boolean isDepthOfFieldOn = false;
    private double apertureRadius = 0;
    private double focalDistance;
    private  double focalRaysAmount = 20;

    // Multithreading
    private boolean isMultithreadingOn = false;

    /**
     *
     * @param position
     * @param vTo
     * @param vUp
     */
    public Camera(Point position, Vector vTo, Vector vUp) {
        if(!isZero(vTo.dotProduct(vUp))) //Vto is not orthogonal to vUp
            throw new IllegalArgumentException("Vto is not orthogonal to vUp");
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();
        this.vRight = vTo.crossProduct(vUp).normalize();
        this.position = position;
    }
    public Camera(Point position,Point target){
        if(position.equals(target))
            throw new IllegalArgumentException("position must by different from target");

        this.position=position;
        vTo=target.subtract(position).normalize();
        vUp =new Vector(0,0,1);
        if (vTo.getZ()!=0) {
            double vUpZ = Math.abs((vTo.getX() * vTo.getX() + vTo.getY() * vTo.getY()) / vTo.getZ());
            vUp = new Vector(vTo.getX(), vTo.getY(), vUpZ).normalize();
        }
        this.vRight=this.vTo.crossProduct(this.vUp);
    }

    /**
     *
     * @param width
     * @param height
     * @return this
     */
    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     *
     * @param distance
     * @return this
     */
    public Camera setVPDistance(double distance) {
        this.distanceCameraToViewPlane = distance;
        return this;
    }

    /**
     *
     * @param imageWriter
     * @return
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    /**
     *
     * @param rayTracerBasic
     * @return
     */
    public Camera setRayTracer(RayTracerBase rayTracerBasic) {
        this.rayTracer = rayTracerBasic;
        return this;
    }

    /**
     *
     * @return Point position
     */
    public Point getPosition() {
        return position;
    }

    /**
     *
     * @return Vector vTo
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     *
     * @return Vector vRight
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     *
     * @return Vector vUp
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     *
     * @return distance from camera to view plane
     */


    /**
     *
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     *
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     *
     * @param nX width of view plane
     * @param nY height of view plane
     * @param j col
     * @param i row
     * @return the constructed ray.
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        //Ray[][] rayThroughPixel = new Ray[nX][nY];
        //rayThroughPixel[i][j] = new Ray(position, )

        Point pIJ = position.add(vTo.scale(distanceCameraToViewPlane)); // = Pc
        double rY = this.height / nY; //rY is the size of the vertical rib of the pixel (without the horizontal rib)
        double rX = this.width / nX; //rX is the size of the horizontal rib of the pixel (without teh vertical rib)

        double xJ = (j - (double)(nX - 1)/2) * rX; //xJ is the horizontal distance of our pixel from the central pixel (in pixels)
        double yI = -(i - (double)(nY - 1)/2) * rY; //yI is the vertical distance of our pixel from the central pixel (in pixels)
        if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));
        Vector vectorToThePixel = pIJ.subtract(this.position);
        Ray rayThroughPixel = new Ray(this.position, vectorToThePixel);
        return rayThroughPixel;
    }

    /**
     * this method render a 3d scene to an image.
     */
    public Camera renderImage() {
        if(imageWriter == null ||
                rayTracer == null)
            throw new MissingResourceException("Not all fields of camera were initiallized", "", "");
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        if(isMultithreadingOn){
            Pixel.initialize(nY, nX, getPrintInterval());
            IntStream.range(0, nY).parallel().forEach(i -> {
                IntStream.range(0, nX).parallel().forEach(j -> {
                    Color pixelColor;
                    Ray ray;
                    if(isAdaptiveSuperSamplingOn){
                        pixelColor = adaptiveAntiAliasing(nX,nY,j,i);
                    }
                    else if(isAntiAliasingOn)
                        pixelColor = antiAliasing(nX,nY,j,i);
                    else{
                        ray = constructRay(nX, nY, j, i);
                        pixelColor = rayTracer.traceRay(ray);
                    }
                    Pixel.pixelDone();
                    Pixel.printPixel();
                    imageWriter.writePixel(j, i, pixelColor);
                });
            });
        }
        else{
            Color pixelColor;
            Ray ray;
            for (int i = 0; i < nX; i++){
                for (int j = 0; j < nY; j++){
                    System.out.println(i + "," + j);
                    if(isAdaptiveSuperSamplingOn){
                        pixelColor = adaptiveAntiAliasing(nX,nY,j,i);
                    }
                    else if(isAntiAliasingOn)
                        pixelColor = antiAliasing(nX,nY,j,i);
                    else{
                        ray = constructRay(nX, nY, j, i);
                        pixelColor = rayTracer.traceRay(ray);
                    }
                    imageWriter.writePixel(j, i, pixelColor);
                }
            }
        }
        return this;
    }

    /**
     * This function implements adaptive super-sampling
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return
     */
    private Color adaptiveAntiAliasing(int nX, int nY, int j, int i) {
        Point center = getCenterOfPixel(nX,nY,j,i);
        double pixelSize = this.height/nY;
        return adaptiveAntiAliasing_(center,pixelSize, adaptiveMaxDepth);
    }

    /**
     * This is the recursive function for adaptive super sampling
     * @param center
     * @param squareSize
     * @param depth
     * @return
     */
    private Color adaptiveAntiAliasing_(Point center, double squareSize, int depth){
        Color result = Color.BLACK;
        Point newCenter = null;
        ArrayList<Color> quartersColors = new ArrayList<Color>(); // each time we divide the square to four squares
        Point p1 = center.add(vUp.scale(0.5*squareSize)).add(vRight.scale(-0.5*squareSize));
        Point p2 = center.add(vUp.scale(0.5*squareSize)).add(vRight.scale(0.5*squareSize));
        Point p3 = center.add(vUp.scale(-0.5*squareSize)).add(vRight.scale(-0.5*squareSize));
        Point p4 = center.add(vUp.scale(-0.5*squareSize)).add(vRight.scale(0.5*squareSize));
        Color p1Color = rayTracer.traceRay(new Ray(position, p1.subtract(position)));
        Color p2Color = rayTracer.traceRay(new Ray(position, p2.subtract(position)));
        Color p3Color = rayTracer.traceRay(new Ray(position, p3.subtract(position)));
        Color p4Color = rayTracer.traceRay(new Ray(position, p4.subtract(position)));
        Color centerColor = rayTracer.traceRay(new Ray(position, center.subtract(position)));
        if(depth == 0 || p1Color.equals(p2Color) && p2Color.equals(p3Color)
                && p3Color.equals(p4Color) && p4Color.equals(centerColor)){
            return p1Color.add(p2Color,p3Color,p4Color,centerColor).scale(0.20);
        }
        else{
            newCenter = center.add(vRight.scale(-0.25*squareSize)).add(vUp.scale(0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(0.25*squareSize)).add(vUp.scale(0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(-0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = p4.add(vRight.scale(0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            return result.scale(0.25);
        }
    }

    /**
     * This function return the center point of a pixel
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i){
        Vector vectorToThePixel;
        Ray rayThroughPixel;
        Point pIJCenter = position.add(vTo.scale(distanceCameraToViewPlane)); // = Pc
        double rY = this.height / nY; //rY is the size of the vertical rib of the pixel (without the horizontal rib)
        double rX = this.width / nX; //rX is the size of the horizontal rib of the pixel (without teh vertical rib)
        double xJ = (j - (double)(nX - 1)/2) * rX; //xJ is the horizontal distance of our pixel from the central pixel (in pixels)
        double yI = -(i - (double)(nY - 1)/2) * rY; //yI is the vertical distance of our pixel from the central pixel (in pixels)
        if (xJ != 0) pIJCenter = pIJCenter.add(vRight.scale(xJ));
        if (yI != 0) pIJCenter = pIJCenter.add(vUp.scale(yI));
        return pIJCenter;
    }
    /**
     * This function implements Anti Aliasing algorithm
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return
     */
    private Color antiAliasing(int nX, int nY, int j, int i) {
        Color sumColors = new Color(0,0,0);
        Vector vectorToThePixel;
        Ray rayThroughPixel;
        Point pIJCenter = position.add(vTo.scale(distanceCameraToViewPlane)); // = Pc
        double rY = this.height / nY; //rY is the size of the vertical rib of the pixel (without the horizontal rib)
        double rX = this.width / nX; //rX is the size of the horizontal rib of the pixel (without teh vertical rib)
        double xJ = (j - (double)(nX - 1)/2) * rX; //xJ is the horizontal distance of our pixel from the central pixel (in pixels)
        double yI = -(i - (double)(nY - 1)/2) * rY; //yI is the vertical distance of our pixel from the central pixel (in pixels)
        if (xJ != 0) pIJCenter = pIJCenter.add(vRight.scale(xJ));
        if (yI != 0) pIJCenter = pIJCenter.add(vUp.scale(yI));

        // make the top-left corner of the pixel
        Point pIJ;
        double interval = rX/eyeRaysAmount;

        for (double z = 0; z < eyeRaysAmount; z++)
        {
            // move pIJ each iteration to the left of the current row
            // (the pixel is divided to grid of rows and columns
            // [interval times rows and interval times columns])
            if(!isZero(rX/2 - (interval)*z))
                pIJ = pIJCenter.add(vUp.scale(rX/2 - (interval)*z));
            else pIJ = pIJCenter;
            pIJ = pIJ.add(vRight.scale(-rX/2));
            for (int q = 0; q < eyeRaysAmount; q++){
                pIJ = pIJ.add(vRight.scale(interval*rX));
                vectorToThePixel = pIJ.subtract(this.position);
                rayThroughPixel = new Ray(this.position, vectorToThePixel);
                sumColors = sumColors.add(rayTracer.traceRay(rayThroughPixel));
            }
        }
        // calculate the average color (there are eyeRaysAmount^2 sample rays);
        Color avarageColor = sumColors.scale((double) 1/(eyeRaysAmount*eyeRaysAmount));
        return avarageColor;
    }

    /**
     * this method add a grid to the rendered image.
     * @param interval the width/height of each square in the grid.
     * @param color
     */
    public Camera printGrid(int interval, Color color) {
        if(imageWriter == null)
            throw new MissingResourceException("ImageWriter is null","","");
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nX; i+= interval){
            for (int j = 0; j < nY; j++){

                imageWriter.writePixel(j, i, color);
            }
        }
        for (int i = 0; i < nX; i++){
            for (int j = 0; j < nY; j+= interval){

                imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }

    /**
     * Function writeToImage produces unoptimized png file of the image according to pixel color matrix in the directory of the project
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }
}
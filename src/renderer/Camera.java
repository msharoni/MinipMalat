package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.MissingResourceException;
import java.util.stream.IntStream;
import static primitives.Util.isZero;


public class Camera {
    final private Point position;
    final private Vector vTo;
    final private Vector vRight;
    final private Vector vUp;
    private double distanceCameraToViewPlane;
    private double width;
    private double height;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    final private boolean isAntiAliasingOn = false;
    final private boolean isAdaptiveSuperSamplingOn = false;

    // Multithreading
    final private boolean isMultithreadingOn = false;

    /**
     *
     * @param position the position
     * @param vTo the vTo vector
     * @param vUp the up vector
     */
    public Camera(Point position, Vector vTo, Vector vUp) {
        if(!isZero(vTo.dotProduct(vUp))) //Vto is not orthogonal to vUp
            throw new IllegalArgumentException("Vto is not orthogonal to vUp");
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();
        this.vRight = vTo.crossProduct(vUp).normalize();
        this.position = position;
    }
    /**
     *
     * @param width width
     * @param height height
     * @return this current camera
     */
    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     *
     * @param distance distance
     * @return this the current camera
     */
    public Camera setVPDistance(double distance) {
        this.distanceCameraToViewPlane = distance;
        return this;
    }

    /**
     *
     * @param imageWriter the gotten image writer
     * @return returns this camera
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    /**
     *
     * @param rayTracerBasic the wanted raytracer-basic
     * @return the current camera
     */
    public Camera setRayTracer(RayTracerBase rayTracerBasic) {
        this.rayTracer = rayTracerBasic;
        return this;
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
        return new Ray(this.position, vectorToThePixel);
    }

    /**
     * this method render a 3d scene to an image.
     */
    public Camera renderImage() {
        if(imageWriter == null ||
                rayTracer == null)
            throw new MissingResourceException("Not all fields of camera were initialized", "", "");
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        if(isMultithreadingOn){
            IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> {
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
                imageWriter.writePixel(j, i, pixelColor);
            }));
        }
        else{
            Color pixelColor;
            Ray ray;
            for (int i = 0; i < nX; i++){
                double total = nX * nY;
                double totalDone = i*nY;
                //System.out.println((totalDone * 100 )/total + "%");
                for (int j = 0; j < nY; j++){
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
            System.out.println("100%");
        }
        return this;
    }

    /**
     * This function implements adaptive super-sampling
     *@param nX width of view plane
     *@param nY height of view plane
     *@param j col
     *@param i row
     * @return what is gotten from the recursive function
     */
    private Color adaptiveAntiAliasing(int nX, int nY, int j, int i) {
        Point center = getCenterOfPixel(nX,nY,j,i);
        double pixelSize = this.height/nY;
        int adaptiveMaxDepth = 4;
        return adaptiveAntiAliasing_(center,pixelSize, adaptiveMaxDepth);
    }

    /**
     * This is the recursive function for adaptive super sampling
     * @param center center
     * @param squareSize size
     * @param depth depth
     * @return antialiasing result
     */
    private Color adaptiveAntiAliasing_(Point center, double squareSize, int depth){
        Color result = Color.BLACK;
        Point newCenter;
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
     *@param nX width of view plane
     *@param nY height of view plane
     *@param j col
     *@param i row
     * @return the center point of a pixel
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i){
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
     *@param nX width of view plane
     *@param nY height of view plane
     *@param j col
     *@param i row
     * @return the color
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
        int eyeRaysAmount = 9;
        double interval = rX/ eyeRaysAmount;

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
        // calculate and return the average color (there are eyeRaysAmount^2 sample rays);
        return sumColors.scale((double) 1/(eyeRaysAmount * eyeRaysAmount));
    }

    /**
     * this method add a grid to the rendered image.
     * @param interval the width/height of each square in the grid.
     * @param color color
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
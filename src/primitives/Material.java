package primitives;


/**
 * The Material class represents the material properties of an object in a scene.
 */
public class Material {

    /**
     * The attenuation coefficient of the diffusive light
     */
    public Double3 kD = new Double3(0),kS = new Double3(0);

    /**
     * The shininess level of the material
     */
    public int nShininess = 0;

    /**
     * Coefficient of transparency
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Coefficient of the reflection
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Sets the diffuse reflection coefficient of the material (Double3).
     * @param kD the diffuse reflection coefficient
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;return this;
    }

    /**
     * Sets the diffuse reflection coefficient of the material (double).
     * @param kD the diffuse reflection coefficient
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * * Sets the specular reflection coefficient of the material (Double3).
     * @param kS the specular reflection coefficient
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;return this;
    }

    /**
     * * Sets the specular reflection coefficient of the material (double)
     * @param kS the specular reflection coefficient
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * setter of the coefficient of transparency (Double3)
     *
     * @param kT the coefficient of transparency
     * @return the updated material
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * setter of the coefficient of transparency (double)
     *
     * @param kT the coefficient of transparency
     * @return the updated material
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * setter of the coefficient of the reflection
     *
     * @param kR the coefficient of the reflection (Double3)
     * @return the updated material
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;

    }

    /**
     * setter of the coefficient of the reflection (double)
     *
     * @param kR the coefficient of the reflection
     * @return the updated material
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;

    }

    /**
     * Sets the shininess exponent of the material.
     * @param nShininess the shininess exponent
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;return this;
    }


    public Double3 getKd() {
        return this.kD;
    }

    public Double3 getKs() {
        return this.kS;
    }

    public int getShininess() {
        return this.nShininess;
    }
}
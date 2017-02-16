package org.engine.graph.lights;

import org.joml.Vector3f;

public class PointLight {

    private Vector3f color;
    public Vector3f getColor() { return color; }
    public void setColor(Vector3f color) {
        this.color = color;
    }    
    private Vector3f position;
    public Vector3f getPosition() { return position; }
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    private float intensity;    
    public float getIntensity() { return intensity; }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    private Attenuation attenuation;
    public Attenuation getAttenuation() { return attenuation; }
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }
    
    public PointLight(Vector3f color, Vector3f position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()), pointLight.getIntensity(), pointLight.getAttenuation());
    }

    public static class Attenuation {

        private float constant;        
        public float getConstant() { return constant; }
        public void setConstant(float constant) {
            this.constant = constant;
        }
        private float linear;
        public float getLinear() { return linear; }
        public void setLinear(float linear) {
            this.linear = linear;
        }
        private float exponent;       
        public float getExponent() { return exponent; }
        public void setExponent(float exponent) {
            this.exponent = exponent;
        }

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }        

    }
}
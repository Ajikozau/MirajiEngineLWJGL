package org.engine.graph;

import org.joml.Vector3f;

public class Material {

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f color;        
    public Vector3f getColor() { return color; }
    public void setColor(Vector3f color) {
        this.color = color;
    }    
    
    private float reflectance;
    public float getReflectance() { return reflectance; }
    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    private Texture texture;      
    public Texture getTexture() { return texture; }
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    private Texture normalMap;    
    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

    public Material() {
        color = DEFAULT_COLOR;
        reflectance = 0;
    }
    
    public Material(Vector3f color, float reflectance) {
        this();
        this.color = color;
        this.reflectance = reflectance;
    }

    public Material(Texture texture) {
        this();
        this.texture = texture;
    }

    public Material(Texture texture, float reflectance) {
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }
    
    public boolean isTextured() {
        return this.texture != null;
    }
    
    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

}
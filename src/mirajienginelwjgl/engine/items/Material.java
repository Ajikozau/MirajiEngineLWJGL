/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.engine.items;

import mirajienginelwjgl.graphics.Texture;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Material {
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f colour;
    public Vector3f getColour() { return colour; }
    public void setColour(Vector3f colour) {
        this.colour = colour;
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
    
    public Material() {
        colour = DEFAULT_COLOUR;
        reflectance = 0;
    }
    
    public Material(Vector3f colour, float reflectance){
        this();
        this.colour = colour;
        this.reflectance = reflectance;
    }
    public Material(Texture texture, float reflectance){
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }
    
    public Material(Texture texture){
        this();
        this.texture = texture;
    }
    
    public boolean isTextured() {
        return texture != null;
    }
}

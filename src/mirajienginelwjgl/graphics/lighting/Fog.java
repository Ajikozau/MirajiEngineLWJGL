/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics.lighting;

import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Fog {
    private boolean active;
    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }
    private Vector3f colour;    
    public Vector3f getColour() { return colour; }
    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
    private float density;
    public float getDensity() { return density; }
    public void setDensity(float density) {
        this.density = density;
    }
    public static Fog NOFOG = new Fog();
    
    public Fog() {
        active = false;
        colour = new Vector3f(0, 0, 0);
        density = 0;
    }
    
    public Fog(boolean active, Vector3f colour, float density){
        this.colour = colour;
        this.density = density;
        this.active = active;
    }
    
    
}

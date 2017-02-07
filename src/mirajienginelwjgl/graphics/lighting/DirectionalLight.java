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
public class DirectionalLight {
    private Vector3f color;
    public Vector3f getColor() { return color; }
    public void setColor(Vector3f color) {
        this.color = color;
    }
    private Vector3f direction;
    public Vector3f getDirection() { return direction; }
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
    private float intensity;    
    public float getIntensity() { return intensity; }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity){
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }
    
    public DirectionalLight(DirectionalLight light){
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Camera {
    private final Vector3f position;
    public Vector3f getPosition() { return position; }
    public void setPosition(float x, float y, float z){
        position.set(x, y, z);
    }
    
    private final Vector3f rotation;
    public Vector3f getRotation() { return rotation; }
    public void setRotation(float x, float y, float z){
        rotation.set(x, y, z);
    }
    
    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }
    
    public Camera(Vector3f position, Vector3f rotation){
        this.position = position;
        this.rotation = rotation;        
    }   
    
    public void movePosition(float offsetX, float offsetY, float offsetZ){
        if (offsetZ != 0){
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0){
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }
    
    public void moveRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}

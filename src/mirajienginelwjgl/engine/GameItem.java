/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.engine;

import mirajienginelwjgl.graphics.Mesh;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class GameItem {
    
    private final Mesh mesh;
    public Mesh getMesh() { return mesh; }
    
    private final Vector3f position;
    public Vector3f getPosition() { return position; }
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);        
    }
    
    private float scale;
    public float getScale() { return scale; }
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    private final Vector3f rotation;
    public Vector3f getRotation() { return rotation; }
    public void setRotation(float x, float y, float z){
        rotation.set(x, y, z);
    }    
    
    public GameItem(Mesh mesh){
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }
    
    
    
    
}

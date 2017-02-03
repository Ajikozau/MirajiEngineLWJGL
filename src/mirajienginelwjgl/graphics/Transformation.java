/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Transformation {
    
    private final Matrix4f projectionMatrix;
    
    private final Matrix4f worldMatrix;
    
    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
    }
    
    public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width/height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }
    
    public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale){
        worldMatrix.identity().translate(offset).rotateX((float)Math.toRadians(rotation.x)).rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).scale(scale);
        return worldMatrix;
    }
}

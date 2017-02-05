/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import mirajienginelwjgl.engine.GameItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Transformation {
    
    private final Matrix4f projectionMatrix;
    
    private final Matrix4f modelViewMatrix;
    
    private final Matrix4f viewMatrix;
    
    public Transformation() {
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }
    
    public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width/height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }
    
    /*public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale){
        worldMatrix.identity().translate(offset).rotateX((float)Math.toRadians(rotation.x)).rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).scale(scale);
        return worldMatrix;
    }*/
       
    public Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        
        viewMatrix.identity();
        //First rotation to rotate over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1,0,0)).rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0));
        //Then translate
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }
    
    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix){
        Vector3f rotation = gameItem.getRotation();
        modelViewMatrix.identity().translate(gameItem.getPosition()).rotateX((float)Math.toRadians(-rotation.x)).rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).scale(gameItem.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }
}

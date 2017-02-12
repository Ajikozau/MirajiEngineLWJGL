/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import mirajienginelwjgl.engine.items.GameItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Transformation {
    
    private final Matrix4f projectionMatrix;    
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }    
    private final Matrix4f modelMatrix;
    private final Matrix4f modelViewMatrix;    
    private final Matrix4f viewMatrix;    
    public Matrix4f getViewMatrix() { return viewMatrix; }    
    private final Matrix4f orthoMatrix;
    private final Matrix4f orthoModelMatrix;    
    
    public Transformation() {
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();        
        viewMatrix = new Matrix4f();
        orthoMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
    }
    
    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width/height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }
    
    public Matrix4f updateViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        
        viewMatrix.identity();
        //First rotation to rotate over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1,0,0)).rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0));
        //Then translate
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }
    
    public Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top){
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }
    
    public Matrix4f buildModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix){
        Vector3f rotation = gameItem.getRotation();
        modelViewMatrix.identity().translate(gameItem.getPosition()).rotateX((float)Math.toRadians(-rotation.x)).rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).scale(gameItem.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }       
   
    public Matrix4f buildOrthoProjModelMatrix(GameItem gameItem, Matrix4f orthoMatrix){
        Vector3f rotation = gameItem.getRotation();
        modelMatrix.identity().translate(gameItem.getPosition()).rotateX((float)Math.toRadians(-rotation.x)).rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).scale(gameItem.getScale());
        orthoModelMatrix.set(orthoMatrix).mul(modelMatrix);
        return orthoModelMatrix;
    }
}

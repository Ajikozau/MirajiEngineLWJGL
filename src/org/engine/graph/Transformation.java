package org.engine.graph;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.engine.items.GameElement;

public class Transformation {

    private final Matrix4f modelMatrix;    
    private final Matrix4f modelViewMatrix;
    private final Matrix4f modelLightViewMatrix;
    private final Matrix4f lightViewMatrix;
    public Matrix4f getLightViewMatrix() { return lightViewMatrix; }
    private final Matrix4f orthoProjMatrix;
    public final Matrix4f getOrthoProjectionMatrix() { return orthoProjMatrix; }
    private final Matrix4f ortho2DMatrix;
    private final Matrix4f orthoModelMatrix;

    public Transformation() {
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        orthoProjMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }    

    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        return orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
    }    

    public void setLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x)).rotateY((float)Math.toRadians(rotation.y)).translate(-position.x, -position.y, -position.z);
    }

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        return ortho2DMatrix.setOrtho2D(left, right, bottom, top);
    }
    
    public Matrix4f buildModelMatrix(GameElement gameItem) {
        Quaternionf rotation = gameItem.getRotation();
        return modelMatrix.translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
    }

    public Matrix4f buildModelViewMatrix(GameElement gameItem, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), viewMatrix);
    }
    
    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(GameElement gameItem, Matrix4f lightViewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), lightViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    public Matrix4f buildOrthoProjModelMatrix(GameElement gameItem, Matrix4f orthoMatrix) {
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(gameItem), orthoModelMatrix);
    }
}

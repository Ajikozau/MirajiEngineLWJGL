/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import helper.StaticHelpers;
import mirajienginelwjgl.engine.GameItem;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author Ajikozau
 */
public class Renderer {
    
    
    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);    
    private static final float Z_NEAR = 0.01f;    
    private static final float Z_FAR = 1000.0f;
        
    private ShaderProgram shaderProgram;
    
    private final Transformation transformation;
    
    public Renderer() {
        transformation = new Transformation();
    }
    
    public void init(Window window) throws Exception {      
        //create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(StaticHelpers.loadResource("/resources/shaders/vertex.vs"));
        shaderProgram.createFragmentShader(StaticHelpers.loadResource("/resources/shaders/fragment.fs"));
        shaderProgram.link();       
        //create matrix
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");      
        shaderProgram.createUniform("texture_sampler");            
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        //update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        
        shaderProgram.setUniform("texture_sampler", 0);
        //render each item
        for (GameItem gameItem : gameItems){
            //set World Matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(gameItem.getPosition(), gameItem.getRotation(), gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            //Render the mesh for this game item
            gameItem.getMesh().render();
        }     
        shaderProgram.unbind();
}
    
     public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}

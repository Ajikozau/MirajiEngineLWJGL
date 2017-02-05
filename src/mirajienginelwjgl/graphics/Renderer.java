/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import helper.StaticHelpers;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.graphics.lighting.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL11.*;

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
    
    private final Transformation transformation;    
    
    private ShaderProgram shaderProgram;
    private float specularPower;
        
    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }
    
    public void init(Window window) throws Exception {      
        //create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(StaticHelpers.loadResource("/resources/shaders/vertex.vs"));
        shaderProgram.createFragmentShader(StaticHelpers.loadResource("/resources/shaders/fragment.fs"));
        shaderProgram.link();       
        //create matrix
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");      
        shaderProgram.createUniform("texture_sampler");       
        //create material uniform
        shaderProgram.createMaterialUniform("material");
        //create lighting uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight, PointLight pointLight) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        //update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        //update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        //update Light uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);
        //copy light object and transform
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1).mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currPointLight);               
        
        shaderProgram.setUniform("texture_sampler", 0);
        //render each item
        for (GameItem gameItem : gameItems){
            Mesh mesh = gameItem.getMesh();
            //set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            //Render the mesh for this game item
            shaderProgram.setUniform("material", mesh.getMaterial());
            mesh.render();
        }     
        shaderProgram.unbind();
}
    
     public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}

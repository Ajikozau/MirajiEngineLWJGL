/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import helper.StaticHelpers;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.graphics.lighting.DirectionalLight;
import mirajienginelwjgl.graphics.lighting.PointLight;
import mirajienginelwjgl.graphics.lighting.SpotLight;
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
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;
    
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
        shaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        shaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        shaderProgram.createDirectionalLightUniform("directionalLight");
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight, PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {
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
        //update light uniforms
        renderLights(viewMatrix, ambientLight, pointLightList, spotLightList, directionalLight);
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
    
    public void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight){
        //update Light uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);
        //copy pointlight objects and transform
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++){
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1).mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLights", currPointLight, i);            
        }
        
        //copy spotlight and transform
        numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++){
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0).mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));

            Vector3f spotLightPos = currSpotLight.getPointLight().getPosition();
            Vector4f auxSpot = new Vector4f(spotLightPos, 1).mul(viewMatrix);
            spotLightPos.x = auxSpot.x;
            spotLightPos.y = auxSpot.y;
            spotLightPos.z = auxSpot.z;
            shaderProgram.setUniform("spotLights", currSpotLight, i);     
        }
        //copy directionallight and transform
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0).mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform("directionalLight", currDirLight);
    }
    
     public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}

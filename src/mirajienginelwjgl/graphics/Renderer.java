/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import helper.StaticHelpers;
import java.util.List;
import java.util.Map;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.items.SkyBox;
import mirajienginelwjgl.graphics.hud.IHud;
import mirajienginelwjgl.graphics.lighting.DirectionalLight;
import mirajienginelwjgl.graphics.lighting.PointLight;
import mirajienginelwjgl.graphics.lighting.SceneLight;
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
    
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram hudShaderProgram;
    private ShaderProgram skyBoxShaderProgram;
    private final float specularPower;
        
    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }
    
    public void init(Window window) throws Exception {      
        setupSkyBoxShader();
        setupSceneShader();
        setupHudShader();        
    }
    
     private void setupSkyBoxShader() throws Exception {
        skyBoxShaderProgram = new ShaderProgram();
        skyBoxShaderProgram.createVertexShader(StaticHelpers.loadResource("/shaders/sb_vertex.vs"));
        skyBoxShaderProgram.createFragmentShader(StaticHelpers.loadResource("/shaders/sb_fragment.fs"));
        skyBoxShaderProgram.link();
        
        skyBoxShaderProgram.createUniform("projectionMatrix");
        skyBoxShaderProgram.createUniform("modelViewMatrix");
        skyBoxShaderProgram.createUniform("texture_sampler");
        skyBoxShaderProgram.createUniform("ambientLight");
    }
     
     private void setupSceneShader() throws Exception {
        //create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(StaticHelpers.loadResource("/shaders/vertex.vs"));
        sceneShaderProgram.createFragmentShader(StaticHelpers.loadResource("/shaders/fragment.fs"));
        sceneShaderProgram.link();       
        //create matrix
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");      
        sceneShaderProgram.createUniform("texture_sampler");       
        //create material uniform
        sceneShaderProgram.createMaterialUniform("material");
        //create lighting uniforms
        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
    }
    
    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(StaticHelpers.loadResource("/shaders/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(StaticHelpers.loadResource("/shaders/hud_fragment.fs"));
        hudShaderProgram.link();
        
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
        hudShaderProgram.createUniform("hasTexture");
    }  
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, Camera camera, Scene scene, IHud hud) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateViewMatrix(camera);
        
        renderScene(window, camera, scene);
        renderSkyBox(window, camera, scene);
        renderHud(window, hud);       
    }
    
     
    private void renderSkyBox(Window window, Camera camera, Scene scene){
        skyBoxShaderProgram.bind();
        skyBoxShaderProgram.setUniform("texture_sampler", 0);        
        
        skyBoxShaderProgram.setUniform("projectionMatrix", transformation.getProjectionMatrix());
        SkyBox skyBox = scene.getSkyBox();        
        Matrix4f viewMatrix = transformation.getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);        
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getSkyBoxLight());
        
        skyBox.getMesh().render();
        skyBoxShaderProgram.unbind();
    }    
    
    public void renderScene(Window window, Camera camera, Scene scene){
        sceneShaderProgram.bind();
        //update projection Matrix
        sceneShaderProgram.setUniform("projectionMatrix", transformation.getProjectionMatrix());
        //update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix();
        //update light uniforms
        renderLights(viewMatrix, scene.getSceneLight());
        sceneShaderProgram.setUniform("texture_sampler", 0);
        //render each mesh associated
        Map<Mesh, List<GameItem>> mapMeshes = scene.getMeshMap();
        for (Mesh mesh : mapMeshes.keySet()){
            sceneShaderProgram.setUniform("material", mesh.getMaterial());
            mesh.renderList(mapMeshes.get(mesh), (GameItem gameItem)->{
                Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
                sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            });
        }        
        sceneShaderProgram.unbind();
    }
    
    public void renderLights(Matrix4f viewMatrix, SceneLight sceneLight){
        //update Light uniforms
        sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());        
        sceneShaderProgram.setUniform("specularPower", specularPower);
        //copy pointlight objects and transform
        PointLight[] pointLightList = sceneLight.getPointLightList();
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++){
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1).mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("pointLights", currPointLight, i);            
        }
        
        //copy spotlight and transform
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
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
            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);     
        }
        //copy directionallight and transform
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0).mul(viewMatrix);                
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }
    
    private void renderHud(Window window, IHud hud){
        hudShaderProgram.bind();
        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for(GameItem gameItem : hud.getGameItems()){
            Mesh mesh = gameItem.getMesh();
            //set orthotaphic and model matrix for this hud
            Matrix4f projModelMatrix = transformation.buildOrthoProjModelMatrix(gameItem, ortho);
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getColour());
            hudShaderProgram.setUniform("hasTexture", gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);
            
            mesh.render();
        }        
        hudShaderProgram.unbind();
    }  
    
    public void cleanup() {                
        if(skyBoxShaderProgram != null){
            skyBoxShaderProgram.cleanup();
        }        
        if(sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if(hudShaderProgram != null){
            hudShaderProgram.cleanup();
        }
    }
}

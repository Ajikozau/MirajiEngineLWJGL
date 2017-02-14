/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.test;

import mirajienginelwjgl.engine.IGameLogic;
import mirajienginelwjgl.engine.MouseInput;
import mirajienginelwjgl.engine.items.SkyBox;
import mirajienginelwjgl.engine.mapping.Terrain;
import mirajienginelwjgl.graphics.Camera;
import mirajienginelwjgl.graphics.Window;
import mirajienginelwjgl.graphics.Renderer;
import mirajienginelwjgl.graphics.Scene;
import mirajienginelwjgl.graphics.lighting.DirectionalLight;
import mirajienginelwjgl.graphics.lighting.SceneLight;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
/**
 *
 * @author Ajikozau
 */
public class TestGame implements IGameLogic {
    
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;
    private final Vector3f cameraInc;
    private final Renderer renderer;    
    private final Camera camera;        
    
    private Scene scene;
    private Hud hud;
    private float lightAngle;
    private Terrain terrain;
    
    public TestGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }
    
    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        scene = new Scene();
        
        float skyBoxScale = 50.0f;
        float terrainScale = 10;
        int terrainSize = 3;
        float minY = -0.1f;
        float maxY = 0.1f;
        int textInc = 40;
        terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "/textures/heightmap.png", "/textures/terrain.png", textInc);
        scene.setGameItems(terrain.getGameItems());
        
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);
        
        setupLights();
        
        hud = new Hud("DEMO");
        
        camera.setPosition(0.0f, 5.0f, 0.0f);
        camera.getRotation().x = 90;
    }
    
    private void setupLights(){
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(1, 1, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }
    
    @Override
    public void input (Window window, MouseInput mouseInput){
        cameraInc.set(0, 0, 0);
        
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;        
        } 
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        } 
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }               
    }
    
    @Override
    public void update (float interval, MouseInput mouseInput){
        if(mouseInput.isRightButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            
            hud.rotateCompass(camera.getRotation().y);
        }
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        
        float height = terrain.getHeight(camera.getPosition());
        if(camera.getPosition().y <= height){
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }
        
        SceneLight sceneLight = scene.getSceneLight();
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 0.5f;
        if(lightAngle > 90){
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
            sceneLight.getSkyBoxLight().set(0.3f, 0.3f, 0.3f);
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getSkyBoxLight().set(factor, factor, factor);
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneLight.getSkyBoxLight().set(1.0f, 1.0f, 1.0f);
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);        
    }
    
    @Override
    public void render(Window window){   
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }
      
    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if(hud != null){
            hud.cleanup();
        }
    }
}

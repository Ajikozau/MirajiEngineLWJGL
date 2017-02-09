/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.test;

import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.IGameLogic;
import mirajienginelwjgl.engine.MouseInput;
import mirajienginelwjgl.engine.items.Material;
import mirajienginelwjgl.engine.items.OBJLoader;
import mirajienginelwjgl.graphics.Camera;
import mirajienginelwjgl.graphics.Mesh;
import mirajienginelwjgl.graphics.Window;
import mirajienginelwjgl.graphics.Renderer;
import mirajienginelwjgl.graphics.Texture;
import mirajienginelwjgl.graphics.lighting.DirectionalLight;
import mirajienginelwjgl.graphics.lighting.PointLight;
import mirajienginelwjgl.graphics.lighting.SceneLight;
import mirajienginelwjgl.graphics.lighting.SpotLight;
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
    private final Camera camera;        
    private final Renderer renderer;
    
    private GameItem[] gameItems;    
    private SceneLight sceneLight;
    private Hud hud;
    private float lightAngle;
    private float spotAngle = 0;
    private float spotInc = 1;
    
    public TestGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }
    
    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        
        float reflectance = 1f;
        //Mesh mesh = OBJLoader.loadMesh("/resources/models/bunny.obj");
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/grassblock.png");
        Material material = new Material(texture, reflectance);
        
        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[]{gameItem};
        
        sceneLight = new SceneLight();
        
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        //pointlight
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);        
        sceneLight.setPointLightList(new PointLight[]{pointLight});
        
        //spotlight
        lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0,0,-1);
        float cutOff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutOff);
        sceneLight.setSpotLightList(new SpotLight[]{spotLight, new SpotLight(spotLight)});
        
        lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
        
        hud = new Hud("DEMO");
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
        
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        PointLight pl = spotLightList[0].getPointLight();
        float lightPos = pl.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)){
            pl.getPosition().z = lightPos + 0.1f;            
        } else if (window.isKeyPressed(GLFW_KEY_M)){
            pl.getPosition().z = lightPos - 0.1f;
        }
    }
    
    @Override
    public void update (float interval, MouseInput mouseInput){
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        
        if(mouseInput.isRightButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            
            hud.rotateCompass(camera.getRotation().y);
        }
        
        //update spotlight
        spotAngle += spotInc * 0.05f;
        if(spotAngle > 2){
            spotInc = -1;
        } else if (spotAngle < -2) {
            spotInc = 1;
        }
        double spotAngleRad = Math.toRadians(spotAngle);
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        Vector3f coneDir = spotLightList[0].getConeDirection();
        coneDir.y = (float) Math.sin(spotAngleRad);
        
        //update directional light       
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 1.1f;
        if(lightAngle > 90){
            directionalLight.setIntensity(0);
            if(lightAngle >= 360){
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80){
            float factor = 1 - (float) (Math.abs(lightAngle) -80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
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
        renderer.render(window, camera, gameItems, sceneLight, hud);
    }
      
    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
        hud.cleanup();
    }
}

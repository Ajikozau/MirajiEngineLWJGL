/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.test;

import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.IGameLogic;
import mirajienginelwjgl.engine.MouseInput;
import mirajienginelwjgl.engine.items.OBJLoader;
import mirajienginelwjgl.graphics.Camera;
import mirajienginelwjgl.graphics.Mesh;
import mirajienginelwjgl.graphics.Window;
import mirajienginelwjgl.graphics.Renderer;
import mirajienginelwjgl.graphics.Texture;
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
    
    public TestGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }
    
    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        Mesh mesh = OBJLoader.loadMesh("/resources/models/bunny.obj");
        //Mesh mesh = OBJLoader.loadMesh("/resources/models/cube.obj");
        //Texture texture = new Texture("/resources/textures/grassblock.png");
        //mesh.setTexture(texture);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(1.5f);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[]{gameItem};
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
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        
        if(mouseInput.isRightButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }
    
    @Override
    public void render(Window window){        
        renderer.render(window, camera, gameItems);
    }
   
   
    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}

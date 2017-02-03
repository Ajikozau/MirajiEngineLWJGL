/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.test;

import mirajienginelwjgl.engine.GameItem;
import mirajienginelwjgl.engine.IGameLogic;
import mirajienginelwjgl.graphics.Mesh;
import mirajienginelwjgl.graphics.Window;
import mirajienginelwjgl.graphics.Renderer;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
/**
 *
 * @author Ajikozau
 */
public class TestGame implements IGameLogic {
    
    private int displxInc = 0;
    private int displyInc = 0;
    private int displzInc = 0;
    private int scaleInc = 0;
    
    private final Renderer renderer;
    private GameItem[] gameItems;
    
    public TestGame() {
        renderer = new Renderer();
    }
    
    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float[] positions = new float[]{
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,};
        float[] colours = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{0, 1, 3, 3, 1, 2,};
        Mesh mesh = new Mesh(positions, colours, indices);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[] { gameItem };
    }
    
    @Override
    public void input (Window window){
        displxInc = 0;
        displyInc = 0;
        displzInc = 0;
        scaleInc = 0;
        
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }
    
    @Override
    public void update (float interval){
        for (GameItem gameItem : gameItems){
            //position
            Vector3f itemPos = gameItem.getPosition();
            gameItem.setPosition(itemPos.x + displxInc * 0.01f, itemPos.y + displyInc * 0.01f, itemPos.z + displzInc * 0.01f);
            //scale
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if ( scale < 0 ) {
                scale = 0;
            }
            gameItem.setScale(scale);
            //rotation angle
            float rotation = gameItem.getRotation().z + 1.5f;
            if (rotation > 360) {
                rotation = 0;
            }
            gameItem.setRotation(0, 0, rotation);
        }
    }
    
    @Override
    public void render(Window window){        
        renderer.render(window, gameItems);
    }
   
   
    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}

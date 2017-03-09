/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

import java.util.List;
import org.engine.MouseInput;
import org.engine.Window;
import org.engine.elements.GameElement;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public abstract class GameState {
    protected static final float MOUSE_SENSITIVITY = 0.2f;
    protected static final float CAMERA_POS_STEP = 0.10f;    
    
    protected final Vector3f cameraInc;
    protected float angleInc;
    protected float lightAngle;    
    protected boolean leftButtonPressed;    
    
    protected final Game game;    
    public GameState(Game game, Vector3f cameraInc, float angleInc, float lightAngle){
        this.game = game;
        this.cameraInc = cameraInc;
        this.angleInc = angleInc;
        this.lightAngle = lightAngle;
    }
    
    public abstract void init(Window window) throws Exception;    
    public abstract void input(Window window, MouseInput mouseInput);
    public abstract void update(float interval, MouseInput mouseInput, Window window);    
    public abstract void render(Window window);    
    public abstract void cleanup();
}

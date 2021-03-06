package org.engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInput {

    private final Vector2d previousPos;
    private final Vector2d currentPos;
    public Vector2d getCurrentPos() { return currentPos; }
    private final Vector2f displVec;
    public Vector2f getDisplVec() { return displVec; }
   
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;    
    public boolean isLeftButtonPressed() { return leftButtonPressed; }
    private boolean rightButtonPressed = false;    
    public boolean isRightButtonPressed() { return rightButtonPressed; }
    private boolean scrollWheelPressed = false;
    public boolean isScrollWheelPressed() { return scrollWheelPressed; }
    

    //private GLFWCursorPosCallback cursorPosCallback;    
    //private GLFWCursorEnterCallback cursorEnterCallback;    
    //private GLFWMouseButtonCallback mouseButtonCallback;

    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                currentPos.set(xpos, ypos);
            }
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
                scrollWheelPressed = button == GLFW_MOUSE_BUTTON_3 && action == GLFW_PRESS;
            }
        });
    }
    
    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.set(currentPos);
    }
}

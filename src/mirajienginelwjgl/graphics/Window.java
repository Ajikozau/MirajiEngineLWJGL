/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author Ajikozau
 */
public class Window {
    
    private final String title;    
    public String getTitle() { return title; }  

    private int width;
    public int getWidth() { return width; }
    
    private int height;
    public int getHeight() { return height; }

    private boolean resized;
    public boolean isResized() { return resized; }
    public void setResized(boolean resized) {
        this.resized = resized;

    }
    private boolean vSync;
    public boolean vSync() { return vSync; }
    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }
        
    private long windowHandle;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWWindowSizeCallback windowSizeCallback;

    
    public Window(String title, int width, int height, boolean vSync){
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;              
    }
    
    
    public void init() {
        //Setup error callback
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        //Int GLFW.
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        //Configure the window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); //window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); //window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        //Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        //Setup resize callback
        glfwSetWindowSizeCallback(windowHandle, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height){
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });
        
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods){
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                    glfwSetWindowShouldClose(window, true);
                }
            } 
        });
        
        //Get resolution
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Center window
        glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        if (vSync()){
            //Enable v-sync
            glfwSwapInterval(1);
        }
        //Show window
        glfwShowWindow(windowHandle);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
    }
    
    public void setClearColor(float r, float g, float b, float alpha){
        glClearColor(r, g, b, alpha);
    }
    
    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }
    
    public boolean windowShouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }
    
    public void update(){
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
   
}

package org.engine;

import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    
    private final WindowOptions opts;
    public WindowOptions getOptions() { return opts; }
    private final Matrix4f projectionMatrix;    
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }
    
    private String title;        
    private int width;
    public int getWidth() { return width; }
    private int height;
    public int getHeight() { return height; } 
    private long windowHandle;
    public long getWindowHandle() { return windowHandle; }   
    private boolean resized;
    public boolean isResized() { return resized; }
    public void setResized(boolean resized) {
        this.resized = resized;
    }
    private boolean vSync;
    public boolean vSyncOn() { return vSync; }
    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }
    

    public Window(String title, int width, int height, boolean vSync, WindowOptions opts) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        this.opts = opts;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        if (opts.compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        boolean maximized = false;
        // If no size has been specified set it to maximized state
        if (width == 0 || height == 0) {
            // Set up a fixed width and height so window initialization does not fail
            width = 100;
            height = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximized = true;
        }

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        });

        if (!maximized) {
            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center our window
            glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (vSyncOn()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        if (opts.showTriangles) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (opts.cullFace) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }

        // Antialiasing
        if (opts.antialiasing) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }
    }
    
    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / (float) height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }
    
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowHandle, title);        
    }
        
    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
        
    public static class WindowOptions {
        public boolean cullFace;
        public boolean showTriangles;
        public boolean showFps;
        public boolean compatibleProfile;
        public boolean antialiasing;
    }
}

package org.engine.graph;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class ShadowMap {

    public static final int SHADOW_MAP_WIDTH = 1024;
    public static final int SHADOW_MAP_HEIGHT = 1024;

    private final int depthMapFBO;
    public int getDepthMapFBO() { return depthMapFBO; }
    private final Texture depthMapTexture;    
    public Texture getDepthMapTexture() { return depthMapTexture; }

    public ShadowMap() throws Exception {
        // Create a FBO to render the depth map
        depthMapFBO = glGenFramebuffers();
        // Create the depth map texture
        depthMapTexture = new Texture(SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);
        // Attach the the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture.getId(), 0);
        // Set only depth
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new Exception("Could not create FrameBuffer");
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    public void cleanup() {
        glDeleteFramebuffers(depthMapFBO);
        depthMapTexture.cleanup();
    }
}

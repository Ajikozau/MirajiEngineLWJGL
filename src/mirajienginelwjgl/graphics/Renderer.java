/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import helper.StaticHelpers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author Ajikozau
 */
public class Renderer {
    
    private ShaderProgram shaderProgram;
    
    public Renderer() {
    }
    
    public void init() throws Exception {      
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(StaticHelpers.loadResource("/resources/vertex.vs"));
        shaderProgram.createFragmentShader(StaticHelpers.loadResource("/resources/fragment.fs"));
        shaderProgram.link();        
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();
}
    
     public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}

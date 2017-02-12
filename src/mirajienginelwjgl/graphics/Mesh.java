/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.items.Material;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author Ajikozau
 */
public class Mesh {
    private final int vaoId;    
    public int getVaoId() { return vaoId; }
    private final List<Integer> vboIdList;
    private final int vertexCount;    
    public int getVertexCount() { return vertexCount; }
    private Material material;
    public Material getMaterial() { return material; }
    public void setMaterial(Material material){
        this.material = material;
    }
    
    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices){
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        
        try {              
            vertexCount = indices.length;
            vboIdList = new ArrayList();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            
            // Texture coords VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            
            //Texture normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null){
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null){
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    
    private void initRender(){
        Texture texture = material.getTexture();
        if (texture != null){
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
    
    private void endRender(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }
        
    public void render() {          
        initRender();
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        endRender();        
    }
    
    public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer){
        initRender();
        for (GameItem gameItem : gameItems){
            consumer.accept(gameItem);
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        endRender();
    }
    
    public void cleanUp() {
        glDisableVertexAttribArray(0);
        //delete VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        Texture texture = material.getTexture();
        if (texture != null){
            texture.cleanup();
        }
        
        //delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);        
    }
    
    public void deleteBuffers() {
        glDisableVertexAttribArray(0);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList){
            glDeleteBuffers(vboId);
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
/**
 *
 * @author Ajikozau
 */
public class Texture {
    private int id;
    public int getId(){ return id; }
    private int width;
    public int getWidth() { return width; }   
    private int height;
    public int getHeight() { return height; }
    
    public Texture(String fileName) throws Exception {        
        this(Texture.class.getResourceAsStream(fileName));
    }
    public Texture(int id){
        this.id = id;
    }
    public Texture(InputStream is) throws Exception {
        loadTexture(is);
    }  
    
    private void loadTexture(InputStream is) throws Exception {
        PNGDecoder decoder = new PNGDecoder(is);
        
        this.width = decoder.getWidth();
        this.height = decoder.getHeight();
        
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
        decoder.decode(buf, width * 4, Format.RGBA);
        buf.flip();
        
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        
        glGenerateMipmap(GL_TEXTURE_2D);
    }
    
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }    
    
    public void cleanup() {
        glDeleteTextures(id);
    }        
}

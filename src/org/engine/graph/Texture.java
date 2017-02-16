package org.engine.graph;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {

    private final int id;
    public int getId() { return id; }
    private final int width;
    public int getWidth() { return this.width; }
    private final int height;
    public int getHeight() { return this.height; }
    private int numRows = 1;  
    public int getNumRows() { return numRows; }    
    private int numCols = 1;
    public int getNumCols() { return numCols; }

    /**
     * Creates an empty texture.
     *
     * @param width Width of the texture
     * @param height Height of the texture
     * @param pixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     * @throws Exception
     */
    public Texture(int width, int height, int pixelFormat) throws Exception {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public Texture(String fileName) throws Exception {
        this(Texture.class.getResourceAsStream(fileName));
    }

    public Texture(String fileName, int numCols, int numRows) throws Exception  {
        this(fileName);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    public Texture(InputStream is) throws Exception {
        try {
            // Load Texture file
            PNGDecoder decoder = new PNGDecoder(is);
            width = decoder.getWidth();
            height = decoder.getHeight();
            // Load texture contents into a byte buffer
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buf, width * 4, Format.RGBA);
            buf.flip();
            // Create a new OpenGL texture 
            id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, id);
            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);
            is.close();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }
    
    public void cleanup() {
        glDeleteTextures(id);
    }
}

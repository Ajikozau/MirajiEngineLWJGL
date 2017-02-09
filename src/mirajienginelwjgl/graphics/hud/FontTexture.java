/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import mirajienginelwjgl.graphics.Texture;

/**
 *
 * @author Ajikozau
 */
public class FontTexture {
    private static final String IMAGE_FORMAT = "png";
    private final Font font;
    private final String charSetName;
    private final Map<Character, CharInfo> charMap;
    private Texture texture;
    public Texture getTexture() { return texture; }
    private int height;
    public int getHeight() { return height; }
    public void setHeight(int height) {
        this.height = height;
    }
    private int width;
    public int getWidth() { return width; }
    public void setWidth(int width) {
        this.width = width;
    }
    
    public FontTexture(Font font, String charSetName) throws Exception {
        this.font = font;
        this.charSetName = charSetName;
        charMap = new HashMap<>();        
        buildTexture();
    }
    
    
    public CharInfo getCharInfo(char c){
        return charMap.get(c);
    }    
    
    private String getAllAvailableChars(String charsetName){
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++){
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private void buildTexture() throws Exception {
        //get font metrics for each char
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();
        
        String allChars = getAllAvailableChars(charSetName);
        width = 0;
        height = 0;
        for (char c : allChars.toCharArray()){
            //get size for each char and update global img size
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }
        g2D.dispose();
        
        //create associated image
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();
        
        //dump img to a buffer
        InputStream is;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ImageIO.write(img, IMAGE_FORMAT, out);
                out.flush();
                is = new ByteArrayInputStream(out.toByteArray());
        }
        texture = new Texture(is);
    }    
    
    public static class CharInfo {
        private final int startX;
        public int getStartX() { return startX; }
        private final int width;
        public int getWidth() { return width; }        
        public CharInfo(int startX, int width){
            this.startX = startX;
            this.width = width;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.engine.items;

import helper.StaticHelpers;
import java.util.ArrayList;
import java.util.List;
import mirajienginelwjgl.graphics.Mesh;
import mirajienginelwjgl.graphics.hud.FontTexture;

/**
 *
 * @author Ajikozau
 */
public class TextItem extends GameItem {
    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;
    private final FontTexture fontTexture;
    private String text;
    public String getText() { return text; }
    
    public TextItem(String text, FontTexture fontTexture) throws Exception {
        super();
        this.text = text;
        this.fontTexture = fontTexture;        
        setMesh(buildMesh());
    }
    
    private Mesh buildMesh(){       
        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList();
        char[] characters = text.toCharArray();
        int numChars = characters.length;
        
        float startx = 0;        
        
        for (int i = 0; i < numChars; i++){
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);
                        
            //build character tile (2 triangles)
            //left top vertex
            positions.add(startx); //x 
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);
            
            //left bottom
            positions.add(startx);
            positions.add((float) fontTexture.getHeight());
            positions.add(ZPOS);
            textCoords.add((float)charInfo.getStartX() / (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);
            
            //right bottom
            positions.add(startx + charInfo.getWidth());
            positions.add((float) fontTexture.getHeight());
            positions.add(ZPOS);
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);
            
            //right top
            positions.add(startx + charInfo.getWidth());
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);
            
            //add indices for left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD+2);
            
            startx += charInfo.getWidth();
        }
        
        float[] posArr = StaticHelpers.listToArray(positions);
        float[] textCoordsArr = StaticHelpers.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }   
    
    public void setText(String text){
        this.text = text;
        getMesh().deleteBuffers();
        setMesh(buildMesh());
    }
    
}

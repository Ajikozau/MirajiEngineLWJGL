/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.test;

import java.awt.Font;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.items.Material;
import mirajienginelwjgl.engine.items.OBJLoader;
import mirajienginelwjgl.engine.items.TextItem;
import mirajienginelwjgl.graphics.Mesh;
import mirajienginelwjgl.graphics.Window;
import mirajienginelwjgl.graphics.hud.FontTexture;
import mirajienginelwjgl.graphics.hud.IHud;
import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class Hud implements IHud {
    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);    
    private static final String CHARSET = "ISO-8859-1";
    private final GameItem[] gameItems;
    private final TextItem statusTextItem;    
    private final GameItem compassItem;
    
    public Hud(String statusText) throws Exception{
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        statusTextItem = new TextItem(statusText, fontTexture);
        statusTextItem.getMesh().getMaterial().setColour(new Vector3f(1, 1, 1));
        
        Mesh mesh = OBJLoader.loadMesh("/models/compass.obj");
        Material material = new Material();
        material.setColour(new Vector3f(1,0,0));
        mesh.setMaterial(material);
        compassItem = new GameItem(mesh);
        compassItem.setScale(40.0f);
        //Rotate to transform to screen coords
        compassItem.setRotation(0f, 0f, 180f);
                
        gameItems = new GameItem[]{statusTextItem, compassItem};
    }
    
    public void setStatusText(String statusText){
        statusTextItem.setText(statusText);
    }
    
    public void rotateCompass(float angle){
        compassItem.setRotation(0, 0, 180 + angle);
    }
    
    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }
    
    public void updateSize(Window window){
        statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
        compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}

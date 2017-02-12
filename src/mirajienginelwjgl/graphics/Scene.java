/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mirajienginelwjgl.engine.items.GameItem;
import mirajienginelwjgl.engine.items.SkyBox;
import mirajienginelwjgl.graphics.lighting.SceneLight;

/**
 *
 * @author Ajikozau
 */
public class Scene {
    
    private Map<Mesh, List<GameItem>> meshMap;    
    public Map<Mesh, List<GameItem>> getMeshMap() { return meshMap; }    
    private GameItem[] gameItems;
    public GameItem[] getGameItems(){ return gameItems; }
    
    private SkyBox skyBox;
    public SkyBox getSkyBox(){ return skyBox; }
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }    
    private SceneLight sceneLight;
    public SceneLight getSceneLight() { return sceneLight; }
    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }   

    public Scene() {
        meshMap = new HashMap<>();
    }    
    
    public void setGameItems(GameItem[] gameItems){
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i = 0; i < numGameItems; i++){
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.get(mesh);
            if (list == null){
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }
    
}

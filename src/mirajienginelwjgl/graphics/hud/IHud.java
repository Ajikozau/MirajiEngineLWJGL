/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics.hud;

import mirajienginelwjgl.engine.items.GameItem;

/**
 *
 * @author Ajikozau
 */
public interface IHud {
    GameItem[] getGameItems();
    
    default void cleanup(){
        GameItem[] gameItems = getGameItems();
        for(GameItem gameItem : gameItems){
            gameItem.getMesh().cleanUp();
        }
    }
}

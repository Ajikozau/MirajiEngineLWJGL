/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

/**
 *
 * @author Ajikozau
 */
public abstract class GameEntity {

    public GameEntity(String fileName) {
        parseFile(fileName);
    }
    
    public abstract void parseFile(String fileName);
    
}

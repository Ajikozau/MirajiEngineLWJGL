/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.engine.elements;

import org.engine.graph.Mesh;

/**
 *
 * @author Ajikozau
 */
public abstract class Entity extends GameElement {    
    public void update() {
    }
    
    public Entity(Mesh mesh){
        super(mesh);
    }
    
}

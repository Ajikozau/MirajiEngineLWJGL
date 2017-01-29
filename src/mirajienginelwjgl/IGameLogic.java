/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl;

import mirajienginelwjgl.graphics.Window;

/**
 *
 * @author Ajikozau
 */
public interface IGameLogic {
    void init() throws Exception;
    void input (Window window);
    void update(float interval);
    void render(Window window);
    void cleanup();
}

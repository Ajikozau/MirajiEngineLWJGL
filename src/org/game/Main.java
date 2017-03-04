package org.game;

import org.engine.GameEngine;
import org.engine.Window;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            Game game = new Game(){                
                @Override
                public void createGameStates(){
                
                }
            };
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = true;
            opts.showFps = true;
            opts.compatibleProfile = true;
            opts.antialiasing = true;
            GameEngine gameEng = new GameEngine("GAME", vSync, opts, game);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}

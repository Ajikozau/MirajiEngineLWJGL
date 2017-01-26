/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl;

/**
 *
 * @author Ajikozau
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new TestGame();
            GameEngine gameEng = new GameEngine("GAME", 640, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace(System.out);
            System.exit(-1);
        }
    }
    
}

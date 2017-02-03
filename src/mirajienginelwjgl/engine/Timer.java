/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.engine;

/**
 *
 * @author Ajikozau
 */
public class Timer {
    private double lastLoopTime;
    public double getLastLoopTime(){ return lastLoopTime; }
    
    public void init(){
        lastLoopTime = getTime();
    }
    
    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }
    
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }
    
    
}
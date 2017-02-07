/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirajienginelwjgl.graphics.lighting;

import org.joml.Vector3f;

/**
 *
 * @author Ajikozau
 */
public class SpotLight {
    private PointLight pointLight;
    public PointLight getPointLight() { return pointLight; }
    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }
    private Vector3f coneDirection;
    public Vector3f getConeDirection() { return coneDirection; }
    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }
    private float cutOff;
    public float getCutOff() { return cutOff; }
    public void setCutOff(float cutoff) {
        this.cutOff = cutoff;
    }   

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }
    
    public SpotLight(SpotLight spotLight){
        this(new PointLight(spotLight.getPointLight()), new Vector3f(spotLight.getConeDirection()), spotLight.getCutOff());
    }
    
    public final void setCutOffAngle(float cutOffAngle){
        this.setCutOff((float)Math.cos(Math.toRadians(cutOffAngle)));
    }
    
}

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
public class SceneLight {
    private Vector3f ambientLight;
    public Vector3f getAmbientLight() { return ambientLight; }
    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }    
    private PointLight[] pointLightList;
    public PointLight[] getPointLightList() { return pointLightList; }
    public void setPointLightList(PointLight[] pointLightList) {
        this.pointLightList = pointLightList;
    }
    private SpotLight[] spotLightList;
    public SpotLight[] getSpotLightList() { return spotLightList; }
    public void setSpotLightList(SpotLight[] spotLightList) {
        this.spotLightList = spotLightList;
    }
    private DirectionalLight directionalLight;
    public DirectionalLight getDirectionalLight() { return directionalLight; }
    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }
    private Vector3f skyBoxLight;
    public Vector3f getSkyBoxLight() { return skyBoxLight; }
    public void setSkyBoxLight(Vector3f skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }   
}

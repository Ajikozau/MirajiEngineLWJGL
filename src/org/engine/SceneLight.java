package org.engine;

import java.util.List;
import org.joml.Vector3f;
import org.engine.graph.lights.DirectionalLight;
import org.engine.graph.lights.PointLight;
import org.engine.graph.lights.SpotLight;

public class SceneLight {

    private Vector3f ambientLight;
    public Vector3f getAmbientLight() { return ambientLight; }
    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }
    private List<SpotLight> spotLightList;
    public List<SpotLight> getSpotLightList() { return spotLightList; }
    public void setSpotLightList(List<SpotLight> spotLightList) {
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
    private List<PointLight> pointLightList;    
    public List<PointLight> getPointLightList() { return pointLightList; }
    public void setPointLightList(List<PointLight> pointLightList) {
        this.pointLightList = pointLightList;
    }
    
    

    
}
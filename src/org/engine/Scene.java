package org.engine;

import helper.StaticHelpers;
import org.engine.elements.SkyBox;
import org.engine.elements.GameElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.engine.graph.InstancedMesh;
import org.engine.graph.Mesh;
import org.engine.graph.particles.IParticleEmitter;
import org.engine.graph.weather.Fog;

public class Scene {

    private final Map<Mesh, List<GameElement>> gameMeshes;
    public Map<Mesh, List<GameElement>> getGameMeshes() { return gameMeshes; }
    private final Map<InstancedMesh, List<GameElement>> instancedGameMeshes;
    public Map<InstancedMesh, List<GameElement>> getGameInstancedMeshes() { return instancedGameMeshes; }
    
    private SkyBox skyBox;    
    public SkyBox getSkyBox() { return skyBox; }
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }  
    private SceneLight sceneLight;    
    public SceneLight getSceneLight() { return sceneLight; }
    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
    private Fog fog;     
    public Fog getFog() { return fog; }
    public void setFog(Fog fog) {
        this.fog = fog;
    }    
    private boolean renderShadows;   
    public boolean renderShadows() { return renderShadows; }       
    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
    }
    private Set<IParticleEmitter> particleEmitters;
    public Set<IParticleEmitter> getParticleEmitters() { return particleEmitters; }
    public void setParticleEmitters(Set<IParticleEmitter> particleEmitters) {
        this.particleEmitters = particleEmitters;
    }

    public Scene() {
        gameMeshes = new HashMap();
        instancedGameMeshes = new HashMap();
        fog = Fog.noFog;
        renderShadows = true;
    }    

    public void setGameElements(List<List<GameElement>> gameElements) {
        // Create a map of meshes to speed up rendering              
        StaticHelpers.iterateList(gameElements, elementList -> {
            StaticHelpers.iterateList(elementList, element -> {
            Mesh[] meshes = element.getMeshes();
            for (Mesh mesh : meshes) {
                boolean instancedMesh = mesh instanceof InstancedMesh;
                List<GameElement> list = instancedMesh ? instancedGameMeshes.get(mesh) : gameMeshes.get(mesh);
                if (list == null) {
                    list = new ArrayList<>();
                    if (instancedMesh) {
                        instancedGameMeshes.put((InstancedMesh)mesh, list);
                    } else {
                        gameMeshes.put(mesh, list);
                    }
                }
                list.add(element);
            }
            });
        });
    }

    public void cleanup() {
        for (Mesh mesh : gameMeshes.keySet()) {
            mesh.cleanUp();
        }
        for (Mesh mesh : instancedGameMeshes.keySet()) {
            mesh.cleanUp();
        }
        if (particleEmitters != null) {
            for (IParticleEmitter particleEmitter : particleEmitters) {
                particleEmitter.cleanup();
            }
        }
    }
}

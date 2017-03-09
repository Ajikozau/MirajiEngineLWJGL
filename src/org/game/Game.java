package org.game;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.engine.MouseInput;
import org.engine.Scene;
import org.engine.SceneLight;
import org.engine.Window;
import org.engine.graph.Camera;
import org.engine.graph.Renderer;
import org.engine.graph.lights.DirectionalLight;
import org.engine.graph.particles.FlowParticleEmitter;
import org.engine.elements.GameElement;
import org.engine.elements.Terrain;
import org.engine.sound.SoundListener;
import org.engine.sound.SoundManager;

public abstract class Game {    
    
    protected final Renderer renderer;
    protected final SoundManager soundMgr;
    protected final Camera camera;
    
    protected GameState gameState;
    
    protected Scene scene;
    protected Hud hud;
    protected Terrain terrain;
    protected FlowParticleEmitter particleEmitter;
    protected MouseBoxSelectionDetector selectDetector;
    protected enum Sounds {
        MUSIC, BEEP, FIRE
    };
    protected List<List<GameElement>> gameElements;
    public List<List<GameElement>> getGameElements() {  return gameElements; }
    protected List<GameElement> layoutElements;
    public List<GameElement> getLayoutElements() { return layoutElements; }
    

    public Game() {
        gameState = new DefaultGameState(this, new Vector3f(0.0f, 0.0f, 0.0f), 0, 45);
        renderer = new Renderer();
        hud = new Hud();
        soundMgr = new SoundManager();
        camera = new Camera();        
        gameElements = new ArrayList<>();
        layoutElements = new ArrayList<>();
        gameElements.add(layoutElements);
    }
    
    public void init(Window window) throws Exception {
        gameState.init(window);
    }   
    
    protected void setupSounds() throws Exception {
        /*SoundBuffer buffBack = new SoundBuffer("/sounds/background.ogg");
        soundMgr.addSoundBuffer(buffBack);
        SoundSource sourceBack = new SoundSource(true, true);
        sourceBack.setBuffer(buffBack.getBufferId());
        soundMgr.addSoundSource(Sounds.MUSIC.toString(), sourceBack);

        SoundBuffer buffBeep = new SoundBuffer("/sounds/beep.ogg");
        soundMgr.addSoundBuffer(buffBeep);
        SoundSource sourceBeep = new SoundSource(false, true);
        sourceBeep.setBuffer(buffBeep.getBufferId());
        soundMgr.addSoundSource(Sounds.BEEP.toString(), sourceBeep);

        SoundBuffer buffFire = new SoundBuffer("/sounds/fire.ogg");
        soundMgr.addSoundBuffer(buffFire);
        SoundSource sourceFire = new SoundSource(true, false);
        Vector3f pos = particleEmitter.getBaseParticle().getPosition();
        sourceFire.setPosition(pos);
        sourceFire.setBuffer(buffFire.getBufferId());
        soundMgr.addSoundSource(Sounds.FIRE.toString(), sourceFire);
        sourceFire.play();
        */
        soundMgr.setListener(new SoundListener(new Vector3f(0, 0, 0)));

        //sourceBack.play();
        
    }

    protected void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        //sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(0, 1, 1), 1.0f);
        directionalLight.setShadowPosMult(10);
        directionalLight.setOrthoCoords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);
    }

    public void input(Window window, MouseInput mouseInput) {
        gameState.input(window, mouseInput);
    }

    public void update(float interval, MouseInput mouseInput, Window window) {
        gameState.update(interval, mouseInput, window);
    }

    public void render(Window window) {
        renderer.render(window, camera, scene);
        hud.render(window);
    }

    public void cleanup() {
        renderer.cleanup();
        soundMgr.cleanup();

        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }
    
    protected abstract void createGameStates();
}

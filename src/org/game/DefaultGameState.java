/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

import org.engine.MouseInput;
import org.engine.Scene;
import org.engine.Window;
import org.engine.graph.Camera;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import org.lwjgl.openal.AL11;

/**
 *
 * @author Ajikozau
 */
public class DefaultGameState extends GameState {

    public DefaultGameState(Game game, Vector3f cameraInc, float angleInc, float lightAngle) {
        super(game, cameraInc, angleInc, lightAngle);
    }    

    @Override
    public void init(Window window) throws Exception {
        Camera camera = game.camera;
        game.hud.init(window);
        game.renderer.init(window);
        game.soundMgr.init();        
        leftButtonPressed = false;
        game.scene = new Scene();        
        game.selectDetector = new MouseBoxSelectionDetector();       
        game.setupLights();        
        camera.getPosition().x = 0.25f;
        camera.getPosition().y = 6.5f;
        camera.getPosition().z = 6.5f;
        camera.getRotation().x = 25;
        camera.getRotation().y = -1;
        // Sounds
        game.soundMgr.init();
        game.soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
        game.setupSounds();
        /*float reflectance = 1f;

        float blockScale = 0.5f;
        float skyBoxScale = 100.0f;
        float extension = 2.0f;

        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy = 0.0f;
        */
        /*
        PNGDecoder decoder = new PNGDecoder(getClass().getResourceAsStream("/textures/heightmap.png"));
        int height = decoder.getHeight();
        int width = decoder.getWidth();
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
        decoder.decode(buf, width * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        int instances = height * width;
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj", instances);
        Texture texture = new Texture("/textures/terrain_textures.png", 2, 1);
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);
        gameItems = new HashSet<GameElement>();        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameElement gameItem = new GameElement(mesh);
                gameItem.setScale(blockScale);
                int rgb = HeightMapMesh.getRGB(i, j, width, buf);
                incy = rgb / (10 * 255 * 255);
                gameItem.setPosition(posx, starty + incy, posz);
                int textPos = Math.random() > 0.5f ? 0 : 1;
                gameItem.setTextPos(textPos);
                gameItems.add(gameItem);

                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }
        scene.setGameElements(gameItems);

        // Particles
        int maxParticles = 200;
        Vector3f particleSpeed = new Vector3f(0, 1, 0);
        particleSpeed.mul(2.5f);
        long ttl = 4000;
        long creationPeriodMillis = 300;
        float range = 0.2f;
        float scale = 0.2f;
        Mesh partMesh = OBJLoader.loadMesh("/models/particle.obj", maxParticles);
        Texture particleTexture = new Texture("/textures/particle_anim.png", 4, 4);
        Material partMaterial = new Material(particleTexture, reflectance);
        partMesh.setMaterial(partMaterial);
        Particle particle = new Particle(partMesh, particleSpeed, ttl, 100);
        particle.setScale(scale);
        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
        particleEmitter.setActive(true);
        particleEmitter.setPositionRndRange(range);
        particleEmitter.setSpeedRndRange(range);
        particleEmitter.setAnimRange(10);
        Set emitters = new HashSet<>();
        emitters.add(particleEmitter);
        scene.setParticleEmitters(emitters);

        // Shadows
        scene.setRenderShadows(false);

        // Fog
        Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
        scene.setFog(new Fog(true, fogColour, 0.02f));

        // Setup  SkyBox
        SkyBox skyBox = new SkyBox("/models/skybox.obj", new Vector3f(0.65f, 0.65f, 0.65f));
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);
        */
        // Setup Lights
        
        
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 0.1f;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            angleInc -= 0.05f;
            game.soundMgr.playSoundSource(Game.Sounds.BEEP.toString());
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            angleInc += 0.05f;
            game.soundMgr.playSoundSource(Game.Sounds.BEEP.toString());               
        } else {
            angleInc = 0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {
        Camera camera = game.camera;
        if (mouseInput.isRightButtonPressed()) {
            // Update camera based on mouse            
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.rotate(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.move(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = game.terrain != null ? game.terrain.getHeight(camera.getPosition()) : -Float.MAX_VALUE;
        if (camera.getPosition().y <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        lightAngle += angleInc;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = game.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

        //particleEmitter.update((long) (interval * 1000));

        // Update view matrix
        camera.updateViewMatrix();

        // Update sound listener position;
        game.soundMgr.updateListenerPosition(camera);

        boolean aux = mouseInput.isLeftButtonPressed();        
        if (aux && !leftButtonPressed && game.selectDetector.selectGameItem(game.getGameElements(), window, mouseInput.getCurrentPos(), camera)) {
            game.hud.incCounter();
            System.out.print(mouseInput.getCurrentPos());
        }
        this.leftButtonPressed = aux;
    }

    @Override
    public void render(Window window) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

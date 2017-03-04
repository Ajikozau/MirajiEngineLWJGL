package org.engine.graph.anim;

import java.util.List;
import org.joml.Matrix4f;
import org.engine.graph.Mesh;
import org.engine.elements.GameElement;

public class AnimGameElement extends GameElement {

    private final List<Matrix4f> invJointMatrices;
    public List<Matrix4f> getInvJointMatrices() { return invJointMatrices; }
    
    private int currentFrame;     
    private List<AnimatedFrame> frames;   
    public List<AnimatedFrame> getFrames() { return frames; }
    public void setFrames(List<AnimatedFrame> frames) {
        this.frames = frames;
    }
    
    public AnimGameElement(Mesh[] meshes, List<AnimatedFrame> frames, List<Matrix4f> invJointMatrices) {
        super(meshes);
        this.frames = frames;
        this.invJointMatrices = invJointMatrices;
        currentFrame = 0;
    }    
    
    public AnimatedFrame getCurrentFrame() {
        return frames.get(currentFrame);
    }
    
    public AnimatedFrame getNextFrame() {
        int nextFrame = currentFrame + 1;    
        if ( nextFrame > frames.size() - 1) {
            nextFrame = 0;
        }
        return this.frames.get(nextFrame);
    }

    public void nextFrame() {
        int nextFrame = currentFrame + 1;    
        if ( nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }       
}

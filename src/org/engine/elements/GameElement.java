package org.engine.elements;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.engine.graph.Mesh;

public class GameElement {

    private boolean selected;    
    public boolean isSelected() { return selected; }     
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    private Mesh[] meshes;    
    public Mesh[] getMeshes() { return meshes; }
    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }
    protected final Vector3f position;   
    public Vector3f getPosition() { return position; }
    private float scale;    
    public float getScale() { return scale; }
    public final void setScale(float scale) {
        this.scale = scale;
    }   
    private final Quaternionf rotation;    
    public Quaternionf getRotation() { return rotation; }
    public final void setRotation(Quaternionf q) {
        this.rotation.set(q);
    }
    private int textPos;    
    public int getTextPos() { return textPos; }
    public void setTextPos(int textPos) {
        this.textPos = textPos;
    }

    public GameElement() {
        selected = false;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Quaternionf();
        textPos = 0;
    }

    public GameElement(Mesh mesh) {
        this();
        this.meshes = new Mesh[]{mesh};
    }

    public GameElement(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public float[] getBounds(){
        return meshes[0].getBounds();
    }
    
    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }
    
    public Mesh getMesh() {
        return meshes[0];
    }

    public void setMesh(Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }
}

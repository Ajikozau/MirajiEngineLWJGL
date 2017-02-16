package org.engine.graph.particles;

import org.joml.Vector3f;
import org.engine.graph.Mesh;
import org.engine.graph.Texture;
import org.engine.items.GameElement;

public class Particle extends GameElement {

    private long updateTextureMillis;    
    public long getUpdateTextureMillis() { return updateTextureMillis; }  
    public void setUpdateTextureMills(long updateTextureMillis) {
        this.updateTextureMillis = updateTextureMillis;
    }
    private long currentAnimTimeMillis;    
    private Vector3f speed;
    public void setSpeed(Vector3f speed) { this.speed = speed; }
    /**
     * Time to live for particle in milliseconds.
     */
    private long ttl;
    public long geTtl() { return ttl; }
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
    private int animFrames;
    public int getAnimFrames() { return animFrames; }
    public Vector3f getSpeed() {
        return speed;
    }    
    
    public Particle(Mesh mesh, Vector3f speed, long ttl, long updateTextureMillis) {
        super(mesh);
        this.speed = new Vector3f(speed);
        this.ttl = ttl;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimTimeMillis = 0;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.animFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        setRotation(baseParticle.getRotation());
        setScale(baseParticle.getScale());
        this.speed = new Vector3f(baseParticle.speed);
        this.ttl = baseParticle.geTtl();
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimTimeMillis = 0;
        this.animFrames = baseParticle.getAnimFrames();
    }
    
    /**
     * Updates the Particle's TTL
     * @param elapsedTime Elapsed Time in milliseconds
     * @return The Particle's TTL
     */
    public long updateTtl(long elapsedTime) {
        this.ttl -= elapsedTime;
        this.currentAnimTimeMillis += elapsedTime;
        if ( this.currentAnimTimeMillis >= this.getUpdateTextureMillis() && this.animFrames > 0 ) {
            this.currentAnimTimeMillis = 0;
            int pos = this.getTextPos();
            pos++;
            if ( pos < this.animFrames ) {
                this.setTextPos(pos);
            } else {
                this.setTextPos(0);
            }
        }
        return this.ttl;
    }    
}
package org.engine.graph.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;
import org.engine.elements.GameElement;

public class FlowParticleEmitter implements IParticleEmitter {

    private final List<GameElement> particles;    
    @Override
    public List<GameElement> getParticles() {
        return particles;
    }
    private final Particle baseParticle;
    @Override
    public Particle getBaseParticle() {
        return baseParticle;
    }
    
    private int maxParticles;
    public int getMaxParticles() { return maxParticles; }
    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }   
    private boolean active;
    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }
    private long creationPeriodMillis;
    public long getCreationPeriodMillis() { return creationPeriodMillis; }  
    public void setCreationPeriodMillis(long creationPeriodMillis) {
        this.creationPeriodMillis = creationPeriodMillis;
    }
    private long lastCreationTime;
    private float speedRndRange;
    public float getSpeedRndRange() { return speedRndRange; }
    public void setSpeedRndRange(float speedRndRange) {
        this.speedRndRange = speedRndRange;
    }
    private float positionRndRange;
    public float getPositionRndRange() { return positionRndRange; }
    public void setPositionRndRange(float positionRndRange) {
        this.positionRndRange = positionRndRange;
    }
    private float scaleRndRange;
    public float getScaleRndRange() { return scaleRndRange; }
    public void setScaleRndRange(float scaleRndRange) {
        this.scaleRndRange = scaleRndRange;
    }
    private long animRange;
    public void setAnimRange(long animRange) {
        this.animRange = animRange;
    }
    public FlowParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
        particles = new ArrayList<>();
        this.baseParticle = baseParticle;
        this.maxParticles = maxParticles;
        this.active = false;
        this.lastCreationTime = 0;
        this.creationPeriodMillis = creationPeriodMillis;
    }  

    public void update(long elapsedTime) {
        long now = System.currentTimeMillis();
        if (lastCreationTime == 0) {
            lastCreationTime = now;
        }
        Iterator<? extends GameElement> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = (Particle) it.next();
            if (particle.updateTtl(elapsedTime) < 0) {
                it.remove();
            } else {
                updatePosition(particle, elapsedTime);
            }
        }

        int length = this.getParticles().size();
        if (now - lastCreationTime >= creationPeriodMillis && length < maxParticles) {
            createParticle();
            this.lastCreationTime = now;
        }
    }

    private void createParticle() {
        Particle particle = new Particle(baseParticle);
        // Add a little bit of randomness of the parrticle
        float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
        float speedInc = sign * (float)Math.random() * speedRndRange;
        float posInc = sign * (float)Math.random() * positionRndRange;        
        float scaleInc = sign * (float)Math.random() * scaleRndRange;        
        long updateAnimInc = (long)sign *(long)(Math.random() * (float)animRange);
        particle.getPosition().add(posInc, posInc, posInc);
        particle.getSpeed().add(speedInc, speedInc, speedInc);
        particle.setScale(particle.getScale() + scaleInc);
        particle.setUpdateTextureMills(particle.getUpdateTextureMillis() + updateAnimInc);
        particles.add(particle);
    }

    /**
     * Updates a particle position
     * @param particle The particle to update
     * @param elapsedTime Elapsed time in milliseconds
     */
    public void updatePosition(Particle particle, long elapsedTime) {
        Vector3f speed = particle.getSpeed();
        float delta = elapsedTime / 1000.0f;
        float dx = speed.x * delta;
        float dy = speed.y * delta;
        float dz = speed.z * delta;
        Vector3f pos = particle.getPosition();
        particle.setPosition(pos.x + dx, pos.y + dy, pos.z + dz);
    }

    @Override
    public void cleanup() {
        for (GameElement particle : getParticles()) {
            particle.cleanup();
        }
    }
}

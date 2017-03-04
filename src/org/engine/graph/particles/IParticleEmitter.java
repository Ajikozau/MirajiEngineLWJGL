package org.engine.graph.particles;

import java.util.List;
import org.engine.elements.GameElement;

public interface IParticleEmitter {
    Particle getBaseParticle();    
    List<GameElement> getParticles();
    void cleanup();    
}

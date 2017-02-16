package org.game;

import java.util.Set;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.engine.Window;
import org.engine.graph.Camera;
import org.engine.items.GameElement;

public class MouseBoxSelectionDetector extends CameraBoxSelectionDetector {

    private final Matrix4f invProjectionMatrix;    
    private final Matrix4f invViewMatrix;
    private final Vector3f mouseDir;    
    private final Vector4f tmpVec;

    public MouseBoxSelectionDetector() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
    }
    
    public boolean selectGameItem(Set<GameElement> gameItems, Window window, Vector2d mousePos, Camera camera) {
        // Transform mouse coordinates into normalized spaze [-1, 1]
                
        float x = (float)(2 * mousePos.x) / (float)window.getWidth() - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)window.getHeight();
        float z = -1.0f;
        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();        
        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;
        
        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);
        
        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameItem(gameItems, camera.getPosition(), mouseDir);
    }
}

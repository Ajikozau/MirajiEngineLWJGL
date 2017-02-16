package org.game;

import java.util.Set;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.engine.graph.Camera;
import org.engine.items.GameElement;

public class CameraBoxSelectionDetector {

    private final Vector3f max;
    private final Vector3f min;
    private final Vector2f nearFar;
    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(Set<GameElement> gameItems, Camera camera) {        
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        selectGameItem(gameItems, camera.getPosition(), dir);
    }
    
    protected boolean selectGameItem(Set<GameElement> gameItems, Vector3f center, Vector3f dir) {
        boolean selected = false;
        GameElement selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        for (GameElement gameItem : gameItems) {
            gameItem.setSelected(false);
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }
        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
            selected = true;
        }
        return selected;
    }
}

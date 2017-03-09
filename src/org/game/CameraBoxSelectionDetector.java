package org.game;

import java.util.List;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.engine.graph.Camera;
import org.engine.elements.GameElement;

public class CameraBoxSelectionDetector {

    private final Vector3f max;
    private final Vector3f min;
    private final Vector2f nearFar;
    private final Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(List<List<GameElement>> gameElements, Camera camera) {        
        dir.set(camera.getViewMatrix().positiveZ(dir).negate());
        selectGameItem(gameElements, camera.getPosition(), dir);
    }
    
    protected boolean selectGameItem(List<List<GameElement>> gameElements, Vector3f center, Vector3f dir) {
        boolean selected = false;
        GameElement selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        for (List<GameElement> gameElementList : gameElements){
            for (GameElement gameElement : gameElementList){
                gameElement.setSelected(false);
                min.set(gameElement.getPosition());
                max.set(gameElement.getPosition());
                min.add(-gameElement.getScale(), -gameElement.getScale(), -gameElement.getScale());
                max.add(gameElement.getScale(), gameElement.getScale(), gameElement.getScale());
                if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                    closestDistance = nearFar.x;
                    selectedGameItem = gameElement;
                }
            }
        }
        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
            selected = true;
        }
        return selected;
    }
}

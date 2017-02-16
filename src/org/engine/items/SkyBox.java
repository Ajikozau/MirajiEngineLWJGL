package org.engine.items;

import org.joml.Vector3f;
import org.engine.graph.Material;
import org.engine.graph.Mesh;
import org.engine.loaders.obj.OBJLoader;
import org.engine.graph.Texture;

public class SkyBox extends GameElement {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        skyBoxMesh.setMaterial(new Material(new Texture(textureFile), 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }

    public SkyBox(String objModel, Vector3f color) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        skyBoxMesh.setMaterial(new Material(color, 0));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}

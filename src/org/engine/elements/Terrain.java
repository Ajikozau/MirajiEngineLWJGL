package org.engine.elements;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.nio.ByteBuffer;
import org.joml.Vector3f;
import org.engine.graph.HeightMapMesh;

public class Terrain {

    private final GameElement[] gameElements;
    public GameElement[] getGameElements() { return gameElements; }
    private final int width;
    private final int depth;
    private final int verticesPerCol;
    private final int verticesPerRow;
    private final HeightMapMesh heightMapMesh;
    /**
     * It will hold the bounding box for each terrain block
     */
    private final Box2D[][] boundingBoxes;    

    /**
     * A Terrain is composed by blocks, each block is a GameItem constructed
     * from a HeightMap.
     *
     * @param terrainSize The number of blocks will be terrainSize * terrainSize
     * @param scale The scale to be applied to each terrain block
     * @param minY The minimum y value, before scaling, of each terrain block
     * @param maxY The maximum y value, before scaling, of each terrain block
     * @param heightMapFile
     * @param textureFile
     * @param textInc
     * @throws Exception
     */
    public Terrain(float scale, float minY, float maxY, String heightMapFile, String textureFile, int textInc) throws Exception {        
        PNGDecoder decoder = new PNGDecoder(getClass().getResourceAsStream(heightMapFile));
        width = decoder.getHeight();
        depth = decoder.getWidth();
        gameElements = new GameElement[width * depth];
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * depth * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // The number of vertices per column and row
        verticesPerCol = depth - 1;
        verticesPerRow = width - 1;

        heightMapMesh = new HeightMapMesh(minY, maxY, buf, depth, width, textureFile, textInc);
        boundingBoxes = new Box2D[width][depth];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < depth; col++) {
                float xDisplacement = (col - ((float) width - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) depth - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

                GameElement terrainBlock = new GameElement(heightMapMesh.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPosition(xDisplacement, 0, zDisplacement);
                gameElements[row * width + col] = terrainBlock;

                boundingBoxes[row][col] = getBoundingBox(terrainBlock);
            }
        }
    }

    public float getHeight(Vector3f position) {
        float result = Float.MIN_VALUE;
        // For each terrain block we get the bounding box, translate it to view coodinates
        // and check if the position is contained in that bounding box
        Box2D boundingBox = null;
        boolean found = false;
        GameElement terrainBlock = null;
        for (int row = 0; row < width && !found; row++) {
            for (int col = 0; col < depth && !found; col++) {
                terrainBlock = gameElements[row * width + col];
                boundingBox = boundingBoxes[row][col];
                found = boundingBox.contains(position.x, position.z);
            }
        }

        // If we have found a terrain block that contains the position we need
        // to calculate the height of the terrain on that position
        if (found) {
            Vector3f[] triangle = getTriangle(position, boundingBox, terrainBlock);
            result = interpolateHeight(triangle[0], triangle[1], triangle[2], position.x, position.z);
        }

        return result;
    }

    protected Vector3f[] getTriangle(Vector3f position, Box2D boundingBox, GameElement terrainBlock) {
        // Get the column and row of the heightmap associated to the current position
        float cellWidth = boundingBox.width / (float) verticesPerCol;
        float cellHeight = boundingBox.height / (float) verticesPerRow;
        int col = (int) ((position.x - boundingBox.x) / cellWidth);
        int row = (int) ((position.z - boundingBox.y) / cellHeight);

        Vector3f[] triangle = new Vector3f[3];
        triangle[1] = new Vector3f(
                boundingBox.x + col * cellWidth,
                getWorldHeight(row + 1, col, terrainBlock),
                boundingBox.y + (row + 1) * cellHeight);
        triangle[2] = new Vector3f(
                boundingBox.x + (col + 1) * cellWidth,
                getWorldHeight(row, col + 1, terrainBlock),
                boundingBox.y + row * cellHeight);
        if (position.z < getDiagonalZCoord(triangle[1].x, triangle[1].z, triangle[2].x, triangle[2].z, position.x)) {
            triangle[0] = new Vector3f(
                    boundingBox.x + col * cellWidth,
                    getWorldHeight(row, col, terrainBlock),
                    boundingBox.y + row * cellHeight);
        } else {
            triangle[0] = new Vector3f(
                    boundingBox.x + (col + 1) * cellWidth,
                    getWorldHeight(row + 2, col + 1, terrainBlock),
                    boundingBox.y + (row + 1) * cellHeight);
        }

        return triangle;
    }

    protected float getDiagonalZCoord(float x1, float z1, float x2, float z2, float x) {
        float z = ((z1 - z2) / (x1 - x2)) * (x - x1) + z1;
        return z;
    }

    protected float getWorldHeight(int row, int col, GameElement gameItem) {
        float y = heightMapMesh.getHeight(row, col);
        return y * gameItem.getScale() + gameItem.getPosition().y;
    }

    protected float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z) {
        // Plane equation ax+by+cz+d=0
        float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
        float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
        float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
        float d = -(a * pA.x + b * pA.y + c * pA.z);
        // y = (-d -ax -cz) / b
        float y = (-d - a * x - c * z) / b;
        return y;
    }

    /**
     * Gets the bounding box of a terrain block
     *
     * @param terrainBlock A GameItem instance that defines the terrain block
     * @return The boundingg box of the terrain block
     */
    private Box2D getBoundingBox(GameElement terrainBlock) {
        float scale = terrainBlock.getScale();
        Vector3f position = terrainBlock.getPosition();

        float topLeftX = HeightMapMesh.STARTX * scale + position.x;
        float topLeftZ = HeightMapMesh.STARTZ * scale + position.z;
        float width = Math.abs(HeightMapMesh.STARTX * 2) * scale;
        float height = Math.abs(HeightMapMesh.STARTZ * 2) * scale;
        Box2D boundingBox = new Box2D(topLeftX, topLeftZ, width, height);
        return boundingBox;
    }

    

    static class Box2D {

        public float x;

        public float y;

        public float width;

        public float height;

        public Box2D(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(float x2, float y2) {
            return x2 >= x
                    && y2 >= y
                    && x2 < x + width
                    && y2 < y + height;
        }
    }
}

package com.sideprojects.megamanxphantomblade.physics.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.MovingTileBase;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;

/**
 * Created by buivuhoang on 30/01/18.
 */
public class MovingRectangleTile extends MovingTileBase {
    private RectangleTile tile;
    private Vector2 position;

    public MovingRectangleTile(float x, float y, float width, float height, boolean slippery) {
        tile = new RectangleTile(x, y, width, height, slippery);
        position = new Vector2(x, y);
        frame = new Texture("maps/moving-platform-1.png");
    }

    public MovingRectangleTile(float x, float y, float width, float height) {
        this(x, y, width, height, false);
    }

    @Override
    public float x() {
        return tile.x();
    }

    @Override
    public float y() {
        return tile.y();
    }

    @Override
    public Vector2 getPosition() {
        position.x = tile.x();
        position.y = tile.y();
        return position;
    }

    @Override
    public float getHeight() {
        return tile.getHeight();
    }

    @Override
    public float getWidth() {
        return tile.getWidth();
    }

    @Override
    public float[] getVertices() {
        return tile.getVertices();
    }

    @Override
    public Vector2 getPostCollisionPos(Collision collision) {
        return tile.getPostCollisionPos(collision);
    }

    @Override
    public float getYPositionIfStandingOnTile(float x) {
        return tile.getYPositionIfStandingOnTile(x);
    }

    @Override
    public Collision getCollisionWithTile(MovingObject object,
                                          CollisionDetectionRay ray,
                                          TileBase tileUp,
                                          TileBase tileDown,
                                          TileBase tileLeft,
                                          TileBase tileRight,
                                          TileBase tileTopLeft,
                                          TileBase tileTopRight,
                                          TileBase tileBottomLeft,
                                          TileBase tileBottomRight,
                                          boolean overlapMode) {
        return tile.getCollisionWithTile(
                object,
                ray,
                tileUp,
                tileDown,
                tileLeft,
                tileRight,
                tileTopLeft,
                tileTopRight,
                tileBottomLeft,
                tileBottomRight,
                overlapMode);
    }

    private float stateTime = 0;
    private float vel = 3f;

    @Override
    public void update(float delta) {
        stateTime += delta;
        if (stateTime >= 2) {
            vel = -vel;
            stateTime = 0;
        }
        tile.setPosition(tile.x() + delta * vel, tile.y());
    }
}

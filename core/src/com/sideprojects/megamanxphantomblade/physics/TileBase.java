package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;

/**
 * Created by buivuhoang on 15/04/17.
 */
public abstract class TileBase {
    public abstract float x();
    public abstract float y();
    public abstract float getHeight();
    public abstract float getWidth();
    public abstract float[] getVertices();
    public abstract Vector2 getPostCollisionPos(Collision collision);
    public abstract float getYPositionIfStandingOnTile(float x);

    public abstract Collision getCollisionWithTile(MovingObject object, CollisionDetectionRay ray,
                                          TileBase tileUp,
                                          TileBase tileDown,
                                          TileBase tileLeft,
                                          TileBase tileRight,
                                          TileBase tileTopLeft,
                                          TileBase tileTopRight,
                                          TileBase tileBottomLeft,
                                          TileBase tileBottomRight,
                                          boolean overlapMode);

    protected float calculateFinalY(float finalX, TileBase nextTile) {
        float finalY;
        int xDifference = (int)finalX - (int)x();
        if (xDifference != 0 && nextTile != null && (int)nextTile.x() == (int)finalX) {
            finalY = nextTile.getYPositionIfStandingOnTile(finalX);
        } else {
            finalY = getYPositionIfStandingOnTile(finalX);
        }
        return finalY;
    }
}

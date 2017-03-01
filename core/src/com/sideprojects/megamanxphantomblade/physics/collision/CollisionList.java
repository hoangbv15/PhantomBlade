package com.sideprojects.megamanxphantomblade.physics.collision;

import java.util.List;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class CollisionList {
    public List<Collision> toList;
    private boolean isCollidingSide;
    private float distanceToSideCollision;
    private Collision.Side collidingSide;

    public CollisionList(List<Collision> collisionList) {
        this.toList = collisionList;
        isCollidingSide = false;
        for (Collision collision: toList) {
            if (collision.side == Collision.Side.LEFT ||
                    collision.side == Collision.Side.RIGHT) {
                isCollidingSide = true;
                distanceToSideCollision = collision.dist;
                collidingSide = collision.side;
            }
        }
    }

    public boolean isCollidingSide() {
        return isCollidingSide;
    }

    public float distanceToSideCollision() {
        return distanceToSideCollision;
    }

    public Collision.Side collidingSide() { return collidingSide; }
}

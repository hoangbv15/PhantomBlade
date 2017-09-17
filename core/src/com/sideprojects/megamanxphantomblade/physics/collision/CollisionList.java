package com.sideprojects.megamanxphantomblade.physics.collision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class CollisionList {
    public static final CollisionList Empty = new CollisionList(new ArrayList<>());
    private static float edgeThreshold = 0.2f;
    public List<Collision> toList;
    private boolean isCollidingSide;
    private float distanceToSideCollision;
    private Collision.Side collidingSide;
    private boolean isAtEdgeLeft;
    private boolean isAtEdgeRight;
    private boolean isColliding;

    public CollisionList(List<Collision> collisionList) {
        this.toList = collisionList;
        isCollidingSide = false;
        isAtEdgeLeft = false;
        isAtEdgeRight = false;
        Collision up = null;
        isColliding = !collisionList.isEmpty();
        for (Collision collision: toList) {
            if (collision.side == Collision.Side.Left ||
                    collision.side == Collision.Side.Right ||
                    collision.side == Collision.Side.Down) {
                isCollidingSide = true;
                distanceToSideCollision = collision.dist;
                collidingSide = collision.side;
            }
            if (collision.side == Collision.Side.Up) {
                up = collision;
            }
        }
        // Check if we are at the edge
        if (up != null && !isCollidingSide) {
            // We are only colliding with the up side
            // That means there's a chance we might be at the edge
            if (up.tileLeft == null && Math.abs(up.tile.x() - up.getPostCollidePos().x) <= edgeThreshold) {
                isAtEdgeLeft = true;
            } else if (up.tileRight == null && Math.abs(up.tile.x() + up.tile.getWidth() - up.getPostCollidePos().x) <= edgeThreshold) {
                isAtEdgeRight = true;
            }
        }
    }

    public boolean isCollidingSide() {
        return isCollidingSide;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public float distanceToSideCollision() {
        return distanceToSideCollision;
    }

    public Collision.Side collidingSide() { return collidingSide; }

    public boolean isAtEdge() {
        return isAtEdgeLeft() || isAtEdgeRight();
    }

    public boolean isAtEdgeLeft() {
        return isAtEdgeLeft;
    }

    public boolean isAtEdgeRight() {
        return isAtEdgeRight;
    }
}

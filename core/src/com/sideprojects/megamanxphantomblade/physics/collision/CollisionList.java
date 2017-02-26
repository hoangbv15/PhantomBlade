package com.sideprojects.megamanxphantomblade.physics.collision;

import java.util.List;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class CollisionList {
    public List<Collision> toList;
    private boolean isCollidingSide;

    public CollisionList(List<Collision> collisionList) {
        this.toList = collisionList;
        for (Collision collision: toList) {
            if (collision.side == Collision.Side.LEFT ||
                    collision.side == Collision.Side.RIGHT) {
                isCollidingSide = true;
            }
        }
        isCollidingSide = false;
    }

    public boolean isCollidingSide() {
        return isCollidingSide;
    }
}

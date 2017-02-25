package com.sideprojects.megamanxphantomblade.physics.collision;

import java.util.List;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class CollisionList {
    public List<Collision> toList;

    public CollisionList(List<Collision> collisionList) {
        this.toList = collisionList;
    }

    public boolean isCollidingSide() {
        for (Collision collision: toList) {
            if (collision.side == Collision.Side.LEFT ||
                    collision.side == Collision.Side.RIGHT) {
                return true;
            }
        }
        return false;
    }
}

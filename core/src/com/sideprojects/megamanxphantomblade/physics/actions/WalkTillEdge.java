package com.sideprojects.megamanxphantomblade.physics.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class WalkTillEdge extends Walk {
    public WalkTillEdge(MovingObject object, int direction, float speed, float time) {
        super(object, direction, speed, time);
    }

    private boolean isAtEdge(CollisionList collisions) {
        return (this.direction == MovingObject.LEFT && collisions.isAtEdgeLeft()) ||
                (this.direction == MovingObject.RIGHT && collisions.isAtEdgeRight());
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return isAtEdge(collisions) || super.finish(collisions);
    }
}

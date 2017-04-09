package com.sideprojects.megamanxphantomblade.physics.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class JumpIfAtEdge extends ActionBase {
    private MovingObject object;
    private float velX;
    private float velY;
    private float waitTimeBeforeJump;
    private boolean jumped;
    private boolean landed;
    private float stateTime;

    public JumpIfAtEdge(MovingObject object, float velX, float velY, float waitTimeBeforeJump) {
        this.object = object;
        this.velX = velX;
        this.velY = velY;
        this.waitTimeBeforeJump = waitTimeBeforeJump;
        jumped = false;
        landed = false;
        stateTime = 0;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        if (!collisions.isAtEdge()) {
            jumped = true;
        }
        if (jumped) {
            if (object.grounded) {
                landed = true;
            }
            return;
        }
        if (stateTime <= waitTimeBeforeJump) {
            stateTime += delta;
            object.vel.x = 0;
            object.vel.y = 0;
            return;
        }

        int direction = MovingObject.LEFT;
        if (collisions.isAtEdgeRight()) {
            direction = MovingObject.RIGHT;
        }
        object.vel.x = velX * direction;
        object.vel.y = velY;
        object.grounded = false;
        jumped = true;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return jumped && landed;
    }
}

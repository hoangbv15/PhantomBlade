package com.sideprojects.megamanxphantomblade.physics.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class FlyTowardsMovingObject extends ActionBase {
    private final MovingObject object;
    private final MovingObject target;
    private final float speed;
    private final float time;
    private float stateTime;

    public FlyTowardsMovingObject(MovingObject object, MovingObject target, float speed, float time) {
        this.object = object;
        this.target = target;
        this.speed = speed;
        this.time = time;
        stateTime = 0;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        stateTime += delta;
        float targetVectorX = target.mapCollisionBounds.x - object.mapCollisionBounds.x;
        float targetVectorY = target.mapCollisionBounds.y - object.mapCollisionBounds.y;
        float targetVectorLength = (float)Math.sqrt(targetVectorX * targetVectorX + targetVectorY * targetVectorY);
        float vectorRatio = speed / targetVectorLength;
        object.vel.x = targetVectorX * vectorRatio;
        object.vel.y = targetVectorY * vectorRatio;
        object.direction = targetVectorX > 0 ? 1 : -1;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return stateTime > time || object.mapCollisionBounds.overlaps(target.mapCollisionBounds);
    }
}

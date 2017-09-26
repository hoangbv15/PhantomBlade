package com.sideprojects.megamanxphantomblade.physics.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 04/04/17.
 */
public class Walk extends ActionBase {
    protected final MovingObject object;
    protected int direction;
    private final float speed;
    private final float time;
    private float stateTime;

    public Walk(MovingObject object, int direction, float speed, float time) {
        this.object = object;
        this.direction = direction;
        this.speed = speed;
        this.time = time;
        stateTime = 0;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        stateTime += delta;
        object.vel.x = direction * speed;
        object.direction = direction;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return stateTime > time || collisions.isCollidingSide();
    }
}

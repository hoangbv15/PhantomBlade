package com.sideprojects.megamanxphantomblade.physics.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 04/04/17.
 */
public class Wait extends ActionBase {
    private MovingObject object;
    private float time;
    private float stateTime;

    public Wait(MovingObject object, float time) {
        this.object = object;
        this.time = time;
        stateTime = 0;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        object.vel.x = 0;
        if (!object.isAffectedByGravity()) {
            object.vel.y = 0;
        }
        stateTime += delta;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return stateTime > time;
    }
}

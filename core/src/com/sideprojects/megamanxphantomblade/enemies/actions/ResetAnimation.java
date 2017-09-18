package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class ResetAnimation extends ActionBase {
    private final MovingObject object;
    private boolean executed;

    public ResetAnimation(MovingObject object) {
        this.object = object;
        executed = false;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        object.stateTime = 0;
        executed = true;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return executed;
    }
}

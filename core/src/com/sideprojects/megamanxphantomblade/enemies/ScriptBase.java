package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.utils.Queue;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.actions.Move;
import com.sideprojects.megamanxphantomblade.enemies.actions.Wait;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.Physics;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * This class defines the behaviour of moving objects such as enemies
 * Created by buivuhoang on 04/04/17.
 */
public abstract class ScriptBase extends Physics {
    private MovingObject object;
    private Queue<ActionBase> actionQueue;
    private float delta;

    public ScriptBase(MovingObject object) {
        super();
        this.object = object;
        actionQueue = new Queue<ActionBase>();
    }

    /**
     * This method describes in a script-style, what the object should do
     */
    public abstract void describe();

    protected void move(int direction, float speed, float time) {
        actionQueue.addFirst(new Move(object, direction, speed, time));
    }

    protected void wait(float time) {
        actionQueue.addFirst(new Wait(object, time));
    }

    @Override
    public void inputProcessing(MovingObject object, float delta, MapBase map) {
        this.delta = delta;
        applyGravity(object, map.GRAVITY, map.MAX_FALLSPEED, delta);
    }

    @Override
    public void postCollisionDetectionProcessing(CollisionList collisions) {
        if (actionQueue.size == 0) {
            describe();
            return;
        }
        ActionBase action = actionQueue.last();
        if (!action.finish(collisions)) {
            action.execute(delta);
        } else {
            actionQueue.removeLast();
        }
    }

    @Override
    protected float getPushBackDuration() {
        return 0;
    }

    @Override
    protected float getPushBackSpeed() {
        return 0;
    }
}

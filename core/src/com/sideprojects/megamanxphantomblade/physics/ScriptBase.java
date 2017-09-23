package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.utils.Queue;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.actions.JumpIfAtEdge;
import com.sideprojects.megamanxphantomblade.physics.actions.Walk;
import com.sideprojects.megamanxphantomblade.physics.actions.WalkTillEdge;
import com.sideprojects.megamanxphantomblade.physics.actions.Wait;
import com.sideprojects.megamanxphantomblade.map.MapBase;
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
        actionQueue = new Queue<>();
    }

    /**
     * This method describes in a script-style, what the object should do
     */
    public abstract void describe();

    protected final void addToQueue(ActionBase action) {
        actionQueue.addFirst(action);
    }

    protected final void move(int direction, float speed, float time) {
        addToQueue(new Walk(object, direction, speed, time));
    }

    protected final void moveTillEdge(int direction, float speed, float time) {
        addToQueue(new WalkTillEdge(object, direction, speed, time));
    }

    protected final void jumpIfAtEdge(float velX, float velY, float waitTimeBeforeJump) {
        addToQueue(new JumpIfAtEdge(object, velX, velY, waitTimeBeforeJump));
    }

    protected final void wait(float time) {
        addToQueue(new Wait(object, time));
    }

    @Override
    public final void inputProcessing(MovingObject object, float delta, MapBase map) {
        this.delta = delta;
        applyGravity(object, map.GRAVITY, map.MAX_FALLSPEED, delta);
    }

    @Override
    public final void postCollisionDetectionProcessing(CollisionList collisions) {
        if (actionQueue.size == 0) {
            describe();
            return;
        }
        ActionBase action = actionQueue.last();
        if (!action.finish(collisions)) {
            action.execute(collisions, delta);
        } else {
            actionQueue.removeLast();
        }
    }

    @Override
    protected final float getPushBackDuration() {
        return 0;
    }

    @Override
    protected final float getPushBackSpeed() {
        return 0;
    }
}

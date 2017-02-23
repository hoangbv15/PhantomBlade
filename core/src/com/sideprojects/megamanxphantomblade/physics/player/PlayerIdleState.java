package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.Collision;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public class PlayerIdleState extends PlayerStateBase {
    @Override
    public PlayerStateBase nextState(InputProcessor input, MovingObject object, List<Collision> collisionList) {
        return this;
    }

    @Override
    public void dash() {
    }

    @Override
    public void enter(MovingObject object, List<Collision> collisionList) {
        object.stateTime = 0;
    }

    @Override
    public void left(MovingObject object, float velocity, float delta) {
        object.direction = MovingObject.LEFT;
        object.vel.x = velocity * object.direction;
    }

    @Override
    public void right(MovingObject object, float velocity, float delta) {
        object.direction = MovingObject.RIGHT;
        object.vel.x = velocity * object.direction;
    }

    @Override
    public void jump(MovingObject object, float velocity, float delta) {
    }
}

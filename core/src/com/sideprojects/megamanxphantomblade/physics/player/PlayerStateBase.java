package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.Collision;
import com.sideprojects.megamanxphantomblade.physics.State;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public abstract class PlayerStateBase implements State {
    /**
     * Determine what the next state should be
     * @param input The input that causes the physics reaction
     * @param collisionList The output of the physics engine after receiving the user's input
     * @return The next state
     */
    public abstract PlayerStateBase nextState(InputProcessor input, MovingObject object, List<Collision> collisionList);

    public abstract void dash();

    @Override
    public void none(MovingObject object) {
        object.vel.x = 0;
    }

    @Override
    public void applyGravity(MovingObject object, float gravity, float maxFallspeed, float delta) {
        if (object.vel.y > 0) {
            object.vel.y = 0;
        }
        if (object.vel.y > maxFallspeed) {
            object.vel.y -= gravity * delta;
        }
    }
}

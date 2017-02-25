package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Touchdown extends Idle {
    public Touchdown(PlayerBase player, PlayerState lastState) {
        super(player, lastState);
    }

    @Override
    public PlayerState enter(MovingObject object) {
        object.stateTime = 0;
        return PlayerState.TOUCHDOWN;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.x != 0 && player.grounded) {
            return new Run(player, player.state);
        }
        if (player.vel.y > 0) {
            return new Jump(player, player.state);
        }
        if (player.stateTime >= player.animations.getTouchdownLeft().getAnimationDuration()) {
            return new Idle(player, player.state);
        }
        return this;
    }
}

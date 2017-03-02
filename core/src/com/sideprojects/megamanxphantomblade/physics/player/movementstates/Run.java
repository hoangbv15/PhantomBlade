package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Run extends Idle {
    public Run(InputProcessor input, PlayerBase player, PlayerState lastState) {
        super(input, player, lastState);
    }

    @Override
    public PlayerState enter(MovingObject object) {
        object.stateTime = 0;
        return PlayerState.RUN;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.y > 0) {
            return new Jump(input, player, player.state);
        }
        if (player.vel.y < 0) {
            return new Fall(input, player, player.state);
        }
        if (input.isCommandPressed(Command.DASH) && canDash(input)) {
            return new Dash(input, player);
        }
        if (player.vel.x == 0 && player.grounded) {
            return new Idle(input, player, player.state);
        }
        return this;
    }
}

package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Run extends Idle {
    public Run(InputProcessor input, PlayerBase player, PlayerState lastState, PlayerStateChangeHandler stateChangeHandler) {
        super(input, player, lastState, stateChangeHandler);
    }

    @Override
    public PlayerState enter(PlayerBase player) {
        player.stateTime = 0;
        return PlayerState.Run;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.y > 0) {
            return new Jump(input, player, player.state, stateChangeHandler);
        }
        if (player.vel.y < 0) {
            return new Fall(input, player, player.state, stateChangeHandler);
        }
        if (input.isCommandPressed(Command.DASH) && canDash(input)) {
            if (input.isCommandPressed(Command.UP)) {
                return new Updash(player, player.state, stateChangeHandler);
            }
            return new Dash(input, player, player.state, stateChangeHandler);
        }
        if (player.vel.x == 0 && player.grounded) {
            return new Idle(input, player, player.state, stateChangeHandler);
        }
        return this;
    }
}

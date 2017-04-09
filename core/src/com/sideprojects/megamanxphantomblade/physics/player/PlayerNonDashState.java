package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Movements that needs to not go to dash mode after being transitioned from dash
 * Unless the player releases the dash button
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerNonDashState extends PlayerMovementStateBase {
    private boolean canDash = true;

    public PlayerNonDashState(InputProcessor input, PlayerBase player, PlayerState lastState, PlayerStateChangeHandler stateChangeHandler) {
        super(player, lastState, stateChangeHandler);
        if (lastState == PlayerState.Dash
                || lastState == PlayerState.Updash
                || lastState == PlayerState.Dashbreak
                || input.isCommandPressed(Command.DASH)
                || player.isJumpDashing) {
            canDash = false;
        }
    }

    @Override
    public boolean canDash(InputProcessor input) {
        return canDash;
    }

    @Override
    public void update(InputProcessor input, PlayerBase player) {
        if (!input.isCommandPressed(Command.DASH) && player.grounded) {
            canDash = true;
        }
    }
}

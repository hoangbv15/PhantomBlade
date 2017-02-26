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
    private boolean canDash;

    public PlayerNonDashState(InputProcessor input, PlayerBase player, PlayerState lastState) {
        super(player);
        if (lastState == PlayerState.DASH && input.isCommandPressed(Command.DASH)) {
            canDash = false;
        }
    }

    @Override
    public boolean canDash(InputProcessor input) {
        return canDash;
    }

    @Override
    public void update(InputProcessor input) {
        if (!input.isCommandPressed(Command.DASH)) {
            canDash = true;
        }
    }
}

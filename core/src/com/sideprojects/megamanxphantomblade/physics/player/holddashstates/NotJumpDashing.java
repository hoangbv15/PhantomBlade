package com.sideprojects.megamanxphantomblade.physics.player.holddashstates;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerJumpDashStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class NotJumpDashing extends PlayerJumpDashStateBase {
    @Override
    public boolean isJumpDashing() {
        return false;
    }

    @Override
    public PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player) {
        if (input.isCommandPressed(Command.DASH) && input.isCommandJustPressed(Command.JUMP)
                && (player.grounded || player.state == PlayerState.WALLSLIDE)) {
            return new JumpDashing();
        }
        return this;
    }
}
package com.sideprojects.megamanxphantomblade.physics.player.holddashstates;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerJumpDashStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class JumpDashing extends PlayerJumpDashStateBase {
    @Override
    public boolean isJumpDashing() {
        return true;
    }

    @Override
    public PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player) {
        if (player.grounded
                || player.state == PlayerState.WALLSLIDE
                || !input.isCommandPressed(Command.DASH)) {
            return new NotJumpDashing();
        }
        return this;
    }
}

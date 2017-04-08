package com.sideprojects.megamanxphantomblade.physics.player.jumpdashstate;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerJumpDashStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class NotJumpDashing extends PlayerJumpDashStateBase {
    public NotJumpDashing(PlayerBase player) {
        super(player);
    }

    @Override
    public boolean isJumpDashing() {
        return false;
    }

    @Override
    public void enter(PlayerBase player) {
        player.isJumpDashing = false;
    }

    @Override
    public PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player) {
        if ((input.isCommandPressed(Command.DASH) || player.state == PlayerState.Dash)
                && input.isCommandJustPressed(Command.JUMP)
                && (player.grounded || player.state == PlayerState.Wallslide)) {
            return new JumpDashing(player);
        }
        return this;
    }
}
package com.sideprojects.megamanxphantomblade.physics.player.jumpdashstate;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerJumpDashStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class JumpDashing extends PlayerJumpDashStateBase {
    public JumpDashing(PlayerBase player) {
        super(player);
    }

    @Override
    public boolean isJumpDashing() {
        return true;
    }

    @Override
    public void enter(PlayerBase player) {
        player.isJumpDashing = true;
    }

    @Override
    public PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player) {
        if (player.grounded || player.state == PlayerState.Wallslide || player.isBeingDamaged()) {
            return new NotJumpDashing(player);
        }
        return this;
    }
}

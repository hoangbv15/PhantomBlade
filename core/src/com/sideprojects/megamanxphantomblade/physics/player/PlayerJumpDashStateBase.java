package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerJumpDashStateBase {
    public PlayerJumpDashStateBase(PlayerBase player) {
        enter(player);
    }


    public abstract boolean isJumpDashing();
    public abstract void enter(PlayerBase player);
    public abstract PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player);
}

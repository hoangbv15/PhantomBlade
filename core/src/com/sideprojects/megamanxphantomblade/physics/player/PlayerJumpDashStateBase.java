package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerJumpDashStateBase {
    public abstract boolean isJumpDashing();

    public abstract PlayerJumpDashStateBase nextState(InputProcessor input, PlayerBase player);
}

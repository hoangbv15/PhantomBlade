package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

/**
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerPhysicsFactory {
    protected InputProcessor input;
    protected PlayerSound stateChangeHandler;

    public PlayerPhysicsFactory(InputProcessor input, PlayerSound stateChangeHandler) {
        this.input = input;
        this.stateChangeHandler = stateChangeHandler;
    }

    public abstract PlayerPhysics create(PlayerBase player);
}

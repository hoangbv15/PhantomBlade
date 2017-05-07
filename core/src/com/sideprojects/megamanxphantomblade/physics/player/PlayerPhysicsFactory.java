package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerPhysicsFactory {
    protected InputProcessor input;

    public PlayerPhysicsFactory(InputProcessor input) {
        this.input = input;
    }

    public abstract PlayerPhysics create(PlayerBase player);
}

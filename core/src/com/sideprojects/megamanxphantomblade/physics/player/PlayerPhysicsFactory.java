package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class PlayerPhysicsFactory {
    private InputProcessor input;
    private PlayerStateChangeHandler stateChangeHandler;

    public PlayerPhysicsFactory(InputProcessor input, PlayerStateChangeHandler stateChangeHandler) {
        this.input = input;
        this.stateChangeHandler = stateChangeHandler;
    }

    public PlayerPhysics create(PlayerBase player) {
        return new PlayerPhysics(input, player, stateChangeHandler);
    }
}

package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXSound;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysicsFactory extends PlayerPhysicsFactory {
    private PlayerXSound stateChangeHandler;
    public PlayerXPhysicsFactory(InputProcessor input, PlayerXSound stateChangeHandler) {
        super(input);
        this.stateChangeHandler = stateChangeHandler;
    }

    @Override
    public PlayerPhysics create(PlayerBase player) {
        return new PlayerXPhysics(input, player, stateChangeHandler);
    }
}

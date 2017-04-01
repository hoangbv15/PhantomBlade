package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysicsFactory extends PlayerPhysicsFactory {
    public PlayerXPhysicsFactory(InputProcessor input, PlayerSound stateChangeHandler) {
        super(input, stateChangeHandler);
    }

    @Override
    public PlayerPhysics create(PlayerBase player) {
        return new PlayerXPhysics(input, player, stateChangeHandler);
    }
}

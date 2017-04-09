package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 20/03/17.
 */
public class Invincible extends PlayerDamageState {
    private float invincibleTime = 1f;
    private float stateTime;
    public Invincible(PlayerBase player, PlayerPhysics physics) {
        super(player);
        physics.setStateToIdle();
    }

    @Override
    public boolean canControl() {
        return true;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, Damage damage, PlayerPhysics physics, float delta) {
        if (stateTime >= invincibleTime) {
            return new NotDamaged(player);
        }
        stateTime += delta;
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        stateTime = 0;
        player.invincible = true;
    }
}

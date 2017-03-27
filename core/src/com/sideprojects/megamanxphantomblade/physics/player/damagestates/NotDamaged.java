package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 20/03/17.
 */
public class NotDamaged extends PlayerDamageState {
    public NotDamaged(PlayerBase player) {
        super(player);
    }

    @Override
    public boolean canControl() {
        return true;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, Damage damage, PlayerPhysics physics, float delta) {
        if (damage != null) {
            return new Damaged(player, damage, physics);
        }
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        player.invincible = false;
    }
}

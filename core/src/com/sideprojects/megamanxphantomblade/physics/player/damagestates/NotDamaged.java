package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
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
    public PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, PlayerMovementStateBase currentMovementState, float delta) {
        if (damage != null) {
            return new Damaged(player, damage);
        }
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        // Do nothing here
    }
}

package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 20/03/17.
 */
public abstract class PlayerDamageState {
    public PlayerDamageState(PlayerBase player) {
        enter(player);
    }

    public abstract boolean canControl();
    public abstract PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, float delta);
    public abstract void enter(PlayerBase player);
}

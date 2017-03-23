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
    public abstract boolean isPushedBack();
    public abstract PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, PlayerPhysics physics, float delta);
    public abstract void enter(PlayerBase player);
}

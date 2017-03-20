package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 19/03/17.
 */
public class Damaged extends PlayerDamageState {
    private float stunTime = 0.5f;
    private EnemyDamage damage;

    public Damaged(PlayerBase player, EnemyDamage damage) {
        super(player);
        this.damage = damage;
        // Reduce player's health here
        System.out.println("damaged " + damage.getDamage());
    }

    @Override
    public boolean canControl() {
        return false;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, float delta) {
        if (player.stateTime >= stunTime) {
            return new Invincible(player);
        }
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        player.stateTime = 0;
    }
}

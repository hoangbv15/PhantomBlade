package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 19/03/17.
 */
public class Damaged extends PlayerDamageState {
    private float stunTime;
    private EnemyDamage damage;

    public Damaged(PlayerBase player, EnemyDamage damage) {
        super(player);
        stunTime = player.animations.get(PlayerAnimation.Type.DamagedNormal).getAnimationDuration();
        this.damage = damage;
        // Reduce player's health here
        System.out.println("damaged " + damage.getDamage());
        switch (damage.type) {
            case Heavy:
            case Normal:
            case Light:
            case InstantDeath:
                player.state = PlayerState.DAMAGEDNORMAL;
                break;
        }
    }

    @Override
    public boolean canControl() {
        return false;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, PlayerMovementStateBase currentMovementState, float delta) {
        if (player.stateTime >= stunTime) {
            player.state = currentMovementState.enter(player);
            return new Invincible(player);
        }
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        player.stateTime = 0;
    }
}

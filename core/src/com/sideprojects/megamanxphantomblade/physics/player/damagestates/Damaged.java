package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerDamageState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 19/03/17.
 */
public class Damaged extends PlayerDamageState {
    private float stunTime;

    public Damaged(PlayerBase player, EnemyDamage damage, PlayerPhysics physics) {
        super(player);
        stunTime = player.animations.get(PlayerAnimation.Type.DamagedNormal).getAnimationDuration();
        // Reduce player's health here
        switch (damage.type) {
            case Heavy:
            case Normal:
            case Light:
            case InstantDeath:
                physics.stateChangeHandler.callback(player.state, PlayerState.DAMAGEDNORMAL);
                player.state = PlayerState.DAMAGEDNORMAL;
                if (damage.side == EnemyDamage.Side.Left) {
                    player.direction = MovingObject.LEFT;
                    physics.pushBack(MovingObject.RIGHT);
                } else {
                    player.direction = MovingObject.RIGHT;
                    physics.pushBack(MovingObject.LEFT);
                }
                break;
        }
    }

    @Override
    public boolean canControl() {
        return false;
    }

    @Override
    public boolean isPushedBack() {
        return false;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, EnemyDamage damage, PlayerPhysics physics, float delta) {
        if (player.stateTime >= stunTime) {
            return new Invincible(player, physics);
        }
        return this;
    }

    @Override
    public void enter(PlayerBase player) {
        player.stateTime = 0;
    }
}

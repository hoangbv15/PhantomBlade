package com.sideprojects.megamanxphantomblade.physics.player.damagestates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.Damage;
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

    public Damaged(PlayerBase player, Damage damage, PlayerPhysics physics) {
        super(player);
        stunTime = player.animations.get(PlayerAnimation.Type.DamagedNormal).getAnimationDuration();
        // Reduce player's health here
        switch (damage.type) {
            case Heavy:
            case Normal:
            case Light:
            case InstantDeath:
                player.takeDamage(damage);
                player.isAttacking = false;
                player.firstFramesOfAttacking = false;
                if (player.isDead()) {
                    physics.soundPlayer.callback(player.state, PlayerState.DEAD);
                    player.state = PlayerState.DEAD;
                } else {
                    physics.soundPlayer.callback(player.state, PlayerState.DAMAGEDNORMAL);
                    player.state = PlayerState.DAMAGEDNORMAL;
                    if (player.canIssueLowHealthWarning && player.isLowHealth()) {
                        physics.soundPlayer.lowHealthWarning();
                    }
                    if (damage.side == Damage.Side.Left) {
                        player.direction = MovingObject.LEFT;
                        physics.pushBack(MovingObject.RIGHT);
                    } else {
                        player.direction = MovingObject.RIGHT;
                        physics.pushBack(MovingObject.LEFT);
                    }
                }
                break;
        }
    }

    @Override
    public boolean canControl() {
        return false;
    }

    @Override
    public PlayerDamageState nextState(PlayerBase player, Damage damage, PlayerPhysics physics, float delta) {
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

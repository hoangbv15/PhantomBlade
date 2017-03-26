package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysics extends PlayerPhysics {
    private static float attackTime = 0.32f;
    private static float justBegunAttackTime = 0.1f;
    private static float attackRecoveryTime = 0.1f;
    private float attackStateTime;

    public PlayerXPhysics(InputProcessor input, PlayerBase player, PlayerSound soundPlayer) {
        super(input, player, soundPlayer);
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {
        // Logic for attacking
        attackStateTime += delta;
        if (input.isCommandJustPressed(Command.ATTACK) && attackStateTime >= attackRecoveryTime) {
            if (player.state == PlayerState.IDLE) {
                player.stateTime = 0;
            }
            player.isAttacking = true;
            player.justBegunAttacking = true;
            attackStateTime = 0;
            soundPlayer.playAttackLight();
        } else {
            if (player.isAttacking && attackStateTime < attackTime) {
                if (attackStateTime >= justBegunAttackTime) {
                    player.justBegunAttacking = false;
                }
            } else {
                player.isAttacking = false;
            }
        }
        super.internalUpdate(object, delta, map);
    }
}

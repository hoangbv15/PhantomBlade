package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;
import com.sideprojects.megamanxphantomblade.player.x.XBuster;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysics extends PlayerPhysics {
    private static float attackFrameTime = 0.04f;
    private static float attackTime = 8 * attackFrameTime;
    private static float firstFramesAttackTime = 4 * attackFrameTime;
    private static float attackRecoveryTime = 0.1f;
    private float attackStateTime;
    private PlayerState prevState;

    public PlayerXPhysics(InputProcessor input, PlayerBase player, PlayerSound soundPlayer) {
        super(input, player, soundPlayer);
        prevState = null;
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {
        // Logic for attacking
        attackStateTime += delta;
        if (input.isCommandJustPressed(Command.ATTACK) && attackStateTime >= attackRecoveryTime) {
            resetAttackStatus();
            lightAttack(map);
        } else {
            player.justBegunAttacking = false;
            if (player.isAttacking && attackStateTime < attackTime) {
                if (attackStateTime >= firstFramesAttackTime) {
                    player.firstFramesOfAttacking = false;
                }
                // if player state changes during attack
                // set state time to after the first attack frames
                // in order to play the ending animation
                if (prevState != player.state) {
                    player.changeDirectionDuringAttack = true;
                }
                prevState = player.state;
            } else {
                resetAttackStatus();
            }
        }
        super.internalUpdate(object, delta, map);
    }

    private void lightAttack(MapBase map) {
        // Does light damage attack
        if (player.state == PlayerState.IDLE) {
            player.stateTime = 0;
        }
        player.isAttacking = true;
        player.firstFramesOfAttacking = true;
        player.justBegunAttacking = true;
        player.attackType = Damage.Type.Light;
        attackStateTime = 0;
        playerSound.playAttackLight();
        createBullet(map);
    }

    private void createBullet(MapBase map) {
        int bulletDirection = player.direction;
        if (player.state == PlayerState.WALLSLIDE) {
            bulletDirection = player.direction * -1;
        }
        Damage.Side side = bulletDirection == MovingObject.LEFT ? Damage.Side.Right : Damage.Side.Left;
        Damage damage = new Damage(player.attackType, side);
        map.addPlayerAttack(new XBuster(player, damage, bulletDirection, player.animations));
    }

    private void resetAttackStatus() {
        player.isAttacking = false;
        player.changeDirectionDuringAttack = false;
    }
}

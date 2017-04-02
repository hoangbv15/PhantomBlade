package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXSound;
import com.sideprojects.megamanxphantomblade.player.x.XBuster;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysics extends PlayerPhysics {
    // Timing for attack animation
    private static float attackFrameTime = 0.04f;
    private static float attackTime = 8 * attackFrameTime;
    private static float firstFramesAttackTime = 4 * attackFrameTime;
    private static float attackRecoveryTime = 0.1f;

    // Timing for charging
    private static float waitBeforeCharging = 0.2f;
    private static float timeToFullyCharged = 2;
    private boolean isCharging;

    private float attackStateTime;
    private PlayerState prevState;

    private PlayerXSound playerXSound;

    public PlayerXPhysics(InputProcessor input, PlayerBase player, PlayerXSound playerSound) {
        super(input, player, playerSound);
        playerXSound = playerSound;
        prevState = null;
        isCharging = false;
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
                    player.changeStateDuringAttack = true;
                }
                prevState = player.state;
            } else {
                resetAttackStatus();
            }

            // if player keep holding, charge
            if (input.isCommandPressed(Command.ATTACK) && attackStateTime >= waitBeforeCharging) {
                playerXSound.startPlayingCharge();
                isCharging = true;
            } else {
                playerXSound.stopPlayingCharge();
                if (isCharging) {
                    if (attackStateTime < timeToFullyCharged) {
                        mediumAttack(map);
                    } else {
                        heavyAttack(map);
                    }
                }
                isCharging = false;
            }
        }
        super.internalUpdate(object, delta, map);
    }

    private void lightAttack(MapBase map) {
        // Does light damage attack
        player.attackType = Damage.Type.Light;
        playerSound.playAttackLight();
        executeAttack(map);
    }

    private void mediumAttack(MapBase map) {
        // Does medium damage attack
        player.attackType = Damage.Type.Normal;
        playerSound.playAttackMedium();
        executeAttack(map);
    }

    private void heavyAttack(MapBase map) {
        // Does medium damage attack
        player.attackType = Damage.Type.Heavy;
        playerSound.playAttackHeavy();
        executeAttack(map);
    }

    private void executeAttack(MapBase map) {
        if (player.state == PlayerState.IDLE) {
            player.stateTime = 0;
        }
        player.isAttacking = true;
        player.firstFramesOfAttacking = true;
        player.justBegunAttacking = true;
        attackStateTime = 0;
        createBullet(map);
    }

    private void createBullet(MapBase map) {
        int bulletDirection = player.direction;
        if (player.state == PlayerState.WALLSLIDE) {
            bulletDirection = player.direction * -1;
        }
        Damage.Side side = bulletDirection == MovingObject.LEFT ? Damage.Side.Right : Damage.Side.Left;
        Damage damage = new Damage(player.attackType, side);
        map.addPlayerAttack(new XBuster(player, damage, bulletDirection, player.animations, playerSound));
    }

    private void resetAttackStatus() {
        player.isAttacking = false;
        player.changeStateDuringAttack = false;
    }
}

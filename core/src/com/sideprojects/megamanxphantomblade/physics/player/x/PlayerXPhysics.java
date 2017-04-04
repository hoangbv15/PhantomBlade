package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimationBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXSound;
import com.sideprojects.megamanxphantomblade.player.x.XBuster;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysics extends PlayerPhysics {
    // Timing for attack animation
    private float attackFrameTime = 0.04f;
    private float attackTime = 8 * attackFrameTime;
    private float firstFramesAttackTime = 4 * attackFrameTime;
    private static float attackRecoveryTime = 0.1f;

    // Timing for charging
    private static float waitBeforeCharging = 0.2f;
    private static float timeToFullyCharged = 1.9f;
    private static float timeToAlmostFullyCharged = 1.5f;

    private float attackStateTime;
    private PlayerState prevState;

    private PlayerXSound playerXSound;

    public PlayerXPhysics(InputProcessor input, PlayerBase player, PlayerXSound playerSound) {
        super(input, player, playerSound);
        playerXSound = playerSound;
        prevState = player.state;
        player.isCharging = false;
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {
        // Logic for attacking
        attackStateTime += delta;
        if (input.isCommandJustPressed(Command.ATTACK) && attackStateTime >= attackRecoveryTime) {
            resetAttackStatus();
            lightAttack(map);
//            heavyAttack(map);
        } else {
            player.justBegunAttacking = false;
            if (player.isAttacking && attackStateTime < attackTime) {
                if (attackStateTime >= firstFramesAttackTime) {
                    player.firstFramesOfAttacking = false;
                }
                // if player state changes during attack
                // set state time to after the first attack frames
                // in order to play the ending animation
                if (player.state == PlayerState.IDLE && prevState != player.state) {
                    player.changeStateDuringAttack = true;
                }
            } else {
                resetAttackStatus();
            }

            // if player keep holding, charge
            if (input.isCommandPressed(Command.ATTACK) && attackStateTime >= waitBeforeCharging) {
                player.isCharging = true;
                playerXSound.startPlayingCharge(delta);
                if (attackStateTime >= timeToAlmostFullyCharged) {
                    player.almostFullyCharged = true;
                }
                if (attackStateTime >= timeToFullyCharged) {
                    player.fullyCharged = true;
                    player.almostFullyCharged = false;
                }
            } else {
                playerXSound.stopPlayingCharge();
                if (player.isCharging) {
                    if (attackStateTime < timeToFullyCharged) {
                        mediumAttack(map);
                    } else {
                        heavyAttack(map);
                    }
                }
                player.isCharging = false;
                player.fullyCharged = false;
                player.almostFullyCharged = false;
            }
        }
        prevState = player.state;
        player.attackStateTime = attackStateTime;
        super.internalUpdate(object, delta, map);
    }

    private void initialiseAttackTimings() {
        attackFrameTime = player.animations.getAttackFrameDuration(PlayerAnimationBase.Type.Idle, player.attackType);
        attackTime = player.animations.getAttackDuration(PlayerAnimationBase.Type.Idle, player.attackType, player.changeStateDuringAttack);
        firstFramesAttackTime = 4 * attackFrameTime;
    }

    private void lightAttack(MapBase map) {
        // Does light damage attack
        player.attackType = Damage.Type.Light;
        playerSound.playAttackLight();
        initialiseAttackTimings();
        executeAttack(map);
    }

    private void mediumAttack(MapBase map) {
        // Does medium damage attack
        player.attackType = Damage.Type.Normal;
        playerSound.playAttackMedium();
        initialiseAttackTimings();
        executeAttack(map);
    }

    private void heavyAttack(MapBase map) {
        // Does medium damage attack
        player.attackType = Damage.Type.Heavy;
        playerSound.playAttackHeavy();
        initialiseAttackTimings();
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

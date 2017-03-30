package com.sideprojects.megamanxphantomblade.physics.player.x;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.VectorPool;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;
import com.sideprojects.megamanxphantomblade.player.x.Bullet;

/**
 * Created by buivuhoang on 26/03/17.
 */
public class PlayerXPhysics extends PlayerPhysics {
    private static float attackTime = 0.32f;
    private static float firstFramesAttackTime = 0.1f;
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
            lightAttack(map);
        } else {
            player.justBegunAttacking = false;
            if (player.isAttacking && attackStateTime < attackTime) {
                if (attackStateTime >= firstFramesAttackTime) {
                    player.firstFramesOfAttacking = false;
                }
            } else {
                player.isAttacking = false;
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
        soundPlayer.playAttackLight();
        createBullet(map);
    }

    private void createBullet(MapBase map) {
        int bulletDirection = player.direction;
        if (player.state == PlayerState.WALLSLIDE) {
            bulletDirection = player.direction * -1;
        }
        Damage.Side side = bulletDirection == MovingObject.LEFT ? Damage.Side.Right : Damage.Side.Left;
        Damage damage = new Damage(player.attackType, side);
        Vector2 pos = new Vector2(player.bounds.x, player.bounds.y);
        Vector2 padding = getBulletPositionPadding(bulletDirection);
        pos.x += padding.x;
        pos.y += padding.y;
        map.addPlayerAttack(new Bullet(pos, damage, bulletDirection, player.animations));
    }

    private Vector2 getBulletPositionPadding(int bulletDirection) {
        Vector2 padding = VectorPool.get(0, + 0.46f);
        if (bulletDirection == MovingObject.RIGHT) {
            padding = VectorPool.get(player.bounds.width, padding.y);
        } else {
            padding = VectorPool.get(-0.3f, padding.y);
        }

        switch(player.state) {
            case JUMP:
                padding = VectorPool.get(padding.x, 0.55f);
                break;
            case FALL:
                padding = VectorPool.get(padding.x, 0.55f);
                break;
            case DASH:
                padding = VectorPool.get(padding.x, 0.2f);
                break;
        }

        return padding;
    }
}

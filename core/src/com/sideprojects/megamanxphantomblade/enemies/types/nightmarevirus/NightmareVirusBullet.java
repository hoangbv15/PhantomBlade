package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirusBullet extends EnemyAttack {
    public NightmareVirusBullet(Rectangle enemy, Rectangle target, float speed, Damage damage, int direction, EnemyAnimationBase animations) {
        super(enemy, target, speed, damage, direction);
        createAnimations(animations);
        initialiseHealthPoints(1);
        Vector2 posPadding = getAnimationPadding();
        mapCollisionBounds.x += posPadding.x;
        mapCollisionBounds.y += posPadding.y;
        mapCollisionBounds.setSize(0.2f, 0.2f);
        updatePos();
    }

    private void createAnimations(EnemyAnimationBase animations) {
        animation = animations.retrieveFromCache(
                EnemyAnimationBase.Type.Attack,
                direction,
                Sprites.NightmareVirus,
                Arrays.asList(11, 12, 13, 14, 15),
                0.05f);
    }

    private Vector2 getAnimationPadding() {
        if (direction == LEFT) {
            return VectorCache.get(0.25f, 0.5f);
        } else {
            return VectorCache.get(0.7f, 0.5f);
        }
    }

    @Override
    protected boolean isAnimationLooping() {
        return true;
    }

    @Override
    public boolean canCollideWithWall() {
        return false;
    }
}

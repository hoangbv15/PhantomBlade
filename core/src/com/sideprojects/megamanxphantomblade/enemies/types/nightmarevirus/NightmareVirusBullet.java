package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.enemies.EnemySound;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirusBullet extends EnemyAttack {
    private EnemySound sounds;

    public NightmareVirusBullet(Rectangle enemy, Rectangle target, float speed, Damage damage, int direction,
                                EnemyAnimationBase animations, EnemySound sounds) {
        super(enemy, target, speed, damage, direction);
        this.sounds = sounds;
        createAnimations(animations);
        initialiseHealthPoints(1);
    }

    private void createAnimations(EnemyAnimationBase animations) {
        animation = animations.retrieveFromCache(
                EnemyAnimationBase.Type.ATTACK,
                direction,
                Sprites.NIGHTMARE_VIRUS,
                Arrays.asList(11, 12, 13, 14, 15),
                0.05f);
    }

    @Override
    protected Vector2 getPosPadding() {
        if (direction == LEFT) {
            return VectorCache.get(0.25f, 0.5f);
        } else {
            return VectorCache.get(0.7f, 0.5f);
        }
    }

    @Override
    public void onInitialise() {
        super.onInitialise();
        sounds.playAttack();
        setDealDamageBoundsSize(0.2f, 0.2f);
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

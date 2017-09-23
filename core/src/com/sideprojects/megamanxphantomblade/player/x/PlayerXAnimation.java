package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;
import java.util.List;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class PlayerXAnimation extends PlayerAnimation {

    @Override
    public Animation<TextureRegion> getAttack(Type type, int direction, Damage.Type attackType, boolean isFirstAttackFrame, boolean changeStateDuringAttack) {
        String texture = getAttackTextureAtlas(type, attackType, isFirstAttackFrame, changeStateDuringAttack);
        if (texture == null) return null;
        return retrieveFromCache(type, direction, texture, getAttackAnimationIndex(type, attackType, changeStateDuringAttack), getAttackFrameDuration(type, attackType));
    }

    private String getAttackTextureAtlas(Type type, Damage.Type attackType, boolean withLight, boolean changeStateDuringAttack) {
        switch(type) {
            case Idle:
                if (attackType == Damage.Type.HEAVY && !changeStateDuringAttack) {
                    return Sprites.xIdleShootCharged;
                }
                return Sprites.xIdleShoot;
            case Run:
                return withLight? Sprites.xRunShootLight : Sprites.xRunShootNoLight;
            case Jump:
            case Fall:
            case Touchdown:
            case Updash:
                return withLight? Sprites.xJumpShootLight : Sprites.xJumpShootNoLight;
            case Dash:
            case Dashbreak:
                return withLight? Sprites.xDashShootLight : Sprites.xDashShootNoLight;
            case Wallslide:
            case Walljump:
                return withLight? Sprites.xWallslideShootLight : Sprites.xWallslideShootNoLight;
            default:
                return null;

        }
    }

    private List<Integer> getAttackAnimationIndex(Type type, Damage.Type attackType, boolean changeStateDuringAttack) {
        switch(type) {
            case Idle:
                if (changeStateDuringAttack) {
                    return Arrays.asList(5, 5, 5, 5, 5, 5, 6, 7);
                }
                if (attackType == Damage.Type.HEAVY) {
                    return Arrays.asList(0, 1, 2, 3, 4, 5, 6, 6, 7, 7, 8, 8);
                }
                return Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
            default:
                return getAnimationIndex(type, false);
        }
    }

    @Override
    public float getAttackFrameDuration(Type type, Damage.Type attackType) {
        switch(type) {
            case Idle:
                return 0.04f;
            default:
                return getFrameDuration(type, false);
        }
    }

    @Override
    public float getAttackDuration(Type type, Damage.Type attackType, boolean changeStateDuringAttack) {
        return getAttackFrameDuration(type, attackType) * getAttackAnimationIndex(type, attackType, changeStateDuringAttack).size();
    }

    @Override
    protected List<Integer> getAnimationIndex(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                if (lowHealth) {
                    return Arrays.asList(1, 2, 1, 0, 1, 2, 1, 0, 4, 5, 4, 3, 4, 5, 4, 3);
                }
                return Arrays.asList(1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 3, 4, 3);
            case Walljump:
                return Arrays.asList(4, 5, 6);
            case Run:
                return null;
            case Jump:
                return Arrays.asList(0, 1, 2, 3);
            case Fall:
                return Arrays.asList(3, 4, 5, 6, 7);
            case Touchdown:
                return Arrays.asList(8, 9, 10);
            case Wallslide:
                return Arrays.asList(0, 1, 2, 3);
            case Dashrocket:
                return null;
            case Dash:
                return Arrays.asList(0, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3);
            case Dashbreak:
                return Arrays.asList(4, 5, 6, 7);
            case Updash:
            case Updashrocket:
            case DamagedNormal:
                return null;
            default:
                return null;
        }
    }

    @Override
    protected String getTextureAtlas(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                return lowHealth ? Sprites.xIdleLowHealth : Sprites.xIdle;
            case Walljump:
                return Sprites.xWallSlide;
            case Run:
                return Sprites.xRun;
            case Jump:
            case Fall:
            case Touchdown:
                return Sprites.xJump;
            case Wallslide:
                return Sprites.xWallSlide;
            case Dashrocket:
                return Sprites.xDashRocket;
            case Dash:
            case Dashbreak:
                return Sprites.xDash;
            case Updash:
                return Sprites.xUpDash;
            case Updashrocket:
                return Sprites.xUpDashRocket;
            case DamagedNormal:
                return Sprites.xDamagedNormal;
            default:
                return super.getTextureAtlas(type, lowHealth);
        }
    }

    @Override
    protected boolean isLooping(Type type, boolean isAttacking) {
        switch (type) {
            case Idle:
                return !isAttacking;
            case Run:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected Vector2 getAnimationPaddingX(Type type, int direction, boolean isAttacking, Damage.Type attackType, boolean changeStateDuringAttack) {
        if (isAttacking) {
            switch (type) {
                case Idle:
                    if (attackType == Damage.Type.HEAVY && !changeStateDuringAttack) {
                        return VectorCache.get(-18, -7);
                    }
                    return VectorCache.get(-15, 0);
                case Run:
                case Jump:
                case Walljump:
                case Fall:
                case Touchdown:
                case Dash:
                case Updash:
                    return VectorCache.get(-8, 0);
                case Dashbreak:
                    return VectorCache.get(-19, 0);
            }
        }

        switch (type) {
            case DamagedNormal:
                return VectorCache.get(-3, -5);

        }

        return VectorCache.get(0, 0);
    }
}

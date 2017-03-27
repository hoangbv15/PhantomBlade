package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class PlayerXAnimation extends PlayerAnimation {

    public Animation<TextureRegion> getBullet(Damage damage, int direction) {
        switch(damage.type) {
            case Light:
                return retrieveFromCache(Type.BulletSmall, direction, Sprites.XBulletSmall, null, 0.04f);
        }
        return null;
    }

    @Override
    public Animation<TextureRegion> getAttack(Type type, int direction, boolean isFirstAttackFrame) {
        String texture = getAttackTextureAtlas(type, isFirstAttackFrame);
        if (texture == null) return null;
        return retrieveFromCache(type, direction, texture, getAttackAnimationIndex(type), getAttackFrameDuration(type));
    }

    private String getAttackTextureAtlas(Type type, boolean withLight) {
        switch(type) {
            case Idle:
                return Sprites.XIdleShoot;
            case Run:
                return withLight? Sprites.XRunShootLight : Sprites.XRunShootNoLight;
            case Jump:
            case Fall:
            case Touchdown:
            case Updash:
                return withLight? Sprites.XJumpShootLight : Sprites.XJumpShootNoLight;
            case Dash:
                return withLight? Sprites.XDashShootLight : Sprites.XDashShootNoLight;
            case Wallslide:
            case Walljump:
                return withLight? Sprites.XWallslideShootLight: Sprites.XWallslideShootNoLight;
            default:
                return null;

        }
    }

    private int[] getAttackAnimationIndex(Type type) {
        switch(type) {
            case Idle:
                return null;
            default:
                return getAnimationIndex(type, false);
        }
    }

    private float getAttackFrameDuration(Type type) {
        switch(type) {
            case Idle:
                return 0.04f;
            default:
                return getFrameDuration(type, false);
        }
    }

    @Override
    protected String getTextureAtlas(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                return lowHealth ? Sprites.XIdleLowHealth : Sprites.XIdle;
            case Walljump:
                return Sprites.XWallSlide;
            case Run:
                return Sprites.XRun;
            case Jump:
            case Fall:
            case Touchdown:
                return Sprites.XJump;
            case Wallslide:
                return Sprites.XWallSlide;
            case Dashrocket:
                return Sprites.XDashRocket;
            case Dash:
            case Dashbreak:
                return Sprites.XDash;
            case Updash:
                return Sprites.XUpDash;
            case Updashrocket:
                return Sprites.XUpDashRocket;
            case DamagedNormal:
                return Sprites.XDamagedNormal;
            default:
                return null;
        }
    }

    @Override
    protected int[] getAnimationIndex(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                if (lowHealth) {
                    return new int[] {1, 2, 1, 0, 1, 2, 1, 0, 4, 5, 4, 3, 4, 5, 4, 3};
                }
                return new int[] {1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 3, 4, 3};
            case Walljump:
                return new int[] {4, 5, 6};
            case Run:
                return null;
            case Jump:
                return new int[] {0, 1, 2, 3};
            case Fall:
                return new int[] {3, 4, 5, 6, 7};
            case Touchdown:
                return new int[] {8, 9, 10};
            case Wallslide:
                return new int[] {0, 1, 2, 3};
            case Dashrocket:
                return null;
            case Dash:
                return new int[] {0, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3};
            case Dashbreak:
                return new int[] {4, 5, 6, 7};
            case Updash:
            case Updashrocket:
            case DamagedNormal:
                return null;
            default:
                return null;
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
    protected Vector2 getAnimationPaddingX(Type type, int direction, boolean isAttacking) {
        if (isAttacking) {
            switch (type) {
                case Idle:
                    return new Vector2(-15, 0);
                case Run:
                case Jump:
                case Walljump:
                case Fall:
                case Touchdown:
                case Dash:
                case Dashbreak:
                case Updash:
                    return new Vector2(-8, 0);
            }
        }

        switch (type) {
            case DamagedNormal:
                return new Vector2(0, -5);
        }

        return new Vector2(0, 0);
    }
}

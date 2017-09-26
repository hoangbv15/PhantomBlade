package com.sideprojects.megamanxphantomblade.player;

import com.sideprojects.megamanxphantomblade.animation.Sprites;

/**
 * Created by buivuhoang on 04/04/17.
 */
public abstract class PlayerAnimation extends PlayerAnimationBase {
    @Override
    protected String getTextureAtlas(Type type, boolean lowHealth) {
        switch (type) {
            case BulletNoDamageExplode:
                return Sprites.BULLET_NO_DAMAGE_EXPLODE;
            default:
                return null;
        }
    }

    @Override
    protected float getFrameDuration(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                if (lowHealth) {
                    return 0.22f;
                }
            case Jump:
            case Walljump:
                return 0.1f;
            case BulletNoDamageExplode:
            case Run:
                return 0.04f;
            case Fall:
            case Touchdown:
            case Wallslide:
            case Dash:
            case Dashrocket:
            case Updash:
            case Updashrocket:
                return 0.05f;
            case Dashbreak:
                return 0.08f;
            case DamagedNormal:
                return 0.06f;
            default:
                return 0.1f;
        }
    }
}

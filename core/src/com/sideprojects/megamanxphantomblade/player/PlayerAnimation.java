package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buivuhoang on 05/02/17.
 */
public abstract class PlayerAnimation {
    protected class AnimationKey {
        private Type type;
        private int direction;
        private String texture;

        public AnimationKey(Type type, int direction, String texture) {
            this.type = type;
            this.direction = direction;
            this.texture = texture;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnimationKey that = (AnimationKey) o;

            if (direction != that.direction) return false;
            if (type != that.type) return false;
            return texture != null ? texture.equals(that.texture) : that.texture == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + direction;
            result = 31 * result + (texture != null ? texture.hashCode() : 0);
            return result;
        }
    }

    private Map<AnimationKey, Animation<TextureRegion>> animationCache;

    public PlayerAnimation() {
        animationCache = new HashMap<AnimationKey, Animation<TextureRegion>>();
    }

    public Animation<TextureRegion> get(Type type, int direction, boolean lowHealth, boolean isAttacking, boolean isFirstAttackFrame) {
        if (isAttacking) {
            Animation<TextureRegion> attack = getAttack(type, direction, isFirstAttackFrame);
            if (attack != null) {
                return attack;
            }
        }
        String texture = getTextureAtlas(type, lowHealth);
        int[] index = getAnimationIndex(type, lowHealth);
        float frameDuration = getFrameDuration(type, lowHealth);

        return retrieveFromCache(type, direction, texture, index, frameDuration);
    }

    public Animation<TextureRegion> get(Type type) {
        return get(type, MovingObject.RIGHT, false, false, false);
    }

    protected Animation<TextureRegion> retrieveFromCache(Type type, int direction, String texture, int[] animationIndex, float frameDuration) {
        AnimationKey key = new AnimationKey(type, direction, texture);
        if (!animationCache.containsKey(key)) {
            boolean flipped = direction == MovingObject.LEFT;
            animationCache.put(key,
                    AnimationLoader.load(texture, animationIndex, flipped, frameDuration));
        }

        return animationCache.get(key);
    }

    public abstract Animation<TextureRegion> getAttack(Type type, int direction, boolean isFirstAttackFrame);
    protected abstract String getTextureAtlas(Type type, boolean lowHealth);
    protected abstract int[] getAnimationIndex(Type type, boolean lowHealth);
    protected abstract boolean isLooping(Type type, boolean isAttacking);

    /**
     * Padding to give the sprite for LEFT direction.
     * RIGHT direction will be automatically mirrored.
     */
    protected abstract Vector2 getAnimationPaddingX(Type type, int direction, boolean isAttacking);

    protected float getFrameDuration(Type type, boolean lowHealth) {
        switch (type) {
            case Idle:
                if (lowHealth) {
                    return 0.22f;
                }
            case Jump:
            case Walljump:
                return 0.1f;
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

    public enum Type {
        Idle, Run, Jump, Fall, Touchdown, Wallslide, Walljump, Dash, Dashbreak, Dashrocket, Updash, Updashrocket, DamagedNormal
    }
}

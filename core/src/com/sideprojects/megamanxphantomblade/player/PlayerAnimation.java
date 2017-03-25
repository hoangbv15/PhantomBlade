package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buivuhoang on 05/02/17.
 */
public abstract class PlayerAnimation {
    protected class AnimationKey {
        protected Type type;
        protected int direction;

        public AnimationKey(Type type, int direction) {
            this.type = type;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnimationKey that = (AnimationKey) o;

            if (direction != that.direction) return false;
            return type != null ? type.equals(that.type) : that.type == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + direction;
            return result;
        }
    }

    private Map<AnimationKey, Animation<TextureRegion>> animationCache;

    public PlayerAnimation() {
        animationCache = new HashMap<AnimationKey, Animation<TextureRegion>>();
    }

    public Animation<TextureRegion> get(Type type, int direction) {
        AnimationKey key = new AnimationKey(type, direction);
        if (!animationCache.containsKey(key)) {
            boolean flipped = direction == MovingObject.LEFT;
            animationCache.put(key,
                    AnimationLoader.load(getTextureAtlas(type), getAnimationIndex(type), flipped, getFrameDuration(type)));
        }

        return animationCache.get(key);
    }

    public Animation<TextureRegion> get(Type type) {
        return get(type, MovingObject.RIGHT);
    }

    protected abstract String getTextureAtlas(Type type);
    protected abstract int[] getAnimationIndex(Type type);
    protected float getFrameDuration(Type type) {
        switch (type) {
            case Idle:
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

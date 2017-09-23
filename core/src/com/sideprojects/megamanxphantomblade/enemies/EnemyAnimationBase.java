package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.AnimationCache;
import com.sideprojects.megamanxphantomblade.animation.Sprites;

import java.util.List;

/**
 * Loads player animations depending on the player state and direction
 * Created by buivuhoang on 05/02/17.
 */
public abstract class EnemyAnimationBase {
    protected class AnimationKey {
        private Type type;
        private int direction;
        private String texture;
        private List<Integer> animationIndex;

        public AnimationKey(Type type, int direction, String texture, List<Integer> animationIndex) {
            this.type = type;
            this.direction = direction;
            this.texture = texture;
            this.animationIndex = animationIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnimationKey that = (AnimationKey) o;

            if (direction != that.direction) return false;
            if (type != that.type) return false;
            if (texture != null ? !texture.equals(that.texture) : that.texture != null) return false;
            return animationIndex != null ? animationIndex.equals(that.animationIndex) : that.animationIndex == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + direction;
            result = 31 * result + (texture != null ? texture.hashCode() : 0);
            result = 31 * result + (animationIndex != null ? animationIndex.hashCode() : 0);
            return result;
        }
    }

    private AnimationCache<AnimationKey> animationCache;

    public EnemyAnimationBase() {
        animationCache = new AnimationCache<>();
    }

    public Animation<TextureRegion> get(Type type, int direction) {
        String texture = getTextureAtlas(type);
        List<Integer> index = getAnimationIndex(type);
        float frameDuration = getFrameDuration(type);

        return retrieveFromCache(type, direction, texture, index, frameDuration);
    }

    public Animation<TextureRegion> get(Type type) {
        return get(type, MovingObject.RIGHT);
    }

    public Animation<TextureRegion> retrieveFromCache(Type type, int direction, String texture, List<Integer> animationIndex, float frameDuration) {
        AnimationKey key = new AnimationKey(type, direction, texture, animationIndex);
        boolean flipped = direction == MovingObject.RIGHT;
        return animationCache.retrieveFromCache(key, flipped, texture, animationIndex, frameDuration);
    }

    protected abstract List<Integer> getAnimationIndex(Type type);
    public abstract boolean isLooping(Type type);

    /**
     * Padding to give the sprite for LEFT direction.
     * RIGHT direction will be automatically mirrored.
     */
    public abstract Vector2 getAnimationPaddingX(Type type, int direction);

    protected String getTextureAtlas(Type type) {
        switch (type) {
            case DIE:
                return Sprites.ENEMY_EXPLODE;
            default:
                return null;
        }
    }

    protected float getFrameDuration(Type type) {
        switch (type) {
            case DIE:
                return 0.04f;
            default:
                return 0.1f;
        }
    }

    public enum Type {
        // Common
        IDLE,
        STOP_IDLING,
        RUN,
        JUMP,
        FALL,
        PREPARE_ATTACK,
        ATTACK,
        FINISH_ATTACK,
        DAMAGED,
        DIE,
        EXPLODE_FRAGMENT
    }
}

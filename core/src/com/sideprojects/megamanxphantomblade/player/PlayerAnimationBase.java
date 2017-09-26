package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.AnimationCache;

import java.util.List;

/**
 * Loads player animations depending on the player state and direction
 * Created by buivuhoang on 05/02/17.
 */
public abstract class PlayerAnimationBase {
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

    public PlayerAnimationBase() {
        animationCache = new AnimationCache<>();
    }

    public Animation<TextureRegion> get(Type type, int direction, boolean lowHealth, boolean isAttacking, Damage.Type attackType, boolean isFirstAttackFrame, boolean changeStateDuringAttack) {
        if (isAttacking) {
            Animation<TextureRegion> attack = getAttack(type, direction, attackType, isFirstAttackFrame, changeStateDuringAttack);
            if (attack != null) {
                return attack;
            }
        }
        String texture = getTextureAtlas(type, lowHealth);
        List<Integer> index = getAnimationIndex(type, lowHealth);
        float frameDuration = getFrameDuration(type, lowHealth);

        return retrieveFromCache(type, direction, texture, index, frameDuration);
    }

    public Animation<TextureRegion> get(Type type) {
        return get(type, MovingObject.RIGHT);
    }

    public Animation<TextureRegion> get(Type type, int direction) {
        return get(type, direction, false, false, Damage.Type.LIGHT, false, false);
    }

    public Animation<TextureRegion> retrieveFromCache(Type type, int direction, String texture, List<Integer> animationIndex, float frameDuration) {
        AnimationKey key = new AnimationKey(type, direction, texture, animationIndex);
        boolean flipped = direction == MovingObject.LEFT;
        return animationCache.retrieveFromCache(key, flipped, texture, animationIndex, frameDuration);
    }

    public abstract Animation<TextureRegion> getAttack(Type type, int direction, Damage.Type attackType, boolean isFirstAttackFrame, boolean changeStateDuringAttack);
    public abstract float getAttackFrameDuration(Type type, Damage.Type attackType);
    public abstract float getAttackDuration(Type type, Damage.Type attackType, boolean changeStateDuringAttack);
    protected abstract String getTextureAtlas(Type type, boolean lowHealth);
    protected abstract List<Integer> getAnimationIndex(Type type, boolean lowHealth);
    protected abstract boolean isLooping(Type type, boolean isAttacking);

    /**
     * Padding to give the sprite for LEFT direction.
     * RIGHT direction will be automatically mirrored.
     */
    protected abstract Vector2 getAnimationPaddingX(Type type, int direction, boolean isAttacking, Damage.Type attackType, boolean changeStateDuringAttack);

    protected abstract float getFrameDuration(Type type, boolean lowHealth);

    public enum Type {
        // Common
        Idle,
        Run,
        Jump,
        Fall,
        Touchdown,
        Wallslide,
        Walljump,
        Dash,
        Dashbreak,
        Dashrocket,
        Updash,
        Updashrocket,
        DamagedNormal,
        BulletNoDamageExplode,
        // X
        BulletSmall,
        BulletSmallMuzzle,
        BulletSmallExplode,
        BulletMedium,
        BulletHeavy,
        BulletHeavyMuzzle,
        BulletMediumMuzzle,
        ChargeInnerCircles,
        BulletHeavyExplode,
        ChargeOuterCircles
    }
}

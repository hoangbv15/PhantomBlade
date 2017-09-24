package com.sideprojects.megamanxphantomblade.enemies.types.mettool;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

import java.util.EnumMap;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class Mettool extends EnemyBase<Mettool.State> {
    public Mettool(float x, float y, MapBase map, SoundPlayer soundPlayer, int difficulty) {
        super(x, y, map);
        mapCollisionBounds.setPosition(x, y);
        mapCollisionBounds.setSize(0.4f, 0.4f);
        takeDamageBounds.setPosition(x, y);
        setDealDamageBoundsSize(0.3f, 0.4f);
        damage = new Damage(Damage.Type.NORMAL, Damage.Side.NONE, -difficulty);
        script = new MettoolScript(this, map.player);
        auxiliaryFrames = new EnumMap<>(EnemyAnimationBase.Type.class);
        animations = new MettoolAnimation();
        sounds = new MettoolSound(soundPlayer);
        state = State.WALK;
    }

    @Override
    protected Vector2 getCollisionBoundsOffset() {
        return VectorCache.get(0.1f, 0f);
    }

    @Override
    protected float deathExplosionTime() {
        return 2f;
    }

    @Override
    protected boolean hasExplodingFragments() {
        return true;
    }

    @Override
    protected void updateTakeDamageBounds() {
        switch (state) {
            case BUCKLED_UP:
                takeDamageBounds.setSize(0.3f, 0.4f);
                break;
            default:
                takeDamageBounds.setSize(0.3f, 0.55f);
                break;
        }
    }

    @Override
    protected void updateAnimation(float delta) {
        EnemyAnimationBase.Type type = null;
        if (isDead()) {
            type = EnemyAnimationBase.Type.DIE;
        } else {
            switch (state) {
                case BUCKLED_UP:
                    type = EnemyAnimationBase.Type.IDLE;
                    break;
                case UNBUCKLE:
                    type = EnemyAnimationBase.Type.STOP_IDLING;
                    break;
                case WALK:
                    type = EnemyAnimationBase.Type.RUN;
                    break;
                case JUMP:
                    type = EnemyAnimationBase.Type.JUMP;
                    break;
                case SHOOT:
                    type = EnemyAnimationBase.Type.ATTACK;
                    break;
                case DIE:
                    type = EnemyAnimationBase.Type.DIE;
                    break;
            }
        }
        if (type != null) {
            Animation<TextureRegion> animation = animations.get(type, direction);
            TextureRegion frame = animation.getKeyFrame(stateTime, animations.isLooping(type));
            if (isDead()) {
                auxiliaryFrames.put(EnemyAnimationBase.Type.DIE, frame);
                currentFrame = null;
            } else {
                currentFrame = animation.getKeyFrame(stateTime, animations.isLooping(type));
                animationPadding = animations.getAnimationPaddingX(type, direction);
            }
        }
    }

    @Override
    public Vector2 getAuxiliaryAnimationPadding(EnemyAnimationBase.Type type, float delta) {
        if (type == EnemyAnimationBase.Type.DIE) {
            if (direction == LEFT) {
                return VectorCache.get(-20, -15);
            }
            return VectorCache.get(-13, -15);
        }
        return VectorCache.get(0, 0);
    }

    @Override
    public int getMaxHealthPoints() {
        return 20;
    }

    protected enum State {
        BUCKLED_UP,
        UNBUCKLE,
        WALK,
        JUMP,
        SHOOT,
        DIE
    }
}

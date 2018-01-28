package com.sideprojects.megamanxphantomblade.enemies.types.catapiride;

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
 * Created by buivuhoang on 30/09/17.
 */
public class Catapiride extends EnemyBase<Catapiride.State> {
    public Catapiride(float x, float y, MapBase map, SoundPlayer soundPlayer, int difficulty) {
        super(x, y, map);
        mapCollisionBounds.setPosition(x, y);
        mapCollisionBounds.setSize(0.4f, 0.4f);
        takeDamageBounds.setPosition(x, y);
        damage = new Damage(Damage.Type.NORMAL, Damage.Side.NONE, -difficulty);
        script = new CatapirideScript(this, map.player);
        animations = new CatapirideAnimation();
        sounds = new CatapirideSound(soundPlayer);
        state = State.WALK;
    }

    @Override
    protected Vector2 getCollisionBoundsOffset() {
        switch (state) {
            case TURN:
            case ROLL:
                return VectorCache.get(0.2f, 0f);
            default:
                return VectorCache.get(0f, 0f);
        }
    }

    @Override
    protected float deathExplosionTime() {
        return 2f;
    }

    @Override
    protected boolean hasExplodingFragments() {
        return false;
    }

    @Override
    protected void updateTakeDamageBounds() {
        switch (state) {
            case TURN:
                takeDamageBounds.setSize(0.2f, 0.5f);
                setDealDamageBoundsSize(0.2f, 0.4f);
                break;
            case ROLL:
                takeDamageBounds.setSize(0.2f, 0.5f);
                setDealDamageBoundsSize(0.2f, 0.4f);
                break;
            default:
                takeDamageBounds.setSize(0.4f, 0.5f);
                setDealDamageBoundsSize(0.4f, 0.4f);
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
                case TURN:
                    type = EnemyAnimationBase.Type.TURN;
                    break;
                case CURL_UP:
                    type = EnemyAnimationBase.Type.PREPARE_SHIELD;
                    break;
                case UN_CURL:
                    type = EnemyAnimationBase.Type.STOP_SHIELD;
                    break;
                case WALK:
                    type = EnemyAnimationBase.Type.RUN;
                    break;
                case ROLL:
                    type = EnemyAnimationBase.Type.ROLL;
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
        return VectorCache.get(-15f, -10f);
    }

    @Override
    public int getMaxHealthPoints() {
        return 20;
    }

    protected enum State {
        WALK,
        TURN,
        CURL_UP,
        ROLL,
        UN_CURL,
        DIE
    }
}

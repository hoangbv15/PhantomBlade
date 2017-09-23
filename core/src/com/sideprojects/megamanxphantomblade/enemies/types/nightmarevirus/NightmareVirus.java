package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

import java.util.HashMap;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirus extends EnemyBase<NightmareVirus.State> {

    public NightmareVirus(float x, float y, MapBase map, SoundPlayer soundPlayer, int difficulty) {
        super(x, y, map);
        mapCollisionBounds.setPosition(x, y);
        mapCollisionBounds.setSize(0.4f, 0.4f);
        takeDamageBounds.setSize(0.4f, 0.5f);
        takeDamageBounds.setPosition(x, y);
        damage = new Damage(Damage.Type.NORMAL, Damage.Side.NONE, -difficulty);
        animations = new NightmareVirusAnimation();
        auxiliaryFrames = new HashMap<>(1);
        script = new NightmareVirusScript(this, map.player, map);
        sounds = new NightmareVirusSound(soundPlayer);
        state = State.Idle;
    }

    @Override
    protected Vector2 getTakeDamageBoundsOffset() {
        return VectorCache.get(0.3f, 0.4f);
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
        // No need to update anything here
    }

    @Override
    protected void updateAnimation(float delta) {
        EnemyAnimationBase.Type type = null;
        if (isDead()) {
            type = EnemyAnimationBase.Type.Die;
        } else {
            switch (state) {
                case Idle:
                    type = EnemyAnimationBase.Type.Idle;
                    break;
                case Fly:
                    type = EnemyAnimationBase.Type.Run;
                    break;
                case PrepareToShoot:
                    type = EnemyAnimationBase.Type.PrepareAttack;
                    break;
                case Shoot:
                    type = EnemyAnimationBase.Type.Attack;
                    break;
                case FinishShooting:
                    type = EnemyAnimationBase.Type.FinishAttack;
                    break;
                case Die:
                    type = EnemyAnimationBase.Type.Die;
                    break;
            }
        }
        if (type != null) {
            Animation<TextureRegion> animation = animations.get(type, direction);
            TextureRegion frame = animation.getKeyFrame(stateTime, animations.isLooping(type));
            if (isDead()) {
                auxiliaryFrames.put(EnemyAnimationBase.Type.Die, frame);
                currentFrame = null;
            } else {
                currentFrame = animation.getKeyFrame(stateTime, animations.isLooping(type));
                animationPadding = animations.getAnimationPaddingX(type, direction);
            }
        }
    }

    @Override
    public Vector2 getAuxiliaryAnimationPadding(EnemyAnimationBase.Type type, float delta) {
        if (type == EnemyAnimationBase.Type.Die) {
            if (direction == LEFT) {
                return VectorCache.get(-5, 10);
            }
            return VectorCache.get(5, 10);
        }
        return VectorCache.get(0, 0);
    }

    @Override
    public int getMaxHealthPoints() {
        return 15;
    }

    @Override
    public boolean isAffectedByGravity() {
        return false;
    }

    @Override
    public boolean isStoppedByWalls() {
        return false;
    }

    protected enum State {
        Idle,
        Fly,
        PrepareToShoot,
        Shoot,
        FinishShooting,
        Die
    }
}

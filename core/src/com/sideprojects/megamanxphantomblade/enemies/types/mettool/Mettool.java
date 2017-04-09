package com.sideprojects.megamanxphantomblade.enemies.types.mettool;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

import java.util.*;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class Mettool extends EnemyBase<Mettool.State> {
    public Mettool(float x, float y, MapBase map, SoundPlayer soundPlayer, int difficulty) {
        super(x, y, map);
        bounds = new Rectangle(x, y, 0.4f, 0.5f);
        damage = new Damage(Damage.Type.Normal, Damage.Side.None, -difficulty);
        script = new MettoolScript(this, map.player);
        auxiliaryFrames = new HashMap<EnemyAnimationBase.Type, TextureRegion>(2);
        animations = new MettoolAnimation();
        sounds = new MettoolSound(soundPlayer);
        state = State.Walk;
    }

    @Override
    protected float deathExplosionTime() {
        return 2;
    }

    @Override
    protected void updateAnimation(float delta) {
        EnemyAnimationBase.Type type = null;
        if (isDead()) {
            type = EnemyAnimationBase.Type.Die;
        } else {
            switch (state) {
                case BuckledUp:
                    type = EnemyAnimationBase.Type.Idle;
                    break;
                case Walk:
                    type = EnemyAnimationBase.Type.Run;
                    break;
                case Jump:
                    type = EnemyAnimationBase.Type.Jump;
                    break;
                case Shoot:
                    type = EnemyAnimationBase.Type.Attack;
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
                return VectorCache.get(-20, -10);
            }
            return VectorCache.get(-13, -10);
        }
        return VectorCache.get(0, 0);
    }

    @Override
    public int getMaxHealthPoints() {
        return 20;
    }

    protected enum State {
        BuckledUp,
        Walk,
        Jump,
        Shoot,
        Die
    }
}

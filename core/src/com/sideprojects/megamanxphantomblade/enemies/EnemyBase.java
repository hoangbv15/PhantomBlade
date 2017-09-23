package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.*;

/**
 * Representing the enemy. Contains the enemy animation and damage management
 * Takes a template that represent the enemy's possible states
 * Created by buivuhoang on 19/03/17.
 */
public abstract class EnemyBase<T> extends MovingObject {
    public Vector2 spawnPos;

    public boolean spawned;
    public boolean canSpawn;

    public EnemyAnimationBase animations;
    protected Map<EnemyAnimationBase.Type, TextureRegion> auxiliaryFrames;
    public List<ExplodeFragment> explodeFragments;

    public TextureRegion currentFrame;
    public Vector2 animationPadding;

    public EnemySound sounds;

    public T state;
    public Damage damage;
    public boolean isTakingDamage;
    public boolean canTakeDamage;

    private float takeDamageDuration = 0.1f;
    private float takeDamageStateTime;

    protected final MapBase map;
    protected EnemyScript script;

    private float deathExplosionStateTime;

    public EnemyBase(float x, float y, MapBase map) {
        this.map = map;
        pos = new Vector2(x, y);
        spawnPos = new Vector2(x, y);
        vel = new Vector2(0, 0);
        isTakingDamage = false;
        canTakeDamage = true;
        canSpawn = true;
        explodeFragments = new ArrayList<>();
        takeDamageStateTime = 0;
        stateTime = 0;
        direction = MovingObject.LEFT;
    }

    public final void despawn(boolean canSpawn) {
        spawned = false;
        mapCollisionBounds.x = spawnPos.x;
        mapCollisionBounds.y = spawnPos.y;
        pos.x = mapCollisionBounds.x;
        pos.y = mapCollisionBounds.y;
        explodeFragments.clear();
        this.canSpawn = canSpawn;
    }

    public void spawn() {
        spawned = true;
        deathExplosionStateTime = 0;
        initialiseHealthPoints(getMaxHealthPoints());
    }

    public void update(float delta) {
        if (isDead()) {
            if (deathExplosionStateTime == 0) {
                sounds.playDie(delta);
            }
            if (deathExplosionStateTime < deathExplosionTime()) {
                deathExplosionStateTime += delta;
                stateTime = deathExplosionStateTime;

                if (hasExplodingFragments()) {
                    // Add explosion fragments
                    Animation<TextureRegion> explodeFragmentAnimation = animations.get(EnemyAnimationBase.Type.EXPLODE_FRAGMENT, direction);
                    List<TextureRegion> fragmentFrames = Arrays.asList(explodeFragmentAnimation.getKeyFrames());
                    if (explodeFragments.isEmpty()) {
                        for (TextureRegion fragmentFrame : fragmentFrames) {
                            explodeFragments.add(new ExplodeFragment(fragmentFrame, pos.x - mapCollisionBounds.getWidth() / 3f, pos.y - mapCollisionBounds.getHeight() / 3f, MathUtils.random(2f), 5f));
                        }
                    } else {
                        for (ExplodeFragment fragment : explodeFragments) {
                            script.applyGravity(fragment, map.GRAVITY, map.MAX_FALLSPEED, delta);
                            fragment.update(delta);
                        }
                    }
                }
            } else {
                despawn(false);
            }
        } else {
            stateTime += delta;
            if (isTakingDamage) {
                takeDamageStateTime += delta;
                if (takeDamageStateTime >= takeDamageDuration) {
                    isTakingDamage = false;
                    takeDamageStateTime = 0;
                }
            }

            script.update(this, delta, map);
        }

        updateAnimation(delta);
        updateTakeDamageBounds();
    }

    protected abstract float deathExplosionTime();

    protected abstract boolean hasExplodingFragments();

    public void setState(T state) {
        this.state = state;
    }

    @Override
    public boolean takeDamage(Damage damage) {
        if (canTakeDamage) {
            isTakingDamage = true;
            return super.takeDamage(damage);
        }
        return false;
    }

    public Map<EnemyAnimationBase.Type, TextureRegion> getAuxiliaryFrames() {
        return auxiliaryFrames;
    }

    protected abstract void updateTakeDamageBounds();

    protected abstract void updateAnimation(float delta);

    public abstract Vector2 getAuxiliaryAnimationPadding(EnemyAnimationBase.Type type, float delta);

    public abstract int getMaxHealthPoints();
}

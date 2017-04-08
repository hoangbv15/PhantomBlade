package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.Map;

/**
 * Representing the enemy. Contains the enemy animation and damage management
 * Takes a template that represent the enemy's possible states
 * Created by buivuhoang on 19/03/17.
 */
public abstract class EnemyBase<T> extends MovingObject {
    public boolean spawned;
    public boolean canSpawn;

    public EnemyAnimationBase animations;
    public Map<EnemyAnimationBase.Type, TextureRegion> auxiliaryFrames;

    public TextureRegion currentFrame;
    public Vector2 animationPadding;

    protected EnemySound sounds;

    public T state;
    public Damage damage;
    public boolean isTakingDamage;
    public boolean canTakeDamage;

    private float takeDamageDuration = 0.1f;
    private float takeDamageStateTime;

    private final MapBase map;
    protected EnemyScript script;

    private float deathExplosionStateTime;

    public EnemyBase(float x, float y, MapBase map) {
        this.map = map;
        pos = new Vector2(x, y);
        vel = new Vector2(0, 0);
        isTakingDamage = false;
        canTakeDamage = true;
        canSpawn = true;
        takeDamageStateTime = 0;
        stateTime = 0;
    }

    public final void despawn(boolean canSpawn) {
        spawned = false;
        this.canSpawn = canSpawn;
    }

    public void spawn() {
        spawned = true;
        deathExplosionStateTime = 0;
        initialiseHealthPoints(getMaxHealthPoints());
    }

    public void update(float delta) {
        if (isDead()) {
            sounds.playDie(delta);
            if (deathExplosionStateTime < deathExplosionTime()) {
                deathExplosionStateTime += delta;
                stateTime = deathExplosionStateTime;
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

    }

    protected abstract float deathExplosionTime();

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

    protected abstract void updateAnimation(float delta);

    public abstract Vector2 getAuxiliaryAnimationPadding();

    public abstract int getMaxHealthPoints();
}

package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;

/**
 * Created by buivuhoang on 19/03/17.
 */
public abstract class EnemyBase extends MovingObject {
    public TextureRegion currentFrame = new TextureRegion(new Texture("Favicon_16x16.png"));
    public Damage damage;
    public boolean shouldBeRemoved;
    public boolean isTakingDamage;
    public boolean canTakeDamage;

    private float takeDamageDuration = 0.1f;
    private float takeDamageStateTime;

    private final MapBase map;
    protected EnemyScript script;


    public EnemyBase(float x, float y, MapBase map) {
        this.map = map;
        pos = new Vector2(x, y);
        vel = new Vector2(0, 0);
        shouldBeRemoved = false;
        isTakingDamage = false;
        canTakeDamage = true;
        takeDamageStateTime = 0;
    }

    public void update(float delta) {
        if (isTakingDamage) {
            takeDamageStateTime += delta;
            if (takeDamageStateTime >= takeDamageDuration) {
                isTakingDamage = false;
                takeDamageStateTime = 0;
            }
        }

        if (isDead()) {
            shouldBeRemoved = true;
        } else {
            script.update(this, delta, map);
        }

        updateAnimation(delta);
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
}

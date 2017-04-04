package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;

/**
 * Created by buivuhoang on 19/03/17.
 */
public class EnemyBase extends MovingObject {
    public TextureRegion currentFrame = new TextureRegion(new Texture("Favicon_16x16.png"));
    public Damage damage;
    public boolean shouldBeRemoved;
    public boolean isTakingDamage;

    private float takeDamageDuration = 0.1f;
    private float stateTime;

    public EnemyBase(float x, float y) {
        pos = new Vector2(x, y);
//        bounds = new Rectangle(x, y, 0.25f, 0.25f);
        bounds = new Rectangle(x, y, 1f, 1f);
        initialiseHealthPoints(150000);
        damage = new Damage(Damage.Type.Normal, Damage.Side.None);
        shouldBeRemoved = false;
        isTakingDamage = false;
        stateTime = 0;
    }

    public void update(float delta) {
        if (isTakingDamage) {
            stateTime += delta;
            if (stateTime >= takeDamageDuration) {
                isTakingDamage = false;
                stateTime = 0;
            }
        }

        if (isDead()) {
            shouldBeRemoved = true;
        }
    }

    @Override
    public boolean takeDamage(Damage damage) {
        isTakingDamage = true;
        return super.takeDamage(damage);
    }
}

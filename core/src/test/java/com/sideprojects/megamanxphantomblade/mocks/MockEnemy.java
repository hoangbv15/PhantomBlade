package com.sideprojects.megamanxphantomblade.mocks;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;

/**
 * Created by buivuhoang on 25/09/17.
 */
public class MockEnemy extends EnemyBase {
    public MockEnemy(float x, float y, MapBase map) {
        super(x, y, map);
    }

    @Override
    protected float deathExplosionTime() {
        return 0;
    }

    @Override
    protected boolean hasExplodingFragments() {
        return false;
    }

    @Override
    protected void updateTakeDamageBounds() {

    }

    @Override
    protected void updateAnimation(float delta) {

    }

    @Override
    public Vector2 getAuxiliaryAnimationPadding(EnemyAnimationBase.Type type, float delta) {
        return null;
    }

    @Override
    public int getMaxHealthPoints() {
        return 10;
    }
}

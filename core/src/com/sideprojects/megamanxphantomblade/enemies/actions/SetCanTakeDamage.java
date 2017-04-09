package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class SetCanTakeDamage extends ActionBase {
    private final EnemyBase enemy;
    private final boolean canTakeDamage;

    public SetCanTakeDamage(EnemyBase enemy, boolean canTakeDamage) {
        this.enemy = enemy;
        this.canTakeDamage = canTakeDamage;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        enemy.canTakeDamage = canTakeDamage;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return enemy.canTakeDamage == canTakeDamage;
    }
}

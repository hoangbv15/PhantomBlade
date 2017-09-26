package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class SpawnEnemyAttackToMap extends ActionBase {
    private final EnemyAttack enemyAttack;
    private final MapBase map;
    private boolean executed;

    public SpawnEnemyAttackToMap(EnemyAttack enemyAttack, MapBase map) {
        this.enemyAttack = enemyAttack;
        this.map = map;
        executed = false;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        enemyAttack.onInitialise();
        map.addEnemyAttack(enemyAttack);
        executed = true;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return executed;
    }
}

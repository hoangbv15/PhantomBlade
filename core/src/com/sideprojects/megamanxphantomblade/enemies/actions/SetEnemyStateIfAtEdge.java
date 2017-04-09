package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 09/04/17.
 */
public class SetEnemyStateIfAtEdge extends SetEnemyState {
    public SetEnemyStateIfAtEdge(EnemyBase enemy, Object state) {
        super(enemy, state);
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        if (collisions.isAtEdge()) {
            super.execute(collisions, delta);
        }
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return !collisions.isAtEdge() || super.finish(collisions);
    }
}

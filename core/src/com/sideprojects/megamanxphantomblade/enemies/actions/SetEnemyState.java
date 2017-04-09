package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.physics.ActionBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class SetEnemyState<T> extends ActionBase {
    private EnemyBase<T> enemy;
    private T state;
    public SetEnemyState(EnemyBase<T> enemy, T state) {
        this.enemy = enemy;
        this.state = state;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        enemy.setState(state);
        enemy.stateTime = 0;
    }

    @Override
    public boolean finish(CollisionList collisions) {
        return enemy.state == state;
    }
}

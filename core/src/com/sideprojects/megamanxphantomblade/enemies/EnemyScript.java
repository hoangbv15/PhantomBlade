package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.enemies.actions.SetEnemyStateIfAtEdge;
import com.sideprojects.megamanxphantomblade.physics.actions.JumpIfAtEdge;
import com.sideprojects.megamanxphantomblade.enemies.actions.MoveTowardsPlayer;
import com.sideprojects.megamanxphantomblade.enemies.actions.SetCanTakeDamage;
import com.sideprojects.megamanxphantomblade.enemies.actions.SetEnemyState;
import com.sideprojects.megamanxphantomblade.physics.ScriptBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Describes a script that the enemy will act accordingly
 * T represents the possible states the enemy can have
 * Created by buivuhoang on 06/04/17.
 */
public abstract class EnemyScript<T> extends ScriptBase {
    protected EnemyBase<T> enemy;
    protected PlayerBase player;

    public EnemyScript(EnemyBase<T> enemy, PlayerBase player) {
        super(enemy);
        this.enemy = enemy;
        this.player = player;
    }

    protected void setCanTakeDamage(boolean canTakeDamage) {
        addToQueue(new SetCanTakeDamage(enemy, canTakeDamage));
    }

    protected void moveTowardsPlayer(float speed, float time) {
        addToQueue(new MoveTowardsPlayer(enemy, player, speed, time));
    }

    protected void setEnemyState(T state) {
        addToQueue(new SetEnemyState<T>(enemy, state));
    }

    protected void setEnemyStateIfAtEdge(T state) {
        addToQueue(new SetEnemyStateIfAtEdge(enemy, state));
    }
}

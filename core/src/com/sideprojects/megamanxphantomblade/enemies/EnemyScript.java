package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.enemies.actions.*;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.ScriptBase;
import com.sideprojects.megamanxphantomblade.physics.actions.FlyTowardsMovingObject;
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

    protected void walkTowardsPlayer(float speed, float time) {
        addToQueue(new WalkTowardsPlayer(enemy, player, speed, time));
    }

    protected void flyTowardsPlayer(float speed, float time) {
        addToQueue(new FlyTowardsMovingObject(enemy, player, speed, time));
    }

    protected void setEnemyState(T state) {
        addToQueue(new SetEnemyState<>(enemy, state));
    }

    protected void resetAnimation() {
        addToQueue(new ResetAnimation(enemy));
    }

    protected void spawnEnemyAttack(EnemyAttack attack, MapBase map) {
        addToQueue(new SpawnEnemyAttackToMap(attack, map));
    }

    protected void setEnemyStateIfAtEdge(T state) {
        addToQueue(new SetEnemyStateIfAtEdge(enemy, state));
    }
}

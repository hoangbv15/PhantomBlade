package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.enemies.actions.MoveTowardsPlayer;
import com.sideprojects.megamanxphantomblade.enemies.actions.SetCanTakeDamage;
import com.sideprojects.megamanxphantomblade.physics.ScriptBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 06/04/17.
 */
public abstract class EnemyScript extends ScriptBase {
    private EnemyBase enemy;
    private PlayerBase player;

    public EnemyScript(EnemyBase enemy, PlayerBase player) {
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
}

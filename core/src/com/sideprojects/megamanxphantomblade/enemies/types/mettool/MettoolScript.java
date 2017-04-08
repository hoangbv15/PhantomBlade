package com.sideprojects.megamanxphantomblade.enemies.types.mettool;

import com.badlogic.gdx.math.MathUtils;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyScript;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 04/04/17.
 */
public class MettoolScript extends EnemyScript<Mettool.State> {
    private static float velocity = 1;
    private static float time = 1;

    public MettoolScript(EnemyBase<Mettool.State> enemy, PlayerBase player) {
        super(enemy, player);
    }

    @Override
    public void describe() {
        int rand = MathUtils.random(1);
        if (rand == 0) {
            stupidAi();
        } else {
            annoyingAi();
        }
    }

    private void stupidAi() {
        setEnemyState(Mettool.State.Walk);
        move(MovingObject.LEFT, velocity, time);
        setCanTakeDamage(false);
        setEnemyState(Mettool.State.BuckledUp);
        wait(time);
        setCanTakeDamage(true);
        setEnemyState(Mettool.State.Walk);
        move(MovingObject.RIGHT, velocity, time);
        setCanTakeDamage(false);
        setEnemyState(Mettool.State.BuckledUp);
        wait(time);
        setCanTakeDamage(true);
    }

    private void annoyingAi() {
        setEnemyState(Mettool.State.Walk);
        moveTowardsPlayer(velocity, 5);
        setCanTakeDamage(false);
        setEnemyState(Mettool.State.BuckledUp);
        wait(time);
        setCanTakeDamage(true);
    }
}

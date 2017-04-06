package com.sideprojects.megamanxphantomblade.enemies.scripts;

import com.badlogic.gdx.math.MathUtils;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyScript;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 04/04/17.
 */
public class Mettool extends EnemyScript {
    private static float velocity = 1;
    private static float time = 1;

    public Mettool(EnemyBase enemy, PlayerBase player) {
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
        move(MovingObject.LEFT, velocity, time);
        setCanTakeDamage(false);
        wait(time);
        setCanTakeDamage(true);
        move(MovingObject.RIGHT, velocity, time);
        setCanTakeDamage(false);
        wait(time);
        setCanTakeDamage(true);
    }

    private void annoyingAi() {
        moveTowardsPlayer(velocity, 5);
        setCanTakeDamage(false);
        wait(time);
        setCanTakeDamage(true);
    }
}

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
    private static float jumpVelocity = 5;
    private static float walkTime = 2;
    private static float waitTimeBuckledUp = 1;
    private static float waitTimeUnbuckle = 0.4f;
    private static float waitTimeBeforeJump = 0.4f;
    private boolean isStupidAi;

    public MettoolScript(EnemyBase<Mettool.State> enemy, PlayerBase player) {
        super(enemy, player);
    }

    @Override
    public void describe() {
        int rand = MathUtils.random(1);
        if (rand == 0) {
            isStupidAi = true;
        } else {
            isStupidAi = false;
        }
        if (isStupidAi) {
            stupidAi();
        } else {
            annoyingAi();
        }
    }

    private void stupidAi() {
        setEnemyState(Mettool.State.Walk);
        moveTillEdge(MovingObject.LEFT, velocity, walkTime);
        int rand = MathUtils.random(2);
        if (rand == 0) {
            setEnemyStateIfAtEdge(Mettool.State.Jump);
            jumpIfAtEdge(velocity, jumpVelocity, waitTimeBeforeJump);
        } else if (rand == 1) {
            setCanTakeDamage(false);
            setEnemyState(Mettool.State.BuckledUp);
            wait(waitTimeBuckledUp);
            setCanTakeDamage(true);
            setEnemyState(Mettool.State.Unbuckle);
            wait(waitTimeUnbuckle);
        }
        setEnemyState(Mettool.State.Walk);
        moveTillEdge(MovingObject.RIGHT, velocity, walkTime);
        setEnemyStateIfAtEdge(Mettool.State.Jump);
        jumpIfAtEdge(velocity, jumpVelocity, waitTimeBeforeJump);
        setCanTakeDamage(false);
        setEnemyState(Mettool.State.BuckledUp);
        wait(waitTimeBuckledUp);
        setCanTakeDamage(true);
        setEnemyState(Mettool.State.Unbuckle);
        wait(waitTimeUnbuckle);
    }

    private void annoyingAi() {
        setEnemyState(Mettool.State.Walk);
        walkTowardsPlayer(velocity, 5);
        setEnemyStateIfAtEdge(Mettool.State.Jump);
        jumpIfAtEdge(velocity, jumpVelocity, waitTimeBeforeJump);
        setCanTakeDamage(false);
        setEnemyState(Mettool.State.BuckledUp);
        wait(waitTimeBuckledUp);
        setCanTakeDamage(true);
        setEnemyState(Mettool.State.Unbuckle);
        wait(waitTimeUnbuckle);
    }
}

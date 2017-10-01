package com.sideprojects.megamanxphantomblade.enemies.types.catapiride;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyScript;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 30/09/17.
 */
public class CatapirideScript extends EnemyScript<Catapiride.State> {
    private final float velocity = 0.5f;
    private final float rollVelocity = 1f;

    private final float walkTime = 4;
    private final float rollTime = 2;

    private final float waitTimeTurn = 0.3f;
    private final float waitTimeCurlUp = 1.4f;

    public CatapirideScript(EnemyBase<Catapiride.State> enemy, PlayerBase player) {
        super(enemy, player);
    }

    @Override
    public void describe() {
        setCanTakeDamage(true);
        setEnemyState(Catapiride.State.WALK);
        moveTillEdge(MovingObject.LEFT, velocity, walkTime);
        setEnemyState(Catapiride.State.TURN);
        wait(waitTimeTurn);
        setEnemyState(Catapiride.State.WALK);
        moveTillEdge(MovingObject.RIGHT, velocity, walkTime);
        setEnemyState(Catapiride.State.CURL_UP);
        wait(waitTimeCurlUp/2);
        setCanTakeDamage(false);
        wait(waitTimeCurlUp/2);
        setEnemyState(Catapiride.State.ROLL);
        moveTillEdge(MovingObject.LEFT, rollVelocity, rollTime);
        setEnemyState(Catapiride.State.UN_CURL);
        wait(waitTimeCurlUp/2);
        setCanTakeDamage(true);
        wait(waitTimeCurlUp/2);
    }
}

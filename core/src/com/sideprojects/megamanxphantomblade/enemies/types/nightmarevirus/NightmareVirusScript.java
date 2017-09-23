package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.badlogic.gdx.math.MathUtils;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyScript;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

import java.util.stream.IntStream;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirusScript extends EnemyScript<NightmareVirus.State> {
    private final MapBase map;
    private static float velocity = 1f;
    private static float waitTimeIdle = 3f;
    private static float flyTowardsPlayerTime = 2.5f;
    private static float randomThreshold = 0.5f;

    private static float waitTimePrepareToShoot = 0.1f;
    private static float waitTimeFinishShooting = waitTimePrepareToShoot;
    private static float waitTimeShotDelay = 0.2f;
    private static float waitTimeShoot = 1f;

    public NightmareVirusScript(EnemyBase<NightmareVirus.State> enemy, PlayerBase player, MapBase map) {
        super(enemy, player);
        this.map = map;
    }

    @Override
    public void describe() {
        setEnemyState(NightmareVirus.State.IDLE);
        doWait();
        int rand = 1;//MathUtils.random(2);
        if (rand == 1) {
            doShootAttack();
            setEnemyState(NightmareVirus.State.IDLE);
            doWait();
        }
        setEnemyState(NightmareVirus.State.FLY);
        flyTowardsPlayer(velocity, MathUtils.random(flyTowardsPlayerTime - randomThreshold, flyTowardsPlayerTime + randomThreshold));
    }

    private void doWait() {
        wait(MathUtils.random(waitTimeIdle - randomThreshold, waitTimeIdle + randomThreshold));
    }

    private void doShootAttack() {
        setEnemyState(NightmareVirus.State.PREPARE_TO_SHOOT);
        wait(waitTimePrepareToShoot);
        IntStream.range(0, 3).forEach(i -> {
            resetAnimation();
            setEnemyState(NightmareVirus.State.SHOOT);
            wait(waitTimeShotDelay);
            spawnEnemyAttack(new NightmareVirusBullet(enemy.mapCollisionBounds, player.takeDamageBounds,
                    10f, enemy.damage, enemy.direction, enemy.animations, enemy.sounds), map);
            wait(waitTimeShoot);
        });
        setEnemyState(NightmareVirus.State.FINISH_SHOOTING);
        wait(waitTimeFinishShooting);
    }
}

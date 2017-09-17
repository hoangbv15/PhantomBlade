package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyScript;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirusScript extends EnemyScript<NightmareVirus.State> {
    private static float velocity = 1f;
    private static float waitTimeIdle = 3f;
    private static float flyTowardsPlayerTime = 2.5f;

    public NightmareVirusScript(EnemyBase<NightmareVirus.State> enemy, PlayerBase player) {
        super(enemy, player);
    }

    @Override
    public void describe() {
        setEnemyState(NightmareVirus.State.Idle);
        wait(waitTimeIdle);
        setEnemyState(NightmareVirus.State.Fly);
        flyTowardsPlayer(velocity, flyTowardsPlayerTime);
    }
}

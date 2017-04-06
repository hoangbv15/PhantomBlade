package com.sideprojects.megamanxphantomblade.enemies;

/**
 * Created by buivuhoang on 05/04/17.
 */
public class EnemySpawn {
    public float x;
    public float y;
    public boolean canBeSpawned;

    public EnemySpawn(float x, float y, boolean canBeSpawned) {
        this.x = x;
        this.y = y;
        this.canBeSpawned = canBeSpawned;
    }
}

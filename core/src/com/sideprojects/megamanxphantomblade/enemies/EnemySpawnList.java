package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.sideprojects.megamanxphantomblade.enemies.types.Mettool;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buivuhoang on 05/04/17.
 */
public class EnemySpawnList {
    // Number of surrounding tiles that can trigger the spawn
    private static int spawnRadius = 2;
    private Map<Integer, Map<Integer, List<EnemySpawn>>> enemySpawnMap;
    private MapBase map;

    public EnemySpawnList(MapBase map) {
        this.map = map;
        enemySpawnMap = new HashMap<Integer, Map<Integer, List<EnemySpawn>>>();
    }

    public void addEnemySpawn(EnemySpawn spawn) {
        int x = (int)spawn.x;
        int y = (int)spawn.y;
        if (!enemySpawnMap.containsKey(x)) {
            enemySpawnMap.put(x, new HashMap<Integer, List<EnemySpawn>>());
        }
        Map<Integer, List<EnemySpawn>> yMap = enemySpawnMap.get(x);
        if (!yMap.containsKey(y)) {
            yMap.put(y, new ArrayList<EnemySpawn>());
        }
        yMap.get(y).add(spawn);
    }

    /**
     * This determines whether the point is potentially visible to the player
     * The viewport height and width are 9 and 16. Half of those are 4.5 and 8.
     * We allow a bit of leeway by giving another 1/4th of the values here.
     */
    public void spawnEnemyIfInRange(int x, int y, List<EnemyBase> enemyList) {
        for (int i = 0; i <= spawnRadius; i++) {
            if (enemySpawnMap.isEmpty()) {
                break;
            }
            if (!enemySpawnMap.containsKey(x + i)) {
                continue;
            }
            Map<Integer, List<EnemySpawn>> yMap = enemySpawnMap.get(x + i);
            for (int j = 0; j <= spawnRadius; j++) {
                if (yMap.isEmpty()) {
                    break;
                }
                if (!yMap.containsKey(y + j)) {
                    continue;
                }
                // We have a spawn!
                spawn(yMap.get(y + j), enemyList);
                yMap.remove(y + j);
            }
            if (yMap.isEmpty()) {
                enemySpawnMap.remove(x + i);
            }
        }
    }

    private void spawn(List<EnemySpawn> enemySpawnList, List<EnemyBase> enemyList) {
        for (EnemySpawn spawn: enemySpawnList) {
            enemyList.add(new Mettool(spawn.x, spawn.y, map));
        }
    }
}

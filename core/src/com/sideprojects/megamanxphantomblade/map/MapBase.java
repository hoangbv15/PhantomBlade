package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.animation.Particles;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.types.mettool.Mettool;
import com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus.NightmareVirus;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class MapBase implements Disposable {
    public static String TotalNumOfTiles = "Total";
    public static String TileIndex = "Index";
    public static String Orientation = "Orientation";
    public static String BottomLeft = "BottomLeft";
    public static String BottomRight = "BottomRight";
    public static String TopLeft = "TopLeft";
    public static String TopRight = "TopRight";
    public static String TileType = "Type";
    public static String SquareTriangle = "SquareTriangle";

    public static String HalfTileSize = "Half";
    public static String TileSize = "Size";

    public static String MapLayer = "Map";
    public static String ObjectLayer = "Objects";
    public static String XSpawn = "XSpawn";
    public static String MettoolSpawn = "MettoolSpawn";
    public static String NightmareVirusSpawn = "NightmareVirusSpawn";

    public float GRAVITY = 15f;
    public float MAX_FALLSPEED = -8f;
    public float WALLSLIDE_FALLSPEED = -2f;
    private static int MAX_PLAYERATTACK = 20;
    private static int MAX_ENEMYATTACK = 20;

    public TiledMap tiledMap;
    private TiledMapTileLayer mapLayer;

    private PlayerFactory playerFactory;
    private PlayerPhysicsFactory playerPhysicsFactory;
    public PlayerBase player;
    public PlayerPhysics playerPhysics;
    public TileBase[][] bounds;

    public List<EnemyBase> enemyList;
    public Queue<PlayerAttack> playerAttackQueue;
    public Queue<EnemyAttack> enemyAttackQueue;

    public Particles particles;

    // This is DI to inject into enemies
    private SoundPlayer soundPlayer;

    public MapBase(PlayerFactory playerFactory, PlayerPhysicsFactory playerPhysicsFactory, SoundPlayer soundPlayer, int difficulty) {
        this.playerFactory = playerFactory;
        this.playerPhysicsFactory = playerPhysicsFactory;
        this.soundPlayer = soundPlayer;
        particles = new Particles(20);
        enemyList = new ArrayList<>();
        playerAttackQueue = new Queue<>(MAX_PLAYERATTACK);
        enemyAttackQueue = new Queue<>(MAX_ENEMYATTACK);
        loadMap(difficulty);
    }

    public float getTileWidth() {
        return mapLayer.getTileWidth();
    }

    public float getTileHeight() {
        return mapLayer.getTileHeight();
    }

    public float getWidth() {
        return mapLayer.getWidth() * getTileWidth();
    }

    public float getHeight() {
        return mapLayer.getHeight() * getTileHeight();
    }

    protected abstract TiledMap getMapResource();

    public abstract ParallaxBackground getBackground();

    private void loadMap(int difficulty) {
        tiledMap = getMapResource();
        if (tiledMap == null) {
            return;
        }
        mapLayer = (TiledMapTileLayer)tiledMap.getLayers().get(MapLayer);
        if (mapLayer == null) {
            return;
        }
        bounds = new TileBase[mapLayer.getWidth()][mapLayer.getHeight()];

        // Spawn player and enemies
        MapObjects objects = tiledMap.getLayers().get(ObjectLayer).getObjects();
        for (int i = 0; i < objects.getCount(); i ++) {
            RectangleMapObject object = (RectangleMapObject)objects.get(i);
            float x = object.getRectangle().x / getTileWidth();
            float y = object.getRectangle().y / getTileHeight();
            if (XSpawn.equals(object.getName())) {
                player = playerFactory.createPlayer(x, y, difficulty);
                playerPhysics = playerPhysicsFactory.create(player);
            }
            if (MettoolSpawn.equals(object.getName())) {
                enemyList.add(new Mettool(x, y, this, soundPlayer, difficulty));
            } else if (NightmareVirusSpawn.equals(object.getName())) {
                enemyList.add(new NightmareVirus(x, y, this, soundPlayer, difficulty));
            }
        }

        // Create bounding boxes
        for (int y = 0; y < mapLayer.getHeight(); y++) {
            for (int x = 0; x < mapLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                if (cell != null) {
                    MapProperties properties = cell.getTile().getProperties();
                    if (properties.containsKey(TileSize) && HalfTileSize.equals(properties.get(TileSize, String.class))) {
                        bounds[x][y] = new RectangleTile(x, y, 1, 45/62f);
                    } else if (properties.containsKey(TileType) && SquareTriangle.equals(properties.get(TileType, String.class))) {
                        String orientation = properties.get(Orientation, String.class);
                        int totalTiles = properties.get(TotalNumOfTiles, Integer.class);
                        int tileIndex = properties.get(TileIndex, Integer.class);
                        float startY = y + tileIndex / (float)totalTiles;
                        float endY = y + (tileIndex + 1) / (float)totalTiles;
                        if (BottomLeft.equals(orientation)) {
                            bounds[x][y] = new SquareTriangleTile(x, startY, x, startY, x, endY, x + 1, startY, tileIndex, totalTiles);
                        } else if (BottomRight.equals(orientation)) {
                            bounds[x][y] = new SquareTriangleTile(x, startY, x + 1, startY, x + 1, endY, x, startY, tileIndex, totalTiles);
                        } else if (TopRight.equals(orientation)) {
                            bounds[x][y] = new SquareTriangleTile(x, startY, x, endY, x, startY, x + 1, endY, tileIndex, totalTiles);
                        } else if (TopLeft.equals(orientation)) {
                            bounds[x][y] = new SquareTriangleTile(x, startY, x + 1, endY, x + 1, startY, x, endY, tileIndex, totalTiles);
                        }
                    } else {
                        bounds[x][y] = new RectangleTile(x, y, 1, 1);
                    }
                }
            }
        }
    }

    /**
     * This determines whether the point is potentially visible to the player
     * The viewport height and width are 9 and 16. Half of those are 4.5 and 8.
     * We allow a bit of leeway by giving another 1/4th of the values here.
     */
    private boolean isPointInPlayerRange(float x, float y) {
        return Math.abs((int)x - (int)player.mapCollisionBounds.x) < 12 && Math.abs((int)y - (int)player.mapCollisionBounds.y) < 7;
    }

    public void update(float deltaTime) {
        playerPhysics.update(player, deltaTime, this);
        player.update(this, deltaTime);
        particles.update(deltaTime);
        updatePlayerAttack(deltaTime);
        updateEnemyAttack(deltaTime);

        for (EnemyBase enemy : enemyList) {
            // If the enemy is outside of player's range, kill it
            if (!isPointInPlayerRange(enemy.mapCollisionBounds.x, enemy.mapCollisionBounds.y)) {
                if (isPointInPlayerRange(enemy.spawnPos.x, enemy.spawnPos.y)) {
                    enemy.despawn(false);
                } else {
                    enemy.despawn(true);
                }
            } else if (!enemy.spawned && enemy.canSpawn) {
                enemy.spawn();
            }
            if (enemy.spawned) {
                enemy.update(deltaTime);
            }
        }

        // If player dies, respawn for now
        if (player.isDead()) {
            player.spawn();
        }
    }

    private void updatePlayerAttack(float deltaTime) {
        Iterator<PlayerAttack> i = playerAttackQueue.iterator();
        while (i.hasNext()) {
            PlayerAttack attack = i.next();
            attack.update(deltaTime);
            if (!isPointInPlayerRange(attack.mapCollisionBounds.x, attack.mapCollisionBounds.y)) {
                attack.setShouldBeRemoved(true);
            }
            if (attack.isShouldBeRemoved()) {
                i.remove();
            } else {
                if (attack.canCollideWithWall()) {
                    playerPhysics.stopAttackIfHitWall(attack, deltaTime, this);
                }
                playerPhysics.dealDamageIfPlayerAttackHitsEnemy(attack, this);
            }
        }
    }

    private void updateEnemyAttack(float deltaTime) {
        Iterator<EnemyAttack> i = enemyAttackQueue.iterator();
        while (i.hasNext()) {
            EnemyAttack attack = i.next();
            attack.update(deltaTime);
            if (!isPointInPlayerRange(attack.mapCollisionBounds.x, attack.mapCollisionBounds.y)) {
                attack.setShouldBeRemoved(true);
            }
            if (attack.isShouldBeRemoved()) {
                i.remove();
            } else {
                if (attack.canCollideWithWall()) {
                    playerPhysics.stopAttackIfHitWall(attack, deltaTime, this);
                }
            }
        }
    }

    public boolean isOutOfBounds(MovingObject object) {
        return object.mapCollisionBounds.y + object.mapCollisionBounds.getHeight() < 0;
    }

    public void addPlayerAttack(PlayerAttack attack) {
        playerAttackQueue.addLast(attack);
        if (playerAttackQueue.size >= MAX_PLAYERATTACK) {
            playerAttackQueue.removeFirst();
        }
    }

    public void addEnemyAttack(EnemyAttack attack) {
        enemyAttackQueue.addLast(attack);
        if (enemyAttackQueue.size >= MAX_ENEMYATTACK) {
            enemyAttackQueue.removeFirst();
        }
    }

    public TileBase getCollidableBox(int x, int y) {
        if (x < 0 || x >= bounds.length) {
            return new RectangleTile(x, y, 1, 1, true);
        }
        if (y >= bounds[0].length) {
            if (x >= 0 && x < bounds.length ) {
                if (bounds[x][bounds[0].length - 1] != null) {
                    return new RectangleTile(x, y, 1, 1, true);
                }
            }
            return null;
        }
        if (y < 0) {
            return null;
        }
        return bounds[x][y];
    }

    public void addParticle(Particle.ParticleType type, float x, float y, boolean isSingletonParticle) {
        particles.add(type, x, y, isSingletonParticle, playerPhysics.movementState.startingDirection);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}
package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.animation.Particles;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class MapBase implements Disposable {
    public static String MapLayer = "Map";
    public static String ObjectLayer = "Objects";
    public static String XSpawn = "XSpawn";
    public static String EnemySpawn = "EnemySpawn";

    public float GRAVITY = 15f;
    public float MAX_FALLSPEED = -8f;
    public float WALLSLIDE_FALLSPEED = -2f;
    private static int MAX_PLAYERATTACK = 20;

    public TiledMap tiledMap;
    private TiledMapTileLayer mapLayer;

    private PlayerFactory playerFactory;
    private PlayerPhysicsFactory playerPhysicsFactory;
    public PlayerBase player;
    public PlayerPhysics playerPhysics;
    public Rectangle[][] bounds;

    public List<EnemyBase> enemyList;
    public Queue<PlayerAttack> playerAttackList;

    public Particles particles;

    public MapBase(PlayerFactory playerFactory, PlayerPhysicsFactory playerPhysicsFactory) {
        this.playerFactory = playerFactory;
        this.playerPhysicsFactory = playerPhysicsFactory;
        particles = new Particles(20);
        enemyList = new ArrayList<EnemyBase>();
        playerAttackList = new Queue<PlayerAttack>(MAX_PLAYERATTACK);
        loadMap();
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

    private void loadMap() {
        tiledMap = getMapResource();
        mapLayer = (TiledMapTileLayer)tiledMap.getLayers().get(MapLayer);

        bounds = new Rectangle[mapLayer.getWidth()][mapLayer.getHeight()];

        // Spawn player and enemies
        MapObjects objects = tiledMap.getLayers().get(ObjectLayer).getObjects();
        for (int i = 0; i < objects.getCount(); i ++) {
            RectangleMapObject object = (RectangleMapObject)objects.get(i);
            float x = object.getRectangle().x / getTileWidth();
            float y = object.getRectangle().y / getTileHeight();
            if (XSpawn.equals(object.getName())) {
                player = playerFactory.createPlayer(x, y);
                playerPhysics = playerPhysicsFactory.create(player);
            }
            if (EnemySpawn.equals(object.getName())) {
                enemyList.add(new EnemyBase(x, y, this));
            }
        }

        // Create bounding boxes
        for (int y = 0; y < mapLayer.getHeight(); y++) {
            for (int x = 0; x < mapLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
                if (cell != null) {
                    bounds[x][y] = new Rectangle(x, y, 1, 1);
                }
            }
        }
    }

    public void update(float deltaTime) {
        playerPhysics.update(player, deltaTime, this);
        player.update(this, deltaTime);
        particles.update(deltaTime);
        Iterator<PlayerAttack> i = playerAttackList.iterator();
        while (i.hasNext()) {
            PlayerAttack attack = i.next();
            attack.update(player, deltaTime);
            playerPhysics.dealPlayerAttackDamage(attack, this);
            if (attack.shouldBeRemoved) {
                i.remove();
            }
        }

        Iterator<EnemyBase> j = enemyList.iterator();
        while (j.hasNext()) {
            EnemyBase enemy = j.next();
            enemy.update(deltaTime);
            if (enemy.shouldBeRemoved) {
                j.remove();
            }
        }
    }

    public void addPlayerAttack(PlayerAttack attack) {
        playerAttackList.addLast(attack);
        if (playerAttackList.size >= MAX_PLAYERATTACK) {
            playerAttackList.removeFirst();
        }
    }

    public Rectangle getCollidableBox(int x, int y) {
        if (x < 0 || y < 0 || x >= bounds.length || y >= bounds[0].length) {
            return new Rectangle(x, y, 1, 1);
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
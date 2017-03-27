package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Queue;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.animation.Particles;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class MapBase {
    public static int EMPTY = 0xffffff;
    public static int GROUND = 0x000000;
    public static int WALL = 0x0000A0;
    public static int START = 0xff0000;
    public static int DOOR = 0x00ffff;
    public static int ENEMY = 0xff00ff;
    public float GRAVITY = 15f;
    public float MAX_FALLSPEED = -8f;
    public float WALLSLIDE_FALLSPEED = -2f;
    private static int MAX_PLAYERATTACK = 20;

    private PlayerFactory playerFactory;
    private PlayerPhysicsFactory playerPhysicsFactory;
    public PlayerBase player;
    public PlayerPhysics playerPhysics;
    public int[][] tiles;
    public Rectangle[][] bounds;

    public List<EnemyBase> enemyList;
    public Queue<PlayerAttack> playerAttackList;

    protected TextureRegion ground;
    public abstract TextureRegion getGround();
    protected TextureRegion wall;
    public abstract TextureRegion getWall();

    public Particles particles;

    public MapBase(PlayerFactory playerFactory, PlayerPhysicsFactory playerPhysicsFactory) {
        this.playerFactory = playerFactory;
        this.playerPhysicsFactory = playerPhysicsFactory;
        particles = new Particles(20);
        enemyList = new ArrayList<EnemyBase>();
        playerAttackList = new Queue<PlayerAttack>(MAX_PLAYERATTACK);
        loadMap();
    }

    public int getTileWidth() {
        return getGround().getRegionWidth();
    }

    public int getTileHeight() {
        return getGround().getRegionHeight();
    }

    public int getWidth() { return tiles.length * getTileWidth(); }

    public int getHeight() { return tiles[0].length * getTileHeight(); }

    protected abstract Pixmap getMapResource();

    public abstract ParallaxBackground getBackground();

    private void loadMap() {
        Pixmap pixmap = getMapResource();
        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];
        bounds = new Rectangle[pixmap.getWidth()][pixmap.getHeight()];

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, pixmap.getHeight() - y - 1) >>> 8) & 0xffffff;

                if (match(pix, START)) {
                    // we load the player here
                    player = playerFactory.createPlayer(x, y);
                    playerPhysics = playerPhysicsFactory.create(player);
                }
                tiles[x][y] = pix;
                if (match(pix, GROUND) || match(pix, WALL)) {
                    // collision rectangles
                    bounds[x][y] = new Rectangle(x, y, 1, 1);
                }
                // basic enemy for testing
                if (match(pix, ENEMY)) {
                    enemyList.add(new EnemyBase(x, y));
                }
            }
        }
    }

    public boolean match (int src, int dst) {
        return src == dst;
    }

    public void update(float deltaTime) {
        playerPhysics.update(player, deltaTime, this);
        player.update(this);
        particles.update(deltaTime);
        for (PlayerAttack attack: playerAttackList) {
            attack.update(deltaTime);
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
}
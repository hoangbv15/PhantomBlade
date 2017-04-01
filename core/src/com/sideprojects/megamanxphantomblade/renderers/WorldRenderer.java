package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class WorldRenderer {
    MapBase map;
    OrthographicCamera gameCam;
    OrthographicCamera guiCam;
    private float camViewPortY = 540f;
    private ParallaxBackground background;
    private SpriteCache cache;
    private SpriteBatch batch;

    private int[][] blocks;
    private float blockHeight = 16f;
    private float blockWidth = 9f;

    private Vector3 lerpTarget;

    private PlayerRenderer playerRenderer;
    private PlayerHealthRenderer playerHealthRenderer;

    // Pre-calculated values to clamp camera position within the map's boundaries
    private float camViewportHalfX;
    private float camViewportHalfY;
    private float mapWidthMinusCamViewportHalfX;
    private float mapHeightMinusCamViewportHalfY;

    public WorldRenderer(MapBase map) {
        this.map = map;
        background = map.getBackground();
        batch = new SpriteBatch(5460);
        gameCam = new OrthographicCamera(camViewPortY * 16 / 9f, camViewPortY);
        guiCam = new OrthographicCamera(16, 9);
        guiCam.zoom = 0.4f;
        playerRenderer = new PlayerRenderer(map.player, map.getTileWidth(), gameCam, batch);
        playerHealthRenderer = new PlayerHealthRenderer(batch);
        cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        blocks = new int[(int)Math.ceil(this.map.tiles.length / blockWidth)][(int)Math.ceil(this.map.tiles[0].length / blockHeight)];
        lerpTarget = new Vector3();
        createBlocks();
        calculateCamClamps();
    }

    private void createBlocks() {
        int width = map.tiles.length;
        int height = map.tiles[0].length;

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * (int) blockHeight; y < blockY * blockHeight + blockHeight; y++) {
                    for (int x = blockX * (int) blockWidth; x < blockX * blockWidth + blockWidth; x++) {
                        if (x >= width || y >= height) continue;
                        int posX = x * map.getTileWidth();
                        int posY = y * map.getTileHeight();
                        if (map.match(map.tiles[x][y], MapBase.GROUND)){
                            cache.add(map.getGround(), posX, posY, map.getTileWidth(), map.getTileHeight());
                        }
                        else if (map.match(map.tiles[x][y], MapBase.WALL)){
                            cache.add(map.getWall(), posX, posY, map.getTileWidth(), map.getTileHeight());
                        }
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }
    }

    private Vector2 applyCameraLerp(Vector2 pos) {
        float playerYOffset = 1/5f * map.getTileHeight();
        Vector2 newPos = new Vector2(
                pos.x * map.getTileWidth(),
                pos.y * map.getTileHeight() - playerYOffset
        );
        return newPos;
    }

    public void render(float delta) {
        // Calculate vertical padding for player's position
        Vector2 pos = applyCameraLerp(map.player.pos);

        // Apply linear interpolation to the camera in order to smooth the camera movement
        gameCam.position.lerp(lerpTarget.set(pos.x, pos.y, 0), 0.5f);
        // Keep the camera within bounds
        gameCam.position.x = MathUtils.clamp(gameCam.position.x, camViewportHalfX, mapWidthMinusCamViewportHalfX);
        gameCam.position.y = MathUtils.clamp(gameCam.position.y, camViewportHalfY, mapHeightMinusCamViewportHalfY);
        gameCam.update();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        background.draw(gameCam, batch);
        batch.end();
        renderMap();
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        renderEnemies();
        playerRenderer.render(pos.x, pos.y, delta);
        renderPlayerAttack();
        renderParticles();
        renderGui(delta);
        batch.end();
        // Need to draw things that use ShapeRenderer last
        playerHealthRenderer.renderHealth(map.player.maxHealthPoints, map.player.healthPoints, map.player.isLowHealth(), delta);
    }

    private void renderGui(float delta) {
        // Need to flip y to render gui from top down
        guiCam.setToOrtho(true);
        batch.setProjectionMatrix(guiCam.combined);
        playerHealthRenderer.renderHealthContanier(map.player.maxHealthPoints);
    }

    private void renderMap() {
        cache.setProjectionMatrix(gameCam.combined);
        cache.begin();

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.draw(blocks[blockX][blockY]);
            }
        }

        cache.end();
    }

    private void renderEnemies() {
        for (EnemyBase enemy: map.enemyList) {
            Vector2 pos = applyCameraLerp(enemy.pos);
            batch.draw(enemy.currentFrame, pos.x, pos.y);
        }
    }

    private void renderPlayerAttack() {
        for (PlayerAttack attack: map.playerAttackList) {
            Vector2 pos = applyCameraLerp(attack.pos);
            batch.draw(attack.currentFrame, pos.x, pos.y);
            if (attack.muzzleFrame != null) {
                Vector2 muzzlePos = applyCameraLerp(attack.muzzlePos);
                batch.draw(attack.muzzleFrame, muzzlePos.x, muzzlePos.y);
            }
        }
    }

    private void renderParticles() {
        if (map.particles.size() == 0) {
            return;
        }

        for (int i = 0; i < map.particles.size(); i++) {
            Particle particle = map.particles.get(i);
            Vector2 pos = applyCameraLerp(particle.pos);
            batch.draw(particle.currentFrame, pos.x, pos.y);
        }
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
        playerRenderer.dispose();
        playerHealthRenderer.dispose();
    }

    public void resize(int width, int height) {
        float aspectRatio = width / (float) height;
        gameCam = new OrthographicCamera(camViewPortY * aspectRatio, camViewPortY);
        calculateCamClamps();
    }

    private void calculateCamClamps() {
        camViewportHalfX = gameCam.viewportWidth / 2;
        camViewportHalfY = gameCam.viewportHeight / 2;
        mapWidthMinusCamViewportHalfX = map.getWidth() - camViewportHalfX;
        mapHeightMinusCamViewportHalfY = map.getHeight() - camViewportHalfY;
    }
}

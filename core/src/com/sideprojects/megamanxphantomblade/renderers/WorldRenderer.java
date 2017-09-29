package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.ExplodeFragment;
import com.sideprojects.megamanxphantomblade.logging.Logger;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;
import com.sideprojects.megamanxphantomblade.renderers.shaders.DamagedShader;
import com.sideprojects.megamanxphantomblade.renderers.shaders.Shader;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class WorldRenderer implements Disposable {
    MapBase map;
    OrthographicCamera gameCam;
    OrthographicCamera guiCam;
    private float camViewPortY = 540f;
    private ParallaxBackground background;
    private SpriteBatch batch;

    private Shader damagedShader;

    private Vector3 lerpTarget;

    private PlayerRenderer playerRenderer;
    private PlayerHealthRenderer playerHealthRenderer;

    // Pre-calculated values to clamp camera position within the map's boundaries
    private float camViewportHalfX;
    private float camViewportHalfY;
    private float mapWidthMinusCamViewportHalfX;
    private float mapHeightMinusCamViewportHalfY;
    private float playerYOffset;

    private MapRenderer mapRenderer;

    public WorldRenderer(Logger logger, MapBase map) {
        this.map = map;
        background = map.getBackground();
        batch = new SpriteBatch(5460);
        gameCam = new OrthographicCamera(camViewPortY * 16 / 9f, camViewPortY);
        guiCam = new OrthographicCamera(16, 9);
        guiCam.zoom = 0.4f;
        damagedShader = new DamagedShader(logger);
        playerRenderer = new PlayerRenderer(logger, map.player, map.getTileWidth(), batch, damagedShader);
        playerHealthRenderer = new PlayerHealthRenderer(batch);
        lerpTarget = new Vector3();
        playerYOffset = 1/5f * map.getTileHeight();
        calculateCamClamps();
        mapRenderer = new OrthogonalTiledMapRenderer(map.tiledMap, batch);
    }

    private Vector2 applyCameraLerp(Vector2 pos) {
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
        // Keep the camera within mapCollisionBounds
        gameCam.position.x = MathUtils.clamp(gameCam.position.x, camViewportHalfX, mapWidthMinusCamViewportHalfX);
        gameCam.position.y = MathUtils.clamp(gameCam.position.y, camViewportHalfY, mapHeightMinusCamViewportHalfY);
        gameCam.update();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        background.draw(gameCam, batch, delta);
        batch.end();
        renderMap();
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        renderEnemies(delta);
        playerRenderer.render(pos.x, pos.y, delta);
        renderEnemyAttack();
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
        mapRenderer.setView(gameCam);
        mapRenderer.render();
    }

    private void renderEnemies(float delta) {
        for (EnemyBase enemy: map.enemyList) {
            if (!enemy.spawned) {
                continue;
            }
            Vector2 pos = applyCameraLerp(enemy.pos);
            if (enemy.currentFrame != null) {
                if (enemy.isTakingDamage) {
                    damagedShader.apply(batch);
                }
                Vector2 padding = enemy.animationPadding;
                batch.draw(enemy.currentFrame, pos.x + padding.x, pos.y + padding.y);
                if (enemy.isTakingDamage) {
                    batch.setShader(null);
                }
            }

            if (enemy.explodeFragments != null) {
                for (Object item: enemy.explodeFragments) {
                    ExplodeFragment fragment = (ExplodeFragment)item;
                    Vector2 fragmentPos = applyCameraLerp(fragment.pos);
                    Color color = batch.getColor();
                    color.a = 0.9f;
                    batch.setColor(color);
                    batch.draw(fragment.frame, fragmentPos.x, fragmentPos.y);
                    batch.setColor(Color.WHITE);
                }
            }

            if (enemy.getAuxiliaryFrames() != null) {
                for (Object key : enemy.getAuxiliaryFrames().keySet()) {
                    TextureRegion frame = (TextureRegion)enemy.getAuxiliaryFrames().get(key);
                    Vector2 padding = enemy.getAuxiliaryAnimationPadding((EnemyAnimationBase.Type) key, delta);
                    float x = pos.x + padding.x;
                    float y = pos.y + padding.y;
                    if (frame != null) {
                        batch.draw(frame, x, y);
                    }
                }
            }
        }
    }

    private void renderPlayerAttack() {
        for (PlayerAttack attack: map.playerAttackQueue) {
            Vector2 pos = applyCameraLerp(attack.pos);
            if (attack.muzzleFrame != null) {
                Vector2 muzzlePos = applyCameraLerp(attack.muzzlePos);
                batch.draw(attack.muzzleFrame, muzzlePos.x, muzzlePos.y);
            }
            if (attack.currentFrame != null) {
                batch.draw(attack.currentFrame, pos.x, pos.y);
            }
        }
    }

    private void renderEnemyAttack() {
        for (EnemyAttack attack: map.enemyAttackQueue) {
            Vector2 pos = applyCameraLerp(attack.pos);
            if (attack.currentFrame != null) {
                batch.draw(attack.currentFrame, pos.x, pos.y);
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

    @Override
    public void dispose() {
        batch.dispose();
        playerRenderer.dispose();
        playerHealthRenderer.dispose();
        damagedShader.dispose();
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

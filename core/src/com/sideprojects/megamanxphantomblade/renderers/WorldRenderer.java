package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.renderers.shaders.TraceShader;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class WorldRenderer {
    MapBase map;
    OrthographicCamera cam;
    private float camViewPortY = 540f;
    private ParallaxBackground background;
    private SpriteCache cache;
    private SpriteBatch batch;
    private ShaderProgram traceShader;

    private int[][] blocks;
    private float blockHeight = 16f;
    private float blockWidth = 9f;

    private Vector3 lerpTarget;

    private float playerYOffset;

    // Keep the last frames of the player to draw a trace
    private Queue<TextureRegion> lastPlayerFrameQueue;
    private Queue<Vector2> lastPlayerPositionQueue;
    // Number of traces to render
    private int numOfTraces = 5;
    // Number of frame skip per trace
    private int traceFrameSkip = 2;
    // Flag to indicate when to start removing traces
    private boolean startRemovingTraces;
    // private count towards number of frame skip
    private int traceFrameSkipCount = 0;

    // Pre-calculated values to clamp camera position within the map's boundaries
    private float camViewportHalfX;
    private float camViewportHalfY;
    private float mapWidthMinusCamViewportHalfX;
    private float mapHeightMinusCamViewportHalfY;

    // Parameters for rendering dash rockets
    private float leftDashRocketPadding;
    private float xDashRocketPadding;
    private float yDashRocketPadding;
    private float xUpDashRocketPadding;
    private float yUpDashRocketPadding;

    public WorldRenderer(MapBase map) {
        this.map = map;
        this.background = map.getBackground();
        this.cam = new OrthographicCamera(camViewPortY * 16 / 9f, camViewPortY);
        this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        this.batch = new SpriteBatch(5460);
        this.blocks = new int[(int)Math.ceil(this.map.tiles.length / blockWidth)][(int)Math.ceil(this.map.tiles[0].length / blockHeight)];
        lerpTarget = new Vector3();
        // Fixing the camera height for now.
        playerYOffset = 1/5f * map.getTileHeight();
        traceShader = TraceShader.getShaderColor(map.player.getTraceColour());
        lastPlayerFrameQueue = new Queue<TextureRegion>(numOfTraces);
        lastPlayerPositionQueue = new Queue<Vector2>(numOfTraces);
        startRemovingTraces = false;
        createBlocks();
        calculateCamClamps();
        // Calculate dash rocket padding
        leftDashRocketPadding = map.player.animations.get(PlayerAnimation.Type.Dash).getKeyFrame(0).getRegionWidth();
        xDashRocketPadding = map.player.animations.get(PlayerAnimation.Type.Dashrocket).getKeyFrame(0).getRegionWidth() / 5f;
        yDashRocketPadding = map.player.animations.get(PlayerAnimation.Type.Dashrocket).getKeyFrame(0).getRegionHeight() / 7f;
        xUpDashRocketPadding = map.player.animations.get(PlayerAnimation.Type.Updash).getKeyFrame(0).getRegionWidth() / 5f;
        yUpDashRocketPadding = map.player.animations.get(PlayerAnimation.Type.Updashrocket).getKeyFrame(0).getRegionHeight();
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
        Vector2 newPos = new Vector2(
                pos.x * map.getTileWidth(),
                pos.y * map.getTileHeight() - playerYOffset
        );
        return newPos;
    }

    public void render() {
        // Calculate vertical padding for player's position
        Vector2 pos = applyCameraLerp(map.player.pos);

        // Apply linear interpolation to the camera in order to smooth the camera movement
        cam.position.lerp(lerpTarget.set(pos.x, pos.y, 0), 0.5f);
        // Keep the camera within bounds
        cam.position.x = MathUtils.clamp(cam.position.x, camViewportHalfX, mapWidthMinusCamViewportHalfX);
        cam.position.y = MathUtils.clamp(cam.position.y, camViewportHalfY, mapHeightMinusCamViewportHalfY);
        cam.update();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        background.draw(cam, batch);
        batch.end();
        renderMap();
        renderPlayer(pos.x, pos.y);
        renderParticles();
    }

    private void renderMap() {
        cache.setProjectionMatrix(cam.combined);
        cache.begin();

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.draw(blocks[blockX][blockY]);
            }
        }

        cache.end();
    }

    private void renderParticles() {
        if (map.particles.size() == 0) {
            return;
        }

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for (int i = 0; i < map.particles.size(); i++) {
            Particle particle = map.particles.get(i);
            Vector2 pos = applyCameraLerp(particle.pos);
            batch.draw(particle.currentFrame, pos.x, pos.y);
        }
        batch.end();
    }

    // Pass posX and posY in so we don't have to recalculate them
    private void renderPlayer(float posX, float posY) {
        TextureRegion currentFrame = map.player.currentFrame;
        float originPosX = posX;
        if (map.player.direction == PlayerBase.RIGHT) {
            // Pad the texture's start x because the engine is drawing from left to right.
            // Without this the animation frames will be misaligned
            posX += map.getTileWidth() * 0.6f - currentFrame.getRegionWidth();
        }

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderPlayerTrace(currentFrame, posX, posY);
        if (map.player.state == PlayerState.DASH) {
            renderPlayerDashRocket(originPosX, posY);
        }
        if (map.player.state == PlayerState.UPDASH) {
            renderPlayerUpDashRocket(originPosX, posY);
        }
        batch.draw(currentFrame, posX, posY);
        batch.end();
    }

    private void renderPlayerDashRocket(float posX, float posY) {
        float y = posY - yDashRocketPadding;
        float x = posX;
        if (map.player.direction == MovingObject.RIGHT) {
            x -= map.player.currentDashRocketFrame.getRegionWidth() + xDashRocketPadding;
        } else {
            x += leftDashRocketPadding + xDashRocketPadding;
        }

        batch.draw(map.player.currentDashRocketFrame, x, y);
    }

    private void renderPlayerUpDashRocket(float posX, float posY) {
        float y = posY - yUpDashRocketPadding;
        float x = posX;
        if (map.player.direction == MovingObject.RIGHT) {
            x += xUpDashRocketPadding;
        }
        batch.draw(map.player.currentDashRocketFrame, x, y);
    }

    private void renderPlayerTrace(TextureRegion currentFrame, float posX, float posY) {
        if (traceFrameSkipCount != traceFrameSkip) {
            traceFrameSkipCount++;
        } else {
            if (startRemovingTraces) {
                lastPlayerFrameQueue.removeFirst();
                lastPlayerPositionQueue.removeFirst();
            }
            traceFrameSkipCount = 0;
            // If player is dashing, draw a trace
            if (map.player.state == PlayerState.DASH || map.player.state == PlayerState.UPDASH || map.player.isJumpDashing) {
                lastPlayerFrameQueue.addLast(currentFrame);
                lastPlayerPositionQueue.addLast(new Vector2(posX, posY));
            }
            if (lastPlayerFrameQueue.size == numOfTraces) {
                startRemovingTraces = true;
            }
        }

        if (map.player.state == PlayerState.DASH || map.player.state == PlayerState.UPDASH || map.player.isJumpDashing) {
            if (lastPlayerFrameQueue.size != numOfTraces) {
                startRemovingTraces = false;
            }
        } else {
            startRemovingTraces = true;
        }


        if (lastPlayerFrameQueue.size != 0) {
            for (int i = 0; i < lastPlayerFrameQueue.size; i++) {
                TextureRegion frame = lastPlayerFrameQueue.get(i);
                Vector2 position = lastPlayerPositionQueue.get(i);
                batch.setShader(traceShader);
                batch.draw(frame, position.x, position.y);
            }
        } else {
            startRemovingTraces = false;
        }
        batch.setShader(null);
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
        traceShader.dispose();
    }

    public void resize(int width, int height) {
        float aspectRatio = width / (float) height;
        cam = new OrthographicCamera(camViewPortY * aspectRatio, camViewPortY);
        calculateCamClamps();
    }

    private void calculateCamClamps() {
        camViewportHalfX = cam.viewportWidth / 2;
        camViewportHalfY = cam.viewportHeight / 2;
        mapWidthMinusCamViewportHalfX = map.getWidth() - camViewportHalfX;
        mapHeightMinusCamViewportHalfY = map.getHeight() - camViewportHalfY;
    }
}

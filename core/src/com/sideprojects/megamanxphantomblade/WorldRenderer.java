package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class WorldRenderer {
    public MapBase map;
    public OrthographicCamera cam;
    private float camFixedHeight;
    private SpriteCache cache;
    private SpriteBatch batch;

    private int[][] blocks;
    private float viewportHeight = 16f;
    private float viewportWidth = 9f;

    private Vector3 lerpTarget;

    private float playerYOffset;

    public WorldRenderer(MapBase map) {
        this.map = map;
        this.cam = new OrthographicCamera(960, 540);
        this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        this.batch = new SpriteBatch(5460);
        this.blocks = new int[(int)Math.ceil(this.map.tiles.length / viewportWidth)][(int)Math.ceil(this.map.tiles[0].length / viewportHeight)];
        lerpTarget = new Vector3();
        // Fixing the camera height for now.
        camFixedHeight = cam.viewportHeight/2;
        playerYOffset = 1/5f * map.getTileHeight();
        createBlocks();
    }

    private void createBlocks() {
        int width = map.tiles.length;
        int height = map.tiles[0].length;

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * (int) viewportHeight; y < blockY * viewportHeight + viewportHeight; y++) {
                    for (int x = blockX * (int) viewportWidth; x < blockX * viewportWidth + viewportWidth; x++) {
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

    public void render() {
        cam.position.lerp(lerpTarget.set(map.player.pos.x * map.getTileWidth(), camFixedHeight, 0), 1);
        cam.update();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        renderMap();
        renderPlayer();
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

    private void renderPlayer() {
        TextureRegion currentFrame = map.player.currentFrame;
        // Calculate vertical padding for player's position
        float posY = map.player.pos.y * map.getTileHeight() - playerYOffset;
        float posX = map.player.pos.x * map.getTileWidth();
        if (map.player.direction == PlayerBase.RIGHT) {
            // Pad the texture's start x because the engine is drawing from left to right.
            // Without this the animation frames will be misaligned
            posX += map.getTileWidth() * 0.6f - currentFrame.getRegionWidth();
        }
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        batch.draw(currentFrame, posX, posY);

        batch.end();
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
    }
}

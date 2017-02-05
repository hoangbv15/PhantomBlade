package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.sideprojects.megamanxphantomblade.map.MapBase;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class WorldRenderer {
    private MapBase map;
    private OrthographicCamera cam;
    private float camFixedHeight;
    private SpriteCache cache;
    private SpriteBatch batch;

    private int[][] blocks;
    private float viewportHeight = 18f;
    private float viewportWidth = 20f;
    private TextureRegion tile;

    private Vector3 lerpTarget;

    public WorldRenderer(MapBase map) {
        this.map = map;
        this.cam = new OrthographicCamera(960, 540);
        this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        this.batch = new SpriteBatch(5460);
        this.blocks = new int[(int)Math.ceil(this.map.tiles.length / viewportWidth)][(int)Math.ceil(this.map.tiles[0].length / viewportHeight)];
        lerpTarget = new Vector3();
        // Fixing the camera height for now.
        camFixedHeight = cam.viewportHeight/2;
        createBlocks();
    }

    private void createBlocks() {
        tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/ground.png")));
        int width = map.tiles.length;
        int height = map.tiles[0].length;

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * (int) viewportHeight; y < blockY * viewportHeight + viewportHeight; y++) {
                    for (int x = blockX * (int) viewportWidth; x < blockX * viewportWidth + viewportWidth; x++) {
                        if (x > width || y > height) continue;
                        if (map.match(map.tiles[x][y], MapBase.TILE)){
                            int posX = x * tile.getRegionWidth();
                            int posY = (height - y - 1) * tile.getRegionHeight();
                            cache.add(tile, posX, posY, tile.getRegionWidth(), tile.getRegionHeight());
                        }
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }

        Gdx.app.debug("Cubocy", "blocks created");
    }

    public void render() {
        cam.position.lerp(lerpTarget.set(map.player.pos.x * tile.getRegionWidth(), camFixedHeight, 0), 1);
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
        // Calculate vertical padding for player's position
        float posY = map.player.pos.y + tile.getRegionHeight() * 2 / 3;
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        TextureRegion currentFrame = map.player.currentFrame;

        batch.draw(currentFrame, map.player.pos.x * tile.getRegionWidth(), posY);

        batch.end();
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
    }
}

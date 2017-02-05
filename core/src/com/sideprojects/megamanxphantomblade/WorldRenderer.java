package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.sideprojects.megamanxphantomblade.animation.AnimationFactory;
import com.sideprojects.megamanxphantomblade.animation.XAnimationFactory;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

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

    private Animation<TextureRegion> playerRunRight;
    private Animation<TextureRegion> playerRunLeft;
    private Animation<TextureRegion> playerIdleRight;
    private Animation<TextureRegion> playerIdleLeft;

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

        createAnimations();
        createViewports();
    }

    private void createViewports() {
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
                            cache.add(tile, posX, posY);
                        }
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }

        Gdx.app.debug("Cubocy", "blocks created");
    }

    private void createAnimations() {
        AnimationFactory aniFactory = new XAnimationFactory();
        playerIdleLeft = aniFactory.getIdleLeft();
        playerIdleRight = aniFactory.getIdleRight();
        playerRunLeft = aniFactory.getRunLeft();
        playerRunRight = aniFactory.getRunRight();
    }

    public void render() {
        cam.position.lerp(lerpTarget.set(map.player.pos.x, camFixedHeight, 0), 1);
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
        // This animation object is dynamically assigned to the correct animation, based on the state of the game
        Animation<TextureRegion> animation = playerIdleLeft;
        boolean loop = true;

        // Calculate vertical padding for player's position
        float posY = map.player.pos.y + tile.getRegionHeight() * 2 / 3;
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        if (map.player.state == PlayerBase.IDLE) {
            if (map.player.direction == PlayerBase.LEFT) {
                animation = playerIdleLeft;
            } else {
                animation = playerIdleRight;
            }
            batch.draw(animation.getKeyFrame(map.player.stateTime, loop), map.player.pos.x, posY);
        } else if (map.player.state == PlayerBase.RUN) {
            if (map.player.direction == PlayerBase.LEFT) {
                animation = playerRunLeft;
            } else {
                animation = playerRunRight;
            }
            TextureRegion frame = animation.getKeyFrame(map.player.stateTime, loop);
            batch.draw(frame, map.player.pos.x, posY);
        }

        batch.end();
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
    }
}

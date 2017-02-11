package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class MapBase {
    public static int EMPTY = 0xffffff;
    public static int TILE = 0x000000;
    public static int START = 0xff0000;
    public static int DOOR = 0x00ffff;
    public float GRAVITY = 12f;
    public float MAX_FALLSPEED = -7f;

    private PlayerFactory playerFactory;
    public PlayerBase player;
    public int[][] tiles;
    public Rectangle[][] bounds;

    public TextureRegion ground;

    public MapBase(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
        ground = new TextureRegion(new Texture(Gdx.files.internal("sprites/ground.png")));
        loadMap();
    }

    protected abstract Pixmap getMapResource();

    private void loadMap() {
        Pixmap pixmap = getMapResource();
        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];
        bounds = new Rectangle[pixmap.getWidth()][pixmap.getHeight()];

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;

                if (match(pix, START)) {
                    // we create the player here
                    player = playerFactory.createPlayer(x, y);
                }
                tiles[x][y] = pix;
                if (match(pix, TILE)) {
                    // collision rectangles
                    bounds[x][y] = new Rectangle(x, y, 1, 1);
                }
            }
        }
    }

    public boolean match (int src, int dst) {
        return src == dst;
    }

    public void update(float deltaTime) {
        player.update(deltaTime, this);
    }

    public Rectangle getCollidableBox(int x, int y) {
        if (x < 0 || y < 0 || x >= bounds.length || y >= bounds[0].length) {
            return new Rectangle(x, y, 1, 1);
        }
        return bounds[x][y];
    }
}

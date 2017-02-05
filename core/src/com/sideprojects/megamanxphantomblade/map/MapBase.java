package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
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

    private PlayerFactory playerFactory;
    public PlayerBase player;
    public int[][] tiles;

    public MapBase(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
        loadMap();
    }

    protected abstract Pixmap getMapResource();

    private void loadMap() {
        Pixmap pixmap = getMapResource();
        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;

                if (match(pix, START)) {
                    // we create the player here
                    player = playerFactory.createPlayer(x, y);
                }
                tiles[x][y] = pix;
            }
        }
    }

    public boolean match (int src, int dst) {
        return src == dst;
    }

    public void update(float deltaTime) {
        player.update(deltaTime, this);
    }
}

package com.sideprojects.megamanxphantomblade.mocks;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.Difficulty;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class MockMap extends MapBase {
    private int maxX = 10;
    private int maxY = 10;

    public MockMap() {
        super(null, null, null, Difficulty.NORMAL);
        if (bounds == null || bounds.length < 1) {
            // Initialise to a 10x10 map
            bounds = new TileBase[maxX][maxY];
        }
    }

    public void addRectTile(int x, int y) {
        argumentCheck(x, y);
        bounds[x][y] = new RectangleTile(x, y, 1, 1);
    }

    public void addSlopeBottomRight(int x, int y) {
        argumentCheck(x, y);
        bounds[x][y] = new SquareTriangleTile(x, y, x + 1, y, x + 1, y + 0.5f, x, y, 0, 2);
        bounds[++x][y] = new SquareTriangleTile(x, y + 0.5f, x + 1, y + 0.5f, x + 1, y + 1, x, y + 0.5f, 1, 2);
    }

    private void argumentCheck(int x, int y) {
        if (x >= maxX || y >= maxY || x < 0 || y < 0) {
            throw new IllegalArgumentException(x + " and " + y + " are illegal dimensions");
        }
    }

    public void addEnemy(float x, float y) {
        enemyList.add(new MockEnemy(x, y, this));
    }

    public void setEnemyCanTakeDamage(boolean canTakeDamage) {
        enemyList.stream().forEach(enemyBase -> enemyBase.canTakeDamage = canTakeDamage);
    }

    @Override
    protected TiledMap getMapResource() {
        return new TiledMap();
    }

    @Override
    public ParallaxBackground getBackground() {
        return null;
    }
}

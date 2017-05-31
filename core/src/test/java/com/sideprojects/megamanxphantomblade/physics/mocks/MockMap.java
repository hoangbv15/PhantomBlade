package com.sideprojects.megamanxphantomblade.physics.mocks;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.sideprojects.megamanxphantomblade.Difficulty;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class MockMap extends MapBase {
    private int maxX = 10;
    private int maxY = 10;

    public MockMap() {
        super(null, null, null, Difficulty.Normal);
    }

    public void addRectTile(int x, int y, float width, float height) {
        if (x >= maxX || y >= maxY) {
            throw new NotImplementedException();
        }

        if (bounds == null || bounds.length < 1) {
            // Initialise to a 10x10 map
            bounds = new TileBase[maxX][maxY];
        }
        bounds[x][y] = new RectangleTile(x, y, width, height);
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

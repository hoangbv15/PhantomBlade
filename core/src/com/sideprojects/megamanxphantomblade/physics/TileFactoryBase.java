package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.maps.MapProperties;

/**
 * Created by buivuhoang on 28/01/18.
 */
public abstract class TileFactoryBase {
    public abstract TileBase getTile(MapProperties cellProperties, int x, int y);
}

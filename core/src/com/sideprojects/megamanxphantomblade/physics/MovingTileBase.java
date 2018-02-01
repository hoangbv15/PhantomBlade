package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 01/02/18.
 */
public abstract class MovingTileBase extends TileBase {
    public Texture frame;

    public abstract void update(float delta);
    public abstract Vector2 getPosition();
}

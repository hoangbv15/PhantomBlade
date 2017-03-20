package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 13/02/17.
 * Objects that move around in the map, with a position, velocity, direction,
 * bounding box.
 * These objects can collide with the map and with each other.
 */
public abstract class MovingObject {
    // Directions
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int NONEDIRECTION = 0;

    public int direction;
    public boolean grounded;

    public Vector2 pos;
    public Vector2 vel;
    public Rectangle bounds;

    public float stateTime;

    public void updatePos() {
        pos.x = bounds.x;
        pos.y = bounds.y;
    }
}

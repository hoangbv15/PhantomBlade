package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.sideprojects.megamanxphantomblade.math.NumberMath.numberIsBetween;

/**
 * Created by buivuhoang on 10/02/17.
 */
public class GeoMath {

    /**
     *  Finds intersection of the extended sides of a tile with a vector.
     */
    public static Vector2 findIntersectionLeft(Rectangle tile, Vector2 start, Vector2 end) {
        float x = tile.x;

        // Quickly fail if x is outside of range
        if (!numberIsBetween(x, start.x, end.x)) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.x - x) / (end.x - start.x);
        float y = (start.y - end.y) * ratio + end.y;

        // Check if y is outside of the tile
        if (!numberIsBetween(y, tile.y, tile.y + tile.height)) {
            return null;
        }

        // Ignore if the collision is on top, and the player is also on top
        if (start.y >= tile.y + tile.height && y >= tile.y + tile.height) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionRight(Rectangle tile, Vector2 start, Vector2 end) {
        float x = tile.x + tile.width;

        // Quickly fail if x is outside of range
        if (!numberIsBetween(x, start.x, end.x)) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.x - x) / (end.x - start.x);
        float y = (start.y - end.y) * ratio + end.y;

        // Check if y is outside of the tile
        if (!numberIsBetween(y, tile.y, tile.y + tile.height)) {
            return null;
        }

        // Ignore if the collision is on top, and the player is also on top
        if (start.y >= tile.y + tile.height && y >= tile.y + tile.height) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionDown(Rectangle tile, Vector2 start, Vector2 end) {
        float y = tile.y;

        // Quickly fail if x is outside of range
        if (!numberIsBetween(y, start.y, end.y)) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.y - y) / (end.y - start.y);
        float x = (start.x - end.x) * ratio + end.x;

        // Check if y is outside of the tile
        if (!numberIsBetween(x, tile.x, tile.x + tile.width)) {
            return null;
        }

        // Ignore if the collision is from outside of the tile's x range
        if ((start.x >= tile.x + tile.width && x >= tile.x + tile.width) ||
                (start.x <= tile.x && x <= tile.x)) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionUp(Rectangle tile, Vector2 start, Vector2 end) {
        float y = tile.y + tile.height;

        // Quickly fail if x is outside of range
        if (!numberIsBetween(y, start.y, end.y)) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.y - y) / (end.y - start.y);
        float x = (start.x - end.x) * ratio + end.x;

        // Check if y is outside of the tile
        if (!numberIsBetween(x, tile.x, tile.x + tile.width)) {
            return null;
        }

        // Ignore if the collision is from outside of the tile's x range
        if ((start.x >= tile.x + tile.width && x >= tile.x + tile.width) ||
                (start.x <= tile.x && x <= tile.x)) {
            return null;
        }

        return new Vector2(x, y);
    }

}

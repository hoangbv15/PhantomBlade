package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.physics.TileBase;

import static com.sideprojects.megamanxphantomblade.math.NumberMath.numberIsBetween;

/**
 * Created by buivuhoang on 10/02/17.
 */
public class GeoMathRectangle {

    /**
     *  Finds intersection of the extended sides of a tile with a vector.
     */
    public static Vector2 findIntersectionLeft(TileBase tile, Vector2 start, Vector2 end) {
        float x = tile.x();

        // Quickly fail if x is outside of range
        if (!numberIsBetween(x, start.x, end.x)) {
            return null;
        }
        // Ignore if the collision is on top, and the player is also on top
        if (start.x > x) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.x - x) / (end.x - start.x);
        float y = (start.y - end.y) * ratio + end.y;

        // Check if y is outside of the tile
        if (!numberIsBetween(y, tile.y(), tile.y() + tile.getHeight())) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionRight(TileBase tile, Vector2 start, Vector2 end) {
        float x = tile.x() + tile.getWidth();

        // Quickly fail if x is outside of range
        if (!numberIsBetween(x, start.x, end.x)) {
            return null;
        }
        // Ignore if the collision is on top, and the player is also on top
        if (start.x < x) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.x - x) / (end.x - start.x);
        float y = (start.y - end.y) * ratio + end.y;

        // Check if y is outside of the tile
        if (!numberIsBetween(y, tile.y(), tile.y() + tile.getHeight())) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionDown(TileBase tile, Vector2 start, Vector2 end) {
        float y = tile.y();

        // Quickly fail if x is outside of range
        if (!numberIsBetween(y, start.y, end.y)) {
            return null;
        }
        // Ignore if the collision is from outside of the tile's x range
        if (start.y > y) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.y - y) / (end.y - start.y);
        float x = (start.x - end.x) * ratio + end.x;

        // Check if y is outside of the tile
        if (!numberIsBetween(x, tile.x(), tile.x() + tile.getWidth())) {
            return null;
        }

        return new Vector2(x, y);
    }

    public static Vector2 findIntersectionUp(TileBase tile, Vector2 start, Vector2 end) {
        float y = tile.y() + tile.getHeight();

        // Quickly fail if y is outside of range
        if (!numberIsBetween(y, start.y, end.y)) {
            return null;
        }
        // Ignore if the collision is from outside of the tile's y range
        if (start.y < y) {
            return null;
        }

        // Find the y of the intersection
        float ratio = (end.y - y) / (end.y - start.y);
        float x = (start.x - end.x) * ratio + end.x;

        // Check if y is outside of the tile
        if (!numberIsBetween(x, tile.x(), tile.x() + tile.getWidth())) {
            return null;
        }

        return new Vector2(x, y);
    }

}

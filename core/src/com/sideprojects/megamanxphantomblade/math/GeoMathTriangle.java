package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;

import static com.sideprojects.megamanxphantomblade.math.NumberMath.numberIsBetween;

/**
 * Created by buivuhoang on 15/04/17.
 */
public class GeoMathTriangle {
    public static Vector2 findIntersectionDown(SquareTriangleTile tile, Vector2 start, Vector2 end) {
        // Ignore if the collision is from outside of the tile's x range
        if (start.y > tile.yTopHigher) {
            return null;
        }

        Vector2 intersect = new Vector2();
        Intersector.intersectLines(tile.xBottomLower, tile.yBottomLower, tile.xBottomHigher, tile.yBottomHigher, start.x, start.y, end.x, end.y, intersect);

        // Check if y is outside of the tile
        if (!numberIsBetween(intersect.x, tile.xCorner, tile.xHorizontal)) {
            return null;
        }

        if (!numberIsBetween(intersect.x, start.x, end.x) || !numberIsBetween(intersect.y, start.y, end.y)) {
            return null;
        }

        return intersect;
    }

    public static Vector2 findVertexIntersectionUp(SquareTriangleTile tile, Vector2 start, Vector2 end) {
//        // Calculate the angle of the speed vector
//        float angle = MathUtils.atan2(end.y - start.y, end.x - start.x);
//        // Compare this angle to the tile's angle
//        // If this angle is larger than the tile's angle, return null
//        if (angle > tile.getAngle()) {
//            return null;
//        }

        Vector2 intersect = findLineIntersectionUp(tile, start, end);

        if (intersect == null) {
            return null;
        }

        if (!numberIsBetween(intersect.x, start.x, end.x) || !numberIsBetween(intersect.y, start.y, end.y)) {
            return null;
        }

        return intersect;
    }

    public static Vector2 findLineIntersectionUp(SquareTriangleTile tile, Vector2 start, Vector2 end) {
        // Ignore if the collision is from outside of the tile's y range
        if (start.y < tile.yBottomLower) {
            return null;
        }

        Vector2 intersect = new Vector2();
        Intersector.intersectLines(tile.xTopLower, tile.yTopLower, tile.xTopHigher, tile.yTopHigher, start.x, start.y, end.x, end.y, intersect);

        // Check if y is outside of the tile
        if (!numberIsBetween(intersect.x, tile.xCorner, tile.xHorizontal)) {
            return null;
        }

        return intersect;
    }
}

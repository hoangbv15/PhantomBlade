package com.sideprojects.megamanxphantomblade.physics.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 11/02/17.
 */
public class CollisionDetectionRay {
    public enum Side {
        Back, Front
    }

    public enum Orientation {
        Horizontal, Vertical, Diagonal
    }

    private Rectangle start;
    private Vector2 end;
    private float paddingX;
    private float paddingY;
    public Side side;
    public Orientation orientation;

    public CollisionDetectionRay(Rectangle start, Vector2 end, float paddingX, float paddingY, Side side, Orientation orientation) {
        this.orientation = orientation;
        this.side = side;
        this.start = start;
        this.end = end;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
    }

    public Vector2 getStart() {
        return new Vector2(start.x + paddingX, start.y + paddingY);
    }

    public Vector2 getEnd() {
        return new Vector2(end.x + paddingX, end.y + paddingY);
    }

    public Vector2 getOrigin(Vector2 collisionPoint) {
        return new Vector2(collisionPoint.x - paddingX, collisionPoint.y - paddingY);
    }
}

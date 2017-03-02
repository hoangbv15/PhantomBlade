package com.sideprojects.megamanxphantomblade.physics.collision;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 11/02/17.
 */
public class CollisionDetectionRay {
    private Vector2 start;
    private Vector2 end;
    private float paddingX;
    private float paddingY;

    public CollisionDetectionRay(Vector2 start, Vector2 end, float paddingX, float paddingY) {
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

    Vector2 getOrigin(Vector2 collisionPoint) {
        return new Vector2(collisionPoint.x - paddingX, collisionPoint.y - paddingY);
    }
}

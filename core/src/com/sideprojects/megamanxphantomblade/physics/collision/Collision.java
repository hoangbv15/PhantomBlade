package com.sideprojects.megamanxphantomblade.physics.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by buivuhoang on 09/02/17.
 */
public class Collision {
    /**
     * The tile on the left of the colliding tile
     */
    protected final Rectangle tileLeft;

    /**
     * The tile on the right of the colliding tile
     */
    protected final Rectangle tileRight;

    /**
     * The point of the collision
      */
    public Vector2 point;

    /**
     * The side that the collision happens on the tile
     */
    public Side side;

    /**
     * The distance between this collision and the player's position before the movement
     */
    float dist;

    /**
     * The position of the map tile
     */
    public Rectangle tile;

    private CollisionDetectionRay ray;

    public Collision(Vector2 collidePoint, Side collideSide, CollisionDetectionRay ray, Rectangle tile, Rectangle tileLeft, Rectangle tileRight) {
        this.point = collidePoint;
        this.side = collideSide;
        if (collidePoint != null) {
            dist = collidePoint.dst(ray.getStart());
        }
        this.tile = tile;
        this.ray = ray;
        this.tileLeft = tileLeft;
        this.tileRight = tileRight;
    }

    public Collision(Vector2 collidePoint, Side collideSide, CollisionDetectionRay ray, Rectangle tile) {
        this(collidePoint, collideSide, ray, tile, null, null);
    }

    public Vector2 getPrecollidePos() {
        return ray.getOrigin(point);
    }

    public enum Side {
        Up, Down, Left, Right
    }

    public static Collision getCollisionNearestToStart(List<Collision> list, final Vector2 start) {
        Collision[] collisionArray = new Collision[list.size()];
        collisionArray = list.toArray(collisionArray);

        Arrays.sort(collisionArray, getComparatorByDistanceToStart());

        return collisionArray[0];
    }

    private static Comparator<Collision> getComparatorByDistanceToStart() {
        return new Comparator<Collision>() {
            @Override
            public int compare(Collision a, Collision b) {
                return Float.compare(a.dist, b.dist);
            }
        };
    }
}


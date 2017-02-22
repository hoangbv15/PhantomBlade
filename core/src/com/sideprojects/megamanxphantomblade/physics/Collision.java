package com.sideprojects.megamanxphantomblade.physics;

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
     * The point of the collision
      */
    Vector2 point;

    /**
     * The side that the collision happens on the tile
     */
    public Side side;

    /**
     * The distance between this collision and the player's position before the movement
     */
    private float dist;

    /**
     * The position of the map tile
     */
    public Rectangle tile;

    private CollisionDetectionRay ray;

    Collision(Vector2 collidePoint, Side collideSide, CollisionDetectionRay ray, Rectangle tile) {
        this.point = collidePoint;
        this.side = collideSide;
        if (collidePoint != null) {
            dist = collidePoint.dst(ray.getStart());
        }
        this.tile = tile;
        this.ray = ray;
    }

    public Vector2 getPrecollidePos() {
        return ray.getOrigin(point);
    }

    public enum Side {
        UP, DOWN, LEFT, RIGHT
    }

    static Collision getCollisionNearestToStart(List<Collision> list, final Vector2 start) {
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


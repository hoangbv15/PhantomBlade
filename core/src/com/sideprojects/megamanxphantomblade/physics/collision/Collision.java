package com.sideprojects.megamanxphantomblade.physics.collision;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.TileBase;

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
    protected final TileBase tileLeft;

    /**
     * The tile on the right of the colliding tile
     */
    protected final TileBase tileRight;

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
    public TileBase tile;

    /**
     * The ray used to detect this collision
     */
    public CollisionDetectionRay ray;

    /**
     * The source moving object colliding with the tile
     */
    public MovingObject object;

    public Collision(MovingObject object, Vector2 collidePoint, Side collideSide, CollisionDetectionRay ray, TileBase tile, TileBase tileLeft, TileBase tileRight) {
        this.object = object;
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

    public Collision(MovingObject object, Vector2 collidePoint, Side collideSide, CollisionDetectionRay ray, TileBase tile) {
        this(object, collidePoint, collideSide, ray, tile, null, null);
    }

    public Vector2 getPostCollidePos() {
        return tile.getPostCollisionPos(this);
    }

    public enum Side {
        Up, Down, Left, Right,
        LeftSlippery, RightSlippery, // Cannot do wallslide
        UpRamp, // Behave exactly like Up, but at an angle
        None
    }

    public static Collision getCollisionNearestToStart(List<Collision> list) {
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


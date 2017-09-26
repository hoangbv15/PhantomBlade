package com.sideprojects.megamanxphantomblade.physics.tiles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.math.GeoMathRectangle;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 15/04/17.
 */
public class RectangleTile extends TileBase {
    private Rectangle tile;
    private float[] vertices;
    private TileBase leftTile;
    private TileBase rightTile;
    private boolean slippery;

    public RectangleTile(float x, float y, float width, float height, boolean slippery) {
        tile = new Rectangle(x, y, width, height);
        this.slippery = slippery;
        vertices = new float[] {x, y, x + width, y, x + width, y + height, x, y + height};
    }

    public RectangleTile(float x, float y, float width, float height) {
        this(x, y, width, height, false);
    }

    @Override
    public float x() {
        return tile.x;
    }

    @Override
    public float y() {
        return tile.y;
    }

    @Override
    public float getHeight() {
        return tile.getHeight();
    }

    @Override
    public float getWidth() {
        return tile.getWidth();
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public Collision getCollisionWithTile(MovingObject object, CollisionDetectionRay ray,
                                                              TileBase tileUp,
                                                              TileBase tileDown,
                                                              TileBase tileLeft,
                                                              TileBase tileRight,
                                                              TileBase tileTopLeft,
                                                              TileBase tileTopRight,
                                                              TileBase tileBottomLeft,
                                                              TileBase tileBottomRight,
                                                              boolean overlapMode) {
        if (tileTopLeft != null && tileTopLeft instanceof SquareTriangleTile) {
            leftTile = tileTopLeft;
        } else if (tileBottomLeft != null && tileBottomLeft instanceof SquareTriangleTile) {
            leftTile = tileBottomLeft;
        } else {
            leftTile = tileLeft;
        }
        if (tileTopRight != null && tileTopRight instanceof SquareTriangleTile) {
            rightTile = tileTopRight;
        } else if (tileBottomRight != null && tileBottomRight instanceof SquareTriangleTile) {
            rightTile = tileBottomRight;
        } else {
            rightTile = tileRight;
        }



        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();

        // If we just wants to check collision from overlapping
        if (overlapMode) {
            if (tile.contains(start)) {
                return new Collision(object, start, Collision.Side.None, ray, this);
            } else {
                return null;
            }
        }

        // Put non-null ones in an array, then sort by distance to start
        // A line can only have at most 2 intersections with a rectangle
        List<Collision> collisionList = new ArrayList<>(2);

        // Find intersection on each side of the tile
        if (shouldThereBeCollisionWithSideTile(this, tileLeft)) {
            Collision left = new Collision(object, GeoMathRectangle.findIntersectionLeft(this, start, end), slippery ? Collision.Side.LeftSlippery : Collision.Side.Left, ray, this);
            if (left.point != null) collisionList.add(left);
        }
        if (shouldThereBeCollisionWithSideTile(this, tileRight)) {
            Collision right = new Collision(object, GeoMathRectangle.findIntersectionRight(this, start, end), slippery ? Collision.Side.RightSlippery : Collision.Side.Right, ray, this);
            if (right.point != null) collisionList.add(right);
        }
        if (tileUp == null) {
            Collision up = new Collision(object, GeoMathRectangle.findIntersectionUp(this, start, end), Collision.Side.Up, ray, this, tileLeft, tileRight);
            if (up.point != null) collisionList.add(up);
        }
        if (tileDown == null) {
            Collision down = new Collision(object, GeoMathRectangle.findIntersectionDown(this, start, end), Collision.Side.Down, ray, this);
            if (down.point != null) collisionList.add(down);
        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList);
    }

    @Override
    public Vector2 getPostCollisionPos(Collision collision) {
        if (collision.side == Collision.Side.Up) {
            Vector2 finalPos = collision.ray.getOrigin(collision.point);
            MovingObject object = collision.object;
            if (object.horizontalRay != null) {
                TileBase nextTile = object.direction == MovingObject.LEFT ? leftTile : rightTile;
                if (nextTile instanceof SquareTriangleTile) {
                    int direction = object.movingDirection();
                    if (direction == ((SquareTriangleTile)nextTile).upDirection) {
                        finalPos.y = calculateFinalY(object.horizontalRay.getEnd().x, nextTile);
                    } else {
                        finalPos.y = calculateFinalY(object.horizontalRay.getEnd().x - object.mapCollisionBounds.getWidth() * direction, nextTile);
                    }
                }
            }
            return finalPos;
        }
        return collision.ray.getOrigin(collision.point);
    }

    @Override
    public float getYPositionIfStandingOnTile(float x) {
        return y() + getHeight();
    }

    private boolean shouldThereBeCollisionWithSideTile(TileBase thisTile, TileBase otherTile) {
        return otherTile == null || (thisTile.y() + thisTile.getHeight()) > (otherTile.y() + otherTile.getHeight());
    }
}

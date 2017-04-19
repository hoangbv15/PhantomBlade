package com.sideprojects.megamanxphantomblade.physics.tiles;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.math.GeoMathRectangle;
import com.sideprojects.megamanxphantomblade.math.GeoMathTriangle;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 15/04/17.
 */
public class SquareTriangleTile extends TileBase {
    public enum SquareAngle {
        BottomLeft, BottomRight, TopLeft, TopRight
    }

    private Polygon tile;
    private float x;
    private float y;
    private float height;
    private float width;
    private SquareAngle squareAngle;

    public float xCorner;
    public float yCorner;
    public float xVertical;
    public float yVertical;
    public float xHorizontal;
    public float yHorizontal;


    // Top vertex
    public float xTopLower;
    public float yTopLower;
    public float xTopHigher;
    public float yTopHigher;

    // Bottom vertex
    public float xBottomLower;
    public float yBottomLower;
    public float xBottomHigher;
    public float yBottomHigher;

    public SquareTriangleTile(float x, float y, float xCorner, float yCorner, float xVertical, float yVertical, float xHorizontal, float yHorizontal) {
        this.x = x;
        this.y = y;
        this.xCorner = xCorner;
        this.yCorner = yCorner;
        this.xVertical = xVertical;
        this.yVertical = yVertical;
        this.xHorizontal = xHorizontal;
        this.yHorizontal = yHorizontal;

        height = Math.abs(yVertical - yCorner);
        width = Math.abs(xHorizontal - xCorner);

        if (xCorner == x && yCorner == y) {
            squareAngle = SquareAngle.BottomLeft;
            xTopLower = xHorizontal;
            yTopLower = yHorizontal;
            xTopHigher = xVertical;
            yTopHigher = yVertical;
            xBottomLower = xCorner;
            yBottomLower = yCorner;
            xBottomHigher = xTopLower;
            yBottomHigher = yTopLower;
        } else if (xCorner > x && yCorner == y) {
            squareAngle = SquareAngle.BottomRight;
            xTopLower = xHorizontal;
            yTopLower = yHorizontal;
            xTopHigher = xVertical;
            yTopHigher = yVertical;
            xBottomLower = xTopLower;
            yBottomLower = yTopLower;
            xBottomHigher = xCorner;
            yBottomHigher = yCorner;
        } else if (xCorner == x && yCorner > y) {
            squareAngle = SquareAngle.TopLeft;
            xTopLower = xCorner;
            yTopLower = yCorner;
            xTopHigher = xHorizontal;
            yTopHigher = yHorizontal;
            xBottomLower = xVertical;
            yBottomLower = yVertical;
            xBottomHigher = xTopHigher;
            yBottomHigher = yTopHigher;
        } else {
            squareAngle = SquareAngle.TopRight;
            xTopLower = xHorizontal;
            yTopLower = yHorizontal;
            xTopHigher = xCorner;
            yTopHigher = yCorner;
            xBottomLower = xVertical;
            yBottomLower = yVertical;
            xBottomHigher = xTopLower;
            yBottomHigher = yTopLower;
        }

        float[] vertices = new float[] {
                xCorner, yCorner, xVertical, yVertical, xHorizontal, yHorizontal
        };
        tile = new Polygon(vertices);
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float[] getVertices() {
        return tile.getVertices();
    }

    @Override
    public Collision getCollisionWithTile(CollisionDetectionRay ray, TileBase tileUp, TileBase tileDown, TileBase tileLeft, TileBase tileRight, boolean overlapMode) {
        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();

        // If we just wants to check collision from overlapping
        if (overlapMode) {
            if (tile.contains(start)) {
                return new Collision(start, Collision.Side.None, ray, this);
            } else {
                return null;
            }
        }

        // Put non-null ones in an array, then sort by distance to start
        // A line can only have at most 2 intersections with a rectangle
        List<Collision> collisionList = new ArrayList<Collision>(2);

        // Find intersection on each side of the tile
//        if (squareAngle != SquareAngle.BottomRight && squareAngle != SquareAngle.TopRight &&
//                shouldThereBeCollisionWithSideTile(this, tileLeft)) {
//            Collision left = new Collision(GeoMathRectangle.findIntersectionLeft(this, start, end), Collision.Side.Left, ray, this);
//            if (left.point != null) collisionList.add(left);
//        }
//        if (squareAngle != SquareAngle.BottomLeft && squareAngle != SquareAngle.TopLeft &&
//                shouldThereBeCollisionWithSideTile(this, tileRight)) {
//            Collision right = new Collision(GeoMathRectangle.findIntersectionRight(this, start, end), Collision.Side.Right, ray, this);
//            if (right.point != null) collisionList.add(right);
//        }
        if (squareAngle != SquareAngle.TopLeft && squareAngle != SquareAngle.TopRight &&
                tileUp == null) {
            Collision up = new Collision(GeoMathTriangle.findIntersectionUp(this, start, end), Collision.Side.UpRamp, ray, this, tileLeft, tileRight);
            if (up.point != null) collisionList.add(up);
        }
//        if (squareAngle != SquareAngle.BottomLeft && squareAngle != SquareAngle.BottomRight &&
//                tileDown == null) {
//            Collision down = new Collision(GeoMathTriangle.findIntersectionDown(this, start, end), Collision.Side.Down, ray, this);
//            if (down.point != null) collisionList.add(down);
//        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList, start);
    }

    @Override
    public void postCollisionProcessing(MovingObject object, Collision collision, float delta) {
        if (collision.side == Collision.Side.UpRamp) {
            object.grounded = true;
            object.mapCollisionBounds.y = collision.getPrecollidePos().y;
            object.vel.y = 0;
            if (object.vel.x != 0) {
//                object.vel.x *= MathUtils.cos(45 * MathUtils.degreesToRadians);
//                object.vel.y = object.vel.x * MathUtils.sin(45 * MathUtils.degreesToRadians) / MathUtils.cos(45 * MathUtils.degreesToRadians);
                object.mapCollisionBounds.y += (object.vel.x - Math.abs(collision.point.x - object.mapCollisionBounds.x)) * delta;
                System.out.println("upramp detected " + object.mapCollisionBounds.y);
            }
        }
    }

    private boolean shouldThereBeCollisionWithSideTile(TileBase thisTile, TileBase otherTile) {
        return otherTile == null || thisTile.getHeight() > otherTile.getHeight();
    }
}

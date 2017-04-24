package com.sideprojects.megamanxphantomblade.physics.tiles;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
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

    private TileBase tileTopLeft;
    private TileBase tileTopRight;
    private TileBase tileBottomLeft;
    private TileBase tileBottomRight;

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
    public Collision getCollisionWithTile(MovingObject object, CollisionDetectionRay ray,
                                          TileBase tileUp,
                                          TileBase tileDown,
                                          TileBase tileLeft,
                                          TileBase tileRight,
                                          TileBase tileTopLeft,
                                          TileBase tileTopRight,
                                          TileBase tileBottomLeft,
                                          TileBase tileBottomLRight,
                                          boolean overlapMode) {
        this.tileTopLeft = tileTopLeft;
        this.tileTopRight = tileTopRight;
        this.tileBottomLeft = tileBottomLeft;
        this.tileBottomRight = tileBottomLRight;

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
                ray.orientation != CollisionDetectionRay.Orientation.Horizontal &&
                tileUp == null) {
            Collision up = new Collision(object, GeoMathTriangle.findVertexIntersectionUp(this, start, end), Collision.Side.UpRamp, ray, this, tileBottomLeft, tileTopRight);
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

        return Collision.getCollisionNearestToStart(collisionList);
    }

    private boolean shouldThereBeCollisionWithSideTile(TileBase thisTile, TileBase otherTile) {
        return otherTile == null || thisTile.getHeight() > otherTile.getHeight();
    }

    @Override
    public Vector2 getPostCollisionPos(Collision collision) {
        CollisionDetectionRay ray = collision.ray;
        MovingObject object = collision.object;
        Vector2 finalPos = ray.getOrigin(collision.point);

        // Filter out which ray can be used for the direction of the player and the triangle
        if (squareAngle == SquareAngle.BottomRight) {
            // player entering from left to right
            if (object.direction == MovingObject.RIGHT && ray.side == CollisionDetectionRay.Side.Front) {
                switch (ray.orientation) {
                    case Diagonal:
                        float finalX = ray.getEnd().x;
                        finalPos.y = calculateFinalY(finalX, tileTopRight);
                        break;
                }
            }
            // player entering from right to left
            else if (object.direction == MovingObject.LEFT && ray.side == CollisionDetectionRay.Side.Back && object.vel.x != 0) {
                // This case needs to be handled differently since only the vertical back ray is detecting the collision
                // Find the intersection between the extended horizontal line with the triangle upside
                // TODO: Java 7 has no lambdas :(
                CollisionDetectionRay horizontalRay = null;
                for (CollisionDetectionRay r: object.detectionRayList) {
                    if (r.orientation == CollisionDetectionRay.Orientation.Horizontal) {
                        horizontalRay = r;
                        break;
                    }
                }

                if (horizontalRay != null) {
                    Vector2 intersection = GeoMathTriangle.findLineIntersectionUp(this, horizontalRay.getStart(), horizontalRay.getEnd());
                    if (intersection != null) {
                        float finalX = horizontalRay.getOrigin(horizontalRay.getEnd()).x + object.mapCollisionBounds.getWidth();
                        finalPos.y = calculateFinalY(finalX, tileBottomLeft);
                    }
                }
            }
        } else if (squareAngle == SquareAngle.BottomLeft) {
        } else {
            return finalPos;
        }

        return finalPos;
    }

    @Override
    public float getYPositionIfStandingOnTile(float x) {
        switch (squareAngle) {
            case BottomRight:
                return y() + (x - (int)x);
            default:
                return y() + (x - (int)x);
        }
    }

    private float calculateFinalY(float finalX, TileBase nextTile) {
        float finalY;
        int xDifference = (int)finalX - (int)x();
        float rightSideOfFloatingX = finalX - (int)finalX;
        if (xDifference != 0) {
            if (nextTile != null) {
                finalY = nextTile.getYPositionIfStandingOnTile(finalX);
            } else {
                finalY = y() + xDifference;
            }
        } else {
            finalY = y() + rightSideOfFloatingX;
        }
        return finalY;
    }
}

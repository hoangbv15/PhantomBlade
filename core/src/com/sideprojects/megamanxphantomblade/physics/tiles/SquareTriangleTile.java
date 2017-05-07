package com.sideprojects.megamanxphantomblade.physics.tiles;

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
    private float tanAngle;

    private TileBase leftTile;
    private TileBase rightTile;

    public float xCorner;
    public float yCorner;
    public float xVertical;
    public float yVertical;
    public float xHorizontal;
    public float yHorizontal;

    public int upDirection;

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

    // Index of current tile and total number of tiles that lie in a row, before incrementing Y
    private int index;
    private int total;

    public SquareTriangleTile(float x, float y, float xCorner, float yCorner, float xVertical, float yVertical, float xHorizontal, float yHorizontal, int index, int total) {
        this.x = x;
        this.y = y;
        this.xCorner = xCorner;
        this.yCorner = yCorner;
        this.xVertical = xVertical;
        this.yVertical = yVertical;
        this.xHorizontal = xHorizontal;
        this.yHorizontal = yHorizontal;
        this.index = index;
        this.total = total;

        height = Math.abs(yVertical - yCorner);
        width = Math.abs(xHorizontal - xCorner);
        tanAngle = height;

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
            upDirection = MovingObject.LEFT;
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
            upDirection = MovingObject.RIGHT;
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
            upDirection = MovingObject.NONEDIRECTION;
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
            upDirection = MovingObject.NONEDIRECTION;
        }

        float[] vertices = new float[] {
                xCorner, yCorner, xHorizontal, yHorizontal, xVertical, yVertical
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
                                          TileBase tileBottomRight,
                                          boolean overlapMode) {
        if (squareAngle == SquareAngle.BottomRight) {
            if (index > 0) {
                leftTile = tileLeft;
            } else {
                leftTile = tileBottomLeft;
            }
            if (index < total - 1 || !(tileTopRight instanceof SquareTriangleTile)) {
                rightTile = tileRight;
            } else {
                rightTile = tileTopRight;
            }
        } else if (squareAngle == SquareAngle.BottomLeft) {
            if (index > 0) {
                rightTile = tileRight;
            } else {
                rightTile = tileBottomRight;
            }
            if (index < total - 1 || !(tileTopLeft instanceof SquareTriangleTile)) {
                leftTile = tileLeft;
            } else {
                leftTile = tileTopLeft;
            }
        }

        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();
        int direction = object.movingDirection();

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
        if (squareAngle != SquareAngle.BottomRight && squareAngle != SquareAngle.TopRight &&
                shouldThereBeCollisionWithSideTile(this, tileLeft)) {
            Collision left = new Collision(object, GeoMathRectangle.findIntersectionLeft(this, start, end), Collision.Side.Left, ray, this);
            if (left.point != null) collisionList.add(left);
        }
        if (squareAngle != SquareAngle.BottomLeft && squareAngle != SquareAngle.TopLeft &&
                shouldThereBeCollisionWithSideTile(this, tileRight)) {
            Collision right = new Collision(object, GeoMathRectangle.findIntersectionRight(this, start, end), Collision.Side.Right, ray, this);
            if (right.point != null) collisionList.add(right);
        }
        if ((squareAngle == SquareAngle.BottomLeft || squareAngle == SquareAngle.BottomRight) && tileUp == null &&
                ((direction == upDirection && ray.side == CollisionDetectionRay.Side.Front && (ray.orientation == CollisionDetectionRay.Orientation.Diagonal || object.diagonalRay == null)) ||
                        (direction != upDirection && ray.side == CollisionDetectionRay.Side.Back)
                )
            ) {
            Collision up = new Collision(object, GeoMathTriangle.findVertexIntersectionUp(this, start, end), Collision.Side.UpRamp, ray, this, leftTile, rightTile);
            if (up.point != null) collisionList.add(up);
        }
        if ((squareAngle == SquareAngle.TopLeft || squareAngle == SquareAngle.TopRight) && tileDown == null &&
                object.vel.y >= 0) {
            Collision down = new Collision(object, GeoMathTriangle.findIntersectionDown(this, start, end), Collision.Side.Down, ray, this);
            if (down.point != null) {
                collisionList.add(down);
            }
        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList);
    }

    private boolean shouldThereBeCollisionWithSideTile(TileBase thisTile, TileBase otherTile) {
        return otherTile == null || (thisTile.y() + thisTile.getHeight()) > (otherTile.y() + otherTile.getHeight());
    }

    @Override
    public Vector2 getPostCollisionPos(Collision collision) {
        CollisionDetectionRay ray = collision.ray;
        Vector2 finalPos = ray.getOrigin(collision.point);
        MovingObject object = collision.object;

        if (collision.side == Collision.Side.UpRamp && object.vel.x != 0) {
            int direction = object.movingDirection();
            TileBase nextTile = direction == MovingObject.LEFT ? leftTile : rightTile;
            if (direction == upDirection && ray.side == CollisionDetectionRay.Side.Front) {
                switch (ray.orientation) {
                    case Horizontal:
                    case Diagonal:
                        float finalX = ray.getEnd().x;
                        finalPos.y = calculateFinalY(finalX, nextTile);
                        break;
                }
            }
            // player entering from right to left
            else if (direction != upDirection && ray.side == CollisionDetectionRay.Side.Back) {
                // This case needs to be handled differently since only the vertical back ray is detecting the collision
                // Find the intersection between the extended horizontal line with the triangle upside
                if (object.horizontalRay != null) {
                    float finalX = object.horizontalRay.getOrigin(object.horizontalRay.getEnd()).x;
                    if (squareAngle == SquareAngle.BottomRight) {
                        finalX -= object.mapCollisionBounds.getWidth() * direction;
                    }
                    finalPos.y = calculateFinalY(finalX, nextTile);
                }
            }
        }

        return finalPos;
    }

    @Override
    public float getYPositionIfStandingOnTile(float x) {
        switch (squareAngle) {
            case BottomRight:
                return y() + (x - (int)x) * getTanAngle();
            case BottomLeft:
                return y() + ((int)x + 1 - x) * getTanAngle();
            default:
                // Invalid case
                return -1;
        }
    }

    public float getTanAngle() {
        return tanAngle;
    }
}

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
    public RectangleTile(float x, float y, float width, float height) {
        tile = new Rectangle(x, y, width, height);
        vertices = new float[] {x, y, x + width, y, x + width, y + height, x, y + height};
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
        if (shouldThereBeCollisionWithSideTile(this, tileLeft)) {
            Collision left = new Collision(GeoMathRectangle.findIntersectionLeft(this, start, end), Collision.Side.Left, ray, this);
            if (left.point != null) collisionList.add(left);
        }
        if (shouldThereBeCollisionWithSideTile(this, tileRight)) {
            Collision right = new Collision(GeoMathRectangle.findIntersectionRight(this, start, end), Collision.Side.Right, ray, this);
            if (right.point != null) collisionList.add(right);
        }
        if (tileUp == null) {
            Collision up = new Collision(GeoMathRectangle.findIntersectionUp(this, start, end), Collision.Side.Up, ray, this, tileLeft, tileRight);
            if (up.point != null) collisionList.add(up);
        }
        if (tileDown == null) {
            Collision down = new Collision(GeoMathRectangle.findIntersectionDown(this, start, end), Collision.Side.Down, ray, this);
            if (down.point != null) collisionList.add(down);
        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList, start);
    }

    @Override
    public void postCollisionProcessing(MovingObject object, Collision collision, float delta) {
        // No need to do anything
    }

    private boolean shouldThereBeCollisionWithSideTile(TileBase thisTile, TileBase otherTile) {
        return otherTile == null || thisTile.getHeight() > otherTile.getHeight();
    }
}

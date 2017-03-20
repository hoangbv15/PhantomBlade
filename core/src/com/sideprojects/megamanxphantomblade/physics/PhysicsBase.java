package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.enemies.EnemyDamage;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.GeoMath;
import com.sideprojects.megamanxphantomblade.math.NumberMath;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 21/02/17.
 */
public abstract class PhysicsBase {
    // Debug property, used for rendering collisions to the screen. Needs to be public
    public CollisionList collisions;

    protected InputProcessor input;

    public PhysicsBase(InputProcessor input) {
        this.input = input;
        collisions = new CollisionList(new ArrayList<Collision>());
    }

    public CollisionList getMapCollision(MovingObject object, float deltaTime, MapBase map) {
        int direction = object.direction;
        Vector2 vel = object.vel;
        Rectangle bounds = object.bounds;

        collisions.toList.clear();
        // From inside out, find the first tile that collides with the player
        float stepX = vel.x * deltaTime;
        float stepY = vel.y * deltaTime;
        // Translate the start and end vectors depending on the direction of the movement
        float paddingX = 0;
        float paddingY = 0;
        if (stepX > 0) {
            paddingX = bounds.width;
        }
        if (stepY > 0) {
            paddingY = bounds.height;
        }
        Vector2 endPosX = new Vector2(bounds.x + stepX, bounds.y);
        Vector2 endPosY = new Vector2(bounds.x, bounds.y + stepY);
        Vector2 endPos = new Vector2(bounds.x + stepX, bounds.y + stepY);

        // Setup collision detection rays
        List<CollisionDetectionRay> detectionRayList = new ArrayList<CollisionDetectionRay>(5);
        detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, 0, paddingY));
        detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, bounds.width, paddingY));
        if (vel.x != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, 0));
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, bounds.height));
        }
        if (vel.x != 0 && vel.y != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPos, paddingX, paddingY));
        }

        // Loop through map and use collision detection rays to detect...well..collisions.
        int xStart = (int)bounds.x;
        int yStart = (int)bounds.y;
        if (vel.y <= 0) {
            yStart += 1;
        }
        if (direction == MovingObject.LEFT) {
            xStart += 1;
        }
        int xEnd = (int)(endPosX.x + paddingX);
        if (direction == MovingObject.RIGHT) {
            xEnd += 1;
        }
        int yEnd = (int)(endPosY.y + paddingY);

        // Loop through the rectangular area that the speed vector occupies
        // Get a list of all collisions with map tiles in the area
        // Identify the collision nearest to the player
        // Player has at most 2 collisions with the map at the same time
        List<Collision> collisionList = new ArrayList<Collision>(2);
        for (int y = yStart; NumberMath.hasNotExceeded(y, yStart, yEnd); y = NumberMath.iteratorNext(y, yStart, yEnd)) {
            for (int x = xStart; NumberMath.hasNotExceeded(x, xStart, xEnd); x = NumberMath.iteratorNext(x, xStart, xEnd)) {
                Rectangle tile = map.getCollidableBox(x, y);
                if (tile == null) {
                    continue;
                }
                // Get the tiles surrounding this one
                Rectangle tileUp = map.getCollidableBox(x, y + 1);
                Rectangle tileDown = map.getCollidableBox(x, y - 1);
                Rectangle tileLeft = map.getCollidableBox(x - 1, y);
                Rectangle tileRight = map.getCollidableBox(x + 1, y);

                for (CollisionDetectionRay ray: detectionRayList) {
                    Collision collision = getSideOfCollisionWithTile(ray, tile,
                            tileUp, tileDown, tileLeft, tileRight);
                    if (collision != null) {
                        collisionList.add(collision);
                        collisions.toList.add(collision);
                    }
                }
            }
        }

        return new CollisionList(collisionList);
    }

    private Collision getSideOfCollisionWithTile(CollisionDetectionRay ray, Rectangle tile,
                                                 Rectangle tileUp,
                                                 Rectangle tileDown,
                                                 Rectangle tileLeft,
                                                 Rectangle tileRight) {
        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();

        // Put non-null ones in an array, then sort by distance to start
        // A line can only have at most 2 intersections with a rectangle
        List<Collision> collisionList = new ArrayList<Collision>(2);

        // Find intersection on each side of the tile
        if (tileLeft == null) {
            Collision left = new Collision(GeoMath.findIntersectionLeft(tile, start, end), Collision.Side.Left, ray, tile);
            if (left.point != null) collisionList.add(left);
        }
        if (tileRight == null) {
            Collision right = new Collision(GeoMath.findIntersectionRight(tile, start, end), Collision.Side.Right, ray, tile);
            if (right.point != null) collisionList.add(right);
        }
        if (tileUp == null) {
            Collision up = new Collision(GeoMath.findIntersectionUp(tile, start, end), Collision.Side.Up, ray, tile);
            if (up.point != null) collisionList.add(up);
        }
        if (tileDown == null) {
            Collision down = new Collision(GeoMath.findIntersectionDown(tile, start, end), Collision.Side.Down, ray, tile);
            if (down.point != null) collisionList.add(down);
        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList, start);
    }

    public EnemyDamage getEnemyCollision(MovingObject object, float deltaTime, MapBase map) {
        int direction = object.direction;
        Vector2 vel = object.vel;
        Rectangle bounds = object.bounds;

        collisions.toList.clear();
        // From inside out, find the first tile that collides with the player
        float stepX = vel.x * deltaTime;
        float stepY = vel.y * deltaTime;
        // Translate the start and end vectors depending on the direction of the movement
        float paddingX = 0;
        float paddingY = 0;
        if (stepX > 0) {
            paddingX = bounds.width;
        }
        if (stepY > 0) {
            paddingY = bounds.height;
        }
        Vector2 endPosX = new Vector2(bounds.x + stepX, bounds.y);
        Vector2 endPosY = new Vector2(bounds.x, bounds.y + stepY);
        Vector2 endPos = new Vector2(bounds.x + stepX, bounds.y + stepY);

        // Setup collision detection rays
        List<CollisionDetectionRay> detectionRayList = new ArrayList<CollisionDetectionRay>(5);
        detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, 0, paddingY));
        detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, bounds.width, paddingY));
        if (vel.x != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, 0));
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, bounds.height));
        }
        if (vel.x != 0 && vel.y != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPos, paddingX, paddingY));
        }

        for (EnemyBase enemy: map.enemyList) {
            for (CollisionDetectionRay ray: detectionRayList) {
                Collision collision = getSideOfCollisionWithTile(ray, enemy.bounds,
                        null, null, null, null);
                if (collision != null) {
                    return new EnemyDamage(EnemyDamage.Type.Normal, collision);
                }
            }
        }

        return null;
    }

    public abstract void update(float delta, MapBase map);
}

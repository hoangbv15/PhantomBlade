package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;

/**
 * Created by buivuhoang on 13/02/17.
 * Objects that move around in the map, with a position, velocity, direction,
 * bounding box.
 * These objects can collide with the map and with each other.
 */
public abstract class MovingObject {
    // Property for collision detection
    public CollisionDetectionRay horizontalRay;
    public CollisionDetectionRay diagonalRay;

    // Directions
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int NONEDIRECTION = 0;

    public int direction;
    public boolean grounded;

    public Vector2 pos;
    public Vector2 vel;
    public Rectangle mapCollisionBounds = new Rectangle();
    public Rectangle takeDamageBounds = new Rectangle();
    private Rectangle dealDamageBounds = new Rectangle();

    public float stateTime;

    public int healthPoints;
    public int maxHealthPoints;

    public void initialiseHealthPoints(int hp) {
        maxHealthPoints = hp;
        healthPoints = hp;
    }

    /**
     * Decreases this object's hp by the amount of damage
     * @param damage The damage that is being dealt to this object
     * @return Whether the damage has successfully been dealt
     */
    public boolean takeDamage(Damage damage) {
        healthPoints -= damage.getDamage();
        return true;
    }

    public void updatePos(float x, float y) {
        mapCollisionBounds.x = x;
        mapCollisionBounds.y = y;
        pos.x = mapCollisionBounds.x;
        pos.y = mapCollisionBounds.y;
        Vector2 collisionBoundsOffset = getCollisionBoundsOffset();
        takeDamageBounds.x = mapCollisionBounds.x + collisionBoundsOffset.x;
        takeDamageBounds.y = mapCollisionBounds.y + collisionBoundsOffset.y;
        dealDamageBounds.x = mapCollisionBounds.x + collisionBoundsOffset.x;
        dealDamageBounds.y = mapCollisionBounds.y + collisionBoundsOffset.y;
    }

    public int movingDirection() {
        if (vel.x > 0) {
            return MovingObject.RIGHT;
        } else if (vel.x == 0) {
            return direction;
        }
        return MovingObject.LEFT;
    }

    public void die() {
        healthPoints = 0;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public boolean isAffectedByGravity() {
        return true;
    }

    public boolean isStoppedByWalls() {
        return true;
    }

    protected Vector2 getCollisionBoundsOffset() { return VectorCache.get(0f, 0f); }

    public Rectangle getDealDamageBounds() {
        return dealDamageBounds;
    }

    protected void setDealDamageBoundsSize(float width, float height) {
        dealDamageBounds.setSize(width, height);
    }

    public void resetCollisionDetectionRays() {
        horizontalRay = null;
        diagonalRay = null;
    }
}

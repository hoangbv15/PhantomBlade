package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.NumberMath;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay.Orientation;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay.Side;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 21/02/17.
 */
public abstract class PhysicsBase {
    // Debug property, used for rendering collisions to the screen. Needs to be public
    public CollisionList collisions;

    // Variables for push back
    private float currentPushBackDurationToGo;
    protected abstract float getPushBackDuration();
    protected abstract float getPushBackSpeed();
    private int pushBackDirection;
    private boolean isBeingPushedBack;

    public PhysicsBase() {
        collisions = new CollisionList(new ArrayList<Collision>());
        isBeingPushedBack = false;
    }

    public final CollisionList getMapCollision(MovingObject object, float deltaTime, MapBase map) {
        return getMapCollision(object, deltaTime, map, false);
    }

    public final CollisionList getMapCollision(MovingObject object, float deltaTime, MapBase map, boolean overlapMode) {
        Vector2 vel = object.vel;
        int direction = vel.x >= 0 ? MovingObject.RIGHT : MovingObject.LEFT;
        Rectangle bounds = object.mapCollisionBounds;

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
        List<CollisionDetectionRay> detectionRayList = object.detectionRayList;
        detectionRayList.clear();
        if (vel.x != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, 0, Side.Front, Orientation.Horizontal));
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, bounds.height, Side.Front, Orientation.Horizontal));
        }
        if (vel.y != 0) {
            Side side1 = direction == MovingObject.LEFT ? Side.Front : Side.Back;
            Side side2 = direction == MovingObject.RIGHT ? Side.Front : Side.Back;
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, 0, paddingY, side1, Orientation.Vertical));
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, bounds.width, paddingY, side2, Orientation.Vertical));
        }
        if (vel.x != 0 && vel.y != 0) {
            detectionRayList.add(new CollisionDetectionRay(bounds, endPos, paddingX, paddingY, Side.Front, Orientation.Diagonal));
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
                TileBase tile = map.getCollidableBox(x, y);
                if (tile == null) {
                    continue;
                }
                // Get the tiles surrounding this one
                TileBase tileUp = map.getCollidableBox(x, y + 1);
                TileBase tileDown = map.getCollidableBox(x, y - 1);
                TileBase tileLeft = map.getCollidableBox(x - 1, y);
                TileBase tileRight = map.getCollidableBox(x + 1, y);
                TileBase tileTopLeft = map.getCollidableBox(x - 1, y + 1);
                TileBase tileTopRight = map.getCollidableBox(x + 1, y + 1);
                TileBase tileBottomLeft = map.getCollidableBox(x - 1, y - 1);
                TileBase tileBottomRight = map.getCollidableBox(x + 1, y - 1);

                for (CollisionDetectionRay ray: detectionRayList) {
                    Collision collision = tile.getCollisionWithTile(object, ray,
                            tileUp, tileDown, tileLeft, tileRight, tileTopLeft, tileTopRight, tileBottomLeft, tileBottomRight,
                            overlapMode);
                    if (collision != null) {
                        collisionList.add(collision);
                        collisions.toList.add(collision);
                    }
                }
            }
        }

        return new CollisionList(collisionList);
    }

    public final Damage getEnemyCollisionDamage(MovingObject object, MapBase map) {
        EnemyBase enemy = getCollidingEnemy(object, map, false);
        if (enemy == null) {
            return null;
        }
        float playerX = object.takeDamageBounds.x + object.takeDamageBounds.width / 2;
        float enemyX = enemy.mapCollisionBounds.x + enemy.mapCollisionBounds.width / 2;
        Damage.Side side = Damage.Side.Left;
        if (playerX < enemyX) {
            side = Damage.Side.Right;
        }
        enemy.damage.side = side;
        return enemy.damage;
    }

    public final void dealDamageIfPlayerAttackHitsEnemy(PlayerAttack attack, MapBase map) {
        if (attack.isDead()) {
            return;
        }
        EnemyBase enemy = getCollidingEnemy(attack, map, true);
        if (enemy == null) {
            return;
        }
        boolean enemyTookDamage = enemy.takeDamage(attack.damage);
        if (!enemy.isDead() || attack.damage.type != Damage.Type.Heavy) {
            attack.die(enemyTookDamage);
        }
    }

    public final void stopAttackIfHitWall(PlayerAttack attack, float delta, MapBase map) {
        CollisionList collisions = getMapCollision(attack, delta, map, true);
        if (!collisions.toList.isEmpty()) {
            attack.die(true);
        }
    }

    private EnemyBase getCollidingEnemy(MovingObject object, MapBase map, boolean isEnemyTakingDamage) {
        for (EnemyBase enemy: map.enemyList) {
            if (enemy.isDead() || !enemy.spawned) {
                continue;
            }
            if (isEnemyTakingDamage) {
                if (object.mapCollisionBounds.overlaps(enemy.takeDamageBounds)) {
                    return enemy;
                }
            }
            else if (object.takeDamageBounds.overlaps(enemy.mapCollisionBounds)) {
                return enemy;
            }
        }
        return null;
    }

    public void pushBack(int direction) {
        pushBackDirection = direction;
        currentPushBackDurationToGo = getPushBackDuration();
        isBeingPushedBack = true;
    }

    private void doesPushBack(MovingObject object, float delta) {
        if (currentPushBackDurationToGo > 0) {
            object.vel.x = getPushBackSpeed() * pushBackDirection * currentPushBackDurationToGo / getPushBackDuration();
            currentPushBackDurationToGo -= delta;
        } else if (isBeingPushedBack) {
            isBeingPushedBack = false;
            object.vel.x = 0;
        }
    }

    public final void update(MovingObject object, float delta, MapBase map) {
        if (object.isDead()) {
            return;
        }
        if (isBeingPushedBack) {
            doesPushBack(object, delta);
        }
        internalUpdate(object, delta, map);
    }

    public abstract void internalUpdate(MovingObject object, float delta, MapBase map);
}

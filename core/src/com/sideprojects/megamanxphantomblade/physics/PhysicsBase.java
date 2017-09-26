package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAttack;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.NumberMath;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay.Orientation;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay.Side;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.Attack;

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
        collisions = new CollisionList(new ArrayList<>());
        isBeingPushedBack = false;
    }

    public final CollisionList getMapCollision(MovingObject object, float deltaTime, MapBase map) {
        return getMapCollision(object, deltaTime, map, false);
    }

    public final CollisionList getMapCollision(MovingObject object, float deltaTime, MapBase map, boolean overlapMode) {
        Vector2 vel = object.vel;
        int direction = vel.x >= 0 ? MovingObject.RIGHT : MovingObject.LEFT;
        direction = vel.x == 0 ? object.direction : direction;
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
        List<CollisionDetectionRay> detectionRayList = new ArrayList<>();
        object.resetCollisionDetectionRays();
        if (vel.x != 0) {
            object.horizontalRay = new CollisionDetectionRay(bounds, endPosX, paddingX, 0, Side.Front, Orientation.Horizontal);
            detectionRayList.add(object.horizontalRay);
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosX, paddingX, bounds.height, Side.Front, Orientation.Horizontal));
        }
        if (vel.y != 0) {
            Side side1 = direction == MovingObject.LEFT ? Side.Front : Side.Back;
            Side side2 = direction == MovingObject.RIGHT ? Side.Front : Side.Back;
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, 0, paddingY, side1, Orientation.Vertical));
            detectionRayList.add(new CollisionDetectionRay(bounds, endPosY, bounds.width, paddingY, side2, Orientation.Vertical));
        }
        if (vel.x != 0 && vel.y != 0) {
            object.diagonalRay = new CollisionDetectionRay(bounds, endPos, paddingX, paddingY, Side.Front, Orientation.Diagonal);
            detectionRayList.add(object.diagonalRay);
        }

        // Loop through map and use collision detection rays to detect...well..collisions.
        int xStart = (int)bounds.x;
        int yStart = (int)bounds.y;
        if (vel.y <= 0) {
            yStart += 1;
        }

        // This is needed for moving left on a slope to work
        // The most correct here is the below, for all width
        // xStart = (int)(bounds.x + bounds.width);
        // But we know our width is < 1, so we can do
        // xStart += 1;
        // To speed this up
        if (direction == MovingObject.LEFT) {
            xStart += 1;
        }

        // paddingX is 0 when vel X is 0
        int xEnd = (int)(endPosX.x + paddingX);

        if (direction == MovingObject.RIGHT) {
            float newEndPosX = endPosX.x + bounds.width;
            if (newEndPosX != (int)newEndPosX)
                xEnd += 1;
        }

        int yEnd = (int)(endPosY.y + paddingY);

        // Loop through the rectangular area that the speed vector occupies
        // Get a list of all collisions with map tiles in the area
        // Identify the collision nearest to the player
        // Player has at most 2 collisions with the map at the same time
        List<Collision> collisionList = new ArrayList<>(2);
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

    public final Damage getDamageDoneToPlayer(MovingObject object, MapBase map) {
        Damage damage;
        float enemyX;
        EnemyBase enemy = getCollidingEnemy(object, map, false);
        if (enemy == null) {
            EnemyAttack attack = getCollidingEnemyAttack(object, map);
            if (attack == null) {
                return null;
            }
            enemyX = attack.getDealDamageBounds().x + attack.getDealDamageBounds().width / 2;
            damage = attack.damage;
        } else {
            enemyX = enemy.getDealDamageBounds().x + enemy.getDealDamageBounds().width / 2;
            damage = enemy.damage;
        }

        float playerX = object.takeDamageBounds.x + object.takeDamageBounds.width / 2;
        Damage.Side side = Damage.Side.LEFT;
        if (playerX < enemyX) {
            side = Damage.Side.RIGHT;
        }
        damage.setSide(side);
        return damage;
    }

    public final void dealDamageIfPlayerAttackHitsEnemy(Attack attack, MapBase map) {
        if (attack.isDead()) {
            return;
        }
        EnemyBase enemy = getCollidingEnemy(attack, map, true);
        if (enemy == null) {
            return;
        }
        boolean enemyTookDamage = enemy.takeDamage(attack.damage);
        if (!enemy.isDead() || attack.damage.getType() != Damage.Type.HEAVY) {
            attack.die(enemyTookDamage);
        }
    }

    private EnemyBase getCollidingEnemy(MovingObject object, MapBase map, boolean isEnemyTakingDamage) {
        for (EnemyBase enemy: map.enemyList) {
            if (enemy.isDead() || !enemy.spawned) {
                continue;
            }
            // Here we get the enemy that is receiving the player's attack
            if (isEnemyTakingDamage) {
                if (object.getDealDamageBounds().overlaps(enemy.takeDamageBounds)) {
                    return enemy;
                }
            }
            // Here we get the enemy that is doing damage to the player
            else if (object.takeDamageBounds.overlaps(enemy.getDealDamageBounds())) {
                return enemy;
            }
        }
        return null;
    }

    private EnemyAttack getCollidingEnemyAttack(MovingObject object, MapBase map) {
        for (EnemyAttack attack: map.enemyAttackQueue) {
            if (!attack.isDead() && object.takeDamageBounds.overlaps(attack.getDealDamageBounds())) {
                // Here we get the enemy attack that is doing damage to the player
                return attack;
            }
        }
        return null;
    }

    public final void stopAttackIfHitWall(Attack attack, float delta, MapBase map) {
        CollisionList collisions = getMapCollision(attack, delta, map, true);
        if (!collisions.toList.isEmpty()) {
            attack.die(true);
        }
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

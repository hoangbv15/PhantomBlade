package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;

/**
 * Created by buivuhoang on 04/04/17.
 */
public abstract class Physics extends PhysicsBase {

    public Physics() {
        super();
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {
        inputProcessing(object, delta, map);

        // Check for collisions
        CollisionList collisions = calculateReaction(object, delta, map);

        postCollisionDetectionProcessing(collisions);
    }

    private CollisionList calculateReaction(MovingObject object, float delta, MapBase map) {
        // Process the player input here
        CollisionList collisionList = getMapCollision(object, delta, map);
        collisions = collisionList;
        // Apply collision-specific movement logic
        // Take current state into account if needed
        for (Collision collision: collisionList.toList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case Left:
                case Right:
                    object.vel.x = 0;
                    object.bounds.x = preCollide.x;
                    break;
                case Up:
                    object.grounded = true;
                case Down:
                    object.vel.y = 0;
                    object.bounds.y = preCollide.y;
                    break;
            }
        }

        object.bounds.x += object.vel.x * delta;
        object.bounds.y += object.vel.y * delta;
        object.updatePos();
        return collisionList;
    }

    protected void applyGravity(MovingObject object, float gravity, float maxFallspeed, float delta) {
        if (object.vel.y > maxFallspeed) {
            object.vel.y -= gravity * delta;
        } else {
            object.vel.y = maxFallspeed;
        }
    }

    public abstract void inputProcessing(MovingObject object, float delta, MapBase map);
    public abstract void postCollisionDetectionProcessing(CollisionList collisions);
}

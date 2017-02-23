package com.sideprojects.megamanxphantomblade.physics.player;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.Collision;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;

import java.util.List;

/**
 * Created by buivuhoang on 21/02/17.
 */
public class PlayerPhysics extends PhysicsBase {
    // Velocities
    public static final float VELOCITY_WALK = 4f;
    public static final float VELOCITY_JUMP = 6f;
    public static final float VELOCITY_X_WALLJUMP = -3f;
    public static final float VELOCITY_DASH_ADDITION = 4f;

    private PlayerStateBase movementState;

    public PlayerPhysics(InputProcessor input) {
        super(input);
        // Create the initial states
        movementState = new PlayerIdleState();
    }

    @Override
    public void update(float delta, MovingObject object, MapBase map) {
        // Apply gravity
        movementState.applyGravity(object, map.GRAVITY, map.MAX_FALLSPEED, delta);

        // Process commands
        if (input.isCommandPressed(Command.LEFT)) {
            movementState.left(object, VELOCITY_WALK, delta);
        } else if (input.isCommandPressed(Command.RIGHT)) {
            movementState.right(object, VELOCITY_WALK, delta);
        } else {
            movementState.none(object);
        }

        List<Collision> collisions = calculateReaction(delta, object, map);
        movementState = movementState.nextState(input, object, collisions);
    }

    private List<Collision> calculateReaction(float delta, MovingObject object, MapBase map) {
        // Process the player input here
        List<Collision> collisionList = getMapCollision(object, delta, map);

        for (Collision collision: collisionList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case UP:
                    object.vel.y = 0;
                    object.pos.y = preCollide.y;
                    break;
                case DOWN:
                    object.vel.y = 0;
                    object.pos.y = preCollide.y;
                    break;
                case LEFT:
                case RIGHT:
                    object.vel.x = 0;
                    object.pos.x = preCollide.x;
                    break;
            }
        }

        object.pos.x += object.vel.x * delta;
        object.pos.y += object.vel.y * delta;
        object.bounds.x = object.pos.x;
        object.bounds.y = object.pos.y;

        return null;
    }
}

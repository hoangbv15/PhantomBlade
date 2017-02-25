package com.sideprojects.megamanxphantomblade.physics.player;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.holddashstates.NotHoldingDash;
import com.sideprojects.megamanxphantomblade.physics.player.movementstates.Idle;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 21/02/17.
 */
public class PlayerPhysics extends PhysicsBase {
    // Velocities
    public static final float VELOCITY_WALK = 4f;
    public static final float VELOCITY_JUMP = 6f;
    public static final float VELOCITY_X_WALLJUMP = -3f;
    public static final float VELOCITY_DASH_ADDITION = 4f;

    private PlayerMovementStateBase movementState;
    private PlayerHoldDashStateBase holdDashState;

    public PlayerBase player;

    public PlayerPhysics(InputProcessor input, PlayerBase player) {
        super(input);
        this.player = player;
        // Create the initial states
        movementState = new Idle(player, null);
        holdDashState = new NotHoldingDash();
    }

    @Override
    public void update(float delta, MapBase map) {
        player.stateTime += delta;

        // Jumping
        if (input.isCommandPressed(Command.JUMP)) {
            if (input.isCommandJustPressed(Command.JUMP)) {
                if (movementState.canJump()) {
                    player.vel.y = VELOCITY_JUMP;
                }
                if (movementState.canWallJump()) {
                    player.vel.x = VELOCITY_X_WALLJUMP * player.direction;
                }
            }
        } else {
            if (player.vel.y > 0) {
                player.vel.y = 0;
            }
        }

        // Running & direction
        if (input.isCommandPressed(Command.LEFT)) {
            if (movementState.canRun()) {
                player.direction = MovingObject.LEFT;
                if (movementState.canWallGlide()) {
                    player.vel.x += VELOCITY_WALK * player.direction * delta * 4;
                } else {
                    player.vel.x = VELOCITY_WALK * player.direction;
                }
            }
        } else if (input.isCommandPressed(Command.RIGHT)) {
            if (movementState.canRun()) {
                player.direction = MovingObject.RIGHT;
                if (movementState.canWallGlide()) {
                    player.vel.x += VELOCITY_WALK * player.direction * delta * 4;
                } else {
                    player.vel.x = VELOCITY_WALK * player.direction;
                }
            }
        } else {
            player.vel.x = 0;
        }

        // Dashing
        if (input.isCommandPressed(Command.DASH)) {
            if (movementState.canDash(input)) {
                player.vel.x += VELOCITY_DASH_ADDITION * player.direction;
                // Air dash
                if (!player.grounded) {
                    player.vel.y = 0;
                }
            }
        }

        // Apply gravity
        applyGravity(player, map.GRAVITY, map.MAX_FALLSPEED, delta);

        // Check for collisions
        CollisionList collisions = calculateReaction(delta, map);

        // Assign next state
        movementState = movementState.nextState(input, player, collisions);
        holdDashState = holdDashState.nextState(input, player, collisions);
    }

    private CollisionList calculateReaction(float delta, MapBase map) {
        // Process the player input here
        CollisionList collisionList = getMapCollision(player, delta, map);

        // Apply collision-specific movement logic
        // Take current state into account if needed
        for (Collision collision: collisionList.toList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case LEFT:
                case RIGHT:
                    if (movementState.canWallSlide()) {
                        player.vel.y = map.WALLSLIDE_FALLSPEED;
                    }
                    player.vel.x = 0;
                    player.pos.x = preCollide.x;
                    break;
                case UP:
                    player.grounded = true;
                case DOWN:
                    player.vel.y = 0;
                    player.pos.y = preCollide.y;
                    break;
            }
        }

        player.pos.x += player.vel.x * delta;
        player.pos.y += player.vel.y * delta;
        player.bounds.x = player.pos.x;
        player.bounds.y = player.pos.y;

        return collisionList;
    }

    private void applyGravity(MovingObject object, float gravity, float maxFallspeed, float delta) {
        if (object.vel.y > maxFallspeed) {
            object.vel.y -= gravity * delta;
        }
    }
}

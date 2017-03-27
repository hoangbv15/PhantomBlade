package com.sideprojects.megamanxphantomblade.physics.player;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.damagestates.NotDamaged;
import com.sideprojects.megamanxphantomblade.physics.player.jumpdashstate.NotJumpDashing;
import com.sideprojects.megamanxphantomblade.physics.player.movementstates.Idle;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

/**
 * Created by buivuhoang on 21/02/17.
 */
public abstract class PlayerPhysics extends PhysicsBase {
    // Velocities
    private static final float VELOCITY_WALK = 2f;
    private static final float VELOCITY_JUMP = 6f;
    private static final float VELOCITY_X_WALLJUMP = -3f;
    private static final float VELOCITY_DASH_ADDITION = 2.5f;
    private static final float VELOCITY_WALLBOUNCE_MULTIPLIER = 5f;
    @Override
    protected float getPushBackDuration() { return 0.08f; }

    @Override
    protected float getPushBackSpeed() { return 3f; }

    public PlayerMovementStateBase movementState;
    public PlayerDamageState damageState;
    private PlayerJumpDashStateBase holdDashState;

    public PlayerSound soundPlayer;

    public PlayerBase player;

    public PlayerPhysics(InputProcessor input, PlayerBase player, PlayerSound soundPlayer) {
        super(input);
        this.player = player;
        this.soundPlayer = soundPlayer;
        // Create the initial states
        player.direction = MovingObject.RIGHT;
        movementState = new Idle(input, player, null, soundPlayer);
        holdDashState = new NotJumpDashing(player);
        damageState = new NotDamaged(player);
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {
        player.stateTime += delta;
        holdDashState = holdDashState.nextState(input, player);

        // Check for enemy damage
        Damage damage = getEnemyCollision(player, map);
        damageState = damageState.nextState(player, damage, this, delta);

        if (damageState.canControl()) {
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
            if (input.isCommandPressed(Command.LEFT) || input.isCommandPressed(Command.RIGHT)) {
                if (movementState.canRun()) {
                    player.direction = input.isCommandPressed(Command.LEFT) ? MovingObject.LEFT : MovingObject.RIGHT;
                    if (movementState.canWallGlide()) {
                        player.vel.x += - VELOCITY_X_WALLJUMP * player.direction * delta * VELOCITY_WALLBOUNCE_MULTIPLIER;
                    } else {
                        player.vel.x = VELOCITY_WALK * player.direction;
                    }
                }
            } else {
                player.vel.x = 0;
            }

            // Dashing
            boolean doNotApplyGravity = false;
            if (player.state == PlayerState.DASH) {
                player.vel.x = (VELOCITY_WALK + VELOCITY_DASH_ADDITION) * player.direction;
                // Air dash
                if (!player.grounded) {
                    player.vel.y = 0;
                    doNotApplyGravity = true;
                }
            }

            if (player.state == PlayerState.UPDASH) {
                player.vel.y = VELOCITY_JUMP + VELOCITY_DASH_ADDITION;
                doNotApplyGravity = true;
            }

            // Hold dash
            if (holdDashState.isJumpDashing()) {
                if (player.vel.x != 0) {
                    if (movementState.canWallJump()) {
                        player.vel.x -= VELOCITY_DASH_ADDITION * player.direction;
                    } else if (movementState.canWallGlide()) {
                        player.vel.x += VELOCITY_DASH_ADDITION * player.direction * delta * VELOCITY_WALLBOUNCE_MULTIPLIER;
                    } else {
                        player.vel.x += VELOCITY_DASH_ADDITION * player.direction;
                    }
                }
            }

            // Apply gravity
            if (!doNotApplyGravity) {
                applyGravity(player, map.GRAVITY, map.MAX_FALLSPEED, map.WALLSLIDE_FALLSPEED, delta);
            }
        } else {
            object.vel.y = 0;
        }

        // Check for collisions
        CollisionList collisions = calculateReaction(delta, map);

        // Assign next state
        if (damageState.canControl()) {
            movementState = movementState.nextState(input, player, collisions);
            // Do any optional update
            movementState.update(input, player);
        }
    }

    public void setStateToIdle() {
        movementState = new Idle(input, player, player.state, soundPlayer);
    }

    private CollisionList calculateReaction(float delta, MapBase map) {
        // Process the player input here
        CollisionList collisionList = getMapCollision(player, delta, map);
        collisions = collisionList;
        // Apply collision-specific movement logic
        // Take current state into account if needed
        for (Collision collision: collisionList.toList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case Left:
                case Right:
                    player.vel.x = 0;
                    player.bounds.x = preCollide.x;
                    break;
                case Up:
                    player.grounded = true;
                case Down:
                    player.vel.y = 0;
                    player.bounds.y = preCollide.y;
                    break;
            }
        }

        player.bounds.x += player.vel.x * delta;
        player.bounds.y += player.vel.y * delta;
        player.updatePos();
        return collisionList;
    }

    private void applyGravity(MovingObject object, float gravity, float maxFallspeed, float wallslideFallspeed, float delta) {
        float finalFallspeed = maxFallspeed;
        if (player.state == PlayerState.WALLSLIDE) {
            finalFallspeed = wallslideFallspeed;
        }
        if (player.state == PlayerState.FALL && object.vel.y > 0) {
            object.vel.y = 0;
        }
        if (object.vel.y > finalFallspeed) {
            object.vel.y -= gravity * delta;
        } else {
            object.vel.y = finalFallspeed;
        }
    }
}

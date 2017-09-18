package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.Physics;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.damagestates.NotDamaged;
import com.sideprojects.megamanxphantomblade.physics.player.jumpdashstate.NotJumpDashing;
import com.sideprojects.megamanxphantomblade.physics.player.movementstates.Idle;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

/**
 * This handles player's movements based in user input, forces applied to the player
 * such as gravity, and sounds that the player creates.
 * The concrete implementations of this should handle any specific actions that can generate force/movement/sounds
 * For example: player attacks.
 * Created by buivuhoang on 21/02/17.
 */
public abstract class PlayerPhysics extends Physics {
    // Velocities
    private static final float VELOCITY_WALK = 2f;
    private static final float VELOCITY_JUMP = 6f;
    private static final float VELOCITY_X_WALLJUMP = -3f;
    private static final float VELOCITY_DASH_ADDITION = 2.5f;
    private static final float VELOCITY_WALLBOUNCE_MULTIPLIER = 5f;
    protected final InputProcessor input;

    @Override
    protected float getPushBackDuration() { return 0.08f; }

    @Override
    protected float getPushBackSpeed() { return 5f; }

    public PlayerMovementStateBase movementState;
    public PlayerDamageState damageState;

    private PlayerJumpDashStateBase holdDashState;

    public PlayerSound playerSound;

    public PlayerBase player;

    public PlayerPhysics(InputProcessor input, PlayerBase player, PlayerSound playerSound) {
        super();
        this.player = player;
        this.playerSound = playerSound;
        this.input = input;
        // Create the initial states
        player.direction = MovingObject.RIGHT;
        movementState = new Idle(input, player, null, playerSound);
        holdDashState = new NotJumpDashing(player);
        damageState = new NotDamaged(player);
    }

    @Override
    public void inputProcessing(MovingObject object, float delta, MapBase map) {
        // If player is out of bounds, kill the player
        if (map.isOutOfBounds(player)) {
            player.die();
        }

        player.stateTime += delta;
        holdDashState = holdDashState.nextState(input, player);

        // Check for enemy damage
        Damage damage = getDamageDoneToPlayer(player, map);
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
            if (player.state == PlayerState.Dash) {
                player.vel.x = (VELOCITY_WALK + VELOCITY_DASH_ADDITION) * player.direction;
                // Air dash
                if (!player.grounded) {
                    player.vel.y = 0;
                    doNotApplyGravity = true;
                }
            }

            if (player.state == PlayerState.Updash) {
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
    }

    @Override
    public void postCollisionDetectionProcessing(CollisionList collisions) {
        // Assign next state
        if (damageState.canControl()) {
            movementState = movementState.nextState(input, player, collisions);
            // Do any optional update
            movementState.update(input, player);
        }

        // Check if player is dead
        if (player.isDead() && player.state != PlayerState.Dead) {
            playerSound.callback(player.state, PlayerState.Dead);
            player.state = PlayerState.Dead;
        }
    }

    public void setStateToIdle() {
        movementState = new Idle(input, player, player.state, playerSound);
    }

    private void applyGravity(MovingObject object, float gravity, float maxFallspeed, float wallslideFallspeed, float delta) {
        float finalFallspeed = maxFallspeed;
        if (player.state == PlayerState.Wallslide) {
            finalFallspeed = wallslideFallspeed;
        }
        if (player.state == PlayerState.Fall && object.vel.y > 0) {
            object.vel.y = 0;
        }
        this.applyGravity(object, gravity, finalFallspeed, delta);
    }
}

package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Dash extends PlayerMovementStateBase {
    private float dashDuration = 0.5f;
    private boolean enterWhileRunning = false;
    private boolean enterWhileIdle = false;
    private boolean enterWhileAirborne = false;
    private Command directionKeyBeingPressed = Command.LEFT;

    public Dash(InputProcessor input, PlayerBase player, PlayerState lastState, PlayerStateChangeHandler stateChangeHandler) {
        super(player, lastState, stateChangeHandler);
        if (player.grounded && (
                input.isCommandPressed(Command.LEFT) ||
                input.isCommandPressed(Command.RIGHT))) {
            enterWhileRunning = true;
            if (input.isCommandPressed(Command.LEFT)) {
                directionKeyBeingPressed = Command.LEFT;
            } else {
                directionKeyBeingPressed = Command.RIGHT;
            }
        } else if (player.grounded) {
            enterWhileIdle = true;
        } else {
            enterWhileAirborne = true;
        }
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public boolean canJump() {
        return !enterWhileAirborne;
    }

    @Override
    public boolean canWallJump() {
        return false;
    }

    @Override
    public boolean canWallGlide() {
        return false;
    }

    @Override
    public boolean canDash(InputProcessor input) {
        return true;
    }

    @Override
    public PlayerState enter(PlayerBase player) {
        player.stateTime = 0;
        return PlayerState.Dash;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (enterWhileRunning) {
            if (player.stateTime >= dashDuration
                    || input.isCommandJustPressed(Command.JUMP)
                    || !input.isCommandPressed(directionKeyBeingPressed)
                    || hasChangedDirection(player)
                    || player.vel.y < 0
                    || collisionList.isCollidingSide()) {
                return nextStateIfExit(input, player, collisionList);
            }
        } else if (enterWhileIdle) {
            if (player.stateTime >= dashDuration
                    || input.isCommandJustPressed(Command.JUMP)
                    || hasChangedDirection(player)
                    || player.vel.y < 0
                    || !input.isCommandPressed(Command.DASH) ||
                    collisionList.isCollidingSide()) {
                return nextStateIfExit(input, player, collisionList);
            }
        } else if (enterWhileAirborne) {
            if (player.stateTime >= dashDuration
                    || hasChangedDirection(player)
                    || !input.isCommandPressed(Command.DASH) ||
                    collisionList.isColliding()) {
                return nextStateIfExit(input, player, collisionList);
            }
        }

        return this;
    }

    private PlayerMovementStateBase nextStateIfExit(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.y > 0) {
            return new Jump(input, player, player.state, stateChangeHandler);
        }
        if (player.vel.y < 0 || !player.grounded) {
            return new Fall(input, player, player.state, stateChangeHandler);
        }
        if (!collisionList.isCollidingSide() &&
                (input.isCommandPressed(Command.LEFT) ||
                input.isCommandPressed(Command.RIGHT))) {
            return new Run(input, player, player.state, stateChangeHandler);
        }
        if (collisionList.isCollidingSide() && collisionList.distanceToSideCollision() == 0) {
            return new Idle(input, player, player.state, stateChangeHandler);
        }
        return new DashBreak(input, player, player.state, stateChangeHandler);
    }

    private boolean hasChangedDirection(PlayerBase player) {
        return player.direction != startingDirection;
    }

    @Override
    public void update(InputProcessor input, PlayerBase player) {
        // No need to do anything
    }
}

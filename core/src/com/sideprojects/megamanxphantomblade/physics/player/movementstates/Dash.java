package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Dash extends PlayerMovementStateBase {
    private float dashDuration = 0.5f;
    private boolean enterWhileRunning = false;
    private boolean enterWhileIdle = false;
    private boolean enterWhileAirborne = false;
    private int startingDirection = 0;

    public Dash(InputProcessor input, PlayerBase player) {
        super(player);
        if (player.grounded && (
                input.isCommandPressed(Command.LEFT) ||
                input.isCommandPressed(Command.RIGHT))) {
            enterWhileRunning = true;
        } else if (player.grounded) {
            enterWhileIdle = true;
        } else {
            enterWhileAirborne = true;
        }
        startingDirection = player.direction;
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
    public boolean canWallSlide() {
        return false;
    }

    @Override
    public boolean canDash(InputProcessor input) {
        return true;
    }

    @Override
    public PlayerState enter(MovingObject object) {
        object.stateTime = 0;
        return PlayerState.DASH;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (enterWhileRunning) {
            if (player.stateTime >= dashDuration
                    || input.isCommandPressed(Command.JUMP)
                    || hasChangedDirection(player)
                    || player.vel.y < 0
                    || collisionList.isCollidingSide()) {
                return nextStateIfExit(input, player, collisionList);
            }
        } else if (enterWhileIdle) {
            if (player.stateTime >= dashDuration
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
                    collisionList.isCollidingSide()) {
                return nextStateIfExit(input, player, collisionList);
            }

        }

        return this;
    }

    private PlayerMovementStateBase nextStateIfExit(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.y > 0) {
            return new Jump(input, player, player.state);
        }
        if (!player.grounded) {
            return new Fall(input, player, player.state);
        }
        if (player.vel.x != 0) {
            return new Run(input, player, player.state);
        }
        return new Idle(input, player, player.state);
    }

    private boolean hasChangedDirection(PlayerBase player) {
        return player.direction != startingDirection;
    }

    @Override
    public void update(InputProcessor input) {
        // No need to do anything
    }
}

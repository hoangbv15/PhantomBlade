package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 18/03/17.
 */
public class Updash extends PlayerMovementStateBase {
    private float dashDuration = 0.3f;
    public Updash(PlayerBase player, PlayerState lastState, PlayerStateChangeHandler stateChangeHandler) {
        super(player, lastState, stateChangeHandler);
    }

    @Override
    public boolean canRun() {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
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
        player.grounded = false;
        return PlayerState.Updash;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.stateTime >= dashDuration
                || !input.isCommandPressed(Command.DASH) ||
                collisionList.isCollidingSide()) {
            return nextStateIfExit(input, player, collisionList);
        }
        return this;
    }

    private PlayerMovementStateBase nextStateIfExit(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        return new Fall(input, player, player.state, stateChangeHandler);
    }

    @Override
    public void update(InputProcessor input, PlayerBase player) {
        // No need to do anything
    }
}

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

    public Dash(InputProcessor input, PlayerBase player) {
        super(player);
        if (player.grounded && (
                input.isCommandPressed(Command.LEFT) ||
                input.isCommandPressed(Command.RIGHT))) {
            enterWhileRunning = true;
        }
    }

    @Override
    public boolean canRun() {
        return true;
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
            if (player.stateTime >= dashDuration) {
                if (player.vel.x != 0 && player.grounded) {
                    return new Run(player, player.state);
                }
                if (player.vel.y > 0) {
                    return new Jump(player, player.state);
                }
                return new Idle(player, player.state);
            }
            return this;
        }



        return this;
    }
}

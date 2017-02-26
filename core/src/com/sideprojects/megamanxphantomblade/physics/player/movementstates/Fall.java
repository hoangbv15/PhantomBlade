package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.Command;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerNonDashState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class Fall extends PlayerNonDashState {
    public Fall(InputProcessor input, PlayerBase player, PlayerState lastState) {
        super(input, player, lastState);
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
    public boolean canRun() {
        return true;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public boolean canWallSlide() {
        return true;
    }

    @Override
    public PlayerState enter(MovingObject object) {
        object.stateTime = 0;
        object.grounded = false;
        return PlayerState.FALL;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (input.isCommandPressed(Command.DASH) && canDash(input)) {
            return new Dash(input, player);
        }
        if (player.vel.x != 0 && player.vel.y == 0) {
            return new Run(input, player, player.state);
        } else if (player.vel.x == 0 && player.vel.y == 0) {
            return new Touchdown(input, player, player.state);
        }
        if (collisionList.isCollidingSide()) {
            return new WallSlide(player);
        }
        return this;
    }
}

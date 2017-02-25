package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class WallSlide extends PlayerMovementStateBase {
    public WallSlide(PlayerBase player) {
        super(player);
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public boolean canWallJump() {
        return true;
    }

    @Override
    public boolean canWallGlide() {
        return true;
    }

    @Override
    public boolean canWallSlide() {
        return true;
    }

    @Override
    public boolean canDash(InputProcessor input) {
        return false;
    }

    @Override
    public PlayerState enter(MovingObject object) {
        object.stateTime = 0;
        return PlayerState.WALLSLIDE;
    }

    @Override
    public PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList) {
        if (player.vel.y > 0) {
            return new WallJump(player, player.state);
        }
        if (player.grounded) {
            return new Touchdown(player, player.state);
        }
        if (!collisionList.isCollidingSide()) {
            return new Fall(player, player.state);
        }
        return this;
    }
}

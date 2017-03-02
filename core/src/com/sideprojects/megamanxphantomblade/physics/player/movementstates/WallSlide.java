package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerMovementStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class WallSlide extends PlayerMovementStateBase {
    private Collision.Side collidingSlide;

    public WallSlide(PlayerBase player, CollisionList collisionList) {
        super(player);
        this.collidingSlide = collisionList.collidingSide();
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
            // Need to save the colliding side from state creation, and pass into wall jump
            return new WallJump(input, player, player.state, collidingSlide);
        }
        if (player.grounded) {
            return new Touchdown(input, player, player.state);
        }
        if (!collisionList.isCollidingSide()) {
            return new Fall(input, player, player.state);
        }
        return this;
    }

    @Override
    public void update(InputProcessor input, PlayerBase player) {
        // No need to do anything
    }
}

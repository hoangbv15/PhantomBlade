package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.State;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 22/02/17.
 */
public abstract class PlayerMovementStateBase implements State {
    public PlayerMovementStateBase(PlayerBase player) {
        player.state = enter(player);
    }

    /**
     * bounce from the wall to the opposite direction
     * @return
     */
    public abstract boolean canWallJump();

    /**
     * Glide back to the wall from a wall jump
     * @return
     */
    public abstract boolean canWallGlide();

    /**
     * Sliding down slowly by holding on to a wall
     * @return
     */
    public abstract boolean canWallSlide();

    /**
     * Quick horizontall dash
     * @return
     */
    public abstract boolean canDash(InputProcessor input);

    public abstract PlayerState enter(MovingObject object);

    /**
     * Determine what the next state should be
     * @param input The input that causes the physics reaction
     * @param collisionList The output of the physics engine after receiving the user's input
     * @return The next state
     */
    public abstract PlayerMovementStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisionList);

    /**
     * Optional method to update internal states within the state if needed
     */
    public abstract void update(InputProcessor input);
}

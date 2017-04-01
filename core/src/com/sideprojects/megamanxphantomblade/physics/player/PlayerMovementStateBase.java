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
    public int startingDirection = MovingObject.NONEDIRECTION;
    protected PlayerStateChangeHandler stateChangeHandler;

    public PlayerMovementStateBase(PlayerBase player, PlayerState lastState, PlayerStateChangeHandler stateChangeHandler) {
        player.state = enter(player);
        this.stateChangeHandler = stateChangeHandler;
        stateChangeHandler.callback(lastState, player.state);
        startingDirection = player.direction;
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
     * Quick horizontal dash
     * @return
     */
    public abstract boolean canDash(InputProcessor input);

    public abstract PlayerState enter(PlayerBase player);

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
    public abstract void update(InputProcessor input, PlayerBase player);
}

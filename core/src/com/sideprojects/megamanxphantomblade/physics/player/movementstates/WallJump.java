package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class WallJump extends Jump {
    private int startingDirection = MovingObject.NONEDIRECTION;
    private PlayerBase player;

    public WallJump(InputProcessor input, PlayerBase player, PlayerState lastState, Collision.Side sideOfCollision, PlayerStateChangeHandler stateChangeHandler) {
        super(input, player, lastState, stateChangeHandler);
        startingDirection = sideOfCollision == Collision.Side.LEFT ? MovingObject.RIGHT : MovingObject.LEFT;
        this.player = player;
    }

    @Override
    public boolean canWallGlide() {
        return player.direction == startingDirection;
    }
}

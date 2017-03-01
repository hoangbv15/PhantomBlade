package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class WallJump extends Jump {
    private int startingDirection = 0;
    private PlayerBase player;

    public WallJump(InputProcessor input, PlayerBase player, PlayerState lastState) {
        super(input, player, lastState);
        startingDirection = player.direction;
        this.player = player;
    }

    @Override
    public boolean canWallGlide() {
        return player.direction == startingDirection;
    }
}

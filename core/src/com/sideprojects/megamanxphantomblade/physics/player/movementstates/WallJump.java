package com.sideprojects.megamanxphantomblade.physics.player.movementstates;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class WallJump extends Jump {
    public WallJump(PlayerBase player, PlayerState lastState) {
        super(player, lastState);
    }

    @Override
    public boolean canWallGlide() {
        return true;
    }
}

package com.sideprojects.megamanxphantomblade.physics.player.holddashstates;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerHoldDashStateBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public class NotHoldingDash extends PlayerHoldDashStateBase {
    @Override
    public PlayerHoldDashStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisions) {
        return this;
    }
}
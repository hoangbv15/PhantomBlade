package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 25/02/17.
 */
public abstract class PlayerHoldDashStateBase {
    public abstract PlayerHoldDashStateBase nextState(InputProcessor input, PlayerBase player, CollisionList collisions);
}

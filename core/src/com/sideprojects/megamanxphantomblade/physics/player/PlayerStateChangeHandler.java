package com.sideprojects.megamanxphantomblade.physics.player;

/**
 * Created by buivuhoang on 02/03/17.
 */
public interface PlayerStateChangeHandler {
    void callback(PlayerState previousState, PlayerState nextState);
    void lowHealthWarning();
}

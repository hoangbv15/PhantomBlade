package com.sideprojects.megamanxphantomblade.physics;

/**
 * Created by buivuhoang on 22/02/17.
 */
public interface State {
    // Command pattern

    boolean canRun();

    boolean canJump();
}
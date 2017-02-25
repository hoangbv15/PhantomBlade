package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.MovingObject;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public interface State {
    // Command pattern

    boolean canRun();

    boolean canJump();
}
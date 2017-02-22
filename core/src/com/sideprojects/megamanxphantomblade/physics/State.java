package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public interface State {
    void enter();
    State nextState(InputProcessor input, PhysicsBase physics);
}

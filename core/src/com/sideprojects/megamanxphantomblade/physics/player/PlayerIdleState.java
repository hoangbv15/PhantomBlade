package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;
import com.sideprojects.megamanxphantomblade.physics.State;

/**
 * Created by buivuhoang on 22/02/17.
 */
public class PlayerIdleState implements State {
    @Override
    public void enter() {

    }

    @Override
    public State nextState(InputProcessor input, PhysicsBase physics) {
        return this;
    }
}

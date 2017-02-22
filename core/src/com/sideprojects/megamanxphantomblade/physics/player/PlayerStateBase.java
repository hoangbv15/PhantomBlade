package com.sideprojects.megamanxphantomblade.physics.player;

import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;
import com.sideprojects.megamanxphantomblade.physics.State;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public class PlayerStateBase implements State {
    @Override
    public void enter() {

    }

    @Override
    public State nextState(InputProcessor input, PhysicsBase physics) {
        return null;
    }
}

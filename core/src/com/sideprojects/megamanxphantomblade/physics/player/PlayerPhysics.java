package com.sideprojects.megamanxphantomblade.physics.player;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.input.InputProcessor;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;
import com.sideprojects.megamanxphantomblade.physics.State;

/**
 * Created by buivuhoang on 21/02/17.
 */
public class PlayerPhysics extends PhysicsBase {
    // Velocities
    public static final float VELOCITY_WALK = 4f;
    public static final float VELOCITY_JUMP = 6f;
    public static final float VELOCITY_X_WALLJUMP = -3f;
    public static final float VELOCITY_DASH_ADDITION = 4f;

    public State movementState;
    public State holdDashState;

    public PlayerPhysics(InputProcessor input) {
        super(input);
        // Create the initial states
        movementState = new PlayerIdleState();
        holdDashState = new PlayerIdleState();
    }

    @Override
    public void update() {
        movementState = movementState.nextState(input, this);
        holdDashState = holdDashState.nextState(input, this);
    }

    public Vector2 calculatePosition(MovingObject object) {
        return null;
    }
}

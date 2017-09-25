package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.PhysicsBase;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class TestablePhysicsBase extends PhysicsBase {
    @Override
    protected float getPushBackDuration() {
        return 0;
    }

    @Override
    protected float getPushBackSpeed() {
        return 0;
    }

    @Override
    public void internalUpdate(MovingObject object, float delta, MapBase map) {

    }
}

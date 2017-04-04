package com.sideprojects.megamanxphantomblade.enemies.scripts;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.enemies.ScriptBase;

/**
 * Created by buivuhoang on 04/04/17.
 */
public class Mettool extends ScriptBase {
    private static float velocity = 1;
    private static float time = 1;

    public Mettool(MovingObject object) {
        super(object);
    }

    @Override
    public void describe() {
        move(MovingObject.LEFT, velocity, time);
        wait(time);
        move(MovingObject.RIGHT, velocity, time);
        wait(time);
    }
}

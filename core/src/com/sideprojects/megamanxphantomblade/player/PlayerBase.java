package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase {
    public static final int IDLE = 0;
    public static final int RUN = 1;

    public static final int LEFT = -1;
    public static final int RIGHT = 1;

    private static final float VELOCITY_X = 10f;

    public int state;
    public int direction;

    public Vector2 pos;
    private Vector2 vel;
    public float stateTime;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        vel = new Vector2();
        stateTime = 0;
        state = IDLE;
        direction = RIGHT;
    }

    public void update(float deltaTime) {
        processKeys();
        tryMove();
        stateTime += deltaTime;
    }

    private void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            state = RUN;
            direction = LEFT;
            vel.x = VELOCITY_X * LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            state = RUN;
            direction = RIGHT;
            vel.x = VELOCITY_X * RIGHT;
        } else {
            state = IDLE;
            vel.x = 0;
        }
    }

    private void tryMove() {
        pos.x += vel.x;
    }
}

package com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase {
    public Vector2 pos;
    public float stateTime;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        stateTime = 0;
    }
}

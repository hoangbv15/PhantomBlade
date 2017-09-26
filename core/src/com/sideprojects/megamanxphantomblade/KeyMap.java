package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Input;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class KeyMap {
    public final int left;
    public final int right;
    public final int up;
    public final int jump;
    public final int dash;
    public final int attack;
    public final int reset;

    public KeyMap() {
        left = Input.Keys.LEFT;
        right = Input.Keys.RIGHT;
        up = Input.Keys.UP;
        jump = Input.Keys.X;
        dash = Input.Keys.Z;
        reset = Input.Keys.S;
        attack = Input.Keys.C;
    }
}

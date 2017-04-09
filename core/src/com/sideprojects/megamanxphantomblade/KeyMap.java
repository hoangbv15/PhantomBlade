package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Input;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class KeyMap {
    public int left;
    public int right;
    public int up;
    public int jump;
    public int dash;
    public int attack;
    public int reset;

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

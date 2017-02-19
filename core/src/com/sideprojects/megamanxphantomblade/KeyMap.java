package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.Input;

/**
 * Created by buivuhoang on 19/02/17.
 */
public class KeyMap {
    public int left;
    public int right;
    public int jump;
    public int dash;
    public int reset;

    public KeyMap() {
        left = Input.Keys.LEFT;
        right = Input.Keys.RIGHT;
        jump = Input.Keys.X;
        dash = Input.Keys.Z;
        reset = Input.Keys.S;
    }
}
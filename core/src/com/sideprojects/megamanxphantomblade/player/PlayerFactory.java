package com.sideprojects.megamanxphantomblade.player;

import com.sideprojects.megamanxphantomblade.KeyMap;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerFactory {
    protected KeyMap keyMap;
    public PlayerFactory(KeyMap keyMap) {
        this.keyMap = keyMap;
    }

    public abstract PlayerBase createPlayer(float x, float y);
}

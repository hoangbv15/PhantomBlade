package com.sideprojects.megamanxphantomblade.player;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerFactory {
    public abstract PlayerBase createPlayer(float x, float y, int difficulty);
}

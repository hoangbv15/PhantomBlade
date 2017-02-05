package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerXFactory implements PlayerFactory {
    public PlayerBase createPlayer(float x, float y) {
        return new PlayerX(x, y);
    }
}

package com.sideprojects.megamanxphantomblade.player;

import com.sideprojects.megamanxphantomblade.KeyMap;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;

/**
 * Created by buivuhoang on 21/02/17.
 */
public abstract class Player extends PlayerBase {
    public Player(float x, float y, KeyMap keyMap, PlayerPhysics physics) {
        super(x, y, keyMap, physics);
    }

    @Override
    public void processKeys(float deltaTime) {
        super.processKeys(deltaTime);
    }

    @Override
    public void tryMove(float deltaTime, MapBase map) {
        super.tryMove(deltaTime, map);
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
    }
}

package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.animation.AnimationFactory;
import com.sideprojects.megamanxphantomblade.animation.XAnimationFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerX extends PlayerBase {
    protected PlayerX(float x, float y) {
        super(x, y);
    }

    @Override
    public void createAnimations() {
        AnimationFactory aniFactory = new XAnimationFactory();
        playerIdleLeft = aniFactory.getIdleLeft();
        playerIdleRight = aniFactory.getIdleRight();
        playerRunLeft = aniFactory.getRunLeft();
        playerRunRight = aniFactory.getRunRight();
    }
}

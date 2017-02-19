package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.KeyMap;
import com.sideprojects.megamanxphantomblade.animation.AnimationFactory;
import com.sideprojects.megamanxphantomblade.animation.XAnimationFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.TraceColour;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerX extends PlayerBase {
    protected PlayerX(float x, float y, KeyMap keyMap) {
        super(x, y, keyMap);
    }

    @Override
    public void createAnimations() {
        AnimationFactory aniFactory = new XAnimationFactory();
        playerIdleLeft = aniFactory.getIdleLeft();
        playerIdleRight = aniFactory.getIdleRight();
        playerRunLeft = aniFactory.getRunLeft();
        playerRunRight = aniFactory.getRunRight();
        playerJumpLeft = aniFactory.getJumpLeft();
        playerJumpRight = aniFactory.getJumpRight();
        playerFallLeft = aniFactory.getFallLeft();
        playerFallRight = aniFactory.getFallRight();
        playerTouchdownLeft = aniFactory.getTouchdownLeft();
        playerTouchdownRight = aniFactory.getTouchdownRight();
        playerWallSlideLeft = aniFactory.getWallSlideLeft();
        playerWallSlideRight = aniFactory.getWallSlideRight();
        playerWallJumpLeft = aniFactory.getWallJumpLeft();
        playerWallJumpRight = aniFactory.getWallJumpRight();
        playerDashLeft = aniFactory.getDashLeft();
        playerDashRight = aniFactory.getDashRight();
        playerDashBreakLeft = aniFactory.getDashBreakLeft();
        playerDashBreakRight = aniFactory.getDashBreakRight();
    }

    @Override
    public TraceColour getTraceColour() {
        return new TraceColour(0, 0, 1);
    }
}

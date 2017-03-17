package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class PlayerXAnimation extends PlayerAnimation {
    @Override
    protected int[] getAnimationIdle() {
        return new int[] {1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 3, 4, 3};
    }

    @Override
    protected String getTextureIdleAtlas() {
        return Sprites.XIdle;
    }

    @Override
    protected String getTextureRunAtlas() {
        return Sprites.XRun;
    }

    @Override
    protected int[] getAnimationRun() {
        return null;
    }

    @Override
    protected String getTextureJumpAtlas() { return Sprites.XJump; }

    @Override
    protected int[] getAnimationJump() { return new int[] {0, 1, 2, 3}; }

    @Override
    protected String getTextureFallAtlas() { return Sprites.XJump; }

    @Override
    protected int[] getAnimationFall() { return new int[] {3, 4, 5, 6, 7}; }

    @Override
    protected String getTextureTouchdownAtlas() { return Sprites.XJump; }

    @Override
    protected int[] getAnimationTouchdown() { return new int[] {8, 9, 10}; }

    @Override
    protected String getTextureWallSlideAtlas() { return Sprites.XWallSlide; }

    @Override
    protected int[] getAnimationWallSlide() { return new int[] {0, 1, 2, 3}; }

    @Override
    protected String getTextureWallJumpAtlas() { return Sprites.XWallSlide; }

    @Override
    protected int[] getAnimationWallJump() { return new int[] {4, 5, 6}; }

    @Override
    protected String getTextureDashAtlas() { return Sprites.XDash; }

    @Override
    protected int[] getAnimationDash() { return new int[] {0, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3}; }

    @Override
    protected String getTextureDashBreakAtlas() { return Sprites.XDash; }

    @Override
    protected int[] getAnimationDashBreak() { return new int[] {4, 5, 6, 7}; }

    @Override
    protected String getTextureDashRocketAtlas() { return Sprites.XDashRocket; }

    @Override
    protected int[] getAnimationDashRocket() { return null; }
}

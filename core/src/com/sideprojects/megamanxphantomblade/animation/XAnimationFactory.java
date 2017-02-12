package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class XAnimationFactory extends AnimationFactory {
    @Override
    protected int[] getAnimationIdle() {
        return new int[] {1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 3, 4, 3};
    }

    @Override
    protected String getTextureIdleAtlas() {
        return "sprites/x/idle.txt";
    }

    @Override
    protected String getTextureRunAtlas() {
        return "sprites/x/run.txt";
    }

    @Override
    protected int[] getAnimationRun() {
        return null;
    }

    @Override
    protected String getTextureJumpAtlas() { return "sprites/x/jump.txt"; }

    @Override
    protected int[] getAnimationJump() { return new int[] {0, 1, 2, 3}; }

    @Override
    protected String getTextureFallAtlas() { return "sprites/x/jump.txt"; }

    @Override
    protected int[] getAnimationFall() { return new int[] {3, 4, 5, 6, 7}; }

    @Override
    protected String getTextureTouchdownAtlas() { return "sprites/x/jump.txt"; }

    @Override
    protected int[] getAnimationTouchdown() { return new int[] {8, 9, 10}; }
}

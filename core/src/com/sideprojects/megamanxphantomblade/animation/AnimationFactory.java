package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by buivuhoang on 05/02/17.
 */
public abstract class AnimationFactory {
    private Animation<TextureRegion> instanceIdleLeft;
    private Animation<TextureRegion> instanceIdleRight;
    private Animation<TextureRegion> instanceRunLeft;
    private Animation<TextureRegion> instanceRunRight;
    private Animation<TextureRegion> instanceJumpLeft;
    private Animation<TextureRegion> instanceJumpRight;
    private Animation<TextureRegion> instanceFallLeft;
    private Animation<TextureRegion> instanceFallRight;
    private Animation<TextureRegion> instanceTouchdownLeft;
    private Animation<TextureRegion> instanceTouchdownRight;

    public Animation<TextureRegion> getIdleLeft() {
        if (instanceIdleLeft == null) {
            instanceIdleLeft = AnimationHelper.create(getTextureIdleAtlas(), getAnimationIdle(), true, 0.1f);
        }
        return instanceIdleLeft;
    }

    public Animation<TextureRegion> getIdleRight() {
        if (instanceIdleRight == null) {
            instanceIdleRight = AnimationHelper.create(getTextureIdleAtlas(), getAnimationIdle(), false, 0.1f);
        }
        return instanceIdleRight;
    }

    public Animation<TextureRegion> getRunLeft() {
        if (instanceRunLeft == null) {
            instanceRunLeft = AnimationHelper.create(getTextureRunAtlas(), getAnimationRun(), true, 0.05f);
        }
        return instanceRunLeft;
    }

    public Animation<TextureRegion> getRunRight() {
        if (instanceRunRight == null) {
            instanceRunRight = AnimationHelper.create(getTextureRunAtlas(), getAnimationRun(), false, 0.05f);
        }
        return instanceRunRight;
    }

    public Animation<TextureRegion> getJumpLeft() {
        if (instanceJumpLeft == null) {
            instanceJumpLeft = AnimationHelper.create(getTextureJumpAtlas(), getAnimationJump(), true, 0.10f);
        }
        return instanceJumpLeft;
    }

    public Animation<TextureRegion> getJumpRight() {
        if (instanceJumpRight == null) {
            instanceJumpRight = AnimationHelper.create(getTextureJumpAtlas(), getAnimationJump(), false, 0.10f);
        }
        return instanceJumpRight;
    }

    public Animation<TextureRegion> getFallLeft() {
        if (instanceFallLeft == null) {
            instanceFallLeft = AnimationHelper.create(getTextureFallAtlas(), getAnimationFall(), true, 0.05f);
        }
        return instanceFallLeft;
    }

    public Animation<TextureRegion> getFallRight() {
        if (instanceFallRight == null) {
            instanceFallRight = AnimationHelper.create(getTextureFallAtlas(), getAnimationFall(), false, 0.05f);
        }
        return instanceFallRight;
    }

    public Animation<TextureRegion> getTouchdownLeft() {
        if (instanceTouchdownLeft == null) {
            instanceTouchdownLeft = AnimationHelper.create(getTextureTouchdownAtlas(), getAnimationTouchdown(), true, 0.05f);
        }
        return instanceTouchdownLeft;
    }

    public Animation<TextureRegion> getTouchdownRight() {
        if (instanceTouchdownRight == null) {
            instanceTouchdownRight = AnimationHelper.create(getTextureTouchdownAtlas(), getAnimationTouchdown(), false, 0.05f);
        }
        return instanceTouchdownRight;
    }

    protected abstract String getTextureIdleAtlas();
    protected abstract int[] getAnimationIdle();

    protected abstract String getTextureRunAtlas();
    protected abstract int[] getAnimationRun();

    protected abstract String getTextureJumpAtlas();
    protected abstract int[] getAnimationJump();

    protected abstract String getTextureFallAtlas();
    protected abstract int[] getAnimationFall();

    protected abstract String getTextureTouchdownAtlas();
    protected abstract int[] getAnimationTouchdown();
}

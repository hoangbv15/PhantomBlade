package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;

/**
 * Created by buivuhoang on 05/02/17.
 */
public abstract class PlayerAnimation {
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
    private Animation<TextureRegion> instanceWallSlideLeft;
    private Animation<TextureRegion> instanceWallSlideRight;
    private Animation<TextureRegion> instanceWallJumpLeft;
    private Animation<TextureRegion> instanceWallJumpRight;
    private Animation<TextureRegion> instanceDashLeft;
    private Animation<TextureRegion> instanceDashRight;
    private Animation<TextureRegion> instanceDashBreakLeft;
    private Animation<TextureRegion> instanceDashBreakRight;
    private Animation<TextureRegion> instanceDashRocketLeft;
    private Animation<TextureRegion> instanceDashRocketRight;

    public Animation<TextureRegion> getIdleLeft() {
        if (instanceIdleLeft == null) {
            instanceIdleLeft = AnimationLoader.load(getTextureIdleAtlas(), getAnimationIdle(), true, 0.1f);
        }
        return instanceIdleLeft;
    }

    public Animation<TextureRegion> getIdleRight() {
        if (instanceIdleRight == null) {
            instanceIdleRight = AnimationLoader.load(getTextureIdleAtlas(), getAnimationIdle(), false, 0.1f);
        }
        return instanceIdleRight;
    }

    public Animation<TextureRegion> getRunLeft() {
        if (instanceRunLeft == null) {
            instanceRunLeft = AnimationLoader.load(getTextureRunAtlas(), getAnimationRun(), true, 0.04f);
        }
        return instanceRunLeft;
    }

    public Animation<TextureRegion> getRunRight() {
        if (instanceRunRight == null) {
            instanceRunRight = AnimationLoader.load(getTextureRunAtlas(), getAnimationRun(), false, 0.04f);
        }
        return instanceRunRight;
    }

    public Animation<TextureRegion> getJumpLeft() {
        if (instanceJumpLeft == null) {
            instanceJumpLeft = AnimationLoader.load(getTextureJumpAtlas(), getAnimationJump(), true, 0.10f);
        }
        return instanceJumpLeft;
    }

    public Animation<TextureRegion> getJumpRight() {
        if (instanceJumpRight == null) {
            instanceJumpRight = AnimationLoader.load(getTextureJumpAtlas(), getAnimationJump(), false, 0.10f);
        }
        return instanceJumpRight;
    }

    public Animation<TextureRegion> getFallLeft() {
        if (instanceFallLeft == null) {
            instanceFallLeft = AnimationLoader.load(getTextureFallAtlas(), getAnimationFall(), true, 0.05f);
        }
        return instanceFallLeft;
    }

    public Animation<TextureRegion> getFallRight() {
        if (instanceFallRight == null) {
            instanceFallRight = AnimationLoader.load(getTextureFallAtlas(), getAnimationFall(), false, 0.05f);
        }
        return instanceFallRight;
    }

    public Animation<TextureRegion> getTouchdownLeft() {
        if (instanceTouchdownLeft == null) {
            instanceTouchdownLeft = AnimationLoader.load(getTextureTouchdownAtlas(), getAnimationTouchdown(), true, 0.05f);
        }
        return instanceTouchdownLeft;
    }

    public Animation<TextureRegion> getTouchdownRight() {
        if (instanceTouchdownRight == null) {
            instanceTouchdownRight = AnimationLoader.load(getTextureTouchdownAtlas(), getAnimationTouchdown(), false, 0.05f);
        }
        return instanceTouchdownRight;
    }

    public Animation<TextureRegion> getWallSlideLeft() {
        if (instanceWallSlideLeft == null) {
            instanceWallSlideLeft = AnimationLoader.load(getTextureWallSlideAtlas(), getAnimationWallSlide(), true, 0.05f);
        }
        return instanceWallSlideLeft;
    }

    public Animation<TextureRegion> getWallSlideRight() {
        if (instanceWallSlideRight == null) {
            instanceWallSlideRight = AnimationLoader.load(getTextureWallSlideAtlas(), getAnimationWallSlide(), false, 0.05f);
        }
        return instanceWallSlideRight;
    }

    public Animation<TextureRegion> getWallJumpLeft() {
        if (instanceWallJumpLeft == null) {
            instanceWallJumpLeft = AnimationLoader.load(getTextureWallJumpAtlas(), getAnimationWallJump(), true, 0.10f);
        }
        return instanceWallJumpLeft;
    }

    public Animation<TextureRegion> getWallJumpRight() {
        if (instanceWallJumpRight == null) {
            instanceWallJumpRight = AnimationLoader.load(getTextureWallJumpAtlas(), getAnimationWallJump(), false, 0.10f);
        }
        return instanceWallJumpRight;
    }

    public Animation<TextureRegion> getDashRight() {
        if (instanceDashRight == null) {
            instanceDashRight = AnimationLoader.load(getTextureDashAtlas(), getAnimationDash(), false, 0.05f);
        }
        return instanceDashRight;
    }

    public Animation<TextureRegion> getDashLeft() {
        if (instanceDashLeft == null) {
            instanceDashLeft = AnimationLoader.load(getTextureDashAtlas(), getAnimationDash(), true, 0.05f);
        }
        return instanceDashLeft;
    }

    public Animation<TextureRegion> getDashBreakRight() {
        if (instanceDashBreakRight == null) {
            instanceDashBreakRight = AnimationLoader.load(getTextureDashBreakAtlas(), getAnimationDashBreak(), false, 0.08f);
        }
        return instanceDashBreakRight;
    }

    public Animation<TextureRegion> getDashBreakLeft() {
        if (instanceDashBreakLeft == null) {
            instanceDashBreakLeft = AnimationLoader.load(getTextureDashBreakAtlas(), getAnimationDashBreak(), true, 0.08f);
        }
        return instanceDashBreakLeft;
    }

    public Animation<TextureRegion> getDashRocketRight() {
        if (instanceDashRocketRight == null) {
            instanceDashRocketRight = AnimationLoader.load(getTextureDashRocketAtlas(), getAnimationDashRocket(), false, 0.05f);
        }
        return instanceDashRocketRight;
    }

    public Animation<TextureRegion> getDashRocketLeft() {
        if (instanceDashRocketLeft == null) {
            instanceDashRocketLeft = AnimationLoader.load(getTextureDashRocketAtlas(), getAnimationDashRocket(), true, 0.05f);
        }
        return instanceDashRocketLeft;
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

    protected abstract String getTextureWallSlideAtlas();
    protected abstract int[] getAnimationWallSlide();

    protected abstract String getTextureWallJumpAtlas();
    protected abstract int[] getAnimationWallJump();

    protected abstract String getTextureDashAtlas();
    protected abstract int[] getAnimationDash();

    protected abstract String getTextureDashBreakAtlas();
    protected abstract int[] getAnimationDashBreak();

    protected abstract String getTextureDashRocketAtlas();
    protected abstract int[] getAnimationDashRocket();
}

package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by buivuhoang on 05/02/17.
 */
public abstract class AnimationFactory {
    private Animation instanceIdleLeft;
    private Animation instanceIdleRight;
    private Animation instanceRunLeft;
    private Animation instanceRunRight;

    public Animation getIdleLeft() {
        if (instanceIdleLeft == null) {
            instanceIdleLeft = AnimationHelper.create(getTextureIdleAtlas(), getAnimationIdle(), true, 0.1f);
        }
        return instanceIdleLeft;
    }

    public Animation getIdleRight() {
        if (instanceIdleRight == null) {
            instanceIdleRight = AnimationHelper.create(getTextureIdleAtlas(), getAnimationIdle(), false, 0.1f);
        }
        return instanceIdleRight;
    }

    public Animation getRunLeft() {
        if (instanceRunLeft == null) {
            instanceRunLeft = AnimationHelper.create(getTextureIdleRun(), getAnimationRun(), true, 0.05f);
        }
        return instanceRunLeft;
    }

    public Animation getRunRight() {
        if (instanceRunRight == null) {
            instanceRunRight = AnimationHelper.create(getTextureIdleRun(), getAnimationRun(), false, 0.05f);
        }
        return instanceRunRight;
    }

    protected abstract String getTextureIdleAtlas();
    protected abstract int[] getAnimationIdle();

    protected abstract String getTextureIdleRun();
    protected abstract int[] getAnimationRun();
}

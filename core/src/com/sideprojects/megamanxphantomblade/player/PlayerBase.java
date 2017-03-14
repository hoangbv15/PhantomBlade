package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase extends MovingObject {
    protected PlayerPhysics physics;

    public PlayerState state;
    // If the player is holding dash button
    public boolean isJumpDashing;

    public PlayerAnimation animations;
    public TextureRegion currentFrame;
    public TextureRegion currentDashRocketFrame;

    public PlayerBase(float x, float y) {
        bounds = new Rectangle(x, y, 0.1f, 0.1f);
        pos = new Vector2(x, y);
        updatePos();
        vel = new Vector2(0, 0);
        createAnimations();
    }

    public void update() {
        updateAnimation();
    }

    private void updateAnimation() {
        Animation<TextureRegion> currentAnimation;
        if (state == PlayerState.IDLE) {
            if (direction == LEFT) {
                currentAnimation = animations.getIdleLeft();
            } else {
                currentAnimation = animations.getIdleRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else if (state == PlayerState.RUN) {
            if (direction == LEFT) {
                currentAnimation = animations.getRunLeft();
            } else {
                currentAnimation = animations.getRunRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else if (state == PlayerState.JUMP) {
            if (direction == LEFT) {
                currentAnimation = animations.getJumpLeft();
            } else {
                currentAnimation = animations.getJumpRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.FALL) {
            if (direction == LEFT) {
                currentAnimation = animations.getFallLeft();
            } else {
                currentAnimation = animations.getFallRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.TOUCHDOWN) {
            if (direction == LEFT) {
                currentAnimation = animations.getTouchdownLeft();
            } else {
                currentAnimation = animations.getTouchdownRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.WALLSLIDE) {
            if (direction == LEFT) {
                currentAnimation = animations.getWallSlideLeft();
            } else {
                currentAnimation = animations.getWallSlideRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.WALLJUMP) {
            if (direction == LEFT) {
                currentAnimation = animations.getWallJumpLeft();
            } else {
                currentAnimation = animations.getWallJumpRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.DASH) {
            Animation<TextureRegion> dashRocketAnimation;
            if (direction == LEFT) {
                currentAnimation = animations.getDashLeft();
                dashRocketAnimation = animations.getDashRocketLeft();
            } else {
                currentAnimation = animations.getDashRight();
                dashRocketAnimation = animations.getDashRocketRight();
            }
            currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.DASHBREAK) {
            if (direction == LEFT) {
                currentAnimation = animations.getDashBreakLeft();
            } else {
                currentAnimation = animations.getDashBreakRight();
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        }
    }

    public abstract void createAnimations();

    public abstract TraceColour getTraceColour();
}

package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.animation.Particle;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase extends MovingObject {
    public PlayerState state;
    public PlayerState previousState;
    // If the player is holding dash button
    public boolean isJumpDashing;
    public boolean invincible;

    // Can only issue low health warning once
    // Resets after health being restored to above threshold
    public boolean canIssueLowHealthWarning;
    public static int lowHealthThreshold = 30;

    public PlayerAnimation animations;
    public TextureRegion currentFrame;
    public TextureRegion currentDashRocketFrame;

    public PlayerBase(float x, float y) {
        bounds = new Rectangle(x, y, 0.1f, 0.1f);
        pos = new Vector2(x, y);
        updatePos();
        vel = new Vector2(0, 0);
        initialiseHealthPoints(100);
        canIssueLowHealthWarning = true;
        createAnimations();
    }

    public void update(MapBase map) {
        updateAnimation(map);
    }

    public boolean isLowHealth() {
        boolean isLow = healthPoints <= lowHealthThreshold;
        if (isLow) {
            canIssueLowHealthWarning = false;
        }
        return isLow;
    }

    private void updateAnimation(MapBase map) {
        Animation<TextureRegion> currentAnimation = null;
        boolean looping = false;
        if (state == PlayerState.IDLE) {
            currentAnimation = animations.get(PlayerAnimation.Type.Idle, direction, isLowHealth());
            looping = true;
        } else if (state == PlayerState.RUN) {
            currentAnimation = animations.get(PlayerAnimation.Type.Run, direction, isLowHealth());
            looping = true;
        } else if (state == PlayerState.JUMP) {
            currentAnimation = animations.get(PlayerAnimation.Type.Jump, direction, isLowHealth());
        } else if (state == PlayerState.FALL) {
            currentAnimation = animations.get(PlayerAnimation.Type.Fall, direction, isLowHealth());
        } else if (state == PlayerState.TOUCHDOWN) {
            currentAnimation = animations.get(PlayerAnimation.Type.Touchdown, direction, isLowHealth());
        } else if (state == PlayerState.WALLSLIDE) {
            currentAnimation = animations.get(PlayerAnimation.Type.Wallslide, direction, isLowHealth());
            float paddingX = direction == RIGHT ? (bounds.getWidth() - 0.15f) : - 0.1f;
            float paddingY = -0.1f;
            map.addParticle(Particle.ParticleType.WALLSLIDE, pos.x + paddingX, pos.y + paddingY, true);
        } else if (state == PlayerState.WALLJUMP) {
            currentAnimation = animations.get(PlayerAnimation.Type.Walljump, direction, isLowHealth());
            if (previousState != state) {
                float paddingX = map.playerPhysics.movementState.startingDirection == RIGHT ? (bounds.getWidth() - 0.1f) : - 0.3f;
                float paddingY = 0f;
                map.addParticle(Particle.ParticleType.WALLKICK, pos.x + paddingX, pos.y + paddingY, false);
            }
        } else if (state == PlayerState.DASH) {
            currentAnimation = animations.get(PlayerAnimation.Type.Dash, direction, isLowHealth());
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimation.Type.Dashrocket, direction, isLowHealth());
            currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            if (previousState != state && grounded) {
                float padding = - bounds.getWidth() * direction;
                map.addParticle(Particle.ParticleType.DASH, pos.x + padding, pos.y, false);
            }
        } else if (state == PlayerState.DASHBREAK) {
            currentAnimation = animations.get(PlayerAnimation.Type.Dashbreak, direction, isLowHealth());
        } else if (state == PlayerState.UPDASH) {
            currentAnimation = animations.get(PlayerAnimation.Type.Updash, direction, isLowHealth());
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimation.Type.Updashrocket, direction, isLowHealth());
            currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
        } else if (state == PlayerState.DAMAGEDNORMAL) {
            currentAnimation = animations.get(PlayerAnimation.Type.DamagedNormal, direction, isLowHealth());
        } else if (state == PlayerState.DEAD) {
            // TODO: Add die animation here
        }

        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, looping);
        }
        previousState = state;
    }

    public abstract void createAnimations();

    public abstract TraceColour getTraceColour();
}

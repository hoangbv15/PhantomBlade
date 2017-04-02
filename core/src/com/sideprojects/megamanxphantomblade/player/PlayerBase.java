package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.animation.Particle;
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

    // Properties for attack animation
    public boolean isAttacking;
    public boolean firstFramesOfAttacking;
    public boolean justBegunAttacking;
    // This is probably only applicable for shooting characters such as X
    public boolean changeStateDuringAttack;
    public Damage.Type attackType;

    // Can only issue low health warning once
    // Resets after health being restored to above threshold
    public boolean canIssueLowHealthWarning;
    public static int lowHealthThreshold = 30;

    public PlayerAnimation animations;
    private Animation<TextureRegion> currentAnimation;
    public TextureRegion currentFrame;
    public TextureRegion currentDashRocketFrame;
    public Vector2 animationPadding;

    public PlayerBase(float x, float y) {
        bounds = new Rectangle(x, y, 0.1f, 0.1f);
        pos = new Vector2(x, y);
        updatePos();
        vel = new Vector2(0, 0);
        initialiseHealthPoints(100);
        canIssueLowHealthWarning = true;
        animationPadding = new Vector2(0, 0);
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
        PlayerAnimation.Type type = PlayerAnimation.Type.Idle;
        if (state == PlayerState.IDLE) {
            type = PlayerAnimation.Type.Idle;
        } else if (state == PlayerState.RUN) {
            type = PlayerAnimation.Type.Run;
        } else if (state == PlayerState.JUMP) {
            type = PlayerAnimation.Type.Jump;
        } else if (state == PlayerState.FALL) {
            type = PlayerAnimation.Type.Fall;
        } else if (state == PlayerState.TOUCHDOWN) {
            type = PlayerAnimation.Type.Touchdown;
        } else if (state == PlayerState.WALLSLIDE) {
            type = PlayerAnimation.Type.Wallslide;
            float paddingX = direction == RIGHT ? (bounds.getWidth() - 0.15f) : - 0.1f;
            float paddingY = -0.1f;
            map.addParticle(Particle.ParticleType.WALLSLIDE, pos.x + paddingX, pos.y + paddingY, true);
        } else if (state == PlayerState.WALLJUMP) {
            type = PlayerAnimation.Type.Walljump;
            if (previousState != state) {
                float paddingX = map.playerPhysics.movementState.startingDirection == RIGHT ? (bounds.getWidth() - 0.1f) : - 0.3f;
                float paddingY = 0f;
                map.addParticle(Particle.ParticleType.WALLKICK, pos.x + paddingX, pos.y + paddingY, false);
            }
        } else if (state == PlayerState.DASH) {
            type = PlayerAnimation.Type.Dash;
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimation.Type.Dashrocket, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
            if (dashRocketAnimation != null) {
                currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            }
            if (previousState != state && grounded) {
                float padding = - bounds.getWidth() * direction;
                map.addParticle(Particle.ParticleType.DASH, pos.x + padding, pos.y, false);
            }
        } else if (state == PlayerState.DASHBREAK) {
            type = PlayerAnimation.Type.Dashbreak;
        } else if (state == PlayerState.UPDASH) {
            type = PlayerAnimation.Type.Updash;
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimation.Type.Updashrocket, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
            if (dashRocketAnimation != null) {
                currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            }
        } else if (state == PlayerState.DAMAGEDNORMAL) {
            type = PlayerAnimation.Type.DamagedNormal;
        } else if (state == PlayerState.DEAD) {
            // TODO: Add die animation here
            type = PlayerAnimation.Type.DamagedNormal;
        }

        currentAnimation = animations.get(type, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
        boolean looping = animations.isLooping(type, isAttacking);
        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, looping);
            animationPadding = animations.getAnimationPaddingX(type, direction, isAttacking, attackType, changeStateDuringAttack);
        }
        previousState = state;

//        state = PlayerState.WALLSLIDE;
//        direction = RIGHT;
//        currentAnimation = animations.get(PlayerAnimation.Type.Wallslide, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
//        currentFrame = currentAnimation.getKeyFrame(stateTime, looping);
    }

    public int currentFrameIndex() {
        Animation.PlayMode prevPlayMode = currentAnimation.getPlayMode();
        currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
        int frameIndex = currentAnimation.getKeyFrameIndex(stateTime);
        currentAnimation.setPlayMode(prevPlayMode);
        return frameIndex;
    }

    public abstract void createAnimations();

    public abstract TraceColour getTraceColour();
}

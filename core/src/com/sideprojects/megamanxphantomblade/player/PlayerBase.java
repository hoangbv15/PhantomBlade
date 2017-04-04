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

import java.util.Map;

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
    public Damage.Type attackType;
    public float attackStateTime;
    // This is probably only applicable for shooting characters such as X
    public boolean changeStateDuringAttack;
    public boolean isCharging;
    public boolean almostFullyCharged;
    public boolean fullyCharged;
    public Map<PlayerAnimationBase.Type, TextureRegion> attackChargeFrames;

    // Can only issue low health warning once
    // Resets after health being restored to above threshold
    public boolean canIssueLowHealthWarning;
    public static int lowHealthThreshold = 30;

    public PlayerAnimationBase animations;
    private Animation<TextureRegion> currentAnimation;
    public TextureRegion currentFrame;
    public TextureRegion currentDashRocketFrame;
    public static float xDashRocketPadding = 0.4f;
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

    public void update(MapBase map, float delta) {
        updateAnimation(map);
        internalUpdate(delta);
    }

    public boolean isLowHealth() {
        boolean isLow = healthPoints <= lowHealthThreshold;
        if (isLow) {
            canIssueLowHealthWarning = false;
        }
        return isLow;
    }

    private void updateAnimation(MapBase map) {
        PlayerAnimationBase.Type type = PlayerAnimationBase.Type.Idle;
        if (state == PlayerState.IDLE) {
            type = PlayerAnimationBase.Type.Idle;
        } else if (state == PlayerState.RUN) {
            type = PlayerAnimationBase.Type.Run;
        } else if (state == PlayerState.JUMP) {
            type = PlayerAnimationBase.Type.Jump;
        } else if (state == PlayerState.FALL) {
            type = PlayerAnimationBase.Type.Fall;
        } else if (state == PlayerState.TOUCHDOWN) {
            type = PlayerAnimationBase.Type.Touchdown;
        } else if (state == PlayerState.WALLSLIDE) {
            type = PlayerAnimationBase.Type.Wallslide;
            float paddingX = direction == RIGHT ? (bounds.getWidth() - 0.15f) : - 0.1f;
            float paddingY = -0.1f;
            map.addParticle(Particle.ParticleType.WALLSLIDE, pos.x + paddingX, pos.y + paddingY, true);
        } else if (state == PlayerState.WALLJUMP) {
            type = PlayerAnimationBase.Type.Walljump;
            if (previousState != state) {
                float paddingX = map.playerPhysics.movementState.startingDirection == RIGHT ? (bounds.getWidth() - 0.1f) : - 0.3f;
                float paddingY = 0f;
                map.addParticle(Particle.ParticleType.WALLKICK, pos.x + paddingX, pos.y + paddingY, false);
            }
        } else if (state == PlayerState.DASH) {
            type = PlayerAnimationBase.Type.Dash;
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimationBase.Type.Dashrocket, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
            if (dashRocketAnimation != null) {
                currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            }
            if (previousState != state && grounded) {
                float padding = xDashRocketPadding;
                if (direction == RIGHT) {
                    padding = - bounds.width - xDashRocketPadding;
                }
                map.addParticle(Particle.ParticleType.DASH, pos.x + padding, pos.y, false);
            }
        } else if (state == PlayerState.DASHBREAK) {
            type = PlayerAnimationBase.Type.Dashbreak;
        } else if (state == PlayerState.UPDASH) {
            type = PlayerAnimationBase.Type.Updash;
            Animation<TextureRegion> dashRocketAnimation = animations.get(PlayerAnimationBase.Type.Updashrocket, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
            if (dashRocketAnimation != null) {
                currentDashRocketFrame = dashRocketAnimation.getKeyFrame(stateTime, false);
            }
        } else if (state == PlayerState.DAMAGEDNORMAL) {
            type = PlayerAnimationBase.Type.DamagedNormal;
        } else if (state == PlayerState.DEAD) {
            // TODO: Add die animation here
            type = PlayerAnimationBase.Type.DamagedNormal;
        }

        currentAnimation = animations.get(type, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
        boolean looping = animations.isLooping(type, isAttacking);
        if (currentAnimation != null) {
            float time;
            if (isAttacking && changeStateDuringAttack) {
                time = attackStateTime;
            } else {
                time = stateTime;
            }
            currentFrame = currentAnimation.getKeyFrame(time, looping);
            animationPadding = animations.getAnimationPaddingX(type, direction, isAttacking, attackType, changeStateDuringAttack);
        }
        previousState = state;

//        state = PlayerState.WALLSLIDE;
//        direction = RIGHT;
//        currentAnimation = animations.get(PlayerAnimationBase.Type.Wallslide, direction, isLowHealth(), isAttacking, attackType, firstFramesOfAttacking, changeStateDuringAttack);
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

    protected abstract void internalUpdate(float delta);

    public abstract Vector2 getChargeAnimationPadding();
}

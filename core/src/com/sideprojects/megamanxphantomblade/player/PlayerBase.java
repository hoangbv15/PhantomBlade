package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.sideprojects.megamanxphantomblade.KeyMap;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.AnimationFactory;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysics;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;

import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase extends MovingObject {
    private KeyMap keyMap;
    protected PlayerPhysics physics;

    // Velocities
    private static final float VELOCITY_WALK = 4f;
    private static final float VELOCITY_JUMP = 6f;
    private static final float VELOCITY_X_WALLJUMP = -3f;
    private static final float VELOCITY_DASH_ADDITION = 4f;

    public PlayerState state;
    // Player can only air dash once
    public boolean canAirDash;
    // If the player is holding dash button
    public boolean isHoldingDash;

    public AnimationFactory animations;
    public TextureRegion currentFrame;

    // The internal clock for chaining movementstates
    private Timer.Task stateChainTimer;

    public PlayerBase(float x, float y, KeyMap keyMap) {
        this.keyMap = keyMap;
        pos = new Vector2(x, y);
        bounds = new Rectangle(x, y, 0.6f, 0.8f);
        vel = new Vector2(0, 0);
        setState(PlayerState.IDLE);
        direction = RIGHT;
        grounded = true;
        canAirDash = true;
        createAnimations();
    }

    public void update(float deltaTime, MapBase map) {
        processKeys(deltaTime);
        tryMove(deltaTime, map);
        stateTime += deltaTime;
        updateAnimation();
    }

    private void setStateAndResetTimer(PlayerState state) {
        setState(state);
        if (stateChainTimer != null) {
            stateChainTimer.cancel();
        }
    }

    private void setState(PlayerState state) {
        if (this.state != state) {
            stateTime = 0;
            this.state = state;
        }
    }

    private void chainState(final PlayerState state1, float duration, final PlayerState state2) {
        if (state != state1) {
            setState(state1);
            // Stop the animation after it finishes and switch state to IDLE
            stateChainTimer = Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (state == state1) {
                        setState(state2);
                    }
                }
            }, duration);
        }
    }

    private void chainState(final PlayerState state1, float duration, final PlayerState state2, final float duration2, final PlayerState state3) {
        if (state != state1) {
            setState(state1);
            // Stop the animation after it finishes and switch state to IDLE
            stateChainTimer = Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (state == state1) {
                        chainState(state2, duration2, state3);
                    }
                }
            }, duration);
        }
    }

    // TODO: Refactor the below blocks to not have state modifications everywhere
    public void processKeys(float deltaTime) {
        // Reset button
        if (Gdx.input.isKeyPressed(keyMap.reset)) {
            vel.x = 0;
            vel.y = 0;
            setState(PlayerState.IDLE);
            canAirDash = true;
            grounded = true;
            isHoldingDash = false;
        }

        if (Gdx.input.isKeyPressed(keyMap.dash)) {
            if (Gdx.input.isKeyJustPressed(keyMap.jump)) {
                isHoldingDash = true;
                canAirDash = false;
            }
            if ((canAirDash || grounded) && state != PlayerState.WALLSLIDE
                    && Gdx.input.isKeyJustPressed(keyMap.dash)) {
                if (!grounded) {
                    canAirDash = false;
                }
                float duration = animations.getDashBreakLeft().getAnimationDuration();
                chainState(PlayerState.DASH, 0.5f, PlayerState.DASHBREAK, duration, PlayerState.IDLE);
            }
        } else {
            if (!grounded && state == PlayerState.DASH) {
                setStateAndResetTimer(PlayerState.FALL);
            }
            if (state == PlayerState.WALLSLIDE) {
                isHoldingDash = false;
            }
        }

        if (Gdx.input.isKeyPressed(keyMap.jump)) {
            if (grounded || state != PlayerState.FALL) {
                if (!(!grounded && state == PlayerState.DASH) &&
                        (state != PlayerState.JUMP && state != PlayerState.WALLJUMP && state != PlayerState.WALLSLIDE)) {
                    vel.y = VELOCITY_JUMP;
                }
                if (state == PlayerState.WALLSLIDE && Gdx.input.isKeyJustPressed(keyMap.jump)) {
                    setStateAndResetTimer(PlayerState.WALLJUMP);
                    vel.y = VELOCITY_JUMP;
                    vel.x = VELOCITY_X_WALLJUMP * direction;
                    if (isHoldingDash) {
                        vel.x -= VELOCITY_DASH_ADDITION * direction;
                    }
                }
                if ((!(!grounded && state == PlayerState.DASH) && state != PlayerState.WALLJUMP) &&
                        Gdx.input.isKeyJustPressed(keyMap.jump)) {
                    setStateAndResetTimer(PlayerState.JUMP);
                    grounded = false;
                }
            }
        } else if (!grounded && state != PlayerState.WALLSLIDE && state != PlayerState.DASH) {
            setStateAndResetTimer(PlayerState.FALL);
        }

        if (Gdx.input.isKeyPressed(keyMap.left)) {
            if (direction == RIGHT && ((!grounded && state == PlayerState.WALLSLIDE) || state == PlayerState.DASH)) {
                setStateAndResetTimer(PlayerState.FALL);
            }
            direction = LEFT;
            if (state == PlayerState.WALLJUMP) {
                if (vel.x > VELOCITY_WALK * direction) {
                    vel.x += VELOCITY_WALK * direction * deltaTime * 4;
                }
                if (isHoldingDash) {
                    vel.x += VELOCITY_DASH_ADDITION * direction * deltaTime * 4;
                }
            } else if (state != PlayerState.DASH){
                vel.x = VELOCITY_WALK * direction;
            }
        } else if (Gdx.input.isKeyPressed(keyMap.right)) {
            if (direction == LEFT && ((!grounded && state == PlayerState.WALLSLIDE) || state == PlayerState.DASH)) {
                setStateAndResetTimer(PlayerState.FALL);
            }
            direction = RIGHT;
            if (state == PlayerState.WALLJUMP) {
                if (vel.x < VELOCITY_WALK * direction) {
                    vel.x += VELOCITY_WALK * direction * deltaTime * 4;
                }
                if (isHoldingDash) {
                    vel.x += VELOCITY_DASH_ADDITION * direction * deltaTime * 4;
                }
            } else if (state != PlayerState.DASH) {
                vel.x = VELOCITY_WALK * direction;
            }
        } else {
            if (grounded && state != PlayerState.TOUCHDOWN && state != PlayerState.DASH && state != PlayerState.DASHBREAK) {
                setStateAndResetTimer(PlayerState.IDLE);
            }
            if (state == PlayerState.WALLSLIDE && !grounded) {
                setStateAndResetTimer(PlayerState.FALL);
            }
            if (!Gdx.input.isKeyPressed(keyMap.dash) && grounded && state == PlayerState.DASH) {
                float duration = animations.getDashBreakLeft().getAnimationDuration();
                chainState(PlayerState.DASHBREAK, duration, PlayerState.IDLE);
            }
            vel.x = 0;
        }
    }

    public void tryMove(float deltaTime, MapBase map) {
        // Apply gravity
        if (state != PlayerState.JUMP && state != PlayerState.WALLJUMP && state != PlayerState.WALLSLIDE) {
            if (vel.y > 0) {
                vel.y = 0;
            }
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            }
        }

        if (state == PlayerState.WALLSLIDE) {
            vel.y = map.WALLSLIDE_FALLSPEED;
        }

        if (state == PlayerState.DASH) {
            vel.x = (VELOCITY_WALK + VELOCITY_DASH_ADDITION) * direction;
            if (!grounded) {
                vel.y = 0;
            }
        } else if (isHoldingDash && !grounded && vel.x != 0 && state != PlayerState.WALLJUMP) {
            vel.x += VELOCITY_DASH_ADDITION * direction;
        }

        // Collision checking here
        List<Collision> collisionList = physics.getMapCollision(this, deltaTime, map).toList;

        for (Collision collision: collisionList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case UP:
                    vel.y = 0;
                    if ((vel.x == 0 && state == PlayerState.FALL) || state == PlayerState.WALLSLIDE) {
                        float duration = animations.getTouchdownLeft().getAnimationDuration();
                        chainState(PlayerState.TOUCHDOWN, duration, PlayerState.IDLE);
                    }
                    canAirDash = true;
                    grounded = true;
                    isHoldingDash = false;
                    pos.y = preCollide.y;
                    break;
                case DOWN:
                    vel.y = 0;
                    setStateAndResetTimer(PlayerState.FALL);
                    pos.y = preCollide.y;
                    break;
                case LEFT:
                case RIGHT:
                    vel.x = 0;
                    boolean collideFromDistance = true;
                    if (pos.x == preCollide.x) {
                        collideFromDistance = false;
                    }
                    pos.x = preCollide.x;
                    if (grounded && state != PlayerState.TOUCHDOWN) {
                        if (state == PlayerState.DASH && collideFromDistance) {
                            float duration = animations.getDashBreakLeft().getAnimationDuration();
                            chainState(PlayerState.DASHBREAK, duration, PlayerState.IDLE);
                        } else if (state != PlayerState.DASHBREAK){
                            setStateAndResetTimer(PlayerState.IDLE);
                        }
                    } else if (state == PlayerState.FALL || state == PlayerState.DASH) {
                        // TODO: Needs to find a way: In megaman X4, wall sliding starts after 50% of jump animation
                        setState(PlayerState.WALLSLIDE);
                        canAirDash = true;
                        isHoldingDash = false;
                    }
                    break;
            }
        }

        if (collisionList.isEmpty() && state == PlayerState.WALLSLIDE) {
            setStateAndResetTimer(PlayerState.FALL);
        }

        // if jumping, apply gravity
        if (state == PlayerState.JUMP || state == PlayerState.WALLJUMP) {
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            } else {
                vel.y = map.MAX_FALLSPEED;
            }
        }

        // player is falling if going downwards
        if (vel.y < 0 && state != PlayerState.WALLSLIDE) {
            setStateAndResetTimer(PlayerState.FALL);
            grounded = false;
        }

        if (grounded && vel.x != 0 && state != PlayerState.DASH) {
            setStateAndResetTimer(PlayerState.RUN);
        }

        pos.x += vel.x * deltaTime;
        pos.y += vel.y * deltaTime;
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    public void updateAnimation() {
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
            if (direction == LEFT) {
                currentAnimation = animations.getDashLeft();
            } else {
                currentAnimation = animations.getDashRight();
            }
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

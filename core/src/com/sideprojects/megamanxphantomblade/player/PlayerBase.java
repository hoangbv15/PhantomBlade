package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.Collision;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase extends MovingObject {
    private Vector2 originPos;

    // States
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int FALL = 3;
    public static final int TOUCHDOWN = 4;
    public static final int WALLSLIDE = 5;
    public static final int WALLJUMP = 6;
    // Velocities
    private static final float VELOCITY_WALK = 4f;
    private static final float VELOCITY_JUMP = 6f;
    private static final float VELOCITY_X_WALLJUMP = -3f;

    public int state;
    public boolean grounded;
    public float stateTime;

    public Animation<TextureRegion> playerRunRight;
    public Animation<TextureRegion> playerRunLeft;
    public Animation<TextureRegion> playerIdleRight;
    public Animation<TextureRegion> playerIdleLeft;
    public Animation<TextureRegion> playerJumpLeft;
    public Animation<TextureRegion> playerJumpRight;
    public Animation<TextureRegion> playerFallLeft;
    public Animation<TextureRegion> playerFallRight;
    public Animation<TextureRegion> playerTouchdownLeft;
    public Animation<TextureRegion> playerTouchdownRight;
    public Animation<TextureRegion> playerWallSlideLeft;
    public Animation<TextureRegion> playerWallSlideRight;
    public Animation<TextureRegion> playerWallJumpLeft;
    public Animation<TextureRegion> playerWallJumpRight;
    public TextureRegion currentFrame;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        bounds = new Rectangle(x, y, 0.6f, 0.8f);
        vel = new Vector2(0, 0);
        setState(IDLE);
        direction = RIGHT;
        grounded = true;
        createAnimations();

        originPos = new Vector2(x, y);
    }

    public void update(float deltaTime, MapBase map) {
        processKeys(deltaTime);
        tryMove(deltaTime, map);
        stateTime += deltaTime;
        updateAnimation();
    }

    // TODO: Refactor the below blocks to not have state modifications everywhere
    private void processKeys(float deltaTime) {
        // Reset button
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pos.x = originPos.x;
            pos.y = originPos.y;
            vel.x = 0;
            vel.y = 0;
            setState(IDLE);
            grounded = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (state != FALL) {
                if (state != JUMP && state != WALLJUMP && state != WALLSLIDE) {
                    vel.y = VELOCITY_JUMP;
                }
                if (state == WALLSLIDE && Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                    setState(WALLJUMP);
                    vel.y = VELOCITY_JUMP;
                    vel.x = VELOCITY_X_WALLJUMP * direction;
                }
                if (state != WALLJUMP && Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                    setState(JUMP);
                    grounded = false;
                }
            }
        } else if (!grounded && state != WALLSLIDE) {
            setState(FALL);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (direction == RIGHT && state == WALLSLIDE && !grounded) {
                setState(FALL);
            }
            direction = LEFT;
            if (state == WALLJUMP) {
                if (vel.x > VELOCITY_WALK * direction) {
                    vel.x += VELOCITY_WALK * direction * deltaTime * 4;
                }
            } else {
                vel.x = VELOCITY_WALK * direction;
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (direction == LEFT && state == WALLSLIDE && !grounded) {
                setState(FALL);
            }
            direction = RIGHT;
            if (state == WALLJUMP) {
                if (vel.x < VELOCITY_WALK * direction) {
                    vel.x += VELOCITY_WALK * direction * deltaTime * 4;
                }
            } else {
                vel.x = VELOCITY_WALK * direction;
            }
        } else {
            if (grounded && state != TOUCHDOWN) {
                setState(IDLE);
            }
            vel.x = 0;
            if (state == WALLSLIDE && !grounded) {
                setState(FALL);
            }
        }
    }

    private void setState(int state) {
        if (this.state != state) {
            stateTime = 0;
            this.state = state;
        }
    }

    private void chainState(final int state1, float duration, final int state2) {
        if (state != state1) {
            setState(state1);
            // Stop the animation after it finishes and switch state to IDLE
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (state == state1) {
                        setState(state2);
                    }
                }
            }, duration);
        }
    }

    private void updateAnimation() {
        Animation<TextureRegion> currentAnimation;
        if (state == IDLE) {
            if (direction == LEFT) {
                currentAnimation = playerIdleLeft;
            } else {
                currentAnimation = playerIdleRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else if (state == RUN) {
            if (direction == LEFT) {
                currentAnimation = playerRunLeft;
            } else {
                currentAnimation = playerRunRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else if (state == JUMP) {
            if (direction == LEFT) {
                currentAnimation = playerJumpLeft;
            } else {
                currentAnimation = playerJumpRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == FALL) {
            if (direction == LEFT) {
                currentAnimation = playerFallLeft;
            } else {
                currentAnimation = playerFallRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == TOUCHDOWN) {
            if (direction == LEFT) {
                currentAnimation = playerTouchdownLeft;
            } else {
                currentAnimation = playerTouchdownRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == WALLSLIDE) {
            if (direction == LEFT) {
                currentAnimation = playerWallSlideLeft;
            } else {
                currentAnimation = playerWallSlideRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else if (state == WALLJUMP) {
            if (direction == LEFT) {
                currentAnimation = playerWallJumpLeft;
            } else {
                currentAnimation = playerWallJumpRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        }
    }

    private void tryMove(float deltaTime, MapBase map) {
        // Apply gravity
        if (state != JUMP && state != WALLJUMP && state != WALLSLIDE) {
            if (vel.y > 0) {
                vel.y = 0;
            }
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            }
        }

        if (state == WALLSLIDE) {
            vel.y = map.WALLSLIDE_FALLSPEED;
        }

        // Collision checking here
        List<Collision> collisionList = map.mapCollisionCheck(this, deltaTime);

        for (Collision collision: collisionList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case UP:
                    vel.y = 0;
                    if ((vel.x == 0 && state == FALL) || state == WALLSLIDE) {
                        float duration = playerTouchdownLeft.getAnimationDuration();
                        chainState(TOUCHDOWN, duration, IDLE);
                    }
                    grounded = true;
                    pos.y = preCollide.y;
                    break;
                case DOWN:
                    vel.y = 0;
                    setState(FALL);
                    pos.y = preCollide.y;
                    break;
                case LEFT:
                case RIGHT:
                    vel.x = 0;
                    pos.x = preCollide.x;
                    if (grounded && state != TOUCHDOWN) {
                        setState(IDLE);
                    } else if (state == FALL) {
                        // TODO: Needs to find a way: In megaman X4, wall sliding starts after 50% of jump animation
                        setState(WALLSLIDE);
                    }
                    break;
            }
        }

        if (collisionList.isEmpty() && state == WALLSLIDE) {
            setState(FALL);
        }

        // if jumping, apply gravity
        if (state == JUMP || state == WALLJUMP) {
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            } else {
                vel.y = map.MAX_FALLSPEED;
            }
        }

        // player is falling if going downwards
        if (vel.y < 0 && state != WALLSLIDE) {
            setState(FALL);
            grounded = false;
        }

        if (grounded && vel.x != 0) {
            setState(RUN);
        }

        pos.x += vel.x * deltaTime;
        pos.y += vel.y * deltaTime;
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    public abstract void createAnimations();
}


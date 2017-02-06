package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase {
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int FALL = 3;

    public static final int LEFT = -1;
    private static final int RIGHT = 1;

    private static final float VELOCITY_WALK = 20f;
    private static final float VELOCITY_JUMP = 6f;

    public int state;
    public int direction;
    public boolean grounded;

    public Vector2 pos;
    public Vector2 vel;
    public Rectangle bounds;
    public float stateTime;

    public Animation<TextureRegion> playerRunRight;
    public Animation<TextureRegion> playerRunLeft;
    public Animation<TextureRegion> playerIdleRight;
    public Animation<TextureRegion> playerIdleLeft;
    public TextureRegion currentFrame;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        bounds = new Rectangle(x, y, 1f, 1);
        vel = new Vector2(0, 0);
        stateTime = 0;
        state = IDLE;
        direction = RIGHT;
        grounded = true;
        createAnimations();
    }

    public void update(float deltaTime, MapBase map) {
        processKeys();
        tryMove(deltaTime, map);
        stateTime += deltaTime;
        updateAnimation();
    }

    private void processKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (state != JUMP) {
                state = JUMP;
                vel.y = VELOCITY_JUMP;
                grounded = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (state != JUMP) {
                state = RUN;
            }
            direction = LEFT;
            vel.x = VELOCITY_WALK * LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (state != JUMP) {
                state = RUN;
            }
            direction = RIGHT;
            vel.x = VELOCITY_WALK * RIGHT;
        } else {
            if (state != JUMP) {
                state = IDLE;
            }
            vel.x = 0;
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
                currentAnimation = playerRunLeft;
            } else {
                currentAnimation = playerRunRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        } else if (state == FALL) {
            if (direction == LEFT) {
                currentAnimation = playerIdleLeft;
            } else {
                currentAnimation = playerIdleRight;
            }
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }
    }

    private void tryMove(float deltaTime, MapBase map) {
        if (vel.x == 0 && vel.y == 0) {
            return;
        }
        // Apply gravity
        if (grounded) {
            vel.y = map.MAX_FALLSPEED;
        }
        // Collision checking here
        collisionCheck(deltaTime, map);

        // if jumping, apply gravity
        if (!grounded) {
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            } else {
                vel.y = map.MAX_FALLSPEED;
            }
        }

        // player is falling if going downwards
        if (vel.y < 0) {
            state = FALL;
        }

        pos.x += vel.x * deltaTime;
        pos.y += vel.y * deltaTime;
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    private void collisionCheck(float deltaTime, MapBase map) {
        OptionalInt xCollision = isXCollidingWithMap(deltaTime, map);

        if (xCollision.isPresent()) {
            state = IDLE;
            if (vel.x > 0) {
                pos.x = xCollision.getAsInt() - bounds.width;
            } else {
                pos.x = xCollision.getAsInt() + 1;
            }
            vel.x = 0;
        }

        OptionalInt yCollision = isYCollidingWithMap(deltaTime, map);

        if (yCollision.isPresent()) {
            if (vel.y > 0) {
                pos.y = yCollision.getAsInt() - bounds.height;
            } else {
                pos.y = yCollision.getAsInt() + 1;
            }
            vel.y = 0;
            grounded = true;
        }




//        int x = (int)Math.floor(bounds.x);
//        int y = (int)Math.floor(pos.y);
//        int xJustBefore = x + 1;
//        if (x > pos.x) {
//            x = (int) Math.ceil(bounds.x);
//            xJustBefore = x - 1;
//        }
//        if (x < 0 || x >= map.tiles.length ||
//                map.match(map.tiles[x][y], MapBase.TILE)) {
//            state = IDLE;
//            vel.x = 0;
//            pos.x = xJustBefore;
//        }
//
//        x = (int)Math.floor(pos.x);
//        y = (int)Math.floor(bounds.y);
//        int yJustBefore = y + 1;
//        if (y > pos.y) {
//            y = (int) Math.ceil(bounds.y);
//            yJustBefore = y - 1;
//        }
//
//        if (y < 0 || y >= map.tiles[0].length ||
//                map.match(map.tiles[x][y], MapBase.TILE)) {
//            vel.y = 0;
//            pos.y = yJustBefore;
//            grounded = true;
//        }
    }

    private OptionalInt isXCollidingWithMap(float deltaTime, MapBase map) {
        int x = (int)Math.floor(pos.x);
        int y = (int)Math.floor(pos.y);
        int xAfter = (int)Math.floor(bounds.x + vel.x * deltaTime);
        IntStream path;
        if (vel.x > 0) {
            x = (int) Math.ceil(bounds.x + bounds.getWidth());
            xAfter = (int) Math.ceil(bounds.x  + bounds.getWidth() + vel.x * deltaTime);
            path = IntStream.range(x, xAfter);
        } else {
            path = IntStream.range(xAfter, x);
        }

        return path.filter(i -> {
            if (i < 0 || i >= map.tiles.length) {
                return true;
            } else {
                Rectangle newBounds = new Rectangle(i, pos.y, bounds.width, bounds.height);
                if (map.bounds[i][y] == null) {
                    return false;
                }
                return newBounds.overlaps(map.bounds[i][y]);
            }
        }).findFirst();
    }

    private OptionalInt isYCollidingWithMap(float deltaTime, MapBase map) {
        int x = (int)Math.floor(pos.x);
        int y = (int)Math.floor(pos.y);
        int yAfter = (int)Math.floor(bounds.y + vel.y * deltaTime);
        IntStream path;
        if (vel.y > 0) {
            y = (int) Math.ceil(bounds.y + bounds.getHeight());
            yAfter = (int) Math.ceil(bounds.y  + bounds.getWidth() + vel.y * deltaTime);
            path = IntStream.range(y, yAfter);
        } else {
            path = IntStream.range(yAfter, y);
        }

        return path.filter(i -> {
            if (i < 0 || i >= map.tiles[0].length) {
                return true;
            } else {
                Rectangle newBounds = new Rectangle(pos.x, i, bounds.width, bounds.height);
                if (map.bounds[x][i] == null) {
                    return false;
                }
                return newBounds.overlaps(map.bounds[x][i]);
            }
        }).findFirst();
    }

    public abstract void createAnimations();
}


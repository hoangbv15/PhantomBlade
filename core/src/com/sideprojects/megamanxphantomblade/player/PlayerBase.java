package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.map.MapBase;

import java.util.OptionalInt;

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

    private static final float VELOCITY_WALK = 6f;
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
        bounds = new Rectangle(x, y, 0.6f, 1);
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
//        if (vel.x == 0 && vel.y == 0) {
//            return;
//        }
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
                vel.y = 0;
                pos.y = yCollision.getAsInt() + 1;
            }
            grounded = true;
        }
    }

    private OptionalInt isXCollidingWithMap(float deltaTime, MapBase map) {
        if (vel.x == 0) {
            return OptionalInt.empty();
        }
        float step = vel.x * deltaTime;
        // First, check if direction is left or right
        if (vel.x < 0) {
            // Player is going left
            if (pos.x - (int)pos.x < Math.abs(step)) {
                // This means the player is about to move out of the current tile
                // In that case, check if the tile on the left is walkable.
                int x = (int)pos.x - 1;
                int y = (int)pos.y;
                if (isTileCollidable(x, y, map)) {
                   return OptionalInt.of(x);
                }
            }
            return OptionalInt.empty();
        }
        // Player is going right
        if ((int)Math.ceil(pos.x) - pos.x - bounds.width < step) {
            // This means the player is about to move out of the current tile
            // Check if the tile on the right is walkable
            int x = (int)pos.x + 1;
            int y = (int)pos.y;
            if (isTileCollidable(x, y, map)) {
                return OptionalInt.of(x);
            }
        }
        return OptionalInt.empty();
    }

    private OptionalInt isYCollidingWithMap(float deltaTime, MapBase map) {
        if (vel.y == 0) {
            return OptionalInt.empty();
        }
        float step = vel.y * deltaTime;
        // First, check if direction is up or down
        if (vel.y < 0) {
            // Player is going down
            if (pos.y - (int)pos.y < Math.abs(step)) {
                // This means the player is about to move out of the current tile
                // In that case, check if the tile below is walkable.
                int y = (int)pos.y - 1;
                int x = (int)pos.x;
                // This is to include the adjacent tile if the player is between 2 tiles
                float x2Float = pos.x + bounds.width;
                int x2 = (int)x2Float;
                if (isTileCollidable(x, y, map) || (x2Float != x2 && isTileCollidable(x2, y, map))) {
                    return OptionalInt.of(y);
                }
            }
            return OptionalInt.empty();
        }
        // Player is going up
        if ((int)Math.ceil(pos.y) - pos.y - bounds.height < step) {
            // This means the player is about to move out of the current tile
            // Check if the tile above is walkable
            int y = (int)pos.y + 1;
            int x = (int)pos.x;
            // This is to include the adjacent tile if the player is between 2 tiles
            float x2Float = pos.x + bounds.width;
            int x2 = (int)x2Float;
            if (isTileCollidable(x, y, map) || (x2Float != x2 && isTileCollidable(x2, y, map))) {
                return OptionalInt.of(y);
            }
        }
        return OptionalInt.empty();
    }

    private boolean isTileCollidable(int x, int y, MapBase map) {
        if (x < 0 || y < 0 || x > map.tiles.length || y > map.tiles[0].length) {
            return true;
        }

        return map.bounds[x][y] != null;
    }

    public abstract void createAnimations();
}


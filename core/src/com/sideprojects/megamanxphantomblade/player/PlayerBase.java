package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.map.MapBase;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase {
    public static final int IDLE = 0;
    public static final int RUN = 1;

    public static final int LEFT = -1;
    private static final int RIGHT = 1;

    private static final float VELOCITY_X = 0.1f;

    public int state;
    public int direction;

    public Vector2 pos;
    private Vector2 vel;
    private Vector2 bounds;
    public float stateTime;

    public Animation<TextureRegion> playerRunRight;
    public Animation<TextureRegion> playerRunLeft;
    public Animation<TextureRegion> playerIdleRight;
    public Animation<TextureRegion> playerIdleLeft;
    public TextureRegion currentFrame;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        bounds = new Vector2(x, y);
        vel = new Vector2(0, 0);
        stateTime = 0;
        state = IDLE;
        direction = RIGHT;
        createAnimations();
    }

    public void update(float deltaTime, MapBase map) {
        processKeys();
        tryMove(map);
        stateTime += deltaTime;
        updateAnimation();
    }

    private void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            state = RUN;
            direction = LEFT;
            vel.x = VELOCITY_X * LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            state = RUN;
            direction = RIGHT;
            vel.x = VELOCITY_X * RIGHT;
        } else {
            state = IDLE;
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
        }
    }

    private void tryMove(MapBase map) {
        if (vel.x == 0 && vel.y == 0) {
            return;
        }
        // Collision checking here
        bounds.x += vel.x;
        bounds.y += vel.y;
        int x = (int)Math.floor(bounds.x);
        int y = (int)Math.floor(bounds.y);
        if (x < 0 || y < 0 || x > map.tiles.length || y > map.tiles[0].length ||
            map.match(map.tiles[x][y], MapBase.TILE)) {
            state = IDLE;
            vel.x = 0;
        }

        pos.x += vel.x;
        pos.y += vel.y;
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    public abstract void createAnimations();
}

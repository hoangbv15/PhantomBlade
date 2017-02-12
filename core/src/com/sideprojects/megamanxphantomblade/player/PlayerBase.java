package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.math.GeoMath;
import com.sideprojects.megamanxphantomblade.math.NumberMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class PlayerBase {
    // Debug property, used for rendering collisions to the screen
    public List<Collision> collisions;
    private Vector2 originPos;

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
    public Animation<TextureRegion> playerJumpLeft;
    public Animation<TextureRegion> playerJumpRight;
    public Animation<TextureRegion> playerFallLeft;
    public Animation<TextureRegion> playerFallRight;
    public TextureRegion currentFrame;

    public PlayerBase(float x, float y) {
        pos = new Vector2(x, y);
        bounds = new Rectangle(x, y, 0.6f, 0.8f);
        vel = new Vector2(0, 0);
        setState(IDLE);
        direction = RIGHT;
        grounded = true;
        createAnimations();

        collisions = new ArrayList<Collision>();
        originPos = new Vector2(x, y);
    }

    public void update(float deltaTime, MapBase map) {
        processKeys();
        tryMove(deltaTime, map);
        stateTime += deltaTime;
        updateAnimation();
    }

    private void processKeys() {
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
                if (state != JUMP) {
                    vel.y = VELOCITY_JUMP;
                }
                setState(JUMP);
                grounded = false;
            }
        } else if (!grounded) {
            setState(FALL);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (grounded) {
                setState(RUN);
            }
            direction = LEFT;
            vel.x = VELOCITY_WALK * LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (grounded) {
                setState(RUN);
            }
            direction = RIGHT;
            vel.x = VELOCITY_WALK * RIGHT;
        } else {
            if (grounded) {
                setState(IDLE);
            }
            vel.x = 0;
        }
    }

    private void setState(int state) {
        if (this.state != state) {
            stateTime = 0;
            this.state = state;
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
        }
    }

    private void tryMove(float deltaTime, MapBase map) {
        // Apply gravity
        if (state != JUMP) {
            if (vel.y > 0) {
                vel.y = 0;
            }
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            }
        }
        // Collision checking here
        collisionCheck(deltaTime, map);

        // if jumping, apply gravity
        if (state == JUMP) {
            if (vel.y > map.MAX_FALLSPEED) {
                vel.y -= map.GRAVITY * deltaTime;
            } else {
                vel.y = map.MAX_FALLSPEED;
            }
        }

        // player is falling if going downwards
        if (vel.y < 0) {
            setState(FALL);
        }

        pos.x += vel.x * deltaTime;
        pos.y += vel.y * deltaTime;
        bounds.x = pos.x;
        bounds.y = pos.y;
    }

    private void collisionCheck(float deltaTime, MapBase map) {
        collisions.clear();
        // From inside out, find the first tile that collides with the player
        float stepX = vel.x * deltaTime;
        float stepY = vel.y * deltaTime;
        // Translate the start and end vectors depending on the direction of the movement
        float paddingX = 0;
        float paddingY = 0;
        if (stepX > 0) {
            paddingX = bounds.width;
        }
        if (stepY > 0) {
            paddingY = bounds.height;
        }
        Vector2 endPosX = new Vector2(pos.x + stepX, pos.y);
        Vector2 endPosY = new Vector2(pos.x, pos.y + stepY);

        // Setup collision detection rays
        List<CollisionDetectionRay> detectionRayList = new ArrayList<CollisionDetectionRay>(0);
        detectionRayList.add(new CollisionDetectionRay(pos, endPosX, paddingX, 0));
        detectionRayList.add(new CollisionDetectionRay(pos, endPosX, paddingX, bounds.height));
        detectionRayList.add(new CollisionDetectionRay(pos, endPosY, 0, paddingY));
        detectionRayList.add(new CollisionDetectionRay(pos, endPosY, bounds.width, paddingY));


        // Loop through map and use collision detection rays to detect...well..collisions.
        int xStart = (int)pos.x;
        int yStart = (int)pos.y;
        if (direction == LEFT) {
            xStart += 1;
        }
        int xEnd = (int)(endPosX.x + paddingX);
        if (direction == RIGHT) {
            xEnd += 1;
        }
        int yEnd = (int)(endPosY.y + paddingY);

        // Loop through the rectangular area that the speed vector occupies
        // Get a list of all collisions with map tiles in the area
        // Identify the collision nearest to the player
        List<Collision> collisionList = new ArrayList<Collision>(0);
        for (int y = yStart; NumberMath.hasNotExceeded(y, yStart, yEnd); y = NumberMath.iteratorNext(y, yStart, yEnd)) {
            for (int x = xStart; NumberMath.hasNotExceeded(x, xStart, xEnd); x = NumberMath.iteratorNext(x, xStart, xEnd)) {
                for (CollisionDetectionRay ray: detectionRayList) {
                    Collision collision = getCollisionVector(x, y, ray, map);
                    if (collision != null) {
                        collisionList.add(collision);
                        collisions.add(collision);
                    }
                }
            }
        }

        for (Collision collision: collisionList) {
            Vector2 preCollide = collision.getPrecollidePos();
            switch (collision.side) {
                case UP:
                    vel.y = 0;
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
                    if (grounded) {
                        setState(IDLE);
                    }
                    break;
            }
        }
    }

    private Collision getCollisionVector(int x, int y, CollisionDetectionRay ray, MapBase map) {
        Rectangle tile = map.getCollidableBox(x, y);
        if (tile == null) {
            return null;
        }

        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();

        // Find intersection on each side of the tile
        Collision left = new Collision(GeoMath.findIntersectionLeft(tile, start, end), Collision.Side.LEFT, ray, tile);
        Collision right = new Collision(GeoMath.findIntersectionRight(tile, start, end), Collision.Side.RIGHT, ray, tile);
        Collision up = new Collision(GeoMath.findIntersectionUp(tile, start, end), Collision.Side.UP, ray, tile);
        Collision down = new Collision(GeoMath.findIntersectionDown(tile, start, end), Collision.Side.DOWN, ray, tile);

        // Put non-null ones in an array, then sort by distance to start
        List<Collision> collisionList = new ArrayList<Collision>(0);
        if (left.point != null) collisionList.add(left);
        if (right.point != null) collisionList.add(right);
        if (up.point != null) collisionList.add(up);
        if (down.point != null) collisionList.add(down);

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList, start);
    }

    public abstract void createAnimations();
}


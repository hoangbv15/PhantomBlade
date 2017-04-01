package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 27/03/17.
 */
public class XBuster extends PlayerAttack {
    // Length of 1 pixel
    private static float P(int p) {
        return p / 62f;
    }

    // Run padding
    private static float[] xRunPosPadding = new float[] {
            -P(0), -P(0), -P(0), -P(1), -P(1), -P(1), -P(0), P(1), P(1), P(1), P(2), P(2), P(2), P(1),
    };

    private static float[] yRunPosPadding = new float[] {
            P(3), P(4), P(3), P(2), P(1), P(2), P(3), P(4), P(3), P(2), P(1), 0, P(1), P(2)
    };

    // Dash padding
    private static float[] yDashPosPadding = new float[] {
            P(23), P(17), P(12)
    };

    // Dashbreak padding
    private static float[] xDashBreakPosPadding = new float[] {
            P(13), P(10), 0, P(11)
    };

    private static float[] yDashBreakPosPadding = new float[] {
            P(25), P(25), P(24)
    };



    private float muzzleTime;
    private Vector2 posPadding;
    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> muzzleAnimation;
    private Animation<TextureRegion> explodeAnimation;
    private boolean explode;
    private Vector2 explodePosPadding;
    private int playerStartDirection;
    private boolean stopUpdatingMuzzlePos;

    public XBuster(PlayerBase player, Damage damage, int direction, PlayerAnimation animations) {
        super(damage, direction);
        // Calculate position for bullet
        posPadding = getBulletPositionPadding(player, direction, player.currentFrameIndex());
        pos = new Vector2();
        vel = new Vector2(0, 0);
        pos.x = player.bounds.x + posPadding.x;
        pos.y = player.bounds.y + posPadding.y;
        playerStartDirection = player.direction;
        posPadding = getMuzzlePositionPadding(player, player.currentFrameIndex());
        muzzlePos = new Vector2();
        muzzlePos.x = player.bounds.x + posPadding.x;
        muzzlePos.y = player.bounds.y + posPadding.y;
        bounds = new Rectangle(pos.x, pos.y, 0.1f, P(8));
        initialiseHealthPoints(10);
        explode = false;
        stopUpdatingMuzzlePos = false;
        stateTime = 0;
        createAnimation(animations);
    }

    private void createAnimation(PlayerAnimation animations) {
        switch(damage.type) {
            case Light:
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmall, direction, Sprites.XBulletSmall, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallMuzzle, direction, Sprites.XShootMuzzle, null, 0.042f);
                muzzleTime = muzzleAnimation.getAnimationDuration();
                explodeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallExplode, direction, Sprites.XBulletSmallExplode, null, 0.05f);
                explodePosPadding = VectorCache.get(-0.2f, -0.5f);
                break;
        }
    }

    @Override
    public void update(PlayerBase player, float delta) {
        stateTime += delta;
        if (isDead()) {
            if (!explode) {
                stateTime = 0;
                explode = true;
                pos.x += explodePosPadding.x;
                pos.y += explodePosPadding.y;
            }
            currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
            if (explodeAnimation.isAnimationFinished(stateTime)) {
                shouldBeRemoved = true;
            }
        } else {
            currentFrame = animation.getKeyFrame(stateTime, true);
            vel.x = 8 * direction;
            pos.x += vel.x * delta;
            bounds.x = pos.x;
        }
        if (stateTime <= muzzleTime && !explode) {// && player.state != PlayerState.IDLE) {
            muzzleFrame = muzzleAnimation.getKeyFrame(stateTime, false);
            if (player.direction != playerStartDirection) {
                stopUpdatingMuzzlePos = true;
            }
            if (!stopUpdatingMuzzlePos) {
                posPadding = getMuzzlePositionPadding(player, player.currentFrameIndex());
                muzzlePos.x = player.bounds.x + posPadding.x;
                muzzlePos.y = player.bounds.y + posPadding.y;
            }
        } else {
            muzzleFrame = null;
        }
    }

    private Vector2 getBulletPositionPadding(PlayerBase player, int bulletDirection, int frameIndex) {
        float paddingY = P(27);
        float paddingX = player.bounds.width;
        if (bulletDirection == MovingObject.LEFT) {
            paddingX = -P(12);
        }

        switch(player.state) {
            case RUN:
                paddingX = paddingX + getPadding(xRunPosPadding, frameIndex) * bulletDirection;
                paddingY = paddingY + getPadding(yRunPosPadding, frameIndex);
                break;
            case WALLJUMP:
            case JUMP:
            case FALL:
                paddingY = P(35);
                paddingX = paddingX + P(2);
                if (bulletDirection == MovingObject.LEFT) {
                    paddingX = -P(13);
                }
                if (player.state == PlayerState.FALL) {
                    paddingY = P(34);
                }
                break;
            case DASH:
                paddingX = paddingX + P(2) * bulletDirection;
                paddingY = getPadding(yDashPosPadding, frameIndex);
                break;
            case DASHBREAK:
                paddingX = paddingX + getPadding(xDashBreakPosPadding, frameIndex) * bulletDirection;
                paddingY = getPadding(yDashBreakPosPadding, frameIndex);
                break;
            case WALLSLIDE:
                paddingY = P(29);
                break;
        }

        return VectorCache.get(paddingX, paddingY);
    }

    private static float getPadding(float[] padding, int i) {
        if (i >= padding.length) {
            return padding[padding.length - 1];
        }
        return padding[i];
    }

    private Vector2 getMuzzlePositionPadding(PlayerBase player, int frameIndex) {
        Vector2 padding = getBulletPositionPadding(player, player.direction, frameIndex);
        float paddingX = padding.x;
        float paddingY = padding.y - P(8);
        switch(player.state) {
            case WALLSLIDE:
                if (player.direction == LEFT) {
                    paddingX = player.bounds.width + P(5);
                } else {
                    paddingX = -P(17);
                }
                break;
        }
        if (player.direction == LEFT) {
            paddingX -= P(8);
        }
        padding = VectorCache.get(paddingX, paddingY);
        return padding;
    }
}

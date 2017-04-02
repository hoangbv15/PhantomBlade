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
import com.sideprojects.megamanxphantomblade.player.PlayerSound;

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
    // Time it takes for muzzle to transform to bullet
    private float muzzleToBulletTime;
    private Vector2 posPadding;
    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> muzzleAnimation;
    private Animation<TextureRegion> explodeAnimation;
    private boolean explode;
    private float explodePosPaddingY;
    private float muzzlePosPaddingY;
    private float bulletPosPaddingY;
    private float explodePosPaddingXLeft;
    private float muzzlePosPaddingXLeft;
    private float bulletPosPaddingXLeft;
    private float explodePosPaddingXRight;
    private float muzzlePosPaddingXRight;
    private float bulletPosPaddingXRight;
    private int playerStartDirection;
    private boolean stopUpdatingMuzzlePos;

    private PlayerSound playerSound;

    public XBuster(PlayerBase player, Damage damage, int direction, PlayerAnimation animations, PlayerSound playerSound) {
        super(damage, direction);
        this.playerSound = playerSound;
        // Calculate position for bullet
        createAnimation(animations);
        pos = new Vector2();
        vel = new Vector2(0, 0);
        playerStartDirection = player.direction;
        posPadding = getMuzzlePositionPadding(player, player.currentFrameIndex());
        muzzlePos = new Vector2();
        muzzlePos.x = player.bounds.x + posPadding.x;
        muzzlePos.y = player.bounds.y + posPadding.y;
        bounds = new Rectangle(pos.x, pos.y, 0.1f, P(8));
        explode = false;
        stopUpdatingMuzzlePos = false;
        stateTime = 0;
    }

    private void createAnimation(PlayerAnimation animations) {
        switch(damage.type) {
            case Heavy:
                initialiseHealthPoints(100);
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletHeavy, direction, Sprites.XBulletHeavy, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletHeavyMuzzle, direction, Sprites.XShootHeavyMuzzle, null, 0.04f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallExplode, direction, Sprites.XBulletSmallExplode, null, 0.05f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = -P(15);
                explodePosPaddingY = -P(25);
                muzzlePosPaddingXLeft = P(38);
                muzzlePosPaddingXRight = -P(36);
                muzzlePosPaddingY = -P(25);
                bulletPosPaddingXLeft = -P(39);
                bulletPosPaddingXRight = 0;
                bulletPosPaddingY = -P(10);
                break;
            case Normal:
                initialiseHealthPoints(10);
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletMedium, direction, Sprites.XBulletMedium, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletMediumMuzzle, direction, Sprites.XShootMediumMuzzle, null, 0.03f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallExplode, direction, Sprites.XBulletSmallExplode, null, 0.05f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = -P(15);
                explodePosPaddingY = -P(25);
                muzzlePosPaddingXLeft = 0;
                muzzlePosPaddingXRight = 0;
                muzzlePosPaddingY = -P(2);
                bulletPosPaddingXLeft = -P(8);
                bulletPosPaddingXRight = P(4);
                bulletPosPaddingY = -P(4);
                break;
            case Light:
                initialiseHealthPoints(10);
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmall, direction, Sprites.XBulletSmall, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallMuzzle, direction, Sprites.XShootMuzzle, null, 0.025f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallExplode, direction, Sprites.XBulletSmallExplode, null, 0.05f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = -P(15);
                explodePosPaddingY = -P(30);
                muzzlePosPaddingXLeft = 0;
                muzzlePosPaddingXRight = 0;
                muzzlePosPaddingY = -P(9);
                bulletPosPaddingXLeft = 0;
                bulletPosPaddingXRight = 0;
                bulletPosPaddingY = P(1);
                break;
        }
        muzzleTime = muzzleAnimation.getAnimationDuration();
        muzzleToBulletTime = muzzleAnimation.getFrameDuration() * 3;
    }

    @Override
    public void update(PlayerBase player, float delta) {
        stateTime += delta;
        if (isDead()) {
            if (!explode) {
                stateTime = 0;
                explode = true;
                pos.x += direction == LEFT ? explodePosPaddingXLeft : explodePosPaddingXRight;
                pos.y += explodePosPaddingY;
                playerSound.playBulletHit();
            }
            currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
            if (explodeAnimation.isAnimationFinished(stateTime)) {
                shouldBeRemoved = true;
            }
        } else {
            if (stateTime <= muzzleToBulletTime) {
                posPadding = getBulletPositionPadding(player, direction, player.currentFrameIndex());
                pos.x = player.bounds.x + posPadding.x;
                pos.y = player.bounds.y + posPadding.y;
                bounds.x = pos.x;
                bounds.y = pos.y;
            } else {
                pos.x += vel.x * delta;
                vel.x = 8 * direction;
                bounds.x = pos.x;
                currentFrame = animation.getKeyFrame(stateTime, true);
            }
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
            paddingX = -P(20);
        }

        switch(player.state) {
            case RUN:
                paddingX += getPadding(xRunPosPadding, frameIndex) * bulletDirection;
                paddingY += getPadding(yRunPosPadding, frameIndex);
                break;
            case WALLJUMP:
            case JUMP:
            case FALL:
                paddingY = P(35);
                paddingX += P(2);
                if (bulletDirection == MovingObject.LEFT) {
                    paddingX = -P(22);
                }

                if (player.state == PlayerState.FALL) {
                    paddingY = P(34);
                }
                break;
            case DASH:
                paddingX += P(2) * bulletDirection;
                paddingY = getPadding(yDashPosPadding, frameIndex);
                break;
            case DASHBREAK:
                paddingX += getPadding(xDashBreakPosPadding, frameIndex) * bulletDirection;
                paddingY = getPadding(yDashBreakPosPadding, frameIndex);
                break;
            case WALLSLIDE:
                paddingX -= P(2) * direction;
                paddingY = P(29);
                break;
        }

        paddingX += (direction == LEFT ? bulletPosPaddingXLeft : bulletPosPaddingXRight);
        paddingY += bulletPosPaddingY;

        return VectorCache.get(paddingX, paddingY);
    }

    private static float getPadding(float[] padding, int i) {
        if (i >= padding.length) {
            return padding[padding.length - 1];
        }
        return padding[i];
    }

    private Vector2 getMuzzlePositionPadding(PlayerBase player, int frameIndex) {
        Vector2 padding = getBulletPositionPadding(player, direction, frameIndex);
        float paddingX = padding.x + (direction == LEFT ? muzzlePosPaddingXLeft : muzzlePosPaddingXRight);
        float paddingY = padding.y + muzzlePosPaddingY;
        padding = VectorCache.get(paddingX, paddingY);
        return padding;
    }
}

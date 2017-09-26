package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimationBase;
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

    // RUN padding
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
    private Animation<TextureRegion> muzzleAnimation;
    private boolean explode;
    private float explodePosPaddingY;
    private float noDamagePosPaddingY;
    private float muzzlePosPaddingY;
    private float bulletPosPaddingY;
    private float explodePosPaddingXLeft;
    private float noDamagePosPaddingXLeft;
    private float muzzlePosPaddingXLeft;
    private float bulletPosPaddingXLeft;
    private float explodePosPaddingXRight;
    private float noDamagePosPaddingXRight;
    private float muzzlePosPaddingXRight;
    private float bulletPosPaddingXRight;
    private int playerStartDirection;
    private boolean stopUpdatingMuzzlePos;

    private PlayerBase player;

    private PlayerSound playerSound;

    public XBuster(PlayerBase player, Damage damage, int direction, PlayerAnimationBase animations, PlayerSound playerSound) {
        super(damage, direction);
        this.playerSound = playerSound;
        this.player = player;
        // Calculate position for bullet
        createAnimation(animations);
        pos = new Vector2();
        createBounds(pos);
        vel = new Vector2(0, 0);
        playerStartDirection = player.direction;
        posPadding = getMuzzlePositionPadding(player, player.currentFrameIndex());
        muzzlePos = new Vector2();
        muzzlePos.x = player.mapCollisionBounds.x + posPadding.x;
        muzzlePos.y = player.mapCollisionBounds.y + posPadding.y;
        explode = false;
        stopUpdatingMuzzlePos = false;
        stateTime = 0;
    }

    private void createAnimation(PlayerAnimationBase animations) {
        explodeNoDamageAnimation = animations.get(PlayerAnimationBase.Type.BulletNoDamageExplode, direction);
        switch(damage.getType()) {
            case HEAVY:
                initialiseHealthPoints(100);
                animation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletHeavy, direction, Sprites.X_BULLET_HEAVY, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletHeavyMuzzle, direction, Sprites.X_SHOOT_HEAVY_MUZZLE, null, 0.04f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletHeavyExplode, direction, Sprites.X_BULLET_HEAVY_EXPLODE, null, 0.03f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = P(4);
                explodePosPaddingY = -P(30);
                noDamagePosPaddingXLeft = -P(10);
                noDamagePosPaddingXRight = P(15);
                noDamagePosPaddingY = -P(22);
                muzzlePosPaddingXLeft = P(38);
                muzzlePosPaddingXRight = -P(29);
                muzzlePosPaddingY = -P(25);
                bulletPosPaddingXLeft = -P(39);
                bulletPosPaddingXRight = -P(7);
                bulletPosPaddingY = -P(9);
                break;
            case NORMAL:
                initialiseHealthPoints(10);
                animation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletMedium, direction, Sprites.X_BULLET_MEDIUM, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletMediumMuzzle, direction, Sprites.X_SHOOT_MEDIUM_MUZZLE, null, 0.03f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletSmallExplode, direction, Sprites.X_BULLET_SMALL_EXPLODE, null, 0.03f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = -P(6);
                explodePosPaddingY = -P(25);
                noDamagePosPaddingXRight = -P(6);
                noDamagePosPaddingXLeft = -P(12);
                noDamagePosPaddingY = explodePosPaddingY;
                muzzlePosPaddingXLeft = 0;
                muzzlePosPaddingXRight = 0;
                muzzlePosPaddingY = -P(2);
                bulletPosPaddingXLeft = -P(9);
                bulletPosPaddingXRight = P(5);
                bulletPosPaddingY = -P(5);
                break;
            case LIGHT:
                initialiseHealthPoints(10);
                animation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletSmall, direction, Sprites.X_BULLET_SMALL, null, 0.05f);
                muzzleAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletSmallMuzzle, direction, Sprites.X_SHOOT_MUZZLE, null, 0.025f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.BulletSmallExplode, direction, Sprites.X_BULLET_SMALL_EXPLODE, null, 0.03f);
                explodePosPaddingXLeft = -P(15);
                explodePosPaddingXRight = -P(17);
                explodePosPaddingY = -P(30);
                noDamagePosPaddingXLeft = -P(8);
                noDamagePosPaddingXRight = -P(25);
                noDamagePosPaddingY = explodePosPaddingY;
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

    private void createBounds(Vector2 pos) {
        switch(damage.getType()) {
            case HEAVY:
                mapCollisionBounds.set(pos.x, pos.y, P(55), P(1));
                setDealDamageBoundsSize(P(55), P(30));
                break;
            case NORMAL:
                mapCollisionBounds.set(pos.x, pos.y, P(27), P(1));
                setDealDamageBoundsSize(P(27), P(18));
                break;
            case LIGHT:
                mapCollisionBounds.set(pos.x, pos.y, P(14), P(1));
                setDealDamageBoundsSize(P(14), P(8));
                break;
        }
    }

    @Override
    public boolean canCollideWithWall() {
        return true;
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if (isDead()) {
            if (!explode) {
                stateTime = 0;
                explode = true;
                float explodePosPaddingX, explodePosPaddingY;
                if (targetTookDamage) {
                    explodePosPaddingX = direction == LEFT ? explodePosPaddingXLeft : explodePosPaddingXRight;
                    explodePosPaddingY = this.explodePosPaddingY;
                    playerSound.playBulletHit();
                } else {
                    explodePosPaddingX = direction == LEFT ? noDamagePosPaddingXLeft : noDamagePosPaddingXRight;
                    explodePosPaddingY = noDamagePosPaddingY;
                    playerSound.playAttackNoDamage();
                }
                // Must not update anything other than rendering position here.
                // Calling updatePos here could result in a wrong map collision detection
                pos.x += explodePosPaddingX;
                pos.y += explodePosPaddingY;
            }

            if (targetTookDamage) {
                currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
            } else {
                currentFrame = explodeNoDamageAnimation.getKeyFrame(stateTime, false);
            }
            if (explodeAnimation.isAnimationFinished(stateTime)) {
                shouldBeRemoved = true;
            }
        } else {
            if (stateTime <= muzzleToBulletTime) {
                posPadding = getBulletPositionPadding(player, direction, player.currentFrameIndex());
                updatePos(
                        player.mapCollisionBounds.x + posPadding.x,
                        player.mapCollisionBounds.y + posPadding.y);
            } else {
                updatePos(pos.x + vel.x * delta, pos.y);
                vel.x = 10f * direction;
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
                muzzlePos.x = player.mapCollisionBounds.x + posPadding.x;
                muzzlePos.y = player.mapCollisionBounds.y + posPadding.y;
            }
        } else {
            muzzleFrame = null;
        }
    }

    private Vector2 getBulletPositionPadding(PlayerBase player, int bulletDirection, int frameIndex) {
        float paddingY = P(27);
        float paddingX = player.mapCollisionBounds.width;
        if (bulletDirection == MovingObject.LEFT) {
            paddingX = -P(20);
        }

        switch(player.state) {
            case Run:
                paddingX += getPadding(xRunPosPadding, frameIndex) * bulletDirection;
                paddingY += getPadding(yRunPosPadding, frameIndex);
                break;
            case Walljump:
            case Jump:
            case Fall:
                paddingY = P(35);
                paddingX += P(2);
                if (bulletDirection == MovingObject.LEFT) {
                    paddingX = -P(22);
                }

                if (player.state == PlayerState.Fall) {
                    paddingY = P(34);
                }
                break;
            case Dash:
                paddingX += P(2) * bulletDirection;
                paddingY = getPadding(yDashPosPadding, frameIndex);
                break;
            case Dashbreak:
                paddingX += getPadding(xDashBreakPosPadding, frameIndex) * bulletDirection;
                paddingY = getPadding(yDashBreakPosPadding, frameIndex);
                break;
            case Wallslide:
                paddingX += P(5) * direction;
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

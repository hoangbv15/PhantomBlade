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

    private static float[] xRunPosPadding = new float[] {
            -0.08f, -0.08f, -0.08f, 0.12f, -0.12f, -0.12f, -0.08f, -0.04f, -0.04f, -0.04f, 0f, 0f, 0f, -0.04f,
    };

    private static float[] yRunPosPadding = new float[] {
            P(3), P(4), P(3), P(2), P(1), P(2), P(3), P(4), P(3), P(2), P(1), 0, P(1), P(2)
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
        pos.x = player.bounds.x + posPadding.x;
        pos.y = player.bounds.y + posPadding.y;
        playerStartDirection = player.direction;
        muzzlePos = new Vector2();
        bounds = new Rectangle(pos.x, pos.y, 0.1f, 0.1f);
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
            pos.x += 8 * delta * direction;
            bounds.x = pos.x;
        }
        if (stateTime <= muzzleTime && !explode) {
            muzzleFrame = muzzleAnimation.getKeyFrame(stateTime, false);
            if (player.direction != playerStartDirection) {
                stopUpdatingMuzzlePos = true;
            }
            if (!stopUpdatingMuzzlePos) {
                muzzlePos.x = player.bounds.x;
                muzzlePos.y = player.bounds.y;
                posPadding = getMuzzlePositionPadding(player, player.currentFrameIndex());
                muzzlePos.x += posPadding.x;
                muzzlePos.y += posPadding.y;
            }
        } else {
            muzzleFrame = null;
        }
    }

    private Vector2 getBulletPositionPadding(PlayerBase player, int bulletDirection, int frameIndex) {
        float paddingY = P(27);
        Vector2 padding;
        if (bulletDirection == MovingObject.RIGHT) {
            padding = VectorCache.get(player.bounds.width, paddingY);
        } else {
            padding = VectorCache.get(-P(12), paddingY);
        }

        switch(player.state) {
            case RUN:
                padding = VectorCache.get(padding.x, padding.y + yRunPosPadding[frameIndex]);
                break;
            case WALLJUMP:
            case JUMP:
            case FALL:
                float y = P(34);
                float x = padding.x + P(2);
                if (bulletDirection == MovingObject.LEFT) {
                    x = -P(13);
                }
                if (player.state == PlayerState.FALL) {
                    y = P(33);
                }
                padding = VectorCache.get(x, y);
                break;
            case DASH:
                padding = VectorCache.get(padding.x + P(1), P(12));
                break;
            case WALLSLIDE:
                padding = VectorCache.get(padding.x, P(29));
                break;
        }

        return padding;
    }

    private Vector2 getMuzzlePositionPadding(PlayerBase player, int frameIndex) {
        Vector2 padding = getBulletPositionPadding(player, player.direction, frameIndex);
        float paddingX = padding.x;
        float paddingY = padding.y - P(8);
        switch(player.state) {
            case WALLSLIDE:
                if (player.direction == LEFT) {
                    paddingX = player.bounds.width - P(4);
                } else {
                    paddingX = P(10);
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

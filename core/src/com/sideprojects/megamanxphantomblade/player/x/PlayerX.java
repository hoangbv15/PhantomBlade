package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimationBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.TraceColour;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerX extends PlayerBase {
    private Animation<TextureRegion> outerCircleChargeAnimation;
    private Animation<TextureRegion> innerCircleChargeAnimation;
    private Animation<TextureRegion> innerCircleAlmostChargeAnimation;
    private float chargeStateTime;
    private float almostChargeStateTime;
    private float fullyChargedStateTime;

    private float idleBoundWidth = 0.4f;
    private float idleBoundHeight = 0.6f;

    protected PlayerX(float x, float y, int difficulty) {
        super(x, y, difficulty);
        mapCollisionBounds.width = idleBoundWidth;
        mapCollisionBounds.height = idleBoundHeight;
        auxiliaryFrames = new HashMap<>(2);
        List<Integer> outerCircleChargeAnimationIndex = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> innerCircleChargeAnimationIndex = Arrays.asList(13, 11, 13, 10, 13, 12, 13, 14);
        List<Integer> innerCircleAlmostChargeAnimationIndex = Arrays.asList(10, 14, 11, 14, 12, 10, 13, 11);
        outerCircleChargeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.ChargeOuterCircles, MovingObject.RIGHT, Sprites.X_CHARGE_PARTICLES, outerCircleChargeAnimationIndex, 0.03f);
        innerCircleChargeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.ChargeInnerCircles, MovingObject.RIGHT, Sprites.X_CHARGE_PARTICLES, innerCircleChargeAnimationIndex, 0.05f);
        innerCircleAlmostChargeAnimation = animations.retrieveFromCache(PlayerAnimationBase.Type.ChargeInnerCircles, MovingObject.RIGHT, Sprites.X_CHARGE_PARTICLES, innerCircleAlmostChargeAnimationIndex, 0.05f);
        chargeStateTime = 0;
    }

    @Override
    protected void updateTakeDamageBounds() {
        switch (state) {
            case Dash:
                takeDamageBounds.setWidth(idleBoundHeight);
                takeDamageBounds.setHeight(idleBoundWidth);
                break;
            default:
                takeDamageBounds.setWidth(idleBoundWidth);
                takeDamageBounds.setHeight(idleBoundWidth);
                break;
        }
    }

    @Override
    public void createAnimations() {
        animations = new PlayerXAnimation();
    }

    @Override
    public TraceColour getTraceColour() {
        return new TraceColour(0, 0, 1);
    }

    @Override
    protected void internalUpdate(float delta) {
        TextureRegion outerCircleFrame = null;
        TextureRegion innerCircleFrame = null;
        if (isCharging) {
            outerCircleFrame = outerCircleChargeAnimation.getKeyFrame(chargeStateTime, true);
            chargeStateTime += delta;
            if (almostFullyCharged) {
                innerCircleFrame = innerCircleAlmostChargeAnimation.getKeyFrame(almostChargeStateTime, true);
                almostChargeStateTime += delta;
            }
            if (fullyCharged) {
                innerCircleFrame = innerCircleChargeAnimation.getKeyFrame(fullyChargedStateTime, true);
                fullyChargedStateTime += delta;
            }
        } else {
            chargeStateTime = 0;
            almostChargeStateTime = 0;
            fullyChargedStateTime = 0;
        }
        auxiliaryFrames.put(PlayerAnimationBase.Type.ChargeOuterCircles, outerCircleFrame);
        auxiliaryFrames.put(PlayerAnimationBase.Type.ChargeInnerCircles, innerCircleFrame);
    }

    @Override
    public Vector2 getAuxiliaryAnimationPadding() {
        float x = -outerCircleChargeAnimation.getKeyFrame(0).getRegionWidth() / 3f;
        float y = -outerCircleChargeAnimation.getKeyFrame(0).getRegionHeight() / 4f;
        return VectorCache.get(x, y);
    }

    @Override
    public void updatePos(float x, float y) {
        super.updatePos(x, y);
        pos.x -= 0.05f;
    }
}

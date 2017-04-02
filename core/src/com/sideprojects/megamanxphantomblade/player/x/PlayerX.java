package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.math.VectorCache;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
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

    protected PlayerX(float x, float y) {
        super(x, y);
        bounds.width = 0.4f;
        bounds.height = 0.7f;
        attackChargeFrames = new HashMap<PlayerAnimation.Type, TextureRegion>(2);
        List<Integer> outerCircleChargeAnimationIndex = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> innerCircleChargeAnimationIndex = Arrays.asList(13, 11, 13, 10, 13, 12, 13, 14);
        List<Integer> innerCircleAlmostChargeAnimationIndex = Arrays.asList(10, 14, 11, 14, 12, 10, 13, 11);
        outerCircleChargeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.ChargeOuterCircles, MovingObject.RIGHT, Sprites.XChargeParticles, outerCircleChargeAnimationIndex, 0.03f);
        innerCircleChargeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.ChargeInnerCircles, MovingObject.RIGHT, Sprites.XChargeParticles, innerCircleChargeAnimationIndex, 0.05f);
        innerCircleAlmostChargeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.ChargeInnerCircles, MovingObject.RIGHT, Sprites.XChargeParticles, innerCircleAlmostChargeAnimationIndex, 0.05f);
        chargeStateTime = 0;
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
        attackChargeFrames.put(PlayerAnimation.Type.ChargeOuterCircles, outerCircleFrame);
        attackChargeFrames.put(PlayerAnimation.Type.ChargeInnerCircles, innerCircleFrame);
    }

    @Override
    public Vector2 getChargeAnimationPadding() {
        float x = -outerCircleChargeAnimation.getKeyFrame(0).getRegionWidth() / 3f;
        float y = -outerCircleChargeAnimation.getKeyFrame(0).getRegionHeight() / 4f;
        return VectorCache.get(x, y);
    }

    @Override
    public void updatePos() {
        super.updatePos();
        pos.x -= 0.05f;
    }
}

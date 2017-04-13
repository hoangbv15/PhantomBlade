package com.rahul.libgdx.parallax;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 10/04/17.
 */
public class AnimationParallaxLayer extends TextureRegionParallaxLayer {
    private Animation<TextureRegion> animation;
    private float stateTime;

    public AnimationParallaxLayer(Animation<TextureRegion> animation, Vector2 parallaxScrollRatio) {
        super(animation.getKeyFrame(0), parallaxScrollRatio);
        this.animation = animation;
    }

    public AnimationParallaxLayer(Animation<TextureRegion> animation, float oneDimen, Vector2 parallaxScrollRatio, Utils.WH wh) {
        super(animation.getKeyFrame(0), oneDimen, parallaxScrollRatio, wh);
        this.animation = animation;
    }

    @Override
    public void draw(Batch batch, float x, float y, float delta) {
        super.draw(batch, x, y, delta);
        stateTime += delta;
        texRegion = animation.getKeyFrame(stateTime, true);
    }
}

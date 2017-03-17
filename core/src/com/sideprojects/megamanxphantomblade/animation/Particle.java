package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by buivuhoang on 14/03/17.
 */
public class Particle {
    public enum ParticleType {
        WALLKICK,
        WALLSLIDE,
        DASH
    }
    public Animation<TextureRegion> animation;
    public TextureRegion currentFrame;
    public ParticleType type;
    public Vector2 pos;
    public float stateTime;
    public int direction;

    public Particle(ParticleType type, float x, float y, int direction, Animation<TextureRegion> animation) {
        this.type = type;
        this.pos = new Vector2(x, y);
        this.animation = animation;
        this.direction = direction;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        currentFrame = animation.getKeyFrame(stateTime, false);
    }

    public int currentFrameIndex() {
        return animation.getKeyFrameIndex(stateTime);
    }

    public void setToFrameIndex(int index) {
        stateTime = index * animation.getFrameDuration();
    }
}

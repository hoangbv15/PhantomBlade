package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimationBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buivuhoang on 07/04/17.
 */
public class AnimationCache<T> {
    private Map<T, Animation<TextureRegion>> animationCache;

    public AnimationCache() {
        animationCache = new HashMap<T, Animation<TextureRegion>>();
    }

    public Animation<TextureRegion> retrieveFromCache(T key, boolean flipped, String texture, List<Integer> animationIndex, float frameDuration) {
        if (!animationCache.containsKey(key)) {
            animationCache.put(key,
                    AnimationLoader.load(texture, animationIndex, flipped, frameDuration));
        }

        return animationCache.get(key);
    }
}

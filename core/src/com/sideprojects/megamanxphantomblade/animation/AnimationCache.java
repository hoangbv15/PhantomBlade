package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buivuhoang on 07/04/17.
 */
public class AnimationCache<T> {
    private Map<T, Animation<TextureRegion>> cache;

    public AnimationCache() {
        cache = new HashMap<>();
    }

    public Animation<TextureRegion> retrieveFromCache(T key, boolean flipped, String texture, List<Integer> animationIndex, float frameDuration) {
        if (!cache.containsKey(key)) {
            cache.put(key,
                    AnimationLoader.load(texture, animationIndex, flipped, frameDuration));
        }

        return cache.get(key);
    }
}

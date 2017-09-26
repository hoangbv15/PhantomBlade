package com.sideprojects.megamanxphantomblade.math;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the creation of Vector2 objects.
 * If the vector is already created, this returns that instance instead of always
 * creating new objects.
 *
 * Notice: This is only meant to store frequently used immutable vectors
 *
 * Created by buivuhoang on 30/03/17.
 */
public class VectorCache {

    private static Map<Float, Map<Float, Vector2>> vectorCache = new HashMap<>();

    public static Vector2 get(float x, float y) {
        if (!vectorCache.containsKey(x)) {
            vectorCache.put(x, new HashMap<>());
        }
        Map<Float, Vector2> innerCache = vectorCache.get(x);
        if (!innerCache.containsKey(y)) {
            innerCache.put(y, new Vector2(x, y));
        }
        return innerCache.get(y);
    }
}

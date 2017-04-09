package com.sideprojects.megamanxphantomblade.enemies.types.mettool;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;
import java.util.List;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class MettoolAnimation extends EnemyAnimationBase {
    @Override
    protected String getTextureAtlas(Type type) {
        switch (type) {
            case Die:
                return super.getTextureAtlas(type);
            default:
                return Sprites.Mettool;
        }
    }

    @Override
    protected List<Integer> getAnimationIndex(Type type) {
        switch (type) {
            case Idle:
                return Arrays.asList(0, 1, 2, 3);
            case Jump:
                return Arrays.asList(13, 13, 13, 13, 14, 15, 16, 17, 18, 19);
            case Run:
                return Arrays.asList(25, 26, 27, 28, 29, 30, 31, 32, 33, 34);
            case Attack:
                return Arrays.asList(20, 21, 22, 23, 24);
            case ExplodeFragment:
                return Arrays.asList(8, 9, 10, 11);
            case Die:
                return null;
            default:
                return null;
        }
    }

    @Override
    public boolean isLooping(Type type) {
        switch (type) {
            case Run:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Vector2 getAnimationPaddingX(Type type, int direction) {
        return VectorCache.get(0, 0);
    }
}

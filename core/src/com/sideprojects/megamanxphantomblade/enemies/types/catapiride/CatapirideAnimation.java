package com.sideprojects.megamanxphantomblade.enemies.types.catapiride;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;
import java.util.List;

/**
 * Created by buivuhoang on 30/09/17.
 */
public class CatapirideAnimation extends EnemyAnimationBase {
    @Override
    protected String getTextureAtlas(Type type) {
        switch (type) {
            case DIE:
                return super.getTextureAtlas(type);
            default:
                return Sprites.CATAPIRIDE;
        }
    }

    @Override
    protected List<Integer> getAnimationIndex(Type type) {
        switch (type) {
            case TURN:
                return Arrays.asList(3, 4, 5);
            case PREPARE_SHIELD:
                return Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 17, 16);
            case STOP_SHIELD:
                return Arrays.asList(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6);
            case RUN:
                return Arrays.asList(0, 1, 2);
            case ROLL:
                return Arrays.asList(18, 19, 20, 21, 22, 23, 24);
            case DIE:
                return null;
            default:
                return null;
        }
    }

    @Override
    public boolean isLooping(Type type) {
        switch (type) {
            case ROLL:
            case RUN:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected float getFrameDuration(Type type) {
        switch (type) {
            case RUN:
                return 0.2f;
            default:
                return super.getFrameDuration(type);
        }
    }

    @Override
    public Vector2 getAnimationPaddingX(Type type, int direction) {
        return VectorCache.get(0, 0);
    }
}

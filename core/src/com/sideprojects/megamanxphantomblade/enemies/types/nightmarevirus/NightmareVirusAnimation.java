package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.enemies.EnemyAnimationBase;
import com.sideprojects.megamanxphantomblade.math.VectorCache;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by buivuhoang on 17/09/17.
 */
public class NightmareVirusAnimation extends EnemyAnimationBase {
    @Override
    protected String getTextureAtlas(Type type) {
        switch (type) {
            case DIE:
                return super.getTextureAtlas(type);
            default:
                return Sprites.NIGHTMARE_VIRUS;
        }
    }


    @Override
    protected List<Integer> getAnimationIndex(Type type) {
        switch (type) {
            case IDLE:
                return Arrays.asList(3, 4, 5, 6, 5, 4);
            case RUN:
                return Arrays.asList(0, 1, 2, 1);
            case FINISH_ATTACK:
            case PREPARE_ATTACK:
                return Collections.singletonList(7);
            case ATTACK:
                return Arrays.asList(8, 9, 10, 9, 8);
            case DIE:
                return null;
            default:
                return null;
        }
    }

    @Override
    protected float getFrameDuration(Type type) {
        switch (type) {
            case RUN:
            case IDLE:
                return 0.2f;
            default:
                return super.getFrameDuration(type);
        }
    }

    @Override
    public boolean isLooping(Type type) {
        switch (type) {
            case IDLE:
            case RUN:
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

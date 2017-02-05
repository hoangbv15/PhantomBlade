package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by buivuhoang on 05/02/17.
 */
class AnimationHelper {
    protected static Animation create(String atlasFile, int[] animationIndex, boolean flipped, float frameDuration) {
        TextureAtlas atlas = new TextureAtlas(atlasFile);

        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        if (flipped) {
            for (TextureRegion region: regions) {
                region.flip(true, false);
            }
        }

        Array<TextureAtlas.AtlasRegion> indexedRegions = new Array<TextureAtlas.AtlasRegion>();

        if (animationIndex == null) {
            indexedRegions = regions;
        } else {
            for (int index : animationIndex) {
                indexedRegions.add(regions.get(index));
            }
        }

        return new Animation<TextureRegion>(frameDuration, indexedRegions);
    }
}

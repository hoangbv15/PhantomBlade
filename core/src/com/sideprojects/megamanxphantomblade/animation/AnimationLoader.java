package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.List;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class AnimationLoader {
    private AnimationLoader() {}

    public static Animation<TextureRegion> load(String atlasFile, List<Integer> animationIndex, boolean flipped, float frameDuration) {
        Array<TextureAtlas.AtlasRegion> regions = load(atlasFile, flipped, false);

        Array<TextureAtlas.AtlasRegion> indexedRegions = new Array<>();

        if (animationIndex == null) {
            indexedRegions = regions;
        } else {
            for (int index : animationIndex) {
                indexedRegions.add(regions.get(index));
            }
        }

        return new Animation<>(frameDuration, indexedRegions);
    }

    private static Array<TextureAtlas.AtlasRegion> load(String atlasFile, boolean xFlipped, boolean yFlipped) {
        TextureAtlas atlas = new TextureAtlas(atlasFile);

        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        if (xFlipped || yFlipped) {
            for (TextureRegion region: regions) {
                region.flip(xFlipped, yFlipped);
            }
        }

        return regions;
    }

    /**
     * GUI elements are rendered from the top to bottom so need to flip y
     */
    public static Array<TextureAtlas.AtlasRegion> loadGui(String atlasFile) {
        return load(atlasFile, false, true);
    }
}

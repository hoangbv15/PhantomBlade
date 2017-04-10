package com.sideprojects.megamanxphantomblade.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.rahul.libgdx.parallax.AnimationParallaxLayer;
import com.rahul.libgdx.parallax.ParallaxBackground;
import com.rahul.libgdx.parallax.TextureRegionParallaxLayer;
import com.rahul.libgdx.parallax.Utils;
import com.sideprojects.megamanxphantomblade.animation.AnimationLoader;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class IntroStage extends MapBase {
    public IntroStage(PlayerFactory playerFactory, PlayerPhysicsFactory playerPhysicsFactory, SoundPlayer soundPlayer, int difficulty) {
        super(playerFactory, playerPhysicsFactory, soundPlayer, difficulty);
    }

    @Override
    protected TiledMap getMapResource() {
        return new TmxMapLoader().load("maps/IntroStage.tmx");
    }

    @Override
    public ParallaxBackground getBackground() {
        float worldWidth = Gdx.graphics.getWidth();
        float worldHeight = Utils.calculateOtherDimension(Utils.WH.width, worldWidth, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Animation<TextureRegion> background = AnimationLoader.load("maps/background.txt", null, false, 0.5f);
        AnimationParallaxLayer backgroundLayer = new AnimationParallaxLayer(background, worldHeight, new Vector2(0.7f,0.7f), Utils.WH.width);

        ParallaxBackground parallaxBackground = new ParallaxBackground();
        parallaxBackground.addLayers(backgroundLayer);
        return parallaxBackground;
    }

    private ParallaxBackground getBackgroundBackup() {
        float worldHeight = Gdx.graphics.getHeight();
        float worldWidth = Utils.calculateOtherDimension(Utils.WH.height, worldHeight, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextureAtlas atlas = new TextureAtlas("maps/background.atlas");

        TextureRegion mountainsRegionA = atlas.findRegion("mountains_a");
        TextureRegionParallaxLayer mountainsLayerA = new TextureRegionParallaxLayer(mountainsRegionA, worldWidth, new Vector2(.3f,.3f), Utils.WH.width);

        TextureRegion mountainsRegionB = atlas.findRegion("mountains_b");
        TextureRegionParallaxLayer mountainsLayerB = new TextureRegionParallaxLayer(mountainsRegionB, worldWidth*.7275f, new Vector2(.6f,.6f), Utils.WH.width);
        mountainsLayerB.setPadLeft(.2725f*worldWidth);

        TextureRegion cloudsRegion = atlas.findRegion("clouds");
        TextureRegionParallaxLayer cloudsLayer = new TextureRegionParallaxLayer(cloudsRegion, worldWidth, new Vector2(.6f,.6f), Utils.WH.width);
        cloudsLayer.setPadBottom(worldHeight*.467f);

        TextureRegion buildingsRegionA = atlas.findRegion("buildings_a");
        TextureRegionParallaxLayer buildingsLayerA = new TextureRegionParallaxLayer(buildingsRegionA, worldWidth, new Vector2(.75f,.75f), Utils.WH.width);

        TextureRegion buildingsRegionB = atlas.findRegion("buildings_b");
        TextureRegionParallaxLayer buildingsLayerB = new TextureRegionParallaxLayer(buildingsRegionB, worldWidth*.8575f, new Vector2(1,1), Utils.WH.width);
        buildingsLayerB.setPadLeft(.07125f*worldWidth);
        buildingsLayerB.setPadRight(buildingsLayerB.getPadLeft());

        TextureRegion buildingsRegionC = atlas.findRegion("buildings_c");
        TextureRegionParallaxLayer buildingsLayerC = new TextureRegionParallaxLayer(buildingsRegionC, worldWidth, new Vector2(1.3f,1.3f), Utils.WH.width);

        ParallaxBackground parallaxBackground = new ParallaxBackground();
        parallaxBackground.addLayers(mountainsLayerA,mountainsLayerB,cloudsLayer,buildingsLayerA,buildingsLayerB,buildingsLayerC);
        return parallaxBackground;
    }
}

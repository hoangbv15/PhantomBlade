package com.sideprojects.megamanxphantomblade.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.rahul.libgdx.parallax.AnimationParallaxLayer;
import com.rahul.libgdx.parallax.ParallaxBackground;
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

        Animation<TextureRegion> background = AnimationLoader.load("maps/background.txt", null, false, 0.05f);
        background.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        AnimationParallaxLayer backgroundLayer = new AnimationParallaxLayer(background, worldHeight, new Vector2(0.7f,0.7f), Utils.WH.width);

        ParallaxBackground parallaxBackground = new ParallaxBackground();
        parallaxBackground.addLayers(backgroundLayer);
        return parallaxBackground;
    }
}

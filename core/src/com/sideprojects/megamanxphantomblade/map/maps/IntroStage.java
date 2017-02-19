package com.sideprojects.megamanxphantomblade.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class IntroStage extends MapBase {

    public IntroStage(PlayerFactory playerFactory) {
        super(playerFactory);
    }

    @Override
    protected Pixmap getMapResource() {
        return new Pixmap(Gdx.files.internal("maps/IntroStage.png"));
    }

    @Override
    public TextureRegion getGround() {
        if (ground == null) {
            ground = new TextureRegion(new Texture(Gdx.files.internal("sprites/maps/IntroStage/ground.png")));
        }
        return ground;
    }

    @Override
    public TextureRegion getWall() {
        if (wall == null) {
            wall = new TextureRegion(new Texture(Gdx.files.internal("sprites/maps/IntroStage/wall.png")));
        }
        return wall;
    }
}

package com.sideprojects.megamanxphantomblade.map.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class MapTest extends MapBase {

    public MapTest(PlayerFactory playerFactory) {
        super(playerFactory);
    }

    @Override
    protected Pixmap getMapResource() {
        return new Pixmap(Gdx.files.internal("maps/test2.png"));
    }
}

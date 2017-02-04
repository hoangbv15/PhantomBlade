package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphandomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphandomblade.map.MapRenderer;
import com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphantomblade.player.PlayerX;

public class PhantomBladeGame extends ApplicationAdapter {
	MapBase map;
	PlayerBase player;
	MapRenderer mapRenderer;


	@Override
	public void create () {
        player = new PlayerX(500f, 500f);
		map = new MapBase(player);
		mapRenderer = new MapRenderer(map);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mapRenderer.render();
	}
	
	@Override
	public void dispose () {
		mapRenderer.dispose();
	}
}

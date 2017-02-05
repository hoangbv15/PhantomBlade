package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.map.test.MapTest;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXFactory;

public class PhantomBladeGame extends ApplicationAdapter {
	MapBase map;
	WorldRenderer mapRenderer;

	@Override
	public void create () {
		map = new MapTest(new PlayerXFactory());
		mapRenderer = new WorldRenderer(map);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		map.update(Gdx.graphics.getDeltaTime());
		mapRenderer.render();
	}
	
	@Override
	public void dispose () {
		mapRenderer.dispose();
	}
}

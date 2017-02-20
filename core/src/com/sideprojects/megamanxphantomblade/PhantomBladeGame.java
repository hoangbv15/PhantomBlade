package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.map.maps.IntroStage;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXFactory;
import com.sideprojects.megamanxphantomblade.renderers.DebugRenderer;
import com.sideprojects.megamanxphantomblade.renderers.WorldRenderer;

public class PhantomBladeGame extends ApplicationAdapter {
	MapBase map;
	WorldRenderer mapRenderer;
	DebugRenderer debugRenderer;
	KeyMap keyMap;

	@Override
	public void create () {
		keyMap = new KeyMap();
		map = new IntroStage(new PlayerXFactory(keyMap));
		mapRenderer = new WorldRenderer(map);
		debugRenderer = new DebugRenderer(mapRenderer);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getRawDeltaTime();
		map.update(delta);
		mapRenderer.render();
//		debugRenderer.render(delta, map.collisions);
	}
	
	@Override
	public void dispose () {
		mapRenderer.dispose();
	}
}

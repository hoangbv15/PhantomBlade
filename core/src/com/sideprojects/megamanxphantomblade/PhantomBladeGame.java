package com.sideprojects.megamanxphantomblade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sideprojects.megamanxphantomblade.input.PlayerInputProcessor;
import com.sideprojects.megamanxphantomblade.logging.TraceLogger;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.map.maps.IntroStage;
import com.sideprojects.megamanxphantomblade.physics.player.x.PlayerXPhysicsFactory;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXFactory;
import com.sideprojects.megamanxphantomblade.player.x.PlayerXSound;
import com.sideprojects.megamanxphantomblade.renderers.DebugRenderer;
import com.sideprojects.megamanxphantomblade.renderers.WorldRenderer;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayer;

public class PhantomBladeGame extends ApplicationAdapter {
	MapBase map;
	WorldRenderer mapRenderer;
	DebugRenderer debugRenderer;
	KeyMap keyMap;
	ShapeRenderer shapeRenderer;
	PlayerXSound playerSounds;

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		mapRenderer.resize(width, height);
	}

	@Override
	public void create () {
		keyMap = new KeyMap();
		SoundPlayer sound = new SoundPlayer();
		playerSounds = new PlayerXSound(sound);
		playerSounds.preload();
		map = new IntroStage(new PlayerXFactory(), new PlayerXPhysicsFactory(new PlayerInputProcessor(keyMap), playerSounds), sound, Difficulty.NORMAL);
		mapRenderer = new WorldRenderer(new TraceLogger(), map);
		debugRenderer = new DebugRenderer(mapRenderer);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getRawDeltaTime();
		map.update(delta);
//		renderGradientBackground();
		mapRenderer.render(delta);
//		debugRenderer.render(delta);
	}

	private void renderGradientBackground() {
		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.rect(
				0,
				0,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(),
				Color.SKY,
				Color.SKY,
				Color.BLACK,
				Color.BLACK
		);
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		mapRenderer.dispose();
		debugRenderer.dispose();
		playerSounds.dispose();
		map.dispose();
	}
}

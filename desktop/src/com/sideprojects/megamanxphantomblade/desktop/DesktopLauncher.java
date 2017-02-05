package com.sideprojects.megamanxphantomblade.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sideprojects.megamanxphantomblade.PhantomBladeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new PhantomBladeGame(), config);
	}
}

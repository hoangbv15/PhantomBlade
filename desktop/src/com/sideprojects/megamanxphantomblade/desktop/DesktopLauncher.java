package com.sideprojects.megamanxphantomblade.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sideprojects.megamanxphantomblade.PhantomBladeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 60;
		config.backgroundFPS = 0;
		config.addIcon("Favicon_16x16.png", Files.FileType.Internal);
		config.addIcon("Favicon_32x32.png", Files.FileType.Internal);
		config.addIcon("Favicon_128x128.png", Files.FileType.Internal);
		new LwjglApplication(new PhantomBladeGame(), config);
	}
}

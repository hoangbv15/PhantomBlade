package com.sideprojects.megamanxphantomblade.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.sideprojects.megamanxphantomblade.PhantomBladeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1920, 1080);
		config.setIdleFPS(0);
		config.setWindowIcon("Favicon_16x16.png", "Favicon_32x32.png", "Favicon_128x128.png");
		new Lwjgl3Application(new PhantomBladeGame(), config);
	}
}

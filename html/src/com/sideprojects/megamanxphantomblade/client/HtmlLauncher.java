package com.sideprojects.megamanxphantomblade.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sideprojects.megamanxphantomblade.PhantomBladeGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1920, 1080);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new PhantomBladeGame();
        }
}
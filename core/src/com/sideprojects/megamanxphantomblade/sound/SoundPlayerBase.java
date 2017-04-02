package com.sideprojects.megamanxphantomblade.sound;

import com.badlogic.gdx.audio.Sound;

/**
 * Created by buivuhoang on 02/03/17.
 */
public interface SoundPlayerBase {
    void play(String file);
    void playInParallel(String file);
    void playInParallelAndStopPreviousSound(String file);
    void playOneRandomly(String... files);
    void loopInParallel(String file);
    void stop(String file);
    Sound loadSound(String file);
    void dispose();
}

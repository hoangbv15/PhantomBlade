package com.sideprojects.megamanxphantomblade.sound;

/**
 * Created by buivuhoang on 02/03/17.
 */
public interface SoundPlayerBase {
    void play(String file);
    void playInParallel(String file);
    void playInParallelAndStopPreviousSound(String file);
    void playOneRandomly(String... files);
    void dispose();
}

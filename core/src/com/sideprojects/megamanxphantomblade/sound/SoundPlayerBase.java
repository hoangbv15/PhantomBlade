package com.sideprojects.megamanxphantomblade.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by buivuhoang on 02/03/17.
 */
public interface SoundPlayerBase extends Disposable {
    void play(String file);
    void playInParallel(String file);
    void playInParallelAndStopPreviousSound(String file);
    void playOneRandomly(String... files);
    void loopInParallel(String file);
    void stop(String file);
    Sound loadSound(String file);
    Music loadMusic(String file);
}

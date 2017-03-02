package com.sideprojects.megamanxphantomblade.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.List;

/**
 * Created by buivuhoang on 02/03/17.
 */
public class SoundPlayer implements SoundPlayerBase {
    public float sfxVolume = 1.0f;
    private LRUCache<String, Sound> soundCache;
    private List<Long> playingSounds;

    public SoundPlayer() {
        soundCache = new LRUCache<String, Sound>(10);
    }

    /***
     * Stop other non-parallel sounds and play this sound only
     * @param file name of the sound the play
     */
    public void play(String file) {
        Sound sound = loadSound(file);
        sound.play(sfxVolume);
    }

    /***
     * Play a sound while overlaying on top of other sounds, being played at the same time
     * @param file name of the sound file to play
     */
    public void playInParallel(String file) {
        Sound sound = loadSound(file);
        sound.play(sfxVolume);
    }

    private Sound loadSound(String file) {
        if (!soundCache.containsKey(file)) {
            soundCache.put(file, Gdx.audio.newSound(Gdx.files.internal(file)));
        }
        return soundCache.get(file);
    }

    public void dispose() {
        for (Sound sound: soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}

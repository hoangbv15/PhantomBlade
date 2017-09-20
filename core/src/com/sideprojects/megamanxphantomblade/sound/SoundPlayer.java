package com.sideprojects.megamanxphantomblade.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by buivuhoang on 02/03/17.
 */
public class SoundPlayer implements SoundPlayerBase {
    public float sfxVolume = 1.0f;
    private LRUCache<String, Sound> soundCache;
    private LRUCache<String, Music> musicCache;
    private long nonOverlappingSoundBeingPlayed;

    public SoundPlayer() {
        soundCache = new LRUCache<>(10);
        musicCache = new LRUCache<>(2);
        // Dispose of sounds that are removed from the cache automatically
        soundCache.setEntryRemovedListener((key, value) -> value.dispose());
    }

    /***
     * Stop other non-parallel sounds and play this sound only
     * @param file name of the sound the play
     */
    @Override
    public void play(String file) {
        Sound sound = loadSound(file);
        sound.stop(nonOverlappingSoundBeingPlayed);
        nonOverlappingSoundBeingPlayed = sound.play(sfxVolume);
    }

    /***
     * Play a sound while overlaying on top of other sounds, being played at the same time
     * @param file name of the sound file to play
     */
    @Override
    public void playInParallel(String file) {
        Sound sound = loadSound(file);
        sound.play(sfxVolume);

    }

    @Override
    public void playInParallelAndStopPreviousSound(String file) {
        Sound sound = loadSound(file);
        sound.stop(nonOverlappingSoundBeingPlayed);
        sound.play(sfxVolume);
    }

    @Override
    public void playOneRandomly(String... files) {
        int randomlyChosenIndex = MathUtils.random(0, files.length - 1);
        playInParallel(files[randomlyChosenIndex]);
    }

    @Override
    public void loopInParallel(String file) {
        Sound sound = loadSound(file);
        sound.loop(sfxVolume);
    }

    @Override
    public void stop(String file) {
        Sound sound = loadSound(file);
        sound.stop();
    }

    @Override
    public Sound loadSound(String file) {
        if (!soundCache.containsKey(file)) {
            soundCache.put(file, Gdx.audio.newSound(Gdx.files.internal(file)));
        }
        return soundCache.get(file);
    }

    @Override
    public Music loadMusic(String file) {
        if (!musicCache.containsKey(file)) {
            musicCache.put(file, Gdx.audio.newMusic(Gdx.files.internal(file)));
        }
        return musicCache.get(file);
    }

    @Override
    public void dispose() {
        for (Sound sound: soundCache.values()) {
            sound.dispose();
        }
        for (Music music: musicCache.values()) {
            music.dispose();
        }
        soundCache.clear();
        musicCache.clear();
    }
}

package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;

/**
 * Created by buivuhoang on 08/04/17.
 */
public abstract class EnemySound {
    protected SoundPlayerBase soundPlayer;

    public EnemySound(SoundPlayerBase soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public abstract void playDie(float delta);
}

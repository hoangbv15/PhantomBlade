package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 08/04/17.
 */
public abstract class EnemySound {
    protected SoundPlayerBase soundPlayer;

    public EnemySound(SoundPlayerBase soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public void playDie(float delta) {
        soundPlayer.playInParallel(Sounds.EnvironmentEnemyExplode);
    }

    public abstract void playAttack();
}

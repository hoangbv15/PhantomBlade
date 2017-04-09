package com.sideprojects.megamanxphantomblade.enemies.types.mettool;

import com.sideprojects.megamanxphantomblade.enemies.EnemySound;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class MettoolSound extends EnemySound {
    public MettoolSound(SoundPlayerBase soundPlayer) {
        super(soundPlayer);
    }

    @Override
    public void playDie(float delta) {
        soundPlayer.playInParallel(Sounds.EnvironmentEnemyExplode);
    }
}

package com.sideprojects.megamanxphantomblade.enemies.types.nightmarevirus;

import com.sideprojects.megamanxphantomblade.enemies.EnemySound;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 19/09/17.
 */
public class NightmareVirusSound extends EnemySound {
    public NightmareVirusSound(SoundPlayerBase soundPlayer) {
        super(soundPlayer);
    }

    @Override
    public void playAttack() {
        soundPlayer.playInParallel(Sounds.NightmareVirusShoot);
    }
}

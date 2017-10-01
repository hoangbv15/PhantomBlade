package com.sideprojects.megamanxphantomblade.enemies.types.catapiride;

import com.sideprojects.megamanxphantomblade.enemies.EnemySound;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;

/**
 * Created by buivuhoang on 30/09/17.
 */
public class CatapirideSound extends EnemySound {
    public CatapirideSound(SoundPlayerBase soundPlayer) {
        super(soundPlayer);
    }

    @Override
    public void playAttack() {
        // This enemy has no attack
    }
}

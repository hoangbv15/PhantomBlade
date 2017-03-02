package com.sideprojects.megamanxphantomblade.player;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 02/03/17.
 */
public abstract class PlayerSounds implements PlayerStateChangeHandler {
    private SoundPlayerBase soundPlayer;

    public PlayerSounds(SoundPlayerBase soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void callback(PlayerState previousState, PlayerState nextState) {
        switch (nextState) {
            case TOUCHDOWN:
            case RUN:
            case IDLE:
                if (previousState == PlayerState.FALL || previousState == PlayerState.WALLSLIDE) {
                    soundPlayer.play(Sounds.XLand);
                }
                break;
            case DASH:
                soundPlayer.play(Sounds.XDash);
                break;
            case DASHBREAK:
                soundPlayer.play(Sounds.XDashBreak);
                break;
            case JUMP:
                soundPlayer.play(Sounds.XJump);
                break;
            case WALLSLIDE:
                soundPlayer.play(Sounds.XWallSlide);
                break;
        }
    }

    public void dispose() {
        soundPlayer.dispose();
    }
}

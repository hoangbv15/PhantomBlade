package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerSounds;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 02/03/17.
 */
public class PlayerXSounds extends PlayerSounds {
    public PlayerXSounds(SoundPlayerBase soundPlayer) {
        super(soundPlayer);
    }

    @Override
    public void callback(PlayerState previousState, PlayerState nextState) {
        super.callback(previousState, nextState);
        switch (nextState) {
            case WALLJUMP:
                soundPlayer.playInParallel(Sounds.XJumpShout1);
                break;
            case JUMP:
                playRandomJumpShout();
                break;
        }
    }

    private void playRandomJumpShout() {
        soundPlayer.playOneRandomly(
                Sounds.XJumpShout1,
                Sounds.XJumpShout2,
                Sounds.XJumpShout3);
    }

    @Override
    protected void playDash() {
        soundPlayer.play(Sounds.XDash);
    }

    @Override
    protected void playLand() {
        soundPlayer.playInParallelAndStopPreviousSound(Sounds.XLand);
    }

    @Override
    protected void playDashBreak() {
        soundPlayer.play(Sounds.XDashBreak);
    }

    @Override
    protected void playJump() {
        soundPlayer.playInParallelAndStopPreviousSound(Sounds.XJump);
    }

    @Override
    protected void playWallSlide() {
        soundPlayer.playInParallelAndStopPreviousSound(Sounds.XWallSlide);
    }

    @Override
    protected void playWallJump() {
        soundPlayer.playInParallelAndStopPreviousSound(Sounds.XWallJump);
    }
}

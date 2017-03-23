package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 02/03/17.
 */
public class PlayerXSound extends PlayerSound {
    public PlayerXSound(SoundPlayerBase soundPlayer) {
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
            case DAMAGEDNORMAL:
                playerRandomDamagedShout();
                break;
        }
    }

    private void playRandomJumpShout() {
        soundPlayer.playOneRandomly(
                Sounds.XJumpShout1,
                Sounds.XJumpShout2,
                Sounds.XJumpShout3);
    }

    private void playerRandomDamagedShout() {
        soundPlayer.playOneRandomly(
                Sounds.XDamagedShout1,
                Sounds.XDamagedShout2
        );
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

    @Override
    public void preload() {
        soundPlayer.loadSound(Sounds.XJumpShout1);
        soundPlayer.loadSound(Sounds.XJumpShout2);
        soundPlayer.loadSound(Sounds.XJumpShout3);
        soundPlayer.loadSound(Sounds.XDash);
        soundPlayer.loadSound(Sounds.XLand);
        soundPlayer.loadSound(Sounds.XDashBreak);
        soundPlayer.loadSound(Sounds.XJump);
        soundPlayer.loadSound(Sounds.XWallSlide);
        soundPlayer.loadSound(Sounds.XWallJump);
    }
}

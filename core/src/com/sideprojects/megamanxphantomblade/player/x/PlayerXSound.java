package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 02/03/17.
 */
public class PlayerXSound extends PlayerSound {
    private boolean isPlayingCharge;

    public PlayerXSound(SoundPlayerBase soundPlayer) {
        super(soundPlayer);
        isPlayingCharge = false;
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

    @Override
    public void lowHealthWarning() {
        soundPlayer.playInParallel(Sounds.XLowHealth);
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
    protected void playDead() {
        soundPlayer.playInParallel(Sounds.XDie);
    }

    @Override
    public void playAttackLight() {
        soundPlayer.playInParallel(Sounds.XAttackLight);
    }

    @Override
    public void playAttackMedium() {
        soundPlayer.playInParallel(Sounds.XAttackMedium);
    }

    @Override
    public void playAttackHeavy() {
        soundPlayer.playInParallel(Sounds.XAttackHeavy);
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

    public void startPlayingCharge() {
        if (!isPlayingCharge) {
            soundPlayer.loopInParallel(Sounds.XCharging);
            isPlayingCharge = true;
        }
    }

    public void stopPlayingCharge() {
        soundPlayer.stop(Sounds.XCharging);
        isPlayingCharge = false;
    }
}

package com.sideprojects.megamanxphantomblade.player;

import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerStateChangeHandler;
import com.sideprojects.megamanxphantomblade.sound.SoundPlayerBase;
import com.sideprojects.megamanxphantomblade.sound.Sounds;

/**
 * Created by buivuhoang on 02/03/17.
 */
public abstract class PlayerSound implements PlayerStateChangeHandler {
    protected SoundPlayerBase soundPlayer;

    public PlayerSound(SoundPlayerBase soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void callback(PlayerState previousState, PlayerState nextState) {
        switch (nextState) {
            case TOUCHDOWN:
            case RUN:
            case IDLE:
                if (previousState == PlayerState.FALL || previousState == PlayerState.WALLSLIDE) {
                    playLand();
                }
                break;
            case UPDASH:
            case DASH:
                playDash();
                break;
            case DASHBREAK:
                playDashBreak();
                break;
            case WALLJUMP:
                playWallJump();
                break;
            case JUMP:
                playJump();
                break;
            case WALLSLIDE:
                playWallSlide();
                break;
            case DEAD:
                playDead();
                break;
        }
    }

    public void playBulletHit() {
        soundPlayer.playInParallel(Sounds.BulletHit);
    }

    protected abstract void playDash();
    protected abstract void playLand();
    protected abstract void playDashBreak();
    protected abstract void playJump();
    protected abstract void playWallSlide();
    protected abstract void playWallJump();
    protected abstract void playDead();
    public abstract void playAttackLight();
    public abstract void playAttackMedium();
    public abstract void playAttackHeavy();

    public abstract void preload();

    public void dispose() {
        soundPlayer.dispose();
    }
}

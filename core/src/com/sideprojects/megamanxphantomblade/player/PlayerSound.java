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
            case Touchdown:
            case Run:
            case Idle:
                if (previousState == PlayerState.Fall || previousState == PlayerState.Wallslide) {
                    playLand();
                }
                break;
            case Updash:
            case Dash:
                playDash();
                break;
            case Dashbreak:
                playDashBreak();
                break;
            case Walljump:
                playWallJump();
                break;
            case Jump:
                playJump();
                break;
            case Wallslide:
                playWallSlide();
                break;
            case Dead:
                playDead();
                break;
        }
    }

    public void playBulletHit() {
        soundPlayer.playInParallel(Sounds.BulletHit);
    }
    public void playAttackNoDamage() {
        soundPlayer.playInParallel(Sounds.BulletNoDamage);
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

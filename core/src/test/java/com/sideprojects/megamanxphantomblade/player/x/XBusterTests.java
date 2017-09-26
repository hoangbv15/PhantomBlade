package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.Difficulty;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimationBase;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerSound;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by buivuhoang on 25/09/17.
 */
public class XBusterTests {
    private XBuster attack;
    private PlayerSound soundSpy;
    private Animation noDamageAnimationSpy;
    private Animation damageAnimationSpy;

    @Before
    public void init() {
        PlayerBase mockPlayer = mock(PlayerBase.class);
        mockPlayer.mapCollisionBounds = new Rectangle();
        mockPlayer.state = PlayerState.Idle;

        noDamageAnimationSpy = mock(Animation.class);
        damageAnimationSpy = mock(Animation.class);

        PlayerAnimationBase playerAnimationSpy = spy(PlayerAnimationBase.class);
        doAnswer(invocation -> mock(Animation.class)).when(playerAnimationSpy).retrieveFromCache(any(), anyInt(), any(), any(), anyFloat());
        doAnswer(invocation -> noDamageAnimationSpy).when(playerAnimationSpy).retrieveFromCache(
                eq(PlayerAnimationBase.Type.BulletNoDamageExplode),
                anyInt(), any(), any(), anyFloat());
        soundSpy = mock(PlayerSound.class);
        doAnswer(invocation -> damageAnimationSpy).when(playerAnimationSpy).retrieveFromCache(
                eq(PlayerAnimationBase.Type.BulletHeavyExplode),
                anyInt(), any(), any(), anyFloat());
        soundSpy = mock(PlayerSound.class);

        attack = new XBuster(
                mockPlayer,
                new Damage(Damage.Type.HEAVY, Damage.Side.NONE, Difficulty.NORMAL),
                MovingObject.NONEDIRECTION,
                playerAnimationSpy,
                soundSpy);
    }

    @Test
    public void should_use_no_damage_animation_when_deals_no_damage() {
        attack.die(false);
        attack.update(1);
        verify(soundSpy).playAttackNoDamage();
        verify(noDamageAnimationSpy).getKeyFrame(anyFloat(), anyBoolean());
    }

    @Test
    public void should_use_damage_animation_when_deals_damage() {
        attack.die(true);
        attack.update(1);
        verify(soundSpy).playBulletHit();
        verify(damageAnimationSpy).getKeyFrame(anyFloat(), anyBoolean());
    }

    @Test
    public void should_not_call_updatePos_when_exploding() {
        XBuster spyWrapper = spy(attack);
        spyWrapper.die(false);
        verify(spyWrapper, never()).updatePos(anyFloat(), anyFloat());
    }
}

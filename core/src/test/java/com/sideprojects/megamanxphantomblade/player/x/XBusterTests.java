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
    private PlayerAnimationBase playerAnimationSpy;
    private PlayerSound soundSpy;
    private Animation animationSpy;

    @Before
    public void init() {
        PlayerBase mockPlayer = mock(PlayerBase.class);
        mockPlayer.mapCollisionBounds = new Rectangle();
        mockPlayer.state = PlayerState.Idle;

        animationSpy = spy(mock(Animation.class));

        this.playerAnimationSpy = spy(PlayerAnimationBase.class);
        doAnswer(invocation -> mock(Animation.class)).when(playerAnimationSpy).retrieveFromCache(any(), anyInt(), any(), any(), anyFloat());
        doAnswer(invocation -> animationSpy).when(playerAnimationSpy).retrieveFromCache(
                eq(PlayerAnimationBase.Type.BulletNoDamageExplode),
                anyInt(), any(), any(), anyFloat());
        soundSpy = spy(mock(PlayerSound.class));
        soundSpy = spy(soundSpy);

        attack = new XBuster(
                mockPlayer,
                new Damage(Damage.Type.NORMAL, Damage.Side.NONE, Difficulty.NORMAL),
                MovingObject.NONEDIRECTION,
                playerAnimationSpy,
                soundSpy);
    }

    @Test
    public void should_use_explode_animation_when_deals_damage() {
        attack.die(false);
        attack.update(1);
        verify(soundSpy).playAttackNoDamage();
        verify(animationSpy).getKeyFrame(anyFloat(), anyBoolean());
    }
}

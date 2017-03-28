package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;
import com.sideprojects.megamanxphantomblade.player.PlayerAttack;

/**
 * Created by buivuhoang on 27/03/17.
 */
public class Bullet extends PlayerAttack {
    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> explodeAnimation;
    private boolean explode;
    private Vector2 explodePosPadding;

    public Bullet(Vector2 pos, Damage damage, int direction, PlayerAnimation animations) {
        super(damage, direction);
        createAnimation(animations);
        this.pos = pos;
        bounds = new Rectangle(pos.x, pos.y, 0.1f, 0.1f);
        initialiseHealthPoints(10);
        explode = false;
    }

    private void createAnimation(PlayerAnimation animations) {
        switch(damage.type) {
            case Light:
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmall, direction, Sprites.XBulletSmall, null, 0.05f);
                explodeAnimation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmallExplode, direction, Sprites.XBulletSmallExplode, null, 0.05f);
                explodePosPadding = new Vector2(-0.2f, -0.5f);
                break;
        }
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if (isDead()) {
            if (!explode) {
                stateTime = 0;
                explode = true;
                pos.x += explodePosPadding.x;
                pos.y += explodePosPadding.y;
            }
            currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
            if (explodeAnimation.isAnimationFinished(stateTime)) {
                shouldBeRemoved = true;
            }
        } else {
            currentFrame = animation.getKeyFrame(stateTime, true);
            pos.x += 8 * delta * direction;
            bounds.x = pos.x;
        }
    }
}

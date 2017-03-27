package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public Bullet(Vector2 pos, Damage damage, int direction, PlayerAnimation animations) {
        super(damage, direction);
        createAnimation(animations);
        this.pos = pos;
    }

    private void createAnimation(PlayerAnimation animations) {
        switch(damage.type) {
            case Light:
                animation = animations.retrieveFromCache(PlayerAnimation.Type.BulletSmall, direction, Sprites.XBulletSmall, null, 0.05f);
        }
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
        pos.x += 8 * delta * direction;
    }
}

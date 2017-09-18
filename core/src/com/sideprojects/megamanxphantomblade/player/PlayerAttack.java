package com.sideprojects.megamanxphantomblade.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Attack;
import com.sideprojects.megamanxphantomblade.Damage;

/**
 * Created by buivuhoang on 18/09/17.
 */
public abstract class PlayerAttack extends Attack {
    public TextureRegion currentFrame;
    public TextureRegion muzzleFrame;
    public Vector2 muzzlePos;

    protected Animation<TextureRegion> animation;
    protected Animation<TextureRegion> explodeAnimation;
    protected Animation<TextureRegion> explodeNoDamageAnimation;

    public PlayerAttack(Damage damage, int direction) {
        super(damage, direction);
    }
}

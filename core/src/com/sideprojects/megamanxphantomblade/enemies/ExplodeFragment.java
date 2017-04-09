package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;

/**
 * Created by buivuhoang on 08/04/17.
 */
public class ExplodeFragment extends MovingObject {
    public TextureRegion frame;

    public ExplodeFragment(TextureRegion frame, float x, float y, float velX, float velY) {
        this.frame = frame;
        this.pos = new Vector2(x, y);
        this.vel = new Vector2(velX, velY);
    }

    public void update(float delta) {
        pos.x += vel.x * delta;
        pos.y += vel.y * delta;
    }
}

package com.sideprojects.megamanxphantomblade.player.x;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.player.PlayerState;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.TraceColour;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerX extends PlayerBase {
    protected PlayerX(float x, float y) {
        super(x, y);
        bounds.width = 0.5f;
        bounds.height = 0.7f;
    }

    @Override
    public void createAnimations() {
        animations = new PlayerXAnimation();
    }

    @Override
    public TraceColour getTraceColour() {
        return new TraceColour(0, 0, 1);
    }

    @Override
    protected void internalUpdate(MapBase map) {
        if (justBegunAttacking) {
            int bulletDirection = direction;
            if (state == PlayerState.WALLSLIDE) {
                bulletDirection = direction * -1;
            }
            Damage.Side side = bulletDirection == LEFT ? Damage.Side.Right : Damage.Side.Left;
            Damage damage = new Damage(attackType, side);
            Vector2 pos = new Vector2(bounds.x, bounds.y);
            Vector2 padding = getBulletPositionPadding(bulletDirection);
            pos.x += padding.x;
            pos.y += padding.y;
            switch(attackType) {
                case Light:
                    map.addPlayerAttack(new Bullet(pos, damage, bulletDirection, animations));
                    break;
            }
        }
    }

    private Vector2 getBulletPositionPadding(int bulletDirection) {
        Vector2 padding = new Vector2(0, + 0.46f);
        if (bulletDirection == RIGHT) {
            padding.x += bounds.width;
        } else {
            padding.x -= 0.3f;
        }

        switch(state) {
            case JUMP:
                padding.y = 0.55f;
                break;
            case FALL:
                padding.y = 0.55f;
                break;
            case DASH:
                padding.y = 0.2f;
                break;
        }

        return padding;
    }

    @Override
    public void updatePos() {
        super.updatePos();
        pos.x -= 0.05f;
    }
}

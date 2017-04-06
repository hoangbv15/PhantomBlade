package com.sideprojects.megamanxphantomblade.enemies.types;

import com.badlogic.gdx.math.Rectangle;
import com.sideprojects.megamanxphantomblade.Damage;
import com.sideprojects.megamanxphantomblade.enemies.EnemyBase;
import com.sideprojects.megamanxphantomblade.map.MapBase;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class Mettool extends EnemyBase {
    public Mettool(float x, float y, MapBase map) {
        super(x, y, map);
        bounds = new Rectangle(x, y, 0.25f, 0.25f);
        initialiseHealthPoints(20);
        damage = new Damage(Damage.Type.Normal, Damage.Side.None);
        script = new com.sideprojects.megamanxphantomblade.enemies.scripts.Mettool(this, map.player);
    }

    @Override
    protected void updateAnimation(float delta) {

    }
}

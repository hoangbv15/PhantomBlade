package com.sideprojects.megamanxphantomblade;

/**
 * Created by buivuhoang on 27/03/17.
 */
public abstract class Attack extends MovingObject {
    public int direction;
    public Damage damage;
    public boolean shouldBeRemoved;
    public boolean targetTookDamage;

    public Attack(Damage damage, int direction) {
        this.damage = damage;
        this.direction = direction;
        shouldBeRemoved = false;
    }

    public void die(boolean targetTookDamage) {
        this.targetTookDamage = targetTookDamage;
        super.die();
    }

    public abstract boolean canCollideWithWall();

    public abstract void update(float delta);
}

package com.sideprojects.megamanxphantomblade;

/**
 * Created by buivuhoang on 19/03/17.
 * Damage that enemies do to the player
 */
public class Damage {
    public enum Type {
        INSTANT_DEATH(99999999),
        HEAVY(20),
        NORMAL(10),
        LIGHT(5);

        private int damage;
        Type(int damage) {
            this.damage = damage;
        }

        public int getDamage() {
            return damage;
        }
    }

    public enum Side {
        LEFT, RIGHT, NONE
    }

    public Type type;
    public Side side;
    public int difficulty;

    public Damage(Type type, Side side, int difficulty) {
        this.type = type;
        this.side = side;
        this.difficulty = difficulty;
    }

    public int getDamage() {
        int finalDamage = type.getDamage() + difficulty;
        if (finalDamage < 0) return 1;
        return finalDamage;
    }
}

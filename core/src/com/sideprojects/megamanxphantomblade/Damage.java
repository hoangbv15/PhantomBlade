package com.sideprojects.megamanxphantomblade;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buivuhoang on 19/03/17.
 * Damage that enemies do to the player
 */
public class Damage {
    public enum Type {
        InstantDeath,
        Heavy,
        Normal,
        Light
    }

    public enum Side {
        Left, Right, None
    }

    private static Map<Type, Integer> damage;
    static {
        damage = new HashMap<Type, Integer>() {{
            put(Type.InstantDeath, 99999999);
            put(Type.Heavy, 20);
            put(Type.Normal, 10);
            put(Type.Light, 5);
        }};
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
        int finalDamage = damage.get(type) + difficulty;
        if (finalDamage < 0) return 1;
        return finalDamage;
    }

    public static int getDamage(Type type) {
        return damage.get(type);
    }
}

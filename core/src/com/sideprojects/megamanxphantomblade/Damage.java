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

    public Damage(Type type, Side side) {
        this.type = type;
        this.side = side;
    }

    public int getDamage() {
        return damage.get(type);
    }

    public static int getDamage(Type type) {
        return damage.get(type);
    }
}

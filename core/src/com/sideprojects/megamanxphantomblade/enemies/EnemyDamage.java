package com.sideprojects.megamanxphantomblade.enemies;

import com.sideprojects.megamanxphantomblade.physics.collision.Collision;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buivuhoang on 19/03/17.
 * Damage that enemies do to the player
 */
public class EnemyDamage {
    public enum Type {
        InstantDeath,
        Heavy,
        Normal,
        Light
    }

    public enum Side {
        Left, Right
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

    public EnemyDamage(Type type, Side side) {
        this.type = type;
        this.side = side;
    }

    public int getDamage() {
        return damage.get(type);
    }
}

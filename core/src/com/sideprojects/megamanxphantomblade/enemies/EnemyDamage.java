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
    public Collision collision;

    public EnemyDamage(Type type, Collision collision) {
        this.type = type;
        this.collision = collision;
    }

    public int getDamage() {
        return damage.get(type);
    }
}

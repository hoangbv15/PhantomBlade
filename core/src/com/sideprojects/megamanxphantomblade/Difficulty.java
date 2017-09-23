package com.sideprojects.megamanxphantomblade;

/**
 * Difficulty acts as damage modifier.
 * Player's damage is added with this number, and enemy's damage is that minus this number.
 * Created by buivuhoang on 09/04/17.
 */
public class Difficulty {
    public static final int easy = 10;
    public static final int normal = 0;
    public static final int hard = -10;

    private Difficulty() {}
}

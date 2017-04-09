package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;

/**
 * Created by buivuhoang on 04/04/17.
 */
public abstract class ActionBase {
    public abstract void execute(CollisionList collisions, float delta);
    public abstract boolean finish(CollisionList collisions);
}

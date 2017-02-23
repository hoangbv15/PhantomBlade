package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.MovingObject;

import java.util.List;

/**
 * Created by buivuhoang on 22/02/17.
 */
public interface State {
    void enter(MovingObject object, List<Collision> collisionList);

    // Command pattern

    void left(MovingObject object, float velocity, float delta);

    void right(MovingObject object, float velocity, float delta);

    void jump(MovingObject object, float velocity, float delta);

    void none(MovingObject object);

    void applyGravity(MovingObject object, float gravity, float maxFallspeed, float delta);
}
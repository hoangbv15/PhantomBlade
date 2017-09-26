package com.sideprojects.megamanxphantomblade.mocks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class MockMovingObject extends MovingObject {
    public MockMovingObject(float x, float y, float velX, float velY) {
        pos = new Vector2(x, y);
        mapCollisionBounds = new Rectangle(x, y, 0.3f, 0.7f);
        vel = new Vector2(velX, velY);
    }
}
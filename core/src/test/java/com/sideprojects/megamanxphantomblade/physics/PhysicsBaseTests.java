package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.physics.mocks.MockMap;
import com.sideprojects.megamanxphantomblade.physics.mocks.MockMovingObject;
import com.sideprojects.megamanxphantomblade.physics.mocks.TestablePhysicsBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class PhysicsBaseTests {
    private MapBase map;
    private PhysicsBase physics;

    @Before
    public void init() {
        // Need to initialise map to have a block in (1, 1)
        MockMap map = new MockMap();
        map.addRectTile(1, 0, 1, 1);
        this.map = map;
        physics = new TestablePhysicsBase();
    }

    @Test
    public void should_not_collide_at_leftest_edge() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(1f, 1.1f, 0, -1);
        object.mapCollisionBounds.x -= object.mapCollisionBounds.getWidth();
        object.updatePos();
        object.direction = MovingObject.RIGHT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertFalse(collisions.isColliding());
    }

    @Test
    public void should_not_collide_at_rightest_edge() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(2f, 1.1f, 0, -1);
        object.direction = MovingObject.LEFT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertFalse(collisions.isColliding());
    }

    @Test
    public void should_collide_in_the_middle() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(1.5f, 1.1f, 0, -1);

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
    }
}

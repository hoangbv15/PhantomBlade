package com.sideprojects.megamanxphantomblade.physics;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.mocks.MockMap;
import com.sideprojects.megamanxphantomblade.mocks.MockMovingObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class PhysicsBaseRectangleCollisionTests {
    private MockMap map;
    private PhysicsBase physics;

    @Before
    public void init() {
        // Need to initialise map to have a block in (1, 0)
        MockMap map = new MockMap();
        map.addRectTile(1, 0);
        this.map = map;
        physics = new TestablePhysicsBase();
    }

    @Test
    public void should_not_collide_at_leftest_edge() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(1, 1.1f, 0, -1);
        object.updatePos(
                object.mapCollisionBounds.x - object.mapCollisionBounds.getWidth(),
                object.mapCollisionBounds.y
        );
        object.direction = MovingObject.RIGHT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertFalse(collisions.isColliding());
    }

    @Test
    public void should_not_collide_at_rightest_edge() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(2, 1.1f, 0, -1);
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

    @Test
    public void should_collide_up_while_standing_between_2_tiles() {
        // Add a tile next to the first tile
        map.addRectTile(2, 0);
        // Object is on the edge between 2 tiles and should collide with both tiles
        MovingObject object = new MockMovingObject(1.9f, 1, 0, -0.5f);
        object.direction = MovingObject.RIGHT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
        Assert.assertEquals(2, collisions.toList.size());
    }

    @Test
    public void should_collide_up_while_walking_between_2_tiles() {
        // Add a tile next to the first tile
        map.addRectTile(2, 0);
        // Object is on the first tile, just next to 2nd tile
        MovingObject object = new MockMovingObject(2, 1, 0.5f, -0.5f);
        object.updatePos(
                object.mapCollisionBounds.x - object.mapCollisionBounds.getWidth(),
                object.mapCollisionBounds.y
        );
        object.direction = MovingObject.RIGHT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
        Assert.assertEquals(5, collisions.toList.size());
    }

    @Test
    public void should_collide_with_left_tile() {
        // Create a moving object and place it next to the tile on the left
        MovingObject object = new MockMovingObject(2, 0, -1, 0);
        object.direction = MovingObject.LEFT;
        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
    }

    @Test
    public void should_collide_with_right_tile() {
        // Create a moving object and place it next to the tile on the right
        MovingObject object = new MockMovingObject(0, 0, 1, 0);
        object.direction = MovingObject.RIGHT;
        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
    }
}

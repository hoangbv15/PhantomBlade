package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionDetectionRay;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.mocks.MockMap;
import com.sideprojects.megamanxphantomblade.mocks.MockMovingObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by buivuhoang on 31/05/17.
 */
public class PhysicsBaseSlopeCollisionTests {
    private MockMap map;
    private PhysicsBase physics;

    @Before
    public void init() {
        // Need to initialise map to have a block in (1, 0)
        MockMap map = new MockMap();
        map.addSlopeBottomRight(1, 0);
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
        // Create a moving object and place it next to the tile on the right
        MovingObject object = new MockMovingObject(3, 1.1f, 0, -1);
        object.direction = MovingObject.LEFT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertFalse(collisions.isColliding());
    }

    @Test
    public void should_collide_in_the_middle() {
        // Create a moving object and place it on top of the tile in the middle
        MovingObject object = new MockMovingObject(1.5f, 1.1f, 0, -1);

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
    }

    @Test
    public void should_collide_up_while_walking_on_tile() {
        // Object is on the first tile, just next to 2nd tile
        MovingObject object = new MockMovingObject(2, 0.5f, 0.5f, -0.5f);
        object.updatePos(
                object.mapCollisionBounds.x - object.mapCollisionBounds.getWidth(),
                object.mapCollisionBounds.y
        );
        object.direction = MovingObject.RIGHT;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        Assert.assertTrue(collisions.isColliding());
        Assert.assertEquals(2, collisions.toList.size());
    }

    @Test
    public void should_go_up_while_walking_up_on_tile() {
        // Object is on the first tile, just next to 2nd tile
        Optional<Collision> findCollision = walkOnSlope(2, 0.5f, 0.5f, -0.5f,
                MovingObject.RIGHT, CollisionDetectionRay.Side.Front);

        Assert.assertTrue(findCollision.isPresent());
        Assert.assertEquals(new Vector2(1.7f, 0.75f), findCollision.get().getPostCollidePos());
    }

    @Test
    public void should_go_down_while_walking_down_on_tile() {
        // Object is on the first tile, just next to 2nd tile
        Optional<Collision> findCollision = walkOnSlope(2.5f, 0.75f, -1f, -0.5f,
                MovingObject.LEFT, CollisionDetectionRay.Side.Back);

        Assert.assertTrue(findCollision.isPresent());
        Assert.assertEquals(new Vector2(2.2f, 0.25f), findCollision.get().getPostCollidePos());
    }

    @Test
    public void should_go_up_while_entering_tile_from_bottom() {
        Optional<Collision> findCollision = walkOnSlope(1, 0, 0.5f, -0.5f,
                MovingObject.RIGHT, CollisionDetectionRay.Side.Front);

        Assert.assertTrue(findCollision.isPresent());
        Assert.assertEquals(new Vector2(0.7f, 0.25f), findCollision.get().getPostCollidePos());
    }

    @Test
    public void should_go_down_while_entering_tile_from_top() {
        map.addRectTile(3, 0);
        Optional<Collision> findCollision = walkOnSlope(3.2f, 1, -0.5f, -0.5f,
                MovingObject.LEFT, CollisionDetectionRay.Side.Back);

        Assert.assertTrue(findCollision.isPresent());
        Assert.assertEquals(new Vector2(2.9f, 0.85f), findCollision.get().getPostCollidePos());
    }

    private Optional<Collision> walkOnSlope(float x, float y, float velX, float velY, int direction, CollisionDetectionRay.Side collisionSide) {
        MovingObject object = new MockMovingObject(x, y, velX, velY);
        object.updatePos(
                object.mapCollisionBounds.x - object.mapCollisionBounds.getWidth(),
                object.mapCollisionBounds.y
        );
        object.direction = direction;

        CollisionList collisions = physics.getMapCollision(object, 1, map);

        return collisions.toList.stream().filter(c -> c.ray.side == collisionSide).findFirst();
    }
}
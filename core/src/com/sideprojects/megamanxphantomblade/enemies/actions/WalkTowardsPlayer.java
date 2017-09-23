package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.actions.WalkTillEdge;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class WalkTowardsPlayer extends WalkTillEdge {
    private final PlayerBase player;
    private static final float THRESHOLD = 0.5f;

    public WalkTowardsPlayer(MovingObject object, PlayerBase player, float speed, float time) {
        super(object, MovingObject.NONEDIRECTION, speed, time);
        this.player = player;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        direction = MovingObject.LEFT;
        if (object.mapCollisionBounds.x < player.mapCollisionBounds.x) {
            direction = MovingObject.RIGHT;
        }
        super.execute(collisions, delta);
    }

    @Override
    public boolean finish(CollisionList collisions) {
        boolean finish = Math.abs(object.mapCollisionBounds.x - player.mapCollisionBounds.x) < THRESHOLD;
        return finish || super.finish(collisions);
    }
}

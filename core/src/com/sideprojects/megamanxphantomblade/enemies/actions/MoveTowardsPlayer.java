package com.sideprojects.megamanxphantomblade.enemies.actions;

import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.physics.actions.Move;
import com.sideprojects.megamanxphantomblade.physics.actions.MoveTillEdge;
import com.sideprojects.megamanxphantomblade.physics.collision.CollisionList;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;

/**
 * Created by buivuhoang on 06/04/17.
 */
public class MoveTowardsPlayer extends MoveTillEdge {
    private final PlayerBase player;
    private static float threshold = 0.5f;

    public MoveTowardsPlayer(MovingObject object, PlayerBase player, float speed, float time) {
        super(object, MovingObject.NONEDIRECTION, speed, time);
        this.player = player;
    }

    @Override
    public void execute(CollisionList collisions, float delta) {
        direction = MovingObject.LEFT;
        if (object.bounds.x < player.bounds.x) {
            direction = MovingObject.RIGHT;
        }
        super.execute(collisions, delta);
    }

    @Override
    public boolean finish(CollisionList collisions) {
        boolean finish = Math.abs(object.bounds.x - player.bounds.x) < threshold;
        return finish || super.finish(collisions);
    }
}

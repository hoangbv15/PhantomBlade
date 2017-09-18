package com.sideprojects.megamanxphantomblade.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.Attack;
import com.sideprojects.megamanxphantomblade.Damage;

/**
 * Created by buivuhoang on 17/09/17.
 */
public abstract class EnemyAttack extends Attack {
    protected Animation<TextureRegion> animation;
    protected Animation<TextureRegion> explodeAnimation;
    public TextureRegion currentFrame;

    public EnemyAttack(Rectangle enemy, Rectangle target, float speed, Damage damage, int direction) {
        super(damage, direction);
        mapCollisionBounds.x = enemy.x;
        mapCollisionBounds.y = enemy.y;
        pos = new Vector2(enemy.x, enemy.y);
        calculateVelocity(target.x, target.y, speed);
        //TODO: position is being created at the object creation time. Need a way to delay this so that the script works properly
    }

    private void calculateVelocity(float targetX, float targetY, float speed) {
        float targetVectorX = targetX - mapCollisionBounds.x;
        float targetVectorY = targetY - mapCollisionBounds.y;
        float targetVectorLength = (float)Math.sqrt(targetVectorX * targetVectorX + targetVectorY * targetVectorY);
        float vectorRatio = speed / targetVectorLength;
        vel = new Vector2(targetVectorX * vectorRatio, targetVectorY * vectorRatio);
        direction = targetVectorX > 0 ? 1 : -1;
    }

    @Override
    public void update(float delta) {
        if (animation == null) {
            return;
        }
        stateTime += delta;
        mapCollisionBounds.x += vel.x * delta;
        mapCollisionBounds.y += vel.y * delta;
        updatePos();
        if (isDead()) {
            if (explodeAnimation == null) {
                shouldBeRemoved = true;
                return;
            }
            currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
            if (explodeAnimation.isAnimationFinished(stateTime)) {
                shouldBeRemoved = true;
            }
        } else {
            currentFrame = animation.getKeyFrame(stateTime, isAnimationLooping());
        }
    }

    protected abstract boolean isAnimationLooping();
}

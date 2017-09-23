package com.sideprojects.megamanxphantomblade.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Queue;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.sound.LRUCache;

/**
 * Created by buivuhoang on 14/03/17.
 */
public class Particles {
    protected LRUCache<ParticleKey, Animation<TextureRegion>> animationCache;
    protected Queue<Particle> particleQueue;

    public Particles(int initialSize) {
        particleQueue = new Queue<>(initialSize);
        animationCache = new LRUCache<>(initialSize);
    }

    /**
     * Add particle effect
     * @param type Type of the particle effect
     * @param x x
     * @param y y
     * @param isSingletonParticle Whether multiple instances of the effect can be spanwed in a row
     * @param direction The direction which the particle effect should have
     */
    public void add(Particle.ParticleType type, float x, float y, boolean isSingletonParticle, int direction) {
        ParticleKey key = new ParticleKey(type, direction);
        if (!animationCache.containsKey(key)) {
            loadParticleAnimation(key);
        }
        Animation<TextureRegion> animation = animationCache.get(key);
        if (animation != null) {
            if (particleQueue.size != 0 && isSingletonParticle &&
                    particleQueue.last().type == type) {
                Particle p = particleQueue.last();
                p.pos.x = x;
                p.pos.y = y;

                // TODO: Hack to loop the middle frames of wall sliding animation
                if (type == Particle.ParticleType.WALLSLIDE && p.currentFrameIndex() == 4) {
                    p.setToFrameIndex(1);
                }
            } else {
                Particle p = new Particle(type, x, y, direction, animation);
                particleQueue.addLast(p);
            }
        }
    }

    private void loadParticleAnimation(ParticleKey key) {
        boolean flipped = key.direction == MovingObject.LEFT;
        Animation<TextureRegion> animation = null;
        switch (key.type) {
            case DASH:
                animation = AnimationLoader.load(Sprites.DASH_DUST, null, flipped, 0.05f);
                break;
            case WALLKICK:
                animation = AnimationLoader.load(Sprites.WALL_KICK, null, flipped, 0.05f);
                break;
            case WALLSLIDE:
                animation = AnimationLoader.load(Sprites.WALLSLIDE, null, flipped, 0.05f);
                break;
        }
        if (animation != null) {
            animationCache.put(key, animation);
        }
    }

    public int size() {
        return particleQueue.size;
    }

    public Particle get(int index) {
        return particleQueue.get(index);
    }

    public void update(float deltaTime) {
        for (int i = 0; i < particleQueue.size; i++) {
            Particle p = particleQueue.get(i);
            p.update(deltaTime);
        }
        // Remove the last element if the animation runs out
        // Try to only remove one at a time, removing a bunch of elements is dangerous
        if (particleQueue.size > 0) {
            Particle first = particleQueue.first();
            if (first.stateTime >= first.animation.getAnimationDuration()) {
                particleQueue.removeFirst();
            }
        }
    }

    protected class ParticleKey {
        public Particle.ParticleType type;
        public int direction;

        protected ParticleKey(Particle.ParticleType type, int direction) {
            this.type = type;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ParticleKey that = (ParticleKey) o;

            if (direction != that.direction) return false;
            return type == that.type;
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + direction;
            return result;
        }
    }
}

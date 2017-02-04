package com.sideprojects.megamanxphantomblade.com.sideprojects.megamanxphandomblade.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class MapRenderer {
    private MapBase map;
//    OrthographicCamera cam;
    SpriteBatch batch;

    private ImmediateModeRenderer20 renderer = new ImmediateModeRenderer20(false, true, 0);

    Animation<TextureRegion> playerRunRight;
    Animation<TextureRegion> playerRunLeft;
    Animation<TextureRegion> playerIdleRight;
    Animation<TextureRegion> playerIdleLeft;

    public MapRenderer(MapBase map) {
        this.map = map;
//        this.cam = new OrthographicCamera(24, 16);
//        this.cam.position.set(map.player.pos.x, map.player.pos.y, 0);

        this.batch = new SpriteBatch(5460);

        createAnimations();
    }

    private void createAnimations() {
        Texture playerTexture = new Texture(Gdx.files.internal("x.png"));
        TextureRegion[] playerFrames = new TextureRegion(playerTexture).split(148, 125)[0];

        playerIdleLeft = new Animation<TextureRegion>(0.1f, playerFrames[0]);
        playerIdleRight = new Animation<TextureRegion>(0.1f, playerFrames[0]);
        playerRunRight = new Animation<TextureRegion>(0.1f, playerFrames[0]);
        playerRunLeft = new Animation<TextureRegion>(0.1f, playerFrames[0]);
    }

    public void render() {
        renderPlayer();
    }

    private void renderPlayer() {
        // This animation object is dynamically assigned to the correct animation, based on the state of the game
        Animation<TextureRegion> animation;
        boolean loop = true;

        // For now, assign it to one hardcoded animation for testing
        animation = playerIdleLeft;
        batch.begin();
        batch.draw(animation.getKeyFrame(map.player.stateTime, loop), map.player.pos.x, map.player.pos.y);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }
}

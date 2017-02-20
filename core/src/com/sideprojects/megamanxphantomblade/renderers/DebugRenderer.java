package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.map.Collision;
import com.sideprojects.megamanxphantomblade.renderers.WorldRenderer;

import java.util.List;

/**
 * Created by buivuhoang on 11/02/17.
 */
public class DebugRenderer {
    private WorldRenderer mainRenderer;
    private OrthographicCamera cam;
    private MapBase map;
    ShapeRenderer shapeDebugger;

    public DebugRenderer(WorldRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
        this.map = mainRenderer.map;
        this.cam = mainRenderer.cam;
        shapeDebugger = new ShapeRenderer();
    }

    public void render(float delta, List<Collision> collisions) {
        Gdx.gl20.glLineWidth(2);
        shapeDebugger.setProjectionMatrix(cam.combined);
        shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
        shapeDebugger.setColor(0, 1, 0, 1);
        for (Collision collision: collisions) {
            Vector2 start = null;
            Vector2 end = null;
            if (collision == null) {
                continue;
            }
            switch(collision.side) {
                case UP:
                    start = new Vector2(collision.tile.x, collision.tile.y + collision.tile.height);
                    end = new Vector2(start.x + collision.tile.width, start.y);
                    break;
                case DOWN:
                    start = new Vector2(collision.tile.x, collision.tile.y);
                    end = new Vector2(start.x + collision.tile.width, start.y);
                    break;
                case LEFT:
                    start = new Vector2(collision.tile.x, collision.tile.y);
                    end = new Vector2(start.x, start.y + collision.tile.height);
                    break;
                case RIGHT:
                    start = new Vector2(collision.tile.x + collision.tile.width, collision.tile.y);
                    end = new Vector2(start.x, start.y + collision.tile.height);
                    break;
            }
            start.x *= map.getTileWidth();
            start.y *= map.getTileHeight();
            end.x *= map.getTileWidth();
            end.y *= map.getTileHeight();
            shapeDebugger.line(start, end);
        }

        Vector2 playerStart = new Vector2(map.player.pos.x * map.getTileWidth(),
                map.player.pos.y * map.getTileHeight());
        Vector2 playerEnd = new Vector2((map.player.pos.x + map.player.vel.x * delta) * map.getTileWidth(),
                (map.player.pos.y + map.player.vel.y * delta) * map.getTileHeight());
        shapeDebugger.line(playerStart, playerEnd);

        shapeDebugger.end();
    }

    public void dispose() {
        shapeDebugger.dispose();
    }
}

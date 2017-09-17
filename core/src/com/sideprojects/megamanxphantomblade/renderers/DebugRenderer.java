package com.sideprojects.megamanxphantomblade.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.sideprojects.megamanxphantomblade.map.MapBase;
import com.sideprojects.megamanxphantomblade.physics.TileBase;
import com.sideprojects.megamanxphantomblade.physics.collision.Collision;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;

import java.util.Arrays;
import java.util.List;

/**
 * Created by buivuhoang on 11/02/17.
 */
public class DebugRenderer implements Disposable {
    private MapBase map;
    private WorldRenderer mainRenderer;
    private ShapeRenderer shapeDebugger;

    public DebugRenderer(WorldRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
        this.map = mainRenderer.map;
        shapeDebugger = new ShapeRenderer();
        shapeDebugger.setAutoShapeType(false);
    }

    public void render(float delta) {
        Gdx.gl20.glLineWidth(2);
        shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
        shapeDebugger.setProjectionMatrix(mainRenderer.gameCam.combined);

        shapeDebugger.setColor(1, 1, 1, 1);

        for (int y = 0; y < map.bounds[0].length; y++) {
            for (int x = 0; x < map.bounds.length; x++) {
                TileBase tile = map.bounds[x][y];
                if (tile != null) {
                    float[] vertices = Arrays.copyOf(tile.getVertices(), tile.getVertices().length);
                    for (int i = 0; i < vertices.length; i++) {
                        if (i % 2 == 0) {
                            vertices[i] *= map.getTileWidth();
                        } else {
                            vertices[i] *= map.getTileHeight();
                        }
                    }
                    shapeDebugger.polyline(vertices);
                }
            }
        }

        shapeDebugger.setColor(0, 1, 0, 1);

        List<Collision> collisions = map.playerPhysics.collisions.toList;

        for (Collision collision: collisions) {
            Vector2 start = null;
            Vector2 end = null;
            if (collision == null) {
                continue;
            }
            switch(collision.side) {
                case Up:
                    start = new Vector2(collision.tile.x(), collision.tile.y() + collision.tile.getHeight());
                    end = new Vector2(start.x + collision.tile.getWidth(), start.y);
                    break;
                case Down:
                    if (collision.tile instanceof  SquareTriangleTile) {
                        SquareTriangleTile tile = (SquareTriangleTile)collision.tile;
                        start = new Vector2(tile.xBottomLower, tile.yBottomLower);
                        end = new Vector2(tile.xBottomHigher, tile.yBottomHigher);
                    } else {
                        start = new Vector2(collision.tile.x(), collision.tile.y());
                        end = new Vector2(start.x + collision.tile.getWidth(), start.y);
                    }
                    break;
                case Left:
                    start = new Vector2(collision.tile.x(), collision.tile.y());
                    end = new Vector2(start.x, start.y + collision.tile.getHeight());
                    break;
                case Right:
                    start = new Vector2(collision.tile.x() + collision.tile.getWidth(), collision.tile.y());
                    end = new Vector2(start.x, start.y + collision.tile.getHeight());
                    break;
                case UpRamp:
                    SquareTriangleTile tile = (SquareTriangleTile)collision.tile;
                    start = new Vector2(tile.xTopLower, tile.yTopLower);
                    end = new Vector2(tile.xTopHigher, tile.yTopHigher);
                    break;
            }
            if (start == null) {
                continue;
            }
            start.x *= map.getTileWidth();
            start.y *= map.getTileHeight();
            end.x *= map.getTileWidth();
            end.y *= map.getTileHeight();
            shapeDebugger.line(start, end);
        }

        Vector2 playerStart = new Vector2(map.player.pos.x * map.getTileWidth(),
                map.player.pos.y * map.getTileHeight());
        Vector2 playerEnd = new Vector2((map.player.pos.x + map.player.vel.x * delta * 10) * map.getTileWidth(),
                (map.player.pos.y + map.player.vel.y * delta * 10) * map.getTileHeight());

        shapeDebugger.setColor(1, 0, 0, 1);
        shapeDebugger.line(playerStart, playerEnd);

        shapeDebugger.end();
    }

    @Override
    public void dispose() {
        shapeDebugger.dispose();
    }
}

package com.sideprojects.megamanxphantomblade.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sideprojects.megamanxphantomblade.MovingObject;
import com.sideprojects.megamanxphantomblade.math.GeoMath;
import com.sideprojects.megamanxphantomblade.math.NumberMath;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.PlayerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buivuhoang on 04/02/17.
 */
public abstract class MapBase {
    // Debug property, used for rendering collisions to the screen
    public List<Collision> collisions;

    public static int EMPTY = 0xffffff;
    public static int TILE = 0x000000;
    public static int START = 0xff0000;
    public static int DOOR = 0x00ffff;
    public float GRAVITY = 15f;
    public float MAX_FALLSPEED = -8f;
    public float WALLSLIDE_FALLSPEED = -2f;

    private PlayerFactory playerFactory;
    public PlayerBase player;
    public int[][] tiles;
    public Rectangle[][] bounds;

    public TextureRegion ground;

    public MapBase(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
        ground = new TextureRegion(new Texture(Gdx.files.internal("sprites/ground.png")));
        collisions = new ArrayList<Collision>();
        loadMap();
    }

    protected abstract Pixmap getMapResource();

    private void loadMap() {
        Pixmap pixmap = getMapResource();
        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];
        bounds = new Rectangle[pixmap.getWidth()][pixmap.getHeight()];

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;

                if (match(pix, START)) {
                    // we create the player here
                    player = playerFactory.createPlayer(x, y);
                }
                tiles[x][y] = pix;
                if (match(pix, TILE)) {
                    // collision rectangles
                    bounds[x][y] = new Rectangle(x, y, 1, 1);
                }
            }
        }
    }

    public boolean match (int src, int dst) {
        return src == dst;
    }

    public void update(float deltaTime) {
        player.update(deltaTime, this);
    }

    public List<Collision> mapCollisionCheck(MovingObject object, float deltaTime) {
        int direction = object.direction;
        Vector2 pos = object.pos;
        Vector2 vel = object.vel;
        Rectangle bounds = object.bounds;

        collisions.clear();
        // From inside out, find the first tile that collides with the player
        float stepX = vel.x * deltaTime;
        float stepY = vel.y * deltaTime;
        // Translate the start and end vectors depending on the direction of the movement
        float paddingX = 0;
        float paddingY = 0;
        if (stepX > 0) {
            paddingX = bounds.width;
        }
        if (stepY > 0) {
            paddingY = bounds.height;
        }
        Vector2 endPosX = new Vector2(pos.x + stepX, pos.y);
        Vector2 endPosY = new Vector2(pos.x, pos.y + stepY);
        Vector2 endPos = new Vector2(pos.x + stepX, pos.y + stepY);

        // Setup collision detection rays
        List<CollisionDetectionRay> detectionRayList = new ArrayList<CollisionDetectionRay>(5);
        detectionRayList.add(new CollisionDetectionRay(pos, endPosY, 0, paddingY));
        detectionRayList.add(new CollisionDetectionRay(pos, endPosY, bounds.width, paddingY));
        if (vel.x != 0) {
            detectionRayList.add(new CollisionDetectionRay(pos, endPosX, paddingX, 0));
            detectionRayList.add(new CollisionDetectionRay(pos, endPosX, paddingX, bounds.height));
        }
        if (vel.x != 0 && vel.y != 0) {
            detectionRayList.add(new CollisionDetectionRay(pos, endPos, paddingX, paddingY));
        }

        // Loop through map and use collision detection rays to detect...well..collisions.
        int xStart = (int)pos.x;
        int yStart = (int)pos.y;
        if (vel.y < 0) {
            yStart += 1;
        }
        if (direction == MovingObject.LEFT) {
            xStart += 1;
        }
        int xEnd = (int)(endPosX.x + paddingX);
        if (direction == MovingObject.RIGHT) {
            xEnd += 1;
        }
        int yEnd = (int)(endPosY.y + paddingY);

        // Loop through the rectangular area that the speed vector occupies
        // Get a list of all collisions with map tiles in the area
        // Identify the collision nearest to the player
        // Player has at most 2 collisions with the map at the same time
        List<Collision> collisionList = new ArrayList<Collision>(2);
        for (int y = yStart; NumberMath.hasNotExceeded(y, yStart, yEnd); y = NumberMath.iteratorNext(y, yStart, yEnd)) {
            for (int x = xStart; NumberMath.hasNotExceeded(x, xStart, xEnd); x = NumberMath.iteratorNext(x, xStart, xEnd)) {
                Rectangle tile = getCollidableBox(x, y);
                if (tile == null) {
                    continue;
                }
                // Get the tiles surrounding this one
                Rectangle tileUp = getCollidableBox(x, y + 1);
                Rectangle tileDown = getCollidableBox(x, y - 1);
                Rectangle tileLeft = getCollidableBox(x - 1, y);
                Rectangle tileRight = getCollidableBox(x + 1, y);

                for (CollisionDetectionRay ray: detectionRayList) {
                    Collision collision = getSideOfCollisionWithTile(ray, tile,
                            tileUp, tileDown, tileLeft, tileRight);
                    if (collision != null) {
                        collisionList.add(collision);
                        collisions.add(collision);
                    }
                }
            }
        }

        return collisionList;
    }

    private Rectangle getCollidableBox(int x, int y) {
        if (x < 0 || y < 0 || x >= bounds.length || y >= bounds[0].length) {
            return new Rectangle(x, y, 1, 1);
        }
        return bounds[x][y];
    }

    private Collision getSideOfCollisionWithTile(CollisionDetectionRay ray, Rectangle tile,
                                                 Rectangle tileUp,
                                                 Rectangle tileDown,
                                                 Rectangle tileLeft,
                                                 Rectangle tileRight) {
        Vector2 start = ray.getStart();
        Vector2 end = ray.getEnd();

        // Put non-null ones in an array, then sort by distance to start
        // A line can only have at most 2 intersections with a rectangle
        List<Collision> collisionList = new ArrayList<Collision>(2);

        // Find intersection on each side of the tile
        if (tileLeft == null) {
            Collision left = new Collision(GeoMath.findIntersectionLeft(tile, start, end), Collision.Side.LEFT, ray, tile);
            if (left.point != null) collisionList.add(left);
        }
        if (tileRight == null) {
            Collision right = new Collision(GeoMath.findIntersectionRight(tile, start, end), Collision.Side.RIGHT, ray, tile);
            if (right.point != null) collisionList.add(right);
        }
        if (tileUp == null) {
            Collision up = new Collision(GeoMath.findIntersectionUp(tile, start, end), Collision.Side.UP, ray, tile);
            if (up.point != null) collisionList.add(up);
        }
        if (tileDown == null) {
            Collision down = new Collision(GeoMath.findIntersectionDown(tile, start, end), Collision.Side.DOWN, ray, tile);
            if (down.point != null) collisionList.add(down);
        }

        if (collisionList.isEmpty()) {
            return null;
        }

        return Collision.getCollisionNearestToStart(collisionList, start);
    }
}
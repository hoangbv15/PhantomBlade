package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.maps.MapProperties;
import com.sideprojects.megamanxphantomblade.physics.tiles.MovingRectangleTile;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;

/**
 * Created by buivuhoang on 28/01/18.
 */
public class TileFactory extends TileFactoryBase {
    private static final String TotalNumOfTiles = "Total";
    private static final String TileIndex = "Index";
    private static final String Orientation = "Orientation";
    private static final String BottomLeft = "BottomLeft";
    private static final String BottomRight = "BottomRight";
    private static final String TopLeft = "TopLeft";
    private static final String TopRight = "TopRight";
    private static final String Type = "Type";

    private static final String Rectangle = "Rectangle";
    private static final String SquareTriangle = "SquareTriangle";
    private static final String MovingRectangle = "MovingRectangle";

    private static final String DefaultTileType = Rectangle;

    private static final String HalfTileSize = "Half";
    private static final String TileSize = "Size";

    @Override
    public TileBase getTile(MapProperties cellProperties, int x, int y) {
        String type = cellProperties.get(Type, String.class);
        type = type == null ? DefaultTileType : type;
        switch (type) {
            case SquareTriangle:
                String orientation = cellProperties.get(Orientation, String.class);
                int totalTiles = cellProperties.get(TotalNumOfTiles, Integer.class);
                int tileIndex = cellProperties.get(TileIndex, Integer.class);
                float startY = y + tileIndex / (float)totalTiles;
                float endY = y + (tileIndex + 1) / (float)totalTiles;
                if (BottomLeft.equals(orientation)) {
                    return new SquareTriangleTile(x, startY, x, startY, x, endY, x + 1, startY, tileIndex, totalTiles);
                } else if (BottomRight.equals(orientation)) {
                    return new SquareTriangleTile(x, startY, x + 1, startY, x + 1, endY, x, startY, tileIndex, totalTiles);
                } else if (TopRight.equals(orientation)) {
                    return new SquareTriangleTile(x, startY, x, endY, x, startY, x + 1, endY, tileIndex, totalTiles);
                } else if (TopLeft.equals(orientation)) {
                    return new SquareTriangleTile(x, startY, x + 1, endY, x + 1, startY, x, endY, tileIndex, totalTiles);
                }
            case Rectangle:
                if (cellProperties.containsKey(TileSize) && HalfTileSize.equals(cellProperties.get(TileSize, String.class))) {
                    return new RectangleTile(x, y, 1, 45/62f);
                } else {
                    return new RectangleTile(x, y, 1, 1);
                }
            case MovingRectangle:
                return new MovingRectangleTile(x, y, 1, 45/62f);
        }
        return null;
    }
}

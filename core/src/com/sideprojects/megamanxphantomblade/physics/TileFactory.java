package com.sideprojects.megamanxphantomblade.physics;

import com.badlogic.gdx.maps.MapProperties;
import com.sideprojects.megamanxphantomblade.physics.tiles.RectangleTile;
import com.sideprojects.megamanxphantomblade.physics.tiles.SquareTriangleTile;

/**
 * Created by buivuhoang on 28/01/18.
 */
public class TileFactory extends TileFactoryBase {
    private static String TotalNumOfTiles = "Total";
    private static String TileIndex = "Index";
    private static String Orientation = "Orientation";
    private static String BottomLeft = "BottomLeft";
    private static String BottomRight = "BottomRight";
    private static String TopLeft = "TopLeft";
    private static String TopRight = "TopRight";
    private static String TileType = "Type";
    private static String SquareTriangle = "SquareTriangle";

    private static String HalfTileSize = "Half";
    private static String TileSize = "Size";

    @Override
    public TileBase getTile(MapProperties cellProperties, int x, int y) {
        if (cellProperties.containsKey(TileSize) && HalfTileSize.equals(cellProperties.get(TileSize, String.class))) {
            return new RectangleTile(x, y, 1, 45/62f);
        } else if (cellProperties.containsKey(TileType) && SquareTriangle.equals(cellProperties.get(TileType, String.class))) {
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
        } else {
            return new RectangleTile(x, y, 1, 1);
        }
        return null;
    }
}

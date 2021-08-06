package com.bernabito.my2dgame.entities.units.structures;

import com.bernabito.my2dgame.entities.units.Unit;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.level.tiles.Tile;
import com.bernabito.my2dgame.level.tiles.TileBuilder;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public class WoodPlanks extends Structure {

    private static final SpriteSheet TILE_SHEET = TileBuilder.TILE_SHEET;
    private static final int WOOD_PLANK_HIT_POINTS = 200;

    private final Tile woodPlank1Tile;
    private final Tile woodPlank2Tile;

    public WoodPlanks(float x, float y) {
        super(new Rectangle2D.Float(x, y, TILE_SHEET.getTileSize(), TILE_SHEET.getTileSize() * 2), true, WOOD_PLANK_HIT_POINTS);
        woodPlank1Tile = new Tile(TILE_SHEET, 0, 14, x, y);
        woodPlank2Tile = new Tile(TILE_SHEET, 1, 14, x, y + TILE_SHEET.getTileSize());
        setupStaticHpBar(x, y - Unit.DEFAULT_HP_BAR_HEIGHT - 5, TILE_SHEET.getTileSize());
    }

    @Override
    public void renderEntity(Graphics2D g) {
        woodPlank1Tile.render(g);
        woodPlank2Tile.render(g);
    }

}

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

public class Rock extends Structure {

    private static final SpriteSheet TILE_SHEET = TileBuilder.TILE_SHEET;
    private static final int ROCK_HIT_POINTS = 100;
    private final Tile rockTile;

    public Rock(float x, float y) {
        super(new Rectangle2D.Float(x, y, TILE_SHEET.getTileSize(), TILE_SHEET.getTileSize()), true, ROCK_HIT_POINTS);
        rockTile = new Tile(TILE_SHEET, 1, 11, x, y);
        setupStaticHpBar(x, y - Unit.DEFAULT_HP_BAR_HEIGHT - 5, TILE_SHEET.getTileSize());
    }

    @Override
    public void renderEntity(Graphics2D g) {
        rockTile.render(g);
    }

}

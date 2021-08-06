package com.bernabito.my2dgame.entities.units.structures;

import com.bernabito.my2dgame.entities.units.Unit;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.level.tiles.Tile;
import com.bernabito.my2dgame.level.tiles.TileBuilder;
import com.bernabito.my2dgame.utils.Animation;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */


public class Cauldron extends Structure {

    private static final SpriteSheet TILE_SHEET = TileBuilder.TILE_SHEET;
    private static final int CAULDRON_HIT_POINTS = 50;
    private final Tile cauldronTile;

    public Cauldron(float x, float y) {
        super(new Rectangle2D.Float(x, y, TILE_SHEET.getTileSize(), TILE_SHEET.getTileSize()), true, CAULDRON_HIT_POINTS);
        cauldronTile = new Tile(TILE_SHEET, new Animation(5, 1, 3, TileBuilder.ANIMATED_TILE_UDPATE_RATE), x, y);
        setupStaticHpBar(x, y - Unit.DEFAULT_HP_BAR_HEIGHT - 5, TILE_SHEET.getTileSize());
    }

    @Override
    public void updateState() {
        super.updateState();
        cauldronTile.updateState();
    }

    @Override
    public void renderEntity(Graphics2D g) {
        cauldronTile.render(g);
    }

}

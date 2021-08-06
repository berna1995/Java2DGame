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

public class Tree extends Structure {

    public enum TreeType {PINE, OAK}

    private static final SpriteSheet TILE_SHEET = TileBuilder.TILE_SHEET;
    private static final int TREE_HIT_POINTS = 200;
    private final Tile[] treeTiles;

    public Tree(float x, float y, TreeType treeType) {
        super(new Rectangle2D.Float(x + 23, y + 32, 20, 30), true, TREE_HIT_POINTS);
        setupStaticHpBar(x, y - Unit.DEFAULT_HP_BAR_HEIGHT - 5, TILE_SHEET.getTileSize() * 2);
        if (treeType == TreeType.PINE) {
            treeTiles = new Tile[]{
                    new Tile(TILE_SHEET, 6, 0, x, y),
                    new Tile(TILE_SHEET, 6, 1, x + TILE_SHEET.getTileSize(), y),
                    new Tile(TILE_SHEET, 7, 0, x, y + TILE_SHEET.getTileSize()),
                    new Tile(TILE_SHEET, 7, 1, x + TILE_SHEET.getTileSize(), y + TILE_SHEET.getTileSize())
            };
        } else {
            treeTiles = new Tile[]{
                    new Tile(TILE_SHEET, 8, 0, x, y),
                    new Tile(TILE_SHEET, 8, 1, x + TILE_SHEET.getTileSize(), y),
                    new Tile(TILE_SHEET, 9, 0, x, y + TILE_SHEET.getTileSize()),
                    new Tile(TILE_SHEET, 9, 1, x + TILE_SHEET.getTileSize(), y + TILE_SHEET.getTileSize())
            };
        }
    }

    @Override
    public void renderEntity(Graphics2D g) {
        for(Tile treeTile : treeTiles)
            treeTile.render(g);
    }

}

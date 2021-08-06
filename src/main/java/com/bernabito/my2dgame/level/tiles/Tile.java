package com.bernabito.my2dgame.level.tiles;

import com.bernabito.my2dgame.engine.GameEntity;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public class Tile implements GameEntity {

    private final SpriteSheet tileSheet;
    private int tileRow;
    private int tileColumn;
    private Animation animation;

    protected final Rectangle2D.Float drawingRectangle;

    public Tile(SpriteSheet tileSheet, int tileRow, int tileColumn, float x, float y) {
        this.tileSheet = tileSheet;
        this.tileRow = tileRow;
        this.tileColumn = tileColumn;
        drawingRectangle = new Rectangle2D.Float(x, y, tileSheet.getTileSize(), tileSheet.getTileSize());
        animation = null;
    }

    public Tile(SpriteSheet tileSheet, Animation animation, float x, float y) {
        this(tileSheet, animation.getAnimationRow(), animation.getCurrentAnimationIndex(), x, y);
        this.animation = animation;
    }


    @Override
    public void render(Graphics2D g) {
        tileSheet.render(g, tileRow, tileColumn, drawingRectangle.x, drawingRectangle.y);
    }

    @Override
    public void updateState() {
        if (animation != null) {
            animation.play();
            tileRow = animation.getAnimationRow();
            tileColumn = animation.getCurrentAnimationIndex();
        }
    }

    public Rectangle2D getDrawingRectangle() {
        return drawingRectangle;
    }

}

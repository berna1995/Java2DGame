package com.bernabito.my2dgame.level.tiles;

import com.bernabito.my2dgame.engine.Collidable;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;

import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public class CollidableTile extends Tile implements Collidable {

    public CollidableTile(SpriteSheet tileSheet, int tileRow, int tileColumn, float x, float y) {
        super(tileSheet, tileRow, tileColumn, x, y);
    }

    public CollidableTile(SpriteSheet tileSheet, Animation animation, float x, float y) {
        super(tileSheet, animation, x, y);
    }

    @Override
    public Rectangle2D getHitbox() {
        return drawingRectangle;
    }

}

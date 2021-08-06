package com.bernabito.my2dgame.entities;

import com.bernabito.my2dgame.engine.Collidable;
import com.bernabito.my2dgame.engine.GameEntity;
import com.bernabito.my2dgame.level.Level;

import java.awt.geom.Point2D;

/**
 * @author Matteo Bernabito
 */

public abstract class GenericEntity implements GameEntity, Collidable {

    protected Point2D.Float location;
    protected Level level;

    public GenericEntity(Point2D.Float location) {
        this.location = location;
    }

    public void initialize(Level level) {
        this.level = level;
    }

    public void setLocationX(float x) {
        location.x = x;
    }

    public void setLocationY(float y) {
        location.y = y;
    }

    public void setLocation(float x, float y) {
        location.x = x;
        location.y = y;
    }

}

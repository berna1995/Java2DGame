package com.bernabito.my2dgame.engine;

import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public interface Collidable {

    Rectangle2D getHitbox();

    default boolean hasCollided(Collidable collider) {
        return getHitbox().intersects(collider.getHitbox());
    }

}

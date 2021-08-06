package com.bernabito.my2dgame.engine;

import com.bernabito.my2dgame.entities.GenericEntity;

import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public final class CollisionSolver {

    private CollisionSolver() {
    }

    public static void resolve(GenericEntity collider, Collidable collided) {
        Rectangle2D playerHitbox = collider.getHitbox();
        Rectangle2D collidedHitbox = collided.getHitbox();
        Rectangle2D intersection = playerHitbox.createIntersection(collidedHitbox);
        double dx = intersection.getCenterX() - collidedHitbox.getCenterX();
        double dy = intersection.getCenterY() - collidedHitbox.getCenterY();
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        double equalityThreeshold = 0.0001;
        // Approccio da un angolo
        if (Math.abs(absDx - absDy) <= equalityThreeshold) {
            if (dx < 0)
                collider.setLocationX((float) (collidedHitbox.getX() - playerHitbox.getWidth()));
            else
                collider.setLocationX((float) (collidedHitbox.getX() + collidedHitbox.getWidth()));
            if (dy < 0)
                collider.setLocationY((float) (collidedHitbox.getY() - playerHitbox.getHeight()));
            else
                collider.setLocationY((float) (collidedHitbox.getY() + collidedHitbox.getHeight()));
        }
        // Approccio da sinistra o destra
        else if (absDx > absDy) {
            if (dx < 0)
                collider.setLocationX((float) (collidedHitbox.getX() - playerHitbox.getWidth()));
            else
                collider.setLocationX((float) (collidedHitbox.getX() + collidedHitbox.getWidth()));
        } // Aprroccio da sopra o sotto
        else {
            if (dy < 0)
                collider.setLocationY((float) (collidedHitbox.getY() - playerHitbox.getHeight()));
            else
                collider.setLocationY((float) (collidedHitbox.getY() + collidedHitbox.getHeight()));
        }
    }

}

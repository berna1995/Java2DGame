package com.bernabito.my2dgame.entities.units;

import com.bernabito.my2dgame.engine.CollisionSolver;
import com.bernabito.my2dgame.entities.GenericEntity;
import com.bernabito.my2dgame.entities.units.structures.Structure;
import com.bernabito.my2dgame.level.tiles.CollidableTile;
import com.bernabito.my2dgame.utils.Vector2f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Bernabito
 */

public abstract class Unit extends GenericEntity {

    public final static float DEFAULT_HP_BAR_HEIGHT = 10.0f;

    private List<CollidableTile> collisionTiles;

    protected Rectangle2D.Float hitbox;
    protected Vector2f speedVector;
    protected int maxHitPoints;
    protected int currentHitPoints;
    protected boolean canMove;

    public Unit(Rectangle2D.Float hitbox, boolean canMove, int maxHitPoints) {
        super(new Point2D.Float(hitbox.x, hitbox.y));
        this.hitbox = hitbox;
        this.canMove = canMove;
        this.maxHitPoints = maxHitPoints;
        currentHitPoints = maxHitPoints;
        speedVector = new Vector2f();
        collisionTiles = new ArrayList<>();
    }

    public float distanceFromMidPoint(Unit otherUnit) {
        Rectangle2D otherHitbox = otherUnit.getHitbox();
        Rectangle2D thisHitbox = getHitbox();
        double deltaX = otherHitbox.getCenterX() - thisHitbox.getCenterX();
        double deltaY = otherHitbox.getCenterY() - thisHitbox.getCenterY();
        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    @Override
    public void setLocationX(float x) {
        if (canMove) {
            super.setLocationX(x);
            hitbox.x = x;
        }
    }

    @Override
    public void setLocationY(float y) {
        if (canMove) {
            super.setLocationY(y);
            hitbox.y = y;
        }
    }

    @Override
    public void setLocation(float x, float y) {
        setLocationX(x);
        setLocationY(y);
    }

    public boolean isDead() {
        return currentHitPoints <= 0;
    }

    @Override
    public void updateState() {
        if (!isDead() && canMove) {
            float vx = speedVector.getX();
            float vy = speedVector.getY();
            location.x += vx;
            location.y += vy;
            hitbox.x += vx;
            hitbox.y += vy;
            collisionTiles.clear();
            for(CollidableTile c : level.getCollidableTiles(hitbox, collisionTiles))
                if (hasCollided(c))
                    CollisionSolver.resolve(this, c);
            for(Structure s : level.getStructures())
                if (hasCollided(s))
                    CollisionSolver.resolve(this, s);
        }
    }

    @Override
    public Rectangle2D getHitbox() {
        return hitbox;
    }

    public abstract void applyDamage(int dmg);

    protected abstract void renderEntity(Graphics2D g);

    @Override
    public final void render(Graphics2D g) {
        renderEntity(g);
        renderHitPointsBar(g);
    }

    protected abstract Rectangle2D.Float getHPBarRectangle();

    private void renderHitPointsBar(Graphics2D g) {
        Rectangle2D.Float hpBarRectangle = getHPBarRectangle();
        if (hpBarRectangle != null) {
            float hpRatio = currentHitPoints / (float) maxHitPoints;
            Rectangle2D.Float redPartRectangle = new Rectangle2D.Float(
                    hpBarRectangle.x,
                    hpBarRectangle.y,
                    hpBarRectangle.width * hpRatio,
                    hpBarRectangle.height);
            g.setColor(Color.RED);
            g.fill(redPartRectangle);
            g.setColor(Color.BLACK);
            g.draw(hpBarRectangle);
        }
    }
}

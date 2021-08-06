package com.bernabito.my2dgame.entities.units.structures;

import com.bernabito.my2dgame.entities.units.Unit;

import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */


public abstract class Structure extends Unit {

    private boolean destructible;
    private Rectangle2D.Float staticHpBar;

    public Structure(Rectangle2D.Float hitbox, boolean destructible, int maxHitPoints) {
        super(hitbox, false, (destructible) ? maxHitPoints : Integer.MAX_VALUE);
        this.destructible = destructible;
        staticHpBar = null;
    }

    @Override
    public void applyDamage(int dmg) {
        if (destructible) {
            currentHitPoints -= dmg;
            if (isDead())
                level.getStructures().remove(this);
        }
    }

    public boolean isDestructible() {
        return destructible;
    }

    protected final void setupStaticHpBar(float x, float y, float width) {
        staticHpBar = new Rectangle2D.Float(x, y, width, Unit.DEFAULT_HP_BAR_HEIGHT);
    }

    @Override
    protected final Rectangle2D.Float getHPBarRectangle() {
        // Barre visibili solo se strutture sono danneggiate
        if (currentHitPoints < maxHitPoints)
            return staticHpBar;
        else
            return null;
    }
}

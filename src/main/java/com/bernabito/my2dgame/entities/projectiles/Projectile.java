package com.bernabito.my2dgame.entities.projectiles;

import com.bernabito.my2dgame.entities.GenericEntity;
import com.bernabito.my2dgame.entities.units.MobileUnit;
import com.bernabito.my2dgame.entities.units.structures.Structure;
import com.bernabito.my2dgame.utils.Vector2f;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public abstract class Projectile extends GenericEntity {

    private final GenericEntity source;
    protected Vector2f speedVector;
    protected int dmg;

    public Projectile(GenericEntity source, float x, float y, Vector2f speedVector, int dmg) {
        super(new Point2D.Float(x, y));
        this.source = Objects.requireNonNull(source);
        this.speedVector = Objects.requireNonNull(speedVector);
        this.dmg = dmg;
    }

    @Override
    public void updateState() {
        location.x += speedVector.getX();
        location.y += speedVector.getY();
        boolean collided = false;
        // Controllo se ho hittato una struttura
        List<Structure> structureList = level.getStructures();
        for(int i = 0; i < structureList.size() && !collided; i++) {
            Structure s = structureList.get(i);
            if (hasCollided(s) && s != source) {
                collided = true;
                s.applyDamage(dmg);
                level.getProjectiles().remove(this);
            }
        }
        // Controllo se ho hittato una unità
        List<MobileUnit> units = level.getUnits();
        for(int i = 0; i < units.size() && !collided; i++) {
            MobileUnit u = units.get(i);
            if (hasCollided(u) && u != source) {
                collided = true;
                u.applyDamage(dmg);
                level.getProjectiles().remove(this);
            }
        }
        // Se il proiettile è uscito fuori mappa
        if (!collided && level.isOutOfMap(getHitbox()))
            level.getProjectiles().remove(this);
    }
}

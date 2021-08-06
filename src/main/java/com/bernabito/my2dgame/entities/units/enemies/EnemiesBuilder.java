package com.bernabito.my2dgame.entities.units.enemies;

import com.bernabito.my2dgame.entities.units.MobileUnit;

/**
 * @author Matteo Bernabito
 */

public final class EnemiesBuilder {

    private EnemiesBuilder() {
    }

    public static MobileUnit buildMobById(float x, float y, int id) {
        MobileUnit mob = null;
        switch (id) {
            case 0:
                mob = new Orc();
                break;
            case 1:
                mob = new Skeleton();
                break;
            case 2:
                mob = new DarkElf();
                break;
            default:
                break;
        }
        if (mob != null)
            mob.setLocation(x, y);
        return mob;

    }

}

package com.bernabito.my2dgame.entities.units;

import com.bernabito.my2dgame.entities.projectiles.Projectile;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;

/**
 * @author Matteo Bernabito
 */

public abstract class RangedUnit extends MobileUnit {

    public RangedUnit(SpriteSheet spriteSheet, float hitboxWidth, float hitboxHeight, float hitboxDeltaX, float hitboxDeltaY, int maxHitPoints,
                      Animation[] movingAnimations, Animation[] attackingAnimations, Animation dyingAnimation, int attackFrame) {
        super(spriteSheet, hitboxWidth, hitboxHeight, hitboxDeltaX, hitboxDeltaY, maxHitPoints, movingAnimations, attackingAnimations, dyingAnimation, attackFrame);
    }

    @Override
    protected void attack() {
        Projectile projectile = buildProjectile();
        projectile.initialize(level);
        level.getProjectiles().add(projectile);
    }

    protected abstract Projectile buildProjectile();

}

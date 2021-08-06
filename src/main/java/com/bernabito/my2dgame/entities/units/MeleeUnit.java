package com.bernabito.my2dgame.entities.units;

import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public abstract class MeleeUnit extends MobileUnit {

    public enum TargetType {AREA_OF_EFFECT, SINGLE}

    private final float attackRange;
    private final int attackDamage;
    private TargetType targetType;
    private Unit target;

    public MeleeUnit(SpriteSheet spriteSheet, float hitboxWidth, float hitboxHeight, float hitboxDeltaX, float hitboxDeltaY, int maxHitPoints,
                     Animation[] movingAnimations, Animation[] attackingAnimations, Animation dyingAnimation, int attackFrame, float attackRange, int attackDamage) {
        super(spriteSheet, hitboxWidth, hitboxHeight, hitboxDeltaX, hitboxDeltaY, maxHitPoints, movingAnimations, attackingAnimations, dyingAnimation, attackFrame);
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        targetType = TargetType.AREA_OF_EFFECT;
        target = null;
    }

    protected final void setTargetType(TargetType targetType, Unit target) {
        this.targetType = targetType;
        if (targetType == TargetType.SINGLE)
            this.target = Objects.requireNonNull(target);
    }

    @Override
    protected void attack() {
        if (targetType == TargetType.AREA_OF_EFFECT) {
            for(Unit u : level.getUnits())
                if (u != this && distanceFromMidPoint(u) <= attackRange)
                    u.applyDamage(attackDamage);
        } else {
            if (distanceFromMidPoint(target) <= attackRange)
                target.applyDamage(attackDamage);
        }
    }

}

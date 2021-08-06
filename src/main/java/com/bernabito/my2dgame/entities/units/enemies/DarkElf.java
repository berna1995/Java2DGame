package com.bernabito.my2dgame.entities.units.enemies;

import com.bernabito.my2dgame.engine.GameEngine;
import com.bernabito.my2dgame.entities.units.MobileUnit;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;
import com.bernabito.my2dgame.utils.Direction;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class DarkElf extends MobileUnit implements AI {

    private static final SpriteSheet SPRITE_SHEET = Objects.requireNonNull(SpriteSheet.loadSpriteSheetFromResources("/Images/DarkElfSpriteSheet.png", 64));

    private static final float DARK_ELF_SPEED_PER_SECOND = 185.0f;
    private static final float DARK_ELF_MAX_MODULE_SPEED = DARK_ELF_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final int HIT_POINTS = 40;

    private static final int WALK_ANIMATIONS_UPDATE_PER_SECOND = 30;
    private static final int WALK_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / WALK_ANIMATIONS_UPDATE_PER_SECOND;
    private static final int WALK_ANIMATION_FRAMES = 9;
    private static final int CAST_ANIMATION_UPDATE_PER_SECOND = 3;
    private static final int CAST_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / CAST_ANIMATION_UPDATE_PER_SECOND;
    private static final int CAST_ANIMATION_FRAMES = 7;
    private static final int DEATH_ANIMATION_UPDATE_PER_SECOND = 15;
    private static final int DEATH_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / DEATH_ANIMATION_UPDATE_PER_SECOND;
    private static final int DEATH_ANIMATION_FRAMES = 6;

    public DarkElf() {
        super(SPRITE_SHEET, 28, 48, 18, 13, HIT_POINTS,
                new Animation[]{new Animation(8, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(9, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(10, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(11, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL)},
                new Animation[]{new Animation(0, 0, CAST_ANIMATION_FRAMES, CAST_ANIMATION_UPDATE_INTERVAL),
                        new Animation(1, 0, CAST_ANIMATION_FRAMES, CAST_ANIMATION_UPDATE_INTERVAL),
                        new Animation(2, 0, CAST_ANIMATION_FRAMES, CAST_ANIMATION_UPDATE_INTERVAL),
                        new Animation(3, 0, CAST_ANIMATION_FRAMES, CAST_ANIMATION_UPDATE_INTERVAL)},
                new Animation(20, 0, DEATH_ANIMATION_FRAMES, DEATH_ANIMATION_UPDATE_INTERVAL),
                CAST_ANIMATION_FRAMES - 1);
    }

    @Override
    protected void attack() {
        MobileUnit mob = new Orc();
        mob.setLocation(hitbox.x, hitbox.y);
        mob.initialize(level);
        level.getUnits().add(mob);
    }

    @Override
    protected void die() {
        level.getUnits().remove(this);
    }

    @Override
    public void applyDamage(int dmg) {
        currentHitPoints -= dmg;
    }

    @Override
    public void playAIStep() {
        if (!isAttacking()) {
            requestAttack(Direction.computeAttackDirection(hitbox, level.getPlayer().getHitbox()));
        }
    }
}

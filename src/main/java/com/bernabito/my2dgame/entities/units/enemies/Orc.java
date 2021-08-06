package com.bernabito.my2dgame.entities.units.enemies;

import com.bernabito.my2dgame.engine.GameEngine;
import com.bernabito.my2dgame.entities.units.MeleeUnit;
import com.bernabito.my2dgame.entities.units.Player;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.level.Level;
import com.bernabito.my2dgame.utils.Animation;
import com.bernabito.my2dgame.utils.Direction;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class Orc extends MeleeUnit implements AI {

    private static final SpriteSheet SPRITE_SHEET = Objects.requireNonNull(SpriteSheet.loadSpriteSheetFromResources("/Images/GreenOrcSpriteSheet.png", 64));

    private static final float ORC_SPEED_PER_SECOND = 140.0f;
    private static final float ORC_MAX_MODULE_SPEED = ORC_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final int HIT_POINTS = 60;
    private static final int ATTACK_DAMAGE = 10;
    private static final int ATTACK_FRAME = 4;
    private static final float ATTACK_RANGE = 35;

    private static final int WALK_ANIMATIONS_UPDATE_PER_SECOND = 30;
    private static final int WALK_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / WALK_ANIMATIONS_UPDATE_PER_SECOND;
    private static final int WALK_ANIMATION_FRAMES = 9;
    private static final int ATTACK_ANIMATION_UPDATE_PER_SECOND = 25;
    private static final int ATTACK_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / ATTACK_ANIMATION_UPDATE_PER_SECOND;
    private static final int ATTACK_ANIMATION_FRAMES = 6;
    private static final int DEATH_ANIMATIONS_UPDATE_PER_SECOND = 15;
    private static final int DEATH_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / DEATH_ANIMATIONS_UPDATE_PER_SECOND;
    private static final int DEATH_ANIMATION_FRAMES = 6;

    public Orc() {
        super(SPRITE_SHEET, 28, 44, 18, 18, HIT_POINTS,
                new Animation[]{new Animation(8, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(9, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(10, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(11, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL)},
                new Animation[]{new Animation(12, 0, ATTACK_ANIMATION_FRAMES, ATTACK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(13, 0, ATTACK_ANIMATION_FRAMES, ATTACK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(14, 0, ATTACK_ANIMATION_FRAMES, ATTACK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(15, 0, ATTACK_ANIMATION_FRAMES, ATTACK_ANIMATION_UPDATE_INTERVAL)},
                new Animation(20, 0, DEATH_ANIMATION_FRAMES, DEATH_ANIMATION_UPDATE_INTERVAL),
                ATTACK_FRAME, ATTACK_RANGE, ATTACK_DAMAGE);
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        setTargetType(TargetType.SINGLE, level.getPlayer());
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
        Player player = level.getPlayer();
        float deltaX = (float) (player.getHitbox().getCenterX() - hitbox.getCenterX());
        float deltaY = (float) (player.getHitbox().getCenterY() - hitbox.getCenterY());
        speedVector.set(deltaX, deltaY);
        speedVector.normalize().scale(ORC_MAX_MODULE_SPEED);
        if (distanceFromMidPoint(player) <= ATTACK_RANGE)
            requestAttack(Direction.computeAttackDirection(getHitbox(), player.getHitbox()));
    }
}

package com.bernabito.my2dgame.entities.units.enemies;

import com.bernabito.my2dgame.engine.GameEngine;
import com.bernabito.my2dgame.entities.projectiles.Arrow;
import com.bernabito.my2dgame.entities.projectiles.Projectile;
import com.bernabito.my2dgame.entities.units.Player;
import com.bernabito.my2dgame.entities.units.RangedUnit;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;
import com.bernabito.my2dgame.utils.Direction;
import com.bernabito.my2dgame.utils.Vector2f;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class Skeleton extends RangedUnit implements AI {

    private static final SpriteSheet SPRITE_SHEET = Objects.requireNonNull(SpriteSheet.loadSpriteSheetFromResources("/Images/SkeletonSpriteSheet.png", 64));

    private static final float ARROW_SPEED_PER_SECOND = 350.0f;
    private static final float ARROW_MODULE_SPEED = ARROW_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final float SKELETON_SPEED_PER_SECOND = 185.0f;
    private static final float SKELETON_MAX_MODULE_SPEED = SKELETON_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final int PROJECTILE_DAMAGE = 10;
    private static final int HIT_POINTS = 40;
    private static final float ATTACK_RANGE = 650;

    private static final int WALK_ANIMATIONS_UPDATE_PER_SECOND = 30;
    private static final int WALK_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / WALK_ANIMATIONS_UPDATE_PER_SECOND;
    private static final int WALK_ANIMATION_FRAMES = 9;
    private static final int SHOOT_ANIMATION_UPDATE_PER_SECOND = 15;
    private static final int SHOOT_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / SHOOT_ANIMATION_UPDATE_PER_SECOND;
    private static final int SHOOT_ANIMATION_FRAMES = 10;
    private static final int DEATH_ANIMATION_UPDATE_PER_SECOND = 15;
    private static final int DEATH_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / DEATH_ANIMATION_UPDATE_PER_SECOND;
    private static final int DEATH_ANIMATION_FRAMES = 6;

    public Skeleton() {
        super(SPRITE_SHEET, 28, 48, 18, 13, HIT_POINTS,
                new Animation[]{new Animation(8, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(9, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(10, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL),
                        new Animation(11, 0, WALK_ANIMATION_FRAMES, WALK_ANIMATION_UPDATE_INTERVAL)},
                new Animation[]{new Animation(16, 0, SHOOT_ANIMATION_FRAMES, SHOOT_ANIMATION_UPDATE_INTERVAL),
                        new Animation(17, 0, SHOOT_ANIMATION_FRAMES, SHOOT_ANIMATION_UPDATE_INTERVAL),
                        new Animation(18, 0, SHOOT_ANIMATION_FRAMES, SHOOT_ANIMATION_UPDATE_INTERVAL),
                        new Animation(19, 0, SHOOT_ANIMATION_FRAMES, SHOOT_ANIMATION_UPDATE_INTERVAL)},
                new Animation(20, 0, DEATH_ANIMATION_FRAMES, DEATH_ANIMATION_UPDATE_INTERVAL),
                SHOOT_ANIMATION_FRAMES - 1);
    }

    @Override
    protected Projectile buildProjectile() {
        Player player = level.getPlayer();
        float deltaX = (float) (player.getHitbox().getCenterX() - hitbox.getCenterX());
        float deltaY = (float) (player.getHitbox().getCenterY() - hitbox.getCenterY());
        Vector2f arrowSpeedVector = new Vector2f(deltaX, deltaY);
        arrowSpeedVector.normalize().scale(ARROW_MODULE_SPEED);
        return new Arrow(this, (float) hitbox.getCenterX(), (float) hitbox.getCenterY(), arrowSpeedVector, PROJECTILE_DAMAGE);
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
        if (distanceFromMidPoint(player) <= ATTACK_RANGE) {
            speedVector.set(0, 0);
            requestAttack(Direction.computeAttackDirection(hitbox, player.getHitbox()));
        } else {
            float deltaX = (float) (player.getHitbox().getCenterX() - hitbox.getCenterX());
            float deltaY = (float) (player.getHitbox().getCenterY() - hitbox.getCenterY());
            speedVector.set(deltaX, deltaY);
            speedVector.normalize().scale(SKELETON_MAX_MODULE_SPEED);
        }
    }
}

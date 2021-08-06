package com.bernabito.my2dgame.entities.units;

import com.bernabito.my2dgame.engine.GameEngine;
import com.bernabito.my2dgame.entities.projectiles.Arrow;
import com.bernabito.my2dgame.entities.projectiles.Projectile;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;
import com.bernabito.my2dgame.utils.Direction;
import com.bernabito.my2dgame.utils.Vector2f;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class Player extends RangedUnit {

    private static final SpriteSheet SPRITE_SHEET = Objects.requireNonNull(SpriteSheet.loadSpriteSheetFromResources("/Images/PlayerSpriteSheet.png", 64));

    private static final float ARROW_SPEED_PER_SECOND = 480.0f;
    private static final float ARROW_MODULE_SPEED = ARROW_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final float PLAYER_SPEED_PER_SECOND = 240.0f;
    private static final float PLAYER_MAX_MODULE_SPEED = PLAYER_SPEED_PER_SECOND / GameEngine.TARGET_UPS;
    private static final int PROJECTILE_DAMAGE = 20;
    private static final int HIT_POINTS = 100;

    private static final int WALK_ANIMATIONS_UPDATE_PER_SECOND = 30;
    private static final int WALK_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / WALK_ANIMATIONS_UPDATE_PER_SECOND;
    private static final int WALK_ANIMATION_FRAMES = 9;
    private static final int SHOOT_ANIMATION_UPDATE_PER_SECOND = 30;
    private static final int SHOOT_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / SHOOT_ANIMATION_UPDATE_PER_SECOND;
    private static final int SHOOT_ANIMATION_FRAMES = 10;
    private static final int DEATH_ANIMATION_UPDATE_PER_SECOND = 15;
    private static final int DEATH_ANIMATION_UPDATE_INTERVAL = GameEngine.TARGET_UPS / DEATH_ANIMATION_UPDATE_PER_SECOND;
    private static final int DEATH_ANIMATION_FRAMES = 6;

    private double projectileAngle;

    public Player() {
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

    public void setSpeedRatio(float vxRatio, float vyRatio) {
        if (vxRatio > 1 || vxRatio < -1 || vyRatio > 1 || vyRatio < -1)
            throw new IllegalArgumentException("Ratio must be between -1.0 and 1.0");
        speedVector.set(vxRatio, vyRatio);
        // Uso solo l'angolo di fatto, la velocità in modulo è sempre massima
        speedVector.normalize().scale(PLAYER_MAX_MODULE_SPEED);
    }

    public void requestAttack(double projectileAngle) {
        requestAttack(Direction.computeDirectionByAngle(projectileAngle));
        this.projectileAngle = projectileAngle;
    }

    @Override
    public void applyDamage(int dmg) {
        currentHitPoints -= dmg;
    }


    @Override
    protected Projectile buildProjectile() {
        Vector2f arrowSpeedVector = new Vector2f(ARROW_MODULE_SPEED, projectileAngle);
        return new Arrow(this, (float) hitbox.getCenterX(), (float) hitbox.getCenterY(), arrowSpeedVector, PROJECTILE_DAMAGE);
    }

    @Override
    protected void die() {
        level.markAsCompleted();
    }


}

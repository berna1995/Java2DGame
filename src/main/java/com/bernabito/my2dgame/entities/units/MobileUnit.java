package com.bernabito.my2dgame.entities.units;

import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;
import com.bernabito.my2dgame.utils.Direction;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public abstract class MobileUnit extends Unit {

    private static final float HP_BAR_LENGTH = 48;
    private static final float HALF_HP_BAR_LENGTH = HP_BAR_LENGTH / 2.0f;

    private final SpriteSheet spriteSheet;
    private final Animation[] movingAnimations;
    private final Animation[] attackingAnimations;
    private final Animation dyingAnimation;

    private final float deltaDrawX;
    private final float deltaDrawY;

    private boolean attackRequested;
    private boolean animatingAttack;
    private boolean animatingDeath;
    private boolean executeDie;

    protected int animationRow;
    protected int animationColumn;
    protected Direction attackDirection;
    private Animation currentAnimation;
    private final int attackFrame;

    public MobileUnit(SpriteSheet spriteSheet, float hitboxWidth, float hitboxHeight, float hitboxDeltaX, float hitboxDeltaY, int maxHitPoints,
                      Animation[] movingAnimations, Animation[] attackingAnimations, Animation dyingAnimation, int attackFrame) {
        super(new Rectangle2D.Float(0, 0, hitboxWidth, hitboxHeight), true, maxHitPoints);
        this.spriteSheet = Objects.requireNonNull(spriteSheet);
        deltaDrawX = hitboxDeltaX;
        deltaDrawY = hitboxDeltaY;
        this.movingAnimations = Objects.requireNonNull(movingAnimations);
        this.attackingAnimations = Objects.requireNonNull(attackingAnimations);
        this.dyingAnimation = Objects.requireNonNull(dyingAnimation);
        this.attackFrame = attackFrame;
        attackRequested = false;
        animatingAttack = false;
        executeDie = false;
        // Di default si punta verso il basso
        currentAnimation = movingAnimations[2];
    }

    @Override
    public void renderEntity(Graphics2D g) {
        spriteSheet.render(g, animationRow, animationColumn, location.x - deltaDrawX, location.y - deltaDrawY);
    }

    @Override
    public void updateState() {
        super.updateState();
        if (isDead()) {
            if (!animatingDeath) {
                animatingDeath = true;
                currentAnimation.reset();
                currentAnimation = dyingAnimation;
            } else {
                if (executeDie)
                    die();
                else if (currentAnimation.isLastFrame())
                    executeDie = true;
                else
                    currentAnimation.play();
            }
        } else {
            if (attackRequested || animatingAttack) {
                if (!animatingAttack) {
                    animatingAttack = true;
                    currentAnimation.reset();
                    currentAnimation = computeShootAnimation(attackDirection);
                } else {
                    currentAnimation.play();
                    if (currentAnimation.getCurrentAnimationIndex() == attackFrame)
                        attack();
                    if (currentAnimation.isLastFrame()) {
                        animatingAttack = false;
                        attackRequested = false;
                        canMove = true;
                    }
                }
            } // Movimento
            else {
                float vx = speedVector.getX();
                float vy = speedVector.getY();
                Direction movingDirection = Direction.computeMovingDirection(vx, vy);
                if (movingDirection == Direction.STILL)
                    currentAnimation.reset();
                else {
                    Animation computedAnimation = computeMovingAnimation(movingDirection);
                    if (computedAnimation != currentAnimation) {
                        currentAnimation.reset();
                        currentAnimation = computedAnimation;
                    } else
                        currentAnimation.play();
                }
            }
        }

        animationRow = currentAnimation.getAnimationRow();
        animationColumn = currentAnimation.getCurrentAnimationIndex();
    }

    private Animation computeShootAnimation(Direction facingDirection) {
        switch (facingDirection) {
            case UP:
                return attackingAnimations[0];
            case LEFT:
                return attackingAnimations[1];
            case DOWN:
                return attackingAnimations[2];
            case RIGHT:
                return attackingAnimations[3];
            default:
                return null;
        }
    }

    private Animation computeMovingAnimation(Direction movingDirection) {
        switch (movingDirection) {
            case UP:
                return movingAnimations[0];
            case LEFT:
                return movingAnimations[1];
            case DOWN:
                return movingAnimations[2];
            case RIGHT:
                return movingAnimations[3];
            default:
                return null;
        }
    }

    protected void requestAttack(Direction attackDirection) {
        attackRequested = true;
        this.attackDirection = attackDirection;
        canMove = false;
    }

    public boolean isAttacking() {
        return attackRequested || animatingAttack;
    }

    @Override
    protected Rectangle2D.Float getHPBarRectangle() {
        if (!isDead())
            return new Rectangle2D.Float((float) hitbox.getCenterX() - HALF_HP_BAR_LENGTH, hitbox.y - Unit.DEFAULT_HP_BAR_HEIGHT - 5, HP_BAR_LENGTH, Unit.DEFAULT_HP_BAR_HEIGHT);
        else
            return null; // Se sto eseguendo l'animazione di morte non mostro la barra della vita
    }

    protected abstract void attack();

    protected abstract void die();
}

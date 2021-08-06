package com.bernabito.my2dgame.utils;

import java.awt.geom.Rectangle2D;

/**
 * @author Matteo Bernabito
 */

public enum Direction {

    STILL, LEFT, RIGHT, UP, DOWN;

    private static double TOP_INFERIOR_LIMIT_ANGLE = Math.toRadians(-135);
    private static double TOP_SUPERIOR_LIMIT_ANGLE = Math.toRadians(-45);
    private static double RIGHT_SUPERIOR_LIMIT_ANGLE = Math.toRadians(45);
    private static double BOTTOM_SUPERIOR_LIMIT_ANGLE = Math.toRadians(135);

    public static Direction computeMovingDirection(float vx, float vy) {
        if (vx == 0 && vy == 0)
            return STILL;

        if (Math.abs(vx) > Math.abs(vy)) {
            if (vx > 0)
                return RIGHT;
            else
                return LEFT;
        } else {
            if (vy > 0)
                return DOWN;
            else
                return UP;
        }
    }

    public static Direction computeAttackDirection(float attackerX, float attackerY, float attackedX, float attackedY) {
        float deltaX = attackedX - attackerX;
        float deltaY = attackedY - attackerY;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX >= 0)
                return RIGHT;
            else
                return LEFT;
        } else {
            if (deltaY >= 0)
                return DOWN;
            else
                return UP;
        }
    }

    public static Direction computeAttackDirection(Rectangle2D attackerHitbox, Rectangle2D attackedHitbox) {
        return computeAttackDirection((float) attackerHitbox.getCenterX(), (float) attackerHitbox.getCenterY(), (float) attackedHitbox.getCenterX(), (float) attackedHitbox.getCenterY());
    }

    public static Direction computeDirectionByAngle(double angle) {
        if (angle >= TOP_INFERIOR_LIMIT_ANGLE && angle < TOP_SUPERIOR_LIMIT_ANGLE)
            return UP;
        else if (angle >= TOP_SUPERIOR_LIMIT_ANGLE && angle < RIGHT_SUPERIOR_LIMIT_ANGLE)
            return RIGHT;
        else if (angle >= RIGHT_SUPERIOR_LIMIT_ANGLE && angle < BOTTOM_SUPERIOR_LIMIT_ANGLE)
            return DOWN;
        else
            return LEFT;
    }

}

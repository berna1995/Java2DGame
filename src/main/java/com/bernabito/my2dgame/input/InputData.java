package com.bernabito.my2dgame.input;

/**
 * @author Matteo Bernabito
 */

public class InputData {

    private float vxRatio;
    private float vyRatio;
    private double attackAngle;
    private boolean attacking;
    private boolean confirming;
    private boolean pausing;

    public InputData() {
        vxRatio = 0;
        vyRatio = 0;
        attackAngle = 0.0;
        attacking = false;
        pausing = false;
        confirming = false;
    }

    public synchronized float getSpeedRatioX() {
        return vxRatio;
    }

    public synchronized void setSpeedRatioX(float vx) {
        vxRatio = vx;
    }

    public synchronized float getSpeedRatioY() {
        return vyRatio;
    }

    public synchronized void setSpeedRatioY(float vy) {
        vyRatio = vy;
    }

    public synchronized boolean isAttackButtonPressed() {
        return attacking;
    }

    public synchronized void setAttackButtonPressed(boolean attacking) {
        this.attacking = attacking;
    }

    public synchronized void setConfirmButtonPressed(boolean confirming) {
        this.confirming = confirming;
    }

    public synchronized boolean isConfirmButtonPressed() {
        return confirming;
    }

    public synchronized boolean isPauseButtonPressed() {
        return pausing;
    }

    public synchronized void setPauseButtonPressed(boolean pausing) {
        this.pausing = pausing;
    }

    public synchronized double getAttackAngle() {
        return attackAngle;
    }

    public synchronized void setAttackAngle(double attackAngle) {
        this.attackAngle = attackAngle;
    }
}

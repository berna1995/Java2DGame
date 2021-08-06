package com.bernabito.my2dgame.utils;

/**
 * @author Matteo Bernabito
 */


public class Animation {

    private final int animationRow;
    private final int animationStartIndex;
    private final int animationEndIndex;
    private final int updateIntervalFrame;

    private int currentAnimationIndex;
    private int framesBeforeUpdate;

    public Animation(int animationRow, int animationStartIndex, int animationLength, int updateIntervalFrame) {
        this.animationRow = animationRow;
        this.animationStartIndex = animationStartIndex;
        currentAnimationIndex = animationStartIndex;
        animationEndIndex = animationStartIndex + animationLength - 1;
        this.updateIntervalFrame = updateIntervalFrame - 1;
        framesBeforeUpdate = 0;
    }

    public void play() {
        if (framesBeforeUpdate == 0) {
            currentAnimationIndex++;
            if (currentAnimationIndex > animationEndIndex)
                currentAnimationIndex = animationStartIndex;
            framesBeforeUpdate = updateIntervalFrame;
        } else {
            framesBeforeUpdate--;
        }
    }

    public void reset() {
        currentAnimationIndex = animationStartIndex;
    }

    public int getAnimationRow() {
        return animationRow;
    }

    public int getCurrentAnimationIndex() {
        return currentAnimationIndex;
    }

    public boolean isLastFrame() {
        return currentAnimationIndex == animationEndIndex;
    }
}

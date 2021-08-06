package com.bernabito.my2dgame.engine;

/**
 * @author Matteo Bernabito
 */

public class GameTimer {

    private long lastTimestampNs;

    public GameTimer() {
        lastTimestampNs = 0;
    }

    public void startFromNow() {
        lastTimestampNs = getCurrentTimeNanos();
    }

    public long computeTimeDelta() {
        long now = getCurrentTimeNanos();
        long delta = now - lastTimestampNs;
        lastTimestampNs = now;
        return delta;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getCurrentTimeNanos() {
        return System.nanoTime();
    }

}

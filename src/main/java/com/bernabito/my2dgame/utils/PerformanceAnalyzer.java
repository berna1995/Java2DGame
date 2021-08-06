package com.bernabito.my2dgame.utils;

/**
 * @author Matteo Bernabito
 */

public class PerformanceAnalyzer {

    private static final int MILLISECOND_IN_NANOS = 1000000;

    private final long[] readings;
    private final int maxReadings;
    private long sum;

    private long startTimestamp;
    private int actualReadings;
    private int currentIndex;

    public PerformanceAnalyzer(int maxReadings) {
        this.maxReadings = maxReadings;
        readings = new long[maxReadings];
        reset();
    }

    public void startTimeMeasure() {
        startTimestamp = System.nanoTime();
    }

    public void endTimeMeasure() {
        long measurement = System.nanoTime() - startTimestamp;

        sum = sum + measurement - readings[currentIndex];
        readings[currentIndex] = measurement;
        currentIndex = (currentIndex + 1) % maxReadings;

        if (actualReadings < maxReadings)
            actualReadings++;
    }

    public void reset() {
        for(int i = 0; i < maxReadings; i++)
            readings[i] = 0L;

        actualReadings = 0;
        currentIndex = 0;
        sum = 0;
    }

    public double averageTimeMillis() {
        return (sum / (double) actualReadings) / MILLISECOND_IN_NANOS;
    }

}

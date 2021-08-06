package com.bernabito.my2dgame.utils;

/**
 * @author Matteo Bernabito
 */

public class Vector2f {
    private float x;
    private float y;

    public Vector2f() {
        this(0.0f, 0.0f);
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(float module, double angle) {
        this.x = (float) (Math.cos(angle) * module);
        this.y = (float) (Math.sin(angle) * module);
    }

    public Vector2f normalize() {
        float module = (float) Math.sqrt((x * x) + (y * y));
        if (module > 0) {
            this.x = this.x / module;
            this.y = this.y / module;
        }
        return this;
    }

    public void scale(float scaleFactor) {
        this.x = this.x * scaleFactor;
        this.y = this.y * scaleFactor;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

}

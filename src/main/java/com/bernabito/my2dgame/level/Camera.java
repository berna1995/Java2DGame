package com.bernabito.my2dgame.level;

import com.bernabito.my2dgame.engine.GameCanvas;
import com.bernabito.my2dgame.entities.units.Player;

import java.awt.geom.Point2D;

/**
 * @author Matteo Bernabito
 */

public class Camera {

    private final GameCanvas gameCanvas;

    private Point2D.Float cameraLocation;

    public Camera(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        cameraLocation = new Point2D.Float(0, 0);
    }

    public void trackPlayer(Player player) {
        cameraLocation.x = -((float) player.getHitbox().getCenterX() - (gameCanvas.getWidth() / 2.0f));
        cameraLocation.y = -((float) player.getHitbox().getCenterY() - (gameCanvas.getHeight() / 2.0f));
    }

    public void setCameraX(float cameraX) {
        cameraLocation.x = cameraX;
    }

    public void setCameraY(float cameraY) {
        cameraLocation.y = cameraY;
    }

    public float getCameraX() {
        return cameraLocation.x;
    }

    public float getCameraY() {
        return cameraLocation.y;
    }

}

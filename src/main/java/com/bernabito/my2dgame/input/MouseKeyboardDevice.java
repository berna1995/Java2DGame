package com.bernabito.my2dgame.input;

import com.bernabito.my2dgame.engine.GameCanvas;

import java.awt.event.*;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class MouseKeyboardDevice implements InputDevice, KeyListener, MouseListener, MouseMotionListener {

    private final InputData inputData;
    private final GameCanvas canvas;

    public MouseKeyboardDevice(GameCanvas canvas) {
        this.canvas = Objects.requireNonNull(canvas);
        inputData = new InputData();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                inputData.setSpeedRatioY(-1.0f);
                break;
            case KeyEvent.VK_S:
                inputData.setSpeedRatioY(1.0f);
                break;
            case KeyEvent.VK_A:
                inputData.setSpeedRatioX(-1.0f);
                break;
            case KeyEvent.VK_D:
                inputData.setSpeedRatioX(1.0f);
                break;
            case KeyEvent.VK_ENTER:
                inputData.setConfirmButtonPressed(true);
                break;
            case KeyEvent.VK_P:
                inputData.setPauseButtonPressed(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (inputData.getSpeedRatioY() < 0)
                    inputData.setSpeedRatioY(0.0f);
                break;
            case KeyEvent.VK_S:
                if (inputData.getSpeedRatioY() > 0)
                    inputData.setSpeedRatioY(0.0f);
                break;
            case KeyEvent.VK_A:
                if (inputData.getSpeedRatioX() < 0)
                    inputData.setSpeedRatioX(0.0f);
                break;
            case KeyEvent.VK_D:
                if (inputData.getSpeedRatioX() > 0)
                    inputData.setSpeedRatioX(0.0f);
                break;
            case KeyEvent.VK_ENTER:
                inputData.setConfirmButtonPressed(false);
                break;
            case KeyEvent.VK_P:
                inputData.setPauseButtonPressed(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        inputData.setAttackButtonPressed(true);
        inputData.setAttackAngle(computeAttackAngle(e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        inputData.setAttackButtonPressed(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        inputData.setAttackAngle(computeAttackAngle(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void poll() {

    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public InputData getInputData() {
        return inputData;
    }

    public void register() {
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void unregister() {
        canvas.removeKeyListener(this);
        canvas.removeMouseListener(this);
        canvas.removeMouseMotionListener(this);
    }

    private double computeAttackAngle(int x, int y) {
        double canvasCenterX = (double) canvas.getWidth() / 2.0;
        double canvasCenterY = (double) canvas.getHeight() / 2.0;
        double deltaX = x - canvasCenterX;
        double deltaY = y - canvasCenterY;
        return Math.atan2(deltaY, deltaX);
    }


}

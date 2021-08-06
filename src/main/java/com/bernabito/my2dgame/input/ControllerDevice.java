package com.bernabito.my2dgame.input;


import com.github.strikerx3.jxinput.*;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */


public class ControllerDevice implements InputDevice {

    private final static float DEAD_ZONE_ANALOG = 0.1f;

    private final XInputDevice xInputDevice;
    private final InputData inputData;
    private boolean isConnected;

    public ControllerDevice(XInputDevice xInputDevice) {
        this.xInputDevice = Objects.requireNonNull(xInputDevice);
        inputData = new InputData();
        isConnected = true;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public InputData getInputData() {
        return inputData;
    }

    @Override
    public void poll() {
        isConnected = xInputDevice.poll();
        if (isConnected) {
            XInputComponents components = xInputDevice.getComponents();
            XInputAxes axes = components.getAxes();
            XInputButtons buttons = components.getButtons();
            inputData.setAttackButtonPressed(buttons.a);
            inputData.setConfirmButtonPressed(buttons.b);
            inputData.setPauseButtonPressed(buttons.start);
            float leftThumbXRatio = processThumbRatio(axes.lx);
            float leftThumbYRatio = -processThumbRatio(axes.ly);
            inputData.setSpeedRatioX(leftThumbXRatio);
            inputData.setSpeedRatioY(leftThumbYRatio);
            inputData.setAttackAngle(Math.atan2(leftThumbYRatio, leftThumbXRatio));
        }
    }

    private float processThumbRatio(float controllerValue) {
        if (Math.abs(controllerValue) <= DEAD_ZONE_ANALOG)
            return 0.0f;
        else
            return controllerValue;
    }
}

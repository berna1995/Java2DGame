package com.bernabito.my2dgame.input;

/**
 * @author Matteo Bernabito
 */

public interface InputDevice {

    void poll();

    boolean isConnected();

    InputData getInputData();


}

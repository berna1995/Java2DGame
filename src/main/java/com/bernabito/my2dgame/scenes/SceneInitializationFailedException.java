package com.bernabito.my2dgame.scenes;

/**
 * @author Matteo Bernabito
 */

public class SceneInitializationFailedException extends Exception {

    public SceneInitializationFailedException(String message, Exception cause) {
        super(message, cause);
    }

    public SceneInitializationFailedException(Exception cause) {
        super(cause);
    }

}

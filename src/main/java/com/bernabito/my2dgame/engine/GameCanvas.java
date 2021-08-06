package com.bernabito.my2dgame.engine;

import java.awt.*;

/**
 * @author Matteo Bernabito
 */

public class GameCanvas extends Canvas {

    public GameCanvas() {
        System.setProperty("sun.awt.noerasebackground", "true");
        setIgnoreRepaint(true);
        setFocusable(true);
        requestFocus();
    }

}

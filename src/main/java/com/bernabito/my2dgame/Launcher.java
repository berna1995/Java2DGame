package com.bernabito.my2dgame;

import com.bernabito.my2dgame.engine.GameCanvas;
import com.bernabito.my2dgame.engine.GameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point del gioco
 *
 * @author Matteo Bernabito
 */

public class Launcher {

    private final static Dimension WINDOW_SIZE = new Dimension(1280, 720);
    private final static String WINDOW_TITLE = "My 2D Game";

    public static void main(String args[]) {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(WINDOW_SIZE);
        frame.setMinimumSize(WINDOW_SIZE);
        GameCanvas gameCanvas = new GameCanvas();
        frame.add(gameCanvas);
        frame.pack();
        GameEngine gameEngine = new GameEngine(gameCanvas);
        gameEngine.setRenderDebugInformations(true);
        gameEngine.start();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

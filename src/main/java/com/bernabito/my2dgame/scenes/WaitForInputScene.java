package com.bernabito.my2dgame.scenes;

import com.bernabito.my2dgame.engine.GameCanvas;
import com.bernabito.my2dgame.input.InputData;

import java.awt.*;

/**
 * @author Matteo Bernabito
 */


public class WaitForInputScene extends Scene {

    private static final Font MESSAGE_FONT = new Font("System", Font.PLAIN, 12);

    private final String message;
    private boolean inputReceived;

    public WaitForInputScene(String message, Scene nextScene, GameCanvas gameCanvas) {
        super(gameCanvas, nextScene);
        this.message = message;
        inputReceived = false;
    }

    @Override
    public void init() {
    }

    @Override
    public void updateState() {
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        g.setColor(Color.WHITE);
        g.setFont(MESSAGE_FONT);
        FontMetrics fontMetrics = g.getFontMetrics();

        String[] lines = message.split("\r\n");

        float fontHeight = fontMetrics.getHeight();
        float totalLinesHeight = fontHeight * lines.length;
        float startY = (gameCanvas.getHeight() / 2.0f) - ((totalLinesHeight - fontHeight) / 2.0f);

        for(int i = 0; i < lines.length; i++) {
            if (!lines[i].isEmpty()) {
                float width = fontMetrics.stringWidth(lines[i]);
                float x = (gameCanvas.getWidth() / 2.0f) - (width / 2.0f);
                float y = startY + (fontHeight * i);
                g.drawString(lines[i], x, y);
            }
        }
    }

    @Override
    public void processInput(InputData inputData) {
        if (inputData.isConfirmButtonPressed())
            inputReceived = true;
    }

    @Override
    public boolean completed() {
        return inputReceived;
    }

}

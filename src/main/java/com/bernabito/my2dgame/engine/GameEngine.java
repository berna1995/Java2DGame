package com.bernabito.my2dgame.engine;

import com.bernabito.my2dgame.input.ControllerDevice;
import com.bernabito.my2dgame.input.InputDevice;
import com.bernabito.my2dgame.input.MouseKeyboardDevice;
import com.bernabito.my2dgame.level.Level;
import com.bernabito.my2dgame.scenes.Scene;
import com.bernabito.my2dgame.scenes.SceneInitializationFailedException;
import com.bernabito.my2dgame.scenes.WaitForInputScene;
import com.bernabito.my2dgame.utils.PerformanceAnalyzer;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class GameEngine {

    public static final int TARGET_UPS = 60;
    private static final int PERF_AVG_SIZE = 30;
    private static final int NUM_BUFFERS = 2;
    private static final long SECOND_IN_NANOSECONDS = 1000000000L;
    private static final long FRAME_TIME_NS = SECOND_IN_NANOSECONDS / TARGET_UPS;
    private static final String START_LEVEL_PATH = "/Levels/Level 1.xml";

    private static final Font DEBUG_INFO_FONT = new Font("System", Font.PLAIN, 11);

    private final GameCanvas gameCanvas;
    private final Thread engineThread;
    private final GameTimer gameTimer;

    private InputDevice inputDevice;

    private boolean engineRunning;
    private boolean renderOnlyIfChanged;
    private boolean showDebugInformations;
    private double currentUPS;
    private double currentFPS;
    private PerformanceAnalyzer updateTimeAnalyzer;
    private PerformanceAnalyzer renderTimeAnalyzer;
    private Scene scene;

    private String PRESS_X_TO_CONTINUE;
    private String PRESS_X_TO_SHOOT;
    private String PRESS_X_TO_MOVE;
    private String PRESS_X_TO_PAUSE;

    public GameEngine(GameCanvas gameCanvas) {
        this.gameCanvas = Objects.requireNonNull(gameCanvas);
        engineThread = new Thread(this::engineLoop);
        gameTimer = new GameTimer();
        engineRunning = false;
        renderOnlyIfChanged = true;
        showDebugInformations = false;
        currentFPS = 0;
        currentUPS = 0;
        updateTimeAnalyzer = new PerformanceAnalyzer(PERF_AVG_SIZE);
        renderTimeAnalyzer = new PerformanceAnalyzer(PERF_AVG_SIZE);
        initializeInputDevices();
        initializeCommandExplainationsStrings();
        Scene firstLevel = new Level(START_LEVEL_PATH, gameCanvas);
        String initialMessage = firstLevel.getSceneName() + "\r\n\r\n" + PRESS_X_TO_MOVE + "\r\n" + PRESS_X_TO_SHOOT + "\r\n" + PRESS_X_TO_PAUSE + "\r\n" + PRESS_X_TO_CONTINUE;
        scene = new WaitForInputScene(initialMessage, firstLevel, gameCanvas);
        try {
            scene.initialize();
        } catch (SceneInitializationFailedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        gameCanvas.createBufferStrategy(NUM_BUFFERS);
    }


    public void start() {
        engineRunning = true;
        gameTimer.startFromNow();
        engineThread.start();
    }

    public void stop() {
        engineRunning = false;
        try {
            engineThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void setRenderOnlyIfChanged(boolean renderOnlyIfChanged) {
        this.renderOnlyIfChanged = renderOnlyIfChanged;
    }

    public void setRenderDebugInformations(boolean renderDebugInformations) {
        showDebugInformations = renderDebugInformations;
    }

    private void engineLoop() {
        long deltaSum = 0L;
        int updates = 0;
        int frames = 0;
        boolean shouldRender = true;
        long onceASecondTimeStamp = GameTimer.getCurrentTimeNanos();

        while (engineRunning) {
            deltaSum += gameTimer.computeTimeDelta();

            handleUserInput();

            // Se sono rimasto "indietro", cerco di recuperare aggiornando lo stato anche piu volte se necessario
            while (deltaSum >= FRAME_TIME_NS) {
                deltaSum -= FRAME_TIME_NS;
                updateTimeAnalyzer.startTimeMeasure();
                updateGameState();
                updateTimeAnalyzer.endTimeMeasure();
                updates++;
                shouldRender = true;
            }

            if (shouldRender) {
                renderTimeAnalyzer.startTimeMeasure();
                render();
                renderTimeAnalyzer.endTimeMeasure();
                frames++;
                shouldRender = !renderOnlyIfChanged;
            }


            // Se è passato almeno un secondo, misuro e salvo FPS e UPS
            long elapsedTime = GameTimer.getCurrentTimeNanos() - onceASecondTimeStamp;
            if (elapsedTime >= SECOND_IN_NANOSECONDS) {
                onceASecondTimeStamp += SECOND_IN_NANOSECONDS;
                currentUPS = (SECOND_IN_NANOSECONDS * updates) / (double) elapsedTime;
                currentFPS = (SECOND_IN_NANOSECONDS * frames) / (double) elapsedTime;
                updates = 0;
                frames = 0;
            }

            checkSceneState();
        }
    }

    private void handleUserInput() {
        inputDevice.poll();
        if (inputDevice.isConnected()) {
            scene.processInput(inputDevice.getInputData());
        } else {
            scene = new WaitForInputScene("Input device disconnected.\r\nPlease reconnect.\r\n" + PRESS_X_TO_CONTINUE, scene, gameCanvas);
        }
    }

    private void updateGameState() {
        scene.updateState();
    }

    private void checkSceneState() {
        if (scene.completed()) {
            if (scene instanceof Level) {
                Level currentLvl = (Level) scene;
                if (currentLvl.getPlayer().isDead())
                    scene = new WaitForInputScene("You died.\r\n\r\n" + PRESS_X_TO_CONTINUE, new Level(START_LEVEL_PATH, gameCanvas), gameCanvas);
                else {
                    if (currentLvl.hasNextScene()) {
                        Scene nextScene = scene.getNextScene();
                        scene = new WaitForInputScene(nextScene.getSceneName() + "\r\n\r\n" + PRESS_X_TO_CONTINUE, scene.getNextScene(), gameCanvas);
                    } else
                        scene = new WaitForInputScene("Congratulations, you've finished the game.", null, gameCanvas);
                }
            } else if (scene instanceof WaitForInputScene) {
                if (scene.hasNextScene()) {
                    scene = scene.getNextScene();
                    try {
                        scene.initialize();
                    } catch (SceneInitializationFailedException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }
    }

    private void render() {
        BufferStrategy bs = gameCanvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        // Rendering della scena
        scene.render(g);
        // Rendering informazioni di debug (se attive)
        if (showDebugInformations)
            renderDebugInformations(g);
        // Rilascio le risorse e mostro ciò che ho disegnato
        g.dispose();
        bs.show();
    }

    private void renderDebugInformations(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(DEBUG_INFO_FONT);
        FontMetrics fontMetrics = g.getFontMetrics();
        float fontActualHeight = fontMetrics.getMaxAscent() - fontMetrics.getDescent() - fontMetrics.getLeading();
        String lineFPSUPS = "FPS: " + Math.round(currentFPS) + " / UPS: " + Math.round(currentUPS);
        String renderTimeString = String.format("Average render time: %.5f ms", renderTimeAnalyzer.averageTimeMillis());
        String updateTimeString = String.format("Average logic time: %.5f ms", updateTimeAnalyzer.averageTimeMillis());
        String lineDevice = "Input Device: " + getInputDeviceName();
        g.setColor(Color.WHITE);
        g.drawString(lineFPSUPS, 0, fontActualHeight);
        g.drawString(renderTimeString, 0, fontActualHeight * 2);
        g.drawString(updateTimeString, 0, fontActualHeight * 3);
        g.drawString(lineDevice, 0, fontActualHeight * 4);
    }

    private void initializeInputDevices() {
        if (XInputDevice.isAvailable()) {
            if (!initializeController())
                initializeKeyboard();
        } else {
            initializeKeyboard();
        }
    }

    private void initializeKeyboard() {
        MouseKeyboardDevice mouseKeyboardDevice = new MouseKeyboardDevice(gameCanvas);
        mouseKeyboardDevice.register();
        inputDevice = mouseKeyboardDevice;
    }

    private boolean initializeController() {
        try {
            XInputDevice xInputDevice = XInputDevice.getDeviceFor(0);
            if (xInputDevice.poll()) {
                inputDevice = new ControllerDevice(xInputDevice);
                return true;
            } else
                return false;
        } catch (XInputNotLoadedException e) {
            return false;
        }
    }

    private void initializeCommandExplainationsStrings() {
        if (inputDevice instanceof MouseKeyboardDevice) {
            PRESS_X_TO_CONTINUE = "Press enter to continue.";
            PRESS_X_TO_SHOOT = "Use the mouse to aim and shoot.";
            PRESS_X_TO_MOVE = "Use W-A-S-D to move.";
            PRESS_X_TO_PAUSE = "Press P to pause.";
        } else if (inputDevice instanceof ControllerDevice) {
            PRESS_X_TO_CONTINUE = "Press B to continue.";
            PRESS_X_TO_SHOOT = "Press A to shoot and left analog to aim.";
            PRESS_X_TO_MOVE = "Use the left analog to move.";
            PRESS_X_TO_PAUSE = "Press start to pause.";
        } else
            throw new UnsupportedOperationException("Unsupported input device");
    }

    private String getInputDeviceName() {
        if (inputDevice instanceof MouseKeyboardDevice)
            return "mouse + kb";
        else if (inputDevice instanceof ControllerDevice)
            return "controller";
        else
            return "unknown";
    }

}

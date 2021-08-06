package com.bernabito.my2dgame.scenes;

import com.bernabito.my2dgame.engine.GameCanvas;
import com.bernabito.my2dgame.input.InputData;

import java.awt.*;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public abstract class Scene {

    private Scene nextScene;
    private String sceneName;
    private boolean initialized;
    protected final GameCanvas gameCanvas;

    public Scene(GameCanvas gameCanvas) {
        this(gameCanvas, null, null);
    }

    public Scene(GameCanvas gameCanvas, Scene nextScene) {
        this(gameCanvas, null, nextScene);
    }

    public Scene(GameCanvas gameCanvas, String sceneName, Scene nextScene) {
        this.gameCanvas = Objects.requireNonNull(gameCanvas);
        this.sceneName = sceneName;
        this.nextScene = nextScene;
        initialized = false;
    }

    protected abstract void init() throws SceneInitializationFailedException;

    public abstract void updateState();

    public abstract void render(Graphics2D g);

    public abstract void processInput(InputData inputData);

    public abstract boolean completed();

    public void initialize() throws SceneInitializationFailedException {
        if (!initialized) {
            init();
            initialized = true;
        }
    }

    public boolean hasNextScene() {
        return nextScene != null;
    }

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneName() {
        return (sceneName != null) ? sceneName : "NoName";
    }

}

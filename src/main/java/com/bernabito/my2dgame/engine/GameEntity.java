package com.bernabito.my2dgame.engine;

import java.awt.*;

/**
 * @author Matteo Bernabito
 */

public interface GameEntity {

    void render(Graphics2D g);

    void updateState();

}

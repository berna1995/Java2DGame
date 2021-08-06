package com.bernabito.my2dgame.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Bernabito
 */

public class SpriteSheet {

    private final static Map<String, SpriteSheet> cacheMap = new HashMap<>();

    private final BufferedImage spriteSheetImage;
    private final int tileSize;
    private final int rows;
    private final int columns;

    private SpriteSheet(BufferedImage spriteSheetImage, int tileSize) {
        this.spriteSheetImage = spriteSheetImage;
        this.tileSize = tileSize;
        rows = spriteSheetImage.getHeight() / tileSize;
        columns = spriteSheetImage.getWidth() / tileSize;

    }

    public void render(Graphics g, int row, int column, float x, float y) {
        render(g, row, column, Math.round(x), Math.round(y));
    }

    public void render(Graphics g, int row, int column, int x, int y) {
        int dx2 = x + tileSize;
        int dy2 = y + tileSize;
        int sx1 = column * tileSize;
        int sy1 = row * tileSize;
        int sx2 = sx1 + tileSize;
        int sy2 = sy1 + tileSize;
        g.drawImage(spriteSheetImage, x, y, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public static SpriteSheet loadSpriteSheetFromResources(String resourcePath, int tileSize) {
        SpriteSheet cachedSpriteSheet = cacheMap.get(resourcePath);
        if (cachedSpriteSheet != null && cachedSpriteSheet.getTileSize() == tileSize)
            return cachedSpriteSheet;
        try {
            BufferedImage spriteSheetImage = ImageIO.read(SpriteSheet.class.getResourceAsStream(resourcePath));
            SpriteSheet spriteSheet = new SpriteSheet(spriteSheetImage, tileSize);
            cacheMap.put(resourcePath, spriteSheet);
            return spriteSheet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

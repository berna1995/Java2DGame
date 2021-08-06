package com.bernabito.my2dgame.level.tiles;

import com.bernabito.my2dgame.engine.GameEngine;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.utils.Animation;

import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public final class TileBuilder {

    public static final SpriteSheet TILE_SHEET = Objects.requireNonNull(SpriteSheet.loadSpriteSheetFromResources("/Images/MapSpriteSheet.png", 32));
    // Per rendere la lunghezza delle animazioni indipendente dalla velocità a cui opera il motore
    private static final int TILE_UPDATE_PER_SECOND = 6;
    public static final int ANIMATED_TILE_UDPATE_RATE = GameEngine.TARGET_UPS / TILE_UPDATE_PER_SECOND;

    private TileBuilder() {
    }

    public static Tile createMapTile(int row, int column, int id) {
        int tileSize = TILE_SHEET.getTileSize();
        float tileX = column * tileSize;
        float tileY = row * tileSize;
        switch (id) {
            case 1: // Erba normale
                return new Tile(TILE_SHEET, 0, 0, tileX, tileY);
            case 2: // Erba con fiori
                return new Tile(TILE_SHEET, 0, 1, tileX, tileY);
            case 3: // Acqua piena
                return new CollidableTile(TILE_SHEET, new Animation(12, 3, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 4: // Acqua alto - sinistra
                return new CollidableTile(TILE_SHEET, new Animation(12, 9, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 5: // Acqua alto
                return new CollidableTile(TILE_SHEET, new Animation(12, 6, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 6: // Acqua alto - destra
                return new CollidableTile(TILE_SHEET, new Animation(15, 6, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 7: // Acqua destra
                return new CollidableTile(TILE_SHEET, new Animation(15, 0, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 8: // Acqua basso - destra
                return new CollidableTile(TILE_SHEET, new Animation(13, 6, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 9: // Acqua basso
                return new CollidableTile(TILE_SHEET, new Animation(13, 0, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 10: // Acqua basso - sinistra
                return new CollidableTile(TILE_SHEET, new Animation(14, 6, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 11: // Acqua sinistra
                return new CollidableTile(TILE_SHEET, new Animation(13, 3, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 12: // Angolo lago alto - sinistra
                return new CollidableTile(TILE_SHEET, new Animation(12, 0, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 13: // Angolo lago alto - destra
                return new CollidableTile(TILE_SHEET, new Animation(14, 0, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 14: // Angolo lago basso - destra
                return new CollidableTile(TILE_SHEET, new Animation(15, 3, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 15: // Angolo lago basso - sinistra
                return new CollidableTile(TILE_SHEET, new Animation(14, 3, 3, ANIMATED_TILE_UDPATE_RATE), tileX, tileY);
            case 16: // Vuoto a sinistra
                return new CollidableTile(TILE_SHEET, 1, 8, tileX, tileY);
            case 17: // Vuoto a destra
                return new CollidableTile(TILE_SHEET, 1, 6, tileX, tileY);
            case 18: // Vuoto in basso
                return new CollidableTile(TILE_SHEET, 0, 7, tileX, tileY);
            case 19: // Vuoto in alto
                return new CollidableTile(TILE_SHEET, 2, 7, tileX, tileY);
            case 20: // Vuoto alto - sinistra
                return new CollidableTile(TILE_SHEET, 0, 9, tileX, tileY);
            case 21: // Vuoto alto - destra
                return new CollidableTile(TILE_SHEET, 1, 9, tileX, tileY);
            case 22: // Vuoto basso - destra
                return new CollidableTile(TILE_SHEET, 1, 5, tileX, tileY);
            case 23: // Vuoto basso - sinistra
                return new CollidableTile(TILE_SHEET, 2, 5, tileX, tileY);
            case 24: // Vuoto angolo alto - sinistra
                return new CollidableTile(TILE_SHEET, 0, 6, tileX, tileY);
            case 25: // Vuoto angolo alto - destra
                return new CollidableTile(TILE_SHEET, 0, 8, tileX, tileY);
            case 26: // Vuoto angolo basso - destra
                return new CollidableTile(TILE_SHEET, 2, 8, tileX, tileY);
            case 27: // Vuoto angolo basso - sinistra
                return new CollidableTile(TILE_SHEET, 2, 6, tileX, tileY);
            default:
                return new CollidableTile(TILE_SHEET, 8, 2, tileX, tileY);
        }
    }
}

package com.bernabito.my2dgame.level;

import com.bernabito.my2dgame.engine.GameCanvas;
import com.bernabito.my2dgame.entities.projectiles.Projectile;
import com.bernabito.my2dgame.entities.units.MobileUnit;
import com.bernabito.my2dgame.entities.units.Player;
import com.bernabito.my2dgame.entities.units.Unit;
import com.bernabito.my2dgame.entities.units.enemies.AI;
import com.bernabito.my2dgame.entities.units.enemies.EnemiesBuilder;
import com.bernabito.my2dgame.entities.units.structures.Structure;
import com.bernabito.my2dgame.entities.units.structures.StructureBuilder;
import com.bernabito.my2dgame.graphics.SpriteSheet;
import com.bernabito.my2dgame.input.InputData;
import com.bernabito.my2dgame.level.tiles.CollidableTile;
import com.bernabito.my2dgame.level.tiles.Tile;
import com.bernabito.my2dgame.level.tiles.TileBuilder;
import com.bernabito.my2dgame.scenes.Scene;
import com.bernabito.my2dgame.scenes.SceneInitializationFailedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Matteo Bernabito
 */

public class Level extends Scene {

    private static final SpriteSheet TILE_SHEET = TileBuilder.TILE_SHEET;
    private static final Color BACKGROUND_COLOR = new Color(50, 33, 37);

    private int mapWidth;
    private int mapHeight;
    private int mapWidthPixels;
    private int mapHeightPixels;

    private Tile[] mapTiles;
    private Player player;
    private List<Projectile> projectiles;
    private List<Structure> structures;
    private List<MobileUnit> units;

    private final String resourcePath;
    private Camera camera;

    private boolean waitingForReleasePause;
    private boolean paused;
    private boolean completed;

    public Level(String resourcePath, GameCanvas gameCanvas) {
        super(gameCanvas);
        this.resourcePath = Objects.requireNonNull(resourcePath);

        String[] splittedPath = resourcePath.split("/");
        String levelName = splittedPath[splittedPath.length - 1].split("\\.")[0];
        setSceneName(levelName);
    }

    @Override
    public void init() throws SceneInitializationFailedException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(Level.class.getResourceAsStream(resourcePath));
            Element root = (Element) document.getElementsByTagName("level").item(0);
            if (root.hasAttribute("next"))
                setNextScene(new Level("/Levels/" + root.getAttribute("next"), gameCanvas));
            // Lettura della mappa di gioco, tile di sfondo
            Element map = (Element) root.getElementsByTagName("map").item(0);
            mapWidth = Integer.parseInt(map.getAttribute("width"));
            mapHeight = Integer.parseInt(map.getAttribute("height"));
            mapWidthPixels = mapWidth * TILE_SHEET.getTileSize();
            mapHeightPixels = mapHeight * TILE_SHEET.getTileSize();
            mapTiles = new Tile[mapWidth * mapHeight];
            String tilesIds[] = map.getTextContent().split(",");
            int currentTileIndex = 0;
            for(int i = 0; i < mapHeight; i++) {
                for(int j = 0; j < mapWidth; j++) {
                    int tileId = Integer.parseInt(tilesIds[currentTileIndex].trim());
                    Tile t = TileBuilder.createMapTile(i, j, tileId);
                    mapTiles[currentTileIndex] = t;
                    currentTileIndex++;
                }
            }
            // Lettura delle strutture
            Element objects = (Element) root.getElementsByTagName("structures").item(0);
            NodeList objectsList = objects.getElementsByTagName("structure");
            structures = new ArrayList<>(objectsList.getLength());
            for(int i = 0; i < objectsList.getLength(); i++) {
                Element obj = (Element) objectsList.item(i);
                int id = Integer.parseInt(obj.getAttribute("id"));
                float structureX = Float.parseFloat(obj.getAttribute("x"));
                float structureY = Float.parseFloat(obj.getAttribute("y"));
                structures.add(Objects.requireNonNull(StructureBuilder.buildStructureById(structureX, structureY, id)));
            }
            // Lettura della locazione del player
            Element playerElement = (Element) root.getElementsByTagName("player").item(0);
            float playerX = Float.parseFloat(playerElement.getAttribute("x"));
            float playerY = Float.parseFloat(playerElement.getAttribute("y"));
            player = new Player();
            player.setLocation(playerX, playerY);
            // Lettura dei nemici
            Element enemies = (Element) root.getElementsByTagName("enemies").item(0);
            NodeList enemiesList = enemies.getElementsByTagName("enemy");
            units = new ArrayList<>(enemiesList.getLength());
            for(int i = 0; i < enemiesList.getLength(); i++) {
                Element mob = (Element) enemiesList.item(i);
                int id = Integer.parseInt(mob.getAttribute("id"));
                float mobX = Float.parseFloat(mob.getAttribute("x"));
                float mobY = Float.parseFloat(mob.getAttribute("y"));
                units.add(Objects.requireNonNull(EnemiesBuilder.buildMobById(mobX, mobY, id)));
            }
            // Aggiungo il player alle units
            units.add(player);
            // Preparo struttura per i proiettili
            projectiles = new ArrayList<>();
            // Inizializzo strutture e unità
            for(Structure s : structures)
                s.initialize(this);
            for(Unit u : units)
                u.initialize(this);
            // Camera del gioco
            camera = new Camera(gameCanvas);
            // Altre variabili di livello
            completed = false;
            paused = false;
            waitingForReleasePause = false;
        } catch (Exception e) {
            throw new SceneInitializationFailedException(e);
        }
    }


    @Override
    public void render(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        g.translate(camera.getCameraX(), camera.getCameraY());

        Rectangle2D worldClipRectangle = new Rectangle2D.Float(-camera.getCameraX(), -camera.getCameraY(), gameCanvas.getWidth(), gameCanvas.getHeight());

        for(Tile t : mapTiles)
            if (t.getDrawingRectangle().intersects(worldClipRectangle))
                t.render(g);
        for(Projectile p : projectiles)
            p.render(g);
        for(Unit u : units)
            u.render(g);
        for(Structure s : structures)
            s.render(g);

        g.translate(-camera.getCameraX(), -camera.getCameraY());
    }


    @Override
    public void updateState() {
        if (!paused) {
            for(int i = 0; i < mapTiles.length; i++)
                mapTiles[i].updateState();
            for(int i = 0; i < projectiles.size(); i++)
                projectiles.get(i).updateState();
            for(int i = 0; i < structures.size(); i++)
                structures.get(i).updateState();
            for(int i = 0; i < units.size(); i++) {
                Unit u = units.get(i);
                if (u instanceof AI)
                    ((AI) u).playAIStep();
                u.updateState();
            }
            camera.trackPlayer(player);
            if (units.size() <= 1)
                markAsCompleted();
        }
    }

    @Override
    public boolean completed() {
        return completed;
    }

    @Override
    public void processInput(InputData inputData) {
        player.setSpeedRatio(inputData.getSpeedRatioX(), inputData.getSpeedRatioY());
        if (inputData.isAttackButtonPressed()) {
            player.requestAttack(inputData.getAttackAngle());
        }
        if (inputData.isPauseButtonPressed()) {
            waitingForReleasePause = true;
        } else {
            if (waitingForReleasePause) {
                paused = !paused;
                waitingForReleasePause = false;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<MobileUnit> getUnits() {
        return units;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public List<CollidableTile> getCollidableTiles(Rectangle2D.Float hitbox, List<CollidableTile> tiles) {
        int left = Math.round(hitbox.x);
        int right = Math.round(hitbox.x + hitbox.width);
        int top = Math.round(hitbox.y);
        int bottom = Math.round(hitbox.y + hitbox.height);
        int iLeft = left / TILE_SHEET.getTileSize();
        int iRight = right / TILE_SHEET.getTileSize();
        int iTop = top / TILE_SHEET.getTileSize();
        int iBottom = bottom / TILE_SHEET.getTileSize();

        for(int i = iTop; i <= iBottom; i++) {
            for(int j = iLeft; j <= iRight; j++) {
                Tile t = mapTiles[(i * mapWidth) + j];
                if (t instanceof CollidableTile)
                    tiles.add((CollidableTile) t);
            }
        }

        return tiles;
    }

    public boolean isOutOfMap(Rectangle2D hitbox) {
        double left = hitbox.getX();
        double right = left + hitbox.getWidth();
        double top = hitbox.getY();
        double bottom = top + hitbox.getHeight();
        return (right < 0) || (left > mapWidthPixels) || (top < 0) || (bottom > mapHeightPixels);
    }

    public void markAsCompleted() {
        completed = true;
    }

}

package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Battles.Battle;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.MovingEntities.*;
import dungeonmania.util.Position;

public class GameMap {
    // Map Variables: **************
    private Map<Position, List<Entity>> dungeonMap;
    private Position entryLocation;
    private String dungeonName;
    private String mapId;
    private Player player;
    private Battle battle;
    private JsonObject jsonMap;
    private int width;
    private int height;
    private int gameIndex = 0;


    // Game State: **************
    private GameState gameState;
    
    /**
     * This constructor used for establishing new games
     * @param difficulty (String)
     * @param dungeonName (String)
     * @param jsonMap (Map as JsonObject)
     */
    public GameMap(String difficulty, String name, JsonObject jsonMap) {
        this.jsonMap = jsonMap;
        this.dungeonName = name;
        this.gameState = MapUtility.createGameState(difficulty);
        this.setMapId();
        this.setGameIndex(jsonMap);
        this.initialiseGameMapFromJSON(jsonMap);
        this.battle = new Battle(difficulty);
        this.player.setBattle(battle);
        this.setObservers();
    }

    /**
     * This constructor used for loading saved games.
     * @param name (String)
     */
    public GameMap(String name, String mapId) {
        this(MapUtility.getSavedMap(name, mapId).get("game-mode").getAsString(), 
            MapUtility.getSavedMap(name, mapId).get("map-name").getAsString(), MapUtility.getSavedMap(name, mapId));
        // Set the mapID
        this.mapId = mapId;
    }

    // ********************************************************************************************\\
    //                                       Initialise Game                                       \\
    // ********************************************************************************************\\

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return Map<Position, List<Entity>> form of a map corresponding to jsonMap
     */
    public void initialiseGameMapFromJSON(JsonObject jsonMap) {
        // Initialise the map:
        this.width = jsonMap.get("width").getAsInt();
        this.height = jsonMap.get("height").getAsInt();
        this.dungeonMap = MapUtility.createInitialisedMap(width, height);

        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            // Get all attributes:
            JsonObject obj = entity.getAsJsonObject();
            // Create the entity object, by factory method
            Position pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            Entity temp = EntityFactory.getEntityObject(i.toString(), pos, obj, this);
            dungeonMap.get(temp.getPos()).add(temp);
            i++;
        }

    }


    // ********************************************************************************************\\
    //                                Accessing Entities on Map                                    \\
    // ********************************************************************************************\\

    /**
     * Returns a list of all self moving entities from the game map.
     * @return List<MovingEntity> List of moving entities on map.
     */
    public List<Bomb> getBombList() {
        List<Bomb> bombList = new ArrayList<>();
        for (Entity e : getAllEntity()) {
            if (e instanceof Bomb) {
                bombList.add((Bomb) e);
            }
        }
        return bombList;
    }

    /**
     * Returns a list of all self moving entities from the game map.
     * @return List<MovingEntity> List of moving entities on map.
     */
    public List<MovingEntity> getMovingEntityList() {
        List<String> movingType = Arrays.asList("mercenary", "spider", "zombie_toast", "older_player", "assassin", "hydra");
        List<MovingEntity> entityList = new ArrayList<>();
        for (Entity e : getAllEntity()) {
            if (movingType.contains(e.getType())) { entityList.add((MovingEntity) e); }
        }
        return entityList;
    }

    /**
     * Returns a list of all entities on the map.
     * @return List<Entity> List of entity on the map.
     */
    public List<Entity> getAllEntity() {
        List<Entity> entityList = new ArrayList<>();
        // Loop through the map entities to check for moving entity
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) { entityList.add(e); }
        }
        return entityList;
    }

    /**
     * Given a position, return a list of all entities at that position.
     * @param pos
     * @return List<Entity> list of all entities at a position.
     */
    public List<Entity> getEntityPositionList(Position pos) {
        List<Entity> eList = new ArrayList<>();
        // Loop to add all positions at a position
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            if (entry.getKey().equals(pos)) { eList.addAll(entry.getValue()); }
        }
        return eList;
    }

    /**
     * Given the id of an entity, search the map and return the
     * entity with the respective id.
     * @param id (String)
     * @return Entity with given id (String).
     */
    public Entity getEntityOnMap(String id) {
        for (Entity e : getAllEntity()) {
            if (e.hasId(id)) { return e; }
        }
        return null;
    }

    /**
     * Given the map and the type of entity you want, returns a list of 
     * entities of that type on the map.
     * @param map
     * @param eType
     * @return
     */
    public List<Entity> getEntityTypeList(String eType) {
        List<Entity> eList = new ArrayList<>();
        for (Entity e : getAllEntity()) {
            if (e.isType(eType)) { eList.add(e); }
        }
        return eList;
    }

    // ********************************************************************************************\\
    //                                   Getter and setters:                                       \\
    // ********************************************************************************************\\

    public Player getPlayer() {
        return this.player; 
    }

    public void setMapId() {
        if (mapId == null) { mapId = "" + System.currentTimeMillis(); }
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.entryLocation = player.getPos(); 
    }

    public Position getEntryPos() {
        return entryLocation;
    }

    public Map<Position, List<Entity>> getMap() {
        return this.dungeonMap;
    }

    public String getMapId() {
        return mapId;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    public void setObservers() {
        for (MovingEntity e : getMovingEntityList()) {
            player.registerObserver(e);
        }
        player.registerObserver(player);
    }

    public Battle getBattle() {
        return battle;
    }
    
    public Integer getGameIndex() {
        return gameIndex;
    }

    // SHould be in response:
    public void setGameIndex(JsonObject obj) {
        this.gameIndex = (obj.get("game-index") == null) ? 
            0 : obj.get("game-index").getAsInt();
    }

    public void incrementGameIndex() {
        this.gameIndex = gameIndex + 1;
    }

    public JsonObject getJsonMap() {
        return jsonMap;
    }

    public Position getOlderPlayerPosition() {
        return getEntityTypeList("older_player").get(0).getPos();
    }
}

package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.*;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Battles.Battle;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Position;

public class GameMap {
    // Map Variables: **************
    private Map<Position, List<Entity>> dungeonMap;
    private Position entryLocation;
    private String dungeonName;
    private String mapId = null;
    private Player player;
    private Battle battle;
    private JsonObject jsonMap;
    private int width;
    private int height;
    private int gameIndex = 0;

    // Game State: **************
    private GameState gameState;

    // Seed counter used for spider
    int seed;
    int period;
    
    /**
     * This constructor used for establishing new games
     * @param difficulty (String)
     * @param dungeonName (String)
     * @param jsonMap (Map as JsonObject)
     */
    public GameMap(String difficulty, String name, JsonObject jsonMap) {
        this.jsonMap = jsonMap;
        this.dungeonName = name;
        this.setMapId();
        this.setGameIndex(jsonMap);
        this.initialiseGameMapFromJSON(jsonMap);
        this.battle = new Battle(difficulty);
        this.player.setBattle(battle);
        this.setPlayerInventory(jsonMap);
        this.setObservers();
        this.gameState = MapUtility.createGameState(difficulty);
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
    //                                    Map and Json Functions                                   \\
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
        // Swamp check if any entity is on the swamp at the start
        for (Entity e : getEntityTypeList("swamp_tile")) {
            ((SwampTile) e).checkTile(getEntityPositionList(e.getPos()));
        }
    }


    // ********************************************************************************************\\
    //                                Accessing Entities on Map                                    \\
    // ********************************************************************************************\\

    /**
     * Returns a list of all self moving entities from the game map.
     * @return List<MovingEntity> List of moving entities on map.
     */
    public List<MovingEntity> getMovingEntityList() {
        List<String> movingType = Arrays.asList("mercenary", "spider", "zombie_toast");
        List<MovingEntity> entityList = new ArrayList<>();
        // Loop through the map entities to check for moving entity
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (movingType.contains(e.getType())) {
                    entityList.add((MovingEntity) e);
                }
            }
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
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (e.hasId(id)) { return e; }
            }
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
        // Loop through to the entity
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (e.isType(eType)) { eList.add(e); }
            }
        }
        return eList;
    }

    // ********************************************************************************************\\
    //                                  Mob Spawning Functions                                     \\
    // ********************************************************************************************\\
    
    /**
     * Spawn all respective mobs.
     */
    public void spawnMob() {
        spawnSpider();
        spawnMercenary();
        period++;
    }

    /**
     * Spawns a spider on the map with a one in ten chance (with
     * restrictions).
     */
    public void spawnSpider() {
        int spiders = 0;
        for (MovingEntity e : getMovingEntityList()) {
            if (e.isType("spider")) { spiders++; }
        }
        // Square too small:
        if(width < 2 || height < 2) { return; }
        // Check conditions
        Random random = new Random(seed);
        if (random.nextInt(10) == 5 && spiders < 5) {
            // Random x and y positions
            int xPos = new Random(seed + 37).nextInt(width - 2) + 1;
            int yPos = new Random(seed + 68).nextInt(height - 2) + 1;
            // Create the spider:
            Position newSpider = new Position(xPos, yPos, 3);
            Position checkAbove = new Position(xPos, yPos - 1, 3);
            Spider spider = new Spider("spider" + System.currentTimeMillis(), "spider", newSpider);
            // Check if current and above positions of the spiders are boulders:
            if (spider.canPass(dungeonMap, newSpider) && spider.canPass(dungeonMap, checkAbove)) {
                dungeonMap.get(newSpider).add(spider);
                player.registerObserver(spider);
            }
        }
        seed += 124;
    }

    /**
     * Periodically spawns a mecenary at the entry location.
     */
    public void spawnMercenary() {
        Mercenary newMerc = new Mercenary("merc" + System.currentTimeMillis(), "mercenary", entryLocation);
        // Check conditions to spawn mercenary
        if (period != 0 && period % 15 == 0) {
            if (newMerc.canPass(dungeonMap, entryLocation)) {
                dungeonMap.get(entryLocation).add(newMerc);
                player.registerObserver(newMerc);
            } else {
                period--;
            }
        }
    }


    // ********************************************************************************************\\
    //                                     Other Functions                                         \\
    // ********************************************************************************************\\

    // This function should be in player.
    /**
     * Given the jsonMap object, get all player inventory items
     * and set it to the player.
     * @param jsonMap (JsonObject)
     */
    public void setPlayerInventory(JsonObject jsonMap) {
        // Case when the player does not exist.
        if (jsonMap.getAsJsonArray("inventory") == null) {
            return;
        }
        // Look at the inventory field in json file.
        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("inventory")) {
            JsonObject obj = entity.getAsJsonObject();
            Entity collectable = EntityFactory.getEntityObject("inventItem" + i, new Position(0, 0), obj, this);
            player.getInventory().put(collectable, player);
            i++;
        }
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
}

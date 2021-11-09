package dungeonmania.gamemap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.*;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Battles.Battle;
import dungeonmania.Goals.*;
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
    private int gameIndex = 0;;

    // Game State: **************
    private GameState gameState;

    // Map Goals: *****************
    private GoalInterface rootGoal;

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
        this.dungeonName = name;
        this.setMapId();
        this.setGameIndex(jsonMap);
        this.setGameMapFromJSON(jsonMap);
        this.battle = new Battle(difficulty);
        this.player.setBattle(battle);
        this.setPlayerInventory(jsonMap);
        this.setObservers();
        this.gameState = MapHelper.createGameState(difficulty);
        this.rootGoal = GoalHelper.getGoalPattern(jsonMap);
        this.jsonMap = jsonMap;
    }

    /**
     * This constructor used for loading saved games.
     * @param name (String)
     */
    public GameMap(String name, String mapId) {
        this(MapHelper.getSavedMap(name, mapId).get("game-mode").getAsString(), 
            MapHelper.getSavedMap(name, mapId).get("map-name").getAsString(), MapHelper.getSavedMap(name, mapId));
        // Set the mapID
        this.mapId = mapId;
    }


    // ********************************************************************************************\\
    //                          Dungeon Response Arguments Helper Function                         \\
    // ********************************************************************************************\\


    // ********************************************************************************************\\
    //                                    Map and Json Functions                                   \\
    // ********************************************************************************************\\

    /**
     * Given the name of the map converts the current game map 
     * into a json file and saves it in the designated folder.
     * @param name (String)
     */
    public void saveMapAsJson(String name) {
        try {  
            // Writes the json file into the folder
            FileWriter file = new FileWriter("saved_games/" + name + ".json");
            file.write(mapToJson().toString(4));
            file.flush();
            file.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }
    
    /**
     * Given the name of the map converts the current game map 
     * into a json file and saves it in the designated folder.
     * @param name (String)
     */
    public void saveTickInstance(String name) {
        try {  
            // Writes the json file into the folder
            FileWriter file = new FileWriter("time_travel_record/" + mapId + "/" + name + ".json");
            file.write(mapToJson().toString(4));
            file.flush();
            file.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }

    

    /**
     * Takes the current map of the game and converts it to 
     * a json object.
     * @return JsonObject of the current state of the map.
     */
    public JSONObject mapToJson() {
        // Main object for file
        JSONObject main = new JSONObject();
        // Add all fields:
        main.put("width", getMapWidth());
        main.put("height", getMapHeight());
        main.put("game-mode", gameState.getMode());
        main.put("map-name", dungeonName);
        main.put("map-id", mapId);
        main.put("game-index", gameIndex);
        main.put("goal-condition", GoalHelper.goalPatternToJson(getRootGoal()));
        main.put("inventory", player.getInventory().toJSON());
        main.put("entities", MapHelper.entitiesToJson(dungeonMap));
        return main;
    }

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return Map<Position, List<Entity>> form of a map corresponding to jsonMap
     */
    public void setGameMapFromJSON(JsonObject jsonMap) {
        // Initialise the map:
        this.width = jsonMap.get("width").getAsInt();
        this.height = jsonMap.get("height").getAsInt();
        dungeonMap = MapHelper.createInitialisedMap(width, height);
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
        swampTileCheck();
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

    public void swampTileCheck() {
        // Loop through all swamp_tile entites
        for (Entity e : MapHelper.getEntityTypeList(dungeonMap, "swamp_tile")) {
            ((SwampTile) e).checkTile(getEntityPositionList(e.getPos()));
        }
    }

    /**
     * Checks if the entity is on top of a swamp tile.
     * @param gameMap
     * @return True is the entity is on a swamp tile, false otherwise.
     */
    public boolean isOnSwampTile(String id) {
        if (id == null) { id = player.getId(); } 
        for (Entity e : MapHelper.getEntityTypeList(dungeonMap, "swamp_tile")) {
            if (((SwampTile) e).entityOnTile(id)) { return true; }
        }
        return false;
    }

    // Swamp tile tick:
    public void swampTick() {
        for (Entity e : MapHelper.getEntityTypeList(dungeonMap, "swamp_tile")) {
            ((SwampTile) e).tickCount();
        }
    }

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

    public int getMapHeight() {
        return this.height;
    }

    public int getMapWidth() {
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

    public void setGameIndex(JsonObject obj) {
        this.gameIndex = (obj.get("game-index") == null) ? 
            0 : obj.get("game-index").getAsInt();
    }

    public void incrementGameIndex() {
        this.gameIndex = gameIndex + 1;
    }

    public GoalInterface getRootGoal() {
        return rootGoal;
    }

    public JsonObject getJsonMap() {
        return jsonMap;
    }
}

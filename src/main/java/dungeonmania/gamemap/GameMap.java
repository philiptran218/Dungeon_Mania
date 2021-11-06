package dungeonmania.gamemap;

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
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Map Variables: **************
    private Map<Position, List<Entity>> dungeonMap;
    private Position entryLocation;
    private String dungeonName;
    private String mapId;
    private Player player;
    private Battle battle;
    private int width;
    private int height;

    // Map Goals: *****************
    private GoalInterface rootGoal;

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
        this.dungeonName = name;
        this.mapId = "" + System.currentTimeMillis();
        this.dungeonMap = jsonToMap(jsonMap);
        this.battle = new Battle(difficulty);
        this.player.setBattle(battle);
        this.setPlayerInventory(jsonMap);
        this.setObservers();
        this.gameState = MapHelper.createGameState(difficulty);
        this.rootGoal = GoalHelper.getGoalPattern(jsonMap);
    }

    /**
     * This constructor used for loading saved games.
     * @param name (String)
     */
    public GameMap(String name) {
        this(MapHelper.getSavedMap(name).get("game-mode").getAsString(), 
            MapHelper.getSavedMap(name).get("map-name").getAsString(), MapHelper.getSavedMap(name));
    }


    // ********************************************************************************************\\
    //                          Dungeon Response Arguments Helper Function                         \\
    // ********************************************************************************************\\

    /**
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponse() {
        return new DungeonResponse(getMapId(), getDungeonName(), mapToListEntityResponse(), 
            player.getInventoryResponse(), getBuildables(), getGoals());
    }

    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List<EntityResponse> List of entity response.
     */
    public List<EntityResponse> mapToListEntityResponse() {
        // EntityResponse list to help append entities on the map
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();
        // Loops through entities on the map entities on the map
        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                // Checks if the the entity is a mecenary or toast_spawner to 
                boolean isInteractable = (e.getType().equals("mercenary") || e.getType().equals("zombie_toast_spawner"));
                if (e.getType().equals("mercenary") && ((Mercenary) e).isAlly()){
                    isInteractable = false;
                }
                // Add the entity to the map
                entityList.add(new EntityResponse(e.getId(), e.getType(), e.getPos(), isInteractable));
            }
        }
        return entityList;
    }

    /**
     * Looks through the player's inventory and checks if the
     * player has enough materials to build a bow or a shield.
     * @return List<String> List of buildable items.
     */
    public List<String> getBuildables() {
        List<String> buildable = new ArrayList<>();
        int numWood = player.getInventory().getNoItemType("wood");
        int numArrow = player.getInventory().getNoItemType("arrow");
        boolean hasKey = player.hasItem("key");
        boolean hasTreasure = player.hasItem("treasure");

        // Checks if sufficient materials
        if (numWood > 0 && numArrow > 2) {
            buildable.add("bow");
        }
        if (numWood > 1 && (hasKey || hasTreasure)) {
            buildable.add("shield");
        }
        return buildable;
    }

    /**
     * Get the goals that needs to be completed for the map.
     * @return String of goals that needs to be completed.
     */
    public String getGoals() {
        return GoalHelper.goalPatternToString(this.getRootGoal(), this.getMap());
    }


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
        main.put("goal-condition", GoalHelper.goalPatternToJson(getRootGoal()));
        main.put("inventory", player.getInventory().toJSON());
        main.put("entities", MapHelper.mapToJSON(dungeonMap));
        return main;
    }

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return Map<Position, List<Entity>> form of a map corresponding to jsonMap
     */
    public Map<Position, List<Entity>> jsonToMap(JsonObject jsonMap) {
        // Initialise the map:
        this.width = jsonMap.get("width").getAsInt();
        this.height = jsonMap.get("height").getAsInt();
        Map<Position, List<Entity>> newMap = MapHelper.createInitialisedMap(width, height);
        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            // Get all attributes:
            JsonObject obj = entity.getAsJsonObject();
            // Create the entity object, by factory method
            Position pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            Entity temp = EntityFactory.getEntityObject(i.toString(), pos, obj);
            // Set player on the map
            if (temp.isType("player")) {
                this.player = (Player) temp;
                this.entryLocation = temp.getPos();
            }
            newMap.get(temp.getPos()).add(temp);
            i++;
        }
        return newMap;
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
            Spider spider = new Spider("" + System.currentTimeMillis(), "spider", newSpider);
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
        if (period != 0 && period % 15 == 0) {
            Mercenary newMerc = new Mercenary("" + System.currentTimeMillis(), "mercenary", entryLocation);
            dungeonMap.get(entryLocation).add(newMerc);
            player.registerObserver(newMerc);
        }
    }

    // ********************************************************************************************\\
    //                                     Other Functions                                         \\
    // ********************************************************************************************\\

    public void swampTileCheck() {
        // Loop through all swamp_tile entites
        for (Entity e : getEntityTypeList("swamp_tile")) {
            ((SwampTile) e).checkTile(getEntityPositionList(e.getPos()));
        }
    }

    /**
     * Checks if the entity is on top of a swamp tile.
     * @param gameMap
     * @return True is the entity is on a swamp tile, false otherwise.
     */
    public boolean isOnSwampTile(Entity entity) {
        for (Entity e : getEntityTypeList("swamp_tile")) {
            if (((SwampTile) e).entityOnTile(entity)) { return true; }
        }
        return false;
    }

    // Swamp tile tick:
    public void swampTick() {
        for (Entity e : getEntityTypeList("swamp_tile")) {
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
            Entity collectable = EntityFactory.getEntityObject("inventItem" + i, new Position(0, 0), obj);
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
    }

    public GoalInterface getRootGoal() {
        return rootGoal;
    }

    public Battle getBattle() {
        return battle;
    }
    
}

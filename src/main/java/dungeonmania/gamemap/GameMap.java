package dungeonmania.gamemap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Battles.Battle;
import dungeonmania.CollectableEntities.*;
import dungeonmania.Goals.*;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.StaticEntities.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Map Variables: **************
    private Map<Position, List<Entity>> dungeonMap;
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

    /**
     * This constructor used for establishing new games
     * @param difficulty (String)
     * @param dungeonName (String)
     * @param jsonMap (Map as JsonObject)
     */
    public GameMap(String difficulty, String name, JsonObject jsonMap) {
        this.dungeonName = name;
        this.mapId = "" + System.currentTimeMillis();
        this.battle = new Battle(difficulty);
        this.dungeonMap = jsonToMap(jsonMap);
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
            FileWriter file = new FileWriter("src/main/resources/saved_games/" + name + ".json");
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
        JSONArray entities = new JSONArray();

        // Add all fields:
        main.put("width", getMapWidth());
        main.put("height", getMapHeight());
        main.put("game-mode", this.gameState.getMode());
        main.put("map-name", this.dungeonName);
        main.put("goal-condition", GoalHelper.goalPatternToJson(this.getRootGoal()));

        JSONArray inventory = new JSONArray();
        // Add all inventory items
        for (CollectableEntity e : player.getInventoryList()) {
            JSONObject c = new JSONObject();
            c.put("type", e.getType());
            if (e.getType().equals("key")) {
                c.put("key", ((Key) e).getKeyId());
            }
            inventory.put(c);
        }   
        main.put("inventory", inventory);

        // Add all entities on the map
        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                JSONObject temp = new JSONObject();
                temp.put("x", entry.getKey().getX());
                temp.put("y", entry.getKey().getY());

                if (e instanceof Portal) {
                    temp.put("colour", ((Portal) e).getPortalColour());
                    temp.put("type", "portal");
                } else {
                    temp.put("type", e.getType());
                }

                if (e.getType().equals("key")) {
                    temp.put("key", ((Key) e).getKeyId());
                } else if (e.getType().equals("door")) {
                    temp.put("key", ((Door) e).getKeyId());
                }
                entities.put(temp);
            }
        }
        main.put("entities", entities);
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
            String type = obj.get("type").getAsString();
            Position pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            // Create the entity object, by factory method
            Entity temp = EntityFactory.getEntityObject(i.toString(), type, pos, obj.get("key"), obj.get("colour"), this.battle);
            // Set player on the map
            if (type.equals("player")) {
                this.player = (Player) temp;
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
    
    /**
     * Given the id of an entity, search the map and return the
     * entity with the respective id.
     * @param id (String)
     * @return Entity with given id (String).
     */
    public Entity getEntityOnMap(String id) {
        for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (e.getId().equals(id)) {
                    return e;
                }
            }
        }
        return null;
    }

    // ********************************************************************************************\\
    //                                     OTHER FUNCTIONS                                         \\
    // ********************************************************************************************\\

    /**
     * Spawns a spider on the map with a one in ten chance (with
     * restrictions).
     */
    public void spawnSpider() {
        int spiders = 0;
        for (MovingEntity e : getMovingEntityList()) {
            if (e.getType().equals("spider")) {
                spiders++;
            }
        }
        // Square too small:
        if(width < 2 || height < 2) {
            return;
        }
        // Check conditions
        Random random = new Random();
        if (random.nextInt(20) == 4 && spiders < 5) {
            Random x = new Random();
            Random y = new Random();
            // New x and y positions
            int xPos = x.nextInt(width - 2) + 1;
            int yPos = y.nextInt(height - 2) + 1;
            // Loop through to check restrictions
            for (Map.Entry<Position, List<Entity>> entry : dungeonMap.entrySet()) {
                for (Entity e : entry.getValue()) {
                    boolean currSquare = ((e.getPos().getX() == xPos) && (e.getPos().getY() == yPos));
                    boolean checkAbove = ((e.getPos().getX() == xPos) && (e.getPos().getY() == yPos - 1));
                    if (e.getType().equals("player") && currSquare || 
                        (e.getType().equals("boulder") && (currSquare || checkAbove))) {
                        return;
                    }
                }
            }
            // Create the spider:
            Position newSpider = new Position(xPos, yPos, 3);
            Spider spider = new Spider("" + System.currentTimeMillis(), "spider", newSpider);
            dungeonMap.get(newSpider).add(spider);
            player.registerObserver(spider);
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
        for (JsonElement entity : jsonMap.getAsJsonArray("inventory")) {
            JsonObject obj = entity.getAsJsonObject();
            String type = obj.get("type").getAsString();
            Position pos = new Position(0, 0, -1);
            Entity collectable = EntityFactory.getEntityObject("" + System.currentTimeMillis(), type, pos, obj.get("key"), null, this.battle);
            player.getInventory().put(collectable, player);
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

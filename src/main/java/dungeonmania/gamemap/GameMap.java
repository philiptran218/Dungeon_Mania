package dungeonmania.gamemap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Inventory;
import dungeonmania.Battles.Battle;
import dungeonmania.CollectableEntities.*;
import dungeonmania.Goals.*;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.StaticEntities.*;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Need to figure something to stand in for entity, or we might need 
    // multiple maps for one dungeon.
    private Map<Position, List<Entity>> dungeonMap;
    private GoalInterface rootGoal;
    private String dungeonName;
    private String mapId;
    private Player player;
    private Battle battle;
    private GameState gameState;
    private int width;
    private int height;



    // ******************************************
    // Need to make varibales to game state here:
    // GameState currState;
    // ******************************************

    /**
     * This constructor used for establishing new games
     * @param difficulty
     * @param dungeonName
     * @param jsonMap
     */
    public GameMap(String difficulty, String name, JsonObject jsonMap) {
        this.dungeonName = name;
        this.mapId = "" + System.currentTimeMillis();
        this.battle = new Battle(difficulty);
        this.dungeonMap = jsonToMap(jsonMap);
        this.rootGoal = GoalHelper.getGoalPattern(jsonMap);
        this.setPlayerInventory(jsonMap);
        this.setObservers();
        this.setGameState(difficulty);
    }

    /**
     * This constructor used for loading saved games.
     * @param map
     */
    public GameMap(String name) {
        this(getSavedMap(name).get("game-mode").getAsString(), getSavedMap(name).get("map-name").getAsString(), getSavedMap(name));
    }

    /**
     * Given difficulty of the name as a string, set the state of 
     * the game respectively.
     * @param difficulty (String)
     */
    public void setGameState(String difficulty) {
        if (difficulty.equals("Peaceful")) {
            this.gameState = new PeacefulState();
        } else if (difficulty.equals("Standard")) {
            this.gameState = new StandardState();
        } else {
            this.gameState = new HardState();
        }
    }
    
    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List of Entity Response
     */
    public List<EntityResponse> mapToListEntityResponse() {
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();

        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                boolean isInteractable = (e.getType().equals("mercenary") || e.getType().equals("zombie_toast_spawner"));
                if (e.getType().equals("mercenary") && ((Mercenary) e).isAlly()){
                    isInteractable = false;
                }
                entityList.add(new EntityResponse(e.getId(), e.getType(), e.getPos(), isInteractable));
            }
        }
        return entityList;
    }
    
    /**
     * Converts the player into item response.
     * @return List of items as a list of item response.
     */
    public List<ItemResponse> inventoryToItemResponse() {
        List<ItemResponse> itemResponse = new ArrayList<>();
        Inventory i = player.getInventory();
        for (CollectableEntity c : i.getInventory()) {
            itemResponse.add(new ItemResponse(c.getId(), c.getType()));
        }
        return itemResponse;
    }

    /**
     * Converts the current game map into a json file and saves it 
     * in the designated folder. Also If there is no game difficulty
     * add a field in the json file for game difficulty.
     */
    public void saveMapAsJson(String name) {
        try {  
            FileWriter file = new FileWriter("src/main/resources/saved_games/" + name + ".json");
            file.write(mapToJson().toString(4));
            file.flush();
            file.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

    /**
     * Given the name of a saved file, attempts to look for the game
     * and return it as a JsonObject
     * @param Name of saved game.
     * @return JsonObject file of the saved game.
     */
    public static JsonObject getSavedMap(String name) {
        try {
            return JsonParser.parseReader(new FileReader("src\\main\\resources\\saved_games\\" + name + ".json")).getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("File not found.");
        }
    }

    /**
     * Takes the current map of this function and converts it to 
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
        for (CollectableEntity e : player.getInventoryList()) {
            JSONObject c = new JSONObject();
            c.put("type", e.getType());
            if (e.getType().equals("key")) {
                c.put("key", ((Key) e).getKeyId());
            }
            inventory.put(c);
        }   
        main.put("inventory", inventory);

        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            Position p = entry.getKey();
            for (Entity e : entry.getValue()) {
                JSONObject temp = new JSONObject();
                temp.put("x", p.getX());
                temp.put("y", p.getY());
                temp.put("type", e.getType());
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
     * Initialises the map given the width and the length with empty lists.
     */
    public Map<Position, List<Entity>> createInitialisedMap(int width, int height) {
        this.width = width;
        this.height = height;
        Map<Position, List<Entity>> map = new HashMap<>();
        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < width; i++) { // width
                for (int j = 0; j < height; j++) { // height
                    map.put(new Position(i, j, k), new ArrayList<Entity>());
                }
            }
        }
        return map;
    }

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return Map<Position, List<Entity>> form of a map corresponding to jsonMap
     */
    public Map<Position, List<Entity>> jsonToMap(JsonObject jsonMap) {
        // Initialise the map:
        Map<Position, List<Entity>> newMap = createInitialisedMap(jsonMap.get("width").getAsInt(), jsonMap.get("height").getAsInt());
        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            // Get all attributes:
            JsonObject obj = entity.getAsJsonObject();
            String type = obj.get("type").getAsString();
            Position pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());

            // Create the entity object, by factory method:
            Entity temp = EntityFactory.getEntityObject(i.toString(), type, pos, obj.get("key"), this.battle);
            // Set player:
            if (type.equals("player")) {
                this.player = (Player) temp;
            }
            newMap.get(temp.getPos()).add(temp);
            i++;
        }
        return newMap;
    }

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
            Entity collectable = EntityFactory.getEntityObject("" + System.currentTimeMillis(), type, pos, obj.get("key"), this.battle);
            player.getInventory().put(collectable, player);
        }
    }

    /**
     * Returns a list of all self moving entities:
     * @return Entity list of all self-moving entities.
     */
    public List<MovingEntity> getMovingEntityList() {
        List<String> movingType = Arrays.asList("mercenary", "spider", "zombie_toast");
        List<MovingEntity> entityList = new ArrayList<>();
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

    /**
     * Spawns a spider on the map with a one in ten chance.
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
        if (random.nextInt(10) == 4 && spiders < 5) {
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
            dungeonMap.get(newSpider).add(new Spider("" + System.currentTimeMillis(), "spider", newSpider));
        }
        
    }

    // Getter and setters:
    public Player getPlayer() {
        return this.player;
    }

    public Map<Position, List<Entity>> getMap() {
        return this.dungeonMap;
    }

    public String getMapId() {
        return mapId;
    }

    public String getGoals() {
        return GoalHelper.goalPatternToString(this.getRootGoal(), this.getMap());
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
    public static void main(String[] args) {
        Random r = new Random();
        System.out.println(r.nextInt(2));
    }
}

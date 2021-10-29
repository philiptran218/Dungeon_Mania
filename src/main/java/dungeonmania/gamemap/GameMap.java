package dungeonmania.gamemap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Inventory;
import dungeonmania.CollectableEntities.*;
import dungeonmania.Goals.*;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.MovingEntityObserver;
import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.*;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Need to figure something to stand in for entity, or we might need 
    // multiple maps for one dungeon.
    private Map<Position, List<Entity>> dungeonMap;
    private String gameDifficulty;
    private String goal;
    private String dungeonName;
    private Player player;
    private String mapId;
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
        this.gameDifficulty = difficulty;
        this.dungeonMap = jsonToMap(jsonMap);
        this.mapId = "" + System.currentTimeMillis();
        this.dungeonName = name;
        this.setObservers();
    }

    /**
     * This constructor used for loading saved games.
     * @param map
     */
    public GameMap(String name) {
        this(getSavedMap(name).get("game-mode").getAsString(), getSavedMap(name).get("map-name").getAsString(), getSavedMap(name));
    }

    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List of Entity Response
     */
    public List<EntityResponse> mapToListEntityResponse() {
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();

        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            // For each position add the entity to the response list:
            // First check if it is one element:
            if (entry.getValue().size() == 1) {
                Entity e = entry.getValue().get(0);
                entityList.add(new EntityResponse(e.getId(), e.getType(), e.getPos(), false));
            } 
            // Check for stacking:
            if (entry.getValue().size() > 1) {
                int layer = 0;
                for (Entity e : entry.getValue()) {
                    int x = e.getPos().getX();
                    int y = e.getPos().getY();
                    entityList.add(new EntityResponse(e.getId(), e.getType(), new Position(x, y, layer), false));
                    layer++;
                }
            }
        }
        return entityList;
    }
    
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
        main.put("game-mode", this.gameDifficulty);
        main.put("map-name", this.dungeonName);
        main.put("goal-condition", this.getGoal());

        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            Position p = entry.getKey();
            if (entry.getValue().size() == 1) {
                Entity e = entry.getValue().get(0);
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
        for (int k = 0; k < 4; k++) {
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
        // Add goals to the map:
        this.goal = jsonMap.get("goal-condition").toString();
        // Initialise the map:
        Map<Position, List<Entity>> newMap = createInitialisedMap(jsonMap.get("width").getAsInt(), jsonMap.get("height").getAsInt());

        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            // Get all attributes:
            JsonObject obj = entity.getAsJsonObject();
            String type = obj.get("type").getAsString();
            Position pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());

            // Create the entity object, by factory method:
            Entity temp = EntityFactory.getEntityObject(i.toString(), type, pos, obj.get("key"));
            // Set player:
            if (type.equals("player")) {
                this.player = (Player) temp;
            }
            newMap.get(temp.getPos()).add(temp);
            i++;
        }
        return newMap;
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

    public String getGoal() {
        return goal;
    }

    public int getMapHeight() {
        return this.height;
    }

    public int getMapWidth() {
        return this.width;
    }

    public String getDifficulty() {
        return this.gameDifficulty;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    /**
     * Convert JsonObject containing goals into a composite pattern
     */
    public GoalInterface goalJsonToPattern(JsonObject jsonGoal) {
        if (jsonGoal.get("goal").getAsString().equals("AND")) {
            GoalInterface goal = new AndGoal();
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else if (jsonGoal.get("goal").getAsString().equals("OR")) {
            GoalInterface goal = new OrGoal();
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else {
            return GoalFactory.getGoal(jsonGoal.get("goal").getAsString());
        }
    }

    public void setObservers() {
        for (MovingEntity e : getMovingEntityList()) {
            player.registerObserver(e);
        }
    }

}
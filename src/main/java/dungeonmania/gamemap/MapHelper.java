package dungeonmania.gamemap;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.Goals.GoalHelper;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Position;

public class MapHelper {

    // ****************************************************************************************************\\
    //                                           Map Conversion                                            \\
    // ****************************************************************************************************\\

    /**
     * Takes a map of entities and convert it to a json array object.
     * @param eList
     * @return
     */
    public static JSONArray entitiesToJson(GameMap gameMap) {
        // Remove all entites that are currently on a swamp tile:
        JSONArray jsonArray = new JSONArray();
        // Add all entities on the map
        for (Entity e : gameMap.getAllEntity()) {
            // Check if it is on a swamp tile and dont put if it is:
            if (!isOnSwampTile(gameMap, e.getId())) { jsonArray.put(e.toJSONObject()); } 
        }
        return jsonArray;
    }

    /**
     * Takes a GameMap object and returns its JSON object.
     * @return JsonObject of the current state of the map.
     */
    public static JSONObject mapToJson(GameMap map) {
        // Main object for file
        JSONObject main = new JSONObject();
        // Add all fields:
        main.put("width", map.getMapWidth());
        main.put("height", map.getMapHeight());
        main.put("game-mode", map.getGameState().getMode());
        main.put("map-name", map.getDungeonName());
        main.put("map-id", map.getMapId());
        main.put("game-index", map.getGameIndex());
        main.put("goal-condition", GoalHelper.goalPatternToJson(GoalHelper.getGoalPattern(map.getJsonMap())));
        main.put("inventory", map.getPlayer().getInventory().toJSON());
        main.put("entities", entitiesToJson(map));
        return main;
    }

    // ****************************************************************************************************\\
    //                                        Getting JSON File                                            \\
    // ****************************************************************************************************\\

    /**
     * Given the name of a saved file, attempts to look for the game
     * and return it as a JsonObject
     * @param name (String)
     * @return JsonObject file of the saved game.
     */
    public static JsonObject getSavedMap(String name, String mapId) {
        try {
            return JsonParser.parseReader(new FileReader("saved_games\\" + name + ".json")).getAsJsonObject();
        } catch (Exception e) {
            try {
                return JsonParser.parseReader(new FileReader("time_travel_record\\" + mapId + "\\" + name + ".json")).getAsJsonObject();
            } catch (Exception c) {
                throw new IllegalArgumentException("File not found.");
            }
        }
    }

    // ****************************************************************************************************\\
    //                                  Saving and Loading Functions                                       \\
    // ****************************************************************************************************\\

    /**
     * Given the name of the map converts the current game map 
     * into a json file and saves it in the designated folder.
     * @param name (String)
     */
    public static void saveMapAsJson(GameMap map, String name) {
        try {  
            // Writes the json file into the folder
            FileWriter file = new FileWriter("saved_games/" + name + ".json");
            file.write(mapToJson(map).toString(4));
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
    public static void saveTickInstance(GameMap map, String name) {
        try {  
            // Writes the json file into the folder
            FileWriter file = new FileWriter("time_travel_record/" + map.getMapId() + "/" + name + ".json");
            file.write(mapToJson(map).toString(4));
            file.flush();
            file.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }


    // ****************************************************************************************************\\
    //                                     Game Initialisation Helper                                      \\
    // ****************************************************************************************************\\

    /**
     * Initialises the map given the width and the length with empty lists.
     * @param width (int)
     * @param height (int)
     * @return Map<Position, List<Entity>> of all entities on the map.
     */
    public static Map<Position, List<Entity>> createInitialisedMap(int width, int height) {
        Map<Position, List<Entity>> map = new HashMap<>();
        // Initialise everything on the map to empty list
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
     * Given difficulty of the name as a string, set the state of 
     * the game respectively.
     * @param difficulty (String)
     */
    public static GameState createGameState(String difficulty) {
        if (difficulty.equals("Peaceful")) {
            return new PeacefulState();
        } else if (difficulty.equals("Standard")) {
            return new StandardState();
        } else {
            return new HardState();
        }
    }


    // ****************************************************************************************************\\
    //                                   SwampTile Helper Functions                                        \\
    // ****************************************************************************************************\\

    /**
     * Given the GameMap object, checks if anything entity is on the 
     * tile currently.
     * @param map
     */
    public static void swampTileTick(GameMap map) {
        // Loop through all swamp_tile entites
        for (Entity e : map.getEntityTypeList("swamp_tile")) {
            ((SwampTile) e).tickCount();
            ((SwampTile) e).checkTile(map.getEntityPositionList(e.getPos()));
        }
    }

    /**
     * Checks if the entity is on top of a swamp tile.
     * @param gameMap
     * @return True is the entity is on a swamp tile, false otherwise.
     */
    public static boolean isOnSwampTile(GameMap map, String id) {
        if (id == null) { id = map.getPlayer().getId(); }

        for (Entity e : map.getEntityTypeList("swamp_tile")) {
            if (((SwampTile) e).entityOnTile(id)) { return true; }
        }
        return false;
    }

    /**
     * If there are entities on the tile when loading the game, add them to the 
     * tile list.
     */
    public static void addEntityToSwampTile(SwampTile swapTile, JsonObject obj, GameMap gameMap) {
        Integer i = 1;
        for (JsonElement entity : obj.getAsJsonArray("entites_on_tile")) {
            JsonObject jObject = entity.getAsJsonObject();
            Position pos = new Position(jObject.get("x").getAsInt(), jObject.get("y").getAsInt());
            String id = swapTile.getId() + "onswamptile" + i;
            // Create the object:
            Entity e = EntityFactory.getEntityObject(id, pos, jObject, gameMap);
            // Add to map
            gameMap.getMap().get(e.getPos()).add(e);
            // Add to swamp map
            swapTile.addToMap(e, jObject.get("ticks_remaining").getAsInt());
            i++;
        }
    }

}

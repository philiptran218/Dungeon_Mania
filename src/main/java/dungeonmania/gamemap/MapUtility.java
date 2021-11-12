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
import dungeonmania.Battles.Battle;
import dungeonmania.Goals.GoalUtility;
import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MapUtility {

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
            if (!entityOnASwampTile(gameMap, e.getId())) { 
                jsonArray.put(e.toJSONObject()); 
            } 
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
        main.put("width", map.getWidth());
        main.put("height", map.getHeight());
        main.put("game-mode", map.getGameState().getMode());
        main.put("map-name", map.getDungeonName());
        main.put("map-id", map.getMapId());
        main.put("game-index", map.getGameIndex());
        main.put("goal-condition", GoalUtility.goalPatternToJson(GoalUtility.getGoalPattern(map.getJsonMap())));
        main.put("entities", entitiesToJson(map));
        return main;
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
     * Create an old version fo the player.
     * @param map
     */
    public static void addOldPlayer(GameMap map) {
        Player player = map.getPlayer();
        for (JsonElement e : getSavedMap(map.getGameIndex().toString(), map.getMapId()).get("entities").getAsJsonArray()) {
            JsonObject entityJson = e.getAsJsonObject();
            if (entityJson.get("type").getAsString().equals("player")) {
                Position pos = new Position(entityJson.get("x").getAsInt(), entityJson.get("y").getAsInt(), 3);
                Player oldPlayer = (Player) EntityFactory.getEntityObject("player", pos, entityJson, map);
                Battle battle = new Battle(map.getGameState().getMode());
                oldPlayer.setBattle(battle);
                oldPlayer.setType("older_player");
                map.getMap().get(pos).add(oldPlayer);
            }
        }
        map.setPlayer(player);
    }
    
    /**
     * Get the direction the old player needs to travel in.
     * @param map
     * @return Direction in which the player needs to travel to.
     */
    public static Direction findOlderPlayerMoveDirection(GameMap map) {
        Integer index = map.getGameIndex() + 1;
        Position newPos = null;
        if (getSavedMap(index.toString(), map.getMapId()) != null) {
            for (JsonElement e : getSavedMap(index.toString(), map.getMapId()).get("entities").getAsJsonArray()) {
                JsonObject entityObj = e.getAsJsonObject();
                if (entityObj.get("type").getAsString().equals("player")) {
                    newPos = new Position(entityObj.get("x").getAsInt(), entityObj.get("y").getAsInt());
                }
            }
        } else {
            // Remove the old player from the map:
            for (Entity e : map.getMap().get(map.getOlderPlayerPosition())) {
                if (e.isType("older_player")) { map.getMap().get(map.getOlderPlayerPosition()).remove(e); }
                return null;
            }
            
        }
        // Current map
        List<Position> posList = map.getOlderPlayerPosition().getCardinallyAdjacentPositions();
        if (posList.get(0).equals(newPos)) {
            return Direction.UP;
        } else if (posList.get(1).equals(newPos)) {
            return Direction.RIGHT;
        } else if (posList.get(2).equals(newPos)) {
            return Direction.DOWN;
        } else {
            return Direction.LEFT;
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
                return null;
            }
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
        if (difficulty.equals("peaceful")) {
            return new PeacefulState();
        } else if (difficulty.equals("standard")) {
            return new StandardState();
        } else {
            return new HardState();
        }
    }


    // ****************************************************************************************************\\
    //                                 SwampTile Map Helper Functions                                      \\
    // ****************************************************************************************************\\

    /**
     * Given the game map, adds entities that have just made its way onto the tile
     * and tick all exisitng entities on the tile by one tick.
     * @param map
     */
    public static void tickAllSwampTiles(GameMap map) {
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
    public static boolean entityOnASwampTile(GameMap map, String id) {
        if (id == null) { id = map.getPlayer().getId(); }

        for (Entity e : map.getEntityTypeList("swamp_tile")) {
            if (((SwampTile) e).entityOnTile(id)) { return true; }
        }
        return false;
    }

}

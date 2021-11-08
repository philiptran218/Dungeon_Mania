package dungeonmania.gamemap;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import org.json.JSONArray;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Position;

public class MapHelper {

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
     * Given the name of a saved file, attempts to look for the game
     * and return it as a JsonObject
     * @param name (String)
     * @return JsonObject file of the saved game.
     */
    public static JsonObject getSavedMap(String name) {
        try {
            return JsonParser.parseReader(new FileReader("saved_games\\" + name + ".json")).getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("File not found.");
        }
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

    /**
     * Takes a map of entities and conver it to a json array object.
     * @param eList
     * @return
     */
    public static JSONArray entitiesToJson(Map<Position, List<Entity>> map) {
        // Remove all entites that are currently on a swamp tile:
        JSONArray jsonArray = new JSONArray();
        // Add all entities on the map
        for (Map.Entry<Position, List<Entity>> entry : map.entrySet()) {
            for (Entity e : entry.getValue()) {
                // Check if it is on a swamp tile and dont put if it is:
                if (!entityOnSwamp(map, e.getId())) { jsonArray.put(e.toJSONObject()); } 
            }
        }
        return jsonArray;
    }

    /**
     * Given the map and the type of entity you want, returns a list of 
     * entities of that type on the map.
     * @param map
     * @param eType
     * @return
     */
    public static List<Entity> getEntityTypeList(Map<Position, List<Entity>> map, String eType) {
        List<Entity> eList = new ArrayList<>();
        // Loop through to the entity
        for (Map.Entry<Position, List<Entity>> entry : map.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (e.isType(eType)) { eList.add(e); }
            }
        }
        return eList;
    }
    
    /**
     * Given the map and the id of an entity, checks if the entity currently
     * sits on a swamp tile or not.
     * @param map
     * @param id
     * @return True if the entity is on a swamp tile, false otherwise.
     */ // function should be in entity::::
    public static boolean entityOnSwamp(Map<Position, List<Entity>> map, String id) {
        for (Entity e : MapHelper.getEntityTypeList(map, "swamp_tile")) {
            if (((SwampTile) e).entityOnTile(id)) { return true; }
        }
        return false;
    }

    /**
     * 
     */
    public static void addEntityToSwampTile(SwampTile swapTile, Map<Position, List<Entity>> map, JsonObject obj, GameMap gameMap) {
        Integer i = 1;
        for (JsonElement entity : obj.getAsJsonArray("entites_on_tile")) {
            JsonObject jObject = entity.getAsJsonObject();
            Position pos = new Position(jObject.get("x").getAsInt(), jObject.get("y").getAsInt());
            String id = swapTile.getId() + "onswamptile" + i;
            // Create the object:
            Entity e = EntityFactory.getEntityObject(id, pos, jObject, gameMap);
            // Add to map
            map.get(e.getPos()).add(e);
            // Add to swamp map
            swapTile.addToMap(e, jObject.get("ticks_remaining").getAsInt());
            // Set player if the entity is player:
            if (e.isType("player")) {  }
            i++;
        }
    }

}

package dungeonmania.gamemap;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import org.json.JSONArray;

import dungeonmania.Entity;
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
        for (int k = -1; k < 5; k++) {
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
    public static JSONArray mapToJSON(Map<Position, List<Entity>> eMap) {
        JSONArray jsonArray = new JSONArray();
        // Add all entities on the map
        for (Map.Entry<Position, List<Entity>> entry : eMap.entrySet()) {
            for (Entity e : entry.getValue()) {
                jsonArray.put(e.toJSONObject());
            }
        }
        return jsonArray;
    }

}

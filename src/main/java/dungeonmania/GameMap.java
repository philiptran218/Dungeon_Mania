package dungeonmania;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Need to figure something to stand in for entity, or we might need 
    // multiple maps for one dungeon.
    
    //Map<Position, Entity> dungeonMap = new HashMap<>();
    private String gameDifficulty;

    // Need to make varibales to game state here:
    // GameState currState;

    public GameMap(String difficulty) {
        // Constructor
        this.gameDifficulty = difficulty;
    }

    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List of Entity Response
     */
    public List<EntityResponse> mapToListEntityResponse(JsonObject map) {
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();
        Integer i = 0;
        for (JsonElement entity : map.getAsJsonArray("entities")) {
            Position pos = new Position(entity.getAsJsonObject().get("x").getAsInt(), entity.getAsJsonObject().get("y").getAsInt());
            String type = entity.getAsJsonObject().get("type").getAsString();
            // Need additional checking here to see if the entity can interact with the frontend.
            // Also, look for ways to make a better id:
            entityList.add(new EntityResponse(i.toString(), type, pos, false));
            i++;
        }
        return entityList;
    }

    /**
     * Given a file path, it will get the json file and return it as
     * a json object.
     * @return Dungeon Map as JsonObject
     */
    public JsonObject getMapAsJson(String filePath) {
        // "src\\main\\resources\\dungeons\\" + dungeonName + ".json"
        try {
            String path = filePath;
            return JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("File does not exist.");
        }
    }
    
    /**
     * Converts the current game map into a json file and saves it 
     * in the designated folder. Also If there is no game difficulty
     * add a field in the json file for game difficulty.
     */
    public void saveMap() {
        return;
    }

    
    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return
     */
    /*
    public Map<Position, Entity> jsonToMap() {
        
    }*/

}

package dungeonmania;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import dungeonmania.MovingEntities.Spider;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Need to figure something to stand in for entity, or we might need 
    // multiple maps for one dungeon.
    private Map<Position, List<Entity>> dungeonMap = new HashMap<>();
    private String gameDifficulty;

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
    public GameMap(String difficulty, JsonObject jsonMap) {
        // THIS IS FOR A NEW GAME 
        this.gameDifficulty = difficulty;

        // Given the json map, we would convert it to a Map<Position, Entity List> 
        // and set dungeonMap to this map.
    }

    /**
     * This constructor used for loading saved games.
     * @param map
     * @return
     */
    public GameMap(JsonObject jsonMap) {
        // This is called for an exisiting game where only
        // the json map is passed in. Convert it to Map<Position, Entity List>
        // and set it to this map.
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
            JsonObject obj = entity.getAsJsonObject();
            Position pos;
            // Check if there is a third layer:
            if (obj.get("layer") == null) {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            } else {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("layer").getAsInt());
            }
            String type = obj.get("type").getAsString();
            // Need additional checking here to see if the entity can interact with the frontend.
            entityList.add(new EntityResponse(i.toString(), type, pos, false));
            i++;
        }
        return entityList;
    }
    
    /**
     * Converts the current game map into a json file and saves it 
     * in the designated folder. Also If there is no game difficulty
     * add a field in the json file for game difficulty.
     */
    public void saveMapAsJson() {
        // Argument would be a Map<Position, Entity>.
        return;
    }

    public List<Entity> insertEntityLayer(List<Entity> entityList, Entity entity) {
        // Empty list or if entity does not have a layer
        if (entityList.isEmpty()) {
            entityList.add(entity);
            return entityList;
        }
        
        for (Entity i : entityList) {
            // Layer of the entity I am inserting
            int srcLayer = entity.getPos().getLayer();
            // Layer of entity I am comparing to
            int dstLayer = i.getPos().getLayer();

            if (srcLayer < dstLayer) {
                entityList.add(entityList.indexOf(i), entity);
                break;
            } 

            // Check for last:
            int listSize = entityList.size() - 1;
            if (entityList.get(listSize).getPos().getLayer() < srcLayer) {
                entityList.add(entity);
                break;
            }
            // Get index after the next.
            int next = entityList.indexOf(i) + 1;
            if (srcLayer > dstLayer && srcLayer < entityList.get(next).getPos().getLayer()){
                entityList.add(next, entity);
                break;
            }
        }
        return entityList;
    }

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return
     */
    public Map<Position, List<Entity>> jsonToMap(JsonObject jsonMap) {
        // Create map:
        Map<Position, List<Entity>> newMap = new HashMap<>();

        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            Position pos = new Position(entity.getAsJsonObject().get("x").getAsInt(), entity.getAsJsonObject().get("y").getAsInt());
            String type = entity.getAsJsonObject().get("type").getAsString();
            EntityFactory.getEntityObject(type, pos);
            JsonObject obj = entity.getAsJsonObject();
            // Check if there is a third layer:
            if (obj.get("layer") == null) {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            } else {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("layer").getAsInt());
            }
            // Update the map with new entity
            newMap.put(pos, insertEntityLayer(newMap.get(pos), EntityFactory.getEntityObject(type, pos)));
        }
        return newMap;
    }
    public static void main(String[] args) {
        
    }
}

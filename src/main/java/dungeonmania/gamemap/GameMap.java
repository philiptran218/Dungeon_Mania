package dungeonmania.gamemap;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import dungeonmania.Entity;
import dungeonmania.EntityFactory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GameMap {
    // Need to figure something to stand in for entity, or we might need 
    // multiple maps for one dungeon.
    private Map<Position, List<Entity>> dungeonMap;
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
        this.dungeonMap = jsonToMap(jsonMap);
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
    public List<EntityResponse> mapToListEntityResponse() {
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();

        for (Map.Entry<Position, List<Entity>> entry : this.dungeonMap.entrySet()) {
            // For each position add the entity to the response list:
            // First check if it is one element:
            if (entry.getValue().size() == 1) {
                Entity e = entry.getValue().get(0);
                entityList.add(new EntityResponse(e.getId(), e.getType(), e.getPos(), false));
            } 
            
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
    
    /**
     * Converts the current game map into a json file and saves it 
     * in the designated folder. Also If there is no game difficulty
     * add a field in the json file for game difficulty.
     */
    public void saveMapAsJson() {
        // Argument would be a Map<Position, Entity>.
        return;
    }

    /**
     * Initialises the map given the width and the length with empty lists.
     */
    public Map<Position, List<Entity>> createInitialisedMap(int width, int height) {
        Map<Position, List<Entity>> map = new HashMap<>();
        for (int i = 0; i < width; i++) { // width
            for (int j = 0; j < height; j++) { // height
                map.put(new Position(i, j), new ArrayList<Entity>());
            }
        }
        return map;
    }

    /**
     * This function adds given entity to the given list taking into account the order
     * of layer.
     * @param currList
     * @param insert
     * @return An ordered list of entities in terms of layer.
     */
    public List<Entity> orderLayer(List<Entity> currList, Entity insert) {
        List<Entity> orderList = new ArrayList<>();
        // Create an integer list:
        List<Integer> intList = new ArrayList<>();
        currList.add(insert);
        // Add all layer ints to the list;
        for (Entity e : currList) {
            intList.add(e.getPos().getLayer());
        }
        // Sort the curr list:
        Collections.sort(intList);

        for (int i : intList) {
            for (Entity e : currList) {
                if (e.getPos().getLayer() == i) {
                    orderList.add(e);
                }
            }
        }
        return orderList;
    }

    /**
     * Takes in a json object, and turns it into a Map<Position, Entity>
     * and returns it.
     * @return
     */
    public Map<Position, List<Entity>> jsonToMap(JsonObject jsonMap) {
        // Initialise the map:
        Map<Position, List<Entity>> newMap = createInitialisedMap(jsonMap.get("width").getAsInt(), jsonMap.get("height").getAsInt());

        Integer i = 0;
        for (JsonElement entity : jsonMap.getAsJsonArray("entities")) {
            // Get all attributes:
            JsonObject obj = entity.getAsJsonObject();
            String type = obj.get("type").getAsString();
            Position pos;

            if(obj.get("layer") == null) {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt());
            } else {
                pos = new Position(obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("layer").getAsInt());
            }

            // Create the entity object, by factory method:
            Entity temp = EntityFactory.getEntityObject(i.toString(), type, pos, obj.get("key"));
            Position insertPosition = new Position(pos.getX(), pos.getY());

            if (obj.get("type").getAsString().equals("player")) {
                System.out.println("HELLO");
                System.out.println(type);
                System.out.println(i);
                System.out.println(temp.getType());
            }
            
            // Before adding the element check the list:
            newMap.put(insertPosition, orderLayer(newMap.get(insertPosition), temp));
            i++;
        }
        return newMap;
    }

}

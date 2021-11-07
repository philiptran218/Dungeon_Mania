package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int factor;
    // This map tracks the number of ticks until entity can move
    private Map<Entity, Integer> eMap = new HashMap<>();

    public SwampTile(String id, String type, Position pos, int factor) {
        super(id, type, pos);
        this.factor = factor;
    }

    /**
     * Given the map, locate the position of the tile map, and adds it 
     * to the eMap.
     * @param gameMap
     */
    public void checkTile(List<Entity> eList) {
        // Go through the entities on tiles and add them
        for (Entity e : eList) {
            if (!e.hasId(super.getId()) && !eMap.containsKey(e)) { eMap.put(e, factor); }
        }
        // Loop through to see if any have been on the tile for enough time
        List<Entity> removeEntity = new ArrayList<>();
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            if (entry.getValue() == 0) { removeEntity.add(entry.getKey()); }
        }
        // Remove all entities in the remove list:
        removeEntity.forEach(e -> eMap.remove(e));
    }

    /**
     * Decreases the factor of every single one of the entities in 
     * the map by 1.
     * @param id
     */
    public void tickCount() {
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            eMap.put(entry.getKey(), entry.getValue() - 1);
        }
    }

    /**
     * Given an entity id, checks if the entity is in 
     * the map, if not the entity can move around.
     * @param id
     * @return True if the entity is not in map, false otherwise.
     */
    public boolean entityOnTile(String id) {
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            if (entry.getKey().hasId(id)) { return true; }
        }
        return false;
    }

    /**
     * Given an entity and value, add it to the map.
     * @param e
     * @param value
     */
    public void addToMap(Entity e, int value) {
        eMap.put(e, value);
    }
    
    @Override
    public JSONObject toJSONObject() {
        JSONObject tmp = super.toJSONObject();
        // Overwrite type
        tmp.put("movement_factor", factor);
        // Add all entities in the list:
        JSONArray entities = new JSONArray();
        // Add all entities currently on the tile:
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            JSONObject obj = entry.getKey().toJSONObject();
            obj.put("ticks_remaining", entry.getValue());
            entities.put(obj);
        }
        tmp.put("entites_on_tile", entities);
        return tmp;
    }

    // Getters and Setters
    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public Map<Entity, Integer> getMap() {
        return eMap;
    }

}
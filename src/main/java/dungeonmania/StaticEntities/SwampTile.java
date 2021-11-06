package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int factor;
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
            if (!e.isType("swamp_tile") && !eMap.containsKey(e)) { eMap.put(e, factor); }
        }

        // Remove the ones that have exhausted all factors
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            if (entry.getValue() == 0) {
                eMap.remove(entry.getKey());
            }
        }
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

    public boolean entityOnTile(Entity entity) {
        for (Map.Entry<Entity, Integer> entry : eMap.entrySet()) {
            if (entry.getKey().hasId(entity.getId())) { return true; }
        }
        return false;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject tmp = super.toJSONObject();
        // Overwrite type
        tmp.put("movement_factor", factor);
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
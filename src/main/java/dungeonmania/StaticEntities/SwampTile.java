package dungeonmania.StaticEntities;

import org.json.JSONObject;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    int factor;

    public SwampTile(String id, String type, Position pos, int factor) {
        super(id, type, pos);
        this.factor = factor;
    }

    // Getters and Setters
    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
    
    @Override
    public JSONObject toJSONObject() {
        JSONObject tmp = super.toJSONObject();
        // Overwrite type
        tmp.put("movement_factor", factor);
        return tmp;
    }
}
package dungeonmania.StaticEntities;

import org.json.JSONObject;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyId;
    /**
     * Constructor for Door
     * @param id
     * @param type
     * @param pos
     * @param keyId
     */
    public Door(String id, String type, Position pos, int keyId) {
        super(id, type, pos);
        this.keyId = keyId;
    }

    public int getKeyId() {
        return keyId;
    }
    
    // Convert it to a JSONObject
    @Override
    public JSONObject toJSONObject() {
        JSONObject tmp = super.toJSONObject();
        // Overwrite type
        tmp.put("type", super.getType());
        tmp.put("key", keyId);
        return tmp;
    }
}

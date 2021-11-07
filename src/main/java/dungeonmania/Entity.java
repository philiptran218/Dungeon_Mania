package dungeonmania;

import org.json.JSONObject;

import dungeonmania.util.Position;

public abstract class Entity {
    private String id;
    private Position pos;
    private String type;

    public Entity (String id, String type, Position pos) {
        this.id = id;
        this.pos = pos;
        this.type = type;
    }

    // Converts itself to a json object:
    public JSONObject toJSONObject() {
        JSONObject self = new JSONObject();
        self.put("x", pos.getX());
        self.put("y", pos.getY());
        self.put("type", getType());
        return self;
    }

    // Convert to an inventory object:
    public JSONObject toJSONObjectInventory() {
        JSONObject self = new JSONObject();
        self.put("type", type);
        return self;
    }

    /**
     * Given a string type, checks if the entity is of the 
     * same type as the one given.
     * @param eType
     * @return True is same type, false otherwise.
     */
    public boolean isType(String eType) {
        return (type.equals(eType));
    }

    // Getters and Setters
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

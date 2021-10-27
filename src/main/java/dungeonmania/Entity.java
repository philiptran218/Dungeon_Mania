package dungeonmania;

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

    // Getters and Setters
    public String getId() {
        return id;
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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

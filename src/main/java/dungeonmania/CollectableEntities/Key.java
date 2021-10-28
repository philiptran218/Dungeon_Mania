package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Key extends Utility {

    private int keyId;

    /**
     * Constructor for Arrow
     * @param id
     * @param type
     * @param pos
     */
    public Key(String id, String type, Position pos, int keyId) {
        super(id, type, pos);

    }

    // Getters and Setters
    public void setKeyId(int id) {
        keyId = id;
    }

    public int getKeyId() {
        return keyId;
    }

    public void use() {
        // Check if player is cardinally adjacent to a door
        // Then check that keyId matches to door's Id
        // Then open door if possible (check goals etc... may be delegated to other functions)
    }

}

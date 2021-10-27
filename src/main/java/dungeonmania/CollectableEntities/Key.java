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

}
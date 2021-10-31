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
        this.setKeyId(keyId);
    }

    /**
     * Is called if the key is used to craft a shield or open a door.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }

    // ********************************************************************************************\\
    //                                    Getters and Setters                                      \\
    // ********************************************************************************************\\
    // Getters and Setters
    public void setKeyId(int id) {
        keyId = id;
    }

    public int getKeyId() {
        return keyId;
    }
}

package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyId;
    private boolean locked = true;
    /**
     * Constructor for Door
     * @param id
     * @param type
     * @param pos
     * @param keyId
     */
    public Door(String id, String type, Position pos, int keyId) {
        super(id, type, pos);
        super.setCanStandOn(false);
        this.keyId = keyId;
    }
    /**
     * Unlocks the door
     */
    public void unlock() {
        locked = false;
        super.setCanStandOn(true);
    }
    // Getters and setters
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
    
}

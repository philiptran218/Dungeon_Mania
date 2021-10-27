package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyId;
    private boolean locked = true;

    public Door(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
        super.setType("Door");
    }

    public void unlock(int key) {
        if (keyId == key) {
            locked = false;
            super.setCanStandOn(true);
        }
    }

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

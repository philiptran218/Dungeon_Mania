package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Arrow extends Utility {

    /**
     * Constructor for Arrow
     * @param id
     * @param type
     * @param pos
     */
    public Arrow(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Uses arrows to build bows.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }
}
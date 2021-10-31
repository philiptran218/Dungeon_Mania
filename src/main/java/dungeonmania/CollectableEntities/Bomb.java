package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Bomb extends CombatItems {

    public Bomb(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Places the bomb onto the map, and removes it from inventory.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }
}
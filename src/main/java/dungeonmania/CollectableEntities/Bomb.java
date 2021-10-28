package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Bomb extends CombatItems {

    public Bomb(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Places the bomb onto the map.
     */
    public void use() {
        // Remove from inventory, add onto entities map
        //this.setPos(new Position(0, 0));
    }

}
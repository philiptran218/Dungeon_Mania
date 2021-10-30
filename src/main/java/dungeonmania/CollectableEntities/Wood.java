package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class Wood extends Utility {

    /**
     * Constructor for Wood
     * @param id
     * @param type
     * @param pos
     */
    public Wood(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Uses wood to build bows and shields. 
     */
    public void use() {
        getPlayer().getInventoryList().remove((CollectableEntity)this);
    }
}

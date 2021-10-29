package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class TheOneRing extends CombatItems {

    /**
     * Constructor for TheOneRing
     * @param id
     * @param type
     * @param pos
     */
    public TheOneRing(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Revives the player if their health falls to 0. Removing TheOneRing from
     * inventory should be done separately.
     */
    public void use() {
        this.getPlayer().setHealth(100);
        getPlayer().getInventoryList().remove(this);
    }

}
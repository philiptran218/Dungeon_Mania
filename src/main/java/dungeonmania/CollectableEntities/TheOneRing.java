package dungeonmania.CollectableEntities;

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
     * Revives the player if their health falls to 0.
     */
    public void use() {
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().getInventoryList().remove((CollectableEntity)this);
    }

}
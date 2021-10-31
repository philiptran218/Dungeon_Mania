package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    /**
     * Constructor for InvisibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvisibilityPotion(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Activates the ability of the potion.
     * Called when the player wants to consume the potion.
     */
    public void use() {
        getPlayer().setInvisDuration(30);
        getPlayer().getInventoryList().remove((CollectableEntity)this);
    }
}
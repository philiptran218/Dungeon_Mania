package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Sceptre extends CombatItems {

    private static final int INITIAL_DURABILITY = 5;
    
    /**
     * Constructor for Sceptre
     * @param id
     * @param type
     * @param pos
     */
    public Sceptre(String id, String type, Position pos) {
        super(id, type, pos);
        this.setDurability(INITIAL_DURABILITY);
    }

    /**
     * Reduces the durability if it has been used
     */
    public void reduceDurability() {
        setDurability(getDurability() - 1);
    }

    /**
     * Used to interact with enemies.
     */
    public void use() {
        reduceDurability();
        checkNoDurability();
    }

    /**
     * Check the durability of the sceptre and if durability is zero 
     * remove it from the inventory.
     */
    public void checkNoDurability() {
        if (getDurability() == 0) {
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }    
}
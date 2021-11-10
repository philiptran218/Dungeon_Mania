package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Sword extends CombatItems {

    private static final double DAMAGE = 1;
    private static final int INITIAL_DURABILITY = 10;
    
    /**
     * Constructor for Sword
     * @param id
     * @param type
     * @param pos
     */
    public Sword(String id, String type, Position pos) {
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
     * Used to interact with enemy spawners.
     */
    public void use() {
        reduceDurability();
        checkNoDurability();
    }

    /**
     * Called when the combat item is being used in battle.
     * Its durability is reduced by 1 and its damage value is returned for
     * use in damage calculation.
     * @return the damage count of the combat item
     */
    public double usedInCombat() {
        reduceDurability();
        checkNoDurability();
        return DAMAGE;
    }

    /**
     * Check the durability of the sword and if durability is zero 
     * remove it from the inventory.
     */
    public void checkNoDurability() {
        if (getDurability() == 0) {
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }    
}
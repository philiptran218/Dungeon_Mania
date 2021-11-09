package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Shield extends CombatItems {

    private static final double REDUCE_DAMAGE = 0.6;
    private static final int INITIAL_DURABILITY = 10;
    
    /**
     * Constructor for Shield
     * @param id
     * @param type
     * @param pos
     */
    public Shield(String id, String type, Position pos) {
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
     * Won't need to be called as shields are only used in battles.
     */
    public void use() {
    }

    /**
     * Called when the combat item is being used in battle.
     * Its durability is reduced by 1 and its damage reduction value is returned for
     * use in damage calculation.
     * @return the damage count of the combat item
     */
    public double usedInCombat() {
        reduceDurability();
        checkNoDurability();
        return REDUCE_DAMAGE;
    }

    /**
     * Check the durability of the armour and if durability is zero 
     * remove it from the inventory.
     */
    public void checkNoDurability() {
        if (getDurability() == 0) {
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }
}
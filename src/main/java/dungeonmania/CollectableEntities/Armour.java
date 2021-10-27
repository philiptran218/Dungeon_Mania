package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Armour extends CombatItems {

    private static final double REDUCE_DAMAGE = 0.5;
    // Leave at 20 for now, change it later
    private static final int INITIAL_DURABILITY = 20;
    private int durability;
    
    /**
     * Constructor for Armour
     * @param id
     * @param type
     * @param pos
     */
    public Armour(String id, String type, Position pos) {
        super(id, type, pos);
        this.setDurability(INITIAL_DURABILITY);
    }

    // Getters and Setters
    public void setDurability(int num) {
        durability = num;
    }
    
    public int getDurability() {
        return durability;
    }

    public double getReduceDamage() {
        return REDUCE_DAMAGE;
    }

    /**
     * Reduces the durability if it has been used
     */
    public void reduceDurability() {
        durability = durability - 1;
    }

    /**
     * Called when the combat item is being used in battle.
     * Its durability is reduced by 1 and its damage reduction value is returned for
     * use in damage calculation.
     * @return the damage count of the combat item
     */
    public double usedInCombat() {
        reduceDurability();
        return REDUCE_DAMAGE;
    }

}
package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class Armour extends CombatItems {

    private static final double REDUCE_DAMAGE = 0.5;
    private static final int INITIAL_DURABILITY = 10;
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
     * Won't need to be called, since armour is only used in battles.
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

    public void checkNoDurability() {
        if (durability == 0) {
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }

}
package dungeonmania.Battles;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;

public class NormalState implements BattleState {
    
    public NormalState() {

    }

    /**
     * Conducts the normal actions for fighting. p1 is the player and
     * p2 is the enemy.
     */
    public void fight(MovingEntity p1, MovingEntity p2) {

        healthModifier(p2, damageCalculation(p1), p1.getHealth());

    }


    /**
     * Checks for base dmg, swords and bows that can modify damage.
     * @param p1
     */
    public double damageCalculation(MovingEntity p1) {
        if (p1 instanceof Player) {
            Player plyr = (Player) p1;
            double dmg = p1.getAttackDamage();
            Sword sword = p1.getInventory().getItem("sword");
            Bow bow = p1.getInventory().getItem("bow");

            if (sword != null) {
                dmg += sword.usedInCombat();
            }
            if (bow != null) {
                dmg += bow.usedInCombat();
            }
            return dmg;
        }
        else {
            return p1.getAttackDamage();
        }
    }

    public void healthModifier(MovingEntity p2, double dmg, double health) {
        double newHealth;
        if (p2 instanceof Player) {
            Shield shield = p2.getInventory().getItem("shield");
            Armour armour = p2.getInventory().getItem("armour");
            double multiplier = 1;

            if (shield != null) {
                multiplier *= shield.usedInCombat();
            }
            if (armour != null) {
                multiplier *= armour.usedInCombat();
            }
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 10);
            p2.setHealth(newHealth);
        }
        else {
            newHealth = p2.getHealth() - ((health * dmg) / 5);
            p2.setHealth(newHealth);
        }
    }


}

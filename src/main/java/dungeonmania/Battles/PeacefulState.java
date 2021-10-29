package dungeonmania.Battles;

import dungeonmania.MovingEntities.*;
import dungeonmania.CollectableEntities.*;

public class PeacefulState implements BattleState {
    
    private Battle battle;

    public PeacefulState(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    /**
     * Since the difficulty is on Peaceful, only the player can attack.
     */
    public void fight(Player p1, MovingEntity p2) {
        healthModifier(p2, damageCalculation(p1), p1.getHealth());

        // Performs a second attack
        if (p2.getHealth() > 0 && this.getBattle().hasBow(p1)) {
            healthModifier(p2, damageCalculation(p1), p1.getHealth());
        }
    }

    /**
     * Checks for base dmg, swords and bows that can modify damage.
     * @param p1
     */
    public double damageCalculation(Player p1) {
        double dmg = p1.getAttackDamage();
        Sword sword = (Sword) p1.getInventory().getItem("sword");
        Bow bow = (Bow) p1.getInventory().getItem("bow");

        if (sword != null) {
            dmg += sword.usedInCombat();
        }
        if (bow != null) {
            dmg += bow.usedInCombat();
        }
        return dmg;
    }

    public void healthModifier(MovingEntity p2, double dmg, double health) {
        double newHealth = p2.getHealth() - ((health * dmg) / 5);
        p2.setHealth(newHealth);
    }

}
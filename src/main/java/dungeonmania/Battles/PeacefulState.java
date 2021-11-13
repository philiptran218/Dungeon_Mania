package dungeonmania.Battles;

import dungeonmania.MovingEntities.*;

import java.util.concurrent.ThreadLocalRandom;

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
    public MovingEntity fight(Player p1, MovingEntity p2) {

        while (p2.getHealth() > 0) {
            healthModifier(p2, damageCalculation(p1, p2), p1.getHealth());

            // Performs a second attack
            if (p2.getHealth() > 0 && this.getBattle().hasBow(p1)) {
                healthModifier(p2, damageCalculation(p1, p2), p1.getHealth());
            }

            // Performs an attack if player has allies
            for (MovingEntity ally : p1.getBribedAllies()) {
                if (p2.getHealth() > 0) {
                    healthModifier(p2, damageCalculation(ally, p2), ally.getHealth());
                }
            }
        }
        spawnOneRing(p1);
        p1.lootBody(p2);
        return p2;
    }

    /**
     * Checks for base dmg, swords, bows, anduril and midnight armour that can modify damage.
     * @param p1 (entity dealing the damage)
     * @param p2 (entity receiving the damage)
     */
    public double damageCalculation(MovingEntity p1, MovingEntity p2) {
        if (p1.getType().equals("player")) {
            Player plyr = (Player) p1;
            double dmg = plyr.getAttackDamage();
            Sword sword = (Sword) plyr.getInventory().getItem("sword");
            Bow bow = (Bow) plyr.getInventory().getItem("bow");
            Anduril anduril = (Anduril) plyr.getInventory().getItem("anduril");
            MidnightArmour mArmour = (MidnightArmour) plyr.getInventory().getItem("midnight_armour");
            if (sword != null) {
                dmg += sword.usedInCombat();
            }
            if (bow != null) {
                dmg += bow.usedInCombat();
            }
            if (anduril != null) {
                if (p2.getType().equals("assassin")) {
                    dmg += (3 * anduril.usedInCombat());
                }
                else {
                    dmg += anduril.usedInCombat();
                }
            }
            if (mArmour != null) {
                dmg += mArmour.getDamage();
            }
            return dmg;
        }
        else {
            return p1.getAttackDamage();
        }
    }

    public void healthModifier(MovingEntity p2, double dmg, double health) {
        double newHealth;
        if (p2.getType().equals("mercenary")) {
            double multiplier = mercenaryDamageMultiplier((Mercenary) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.getType().equals("zombie_toast")) {
            double multiplier = zombieDamageMultiplier((ZombieToast) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.getType().equals("assassin")) {
            double multiplier = assassinDamageMultiplier((Assassin) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.isType("older_player")) {
            newHealth = 0;
            // Checks if the older player has midnight armour
            if (((Player) p2).getInventory().getItem("midnight_armour") != null) {
                return;
            }
        }
        else {
            newHealth = p2.getHealth() - ((health * dmg) / 5);
        }
        p2.setHealth(newHealth);
    }

    public double zombieDamageMultiplier(ZombieToast zombieToast) {
        Armour armour = zombieToast.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                zombieToast.setArmour(null);
            }
        }
        return multiplier;
    }

    public double mercenaryDamageMultiplier(Mercenary mercenary) {
        Armour armour = mercenary.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                mercenary.setArmour(null);
            }
        }
        return multiplier;
    }

    public double assassinDamageMultiplier(Assassin assassin) {
        Armour armour = assassin.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                assassin.setArmour(null);
            }
        }
        return multiplier;
    }

    public void spawnOneRing(Player p1) {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 10% chance that The One Ring spawns after winning a battle
        if (num > 8) {
            TheOneRing ring = new TheOneRing("" + System.currentTimeMillis(), "one_ring", null);
            p1.getInventory().put(ring, p1);
        }
    }

}
package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    private final double AssassinHealth = 15;
    private final double AssassinDamage = 5;

    public Hydra(String id, String type, Position pos) {
        super(id, type, pos);
        super.setHealth(AssassinHealth);
        super.setAttackDamage(AssassinDamage);
    }
}
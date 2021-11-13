package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    private final double HydraHealth = 40;
    private final double HydraDamage = 4;

    public Hydra(String id, String type, Position pos) {        
        super(id, type, pos);
        super.setHealth(HydraHealth);
        super.setAttackDamage(HydraDamage);
    }
}
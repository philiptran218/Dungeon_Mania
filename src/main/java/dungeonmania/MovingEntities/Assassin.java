package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    private final double AssassinHealth = 20;
    private final double AssassinDamage = 3;

    public Assassin(String id, String type, Position pos) {
        super(id, type, pos);
        super.setHealth(AssassinHealth);
        super.setAttackDamage(AssassinDamage);
    }
}

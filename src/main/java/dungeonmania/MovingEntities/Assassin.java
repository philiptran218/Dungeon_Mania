package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    private final double AssassinHealth = 5;
    private final double AssassinDamage = 15;

    public Assassin(String id, String type, Position pos) {
        super(id, type, pos);
        super.setHealth(AssassinHealth);
        super.setAttackDamage(AssassinDamage);
    }
}

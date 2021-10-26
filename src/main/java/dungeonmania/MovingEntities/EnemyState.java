package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class EnemyState implements MercenaryState{
    private Mercenary mercenary;
    private Player player;


    public EnemyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }
    @Override
    public void move() {
        Position playerloc = player.getLocation();
    }
    
}

package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;

public class MercenaryAllyState implements MercenaryState{
    private Mercenary mercenary;
    private Player player;


    public MercenaryAllyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    @Override
    public void move(Map<Position, List<Entity>> map) {
        
    }
    
}

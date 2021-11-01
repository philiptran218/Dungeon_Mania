package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.Entity;

public interface MercenaryState {
    public void move(Map<Position, List<Entity>> map);
    public void moveAway(Map<Position, List<Entity>> map);
}

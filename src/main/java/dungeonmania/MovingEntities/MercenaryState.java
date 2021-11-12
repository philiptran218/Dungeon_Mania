package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.Entity;
import dungeonmania.response.models.AnimationQueue;

public interface MercenaryState {
    public void move(Map<Position, List<Entity>> map, List<AnimationQueue> animations);
    public void moveAway(Map<Position, List<Entity>> map, List<AnimationQueue> animations);
}

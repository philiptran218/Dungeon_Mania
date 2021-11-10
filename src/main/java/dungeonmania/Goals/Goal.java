package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface Goal {
    public boolean isGoalComplete(Map<Position, List<Entity>> map);
    public String getGoalName();
}

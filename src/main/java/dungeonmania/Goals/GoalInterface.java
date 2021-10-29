package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface GoalInterface {
    public boolean isGoalComplete(Map<Position, List<Entity>> map);
    public void add(GoalInterface goal);
    public String getGoalName();
    public List<GoalInterface> getChildren();
}

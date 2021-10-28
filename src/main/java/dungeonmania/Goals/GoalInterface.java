package dungeonmania.Goals;

import java.util.List;

import dungeonmania.gamemap.GameMap;

public interface GoalInterface {
    public boolean isGoalComplete(GameMap game);
    public void add(GoalInterface goal);
    public String getGoalName();
    public List<GoalInterface> getChildren();
}

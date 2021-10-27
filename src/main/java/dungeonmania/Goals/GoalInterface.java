package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public interface GoalInterface {
    public boolean isGoalComplete(GameMap game);
    public void add(GoalInterface goal);
}

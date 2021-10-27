package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public class AndGoal extends CompositeGoal implements GoalInterface{
    public boolean isGoalComplete(GameMap game) {
        return true;
    }

}

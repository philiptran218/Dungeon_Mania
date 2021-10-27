package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public abstract class CompositeGoal {
    public boolean isGoalComplete(GameMap game) {
        return true;
    }
}

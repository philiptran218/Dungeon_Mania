package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public class OrGoal extends CompositeGoal implements GoalInterface {
    
    private String goalName = "OR";

    public boolean isGoalComplete(GameMap game) {
        return true;
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
}

package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public class BouldersGoal implements GoalInterface {
    
    private String goalName = "boulders";

    public boolean isGoalComplete(GameMap game) {
        return true;
    }

    public void add(GoalInterface goal) {
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
}

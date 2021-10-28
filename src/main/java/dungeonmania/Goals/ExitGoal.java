package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public class ExitGoal implements GoalInterface {
    
    private String goalName = "exit";

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

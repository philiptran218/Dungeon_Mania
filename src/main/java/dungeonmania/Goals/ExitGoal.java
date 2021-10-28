package dungeonmania.Goals;

import java.util.List;

import dungeonmania.gamemap.GameMap;

public class ExitGoal implements GoalInterface {
    
    private String goalName = "exit";

    @Override
    public boolean isGoalComplete(GameMap game) {
        return true;
    }

    @Override
    public void add(GoalInterface goal) {
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
    @Override
    public List<GoalInterface> getChildren() {
        return null;
    }
}

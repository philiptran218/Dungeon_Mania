package dungeonmania.Goals;

import java.util.List;

import dungeonmania.gamemap.GameMap;

public class TreasureGoal implements GoalInterface {
    
    private String goalName = "treasure";

    public boolean isGoalComplete(GameMap game) {
        return true;
    }
    public void add(GoalInterface goal) {
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
    @Override
    public List<GoalInterface> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }
}

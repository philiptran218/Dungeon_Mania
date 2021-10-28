package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameMap;
import dungeonmania.util.Position;

public class BouldersGoal implements GoalInterface {
    
    private String goalName = "boulders";
    
    @Override
    public boolean isGoalComplete(GameMap game) {
        
        return false;
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

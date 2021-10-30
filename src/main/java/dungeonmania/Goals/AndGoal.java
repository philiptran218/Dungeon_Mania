package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class AndGoal extends CompositeGoal implements GoalInterface{
    private String goalName = "AND";
    
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        boolean complete = true;
        for (GoalInterface goal : this.getChildren()) {
            complete = complete && goal.isGoalComplete(map);
        }
        return complete;
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }

    

}

package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class OrGoal extends CompositeGoal {
    
    private String goalName = "OR";

    @Override
    public String getGoalName() {
        return this.goalName;
    }

    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        boolean complete = false;
        for (GoalInterface goal : this.getChildren()) {
            complete = complete || goal.isGoalComplete(map);
        }
        return complete;
    }
}

package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class AndGoal extends CompositeGoal {
    private String goalName = "AND";
    
    /**
     * Loops through the AND goal's children and applies the AND comparator to
     * all the children together i.e. child1 && child2 && child3 && ... etc.
     * 
     * @param  map the current state of the map.
     * @return complete - a bollean which is ture if the goal is complete and false if not.
     */
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

package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class OrGoal extends CompositeGoal {
    private String goalName = "OR";

    /**
     * Loops through the AND goal's children and applies the OR comparator to
     * all the children together i.e. child1 && child2 && child3 && ... etc.
     * 
     * @param  map the current state of the map.
     * @return complete - a bollean which is ture if the goal is complete and false if not.
     */
    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        boolean complete = false;
        for (Goal goal : this.getChildren()) {
            complete = complete || goal.isGoalComplete(map);
        }
        return complete;
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
}

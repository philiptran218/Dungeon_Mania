package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.util.Position;

public class ExitGoal implements GoalInterface { 
    private String goalName = "exit";

    /**
     * Loops through the map and checks if the player is on an exit.
     * 
     * @param  map the current state of the map.
     * @return complete - a bollean which is ture if the goal is complete and false if not.
     */
    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {  
                if (entity instanceof Exit 
                    && ((Exit) entity).isUnderPlayer(map)) {
                    return true;
                }
            }
        }
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

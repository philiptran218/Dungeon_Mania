package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.util.Position;

public class BouldersGoal implements Goal {
    private String goalName = "boulders";
    
    /**
     * Loops through the map to check if all floor switches have been activated.
     * 
     * @param  map the current state of the map.
     * @return complete - a bollean which is ture if the goal is complete and false if not.
     */
    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {
                if (entity instanceof FloorSwitch 
                    && !((FloorSwitch) entity).isUnderBoulder(map)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
}

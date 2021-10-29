package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.util.Position;

public class BouldersGoal implements GoalInterface {
    
    private String goalName = "boulders";
    
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

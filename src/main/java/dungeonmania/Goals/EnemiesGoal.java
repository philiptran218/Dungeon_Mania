package dungeonmania.Goals;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

import java.util.Map;

public class EnemiesGoal implements GoalInterface {
    
    private String goalName = "enemies";

    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {
                if (isEnemy(entity)) {
                    return false;
                }
            }
        }
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
        return null;
    }

    private boolean isEnemy(Entity entity) {
        switch (entity.getType()) {
            case "zombie_toast":
                return true;
            case "zombie_toast_spawner":
                return true;
            case "spider":
                return true;
            case "mercenary":
                // Check if mercenary is enemy or ally
                return true;
            default:
                return false;
        }
    }
}

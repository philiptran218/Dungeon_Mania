package dungeonmania.Goals;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Assassin;
import dungeonmania.util.Position;

import java.util.Map;

public class EnemiesGoal implements Goal {
    private String goalName = "enemies";

    /**
     * Loops through the map and checks if all enemies and spawners have been destroyed.
     * 
     * @param  map the current state of the map.
     * @return complete - a bollean which is ture if the goal is complete and false if not.
     */
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
    
    @Override
    public String getGoalName() {
        return this.goalName;
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
                if (((Mercenary) entity).isAlly()) {
                    return false;
                } else {
                    return true;
                }
            case "assassin":
                if (((Assassin) entity).isAlly()) {
                    return false;
                } else {
                    return true;
                }
            case "hydra":
                return true;
            default:
                return false;
        }
    }
}

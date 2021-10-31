package dungeonmania.gamemap;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.util.Position;

public class HardState implements GameState {
    private String mode = "Hard";

   // Spawns the zombie from the zombie toast spawner
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner) {
        if (tickProgress == 14) { 
            // Adds each direction into a list
            List<Position> cardinallyAdjacentPos = zombieSpawner.getCardinallyAdjacentPositions();
            // Checks the surrounding positions for any open spots
            for (Position dir : cardinallyAdjacentPos) {
                List <Entity> entitiesOnPosition = gameMap.get(dir.asLayer(1));
                List <Entity> mobsOnPosition = gameMap.get(dir.asLayer(3));
                // If an open spot is found, a zombie is spawned
                if (entitiesOnPosition.isEmpty() && mobsOnPosition.isEmpty()) {
                    //Entity newZombie = EntityFactory.getEntityObject("" + System.currentTimeMillis(), "zombie_toast", checkOpenPosition.asLayer(3), null, null);
                    ZombieToast newZombie = new ZombieToast("" + System.currentTimeMillis(), "zombie_toast", dir.asLayer(3));
                    gameMap.get(dir.asLayer(3)).add(newZombie);
                    break;
                } 
            }
            // Increments the zombie id and resets the tick progress
            tickProgress = 0;
        }
        // Increments the ticks
        else {
            tickProgress++;
        }
        return tickProgress;
    }

    @Override
    public String getMode() {
        return mode;
    }
}

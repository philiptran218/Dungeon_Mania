package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HardState implements GameState {
    // Spawns the zombie from the zombie toast spawner
    public void spawnZombie(int tickProgress, Map<Position, List<Entity>> listOfEntities, Position zombieSpawner, int zombieId) {
        if (tickProgress == 14) {  
            // Adds each direction into a list          
            List <Direction> directions = new ArrayList<Direction>();
            directions.add(Direction.UP);
            directions.add(Direction.RIGHT);
            directions.add(Direction.DOWN);
            directions.add(Direction.LEFT);
            // Checks the surrounding positions for any open spots
            for (Direction dir : directions) {
                Position checkOpenPosition = zombieSpawner.translateBy(dir);
                List <Entity> entitiesOnPosition = listOfEntities.get(checkOpenPosition);
                // If an open spot is found, a zombie is spawned
                if (entitiesOnPosition.get(3) == null || entitiesOnPosition.get(1) == null ||
                    entitiesOnPosition.get(1) instanceof Exit || entitiesOnPosition.get(1) instanceof Portal) {
                    ZombieToast newZombie = new ZombieToast("Zombie" + zombieId, "zombie_toast", checkOpenPosition);
                    entitiesOnPosition.add(newZombie);
                    break;
                }
                // Zombie is spawned if the door is unlocked
                else if (entitiesOnPosition.get(1) instanceof Door) {
                    Door checkLocked = (Door) entitiesOnPosition.get(0);
                    if (checkLocked.isLocked() == false) {
                        Entity newZombie = new ZombieToast("Zombie" + zombieId, "zombie_toast", checkOpenPosition);
                        entitiesOnPosition.add(newZombie);
                        break;
                    }
                }
            }
            // Increments the zombie id and resets the tick progress
            zombieId++;
            tickProgress = 1;
        }
        // Increments the ticks
        else {
            tickProgress++;
        }
    }
}

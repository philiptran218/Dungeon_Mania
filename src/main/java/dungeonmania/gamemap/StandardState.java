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

public class StandardState implements GameState{
    private String mode = "Standard";
     // Spawns the zombie from the zombie toast spawner
     public void spawnZombie(int tickProgress, Map<Position, List<Entity>> listOfEntities, Position zombieSpawner) {
        if (tickProgress == 19) {
            // Adds each direction into a list
            List <Direction> directions = new ArrayList<Direction>();
            directions.add(Direction.UP);
            directions.add(Direction.RIGHT);
            directions.add(Direction.DOWN);
            directions.add(Direction.LEFT);
            // Checks surrounding positions for any open spots
            for (Direction dir : directions) {
                Position checkOpenPosition = zombieSpawner.translateBy(dir);
                List <Entity> entitiesOnPosition = listOfEntities.get(checkOpenPosition);
                // Spawns the zombie if there are no entities in the way
                if (entitiesOnPosition.get(3) == null || entitiesOnPosition.get(1) == null ||
                    entitiesOnPosition.get(1) instanceof Exit || entitiesOnPosition.get(1) instanceof Portal) {
                    ZombieToast newZombie = new ZombieToast("" + System.currentTimeMillis(), "zombie_toast", checkOpenPosition);
                    entitiesOnPosition.add(newZombie);
                    break;
                }
                // Spawns the zombie if there is a door that is unlocked
                else if (entitiesOnPosition.get(1) instanceof Door) {
                    Door checkLocked = (Door) entitiesOnPosition.get(0);
                    if (checkLocked.isLocked() == false) {
                        Entity newZombie = new ZombieToast("" + System.currentTimeMillis(), "zombie_toast", checkOpenPosition);
                        entitiesOnPosition.add(newZombie);
                        break;
                    }
                }
            }
            // Resets the tick progress and increments the zombie id
            tickProgress = 1;
        }
        // Increments the zombie id
        else {
            tickProgress++;
        }
    }

    @Override
    public String getMode() {
        return mode;
    }

}

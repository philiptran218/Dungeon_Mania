package dungeonmania.gamemap;

import java.util.List;
import java.util.Map;

import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;

public class HardState implements GameState {
    private String mode = "hard";
    private static final double PLAYER_MAX_HEALTH = 15;
    
    // Spawns the zombie from the zombie toast spawner
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner, List<AnimationQueue> animations) {
        if (tickProgress == 14) {
            Player player = null;
            for (List<Entity> entities: gameMap.values()) {
                for (Entity entity: entities) {
                    if (entity.getType().equals("player")) {
                        player = (Player) entity;
                    }
                }
            }

            // Adds each direction into a list
            List<Position> cardinallyAdjacentPos = zombieSpawner.getCardinallyAdjacentPositions();
            // Checks the surrounding positions for any open spots
            for (Position dir : cardinallyAdjacentPos) {
                List <Entity> entitiesOnPosition = gameMap.get(dir.asLayer(1));
                List <Entity> mobsOnPosition = gameMap.get(dir.asLayer(3));
                // If an open spot is found, a zombie is spawned
                if (entitiesOnPosition.isEmpty() && mobsOnPosition.isEmpty()) {
                    ZombieToast newZombie = new ZombieToast("Zombie" + System.currentTimeMillis(), "zombie_toast", dir.asLayer(3));
                    gameMap.get(dir.asLayer(3)).add(newZombie);
                    AnimationUtility.setMovingEntityHealthBar(animations, newZombie);
                    player.registerObserver(newZombie);
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

    @Override
    public double getPlayerMaxHealth() {
        return PLAYER_MAX_HEALTH;
    }
}

package dungeonmania.gamemap;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface GameState {
    // Different method of combat

    // Mob Spawning
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner);
    // Player stats
    
    // Get gamemode as string:
    public String getMode();
        
}

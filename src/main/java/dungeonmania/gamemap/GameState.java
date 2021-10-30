package dungeonmania.gamemap;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface GameState {
    // Different method of combat

    // Mob Spawning
    public void spawnZombie(int tickProgress, Map<Position, List<Entity>> listOfEntities, Position zombieSpawner, int zombieId);
    // Player stats
    
    // Get gamemode as string:
    public String getMode();
        
}

package dungeonmania.gamemap;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface GameState {
    // Different method of combat

    // Mob Spawning
    public int spawnZombie(int tickProgress, HashMap<Position, List<Entity>> listOfEntities, Position zombieSpawner);
    // Player stats
    
        
}

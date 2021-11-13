package dungeonmania.gamemap;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;

public interface GameState {
    // Different method of combat

    // Mob Spawning
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner, List<AnimationQueue> animations);
    // Player stats
    
    // Get gamemode as string:
    public String getMode();

    public double getPlayerMaxHealth();
        
}

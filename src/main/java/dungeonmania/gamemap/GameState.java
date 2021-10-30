package dungeonmania.gamemap;

public interface GameState {
    // Different method of combat

    // Mob Spawning
    public int spawnZombie(int tickProgress);
    // Player stats
    
    // Get gamemode as string:
    public String getMode();
        
}

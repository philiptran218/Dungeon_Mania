package dungeonmania.gamemap;

import java.util.Map;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class PeacefulState implements GameState{
    private String mode = "peaceful";
    // Spawns the zombie from the zombie toast spawner
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner) {
        // Does not spawn a zombie in peaceful mode
        return 0;
    }

    @Override
    public String getMode() {
        return mode;
    }
}

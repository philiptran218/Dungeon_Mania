package dungeonmania.gamemap;

import java.util.Map;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class PeacefulState implements GameState{
    private String mode = "Peaceful";
    public int spawnZombie(int tickProgress, Map<Position, List<Entity>> gameMap, Position zombieSpawner) {
        return 0;
    }

    @Override
    public String getMode() {
        return mode;
    }
}

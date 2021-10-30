package dungeonmania.gamemap;

import java.util.Map;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class PeacefulState implements GameState{
    private String mode = "Peaceful";
    public void spawnZombie(int tickProgress, Map<Position, List<Entity>> listOfEntities, Position zombieSpawner) {
        return;
    }

    @Override
    public String getMode() {
        return mode;
    }
}

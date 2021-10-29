package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.gamemap.GameState;
import dungeonmania.gamemap.HardState;
import dungeonmania.gamemap.PeacefulState;
import dungeonmania.gamemap.StandardState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    private int zombieId = 0;
    GameState state;
    GameState hardState;
    GameState standardState;
    GameState peacefulState;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
        hardState = new HardState();
        standardState = new StandardState();
        peacefulState = new PeacefulState();
    }
    /**
     * Getter for tickProgress
     * @return the progress of the tick
     */
    public int getTickProgress() {
        return tickProgress;
    }
    /**
     * Setter for tickProgress
     * @param tickProgress
     */
    public void setTickProgress(int tickProgress) {
        this.tickProgress = tickProgress;
    }
    /**
     * Spawns the zombie in 15 or 20 ticks depending on the game mode
     * @param zombieSpawner
     * @param listOfEntities
     */
    public void tick(Position zombieSpawner, HashMap<Position, List<Entity>> listOfEntities) {
        state.spawnZombie(tickProgress, listOfEntities, zombieSpawner, zombieId);
    }
    /**
     * Checks if the interaction between the player and the zombie toast spawner is valid
     * @param entityPosition
     * @param gameMap
     * @param isAdjacent
     * @param playerEntity
     * @return the entity of the player
     */
    public Player canSpawnerBeDestroyed(Position entityPosition, Map<Position, List<Entity>> gameMap, boolean isAdjacent, Player playerEntity) {
        // Add all the directions into a list
        List <Direction> directions = new ArrayList<Direction>();
        directions.add(Direction.UP);
        directions.add(Direction.RIGHT);
        directions.add(Direction.DOWN);
        directions.add(Direction.LEFT);
        for (Direction dir : directions) {
            // Checks the surrounding positions
            Position checkPosition = entityPosition.translateBy(dir);
            Entity entityOnPosition = gameMap.get(checkPosition).get(3);
            // Checks if the player is in the surrounding positions and gets the player if true
            if (entityOnPosition != null && entityOnPosition instanceof Player) {
                isAdjacent = true;
                playerEntity = (Player) entityOnPosition;
            }
        }
        return playerEntity;
    }
}

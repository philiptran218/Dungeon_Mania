package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.gamemap.GameState;
import dungeonmania.gamemap.HardState;
import dungeonmania.gamemap.StandardState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    GameState state;
    GameState hardState;
    GameState standardState;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
        hardState = new HardState();
        standardState = new StandardState();
    }
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
     */
    public void tick() {
        tickProgress = state.spawnZombie(tickProgress);
    }

    public void destroy(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        map.get(new Position(pos.getX(), pos.getY(), 1)).clear();
    }

}

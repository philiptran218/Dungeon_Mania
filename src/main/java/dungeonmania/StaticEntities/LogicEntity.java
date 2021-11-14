package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface LogicEntity {

    /**
     * Recursively checks to see if a logic entity is on/true based on its inputs and logic.
     * 
     * @param map - a map containing all the entities and their respective positions
     * @param visitedIDs - a list of strings storing the ids of entities which have already been visited in the recursion
     * @return a boolean representing the logic status of the entity
     */
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs);
}

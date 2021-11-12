package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface LogicGate {
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs);
}

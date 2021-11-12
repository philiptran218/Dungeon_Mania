package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements LogicGate {

    private String logic = "none";

    public LightBulb(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }

    public LightBulb(String id, String type, Position pos) {
        super(id, type, pos);
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs) {
        return false;
    }

}

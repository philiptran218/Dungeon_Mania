package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity implements LogicGate{

    private String logic;

    public SwitchDoor(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }

    public SwitchDoor(String id, String type, Position pos) {
        super(id, type, pos);
        this.logic = "none";
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map) {
        return false;
    }
    
}

package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements LogicGate{

    public Wire(String id, String type, Position pos) {
        super(id, type, pos);
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map) {
        //TODO: Recursively look to see if any adajacent wires are active.
        return false;
    }
    
}
